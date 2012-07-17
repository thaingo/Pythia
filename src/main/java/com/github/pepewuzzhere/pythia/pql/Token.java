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

import java.util.Objects;

/**
 * Representation of token - requires {@link TokenType} and optional value.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public class Token implements ISymbol {

    private final TokenType symbol;
    private final String value;

    /**
     * Construct token containing only symbol.
     *
     * @param symbol TokenType of token
     */
    public Token(TokenType symbol) {
        this.symbol = symbol;
        this.value  = null;
    }

    /**
     * Construct token containing both symbol and value.
     *
     * @param symbol TokenType of token
     * @param value Value of token
     */
    public Token(TokenType symbol, String value) {
        this.symbol = symbol;
        this.value  = value;
    }

    /**
     * Gets the symbol of token.
     *
     * @return The symbol of token.
     */
    public TokenType getSymbol() {
        return symbol;
    }

    /**
     * Gets the value of token.
     *
     * @return The value of token.
     */
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Token)
                && (symbol == null ? null == ((Token) obj).getSymbol()
                                   : symbol.equals(((Token) obj).getSymbol()))
                && (value == null ? null == ((Token) obj).getValue()
                                  : value.equals(((Token) obj).getValue()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.symbol != null ? this.symbol.hashCode() : 0);
        hash = 53 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public boolean grammarEquals(ISymbol symbol) {
        if (symbol instanceof Token) {
            Token t = (Token)symbol;
            boolean arg2 =
                    (this.symbol == null ? null == t.getSymbol()
                                         : this.symbol.equals(t.getSymbol()));

            boolean arg3 = true;
            if (t.getSymbol() != TokenType.VARIABLE) {
                arg3 = value == null ? null == t.getValue()
                                     : value.equals(t.getValue());
            }
            return arg2 && arg3;
        } else {
            return false;
        }
    }

}