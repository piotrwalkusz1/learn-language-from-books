package com.piotrwalkusz.learnlanguagefrombooks.core.filedecoder

import java.io.InputStream
import java.io.Reader

class PlainTextFileDecoder : FileDecoder {

    override fun read(source: InputStream): Reader {
        return source.bufferedReader()
    }
}