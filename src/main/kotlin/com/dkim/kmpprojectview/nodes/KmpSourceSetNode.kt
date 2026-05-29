package com.dkim.kmpprojectview.nodes

import com.intellij.icons.AllIcons
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VfsUtil

class KmpSourceSetNode(project: Project, sourceSetDir: VirtualFile, settings: ViewSettings)
    : ProjectViewNode<VirtualFile>(project, sourceSetDir, settings) {

    companion object {
        private val ALLOWED_FOLDERS = setOf("kotlin", "java", "resources", "res", "manifests")
    }

    override fun getChildren(): Collection<AbstractTreeNode<*>> {
        return (value.children ?: emptyArray())
            .filter { it.isDirectory && it.name in ALLOWED_FOLDERS }
            .sortedBy { it.name }
            .map { KmpSourceFolderNode(project, it, settings) }
    }

    override fun update(presentation: PresentationData) {
        presentation.setPresentableText(value.name)
        presentation.setIcon(AllIcons.Nodes.Module)
    }

    override fun contains(file: VirtualFile): Boolean = VfsUtil.isAncestor(value, file, false)
}
