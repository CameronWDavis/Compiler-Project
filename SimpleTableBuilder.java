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

    ArrayList<SymbolTable> stack = new ArrayList<>();
    SymbolTable table = new SymbolTable("final");

    int countBlock = 1;

    public boolean addStack(SymbolTable table) {
        for (SymbolTable symTable : stack) {
            if (symTable.getScope().equals(table.getScope())) {
                return false;
            }
        }
        stack.add(table);
        return true;
    }

    @Override
    public void enterProgram(LittleParser.ProgramContext ctx) {
        SymbolTable globalTable = new SymbolTable("GLOBAL");
        table = globalTable;
        addStack(globalTable);
    }

    @Override
    public void enterString_decl(LittleParser.String_declContext ctx) {
        String name = ctx.id().getText();
        String type = "STRING";
        String value = ctx.str().getText();
        table.addSymbol(name, type, value);
    }

    @Override
    public void enterVar_decl(LittleParser.Var_declContext ctx) {
        String type = ctx.var_type().getText();
        String[] names = ctx.id_list().getText().split(",");
        for (String name : names) {
            table.addSymbol(name, type);
        }
    }

    public String getBlkName() {
        return "BLOCK " + countBlock++;
    }

    @Override
    public void enterIf_stmt(LittleParser.If_stmtContext ctx) {
        SymbolTable blockTable = new SymbolTable(getBlkName());
        table = blockTable;
        addStack(blockTable);
    }

    @Override
    public void exitIf_stmt(LittleParser.If_stmtContext ctx) {
        table = stack.get(stack.size() - 2);
    }

    @Override
    public void enterElse_part(LittleParser.Else_partContext ctx) {
        SymbolTable blockTable = new SymbolTable(getBlkName());
        table = blockTable;
        addStack(blockTable);
    }

    @Override
    public void exitElse_part(LittleParser.Else_partContext ctx) {
        table = stack.get(stack.size() - 2);
    }

    @Override
    public void enterWhile_stmt(LittleParser.While_stmtContext ctx) {
        SymbolTable blockTable = new SymbolTable(getBlkName());
        table = blockTable;
        addStack(blockTable);
    }

    @Override
    public void exitWhile_stmt(LittleParser.While_stmtContext ctx) {
        table = stack.get(stack.size() - 2);
    }

    @Override
    public void enterFunc_decl(LittleParser.Func_declContext ctx) {
        String name = ctx.id().getText();
        SymbolTable funcTable = new SymbolTable(name);
        table = funcTable;
        addStack(funcTable);
    }

    @Override
    public void exitFunc_decl(LittleParser.Func_declContext ctx) {
        table = stack.get(stack.size() - 2);
    }

    @Override
    public void enterParam_decl(LittleParser.Param_declContext ctx) {
        String type = ctx.var_type().getText();
        String name = ctx.id().getText();
        table.addSymbol(name, type);
    }

    public void prettyPrint() {
        for (SymbolTable currentTable : stack) {
            currentTable.printTable();
            System.out.println();
        }
    }
}
