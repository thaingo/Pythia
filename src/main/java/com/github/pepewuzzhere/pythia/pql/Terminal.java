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

/**
 * PQL grammar - terminal symbol list.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public enum Terminal implements ISymbol {

    /** KEYSPACE keyword */
    KEY_KEYSPACE(new Token(TokenType.KEYWORD, "KEYSPACE")),
    /** KILL keyword */
    KEY_KILL(new Token(TokenType.KEYWORD, "KILL")),
    /** USE keyword */
    KEY_USE(new Token(TokenType.KEYWORD, "USE")),
    /** CREATE keyword */
    KEY_CREATE(new Token(TokenType.KEYWORD, "CREATE")),
    /** COLUMNFAMILY keyword */
    KEY_COLUMNFAMILY(new Token(TokenType.KEYWORD, "COLUMNFAMILY")),
    /** DROP keyword */
    KEY_DROP(new Token(TokenType.KEYWORD, "DROP")),
    /** INSERT keyword */
    KEY_INSERT(new Token(TokenType.KEYWORD, "INSERT")),
    /** INTO keyword */
    KEY_INTO(new Token(TokenType.KEYWORD, "INTO")),
    /** VALUES keyword */
    KEY_VALUES(new Token(TokenType.KEYWORD, "VALUES")),
    /** UPDATE keyword */
    KEY_UPDATE(new Token(TokenType.KEYWORD, "UPDATE")),
    /** SELECT keyword */
    KEY_SELECT(new Token(TokenType.KEYWORD, "SELECT")),
    /** FROM keyword */
    KEY_FROM(new Token(TokenType.KEYWORD, "FROM")),
    /** DELETE keyword */
    KEY_DELETE(new Token(TokenType.KEYWORD, "DELETE")),
    /** WHERE keyword */
    KEY_WHERE(new Token(TokenType.KEYWORD, "WHERE")),
    /** KEY keyword */
    KEY_KEY(new Token(TokenType.KEYWORD, "KEY")),
    /** SET keyword */
    KEY_SET(new Token(TokenType.KEYWORD, "SET")),
    /** ( symbol */
    SYMBOL_LPAREN(new Token(TokenType.LPAREN)),
    /** ) symbol */
    SYMBOL_RPAREN(new Token(TokenType.RPAREN)),
    /** = symbol */
    SYMBOL_EQUAL(new Token(TokenType.EQUAL)),
    /** , symbol */
    SYMBOL_COMMA(new Token(TokenType.COMMA)),
    /** variable */
    VAR(new Token(TokenType.VARIABLE)),
    /** empty symbol */
    EPSILON(new Token(TokenType.EPSILON)),
    /** end of input */
    END;

    private final Token token;

    Terminal() {
        token = null;
    }

    Terminal(Token token) {
        this.token = token;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    /**
     * Checks if this symbol is grammatically equals with providen.
     *
     * @param symbol symbol to compare
     * @return true if symbol is grammatically equal, false otherwise
     */
    boolean grammarEquals(final Token symbol) {
        if (this.token == null) {
            return false;
        }
        final boolean arg2 = (this.token.getSymbol() == null
                           ? null == symbol.getSymbol()
                           : this.token.getSymbol().equals(symbol.getSymbol()));

        boolean arg3 = true;
        if (token.getSymbol() != TokenType.VARIABLE) {
            arg3 = this.token.getValue() == null
                 ? null == symbol.getValue()
                 : this.token.getValue().equals(symbol.getValue());
        }
        return arg2 && arg3;
    }
}