package com.dkim.kmpprojectview.nodes

import com.intellij.icons.AllIcons
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiManager

class KmpSourceFolderNode(project: Project, folder: VirtualFile, settings: ViewSettings)
    : ProjectViewNode<VirtualFile>(project, folder, settings) {

    override fun getChildren(): Collection<AbstractTreeNode<*>> {
        val psiDir = PsiManager.getInstance(project).findDirectory(value) ?: return emptyList()
        return PsiDirectoryNode(project, psiDir, settings).getChildren()
    }

    override fun update(presentation: PresentationData) {
        presentation.setPresentableText(value.name)
        presentation.setIcon(iconFor(value.name))
    }

    override fun contains(file: VirtualFile): Boolean = VfsUtil.isAncestor(value, file, false)

    private fun iconFor(name: String) = when (name) {
        "kotlin", "java" -> AllIcons.Modules.SourceRoot
        "resources", "res" -> AllIcons.Modules.ResourcesRoot
        else -> AllIcons.Nodes.Folder
    }
}
