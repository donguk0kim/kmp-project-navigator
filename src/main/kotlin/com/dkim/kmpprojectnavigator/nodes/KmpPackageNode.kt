package com.dkim.kmpprojectnavigator.nodes

import com.intellij.icons.AllIcons
import com.intellij.ide.projectView.NodeSortOrder
import com.intellij.ide.projectView.NodeSortSettings
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.PsiFileNode
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiManager

class KmpPackageNode(
    project: Project,
    dir: VirtualFile,
    private val displayName: String,
    settings: ViewSettings
) : ProjectViewNode<VirtualFile>(project, dir, settings) {

    override fun getChildren(): Collection<AbstractTreeNode<*>> = buildChildren(project, value, settings)

    override fun getWeight(): Int = PACKAGE_WEIGHT

    override fun getSortOrder(settings: NodeSortSettings): NodeSortOrder = NodeSortOrder.FOLDER

    override fun getSortKey(): Comparable<*> = packageSortKey(displayName)

    override fun getTypeSortKey(): Comparable<*> = PACKAGE_TYPE_SORT_KEY

    override fun update(presentation: PresentationData) {
        presentation.setPresentableText(displayName)
        presentation.setIcon(AllIcons.Nodes.Package)
    }

    override fun contains(file: VirtualFile): Boolean = VfsUtil.isAncestor(value, file, false)

    companion object {
        private const val PACKAGE_WEIGHT = 0
        private const val PACKAGE_TYPE_SORT_KEY = "0.package"

        fun buildChildren(project: Project, dir: VirtualFile, settings: ViewSettings): List<AbstractTreeNode<*>> {
            val psiManager = PsiManager.getInstance(project)
            return (dir.children ?: emptyArray())
                .sortedWith(compareBy<VirtualFile> { !it.isDirectory }.thenBy { it.name.lowercase() })
                .mapNotNull { child ->
                    if (child.isDirectory) {
                        val (compacted, name) = compact(child)
                        KmpPackageNode(project, compacted, name, settings)
                    } else {
                        psiManager.findFile(child)?.let { PsiFileNode(project, it, settings) }
                    }
                }
        }

        // Collapse single-child-only directories into a dotted name.
        // Stops when a directory has multiple subdirs, any files, or no children.
        fun compact(dir: VirtualFile): Pair<VirtualFile, String> {
            val parts = mutableListOf(dir.name)
            var current = dir
            while (true) {
                val children = current.children ?: break
                val subdirs = children.filter { it.isDirectory }
                val files = children.filter { !it.isDirectory }
                if (subdirs.size == 1 && files.isEmpty()) {
                    current = subdirs[0]
                    parts.add(current.name)
                } else break
            }
            return current to parts.joinToString(".")
        }

        private fun packageSortKey(name: String): String = "0.${name.lowercase()}"
    }
}
