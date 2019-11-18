package moe.kogane.viewbinding.binding.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class BindViewRef(val viewId: Int)