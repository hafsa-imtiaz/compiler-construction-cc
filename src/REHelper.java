import java.util.*;

class REHelper {
    // Regular Expressions
    //integer: (0|1|2|3|4|5|6|7|8|9)+
    //decimal: ((0|1|2|3|4|5|6|7|8|9)+).(0|1|2|3|4|5|6|7|8|9)+ (any number of decimals)
    //only 5 decimal places: ((0|1|2|3|4|5|6|7|8|9)+).(0|1|2|3|4|5|6|7|8|9)(0|1|2|3|4|5|6|7|8|9)?(0|1|2|3|4|5|6|7|8|9)?(0|1|2|3|4|5|6|7|8|9)?(0|1|2|3|4|5|6|7|8|9)?
    //keyword: (bin|tin|dec|harf|in|out)
    //binary value: (true|false)
    //character: '(a|b|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z)'
    //identifier: (a|b|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z)+

    //implement single-line comments, multi-line comments, and strings in code --> no REs for them as of now

        /* needed for decimal
        String infix = "((0|1|2|3|4|5|6|7|8|9)+).(0|1|2|3|4|5|6|7|8|9)+";
        infix = infix.replace('.', 'a');
        String postfix = RegExUtils.infixToPostfix(infix).replace('a', '.');
        */

    // Returns precedence of basic RE operators
    private static int precedence(char c) {
        switch (c) {
            case '*':
            case '+':
            case '?':
                return 3;
            case '~': // ye use kiya hai cause '.' is gonna be used for decimal point
                return 2;
            case '|':
                return 1;
            default:
                return 0;
        }
    }

    // Inserts an explicit concatenation operator '~' when needed.
    // E.g., "ab" -> "a~b", "a|b" stays "a|b", "(a)(b)" -> "(a).(b)"
    private static String insertExplicitConcatOperator(String re) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < re.length(); i++) {
            sb.append(re.charAt(i));
            // If not at last char
            if (i + 1 < re.length()) {
                char c1 = re.charAt(i);
                char c2 = re.charAt(i + 1);
                // If c1 is a literal, ')' or '*', '+' etc. and c2 is a literal or '('
                if ((Character.isLetterOrDigit(c1) || c1 == ')' || c1 == '*' || c1 == '+' || c1 == '?') &&
                        (Character.isLetterOrDigit(c2) || c2 == '(')) {
                    sb.append('~');
                }
            }
        }
        return sb.toString();
    }

    // Converts infix to postfix using a standard Shunting Yard approach
    public static String infixToPostfix(String re) {
        re = insertExplicitConcatOperator(re);
        Stack<Character> stack = new Stack<>();
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < re.length(); i++) {
            char c = re.charAt(i);

            switch (c) {
                case '(':
                    stack.push(c);
                    break;

                case ')':
                    // Pop until '('
                    while (!stack.isEmpty() && stack.peek() != '(') {
                        output.append(stack.pop());
                    }
                    stack.pop(); // remove '('
                    break;

                case '*':
                case '+':
                case '?':
                case '|':
                case '~':
                    while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(c)) {
                        output.append(stack.pop());
                    }
                    stack.push(c);
                    break;

                default:
                    // Literal or symbol
                    output.append(c);
                    break;
            }
        }

        while (!stack.isEmpty()) {
            output.append(stack.pop());
        }

        return output.toString();
    }
}