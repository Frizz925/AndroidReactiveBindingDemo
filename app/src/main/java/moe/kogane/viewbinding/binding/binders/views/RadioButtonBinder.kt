package moe.kogane.viewbinding.binding.binders.views

import android.widget.RadioButton
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.Subject

abstract class RadioButtonBinder<T> : ViewBinder<RadioButton, T> {
    override fun bind(view: RadioButton, subject: Subject<T>): Disposable {
        view.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                subject.onNext(parse(buttonView.id))
            }
        }
        return subject.subscribe { t: T ->
            view.isChecked = view.id == format(t)
        }
    }

    protected abstract fun format(value: T): Int

    protected abstract fun parse(viewId: Int): T
}