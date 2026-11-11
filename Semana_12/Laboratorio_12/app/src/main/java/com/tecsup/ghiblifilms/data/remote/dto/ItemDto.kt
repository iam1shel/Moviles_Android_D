package com.tecsup.ghiblifilms.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    val id: String,
    val title: String,
    @SerialName("original_title") val originalTitle: String? = null,
    @SerialName("original_title_romanised") val originalTitleRomanised: String? = null,
    val image: String? = null,
    @SerialName("movie_banner") val movieBanner: String? = null,
    val description: String? = null,
    val director: String = "",
    val producer: String? = null,
    @SerialName("release_date") val releaseDate: String = "",
    @SerialName("running_time") val runningTime: String? = null,
    @SerialName("rt_score") val rtScore: String? = null
)
