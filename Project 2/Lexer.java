import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Haley on 4/19/2017.
 */
public class Lexer {

        private int counter = 0;
        private static String[] keywords = {"cos", "sin", "sqrt"};
        private static String[] textWords = {"input", "show", "msg", "newline"};
        private static char[] symbols = {'=', '+', '-', '*', '/', '(', ')'};
        public static ArrayList<Token> list;

        public static void main(String[] args) {
        /*Scanner in = new Scanner(System.in);
        System.out.print("Enter file name: ");
        String file = in.next();
        Lexer lex = new Lexer(file);
        list = lex.scan();
            for (int k = 0; k < list.size(); k++) {
                System.out.println(list.get(k));
            } */
        }

        private Scanner input;

        public Lexer(String fileName) {
            try {
                input = new Scanner(new File(fileName));
                list = this.scan();
                main(null);
            } catch (Exception e) {
            }
        }

        public ArrayList<Token> scan() {
            ArrayList<Token> list = new ArrayList<Token>();

            int state = 0;
            String id = "";
            double value = 0;
            int decPlace = 1;

            while (input.hasNext()) {
                String s = input.nextLine();
                s += "\n";
                for (int k = 0; k < s.length(); k++) {
                    // process next individual char in the file
                    char x = s.charAt(k);

                    if (x == -1) {
                        System.exit(0);
                    }
                    else if (state == 0) {
                        if (x == ' ') {
                            // stay in state 0
                        } else if (x == '#') {
                            state = 1;
                            id += x;
                        } else if ((x > 64 && x < 91) || (x > 96 && x < 123)) {
                            state = 2;
                            id += x;
                        } else if (doesHave(x, symbols)) {
                            state = 3;
                            id += x;
                        } else if (x > 47 && x < 58) {
                            state = 4;
                            value = 10 * value + (x - '0');
                        } else if (x =='"') {
                            state = 6;
                            //id += x;
                        }
                    } else if (state == 1) {
                        if (x == 10 || x == 13) {
                            Token hold = new Token("comment", id);
                            id = "";
                            state = 0;
                            list.add(hold);
                        } else {
                            id += x;
                        }
                    } else if (state == 2) {
                        if (x == 10 || x == 13 || x == 32) {
                            if (doesHave(id, keywords)) {
                                Token hold = new Token("BIFN", id);
                                id = "";
                                state = 0;
                                list.add(hold);
                                k--;
                            }else if (doesHave(id, textWords)) {
                                Token hold = new Token("print", id);
                                id = "";
                                state = 0;
                                list.add(hold);
                                k--;
                            } else {
                                Token hold = new Token("variable", id);
                                id = "";
                                state = 0;
                                list.add(hold);
                                k--;
                            }
                        }
                        else if ((x > 64 && x < 91) || (x > 96 && x < 123) ||
                                (x > 47 && x < 58)) {
                            id += x;
                        } else if (x == '(') {
                            if (doesHave(id, keywords)) {
                                Token hold = new Token("BIFN", id);
                                id = "";
                                state = 0;
                                list.add(hold);
                                k--;
                            }
                            else {
                                System.out.println("No such method!");
                            }
                        } else if (doesHave(x, symbols)) {
                            Token hold = new Token("variable", id);
                            id = "";
                            state = 0;
                            list.add(hold);
                            k--;
                        }
                    } else if (state == 3) {
                        Token hold = new Token("symbol", id);
                        id = "";
                        state = 0;
                        list.add(hold);
                        k--;
                    } else if (state == 4) {
                        if (x > 47 && x < 58) {
                            state = 4;
                            value = 10 * value + (x - '0');
                        } else if (x =='.') {
                            id += x;
                            state = 5;
                        }
                        else {
                            Token hold = new Token("number", value + "");
                            value = 0;
                            state = 0;
                            list.add(hold);
                            id = "";
                            k--;
                        }
                    }
                    else if (state == 5) {
                        if(x > 47 && x < 58) {
                            value = value + ((x - '0') / Math.pow(10, decPlace));
                            decPlace++;
                        } else {
                            Token hold = new Token("number", value + "");
                            value = 0;
                            state = 0;
                            list.add(hold);
                            id ="";
                            decPlace = 1;
                            k--;
                        }
                    }
                    else if (state == 6) {
                        if (x == '"') {
                            Token hold = new Token("string",id);
                            id = "";
                            state = 0;
                            list.add(hold);
                        } else {
                            id += x;
                        }
                    }

                }
            }
            return list;
        }

        public static boolean doesHave(char q, char[] list) {
            for (int x = 0; x < list.length; x++) {
                if (list[x] == q) {
                    return true;
                }
            }
            return false;
        }

        public static boolean doesHave(String q, String[] list) {
            for (int x = 0; x < list.length; x++) {
                if (list[x].equals(q)) {
                    return true;
                }
            }
            return false;
        }

        public Token getToken() {
            if (counter > list.size() - 1) {
                return null;
            } else {
                Token give = list.get(counter);
                counter++;
                return give;
            }
        }

        public void putBackToken() {
            if(counter > 1) {
                counter--;
            } else {
                counter = 0;
            }
        }
    }
