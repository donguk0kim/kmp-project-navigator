package com.dkim.kmpprojectnavigator

import com.dkim.kmpprojectnavigator.nodes.KmpRootNode
import com.intellij.ide.projectView.ProjectViewSettings
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.AbstractProjectTreeStructure
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project

class KmpProjectTreeStructure(private val project: Project) : AbstractProjectTreeStructure(project) {

    override fun createRoot(project: Project, settings: ViewSettings): AbstractTreeNode<*> =
        KmpRootNode(project, settings)

    override fun isHideEmptyMiddlePackages(): Boolean =
        ProjectViewSettings.Delegate(project, KmpProjectNavigatorPane.ID).isHideEmptyMiddlePackages

    override fun isCompactDirectories(): Boolean = false
}
