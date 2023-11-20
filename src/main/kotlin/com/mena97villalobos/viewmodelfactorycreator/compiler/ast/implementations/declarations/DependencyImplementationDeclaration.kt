package com.mena97villalobos.viewmodelfactorycreator.compiler.ast.implementations.declarations

import com.mena97villalobos.viewmodelfactorycreator.compiler.ast.Visitor
import com.mena97villalobos.viewmodelfactorycreator.compiler.ast.base.BaseDependencyImplementation
import com.mena97villalobos.viewmodelfactorycreator.compiler.ast.base.BaseDependency
import com.mena97villalobos.viewmodelfactorycreator.compiler.syntax.SourcePosition

data class DependencyImplementationDeclaration(
        val keywordSpelling: String,
        val dependencyIdentifier: BaseDependency,
        val position: SourcePosition
): BaseDependencyImplementation(position) {
    override fun visit(v: Visitor, o: Any): Any = v.visitImplementationDeclaration(this, o)
}