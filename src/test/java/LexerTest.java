import com.scorsi.horilang.lexer.Lexer;
import com.scorsi.horilang.lexer.LexerStream;
import com.scorsi.horilang.lexer.Token;
import com.scorsi.horilang.lexer.TokenType;
import com.scorsi.horilang.lexer.builder.LexerBuilder;
import org.junit.Before;
import org.junit.Test;

public class LexerTest {

    Lexer lexer;

    @Before
    public void init() {
        lexer = new LexerBuilder().defaultBuild();
    }

    @Test
    public void testBasicVarDeclarationWithAssignment() {
        LexerStream stream = lexer.lex("var a = 1;");
        Token token;

        token = stream.next();
        assert token.getType() == TokenType.VAR;
        token = stream.next();
        assert token.getType() == TokenType.NAME;
        assert token.getValue() instanceof String;
        assert token.getValue().equals("a");
        token = stream.next();
        assert token.getType() == TokenType.ASSIGN;
        token = stream.next();
        assert token.getType() == TokenType.NUMBER;
        assert token.getValue() instanceof String;
        assert token.getValue().equals("1");
        token = stream.next();
        assert token.getType() == TokenType.SEMICOLON;

        token = stream.next();
        assert token == null;
    }

    @Test
    public void testBasicIfWithoutBody() {
        LexerStream stream = lexer.lex("if (a == 1) { }");
        Token token;

        token = stream.next();
        assert token.getType() == TokenType.IF;
        token = stream.next();
        assert token.getType() == TokenType.LPAREN;
        token = stream.next();
        assert token.getType() == TokenType.NAME;
        assert token.getValue() instanceof String;
        assert token.getValue().equals("a");
        token = stream.next();
        assert token.getType() == TokenType.EQUAL;
        token = stream.next();
        assert token.getType() == TokenType.NUMBER;
        assert token.getValue() instanceof String;
        assert token.getValue().equals("1");
        token = stream.next();
        assert token.getType() == TokenType.RPAREN;
        token = stream.next();
        assert token.getType() == TokenType.LBRACE;
        token = stream.next();
        assert token.getType() == TokenType.RBRACE;

        token = stream.next();
        assert token == null;
    }

    @Test
    public void testIfElseWithoutBody() {
        LexerStream stream = lexer.lex("if (a == 3) { } else { }");
        Token token;

        token = stream.next();
        assert token.getType() == TokenType.IF;
        token = stream.next();
        assert token.getType() == TokenType.LPAREN;
        token = stream.next();
        assert token.getType() == TokenType.NAME;
        assert token.getValue() instanceof String;
        assert token.getValue().equals("a");
        token = stream.next();
        assert token.getType() == TokenType.EQUAL;
        token = stream.next();
        assert token.getType() == TokenType.NUMBER;
        assert token.getValue() instanceof String;
        assert token.getValue().equals("3");
        token = stream.next();
        assert token.getType() == TokenType.RPAREN;
        token = stream.next();
        assert token.getType() == TokenType.LBRACE;
        token = stream.next();
        assert token.getType() == TokenType.RBRACE;
        token = stream.next();
        assert token.getType() == TokenType.ELSE;
        token = stream.next();
        assert token.getType() == TokenType.LBRACE;
        token = stream.next();
        assert token.getType() == TokenType.RBRACE;

        token = stream.next();
        assert token == null;
    }

    @Test
    public void testBasicFunctionCall() {
        LexerStream stream = lexer.lex("print(\"test\")");
        Token token;

        token = stream.next();
        assert token.getType() == TokenType.NAME;
        assert token.getValue() instanceof String;
        assert token.getValue().equals("print");
        token = stream.next();
        assert token.getType() == TokenType.LPAREN;
        token = stream.next();
        assert token.getType() == TokenType.DQUOTE;
        token = stream.next();
        assert token.getType() == TokenType.NAME;
        assert token.getValue() instanceof String;
        assert token.getValue().equals("test");
        token = stream.next();
        assert token.getType() == TokenType.DQUOTE;
        token = stream.next();
        assert token.getType() == TokenType.RPAREN;

        token = stream.next();
        assert token == null;
    }

}
