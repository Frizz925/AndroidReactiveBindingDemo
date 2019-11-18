package moe.kogane.viewbinding.binding.binders.views

class GenericTextViewBinder : TextViewBinder<String>() {
    override fun format(value: String): String {
        return value
    }

    override fun parse(text: String): String {
        return text
    }
}