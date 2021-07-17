package com.mutsanna.moviecatalogue.utils

import java.io.File

class ResponseFileReader(path: String) {
    val content: String

    init {
        val testFolderResources = File(File("").absolutePath, "src/test/assets")

        val file = File(testFolderResources.absolutePath, path)
        content = String(file.readBytes())
    }
}