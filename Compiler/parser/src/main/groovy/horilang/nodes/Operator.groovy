package horilang.nodes

import groovy.transform.ToString
import horilang.lexer.Token
import horilang.lexer.TokenType
import horilang.nodes.Node
import org.jetbrains.annotations.NotNull

@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
class Operator extends Node {

    TokenType value

    Operator(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        super(tokens, nodes)
        build(tokens, nodes)
    }

    private build(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        value = tokens[0].type
    }

}
