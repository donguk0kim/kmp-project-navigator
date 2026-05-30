package com.dkim.kmpprojectnavigator.nodes

import com.intellij.icons.AllIcons
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VfsUtil

class KmpSourceFolderNode(project: Project, folder: VirtualFile, settings: ViewSettings)
    : ProjectViewNode<VirtualFile>(project, folder, settings) {

    override fun getChildren(): Collection<AbstractTreeNode<*>> =
        KmpPackageNode.buildChildren(project, value, settings)

    override fun getSortKey(): Comparable<*> =
        KmpSourceSetContentSort.sortKey(value.name)

    override fun getTypeSortKey(): Comparable<*> = sortKey

    override fun update(presentation: PresentationData) {
        presentation.setPresentableText(value.name)
        presentation.setIcon(iconFor(value.name))
    }

    override fun contains(file: VirtualFile): Boolean = VfsUtil.isAncestor(value, file, false)

    private fun iconFor(name: String) =
        if (KmpSourceSetContentSort.isResourceFolder(name)) {
            AllIcons.Modules.ResourcesRoot
        } else {
            AllIcons.Modules.SourceRoot
        }
}
