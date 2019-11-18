package moe.kogane.viewbinding.binding.binders.views

class GenericCheckBoxBinder : CheckBoxBinder<Boolean>() {
    override fun format(value: Boolean): Boolean {
        return value
    }

    override fun parse(isChecked: Boolean): Boolean {
        return isChecked
    }
}