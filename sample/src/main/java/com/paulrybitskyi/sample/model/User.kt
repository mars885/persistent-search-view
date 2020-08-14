package com.paulrybitskyi.sample.model

import java.io.Serializable

internal data class User(
    val id: Int,
    val username: String,
    val fullName: String,
    val profileImageId: Int,
    val firstState: Boolean,
    val secondState: Boolean
) : Serializable