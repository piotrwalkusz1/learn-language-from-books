package com.piotrwalkusz.learnlanguagefrombooks.core.filedecoder

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.InputStream
import java.io.Reader

class PdfFileDecoder : FileDecoder {

    override fun read(source: InputStream): Reader {
        val document: PDDocument = PDDocument.load(source)

        return PDFTextStripper().getText(document).reader()
    }
}