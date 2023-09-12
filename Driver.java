import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

public class Driver {

    public static void main(String[] args) {
        //this reads in standard input
        ANTLRInputStream input = new ANTLRInputStream(System.in);

        //lexical analyzer
        LittleLexer lexer = new LittleLexer(input);

        //token generation
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        
    }

    private static void outputTokens(lexer,tokens){
        int i = 0;
        while(tokens[0] != null){
            System.out.println("Token Type: " + LittleLexer.VOCABULARY.getDisplayName(tokens.getType()));
            System.out.println("Value: " + tokens.getText());
            tokens.consume();
            i++; //iterate over
        }
    }

}
