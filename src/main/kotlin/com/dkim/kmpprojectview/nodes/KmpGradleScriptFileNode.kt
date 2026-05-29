package com.dkim.kmpprojectview.nodes

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.IconUtil

class KmpGradleScriptFileNode(project: Project, file: VirtualFile, settings: ViewSettings)
    : ProjectViewNode<VirtualFile>(project, file, settings) {

    override fun getChildren(): Collection<AbstractTreeNode<*>> = emptyList()

    override fun update(presentation: PresentationData) {
        presentation.setPresentableText(buildDisplayName())
        presentation.setIcon(IconUtil.getIcon(value, 0, project))
    }

    override fun contains(file: VirtualFile): Boolean = value == file

    private fun buildDisplayName(): String {
        for (module in ModuleManager.getInstance(project).modules) {
            val moduleRoot = ModuleRootManager.getInstance(module).contentRoots.firstOrNull() ?: continue
            if (value.parent == moduleRoot) {
                return if (module.name == project.name) {
                    "${value.name} (Project: ${project.name})"
                } else {
                    "${value.name} (Module: ${module.name.removePrefix("${project.name}.")})"
                }
            }
        }
        return "${value.name} (Project: ${project.name})"
    }
}
