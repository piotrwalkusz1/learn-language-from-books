package com.piotrwalkusz.learnlanguagefrombooks.core.wordexporter

import java.io.Reader

class CsvWordExporter : WordExporter {

    override fun export(wordsAndTranslations: Map<String, String>): Reader {
        return wordsAndTranslations
                .map { "${it.key};${it.value}" }
                .joinToString("\n")
                .reader()
    }
}
