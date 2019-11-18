package moe.kogane.viewbinding.binding.binders.views

import android.widget.CheckBox
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.Subject

abstract class CheckBoxBinder<T> : ViewBinder<CheckBox, T> {
    override fun bind(view: CheckBox, subject: Subject<T>): Disposable {
        view.setOnCheckedChangeListener { _, isChecked ->
            subject.onNext(parse(isChecked))
        }
        return subject.subscribe { t: T ->
            val isChecked = format(t)
            if (view.isChecked != isChecked) {
                view.isChecked = isChecked
            }
        }
    }

    protected abstract fun format(value: T): Boolean

    protected abstract fun parse(isChecked: Boolean): T
}