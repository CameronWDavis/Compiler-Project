import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import java.io.FileInputStream;
import java.io.File;
import org.antlr.v4.runtime.Token;

public class Driver {
    public static void main(String args[]) throws Exception {

        String fileName = "input.txt";
        File file = new File(fileName);
        FileInputStream fis = null;

        fis = new FileInputStream(file);

        ANTLRInputStream input = new ANTLRInputStream(fis);

        //lexical analyzer
        LittleLexer lexer = new LittleLexer(input);

        //token generation
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Generate tokens
        tokens.fill();
        printTokens(tokens,lexer);
    }

         //This is a function to print tokens created by the lexical rules
        private static void printTokens(CommonTokenStream tokens,LittleLexer lexer){
            for (Token token : tokens.getTokens()) {
                String tokenName = LittleLexer.VOCABULARY.getSymbolicName(token.getType());
                String tokenValue = token.getText();

                System.out.println("Token Type: " + tokenName);
                System.out.println("Value: " + tokenValue);
            }
        }


}
