package com.scorsi.horilang.ast

import com.scorsi.horilang.Token
import groovy.transform.ToString
import org.jetbrains.annotations.NotNull

@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
class Block extends Node {

    List<Node> statements

    @Override
    Node build(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        statements = nodes.collect()
        nodes.clear()
        return this
    }

}
