//imports for antlr libraries
import org.antlr.v4.runtime.*;
import java.io.IOException;
import org.antlr.v4.runtime.tree.*;


public class Driver {

public static void  main(String[] args) throws IOException {

//this tells ANTLR the input and what the lexer should analyize 
        ANTLRInputStream input = new ANTLRInputStream(System.in);

	//creation of lexer this turns text into lexical tokens 
        LittleLexer  lexer = new LittleLexer (input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

	//creation of parser this turns our tokens into formal grammar also sees if file is valid
        LittleParser parser = new LittleParser(tokens);
        ParseTree tree = parser.program();
	ParseTreeWalker walker = new ParseTreeWalker(); //parse the program 

	//creation of symbol table this gives our words meaning 
	IRCode irGen = new IRCode(); // create our IR object 
            walker.walk(irGen,tree);
            irGen.prettyPrint(); // print hte IR codes 
            irGen.printTiny(); // print the tiny code
	
}
}
