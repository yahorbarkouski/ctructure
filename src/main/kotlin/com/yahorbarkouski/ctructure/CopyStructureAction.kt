package com.yahorbarkouski.ctructure

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.psi.PsiDirectory
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTabbedPane
import com.yahorbarkouski.ctructure.format.JsonStructureFormat
import com.yahorbarkouski.ctructure.format.PlainStructureFormat
import com.yahorbarkouski.ctructure.format.YamlStructureFormat
import java.awt.Dimension
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

/**
* An action that copies the structure of a specified PsiDirectory to the clipboard in various formats.
*/
class CopyStructureAction : AnAction() {

    private val formats = listOf(YamlStructureFormat(), PlainStructureFormat(), JsonStructureFormat())

    /**
     * Shows a dialog that allows user to copy the structure of the selected PsiDirectory.
     * If there is no PsiDirectory selected, the method returns.
     *
     * @param e AnActionEvent representing the action performed on the selected PsiDirectory.
     */
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val psiElement = CommonDataKeys.PSI_ELEMENT.getData(e.dataContext) as? PsiDirectory ?: return

        val tabbedPane = JBTabbedPane()
        val editors = formats.map { format ->
            format.createEditor(psiElement, project).also { editor ->
                tabbedPane.addTab(format.title, JBScrollPane(editor.component))
            }
        }

        val builder = DialogBuilder(project)
        builder.centerPanel(tabbedPane)
        builder.addCancelAction()
        builder.addOkAction().setText("Copy")
        builder.setTitle("Copy Structure")
        builder.setPreferredFocusComponent(tabbedPane)
        builder.setOkOperation {
            val selectedText = editors[tabbedPane.selectedIndex].document.text
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            clipboard.setContents(StringSelection(selectedText), null)
            builder.dialogWrapper.close(0)
        }

        builder.window.preferredSize = Dimension(800, 600)
        builder.show()
    }

    /**
     * Overrides the update method to enable or disable the action's presentation, based on whether the
     * data context contains a PSI element that is a directory.
     *
     * @param e the action event
     */
    override fun update(e: AnActionEvent) {
        val dataContext = e.dataContext
        val psiElement = CommonDataKeys.PSI_ELEMENT.getData(dataContext)
        e.presentation.isEnabled = psiElement is PsiDirectory
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}

