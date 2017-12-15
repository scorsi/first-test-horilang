import horilang.lexer.Token;
import horilang.lexer.TokenType;
import horilang.nodes.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class NodesTest {

    private List<Token> tokens = new ArrayList<>();
    private List<Node> nodes = new ArrayList<>();

    private static final String STRING_VALUE = "test";
    private static final String INTEGER_VALUE = "42";
    private static final String FLOAT_VALUE = "42.00";
    private static final String ASSIGNMENT_SYMBOL = "myVar";
    private static final String DECLARATION_SYMBOL = "myVar";

    @Before
    public void before() {
    }

    @After
    public void after() {
    }

    private Value createIntegerValue() {
        tokens.add(new Token(TokenType.INTEGER, INTEGER_VALUE));
        return new Value(tokens, nodes);
    }

    private Value createStringValue() {
        tokens.add(new Token(TokenType.STRING, STRING_VALUE));
        return new Value(tokens, nodes);
    }

    private Value createFloatValue() {
        tokens.add(new Token(TokenType.FLOAT, FLOAT_VALUE));
        return new Value(tokens, nodes);

    }

    private VariableAssignment createVariableAssignment() {
        tokens.add(new Token(TokenType.SYMBOL, ASSIGNMENT_SYMBOL));
        tokens.add(new Token(TokenType.ASSIGN, "="));
        nodes.add(createIntegerValue());
        return new VariableAssignment(tokens, nodes);
    }

    private VariableDeclaration createVariableDeclaration() {
        tokens.add(new Token(TokenType.VAR, "var"));
        tokens.add(new Token(TokenType.SYMBOL, DECLARATION_SYMBOL));
        tokens.add(new Token(TokenType.EQUAL, "="));
        nodes.add(createIntegerValue());
        return new VariableDeclaration(tokens, nodes);
    }

    private Operator createAddOperator() {
        tokens.add(new Token(TokenType.ADD, "+"));
        return new Operator(tokens, nodes);
    }

    private Operator createSubOperator() {
        tokens.add(new Token(TokenType.SUB, "-"));
        return new Operator(tokens, nodes);
    }

    @Test
    public void integerValue() {
        Value node = createIntegerValue();
        assert node.getValue().equals(INTEGER_VALUE);
        assert node.getType().equals("Integer");
        assert node.getSign() == null;
    }

    @Test
    public void stringValue() {
        Value node = createStringValue();
        assert node.getValue().equals(STRING_VALUE);
        assert node.getType().equals("String");
    }

    @Test
    public void floatValue() {
        Value node = createFloatValue();
        assert node.getValue().equals(FLOAT_VALUE);
        assert node.getType().equals("Float");
    }

    @Test
    public void variableAssignment() {
        VariableAssignment node = createVariableAssignment();
        assert node.getLeftValue().equals(ASSIGNMENT_SYMBOL);
        assert node.getRightValue().equals(createIntegerValue());
    }

    @Test
    public void variableDeclaration() {
        VariableDeclaration node = createVariableDeclaration();
        assert node.getSymbol().equals(DECLARATION_SYMBOL);
        assert node.getRightValue().equals(createIntegerValue());
    }

    @Test
    public void addOperator() {
        Operator node = createAddOperator();
        assert node.getValue().equals(TokenType.ADD);
    }

    @Test
    public void subOperator() {
        Operator node = createSubOperator();
        assert node.getValue().equals(TokenType.SUB);
    }

}
