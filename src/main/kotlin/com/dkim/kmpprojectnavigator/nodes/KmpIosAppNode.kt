package com.dkim.kmpprojectnavigator.nodes

import com.dkim.kmpprojectnavigator.KmpIcons
import com.intellij.icons.AllIcons
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode
import com.intellij.ide.projectView.impl.nodes.PsiFileNode
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiManager

class KmpIosAppNode(project: Project, dir: VirtualFile, settings: ViewSettings)
    : ProjectViewNode<VirtualFile>(project, dir, settings) {

    override fun getChildren(): Collection<AbstractTreeNode<*>> {
        val psiManager = PsiManager.getInstance(project)
        return (value.children ?: emptyArray())
            .sortedBy { it.name }
            .mapNotNull { child ->
                when {
                    child.isDirectory && isIosSourceSetDir(child) ->
                        KmpIosSourceSetNode(project, child, settings)
                    child.isDirectory ->
                        psiManager.findDirectory(child)?.let { PsiDirectoryNode(project, it, settings) }
                    else ->
                        psiManager.findFile(child)?.let { PsiFileNode(project, it, settings) }
                }
            }
    }

    override fun update(presentation: PresentationData) {
        presentation.setPresentableText(value.name)
        presentation.setIcon(KmpIcons.LibraryModule)
    }

    override fun contains(file: VirtualFile): Boolean = VfsUtil.isAncestor(value, file, false)

    private fun isIosSourceSetDir(dir: VirtualFile): Boolean {
        if (dir.name in IGNORED_DIRECTORIES || dir.name.endsWith(".xcodeproj") || dir.name.endsWith(".xcworkspace")) {
            return false
        }

        return dir.name == "ios" || containsIosSourceIndicator(dir)
    }

    private fun containsIosSourceIndicator(dir: VirtualFile, depth: Int = 2): Boolean {
        if (depth < 0) return false

        return (dir.children ?: emptyArray()).any { child ->
            when {
                child.isDirectory ->
                    child.extension in SOURCE_DIRECTORY_EXTENSIONS ||
                        (child.name !in IGNORED_DIRECTORIES && containsIosSourceIndicator(child, depth - 1))
                child.name in SOURCE_FILE_NAMES -> true
                child.extension in SOURCE_FILE_EXTENSIONS -> true
                else -> false
            }
        }
    }

    private companion object {
        private val IGNORED_DIRECTORIES = setOf(
            "build",
            "DerivedData",
            "Pods",
            ".swiftpm",
            "xcshareddata",
            "xcuserdata",
        )
        private val SOURCE_DIRECTORY_EXTENSIONS = setOf("xcassets")
        private val SOURCE_FILE_EXTENSIONS = setOf("swift", "m", "mm", "h", "hpp", "c", "cpp", "metal", "storyboard", "xib")
        private val SOURCE_FILE_NAMES = setOf("Info.plist")
    }
}

private class KmpIosSourceSetNode(project: Project, dir: VirtualFile, settings: ViewSettings)
    : ProjectViewNode<VirtualFile>(project, dir, settings) {

    override fun getChildren(): Collection<AbstractTreeNode<*>> {
        val psiDir = PsiManager.getInstance(project).findDirectory(value) ?: return emptyList()
        return PsiDirectoryNode(project, psiDir, settings).getChildren()
    }

    override fun update(presentation: PresentationData) {
        presentation.setPresentableText(value.name)
        presentation.setIcon(AllIcons.Nodes.Module)
    }

    override fun contains(file: VirtualFile): Boolean = VfsUtil.isAncestor(value, file, false)
}
