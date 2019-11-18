package moe.kogane.viewbinding.binding.views

import android.widget.*
import moe.kogane.viewbinding.R
import moe.kogane.viewbinding.binding.annotations.BindViewRef
import moe.kogane.viewbinding.binding.models.MainModel

class MainView {
    @BindViewRef(R.id.txtName)
    lateinit var name: TextView

    @BindViewRef(R.id.txtEmail)
    lateinit var email: TextView

    @BindViewRef(R.id.txtPassword)
    lateinit var password: TextView

    @BindViewRef(R.id.txtPasswordConfirm)
    lateinit var passwordConfirm: TextView

    @BindViewRef(R.id.rdbGenderMale)
    lateinit var genderMale: RadioButton

    @BindViewRef(R.id.rdbGenderFemale)
    lateinit var genderFemale: RadioButton

    @BindViewRef(R.id.spnEducation)
    lateinit var education: Spinner

    @BindViewRef(R.id.chkAgree)
    lateinit var agree: CheckBox

    @BindViewRef(R.id.btnSubmit)
    lateinit var submit: Button

    @BindViewRef(R.id.btnReset)
    lateinit var reset: Button

    @BindViewRef(R.id.txtOutput)
    lateinit var output: TextView

    fun updateOutput(model: MainModel) {
        val agree = if (model.agree) "Yes" else "No"
        StringBuilder().run {
            append("Name: ${model.name}\n")
            append("E-mail: ${model.email}\n")
            append("Gender: ${model.gender?.text}\n")
            append("Education: ${model.education?.text}\n")
            append("Agree: ${agree}\n")
            output.text = toString()
        }
    }
}
