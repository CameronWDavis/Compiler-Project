//imports for antlr libraries 
import org.antlr.v4.runtime.*;
import java.io.IOException;

public class Driver {
    //thows exception incase cant find file
    public static void main(String[] args) throws IOException {

        //this tells ANTLR the input and what the lexer should analyize 
        ANTLRInputStream input = new ANTLRInputStream(System.in);
        LittleLexer  lexer = new LittleLexer (input);

        //function call for printing 
        printTokens(lexer);
    }

    
    private static void printTokens(LittleLexer lexer) {
        //for loop to print token and type
        for (Token token = lexer.nextToken(); token.getType() != Token.EOF; token = lexer.nextToken()) {
            String tokenName = Little.VOCABULARY.getDisplayName(token.getType());
            String tokenValue = token.getText();

            System.out.println("Token Type: " + tokenName);
            System.out.println("Value: " + tokenValue);
        }
    }
}
