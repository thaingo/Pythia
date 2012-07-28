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
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
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
class TableDrivenTokenIterator implements ITokenIterator {

    private static enum LexemeState {
        LS_INIT,            // start position
        LS_READ_WORD,       // reading word lexeme
        LS_WORD,            // return word
        LS_SYMBOL,          // return symbol
        LS_READ_VAR,        // reading variable (beetwen "")
        LS_VARIABLE,        // return variable
        LS_ERROR;           // error
    }

    private static enum InputType {
        IT_LETTER(Pattern.compile("[a-zA-Z]")), // [a-zA-Z]
        IT_WHITESPACE(Pattern.compile("\\s")),  // [ \t\n]+
        IT_SYMBOL(Pattern.compile("[=(),]")),   // [()=,]
        IT_QUOT(Pattern.compile("\"")),         // ["]
        IT_OTHER,                               // anything else
        IT_END;                                 // end of source

        private final Pattern pattern;

        InputType() {
            this.pattern = null;
        }

        InputType(Pattern pattern) {
            this.pattern = pattern;
        }

        Pattern getPattern() {
            return pattern;
        }
    }

    private static final Map<LexemeState, EnumMap<InputType, LexemeState>>
            TRANSITIONS = new EnumMap<>(LexemeState.class);

    static {
        TRANSITIONS.put(LexemeState.LS_INIT,
                new EnumMap<InputType, LexemeState>(InputType.class));
        TRANSITIONS.put(LexemeState.LS_READ_WORD,
                new EnumMap<InputType, LexemeState>(InputType.class));
        TRANSITIONS.put(LexemeState.LS_WORD,
                new EnumMap<InputType, LexemeState>(InputType.class));
        TRANSITIONS.put(LexemeState.LS_SYMBOL,
                new EnumMap<InputType, LexemeState>(InputType.class));
        TRANSITIONS.put(LexemeState.LS_READ_VAR,
                new EnumMap<InputType, LexemeState>(InputType.class));
        TRANSITIONS.put(LexemeState.LS_VARIABLE,
                new EnumMap<InputType, LexemeState>(InputType.class));

        TRANSITIONS.get(LexemeState.LS_INIT).put(
                InputType.IT_LETTER, LexemeState.LS_READ_WORD);
        TRANSITIONS.get(LexemeState.LS_INIT).put(
                InputType.IT_WHITESPACE, LexemeState.LS_INIT);
        TRANSITIONS.get(LexemeState.LS_INIT).put(
                InputType.IT_SYMBOL, LexemeState.LS_SYMBOL);
        TRANSITIONS.get(LexemeState.LS_INIT).put(
                InputType.IT_QUOT, LexemeState.LS_READ_VAR);
        TRANSITIONS.get(LexemeState.LS_INIT).put(
                InputType.IT_OTHER, LexemeState.LS_ERROR);
        TRANSITIONS.get(LexemeState.LS_INIT).put(
                InputType.IT_END, LexemeState.LS_ERROR);

        TRANSITIONS.get(LexemeState.LS_READ_WORD).put(
                InputType.IT_LETTER, LexemeState.LS_READ_WORD);
        TRANSITIONS.get(LexemeState.LS_READ_WORD).put(
                InputType.IT_WHITESPACE, LexemeState.LS_WORD);
        TRANSITIONS.get(LexemeState.LS_READ_WORD).put(
                InputType.IT_SYMBOL, LexemeState.LS_WORD);
        TRANSITIONS.get(LexemeState.LS_READ_WORD).put(
                InputType.IT_QUOT, LexemeState.LS_READ_WORD);
        TRANSITIONS.get(LexemeState.LS_READ_WORD).put(
                InputType.IT_OTHER, LexemeState.LS_ERROR);
        TRANSITIONS.get(LexemeState.LS_READ_WORD).put(
                InputType.IT_END, LexemeState.LS_WORD);

        TRANSITIONS.get(LexemeState.LS_WORD).put(
                InputType.IT_LETTER, LexemeState.LS_ERROR);
        TRANSITIONS.get(LexemeState.LS_WORD).put(
                InputType.IT_WHITESPACE, LexemeState.LS_ERROR);
        TRANSITIONS.get(LexemeState.LS_WORD).put(
                InputType.IT_SYMBOL, LexemeState.LS_ERROR);
        TRANSITIONS.get(LexemeState.LS_WORD).put(
                InputType.IT_QUOT, LexemeState.LS_ERROR);
        TRANSITIONS.get(LexemeState.LS_WORD).put(
                InputType.IT_OTHER, LexemeState.LS_ERROR);
        TRANSITIONS.get(LexemeState.LS_WORD).put(
                InputType.IT_END, LexemeState.LS_ERROR);

        TRANSITIONS.get(LexemeState.LS_SYMBOL).put(
                InputType.IT_LETTER, LexemeState.LS_ERROR);
        TRANSITIONS.get(LexemeState.LS_SYMBOL).put(
                InputType.IT_WHITESPACE, LexemeState.LS_ERROR);
        TRANSITIONS.get(LexemeState.LS_SYMBOL).put(
                InputType.IT_SYMBOL, LexemeState.LS_ERROR);
        TRANSITIONS.get(LexemeState.LS_SYMBOL).put(
                InputType.IT_QUOT, LexemeState.LS_ERROR);
        TRANSITIONS.get(LexemeState.LS_SYMBOL).put(
                InputType.IT_OTHER, LexemeState.LS_ERROR);
        TRANSITIONS.get(LexemeState.LS_SYMBOL).put(
                InputType.IT_END, LexemeState.LS_ERROR);

        TRANSITIONS.get(LexemeState.LS_READ_VAR).put(
                InputType.IT_LETTER, LexemeState.LS_READ_VAR);
        TRANSITIONS.get(LexemeState.LS_READ_VAR).put(
                InputType.IT_WHITESPACE, LexemeState.LS_READ_VAR);
        TRANSITIONS.get(LexemeState.LS_READ_VAR).put(
                InputType.IT_SYMBOL, LexemeState.LS_READ_VAR);
        TRANSITIONS.get(LexemeState.LS_READ_VAR).put(
                InputType.IT_QUOT, LexemeState.LS_VARIABLE);
        TRANSITIONS.get(LexemeState.LS_READ_VAR).put(
                InputType.IT_OTHER, LexemeState.LS_READ_VAR);
        TRANSITIONS.get(LexemeState.LS_READ_VAR).put(
                InputType.IT_END, LexemeState.LS_ERROR);

        TRANSITIONS.get(LexemeState.LS_VARIABLE).put(
                InputType.IT_LETTER, LexemeState.LS_ERROR);
        TRANSITIONS.get(LexemeState.LS_VARIABLE).put(
                InputType.IT_WHITESPACE, LexemeState.LS_ERROR);
        TRANSITIONS.get(LexemeState.LS_VARIABLE).put(
                InputType.IT_SYMBOL, LexemeState.LS_ERROR);
        TRANSITIONS.get(LexemeState.LS_VARIABLE).put(
                InputType.IT_QUOT, LexemeState.LS_ERROR);
        TRANSITIONS.get(LexemeState.LS_VARIABLE).put(
                InputType.IT_OTHER, LexemeState.LS_ERROR);
        TRANSITIONS.get(LexemeState.LS_VARIABLE).put(
                InputType.IT_END, LexemeState.LS_ERROR);

    }

    private String source;  // source string
    private int pos = 0; // position of current character of source

    // correct input types
    private static final Set<InputType> INPUT_TYPES =
            EnumSet.of(
                InputType.IT_LETTER, InputType.IT_WHITESPACE,
                InputType.IT_SYMBOL, InputType.IT_QUOT
            );

    // states that are accepted as correct lexeme input
    private static final Set<LexemeState> ACCEPT_STATE =
            EnumSet.of(
                LexemeState.LS_WORD, LexemeState.LS_SYMBOL,
                LexemeState.LS_VARIABLE
            );

    // KEYWORDS of PQL
    private static final String[] KEYWORDS = {
        "CREATE", "USE", "DROP", "SELECT", "UPDATE", "INSERT", "DELETE",
        "KEYSPACE", "COLUMNFAMILY", "KEY", "FROM", "WHERE", "SET",
        "INTO", "VALUES", "KILL"
    };

    @Override
    public Token next() throws PythiaException {

        if (source == null) {
            return null;
        }

        LexemeState state = LexemeState.LS_INIT;
        InputType inputType = InputType.IT_OTHER;
        final Stack<Character> readed = new Stack<>();

        // For each character to end of input string - break when error or
        // correct lexeme found.
        while ((state != LexemeState.LS_ERROR)
                && !ACCEPT_STATE.contains(state)
                && (inputType != InputType.IT_END)
        ) {
            char c = 0;
            // if not end of string
            if (pos < source.length()) {
                inputType = InputType.IT_OTHER;
                c = source.charAt(pos);
                // find input type
                for (InputType input : INPUT_TYPES) {
                    final Pattern pattern = input.getPattern();
                    if (pattern != null && pattern.matcher("" + c).find()) {
                        inputType = input;
                        break;
                    }
                }

            } else {
                inputType = InputType.IT_END;
            }
            // get current state
            state = from(state, inputType);
            // move next position only if not character that ends word
            if (state != LexemeState.LS_WORD) {
                pos++;
                // read character only if its part of keyword, symbol or
                // variable
                if (inputType != InputType.IT_QUOT
                    && (
                        state == LexemeState.LS_READ_VAR ||
                        state == LexemeState.LS_READ_WORD ||
                        state == LexemeState.LS_SYMBOL
                        )
                ) {
                    readed.push(c);
                }
            }
        }

        // if not correct lexeme throw error
        if (!ACCEPT_STATE.contains(state)) {
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
    public void setSource(final String src) {
        source = src;
        pos    = 0;
    }

    @Override
    public boolean hasNext() {
        return (source != null) && (pos < source.length());
    }

    /*
     * Checks if string is keyword.
     *
     * @param readed string to check
     * @return true if it is keyword, false otherwise
     */
    private boolean isKeyword(String readed) {
        for (int i = 0; i < KEYWORDS.length; ++i) {
            if (KEYWORDS[i].equalsIgnoreCase(readed)) {
                return true;
            }
        }
        return false;
    }

    private static LexemeState from(
        final LexemeState state, final InputType type
    ) {
        return TRANSITIONS.get(state).get(type);
    }

}
