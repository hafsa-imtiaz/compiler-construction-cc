# **AHA Programming Language**

## **Introduction**
AHA is a beautiful, user-friendly (FAST-student friendly) programming language designed with a simplified syntax inspired by C++ and Python. It introduces custom keywords, unique data types, and specific formatting rules to ensure a structured and readable coding experience.

---
## **File Format**
- AHA programs are saved with the `.aha` file extension.

---

## **Keywords**

| Standard C++ | AHA Equivalent | Description                |
|--------------|----------------|----------------------------|
| `cin`        | `in`           | Used for input operations  |
| `cout`       | `out`          | Used for output operations |

---

## **Data Types**

| Data Type | AHA Equivalent | Description                               |
|-----------|----------------|-------------------------------------------|
| `bool`    | `bin`          | Stores Boolean values (`true` or `false`) |
| `int`     | `tin`          | Stores whole numbers                      |
| `float`   | `dec`          | Stores decimal numbers                    |
| `char`    | `harf`         | Stores a single character                 |

---

## **Comments**

AHA supports both single-line and multi-line comments.

| Comment Type | Syntax          | Example                              |
|--------------|-----------------|--------------------------------------|
| Single-line  | `$$ comment`    | `$$ This is a single-line comment`   |
| Multi-line   | `${ comment }$` | `${ This is a multi-line comment }$` |

---

## **Operators**

| Operation      | Symbol | Example      |
|----------------|--------|--------------|
| Addition       | `+`    | `c = a + b;` |
| Subtraction    | `-`    | `c = a - b;` |
| Multiplication | `*`    | `c = a * b;` |
| Division       | `/`    | `c = a / b;` |
| Modulus        | `%`    | `c = a % b;` |
| Exponentiation | `^`    | `c = a ^ b;` |

---

## **Variable Naming Conventions**
- Variable names **must** consist of **only lowercase letters (a-z)**.
- Uppercase letters, numbers, and special characters are **not permitted** in variable names.

---

## **Syntax Guide**

### **Variable Declaration and Initialization**
```aha
tin num = 10;
dec pi = 3.14;
bin flag = true;
harf letter = 'A';
```

### **User Input**
```aha
tin age;
in(age);
```

### **Output Operations**
```aha
out("Hello, AHA!\n");
out("Age: ", age, "\n");
```

---

## **Sample Program**
```aha
tin a, b, sum;
out("Enter two numbers: ");
in(a);
in(b);
sum = a + b;
out("Sum is: ", sum, "\n");
```

---

## **Summary**
The AHA programming language provides a **clear, structured, and beginner-friendly** syntax for developing simple programs. With its beautiful commands, strict variable naming conventions, and easy-to-read formatting, AHA is designed to enhance code clarity and maintainability.

### Have fun coding with the best language ever: AHA!

A beautiful project by Areen Zainab (22i - 1115) and Hafsa Imtiaz (22i - 0959)