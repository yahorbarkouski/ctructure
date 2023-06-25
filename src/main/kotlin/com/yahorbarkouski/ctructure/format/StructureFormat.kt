package com.yahorbarkouski.ctructure.format

import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory

/**
 * Abstract class defining the structure format of a file type.
 *
 * @param fileType the type of file to be formatted
 */
abstract class StructureFormat(private val fileType: FileType) {

    abstract val title: String

    /**
     * Generates a string representation of the contents of the given directory and its children.
     *
     * @param directory The directory to generate the structure of.
     * @param indent The string to use for indentation of child files and directories.
     * @return A string representation of structure of the given directory and its children.
     */
    abstract fun generate(directory: PsiDirectory, indent: String = ""): String

    /**
     * Creates a new EditorEx instance with the specified directory and project,
     * used for better displaying the structure of the directory.
     *
     * @param directory the PsiDirectory to generate the document from
     * @param project the Project instance associated with the editor
     * @return the newly created EditorEx instance
     */
    fun createEditor(directory: PsiDirectory, project: Project): EditorEx {
        val editorFactory = EditorFactory.getInstance()
        val document = editorFactory.createDocument(generate(directory))
        return (editorFactory.createEditor(document, project) as EditorEx).apply {
            isViewer = true
            highlighter = EditorHighlighterFactory.getInstance().createEditorHighlighter(project, fileType)
        }
    }
}