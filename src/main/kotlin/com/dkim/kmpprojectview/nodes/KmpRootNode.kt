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
        val allModules = ModuleManager.getInstance(project).modules
        val allModuleContentRoots = allModules
            .flatMap { ModuleRootManager.getInstance(it).contentRoots.toList() }
            .toSet()

        val moduleNodes = allModules
            .filter { it.name != project.name }
            .filter { module ->
                // Exclude KMP source-set child modules (e.g. "Seatosky.app.commonMain")
                val shortName = module.name.removePrefix("${project.name}.")
                !shortName.contains('.')
            }
            .filter { module ->
                // Exclude virtual KMP target modules (e.g. "Seatosky.ios") that have no
                // content root — they are not real Gradle subprojects
                ModuleRootManager.getInstance(module).contentRoots.isNotEmpty()
            }
            .map { KmpModuleNode(project, it, settings) }

        val iosNodes = findIosAppDirs(allModuleContentRoots)
            .map { KmpIosAppNode(project, it, settings) }

        return moduleNodes + iosNodes + KmpGradleScriptsNode(project, settings)
    }

    override fun update(presentation: PresentationData) {
        presentation.setPresentableText(project.name)
    }

    override fun contains(file: VirtualFile): Boolean = true

    private fun findIosAppDirs(moduleRoots: Set<VirtualFile>): List<VirtualFile> {
        val basePath = project.basePath ?: return emptyList()
        val baseDir = LocalFileSystem.getInstance().findFileByPath(basePath) ?: return emptyList()
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
