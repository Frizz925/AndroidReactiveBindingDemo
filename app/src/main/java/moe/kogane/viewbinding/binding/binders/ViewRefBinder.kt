package moe.kogane.viewbinding.binding.binders

import android.view.View
import moe.kogane.viewbinding.binding.annotations.BindViewRef

class ViewRefBinder(val root: View) {
    fun <T : Any> bind(viewClass: Class<T>): T {
        val result = viewClass.newInstance()
        viewClass.declaredFields.forEach {
            it.getAnnotation(BindViewRef::class.java)?.apply {
                it.set(result, root.findViewById(viewId))
            }
        }
        return result
    }
}