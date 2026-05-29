package com.dkim.kmpprojectview.nodes

import com.intellij.icons.AllIcons
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VfsUtil

class KmpModuleNode(project: Project, module: Module, settings: ViewSettings)
    : ProjectViewNode<Module>(project, module, settings) {

    override fun getChildren(): Collection<AbstractTreeNode<*>> = emptyList()

    override fun update(presentation: PresentationData) {
        presentation.setPresentableText(value.name.removePrefix("${project.name}."))
        presentation.setIcon(AllIcons.Nodes.Module)
    }

    override fun contains(file: VirtualFile): Boolean {
        return ModuleRootManager.getInstance(value).contentRoots
            .any { VfsUtil.isAncestor(it, file, false) }
    }
}
