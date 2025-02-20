import java.util.*;

class Tokenizer {

    class Token {
        public String type;
        public String value;

        public Token(String type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return type + ": " + value;
        }
    }

    // DFAs
    private final DFA integerDFA;
    private final DFA decimalDFA;
    private final DFA keywordDFA;      // recognizes (bin|tin|dec|harf|in|out)
    private final DFA binaryValueDFA;  // recognizes (true|false)
    private final DFA identifierDFA;   // recognizes [a-z]+ --> no caps and also only underscore

    private List<Token> tokens;
    private int index;
    private String input;

    public Tokenizer(String input) {
        this.input = input;
        this.index = 0;

        this.integerDFA      = new DFA(new NFA("(0|1|2|3|4|5|6|7|8|9)+"));
        this.decimalDFA      = new DFA(new NFA("((0|1|2|3|4|5|6|7|8|9)+).(0|1|2|3|4|5|6|7|8|9)+"));
        this.keywordDFA      = new DFA(new NFA("(bin|tin|dec|harf|in|out)"));
        this.binaryValueDFA  = new DFA(new NFA("(true|false)"));
        this.identifierDFA   = new DFA(new NFA("(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z)+"));

        this.tokens = new ArrayList<>();
        this.tokens = tokenize();
    }

    public List<Token> tokenize() {
        while (index < input.length()) {
            char current = input.charAt(index);

            //white space ka off scene
            if (Character.isWhitespace(current)) {
                index++;
                continue;
            }

            // comment handler
            if (current == '$') {
                if (index + 1 < input.length()) {
                    char next = input.charAt(index + 1);
                    // single-line comment $$ comment tera comment mera
                    if (next == '$') {
                        int newLineIdx = input.indexOf('\n', index);
                        index = (newLineIdx == -1) ? input.length() : newLineIdx + 1;
                        continue;
                    }
                    // multi-line commenting horahi hai boys ${ comment tera comment mera }$
                    else if (next == '{') {
                        int endIdx = input.indexOf("}$", index);
                        index = (endIdx == -1) ? input.length() : endIdx + 2;
                        continue;
                    }
                }
            }

            // strings ke liye
            if (current == '"') {
                int start = index;
                index++;
                while (index < input.length()) {
                    // stop at an unescaped double quote
                    if (input.charAt(index) == '"' && input.charAt(index - 1) != '\\') {
                        break;
                    }
                    index++;
                }
                if (index < input.length()) index++; // move past closing quote
                tokens.add(new Token("STRING", input.substring(start, index)));
                continue;
            }

            // char ke liye --> handling without re
            if (current == '\'') {
                int start = index;
                index++; // skip opening quote

                // agla single quote lo
                while (index < input.length() && input.charAt(index) != '\'') {
                    index++;
                }
                // sadness --> error check
                if (index < input.length()) {
                    index++;
                }
                tokens.add(new Token("CHAR_LITERAL", input.substring(start, index)));
                continue;
            }

            // check time bbg
            if (Character.isLetter(current)) {
                int start = index;
                while (index < input.length() && (Character.isLetterOrDigit(input.charAt(index)))) {
                    index++;
                }
                String word = input.substring(start, index);
                // kya ye keyword hai?
                if (keywordDFA.accepts(word)) {
                    tokens.add(keywordMatch(word));
                }
                // kya ye true and false hai?
                else if (binaryValueDFA.accepts(word)) {
                    tokens.add(keywordMatch(word));
                }
                // kya ye var hai??
                else if (identifierDFA.accepts(word)) {
                    tokens.add(new Token("IDENTIFIER", word));
                }
                // sadness ye unknown
                else {
                    tokens.add(new Token("UNKNOWN", word));
                }
                continue;
            }

            // numbers --- int literals and float literals
            if (Character.isDigit(current)) {
                int start = index;
                boolean hasDot = false;

                while (index < input.length()) {
                    char ch = input.charAt(index);
                    if (Character.isDigit(ch)) {
                        index++;
                    } else if (ch == '.' && !hasDot) {
                        hasDot = true;
                        index++;
                    } else {
                        break;
                    }
                }
                String number = input.substring(start, index);
                // using integer wali dfa :
                /*
                if (decimalDFA.accepts(number)) {
                    tokens.add(new Token("FLOAT_LITERAL", number));
                } else if (integerDFA.accepts(number)) {
                    tokens.add(new Token("INT_LITERAL", number));
                } else {
                    tokens.add(new Token("UNKNOWN", number));
                }
                */
                tokens.add(new Token(hasDot ? "FLOAT_LITERAL" : "INT_LITERAL", number));
                continue;
            }

            // operators, += shit
            if ("=+*/%^-(){};,".indexOf(current) != -1) {
                tokens.add(symbolMatch(current));
                index++;
                continue;
            }

            // unknown for jo na pata ho
            tokens.add(new Token("UNKNOWN", String.valueOf(current)));
            index++;
        }

        return tokens;
    }

    private Token symbolMatch(char symbol) {
        switch (symbol) {
            case '=': return new Token("ASSIGN", "=");
            case '+': return new Token("PLUS", "+");
            case '-': return new Token("MINUS", "-");
            case '*': return new Token("MULTIPLY", "");
            case '/': return new Token("DIVIDE", "/");
            case '%': return new Token("MODULUS", "%");
            case '^': return new Token("EXPONENT", "^");
            case '(': return new Token("OPEN_BRACKET", "(");
            case ')': return new Token("CLOSE_BRACKET", ")");
            case '{': return new Token("OPEN_CURLY", "{");
            case '}': return new Token("CLOSE_CURLY", "}");
            case ',': return new Token("COMMA", ",");
            case ';': return new Token("SEMICOLON", ";");
            default:
                return new Token("UNKNOWN", String.valueOf(symbol));
        }
    }

    private Token keywordMatch(String word) {
        switch (word) {
            case "bin": return new Token("DATATYPE_BIN", word);
            case "tin": return new Token("DATATYPE_TIN", word);
            case "dec": return new Token("DATATYPE_DEC", word);
            case "harf": return new Token("DATATYPE_CHAR", word);
            case "in": return new Token("INPUT", word);
            case "out": return new Token("OUTPUT", word);
            case "true": return new Token("TRUE", word);
            case "false": return new Token("FALSE", word);
            default: return new Token("IDENTIFIER", word);
        }
    }

    public void printTokens() {
        for (Token token : tokens) {
            System.out.println("[" + token.type + ": " + token.value + "]");
        }
    }
}