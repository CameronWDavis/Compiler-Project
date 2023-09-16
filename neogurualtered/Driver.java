import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Token;
import java.io.IOException;

public class Driver {
    public static void main(String[] args) throws IOException {
        ANTLRInputStream input = new ANTLRInputStream(System.in);

        Little lexer = new Little(input);

        Token token;

        while ((token = lexer.nextToken()).getType() != Token.EOF) {

            String tokenName = Little.VOCABULARY.getDisplayName(token.getType());

            String tokenValue = token.getText();

            System.out.println("Token Type: " + tokenName);
            System.out.println("Value: " + tokenValue);
        }
    }
}

//import org.antlr.v4.runtime.ANTLRInputStream;
//import org.antlr.v4.runtime.CommonTokenStream;
//import org.antlr.v4.runtime.Token;
//
//public class Driver {
//
//    public static void main(String[] args) throws Exception {
//        ANTLRInputStream input = new ANTLRInputStream(System.in);
//
//        Little lexer = new Little(input);
//
//        CommonTokenStream tokens = new CommonTokenStream(lexer);
//
//        printTokens(tokens);
//    }
//
//    private static void printTokens(CommonTokenStream tokens) {
//        for (Token token : tokens.getTokens()) {
//            String tokenName = Little.VOCABULARY.getSymbolicName(token.getType());
//            String tokenValue = token.getText();
//
//            System.out.println("Token Type: " + tokenName);
//            System.out.println("Value: " + tokenValue);
//        }
//    }
//}

// import org.antlr.v4.runtime.ANTLRInputStream;
// import org.antlr.v4.runtime.CommonTokenStream;
// import org.antlr.v4.runtime.Token;
// import org.antlr.v4.runtime.*;
// import java.io.IOException;
//
// public class Driver {
//
//     public static void main(String[] args)throws Exception {
//         //this reads in standard input
//         ANTLRInputStream input = new ANTLRInputStream(System.in);
//
//         //lexical analyzer
//         Little lexer = new Little(input);
//
//         //token generation
//         CommonTokenStream tokens = new CommonTokenStream(lexer);
//
//         printTokens(tokens, lexer);
//
//     }
//
//     private static void printTokens(CommonTokenStream tokens, Little lexer) {
//         for (Token token : tokens.getTokens()) {
//             String tokenName = Little.VOCABULARY.getSymbolicName(token.getType());
//             String tokenValue = token.getText();
//
//             System.out.println("Token Type: " + tokenName);
//             System.out.println("Value: " + tokenValue);
//         }
//
//     }
// }