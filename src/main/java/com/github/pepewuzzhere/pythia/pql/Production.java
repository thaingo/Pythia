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
public class Production {

    private final int[] symbols;

    /**
     * Creates production - list of symbols codes to produce nonterminal symbol.
     *
     * @param symbols
     */
    public Production(int... symbols) {
        this.symbols = symbols;
    }

    /**
     * Gets symbol list for this production.
     *
     * @return Symbol list
     */
    public int[] getSymbols() {
        return symbols;
    }

    /**
     * Checks if this production contains provided symbol.
     *
     * @param symbol Symbol code to find
     * @return True if production contains this symbol, false otherwise.
     */
    public boolean hasSymbol(int symbol) {
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
     * @param symbol Symbol code to find
     * @return Symbol next to provided or -1
     */
    public int nextSymbol(int symbol) {
        for (int i = 0; i < symbols.length; ++i) {
            if (symbols[i] == symbol) {
                if ((i + 1) < symbols.length) {
                    return symbols[i + 1];
                } else {
                    return -1;
                }
            }
        }
        return -1;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Production)) {
            return false;
        }
        int[] s1 = ((Production) obj).getSymbols();
        int[] s2 = getSymbols();

        return Arrays.equals(s1, s2);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Arrays.hashCode(this.symbols);
        return hash;
    }
}
