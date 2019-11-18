package moe.kogane.viewbinding.binding.binders.views

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.Subject

abstract class TextViewBinder<T> :
    ViewBinder<TextView, T> {
    override fun bind(view: TextView, subject: Subject<T>): Disposable {
        view.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                subject.onNext(parse(s.toString()))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                subject.onNext(parse(s.toString()))
            }
        })
        return subject.subscribe { t: T ->
            val text = format(t)
            if (!view.text.toString().equals(text)) {
                view.text = text
            }
        }
    }

    protected abstract fun format(value: T): String

    protected abstract fun parse(text: String): T
}