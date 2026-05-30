package com.dkim.kmpprojectnavigator.nodes

import com.intellij.icons.AllIcons
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.PsiFileNode
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager

class KmpManifestsNode(
    project: Project,
    private val manifestFile: VirtualFile,
    settings: ViewSettings
) : ProjectViewNode<VirtualFile>(project, manifestFile.parent, settings) {

    override fun getChildren(): Collection<AbstractTreeNode<*>> {
        val psiFile = PsiManager.getInstance(project).findFile(manifestFile) ?: return emptyList()
        return listOf(PsiFileNode(project, psiFile, settings))
    }

    override fun update(presentation: PresentationData) {
        presentation.setPresentableText("manifests")
        presentation.setIcon(AllIcons.Modules.SourceRoot)
    }

    override fun getSortKey(): Comparable<*> =
        KmpSourceSetContentSort.sortKey(KmpSourceSetContentSort.MANIFESTS_FOLDER)

    override fun getTypeSortKey(): Comparable<*> = sortKey

    override fun contains(file: VirtualFile): Boolean = file == manifestFile
}
