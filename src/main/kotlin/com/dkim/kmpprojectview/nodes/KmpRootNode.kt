package com.dkim.kmpprojectview.nodes

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile

class KmpRootNode(project: Project, settings: ViewSettings)
    : ProjectViewNode<Project>(project, project, settings) {

    override fun getChildren(): Collection<AbstractTreeNode<*>> {
        val moduleNodes = ModuleManager.getInstance(project).modules
            .filter { it.name != project.name }
            .map { KmpModuleNode(project, it, settings) }

        val iosNodes = findIosAppDirs()
            .map { KmpIosAppNode(project, it, settings) }

        val topLevel = (moduleNodes + iosNodes).sortedBy { it.displayName() }

        return topLevel + KmpGradleScriptsNode(project, settings)
    }

    override fun update(presentation: PresentationData) {
        presentation.setPresentableText(project.name)
    }

    override fun contains(file: VirtualFile): Boolean = true

    private fun KmpModuleNode.displayName() = value.name.removePrefix("${project.name}.")
    private fun KmpIosAppNode.displayName() = value.name
    private fun AbstractTreeNode<*>.displayName() = when (this) {
        is KmpModuleNode -> displayName()
        is KmpIosAppNode -> displayName()
        else -> ""
    }

    private fun findIosAppDirs(): List<VirtualFile> {
        val basePath = project.basePath ?: return emptyList()
        val baseDir = LocalFileSystem.getInstance().findFileByPath(basePath) ?: return emptyList()

        val moduleRoots = ModuleManager.getInstance(project).modules
            .flatMap { ModuleRootManager.getInstance(it).contentRoots.toList() }
            .toSet()

        return (baseDir.children ?: emptyArray())
            .filter { it.isDirectory && it !in moduleRoots && isIosAppDir(it) }
            .sortedBy { it.name }
    }

    private fun isIosAppDir(dir: VirtualFile): Boolean {
        return (dir.children ?: emptyArray()).any { child ->
            child.name.endsWith(".xcodeproj") || child.name.endsWith(".xcworkspace")
        }
    }
}
