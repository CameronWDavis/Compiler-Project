import java.util.*;

class SymbolTable {
    public String scope;
    public Map<String, Map<String, String>> table;

    public String getScope(){
        return this.scope;
    }

    public SymbolTable(String scope) {
        this.scope = scope;
        this.table = new LinkedHashMap<>();
    }

    public void addSymbol(String name, String type) {
        addSymbol(name, type, null);
    }

    public void addSymbol(String name, String type, String value) {
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
}

public class SimpleTableBuilder extends LittleBaseListener {
    ArrayList <SymbolTable> stack = new ArrayList<SymbolTable>();
    SymbolTable table = new SymbolTable("temp");
    int blockCount = 1 ;


    public boolean pushStack(SymbolTable table) {
        if (stack.stream().noneMatch(tempTable -> tempTable.scope.equals(table.scope))) {
            stack.add(table);
            return true;
        }
        return false;
    }


    @Override public void enterProgram(LittleParser.ProgramContext ctx){
        SymbolTable globalTable = new SymbolTable("GLOBAL");
        table = globalTable;
        pushStack(table);
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

    }


    @Override public void enterVar_decl(LittleParser.Var_declContext ctx){
        String type = ctx.var_type().getText();
        if(type.compareTo("INT") == 0 ){
            String name = ctx.var_type().getText();
            String[] intValue = ctx.id_list().getText().split(",");
            for(int i = 0; i < intValue.length; i++){
                table.addSymbol(intValue[i], type);
            }
        }
        else if (type.compareTo("FLOAT") == 0) {
            String name = ctx.id_list().getText();
            String[] intValue = ctx.id_list().getText().split(",");
            for(int i = 0; i < intValue.length; i++){
                table.addSymbol(intValue[i], type);
            }
        }
    }

    @Override public void enterParam_decl(LittleParser.Param_declContext ctx) {
        String type = ctx.var_type().getText();
        String name = ctx.id().getText();
        table.addSymbol(name, type );
    }

    @Override public void exitParam_decl(LittleParser.Param_declContext ctx) { }

    public String getBlock() {
        return "BLOCK " + blockCount++;
    }


    @Override public void enterIf_stmt(LittleParser.If_stmtContext ctx){
        if(pushStack(table)){
            table = stack.get(stack.size() - 2);
        } else {
            table = stack.get(stack.size() - 1);
            if(blockCount > 1){
                blockCount--;
            }
        }
        SymbolTable blockTable = new SymbolTable(getBlock());
        table = blockTable;
    }

    @Override public void exitIf_stmt(LittleParser.If_stmtContext ctx){
        if(pushStack(table)) {
            table = stack.get(stack.size() - 2);
        }else {
            table = stack.get(stack.size() - 1);
        }
    }

    @Override public void enterElse_part(LittleParser.Else_partContext ctx){
        if(pushStack(table)){
            table = stack.get(stack.size() - 2);
        }else{
            table = stack.get(stack.size() - 1 );
        }
        SymbolTable blockTable = new SymbolTable(getBlock());
        table = blockTable;
    }

    @Override public void exitElse_part(LittleParser.Else_partContext ctx) { }

    @Override public void enterWhile_stmt(LittleParser.While_stmtContext ctx) {
        if(pushStack(table)){
            table = stack.get(stack.size() - 2);
        }else {
            table = stack.get(stack.size() - 1);
        }
        SymbolTable blockTable = new SymbolTable(getBlock());
        table = blockTable;
    }

    @Override public void exitWhile_stmt(LittleParser.While_stmtContext ctx) {
        if(pushStack(table)){
            table = stack.get(stack.size() - 2);
        }else {
            table = stack.get(stack.size() - 1);
        }
    }

    @Override public void enterFunc_decl(LittleParser.Func_declContext ctx) {
        if(pushStack(table)){
            table = stack.get(stack.size() - 2);
        }else {
            table = stack.get(stack.size() - 1);
        }
        String name = ctx.id().getText();
        SymbolTable funcTable = new SymbolTable(name);
        table = funcTable;
    }

    @Override public void exitFunc_decl(LittleParser.Func_declContext ctx) {
        if(pushStack(table)){
            table = stack.get(stack.size() - 2);
        }else {
            table = stack.get(stack.size() - 1);
        }

    }

    public void prettyPrint(){
        for (SymbolTable currentTable : stack){
            currentTable.printTable();
            System.out.println();
        }
    }

}
