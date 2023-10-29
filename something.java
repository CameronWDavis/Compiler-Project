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
