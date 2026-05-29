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
import com.intellij.util.IconUtil

class KmpGradleScriptsNode(project: Project, settings: ViewSettings)
    : ProjectViewNode<String>(project, "Gradle Scripts", settings) {

    override fun getChildren(): Collection<AbstractTreeNode<*>> =
        findGradleScripts().mapIndexed { index, file ->
            KmpGradleScriptFileNode(project, file, settings, index)
        }

    override fun update(presentation: PresentationData) {
        presentation.setPresentableText("Gradle Scripts")
        val icon = findGradleScripts().firstOrNull()
            ?.let { IconUtil.getIcon(it, 0, project) }
            ?: AllIcons.Nodes.Folder
        presentation.setIcon(icon)
    }

    override fun getWeight(): Int = Int.MAX_VALUE

    override fun contains(file: VirtualFile): Boolean = false

    private fun findGradleScripts(): List<VirtualFile> {
        val scripts = mutableListOf<VirtualFile>()
        val basePath = project.basePath ?: return emptyList()
        val baseDir = LocalFileSystem.getInstance().findFileByPath(basePath) ?: return emptyList()

        // 1. Project-level build file first
        listOf("build.gradle.kts", "build.gradle").forEach { name ->
            baseDir.findChild(name)?.let { scripts.add(it) }
        }

        // 2. Module build files sorted alphabetically by short module name
        val modules = ModuleManager.getInstance(project).modules
            .filter { it.name != project.name }
            .sortedBy { it.name.removePrefix("${project.name}.") }
        for (module in modules) {
            val moduleRoot = ModuleRootManager.getInstance(module).contentRoots.firstOrNull() ?: continue
            listOf("build.gradle.kts", "build.gradle").forEach { name ->
                moduleRoot.findChild(name)?.let { scripts.add(it) }
            }
        }

        // 3. proguard-rules.pro from module directories
        for (module in modules) {
            val moduleRoot = ModuleRootManager.getInstance(module).contentRoots.firstOrNull() ?: continue
            moduleRoot.findChild("proguard-rules.pro")?.let { scripts.add(it) }
        }

        // 4. Remaining project-level files in Android Studio order
        baseDir.findChild("gradle.properties")?.let { scripts.add(it) }

        baseDir.findChild("gradle")
            ?.findChild("wrapper")
            ?.findChild("gradle-wrapper.properties")
            ?.let { scripts.add(it) }

        baseDir.findChild("gradle")
            ?.findChild("libs.versions.toml")
            ?.let { scripts.add(it) }

        baseDir.findChild("local.properties")?.let { scripts.add(it) }

        listOf("settings.gradle.kts", "settings.gradle").forEach { name ->
            baseDir.findChild(name)?.let { scripts.add(it) }
        }

        return scripts
    }
}
