package horilang.nodes

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import horilang.lexer.Token
import horilang.lexer.TokenType
import org.jetbrains.annotations.NotNull

@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
@EqualsAndHashCode
class Operator extends Node {

    TokenType value

    Operator(@NotNull List<Token> tokens, @NotNull List<horilang.nodes.Node> nodes) {
        super(tokens, nodes)

        value = tokens[0].type
    }

}
