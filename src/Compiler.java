// 22i - 0959 ---> Hafsa Imtiaz
// 22i - 1115 ---> Areen Zainab
// Section H

// Compiler Construction Assignment #1
// Lexical Analyser (apni zubaan ka)

// Language is: AHA
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Compiler {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the file path: ");
        String filePath = scanner.nextLine();

        if (!filePath.contains("/") && !filePath.contains("\\")) {
            filePath = "src/" + filePath;
        }

        try {
            String code = readAhaFile(filePath);
            Lexical_Analyser(code);
        } catch (IOException e) {
            System.out.println("ERROR: Could not process your file. Try again later. Sadness Moment.\n" + e.getMessage());
        }
    }

    public static void Lexical_Analyser(String code) {
        // PLS WORK UUF PLS PLS PLS
        Tokenizer tokenizer = new Tokenizer(code);
    }
    
    // AHA file ko pls read
    public static String readAhaFile(String filePath) throws IOException {
        if (!filePath.toLowerCase().endsWith(".aha")) {
            throw new IllegalArgumentException("File must have a .aha extension.");
        }

        File file = new File(filePath);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        // :((( sadness more mehnat for agli ass
        return sb.toString();   // file bhaijo analyser ko
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
- Multi-Line: ${ comment mera comment tera }$

Operators →
- Addition (+),
- Subtraction (-),
- Multiplication (*),
- Division (/),
- Modulus (%),
- Exponentiation (^).

Variable Naming Rules → only (a-z).

File Type → Extension: .aha

Syntax:
1. datatype var_name = value;
2. in(var_name);
3. out("can be string", var_name, "\nstring");
 */