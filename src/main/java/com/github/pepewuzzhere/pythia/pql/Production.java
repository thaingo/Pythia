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

import java.util.Arrays;

/**
 * Class represents list of symbols to produce nonterminal symbol.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
class Production {

    private final ISymbol[] symbols;

    /**
     * Creates production - list of symbols codes to produce nonterminal symbol.
     *
     * @param symbols list of symbol that describe production
     * @throws IllegalArgumentException if symbol list is empty
     */
    Production(final ISymbol... symbols) {
        if (symbols == null || symbols.length == 0) {
            throw new IllegalArgumentException("Symbols are required");
        }
        this.symbols = symbols;
    }

    /**
     * Gets symbol list for this production.
     *
     * @return symbol list
     */
    ISymbol[] getSymbols() {
        ISymbol[] clone = new ISymbol[symbols.length];
        System.arraycopy(symbols, 0, clone, 0, symbols.length);
        return clone;
    }

    /**
     * Checks if this production contains provided symbol.
     *
     * @param symbol symbol code to find
     * @return true if production contains this symbol, false otherwise.
     */
    boolean hasSymbol(final ISymbol symbol) {
        if (symbol == null) {
            return false;
        }
        for (int i = 0; i < symbols.length; ++i) {
            if (symbols[i] == symbol) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets next symbol in production.
     *
     * @param symbol symbol code to find
     * @return symbol next to provided or null
     */
    ISymbol nextSymbol(final ISymbol symbol) {
        if (symbol == null) {
            return null;
        }
        for (int i = 0; i < symbols.length; ++i) {
            if (symbols[i] == symbol) {
                if ((i + 1) < symbols.length) {
                    return symbols[i + 1];
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Production)) {
            return false;
        }
        final ISymbol[] s1 = ((Production) obj).getSymbols();
        final ISymbol[] s2 = getSymbols();

        return Arrays.equals(s1, s2);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Arrays.hashCode(this.symbols);
        return hash;
    }
}
