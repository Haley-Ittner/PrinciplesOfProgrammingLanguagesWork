import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Haley on 4/19/2017.
 */
public class Node {
    public static int count = 0;  // maintain unique id for each node

    private int id;
    private static ArrayList<String> vars = new ArrayList<String>();
    private static ArrayList<String> vals = new ArrayList<String>();
    //private ArrayList<String> express = new ArrayList<String>();
    double holder;


    private String kind;  // non-terminal or terminal category for the node
    private String info;  // extra information about the node such as
    // the actual identifier for an I

    // references to children in the parse tree
    private Node first, second, third;

    // construct a common node with no info specified
    public Node(String k, Node one, Node two, Node three) {
        kind = k;
        info = "";
        first = one;
        second = two;
        third = three;
        id = count;
        count++;
        System.out.println(this);
    }

    // construct a node with specified info
    public Node(String k, String inf, Node one, Node two, Node three) {
        kind = k;
        info = inf;
        first = one;
        second = two;
        third = three;
        id = count;
        count++;
        System.out.println(this);
    }

    // construct a node that is essentially a token
    public Node(Token token) {
        kind = token.getKind();
        info = token.getDetails();
        first = null;
        second = null;
        third = null;
        id = count;
        count++;
        System.out.println(this);
    }

    public String toString() {
        return "#" + id + "[" + kind + "," + info + "]";
    }


    public void execute() {
        Scanner scnr = new Scanner(System.in);
        String holder;

        if (kind.equals("Statements")) {
            if (first != null) {
                first.execute();
            }
            if (second != null) {
                second.execute();
            }
        } else if (kind.equals("Statement")) {
            if (info.equals("=")) {
                vars.add(first.info);
                double value = second.expressionExecute();
                vals.add(vars.indexOf(first.info), (value + ""));
                //printList(vars);
                //printList(vals);
                //gotta set something in vars
            } else if (info.equals("show")) {
                System.out.print(first.expressionExecute());
                //print out the expression
            } else if (info.equals("msg")) {
                System.out.print(first.info);
            } else if (info.equals("input")) {
                System.out.print(first.info);
                vars.add(second.info);
                holder = scnr.next();
                vals.add(vars.indexOf(second.info), holder);
            } else if (info.equals("newline")) {
                System.out.println();
            }
        }
    }

    public double expressionExecute() {
        //System.out.println("Beginning Kind " + kind +", beginning info " + info);
        if (kind.equals("Expression")) {
            if (info.equals("+")) {
                return first.expressionExecute() + second.expressionExecute();
                //gotta add/subtract the values after
            } else if (info.equals("-")) {
                return first.expressionExecute() - second.expressionExecute();
            } else if (info.equals("")) {
                return first.expressionExecute();
            }
        } else if (kind.equals("Term")) {
            if (info.equals("*")) {
                return first.expressionExecute() * second.expressionExecute();
                //gotta divide/multiply the values after
            } else if (info.equals("/")) {
                return first.expressionExecute() / second.expressionExecute();
            } else if (info.equals("")) {
                return first.expressionExecute();
            }
        } else if (kind.equals("Factor")) {
            if (info.equals("(")) {
                return (first.expressionExecute());
                //gotta figure this out. the stuff after this takes precedence
            } else if (info.equals("sqrt") || info.equals("cos") || info.equals("sin")) {
                return nodeArithmetic(info, first.expressionExecute());
                //execute the method with the values after
            } else if (info.equals("")) {
                return first.expressionExecute();
            } else if (info.equals("-")) {
                return -1 *(first.expressionExecute());
            }
        } else if (kind.equals("number")) {
            //System.out.println("Returning number " + info);
            holder = Double.valueOf(info);
            //System.out.println("Returning value " + holder);
            return Double.valueOf(info);
            //return the number
        } else if (kind.equals("variable")) {
            int index = vars.indexOf(info);
            if (index == -1) {
                System.out.println("The variable " + info + " was used before assigned");
            } else {
                holder = Double.valueOf(vals.get(index));
                return Double.valueOf(vals.get(index));
            }
        }
        //System.out.println("Ending Kind: " + kind + ", ending info " + info);
        return 0;
    }

    private double nodeArithmetic(String op, double express) {
        if (op.equals("sqrt")) {
            return Math.sqrt(express);
        } else if (op.equals("cos")) {
            return Math.cos(express);
        } else if (op.equals("sin")) {
            return Math.sin(express);
        }
        return 0;
    }

    private void printList(ArrayList<String> hold) {
        for (int x = 0; x < hold.size(); x++) {
            System.out.print(hold.get(x) + " ");
        }
        System.out.println();
    }

    // produce array with the non-null children
    // in order
    private Node[] getChildren() {
        int count = 0;
        if (first != null) count++;
        if (second != null) count++;
        if (third != null) count++;
        Node[] children = new Node[count];
        int k = 0;
        if (first != null) {
            children[k] = first;
            k++;
        }
        if (second != null) {
            children[k] = second;
            k++;
        }
        if (third != null) {
            children[k] = third;
            k++;
        }

        return children;
    }

    //******************************************************
    // graphical display of this node and its subtree
    // in given camera, with specified location (x,y) of this
    // node, and specified distances horizontally and vertically
    // to children
    public void draw(Camera cam, double x, double y, double h, double v) {

        System.out.println("draw node " + id);

        // set drawing color
        cam.setColor(Color.black);

        String text = kind;
        if (!info.equals("")) text += "(" + info + ")";
        cam.drawHorizCenteredText(text, x, y);

        // positioning of children depends on how many
        // in a nice, uniform manner
        Node[] children = getChildren();
        int number = children.length;
        System.out.println("has " + number + " children");

        double top = y - 0.75 * v;

        if (number == 0) {
            return;
        } else if (number == 1) {
            children[0].draw(cam, x, y - v, h / 2, v);
            cam.drawLine(x, y, x, top);
        } else if (number == 2) {
            children[0].draw(cam, x - h / 2, y - v, h / 2, v);
            cam.drawLine(x, y, x - h / 2, top);
            children[1].draw(cam, x + h / 2, y - v, h / 2, v);
            cam.drawLine(x, y, x + h / 2, top);
        } else if (number == 3) {
            children[0].draw(cam, x - h, y - v, h / 2, v);
            cam.drawLine(x, y, x - h, top);
            children[1].draw(cam, x, y - v, h / 2, v);
            cam.drawLine(x, y, x, top);
            children[2].draw(cam, x + h, y - v, h / 2, v);
            cam.drawLine(x, y, x + h, top);
        } else {
            System.out.println("no Node kind has more than 3 children???");
            System.exit(1);
        }

    }
}
