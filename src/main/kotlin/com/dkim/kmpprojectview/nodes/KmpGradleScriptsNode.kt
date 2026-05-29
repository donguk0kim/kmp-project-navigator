package com.dkim.kmpprojectview.nodes

import com.intellij.icons.AllIcons
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile

class KmpGradleScriptsNode(project: Project, settings: ViewSettings)
    : ProjectViewNode<String>(project, "Gradle Scripts", settings) {

    override fun getChildren(): Collection<AbstractTreeNode<*>> =
        findGradleScripts().map { KmpGradleScriptFileNode(project, it, settings) }

    override fun update(presentation: PresentationData) {
        presentation.setPresentableText("Gradle Scripts")
        presentation.setIcon(AllIcons.Nodes.Folder)
    }

    override fun contains(file: VirtualFile): Boolean = false

    private fun findGradleScripts(): List<VirtualFile> {
        val scripts = mutableListOf<VirtualFile>()
        val basePath = project.basePath ?: return emptyList()
        val baseDir = LocalFileSystem.getInstance().findFileByPath(basePath) ?: return emptyList()

        listOf(
            "build.gradle", "build.gradle.kts",
            "settings.gradle", "settings.gradle.kts",
            "gradle.properties", "local.properties"
        ).forEach { name -> baseDir.findChild(name)?.let { scripts.add(it) } }

        baseDir.findChild("gradle")
            ?.findChild("wrapper")
            ?.findChild("gradle-wrapper.properties")
            ?.let { scripts.add(it) }

        for (module in ModuleManager.getInstance(project).modules) {
            if (module.name == project.name) continue
            val moduleRoot = ModuleRootManager.getInstance(module).contentRoots.firstOrNull() ?: continue
            listOf("build.gradle", "build.gradle.kts").forEach { name ->
                moduleRoot.findChild(name)?.let { scripts.add(it) }
            }
        }

        return scripts
    }
}
