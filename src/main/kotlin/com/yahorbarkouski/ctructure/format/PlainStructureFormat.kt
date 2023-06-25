package com.yahorbarkouski.ctructure.format

import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.psi.PsiDirectory

/**
 * A StructureFormat implementation that generates a plain text format structure
 *
 * @param fileType The FileType for Plain files.
 */
class PlainStructureFormat : StructureFormat(PlainTextFileType.INSTANCE) {

    override val title: String
        get() = "Plain"

    /**
     * Generates plain string representation of the structure of a given directory and its subdirectories recursively.
     *
     * @param directory the directory to generate the structure representation for
     * @param indent the indentation string to use for each level of recursion
     * @return a string representation of the structure of the given directory and its subdirectories in plain format
     */
    override fun generate(directory: PsiDirectory, indent: String): String {
        val builder = StringBuilder()
        builder.append(indent).append("- ").append(directory.name).append("\n")

        for (file in directory.files) {
            builder.append(indent).append("  - ").append(file.name).append("\n")
        }

        for (subDirectory in directory.subdirectories) {
            builder.append(generate(subDirectory, "$indent  "))
        }

        return builder.toString()
    }
}