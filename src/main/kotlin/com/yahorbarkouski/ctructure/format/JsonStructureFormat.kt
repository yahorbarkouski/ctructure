package com.yahorbarkouski.ctructure.format

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.intellij.json.JsonFileType
import com.intellij.psi.PsiDirectory

/**
 * A StructureFormat implementation for generating JSON structure from a given PsiDirectory.
 *
 * @param fileType The FileType for JSON files.
 */
class JsonStructureFormat : StructureFormat(JsonFileType.INSTANCE) {

    override val title: String
        get() = "JSON"


    override fun generate(directory: PsiDirectory, indent: String): String {
        val jsonStructure = getJsonStructure(directory)
        val mapper = ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
        return mapper.writeValueAsString(jsonStructure)
    }

    /**
     * Returns a JSON structure representing the directory hierarchy of the input PsiDirectory.
     *
     * @param directory the PsiDirectory to be parsed
     * @return a Map<String, Any> object representing the directory hierarchy in JSON format. The structure may include
     *         "files" key, containing a list of file names within the directory, and/or a "directories" key, consisting of
     *         a Map of subdirectories within the directory, recursively parsed using this function.
     */
    private fun getJsonStructure(directory: PsiDirectory): Map<String, Any> {
        val structure = mutableMapOf<String, Any>()

        val files = directory.files.map { it.name }
        if (files.isNotEmpty()) {
            structure["files"] = files
        }

        val subDirectories = directory.subdirectories
            .filter { it.files.isNotEmpty() || it.subdirectories.isNotEmpty() }
            .associateBy({ it.name }, { getJsonStructure(it) })

        if (subDirectories.isNotEmpty()) {
            structure["directories"] = subDirectories
        }

        return structure
    }
}