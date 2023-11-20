package com.mena97villalobos.viewmodelfactorycreator.compiler.ast.base

import com.mena97villalobos.viewmodelfactorycreator.compiler.ast.AST
import com.mena97villalobos.viewmodelfactorycreator.compiler.syntax.SourcePosition

abstract class BaseDependency(position: SourcePosition): AST(position)