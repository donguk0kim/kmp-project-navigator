package com.dkim.kmpprojectnavigator

import com.intellij.icons.AllIcons
import com.intellij.ide.SelectInTarget
import com.intellij.ide.dnd.aware.DnDAwareTree
import com.intellij.ide.projectView.impl.AbstractProjectViewPaneWithAsyncSupport
import com.intellij.ide.util.treeView.AbstractTreeStructureBase
import com.intellij.openapi.project.Project
import javax.swing.Icon
import javax.swing.tree.DefaultTreeModel

class KmpProjectNavigatorPane(project: Project) : AbstractProjectViewPaneWithAsyncSupport(project) {

    companion object {
        const val ID = "KmpProjectNavigator"
    }

    override fun getTitle(): String = "KMP Project"
    override fun getIcon(): Icon = AllIcons.General.ProjectStructure
    override fun getId(): String = ID
    override fun getWeight(): Int = 10

    override fun createStructure(): AbstractTreeStructureBase = KmpProjectTreeStructure(myProject)

    override fun createTree(treeModel: DefaultTreeModel): DnDAwareTree = DnDAwareTree(treeModel)

    override fun createSelectInTarget(): SelectInTarget = KmpSelectInTarget(myProject)
}
