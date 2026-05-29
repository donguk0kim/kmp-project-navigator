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
        val children = mutableListOf<AbstractTreeNode<*>>()

        val hasManifestsDir = value.findChild("manifests")?.isDirectory == true
        if (!hasManifestsDir) {
            value.findChild("AndroidManifest.xml")?.let {
                children.add(KmpManifestsNode(project, it, settings))
            }
        }

        (value.children ?: emptyArray())
            .filter { it.isDirectory && it.name in ALLOWED_FOLDERS }
            .sortedBy { it.name }
            .mapTo(children) { KmpSourceFolderNode(project, it, settings) }

        return children
    }

    override fun update(presentation: PresentationData) {
        presentation.setPresentableText(value.name)
        presentation.setIcon(AllIcons.Nodes.Module)
    }

    override fun contains(file: VirtualFile): Boolean = VfsUtil.isAncestor(value, file, false)
}
