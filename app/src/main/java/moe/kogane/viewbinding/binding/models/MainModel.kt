package moe.kogane.viewbinding.binding.models

import moe.kogane.viewbinding.models.Education
import moe.kogane.viewbinding.models.Gender

class MainModel {
    public var name = ""
    public var email = ""
    public var password = ""
    public var passwordConfirm = ""
    public var gender: Gender? = null
    public var education: Education? = null
    public var agree = false
}