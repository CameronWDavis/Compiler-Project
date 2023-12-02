import java.util.ArrayList;



public class AST extends LittleBaseListener
{
    //Node class since wer are using a binary tree with left and right nodes
   private class Node{
        String name;
        Node left, right;

        public Node(){}

        public Node(String value) {
            this.name = value;
        }

        public void setValue(String value){
            this.name = value;
        }

        public void addLeft(Node left){
            this.left = left;
        }

        public void addRight(Node right){
            this.right = right;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (this.name != null) {
                sb.append(this.name);
            }
            if (this.left != null) {
                sb.append(this.left);
            }
            if (this.right != null) {
                sb.append(this.right);
            }
            return sb.toString();
        }
    }// end of Node class

    ArrayList<Node> ast = new ArrayList<>();
    Node current;
    @Override
    public void enterFunc_dec1(LittleParser.Func_declContext ctx){
        Node leaf = new Node("FUNCTION(" + ctx.id().getText() + ")");
        current = leaf;
        ast.add(current);
    }

    @Override public void exitFunc_decl(LittleParser.Func_declContext ctx) {
        Node node = new Node("RETURN");
        current = node;
        ast.add(current);
    }
    @Override public void enterString_decl(LittleParser.String_declContext ctx)
    {
        Node node = new Node("=");
        current = node;
        Node left = new Node("STRING(" + ctx.id().getText() + ")");
        Node right = new Node(ctx.str().getText());
        node.addLeft(left);
        node.addRight(right);
        ast.add(current);
    }

    @Override public void enterVar_decl(LittleParser.Var_declContext ctx)
    {
        String type = ctx.var_type().getText();
        String idlist = ctx.id_list().getText();
        String[] ids = idlist.split(",");
        if (type.compareTo("INT") == 0) {
            for (int i = 0; i < ids.length; i ++) {
                Node node = new Node("INT(" + ids[i] + ")");
                current = node;
                ast.add(current);
            }
        } else if (type.compareTo("FLOAT") == 0) {
            for (int i = 0; i < ids.length; i ++) {
                Node node = new Node("FLOAT(" + ids[i] + ")");
                current = node;
                ast.add(current);
            }
        }
    }

    @Override public void enterAssign_stmt(LittleParser.Assign_stmtContext ctx) {
        // Creates an assignment node with the variable being updated
        Node node = new Node("=");
        current = node;
        Node left = new Node("ID(" + ctx.assign_expr().id().getText() +")");
        current.addLeft(left);
        ast.add(current);
    }
    @Override public void enterExpr(LittleParser.ExprContext ctx) {
        // New node for the expression
        Node node = new Node();

        // Setting the assignment node to return to
        Node prev = current;
        current = node;
        prev.addRight(current);

    }
    @Override public void enterAddop(LittleParser.AddopContext ctx) {
        current.setValue(ctx.getText());
    }
    @Override public void enterMulop(LittleParser.MulopContext ctx) {
        current.setValue(ctx.getText());
    }

    @Override public void enterPrimary(LittleParser.PrimaryContext ctx) {
        Node node = new Node();
        Node prev = current;
        current = node;

        if (prev.left == null) {
            prev.addLeft(node);
        } else {
            prev.addRight(node);
        }
        if (ctx.id() != null) {
            current.setValue("ID(" + ctx.id().getText() + ")");
        } else if (ctx.INTLITERAL() != null) {
            current.setValue("INTLITERAL(" + ctx.INTLITERAL().getText() + ")");
        } else if (ctx.FLOATLITERAL() != null) {
            current.setValue("FLOATLITERAL(" + ctx.FLOATLITERAL().getText() + ")");
        }

        current = prev;
    }

    @Override public void enterRead_stmt(LittleParser.Read_stmtContext ctx) {
        String idlist = ctx.id_list().getText();
        String[] ids = idlist.split(",");
        for (int i = 0; i < ids.length; i++) {
            Node node = new Node("READ(" + ids[i] + ")");
            current = node;
            ast.add(current);
        }
    }

    @Override public void enterWrite_stmt(LittleParser.Write_stmtContext ctx) {
        String idlist = ctx.id_list().getText();
        String[] ids = idlist.split(",");
        for (int i = 0; i < ids.length; i++) {
            Node node = new Node("WRITE(" + ids[i] + ")");
            current = node;
            ast.add(current);
        }
    }

    //statement to print out the ast
    public void astOut() {
        for (Node start : ast) {
            System.out.println(start.toString());
        }
    }
}
