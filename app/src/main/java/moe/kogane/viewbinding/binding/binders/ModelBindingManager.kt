package moe.kogane.viewbinding.binding.binders

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import moe.kogane.viewbinding.binding.binders.views.GenericCheckBoxBinder
import moe.kogane.viewbinding.binding.binders.views.GenericTextViewBinder
import moe.kogane.viewbinding.binding.binders.views.ViewBinder
import java.lang.reflect.Field
import java.util.ArrayList

class ModelBindingManager<T : Any>(val reactiveBinder: ReactiveViewBinder) {
    val subject = BehaviorSubject.create<T>()

    private val mSubjectMap = HashMap<String, Subject<*>>()
    private val mDisposableMap = HashMap<String, Disposable>()
    private val mDisposables = HashSet<Disposable>()

    fun registerModel(model: T) {
        model.javaClass.declaredFields.forEach {
            if (mSubjectMap.containsKey(it.name)) {
                updateSubject(model, it, mSubjectMap[it.name]!!)
            } else {
                mSubjectMap.put(it.name, createSubject(model, it))
            }
        }
    }

    fun unregister() {
        mDisposableMap.keys.forEach {
            mDisposableMap[it]?.dispose()
        }
        val disposables = ArrayList<Disposable>()
        mDisposables.forEach {
            it.dispose()
            disposables.add(it)
        }
        mDisposables.removeAll(disposables)
    }

    fun bindTextView(propertyName: String, textView: TextView) {
        bind(propertyName, textView, GenericTextViewBinder::class.java)
    }

    fun bindCheckBox(propertyName: String, checkBox: CheckBox) {
        bind(propertyName, checkBox, GenericCheckBoxBinder::class.java)
    }

    fun <V : View, T> bind(
        propertyName: String,
        view: V,
        binderClass: Class<out ViewBinder<V, T>>
    ) {
        mDisposables.add(reactiveBinder.bind(view, binderClass, getSubject(propertyName)))
    }

    fun <V : View, T> bind(propertyName: String, view: V, binder: ViewBinder<V, T>) {
        mDisposables.add(binder.bind(view, getSubject(propertyName)))
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getSubject(propertyName: String): Subject<T> {
        val subject = mSubjectMap[propertyName] as Subject<T>?
        if (subject != null) {
            return subject
        }
        throw IllegalArgumentException("No subject was created for property ${propertyName}")
    }

    private fun createSubject(model: T, field: Field): Subject<*> {
        val subject = BehaviorSubject.create<Any>()
        updateSubject(model, field, subject)
        return subject
    }

    @Suppress("UNCHECKED_CAST")
    private fun updateSubject(model: T, field: Field, subject: Subject<*>) {
        if (mDisposableMap.containsKey(field.name)) {
            mDisposableMap.remove(field.name)!!.dispose()
        }
        field.isAccessible = true
        val value: Any? = field.get(model)
        value?.let {
            (subject as Subject<Any>).onNext(value)
        }
        mDisposableMap.put(field.name, subject.subscribe { t: Any ->
            field.set(model, t)
            this.subject.onNext(model)
        })
    }
}