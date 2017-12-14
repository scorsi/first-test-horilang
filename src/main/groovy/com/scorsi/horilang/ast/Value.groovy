package com.scorsi.horilang.ast

import com.scorsi.horilang.Token
import groovy.transform.ToString
import org.jetbrains.annotations.NotNull

@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
class Value extends Node {

    String value
    String type

    @Override
    Node build(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        value = tokens[0].value
        type = tokens[0].type.toString().toLowerCase().capitalize()
        return this
    }

}
