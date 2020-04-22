package com.piotrwalkusz.learnlanguagefrombooks.core.wordexporter

import java.io.Reader

interface WordExporter {

    fun export(wordsAndTranslations: Map<String, String>): Reader
}