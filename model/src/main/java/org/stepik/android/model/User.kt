package org.stepik.android.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class User(
        val id: Long = 0,
        val profile: Long = 0,
        @SerializedName("full_name") val fullName: String? = null,
        val details: String? = null,
        @SerializedName("short_bio") val shortBio: String? = null,
        val avatar: String? = null,

        @SerializedName("is_private") val isPrivate: Boolean = false,
        @SerializedName("join_date") val joinDate: Date?
)