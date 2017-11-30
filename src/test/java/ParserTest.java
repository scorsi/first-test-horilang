import com.scorsi.horilang.lexer.Lexer;
import com.scorsi.horilang.lexer.LexerStream;
import com.scorsi.horilang.lexer.TokenType;
import com.scorsi.horilang.lexer.builder.LexerBuilder;
import com.scorsi.horilang.parser.Parser;
import com.scorsi.horilang.parser.ast.*;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    Lexer lexer;

    @Before
    public void init() {
        lexer = new LexerBuilder().defaultBuild();
    }

    @Test
    public void testEmptyInput() {
        assert new Parser(lexer.lex("")).parse() == null;
    }

    @Test
    public void testDeclaration() {
        Parser parser = new Parser(lexer.lex("var a : Integer"));
        Node node = parser.parse();
        assert node != null;
        assert node instanceof BlockNode;
        assert ((BlockNode) node).getStatements().size() == 1;
        assert ((BlockNode) node).getStatements().get(0) instanceof DeclarationNode;
        DeclarationNode declarationNode = (DeclarationNode)((BlockNode) node).getStatements().get(0);
        assert declarationNode.getSymbol().getType() == TokenType.SYMBOL && declarationNode.getSymbol().getValue().equals("a");
        assert declarationNode.getType().getType() == TokenType.TYPE && declarationNode.getType().getValue().equals("Integer");
        assert declarationNode.getValue() == null;
    }

    @Test
    public void testDeclarationWithAssignment() {
        Parser parser = new Parser(lexer.lex("var testString : String = \"test test\""));
        Node node = parser.parse();
        assert node != null;
        assert node instanceof BlockNode;
        assert ((BlockNode) node).getStatements().size() == 1;
        assert ((BlockNode) node).getStatements().get(0) instanceof DeclarationNode;
        DeclarationNode declarationNode = (DeclarationNode)((BlockNode) node).getStatements().get(0);
        assert declarationNode.getSymbol().getType() == TokenType.SYMBOL && declarationNode.getSymbol().getValue().equals("testString");
        assert declarationNode.getType().getType() == TokenType.TYPE && declarationNode.getType().getValue().equals("String");
        assert declarationNode.getValue().getType() == TokenType.STRING && declarationNode.getValue().getValue().equals("\"test test\"");
    }

    @Test
    public void testAssignment() {
        Parser parser = new Parser(lexer.lex("abcAZ9 = \"test\""));
        Node node = parser.parse();
        assert node != null;
        assert node instanceof BlockNode;
        assert ((BlockNode) node).getStatements().size() == 1;
        assert ((BlockNode) node).getStatements().get(0) instanceof AssignmentNode;
        AssignmentNode assignmentNode = (AssignmentNode)((BlockNode) node).getStatements().get(0);
        assert assignmentNode.getSymbol().getType() == TokenType.SYMBOL && assignmentNode.getSymbol().getValue().equals("abcAZ9");
        assert assignmentNode.getValue().getType() == TokenType.STRING && assignmentNode.getValue().getValue().equals("\"test\"");
    }

    @Test
    public void testBasicIf() {
        Parser parser = new Parser(lexer.lex("if (a == 0) { b = 1; }"));
        Node node = parser.parse();
        assert node != null;
        assert node instanceof BlockNode;
        assert ((BlockNode) node).getStatements().size() == 1;
        assert ((BlockNode) node).getStatements().get(0) instanceof ConditionalNode;
        ConditionalNode conditionalNode = (ConditionalNode)((BlockNode) node).getStatements().get(0);
        assert conditionalNode.getCondition().getLeft().getType() == TokenType.SYMBOL && conditionalNode.getCondition().getLeft().getValue().equals("a");
        assert conditionalNode.getCondition().getRight().getType() == TokenType.NUMBER && conditionalNode.getCondition().getRight().getValue().equals("0");
        assert conditionalNode.getCondition().getOperator().getType() == TokenType.EQUAL;
        BlockNode thenBlock = conditionalNode.getThenBlock();
        assert thenBlock.getStatements().size() == 1;
        assert thenBlock.getStatements().get(0) instanceof AssignmentNode;
        AssignmentNode assignmentNode = (AssignmentNode)thenBlock.getStatements().get(0);
        assert assignmentNode.getSymbol().getValue().equals("b");
        assert assignmentNode.getValue().getType() == TokenType.NUMBER;
        assert assignmentNode.getValue().getValue().equals("1");
        assert conditionalNode.getElseBlock() == null;
    }

    @Test
    public void testBasicIfWithElse() {
        Parser parser = new Parser(lexer.lex("if (a == 0) { b = 1; } else { c = 2; }"));
        Node node = parser.parse();
        assert node != null;
        assert node instanceof BlockNode;
        assert ((BlockNode) node).getStatements().size() == 1;
        assert ((BlockNode) node).getStatements().get(0) instanceof ConditionalNode;
        ConditionalNode conditionalNode = (ConditionalNode)((BlockNode) node).getStatements().get(0);
        assert conditionalNode.getCondition().getLeft().getType() == TokenType.SYMBOL && conditionalNode.getCondition().getLeft().getValue().equals("a");
        assert conditionalNode.getCondition().getRight().getType() == TokenType.NUMBER && conditionalNode.getCondition().getRight().getValue().equals("0");
        assert conditionalNode.getCondition().getOperator().getType() == TokenType.EQUAL;
        BlockNode thenBlock = conditionalNode.getThenBlock();
        assert thenBlock.getStatements().size() == 1;
        assert thenBlock.getStatements().get(0) instanceof AssignmentNode;
        AssignmentNode assignmentNode = (AssignmentNode)thenBlock.getStatements().get(0);
        assert assignmentNode.getSymbol().getValue().equals("b");
        assert assignmentNode.getValue().getType() == TokenType.NUMBER;
        assert assignmentNode.getValue().getValue().equals("1");
        assert conditionalNode.getElseBlock() != null;
        BlockNode elseBlock = conditionalNode.getElseBlock();
        assert elseBlock.getStatements().size() == 1;
        assert thenBlock.getStatements().get(0) instanceof AssignmentNode;
        assignmentNode = (AssignmentNode)elseBlock.getStatements().get(0);
        assert assignmentNode.getSymbol().getValue().equals("c");
        assert assignmentNode.getValue().getType() == TokenType.NUMBER;
        assert assignmentNode.getValue().getValue().equals("2");
    }

}
