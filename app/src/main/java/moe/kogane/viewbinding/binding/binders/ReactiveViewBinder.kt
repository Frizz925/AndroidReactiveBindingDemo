package moe.kogane.viewbinding.binding.binders

import android.view.View
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.Subject
import moe.kogane.viewbinding.binding.binders.views.ViewBinder

class ReactiveViewBinder {
    private val mBinderCache = HashMap<Class<*>, ViewBinder<out View, *>>()

    @Suppress("UNCHECKED_CAST")
    fun <V : View, T> bind(
        view: V,
        binderClass: Class<out ViewBinder<V, T>>,
        subject: Subject<T>
    ): Disposable {
        var binder = mBinderCache[binderClass] as ViewBinder<V, T>?
        if (binder == null) {
            binder = binderClass.newInstance()
            mBinderCache.put(binderClass, binder)
        }
        return binder.bind(view, subject)
    }
}