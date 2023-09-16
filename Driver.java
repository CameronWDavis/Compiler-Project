import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Token;
import java.io.IOException;

public class Driver {
    public static void main(String[] args) throws IOException {
        ANTLRInputStream input = new ANTLRInputStream(System.in);
        LittleLexer  lexer = new LittleLexer (input);

        processTokens(lexer);
    }

    private static void processTokens(Little lexer) {
        for (Token token = lexer.nextToken(); token.getType() != Token.EOF; token = lexer.nextToken()) {
            String tokenName = Little.VOCABULARY.getDisplayName(token.getType());
            String tokenValue = token.getText();

            System.out.println("Token Type: " + tokenName);
            System.out.println("Value: " + tokenValue);
        }
    }
}
