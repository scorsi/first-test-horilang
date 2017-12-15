package horilang.nodes

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import horilang.lexer.Token
import horilang.lexer.TokenType
import org.jetbrains.annotations.NotNull

@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
@EqualsAndHashCode
class VariableDeclaration extends Node {

    String symbol
    Boolean isConst = false
    Node rightValue = null

    VariableDeclaration(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        super(tokens, nodes)
        build(tokens, nodes)
    }

    private build(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        if (!nodes.isEmpty() && tokens.size() > 2 && tokens[2].type == TokenType.ASSIGN) {
            def node = nodes.last()
            if (!node instanceof Value && !node instanceof Expression)
                throw new RuntimeException("Unexpected parameter, wanted [Expression, Value] but got $node")
            rightValue = node
            nodes.remove(nodes.last())
        }
        symbol = tokens[1].value
        if (tokens[0].type == TokenType.VAL)
            isConst = true
    }

}
