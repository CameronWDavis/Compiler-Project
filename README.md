IR and Tiny Code Generator
Overview

This Java application is designed to generate Intermediate Representation (IR) codes from an input program and then convert these IR codes into Tiny code, which can be run via a Tiny simulator. The application is built using ANTLR for lexical analysis and parsing, and it includes custom logic for IR and Tiny code generation.
Components

    Driver Class (Driver.java):
        The entry point of the application.
        Handles the initial setup for ANTLR, including the creation of lexer and parser.
        Manages the creation of the symbol table and the generation of IR and Tiny codes.

    Symbols Class (Symbols.java):
        Manages the symbol table, which provides meanings to words in the input.
        Stores and prints IR codes.

    IRCode Class (IRCode.java):
        Extends from ANTLR's base listener for the Little language.
        Contains logic for generating IR codes based on the parsed input.
        Handles different statements like assignments, read, and write statements.
        Includes methods for pretty printing and Tiny code generation.

    Compiler Class (Nested in IRCode.java):
        Responsible for converting IR codes into Tiny code.
        Manages the mapping of variables to registers and generation of Tiny instructions.

    ANTLR Dependencies:
        The project requires ANTLR libraries for parsing and lexical analysis.

Requirements

    Java Development Kit (JDK)
    ANTLR v4 Runtime

Usage

    Compilation:
        Compile the Java files using a Java compiler.
        Ensure ANTLR runtime is included in the classpath.

    Execution:
        Run the Driver class.
        Provide the input program for which IR and Tiny codes are to be generated.

Input

The application expects an input program in the language defined by the LittleLexer and LittleParser. The input is processed to generate IR codes and subsequently Tiny code.
Output

    IR codes are printed first, representing the intermediate representation of the input program.
    Tiny code is printed next, which can be used in a Tiny simulator
