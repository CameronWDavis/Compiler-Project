import java.util.*;

class SymbolTable { //this class creates a symbol table using a hashmap

    //class variables
    public String scope;
    public Map<String, Map<String, String>> table;

    public String getScope(){
        return this.scope; // method ot return scope
    }

    public SymbolTable(String scope) { //constructor for symbole table
        this.scope = scope;
        this.table = new LinkedHashMap<>();
    }

    public void addSymbol(String name, String type) {// add a symbol to ttable
        addSymbol(name, type, null);
    }

    public void addSymbol(String name, String type, String value) { //add symbol and throw exception if needed
        if (table.containsKey(name)) {
            System.out.println("DECLARATION ERROR " + name);
            throw new IllegalArgumentException("DECLARATION ERROR " + name);
        } else {
            Map<String, String> symbolProperties = new LinkedHashMap<>();
            symbolProperties.put("type", type);
            symbolProperties.put("value", value);
            table.put(name, symbolProperties);
        }
    }


    //print the table out
    public void printTable() {
        System.out.println("Symbol table " + this.scope);
        for (Map.Entry<String, Map<String, String>> entry : table.entrySet()) {
            String name = entry.getKey();
            Map<String, String> properties = entry.getValue();
            String type = properties.get("type");
            String value = properties.get("value");
            if ("STRING".equals(type)) {
                System.out.println("name " + name + " type " + type + " value " + value);
            } else {
                System.out.println("name " + name + " type " + type);
            }
        }
    }
} // end of SymboleTable

public class SimpleTableBuilder extends LittleBaseListener {
    //class variables
    ArrayList <SymbolTable> stack = new ArrayList<SymbolTable>();
    SymbolTable table = new SymbolTable("temp");
    int blockCount = 1 ;

    //add a table tp the stack
    public boolean pushStack(SymbolTable table) {
        if (stack.stream().noneMatch(tempTable -> tempTable.scope.equals(table.scope))) {
            stack.add(table);
            return true;
        }
        return false;
    }

    public void retrieveTableState(){ //method to get the state of the table 
        if(pushStack(table)){
            table = stack.get(stack.size() - 2);
        }else{
            table = stack.get(stack.size() - 1 );
        }
    }


    @Override public void enterProgram(LittleParser.ProgramContext ctx){
        SymbolTable globalTable = new SymbolTable("GLOBAL");
        table = globalTable;
        pushStack(table);
    }

    @Override public void exitProgram(LittleParser.ProgramContext ctx) { //nothing else needed
         }

    @Override public void enterFunc_decl(LittleParser.Func_declContext ctx) {
        retrieveTableState();
        String name = ctx.id().getText();
        SymbolTable funcTable = new SymbolTable(name);
        table = funcTable;//create table for function
    }

    @Override public void exitFunc_decl(LittleParser.Func_declContext ctx) {
        retrieveTableState();
    }
    
    @Override
    public void enterString_decl(LittleParser.String_declContext ctx) {
        String name = ctx.id().getText();
        String type = "STRING";
        String value = ctx.str().getText();
        table.addSymbol(name, type,value);
    }

    @Override
    public void exitString_decl(LittleParser.String_declContext ctx){
    //empty not needed
    }


    @Override
    public void enterVar_decl(LittleParser.Var_declContext ctx) {
        String type = ctx.var_type().getText();
        if ("INT".equals(type) || "FLOAT".equals(type)) {
            String[] variableNames = ctx.id_list().getText().split(",");
            for (String variableName : variableNames) {
                table.addSymbol(variableName.trim(), type);
            }
        }
    }

    @Override public void exitVar_decl(LittleParser.Var_declContext ctx) { }

    @Override public void enterParam_decl(LittleParser.Param_declContext ctx) {
        String type = ctx.var_type().getText();
        String name = ctx.id().getText();
        table.addSymbol(name, type );
    }

    @Override public void exitParam_decl(LittleParser.Param_declContext ctx) { }

    @Override public void enterIf_stmt(LittleParser.If_stmtContext ctx){
        if(pushStack(table)){
            table = stack.get(stack.size() - 2);
        } else {
            table = stack.get(stack.size() - 1);
            if(blockCount > 1){
                blockCount--;
            }
        }
        SymbolTable blockTable = new SymbolTable("BLOCK " + blockCount++);
        table = blockTable;
    }

    @Override public void exitIf_stmt(LittleParser.If_stmtContext ctx){
        retrieveTableState();
    }

    @Override public void enterElse_part(LittleParser.Else_partContext ctx){
        retrieveTableState();
        SymbolTable blockTable = new SymbolTable("BLOCK " + blockCount++);
        table = blockTable;
    }

    @Override public void exitElse_part(LittleParser.Else_partContext ctx) { }

    @Override public void enterWhile_stmt(LittleParser.While_stmtContext ctx) {
        retrieveTableState();
        SymbolTable blockTable = new SymbolTable("BLOCK " + blockCount++);
        table = blockTable;
    }

    @Override public void exitWhile_stmt(LittleParser.While_stmtContext ctx) {
        retrieveTableState();
    }

    public void prettyPrint(){ //print the table after its been processed
        for (SymbolTable currentTable : stack){
            currentTable.printTable();
            System.out.println();
        }
    }
}//end Simpletablebuilder 
