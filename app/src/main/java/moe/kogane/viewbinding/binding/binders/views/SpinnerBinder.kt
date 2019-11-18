package moe.kogane.viewbinding.binding.binders.views

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.Subject

abstract class SpinnerBinder<T> : ViewBinder<Spinner, T> {
    override fun bind(view: Spinner, subject: Subject<T>): Disposable {
        view.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                subject.onNext(parse(position))
            }
        }
        return subject.subscribe { t: T ->
            val selection = format(t)
            if (view.selectedItemPosition != selection) {
                view.setSelection(selection)
            }
        }
    }

    protected abstract fun format(value: T): Int

    protected abstract fun parse(position: Int): T
}