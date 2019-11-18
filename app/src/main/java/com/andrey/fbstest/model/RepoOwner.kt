package com.andrey.fbstest.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RepoOwner(
    var login: String = "",
    var avatar_url: String = ""
): Parcelable {
    override fun toString(): String {
        return login
    }
}