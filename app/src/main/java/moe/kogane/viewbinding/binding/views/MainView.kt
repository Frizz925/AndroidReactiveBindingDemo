package moe.kogane.viewbinding.binding.views

import android.widget.*
import moe.kogane.viewbinding.R
import moe.kogane.viewbinding.binding.annotations.BindView

class MainView {
    @BindView(R.id.txtName)
    lateinit var name: TextView

    @BindView(R.id.txtEmail)
    lateinit var email: TextView

    @BindView(R.id.txtPassword)
    lateinit var password: TextView

    @BindView(R.id.txtPasswordConfirm)
    lateinit var passwordConfirm: TextView

    @BindView(R.id.rdbGenderMale)
    lateinit var genderMale: RadioButton

    @BindView(R.id.rdbGenderFemale)
    lateinit var genderFemale: RadioButton

    @BindView(R.id.spnEducation)
    lateinit var education: Spinner

    @BindView(R.id.chkAgree)
    lateinit var agree: CheckBox

    @BindView(R.id.btnSubmit)
    lateinit var submit: Button

    @BindView(R.id.btnReset)
    lateinit var reset: Button

    @BindView(R.id.txtOutput)
    lateinit var output: TextView
}
