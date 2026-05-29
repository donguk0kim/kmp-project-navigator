package com.dkim.kmpprojectview.nodes

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class KmpRootNode(project: Project, settings: ViewSettings)
    : ProjectViewNode<Project>(project, project, settings) {

    override fun getChildren(): Collection<AbstractTreeNode<*>> {
        val modules = ModuleManager.getInstance(project).modules
            .filter { it.name != project.name }
            .sortedBy { it.name }
            .map { KmpModuleNode(project, it, settings) }

        return modules + KmpGradleScriptsNode(project, settings)
    }

    override fun update(presentation: PresentationData) {
        presentation.setPresentableText(project.name)
    }

    override fun contains(file: VirtualFile): Boolean = true
}
