package model

import java.time.LocalDateTime
import java.util.Date

data class New(
    val date: String,
    val description: String,
    val id: Int,
    val keywords: List<String>,
    val title: String,
    val visible: Boolean,
    var dateLikeDateType: LocalDateTime
)