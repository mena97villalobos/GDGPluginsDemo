package com.mena97villalobos.viewmodelfactorycreator.compiler.ast

import com.mena97villalobos.viewmodelfactorycreator.compiler.ast.implementations.terminals.Identifier
import com.mena97villalobos.viewmodelfactorycreator.compiler.ast.implementations.declarations.*

interface Visitor {

    fun visitDependencyBlock(block: DependenciesBlockDeclaration, o: Any): Any

    fun visitModuleIdentifier(module: DependencyDeclaration, o: Any): Any

    fun visitImplementationDeclaration(declaration: DependencyImplementationDeclaration, o: Any): Any

    fun visitUnrelatedFileContent(content: UnrelatedFileContent, o: Any): Any

    fun visitVariableDeclaration(variable: VariableDeclaration, o: Any): Any

    fun visitIdentifier(declaration: Identifier, o: Any): Any

}