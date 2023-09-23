//imports for antlr libraries 
import org.antlr.v4.runtime.*;
import java.io.IOException;

public class Driver {

    static class ErrorCheck extends BaseErrorListener {
        private static boolean error = false;

        public void syntaxError(Recognizer<?, ?> recognizer,
                                Object offendingSymbol,
                                int line, int charPositionInLine,
                                String msg,
                                RecognitionException e) {
            error = true;

        }
        public static boolean getError(){
            return error;
        }
    }
    
    //thows exception incase cant find file
    public static void main(String[] args) throws IOException {

        //this tells ANTLR the input and what the lexer should analyize 
        ANTLRInputStream input = new ANTLRInputStream(System.in);
        LittleLexer  lexer = new LittleLexer (input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LittleParser parser = new LittleParser(tokens);
        parser.addErrorListener(new ErrorCheck());

        ParseTree tree = parser.program();



         if (ErrorCheck.getError() == false) {
                System.out.println("Accepted");
            } else {
            System.out.println("Not accepted");
        }

        //function call for printing 
       // printTokens(lexer);
    }

    //Function no longer in use right now 
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
