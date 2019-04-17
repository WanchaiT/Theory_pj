
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class Regex {//U

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String exp = s.nextLine().trim();
        System.out.println(RegExConverter2.infixToPostfix(exp));

    }

    static class Automata {

        static int x = 50;
        static int y = 50;
        ArrayList<State> sts = new ArrayList<>();
        ArrayList<Transition> trs = new ArrayList<>();
        ArrayList<String> alp = new ArrayList<>();

        public Automata(String q) {
            alp.add(q);
            State q0 = new State(x,y);
            Transition t0 = new Transition(null,q0);
            t0.x = x-30;
            t0.y = y;
            changePostion();
            State q1 = new State(x,y);
            q1.isAccept = true;
            changePostion();
            Transition t1 = new Transition(q0, q1);
            t1.text = q;
            
            sts.add(q0);
            sts.add(q1);
            
            trs.add(t0);
            trs.add(t1);
        }
        public void changePostion(){
            x = (x + 50) % 950;
            y = (y + 50) % 950;
        }
    }

    static Automata concatinate(Automata q, Automata r) {
        
        
        return q;
    }

    static Automata star(Automata q) {

        return q;
    }

    static Automata union(Automata q, Automata r) {

        return q;
    }

    public static Automata regexToAutomata(String regPost) {
        String operation = ".U*";
        Stack<Automata> stack = new Stack<>();
        stack.push(new Automata("" + regPost.charAt(0)));
        for (int i = 1; i < regPost.length(); i++) {
            String e = regPost.charAt(i) + "";
            if (!operation.contains(e)) {
                stack.push(new Automata(e));
            } else if (e.equals("U")) {
                Automata q = stack.pop();
                Automata r = stack.pop();
                Automata qr = union(q, r);
                stack.push(qr);
            } else if (e.equals(".")) {
                Automata q = stack.pop();
                Automata r = stack.pop();
                Automata qr = concatinate(q, r);
                stack.push(qr);
            } else if (e.equals("*")) {
                Automata q = stack.pop();
                Automata q_ = star(q);
                stack.push(q_);
            }
        }
        return stack.pop();
    }
}

class RegExConverter2 { //reference https://gist.github.com/gmenard/6161825

    private static final Map<Character, Integer> precedenceMap;

    static {
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        map.put('(', 1);
        map.put('U', 2);
        map.put('.', 3); // explicit concatenation operator
        map.put('*', 4);
        precedenceMap = Collections.unmodifiableMap(map);
    }

    ;

	private static Integer getPrecedence(Character c) {
        Integer precedence = precedenceMap.get(c);
        return precedence == null ? 5 : precedence;
    }

    private static String formatRegEx(String regex) {
        String res = new String();
        List<Character> allOperators = Arrays.asList('|', '?', '+', '*', '^', 'U');
        List<Character> binaryOperators = Arrays.asList('^', '|', 'U');

        for (int i = 0; i < regex.length(); i++) {
            Character c1 = regex.charAt(i);

            if (i + 1 < regex.length()) {
                Character c2 = regex.charAt(i + 1);

                res += c1;

                if (!c1.equals('(') && !c2.equals(')') && !allOperators.contains(c2) && !binaryOperators.contains(c1)) {
                    res += '.';
                }
            }
        }
        res += regex.charAt(regex.length() - 1);

        return res;
    }

    public static String infixToPostfix(String regex) {
        String postfix = new String();

        Stack<Character> stack = new Stack<Character>();

        String formattedRegEx = formatRegEx(regex);
        System.out.println(formattedRegEx);
        for (Character c : formattedRegEx.toCharArray()) {
            switch (c) {
                case '(':
                    stack.push(c);
                    break;

                case ')':
                    while (!stack.peek().equals('(')) {
                        postfix += stack.pop();
                    }
                    stack.pop();
                    break;

                default:
                    while (stack.size() > 0) {
                        Character peekedChar = stack.peek();

                        Integer peekedCharPrecedence = getPrecedence(peekedChar);
                        Integer currentCharPrecedence = getPrecedence(c);

                        if (peekedCharPrecedence >= currentCharPrecedence) {
                            postfix += stack.pop();
                        } else {
                            break;
                        }
                    }
                    stack.push(c);
                    break;
            }

        }

        while (stack.size() > 0) {
            postfix += stack.pop();
        }

        return postfix;
    }
}
