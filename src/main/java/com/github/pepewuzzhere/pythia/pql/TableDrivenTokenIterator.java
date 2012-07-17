/*
 * The MIT License
 *
 * Copyright 2012 Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.pepewuzzhere.pythia.pql;

import com.github.pepewuzzhere.pythia.PythiaError;
import com.github.pepewuzzhere.pythia.PythiaException;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * Implementation of token iterator - returns stream of lexeme using
 * table-driven algorithm.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public class TableDrivenTokenIterator implements ITokenIterator {

    // LEXEME STATE
    private final int LS_INIT      = 0; // start position
    private final int LS_READ_WORD = 1; // reading word lexeme
    private final int LS_WORD      = 2; // return word
    private final int LS_SYMBOL    = 3; // return symbol
    private final int LS_READ_VAR  = 4; // reading variable (beetwen "")
    private final int LS_VARIABLE  = 5; // return variable
    private final int LS_ERROR     = 6; // error

    // INPUT TYPE
    private final int IT_LETTER     = 0; // [a-zA-Z]
    private final int IT_WHITESPACE = 1; // [ \t\n]+
    private final int IT_SYMBOL     = 2; // [()=,]
    private final int IT_QUOT       = 3; // ["]
    private final int IT_OTHER      = 4; // anything else
    private final int IT_END        = 5; // end of source

    private String source;  // source string
    private int pos = 0; // position of current character of source
    private final int[][] transition; // transition states tab;e
    private final boolean[] acceptState; // array with states that are accepted
                                         // as correct lexeme input
    private final Pattern[] inputPattern; // array with patterns descibing
                                          // correct input values

    // correct input types
    private final int[] inputTypes = {
        IT_LETTER, IT_WHITESPACE, IT_SYMBOL, IT_QUOT};

    // keywords of PQL
    private final String[] keywords = {
        "CREATE", "USE", "DROP", "SELECT", "UPDATE", "INSERT", "DELETE",
        "KEYSPACE", "COLUMNFAMILY", "KEY", "FROM", "WHERE", "SET",
        "INTO", "VALUES", "KILL"
    };

    /**
     * Initialization of transition table, states to accept as correct lexemes
     * and possible input patterns.
     */
    public TableDrivenTokenIterator() {

        // state tranistion table
        transition = new int[][] {
            {LS_READ_WORD, LS_INIT, LS_SYMBOL, LS_READ_VAR, LS_ERROR, LS_ERROR},
            {LS_READ_WORD, LS_WORD, LS_WORD, LS_WORD, LS_ERROR, LS_WORD},
            {LS_ERROR, LS_ERROR, LS_ERROR, LS_ERROR, LS_ERROR, LS_ERROR},
            {LS_ERROR, LS_ERROR, LS_ERROR, LS_ERROR, LS_ERROR, LS_ERROR},
            {LS_READ_VAR, LS_READ_VAR, LS_READ_VAR, LS_VARIABLE, LS_READ_VAR,
                LS_ERROR},
            {LS_ERROR, LS_ERROR, LS_ERROR, LS_ERROR, LS_ERROR, LS_ERROR},
        };

        // state to accept as correct lexeme
        acceptState = new boolean[7];
        acceptState[LS_INIT]      = false;
        acceptState[LS_READ_WORD] = false;
        acceptState[LS_WORD]      = true;
        acceptState[LS_READ_VAR]  = false;
        acceptState[LS_SYMBOL]    = true;
        acceptState[LS_VARIABLE]  = true;
        acceptState[LS_ERROR]     = false;

        // input type patterns
        inputPattern = new Pattern[4];
        inputPattern[IT_LETTER]     = Pattern.compile("[a-zA-Z]");
        inputPattern[IT_WHITESPACE] = Pattern.compile("\\s");
        inputPattern[IT_SYMBOL]     = Pattern.compile("[=(),]");
        inputPattern[IT_QUOT]       = Pattern.compile("\"");
    }

    @Override
    public Token next() throws PythiaException {

        if (source == null) {
            return null;
        }

        int state = LS_INIT;
        int inputType = IT_OTHER;
        Stack<Character> readed = new Stack<>();

        // For each character to end of input string - break when error or
        // correct lexeme found.
        while ((state != LS_ERROR)
                && !acceptState[state]
                && (inputType != IT_END)
        ) {
            char c = 0;
            // if not end of string
            if (pos < source.length()) {
                inputType = IT_OTHER;
                c = source.charAt(pos);
                // find input type
                for (int i = 0; i < inputTypes.length; ++i) {
                    if (inputPattern[inputTypes[i]].matcher("" + c).find()) {
                        inputType = inputTypes[i];
                        break;
                    }
                }
            } else {
                inputType = IT_END;
            }
            // get current state
            state = transition[state][inputType];
            // move next position only if not character that ends word
            if (state != LS_WORD) {
                pos++;
                // read character only if its part of keyword, symbol or
                // variable
                if (inputType != IT_QUOT
                    && (
                        state == LS_READ_VAR ||
                        state == LS_READ_WORD ||
                        state == LS_SYMBOL
                        )
                ) {
                    readed.push(c);
                }
            }
        }

        // if not correct lexeme throw error
        if (!acceptState[state]) {
            throw new PythiaException(PythiaError.SYMBOL_NOT_FOUND);
        }

        String lexeme = "";
        for (Character c : readed) {
            lexeme += "" + c;
        }

        // create token
        switch (state) {
            case LS_SYMBOL:
                switch (lexeme) {
                    case "(":
                        return new Token(TokenType.LPAREN);
                    case ")":
                        return new Token(TokenType.RPAREN);
                    case "=":
                        return new Token(TokenType.EQUAL);
                    case ",":
                        return new Token(TokenType.COMMA);
                    default:
                        assert false : "Unknown symbol";
                }
                break;
            case LS_VARIABLE:
                return new Token(TokenType.VARIABLE, lexeme);
            case LS_WORD:
                if (isKeyword(lexeme)) {
                    return new Token(TokenType.KEYWORD, lexeme.toUpperCase());
                } else {
                    return new Token(TokenType.VARIABLE, lexeme);
                }
            default:
                assert false : "Unknown state of lexeme";
        }
        return null;
    }

    @Override
    public void setSource(String src) {
        source = src;
        pos    = 0;
    }

    @Override
    public boolean hasNext() {
        return (source != null) && (pos < source.length());
    }

    /**
     * Checks if string is keyword.
     *
     * @param readed String to check
     * @return True if it is keyword, false otherwise
     */
    private boolean isKeyword(String readed) {
        for (int i = 0; i < keywords.length; ++i) {
            if (keywords[i].equalsIgnoreCase(readed)) {
                return true;
            }
        }
        return false;
    }

}
