package horilang.nodes

import groovy.transform.ToString
import horilang.Token
import horilang.TokenType
import org.jetbrains.annotations.NotNull

@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
class Operator extends Node {

    TokenType value

    @Override
    Node build(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        value = tokens[0].type
        return this
    }

}
