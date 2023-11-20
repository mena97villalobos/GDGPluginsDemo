package com.mena97villalobos.viewmodelfactorycreator.compiler.ast.implementations.declarations

import com.mena97villalobos.viewmodelfactorycreator.compiler.ast.Visitor
import com.mena97villalobos.viewmodelfactorycreator.compiler.ast.base.BaseDependencyImplementation
import com.mena97villalobos.viewmodelfactorycreator.compiler.ast.base.BaseDependenciesBlock
import com.mena97villalobos.viewmodelfactorycreator.compiler.ast.base.BaseVariable
import com.mena97villalobos.viewmodelfactorycreator.compiler.syntax.SourcePosition

data class DependenciesBlockDeclaration(
        val implementations: MutableList<BaseDependencyImplementation>,
        val variables: MutableList<BaseVariable>,
        var restOfFile: String,
        val position: SourcePosition
): BaseDependenciesBlock(position) {
    override fun visit(v: Visitor, o: Any): Any = v.visitDependencyBlock(this, o)

}