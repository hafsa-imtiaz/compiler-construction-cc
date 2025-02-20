public class Main {
    public static void main(String[] args) {
        //worker();
        kaam();
    }

    public static void worker() {
        String infix = "(bin|tin|dec|harf|in|out)";
        String postfix = REHelper.infixToPostfix(infix);
        System.out.println("Postfix: " + postfix);

        // nfa banao
        NFA nfa = new NFA(postfix);
        nfa.printNFA();

        // nfa se dfa
        DFA meri = new DFA(nfa);
        meri.printDFA();
        System.out.println(meri.accepts("dec"));
    }

    public static void kaam() {
        // code testing
        String code = """
            $$ ye mera sample code hai
            dec xy = 12.34;
            in(x);
            $$ single-line comment
            x = x + 5;
            ${ multi-line
               comment }$
            out("Value:", x, "\\n");
            bin val = true;
            out("Value:", x);
            harf yy = 'c';
            """;

        // PLS WORK UUF
        Tokenizer tokenizer = new Tokenizer(code);
        tokenizer.printTokens();
    }
}

/*
Keywords →
- input (cin): in
- output (cout): out

Data types →
- bool (true/false) ---> bin
- int (whole numbers) ---> tin
- float (decimal numbers) ---> dec
- char (single character) ---> harf

Comments →
- Single Line: $$ comment
- Multi-Line: ${ comment comment }$

Operators →
- Addition (+),
- Subtraction (-),
- Multiplication (*),
- Division (/),
- Modulus (%),
- Exponentiation (^).

Variable Naming Rules → Only lowercase letters (a-z).

File Type → Extension: .aha

Syntax:
1. datatype var_name = value;
2. in(var_name);
3. out("can be string", var_name, "\nstring");
 */