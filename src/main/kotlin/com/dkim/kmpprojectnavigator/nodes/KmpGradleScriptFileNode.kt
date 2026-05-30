package com.dkim.kmpprojectnavigator.nodes

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vcs.FileStatus
import com.intellij.openapi.vcs.FileStatusManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.IconUtil

class KmpGradleScriptFileNode(
    project: Project,
    file: VirtualFile,
    settings: ViewSettings,
    private val sortOrder: Int = 0,
) : ProjectViewNode<VirtualFile>(project, file, settings) {

    override fun getWeight(): Int = sortOrder

    override fun getChildren(): Collection<AbstractTreeNode<*>> = emptyList()

    override fun getVirtualFile(): VirtualFile = value

    override fun canNavigate(): Boolean = value.isValid

    override fun canNavigateToSource(): Boolean = canNavigate()

    override fun navigate(requestFocus: Boolean) {
        OpenFileDescriptor(project, value).navigate(requestFocus)
    }

    override fun getFileStatus(): FileStatus =
        FileStatusManager.getInstance(project).getStatus(value)

    override fun update(presentation: PresentationData) {
        val (name, description) = buildDisplayParts()
        presentation.setPresentableText(name)
        presentation.setLocationString(description)
        presentation.setIcon(IconUtil.getIcon(value, 0, project))
    }

    override fun contains(file: VirtualFile): Boolean = value == file

    private fun buildDisplayParts(): Pair<String, String> {
        val description = when (value.name) {
            "gradle.properties" -> "Project Properties"
            "gradle-wrapper.properties" -> "Gradle Version"
            "local.properties" -> "SDK Location"
            "settings.gradle", "settings.gradle.kts" -> "Project Settings"
            "libs.versions.toml" -> "Version Catalog \"libs\""
            "proguard-rules.pro" -> getProguardDescription()
            else -> null
        }
        if (description != null) return value.name to "($description)"

        for (module in ModuleManager.getInstance(project).modules) {
            val moduleRoot = ModuleRootManager.getInstance(module).contentRoots.firstOrNull() ?: continue
            if (value.parent == moduleRoot) {
                return if (module.name == project.name) {
                    value.name to "(Project: ${project.name})"
                } else {
                    val shortName = module.name.removePrefix("${project.name}.")
                    value.name to "(Module :$shortName)"
                }
            }
        }
        return value.name to "(Project: ${project.name})"
    }

    private fun getProguardDescription(): String {
        for (module in ModuleManager.getInstance(project).modules) {
            if (module.name == project.name) continue
            val moduleRoot = ModuleRootManager.getInstance(module).contentRoots.firstOrNull() ?: continue
            if (value.parent == moduleRoot) {
                val shortName = module.name.removePrefix("${project.name}.")
                return "ProGuard Rules for \":$shortName\""
            }
        }
        return "ProGuard Rules"
    }
}
