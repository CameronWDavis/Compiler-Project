import java.util.*;

//this class creates a symbol table using a hashmap
class SymbolTable {
    public String scope;
    public Map<String, Map<String, String>> table;
    public List<String> irCodes; // List to store IR codes

    public SymbolTable(String scope) {
        this.scope = scope;
        this.table = new LinkedHashMap<>();
        this.irCodes = new ArrayList<>();
    }

    public void addIRCode(String code) {
        irCodes.add(code);
    }

    public void printIRCodes() {
        for (String code : irCodes) {
            System.out.println(code);
        }
    }


} // end of SymboleTable

public class SimpleTableBuilder extends LittleBaseListener {

    private int tempVarCount = 0;
    private boolean isFloat = false;
    LinkedList<SymbolTable> stack = new LinkedList<>();
    SymbolTable currentTable;

    private String newTempVar() {
        return "$T" + (++tempVarCount);
    }

    @Override
    public void enterProgram(LittleParser.ProgramContext ctx) {
        SymbolTable globalTable = new SymbolTable("GLOBAL");
        currentTable = globalTable;
        stack.push(globalTable);

        // Add initial IR codes to the global table
        currentTable.addIRCode(";IR code");
        currentTable.addIRCode(";LABEL main");
        currentTable.addIRCode(";LINK");
    }

    private Map<String, String> stringDeclarations = new HashMap<>();

    @Override
    public void enterString_decl(LittleParser.String_declContext ctx) {
        String varName = ctx.id().getText();
        String value = ctx.str().getText();
        stringDeclarations.put(varName, value);
    }

    @Override
    public void exitProgram(LittleParser.ProgramContext ctx) {
        currentTable.addIRCode(";RET");
        currentTable.addIRCode(";tiny code");
    }

    @Override
    public void enterFunc_dec1(LittleParser.Func_declContext ctx) {
        
    }


    @Override
    public void enterVar_decl(LittleParser.Var_declContext ctx) {
        String type = ctx.var_type().getText();
        if ("INT".equals(type) ){
            isFloat = false;
        }else if("FLOAT".equals(type)){
            isFloat = true;
        }
    }

    @Override
    public void enterAssign_stmt(LittleParser.Assign_stmtContext ctx) {
        String variableName = ctx.assign_expr().id().getText();
        String expression = ctx.assign_expr().expr().getText();

        String tempVar = newTempVar();
        if (isFloat == false) {
            if (expression.matches("\\d+")) {
                // Direct numeric assignment
                currentTable.addIRCode(";STOREI " + expression + " " + tempVar);
            } else if (expression.contains("*") || expression.contains("+") || expression.contains("-") || expression.contains("/")) {
                // Handle arithmetic operations
                String operator = expression.contains("*") ? "*" : expression.contains("+") ? "+" : expression.contains("-") ? "-" : "/";
                String[] operands = expression.split("[\\*\\+\\-/]");
                String opCode = operator.equals("*") ? "MULTI" : operator.equals("+") ? "ADDI" : operator.equals("-") ? "SUBI" : "DIVI";
                currentTable.addIRCode(";" + opCode + " " + operands[0].trim() + " " + operands[1].trim() + " " + tempVar);
            }
            // Store the result in the variable
            currentTable.addIRCode(";STOREI " + tempVar + " " + variableName);
        }
        if (isFloat == true) {
            if (expression.matches("-?\\d+(\\.\\d+)?")) { // Regex for float literals
                // Direct float assignment
                currentTable.addIRCode(";STOREF " + expression + " " + tempVar);
            } else if (expression.contains("*") || expression.contains("+") || expression.contains("-") || expression.contains("/")) {
                // Handle arithmetic operations for floats
                String operator = expression.contains("*") ? "*" : expression.contains("+") ? "+" : expression.contains("-") ? "-" : "/";
                String[] operands = expression.split("[\\*\\+\\-/]");
                String opCode = operator.equals("*") ? "MULTF" : operator.equals("+") ? "ADDF" : operator.equals("-") ? "SUBF" : "DIVF";
                currentTable.addIRCode(";" + opCode + " " + operands[0].trim() + " " + operands[1].trim() + " " + tempVar);
            }
            // Store the result in the variable
            currentTable.addIRCode(";STOREF " + tempVar + " " + variableName);
        }
    }

    @Override
    public void enterRead_stmt(LittleParser.Read_stmtContext ctx) {
        String idList = ctx.id_list().getText();
        String[] ids = idList.split(",");

        for (String id : ids) {
            String variableName = id.trim();
            currentTable.addIRCode(";READI " + variableName);
        }
    }
    @Override
    public void enterWrite_stmt(LittleParser.Write_stmtContext ctx) {
        String itemList = ctx.id_list().getText(); // Get the comma-separated list of items
        String[] items = itemList.split(","); // Split the list into individual items

        for (String item : items) {
            item = item.trim(); // Trim the item to remove any whitespace
            if ("newline".equals(item)) {
                currentTable.addIRCode(";WRITES newline");
            } else if(isFloat == false){
                currentTable.addIRCode(";WRITEI " + item);
            }else{
                currentTable.addIRCode(";WRITEF " + item);
            }
        }
    }


    public void prettyPrint() {
        for (SymbolTable table : stack) {
            //table.printTable();
            table.printIRCodes();
        }
    }

    Compiler compiler = new Compiler(stack,stringDeclarations);

    public void printTiny(){
        ArrayList<String> tinyCode = compiler.Compile();
        for (String line : tinyCode) {
            System.out.println(line);
        }
    }
    public class Compiler {

        private LinkedList<SymbolTable> stack;
        Map<String, String> registerMap = new HashMap<>();
        private int registerCount = 0;

        private String mapToRegister(String irVariable, Map<String, String> registerMap) {
            if (irVariable.startsWith("$T")) {
                return registerMap.computeIfAbsent(irVariable, k -> "r" + registerCount++);
            }
            return irVariable;
        }

        public Compiler(LinkedList<SymbolTable> stack,Map<String, String> stringDeclarations) {
            this.stack = stack;
        }

        public ArrayList<String> Compile() {
            ArrayList<String> tinyCode = new ArrayList<>();
            Map<String, String> registerMap = new HashMap<>();
            Set<String> variables = new HashSet<>();

            for (SymbolTable table : stack) {
                for (String line : table.irCodes) {
                    String[] parts = line.split(" ");
                    if (parts.length > 1) {
                        String potentialVar = parts[parts.length - 1];
                        if (!potentialVar.startsWith("$T") && !potentialVar.matches("\\d+")) {
                            variables.add(potentialVar);
                        }
                    }
                }
            }



            for (String var : variables) {
                if(var.length() == 1){
                    tinyCode.add("var " + var);
                }
            }

            for (Map.Entry<String, String> entry : stringDeclarations.entrySet()) {
                tinyCode.add("str " + entry.getKey() + " " + entry.getValue());
            }

            // Process IR codes and generate Tiny code
            for (SymbolTable table : stack) {
                for (String line : table.irCodes) {
                    String[] parts = line.split(" ");
                    String instruction = parts[0].substring(1); // Remove leading ";"
                    String tinyInstruction = "";

                    switch (instruction) {
                        case "STOREI":
                        case "STOREF":
                            String src = parts[1];
                            String dest = parts[2];

                            if (src.contains("$T")) {
                                src = "r" + (Integer.parseInt(src.substring(2)) - 1); // Adjust for zero-based indexing
                            } else if (dest.contains("$T")) {
                                dest = "r" + registerCount++;
                            }

                            tinyInstruction = "move " + src + " " + dest;
                            break;
                        case "READF":
                        case "READI":
                            tinyInstruction = "sys readi " + parts[1];
                            break;
                        case "WRITEF":
                            tinyInstruction = "sys writer " + parts[1];
                            break;
                        case "WRITEI":
                            tinyInstruction = "sys writei " + parts[1];
                            break;
                        case "WRITES":
                            if (parts[1].equals("newline")) {
                                tinyInstruction = "sys writes newline";
                            }
                            break;
                        case "MULTI":
                            String operand1 = mapToRegister(parts[1].replaceAll("[()]", ""), registerMap);
                            String operand2 = mapToRegister(parts[2].replaceAll("[()]", ""), registerMap);
                            String result = mapToRegister(parts[3].replaceAll("[()]", ""), registerMap);
                            tinyCode.add("move " + operand1 + " " + result);
                            tinyCode.add("muli " + operand2 + " " + result);
                            break;
                        case "MULTF":
                            operand1 = mapToRegister(parts[1].replaceAll("[()]", ""), registerMap);
                            operand2 = mapToRegister(parts[2].replaceAll("[()]", ""), registerMap);
                            result = mapToRegister(parts[3].replaceAll("[()]", ""), registerMap);
                            tinyCode.add("move " + operand1 + " " + result);
                            tinyCode.add("mulf " + operand2 + " " + result);
                            break;
                        case "ADDI":
                        case "SUBI":
                        case "DIVI":
                            // Map temporary variables to registers
                             operand1 = mapToRegister(parts[1].replaceAll("[()]", ""), registerMap);
                             operand2 = mapToRegister(parts[2].replaceAll("[()]", ""), registerMap);
                             result = mapToRegister(parts[3].replaceAll("[()]", ""), registerMap);

                            // Generate tiny code for the operation
                            String opCode = instruction.substring(0, 4).toLowerCase();
                            tinyCode.add("move " + operand1 + " " + result);
                            tinyCode.add(opCode + " " + operand2 + " " + result);
                            break;
                        case "DIVF":
                        case "SUBF":
                        case "ADDF":
                            // Map temporary variables to registers
                            operand1 = mapToRegister(parts[1].replaceAll("[()]", ""), registerMap);
                            operand2 = mapToRegister(parts[2].replaceAll("[()]", ""), registerMap);
                            result = mapToRegister(parts[3].replaceAll("[()]", ""), registerMap);

                            // Generate tiny code for the operation
                            opCode = instruction.substring(0, 3).toLowerCase() + "r";
                            tinyCode.add("move " + operand1 + " " + result);
                            tinyCode.add(opCode + " " + operand2 + " " + result);
                            break;
                    }

                    if (!tinyInstruction.isEmpty()) {
                        tinyCode.add(tinyInstruction);
                    }
                }
            }


            tinyCode.add("sys halt");
            return tinyCode;
        }
    }
}
