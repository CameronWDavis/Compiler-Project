//imports for antlr libraries
import org.antlr.v4.runtime.*;
import java.io.IOException;
import org.antlr.v4.runtime.tree.*;


public class Driver {

//this class is used to see if the inputed file is valid or not
    static class ErrorCheck extends BaseErrorListener {
        private static boolean error = false;

        public void syntaxError(Recognizer<?, ?> recognizer,
                                Object offendingSymbol,
                                int line, 
                                int charPositionInLine,
                                String msg,
                                RecognitionException e) {
            error = true;

        }
        public static boolean getError(){
            return error;
        }
    }

public static void  main(String[] args) throws IOException {

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
}
}
