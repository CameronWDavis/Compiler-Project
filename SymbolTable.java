public class SymbolTable {
    private final LinkedHashMap<String, Symbol> symbols = new LinkedHashMap<>();
    private String scope;

    public SymbolTable(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }

    public void addSymbol(Symbol symbol) {
        if (symbols.containsKey(symbol.toString())) {
            throw new IllegalArgumentException("DECLARATION ERROR " + symbol);
        }
        symbols.put(symbol.toString(), symbol);
    }

    public void printTable() {
        System.out.println("Symbol table " + scope);
        for (Symbol symbol : symbols.values()) {
            System.out.println(symbol);
        }
    }
}
