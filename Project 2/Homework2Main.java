import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Haley on 4/19/2017.
 */
public class Homework2Main {

    public static void main(String[] args) throws IOException {
        Scanner keys = new Scanner(System.in);
        System.out.print("Enter file name: ");
        String name = keys.nextLine();
        Lexer lex = new Lexer(name);
        Parser parser = new Parser(lex);

        Node root = parser.parseStatements();

        //TreeViewer viewer = new TreeViewer("Parse Tree", 0, 0, 800, 500, root);
        root.execute();
    }
}
