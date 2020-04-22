package com.piotrwalkusz.learnlanguagefrombooks.core.common

enum class Language {

    ENGLISH,
    POLISH;

    companion object {

        fun valueOfIgnoreCase(value: String): Language? {
            return values().find { it.name.toLowerCase() == value.toLowerCase() }
        }
    }
}