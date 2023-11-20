package com.mena97villalobos.viewmodelfactorycreator

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.descriptors.ValueParameterDescriptor

class ViewModelFactoryCreator: PsiElementBaseIntentionAction(), IntentionAction {

    // TODO 8: Nombre que se mostrará en las configuraciones del IDE
    override fun getFamilyName(): String = "View model factory creator"

    // TODO 9: Texto que se mostrará en el context actions del IDE
    override fun getText(): String = "Create view model factory"

    // TODO 10: Función que determina si el context action debe mostrarse
    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        TODO("Not yet implemented")
    }

    // TODO 11: Función que se ejecuta al seleccionar el context action
    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        TODO("Not yet implemented")
    }

    private fun generateParametersList(list: List<ValueParameterDescriptor>) =
            list.joinToString { "private val ${it.name}: ${it.type}" }

    private fun generateCallingParams(list: List<ValueParameterDescriptor>) =
            list.joinToString { it.name.toString() }

    private fun getViewModelFactory(
            className: String,
            params: List<ValueParameterDescriptor>?
    ) = """


        @Suppress("UNCHECKED_CAST")
        class ${className}Factory(${generateParametersList(params ?: emptyList())}) :
            ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(${className}::class.java))
                    return ${className}(${generateCallingParams(params ?: emptyList())}) as T
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
        """
}