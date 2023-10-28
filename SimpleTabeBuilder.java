import java.util.*;

public class SimpleTableBuilder extends LittleBaseListener {

    ArrayList<SymbolTable> stack = new ArrayList<SymbolTable>();
    SymbolTable table = new SymbolTable("temp");

    private Set<String> scopes = new HashSet<>();

    int blockNum = 1;

    public boolean addStack(SymbolTable table) {
        if (!scopes.contains(table.getScope())) {
            stack.add(table);
            scopes.add(table.getScope());
            return true;
        }
        return false;
    }

    @Override public void enterProgram(LittleParser.ProgramContext ctx)
    {
        SymbolTable global_table = new SymbolTable("GLOBAL");
        table = global_table;
        addStack(table);
    }
    @Override public void enterString_decl(LittleParser.String_declContext ctx)
    {
        table.addSymbol(new Symbol(ctx.id().getText(), "STRING", ctx.str().getText()));
    }

    @Override public void enterVar_decl(LittleParser.Var_declContext ctx)
    {
        String type = ctx.var_type().getText();
        String[] identifiers = ctx.id_list().getText().split(",");

        if ("INT".equals(type) || "FLOAT".equals(type)) {
            for (String identifier : identifiers) {
                Symbol symbol = new Symbol(identifier.trim(), type);
                table.addSymbol(symbol);
            }
        }
    }

    public String getBlkName() {
        return "BLOCK " + blockNum++;
    }

    @Override public void enterIf_stmt(LittleParser.If_stmtContext ctx) {
        SymbolTable blockTable = new SymbolTable(getBlkName());
        if (addStack(blockTable)) {
            table = blockTable;
        } else {
            System.err.println("Error: A symbol table with the scope '" + blockTable.getScope() + "' already exists.");
        }
    }

    @Override public void exitIf_stmt(LittleParser.If_stmtContext ctx) {
        if(addStack(table)) {
            table = stack.get(stack.size() - 2);
        }
        else {
            table = stack.get(stack.size() - 1);
        }
    }

    @Override
    public void enterElse_part(LittleParser.Else_partContext ctx) {
        table = addStack(table) ? stack.get(stack.size() - 2) : stack.get(stack.size() - 1);
        table = new SymbolTable(getBlkName());
    }

    @Override public void exitElse_part(LittleParser.Else_partContext ctx) {

    }

    @Override public void enterWhile_stmt(LittleParser.While_stmtContext ctx) {
        table = addStack(table) ? stack.get(stack.size() - 2) : stack.get(stack.size() - 1);
        table = new SymbolTable(getBlkName());
    }

    @Override public void exitWhile_stmt(LittleParser.While_stmtContext ctx) {
        if(addStack(table)) {
            table = stack.get(stack.size() - 2);
        }
        else {
            table = stack.get(stack.size() - 1);
        }

    }

    @Override public void enterFunc_decl(LittleParser.Func_declContext ctx) {

        if (addStack(table)) {
            table = stack.get(stack.size() - 1);
        }
        String name = ctx.id().getText();
        SymbolTable func_table = new SymbolTable(name);
        table = func_table;
    }

    @Override public void exitFunc_decl(LittleParser.Func_declContext ctx) {
        if(addStack(table)) {
            table = stack.get(stack.size() - 2);
        }
        else {
            table = stack.get(stack.size() - 1);
        }
    }

    @Override public void enterParam_decl(LittleParser.Param_declContext ctx) {
        String type = ctx.var_type().getText();
        String name = ctx.id().getText();
        Symbol symbol = new Symbol(name, type);
        table.addSymbol(symbol);
    }

    @Override public void exitParam_decl(LittleParser.Param_declContext ctx) {

    }
    public void prettyPrint() {
        for (SymbolTable currentTable : stack) {
            currentTable.printTable();
            System.out.println();
        }
    }
}
