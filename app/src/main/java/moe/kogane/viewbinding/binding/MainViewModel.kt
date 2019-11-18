package moe.kogane.viewbinding.binding

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.edit
import io.reactivex.disposables.Disposable
import moe.kogane.viewbinding.R
import moe.kogane.viewbinding.binding.binders.ModelBindingManager
import moe.kogane.viewbinding.binding.binders.ReactiveViewBinder
import moe.kogane.viewbinding.binding.binders.ViewRefBinder
import moe.kogane.viewbinding.binding.binders.views.RadioButtonBinder
import moe.kogane.viewbinding.binding.binders.views.SpinnerBinder
import moe.kogane.viewbinding.binding.models.MainModel
import moe.kogane.viewbinding.binding.views.MainView
import moe.kogane.viewbinding.models.Education
import moe.kogane.viewbinding.models.Gender

class MainViewModel(val activity: Activity) {
    var model = MainModel()

    private val mReactiveViewBinder = ReactiveViewBinder()
    private val mModelBindingManager = ModelBindingManager<MainModel>(mReactiveViewBinder)

    private lateinit var mRoot: View
    private lateinit var mView: MainView

    private lateinit var mDisposable: Disposable

    private val mEducationAdapter = ArrayAdapter(
        activity,
        android.R.layout.simple_dropdown_item_1line,
        Education.values().map { it.text }
    )

    private val mGenderBinder = object : RadioButtonBinder<Gender>() {
        override fun format(value: Gender): Int {
            return when (value) {
                Gender.MALE -> mView.genderMale.id
                Gender.FEMALE -> mView.genderFemale.id
            }
        }

        override fun parse(viewId: Int): Gender {
            return when (viewId) {
                mView.genderMale.id -> Gender.MALE
                mView.genderFemale.id -> Gender.FEMALE
                else -> Gender.MALE // HACK: Return Male for now until we implement optionals
            }
        }
    }

    private val mEducationBinder = object : SpinnerBinder<Education>() {
        override fun format(value: Education): Int {
            return value.ordinal
        }

        override fun parse(position: Int): Education {
            return Education.values()[position]
        }
    }

    fun register(root: View) {
        init(root)
        loadPreferences(model)

        mView.education.adapter = mEducationAdapter
        mModelBindingManager.run {
            registerModel(model)

            bindTextView("name", mView.name)
            bindTextView("email", mView.email)
            bindTextView("password", mView.password)
            bindTextView("passwordConfirm", mView.passwordConfirm)
            bindCheckBox("agree", mView.agree)

            bind("gender", mView.genderMale, mGenderBinder)
            bind("gender", mView.genderFemale, mGenderBinder)
            bind("education", mView.education, mEducationBinder)

            mDisposable = subject.subscribe {
                mView.updateOutput(it)
                validate(it)
            }

            mView.submit.setOnClickListener {
                savePreferences(model)
                Toast.makeText(activity, R.string.ui_toast_saved, Toast.LENGTH_LONG).show()
            }
            mView.reset.setOnClickListener {
                model = MainModel()
                mModelBindingManager.registerModel(model)
                savePreferences(model)
                Toast.makeText(activity, R.string.ui_toast_reset, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun unregister() {
        mModelBindingManager.unregister()
        mDisposable.dispose()
    }

    private fun init(root: View) {
        mRoot = root
        mView = ViewRefBinder(root).bind(MainView::class.java)
    }

    private fun validate(model: MainModel) {
        var hasError = false
        if (model.name.isEmpty()) {
            mView.name.error = activity.getString(R.string.ui_error_name_empty)
            hasError = true
        } else {
            mView.name.error = null
        }

        if (model.email.isEmpty()) {
            mView.email.error = activity.getString(R.string.ui_error_email_empty)
            hasError = true
        } else if (!validateEmail(model.email)) {
            mView.email.error = activity.getString(R.string.ui_error_email_invalid)
            hasError = true
        } else {
            mView.email.error = null
        }

        if (model.password.isEmpty()) {
            mView.password.error = activity.getString(R.string.ui_error_password_empty)
            hasError = true
        } else {
            mView.password.error = null
        }

        if (!model.password.equals(model.passwordConfirm)) {
            mView.passwordConfirm.error = activity.getString(R.string.ui_error_password_confirm)
            hasError = true
        } else {
            mView.passwordConfirm.error = null
        }

        if (hasError) {
            mView.submit.isEnabled = false
        } else {
            mView.submit.isEnabled = model.agree
        }
    }

    private fun validateEmail(email: String): Boolean {
        return true // TODO: I'm lazy
    }

    private fun loadPreferences(model: MainModel) {
        val prefs = activity.getPreferences(Context.MODE_PRIVATE)
        model.name = prefs.getString("name", model.name)!!
        model.email = prefs.getString("email", model.email)!!
        model.password = prefs.getString("password", model.password)!!
        model.passwordConfirm = prefs.getString("passwordConfirm", model.passwordConfirm)!!
        model.gender = Gender.valueOf(
            prefs.getString("gender", Gender.MALE.name)!!
        )
        model.education = Education.valueOf(
            prefs.getString("education", Education.UNDERGRADUATE.name)!!
        )
        model.agree = prefs.getBoolean("agree", model.agree)
    }

    private fun savePreferences(model: MainModel) {
        activity.getPreferences(Context.MODE_PRIVATE).edit {
            putString("name", model.name)
            putString("email", model.email)
            putString("password", model.password)
            putString("passwordConfirm", model.passwordConfirm)
            putString("gender", model.gender.name)
            putString("education", model.education.name)
            putBoolean("agree", model.agree)
            commit()
        }
    }
}