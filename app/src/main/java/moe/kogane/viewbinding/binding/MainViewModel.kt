package moe.kogane.viewbinding.binding

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.edit
import io.reactivex.disposables.Disposable
import moe.kogane.viewbinding.binding.binders.ModelBinderFactory
import moe.kogane.viewbinding.binding.binders.ViewBinderFactory
import moe.kogane.viewbinding.binding.binders.ViewRefBinder
import moe.kogane.viewbinding.binding.binders.views.RadioButtonBinder
import moe.kogane.viewbinding.binding.binders.views.SpinnerBinder
import moe.kogane.viewbinding.binding.models.MainModel
import moe.kogane.viewbinding.binding.views.MainView
import moe.kogane.viewbinding.models.Education
import moe.kogane.viewbinding.models.Gender

class MainViewModel(val activity: Activity) {
    var model = MainModel()

    private val mViewBinderFactory = ViewBinderFactory()
    private val mModelBinderFactory = ModelBinderFactory<MainModel>(mViewBinderFactory)

    private lateinit var mRoot: View
    private lateinit var mView: MainView

    private lateinit var mOutputDisposable: Disposable

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
        mModelBinderFactory.run {
            registerModel(model)

            bindTextView("name", mView.name)
            bindTextView("email", mView.email)
            bindTextView("password", mView.password)
            bindTextView("passwordConfirm", mView.passwordConfirm)
            bindCheckBox("agree", mView.agree)

            bind("gender", mView.genderMale, mGenderBinder)
            bind("gender", mView.genderFemale, mGenderBinder)
            bind("education", mView.education, mEducationBinder)

            mOutputDisposable = subject.subscribe {
                val agree = if (it.agree) "Yes" else "No"
                StringBuilder().run {
                    append("Name: ${it.name}\n")
                    append("E-mail: ${it.email}\n")
                    append("Gender: ${it.gender?.text}\n")
                    append("Education: ${it.education?.text}\n")
                    append("Agree: ${agree}\n")
                    mView.output.text = toString()
                }
            }

            getSubject<Boolean>("agree").subscribe { t: Boolean ->
                mView.submit.isEnabled = t
            }

            mView.submit.setOnClickListener {
                savePreferences(model)
                Toast.makeText(activity, "Registration data saved", Toast.LENGTH_LONG).show()
            }
            mView.reset.setOnClickListener {
                model = MainModel()
                mModelBinderFactory.registerModel(model)
                savePreferences(model)
                Toast.makeText(activity, "Registration data reset", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun unregister() {
        mModelBinderFactory.unregister()
        mOutputDisposable.dispose()
    }

    private fun init(root: View) {
        mRoot = root
        mView = ViewRefBinder(root).bind(MainView::class.java)
    }

    private fun loadPreferences(model: MainModel) {
        val prefs = activity.getPreferences(Context.MODE_PRIVATE)
        model.name = prefs.getString("name", model.name)!!
        model.email = prefs.getString("email", model.email)!!
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
            putString("gender", model.gender?.name)
            putString("education", model.education?.name)
            putBoolean("agree", model.agree)
            commit()
        }
    }
}