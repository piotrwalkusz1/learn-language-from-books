package com.piotrwalkusz.learnlanguagefrombooks.core.filedecoder

import java.io.InputStream
import java.io.Reader

interface FileDecoder {

    fun read(source: InputStream): Reader
}