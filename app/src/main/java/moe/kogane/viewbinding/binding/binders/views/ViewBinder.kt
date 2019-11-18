package moe.kogane.viewbinding.binding.binders.views

import android.view.View
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.Subject

interface ViewBinder<V : View, T> {
    fun bind(view: V, subject: Subject<T>): Disposable
}