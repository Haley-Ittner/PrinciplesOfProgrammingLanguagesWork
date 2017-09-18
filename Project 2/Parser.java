import java.util.Scanner;

/**
 * Created by Haley on 4/19/2017.
 */
public class Parser {
    private Lexer lex;
    private Token tok;

    public Parser (Lexer lexer) {
        lex = lexer;
    }

    public static void main(String[] args) throws Exception {
        /*System.out.print("Enter file name: ");
        Scanner keys = new Scanner(System.in);
        String name = keys.nextLine();
        Lexer lex = new Lexer(name);
        Parser parser = new Parser(lex);

        Node root = parser.parseStatements();  // parser.parseProgram();

        //TreeViewer viewer = new TreeViewer("Parse Tree", 0, 0, 1000, 800, root );

        root.execute(); */

    }

    public Node parseStatements() {
        Node first = null;
        Node second = null;

        tok = lex.getToken();
        if (tok != null) {
            first = parseStatement();
        } else {
            return new Node("Statements", null, null, null);
        }
        //tok = lex.getToken();
        if (tok != null) {
            second = parseStatements();
        } else {
            return new Node("Statements", first, null, null);
        }
        return new Node("Statements", first, second, null);
    }

    private Node parseStatement() {
        Node first = null;
        Node second = null;
        Node third = null;
        String type = "";

        if (tok.isKind("variable")) {
            first = new Node(tok);
            tok = lex.getToken();
            if (!tok.getDetails().equals("=")) {
                System.out.println("Must have an equals sign when assigning variables");
                System.exit(0);
            } else {
                type = tok.getDetails();
                tok = lex.getToken();
                if (!tok.isKind("number") && !tok.isKind("variable") && !tok.getDetails().equals("-") &&
                        !tok.getDetails().equals("(") && !tok.isKind("BIFN")) {
                    System.out.println("Must have a value to assign to the variable");
                    System.exit(0);
                } else {
                    second = parseExpression();
                }
            }
        } else if (tok.getDetails().equals("show")) {
            type = tok.getDetails();
            tok = lex.getToken();
            if (!tok.isKind("number") && !tok.isKind("variable") && !tok.isKind("BIFN") &&
                    !tok.getDetails().equals("-") && !tok.getDetails().equals("(")) {
                System.out.println("Must have something to print(variable)");
                System.exit(0);
            } else {
                first = parseExpression();
            }
        } else if (tok.getDetails().equals("msg")) {
            type = tok.getDetails();
            tok = lex.getToken();
            if (!tok.isKind("string")) {
                System.out.println("Must have something to print(string)");
                System.exit(0);
            } else {
                first = new Node(tok);
            }
        } else if (tok.getDetails().equals("input")) {
            type = tok.getDetails();
            tok = lex.getToken();
            if (!tok.isKind("string")) {
                System.out.println("Must have a prompt for the user");
                System.exit(0);
            } else {
                first = new Node(tok);
                tok = lex.getToken();
                if (!tok.isKind("variable")) {
                    System.out.println("Must have a variable to pur user input into");
                    System.exit(0);
                } else {
                    second = new Node(tok);
                }
            }
        } else if (tok.getDetails().equals("newline")) {
            type = tok.getDetails();
        }
        return new Node("Statement", type, first, second, third);
    }

    private Node parseExpression() {
        Node first = null;
        Node second = null;
        Node third = null;
        String type = "";

        first = parseTerm();
        tok = lex.getToken();
        if (tok != null) {
            if (!tok.getDetails().equals("+") && !tok.getDetails().equals("-")) {
                lex.putBackToken();
                return new Node("Expression", type, first, null, null);
            } else {
                type = tok.getDetails();
                tok = lex.getToken();
                if (tok != null) {
                    second = parseExpression();
                } else {
                    System.out.println("Error");
                }
            }
        }
        return new Node("Expression", type, first, second, third);
    }

    private Node parseTerm() {
        Node first = null;
        Node second = null;
        Node third = null;
        String type = "";

        first = parseFactor();
        tok = lex.getToken();
        if (tok != null) {
            if (!tok.getDetails().equals("*") && !tok.getDetails().equals("/")) {
                lex.putBackToken();
                return new Node("Term", type, first, null, null);
            } else {
                type = tok.getDetails();
                tok = lex.getToken();
                if (tok != null) {
                    second = parseTerm();
                } else {
                    System.out.println("Error");
                }
            }
        }
        return new Node("Term", type, first, second, third);
    }

    private Node parseFactor() {
        Node first = null;
        Node second = null;
        Node third = null;
        String type = "";

        if (tok.isKind("symbol")) {
            type = tok.getDetails();
            if (tok.getDetails().equals("-")) {
                tok = lex.getToken();
                first = parseFactor();
            } else {
                tok = lex.getToken();
                first = parseExpression();
                tok = lex.getToken();
            }
        } else if (tok.isKind("number")) {
            first = new Node(tok);
        } else if (tok.isKind("variable")) {
            first = new Node(tok);
        } else if (tok.isKind("BIFN")) {
            type = tok.getDetails();
            tok = lex.getToken();
            if (!tok.getDetails().equals("(")) {
                System.out.println("Built in functions must have a starting parenthesis");
                System.exit(0);
            } else {
                tok = lex.getToken();
                first = parseExpression();
                tok = lex.getToken();
                if (!tok.getDetails().equals(")")) {
                    System.out.println("Built in functions must have an ending parenthesis");
                    System.exit(0);
                }
            }
        } else {
            System.out.println("Error");
        }
        return new Node("Factor", type, first, second, third);
    }
}
