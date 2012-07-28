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
 * Interface for PQL grammars.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public abstract class IGrammar {

    /**
     * Gets list of grammar productions.
     *
     * @return list of grammar productions.
     */
    abstract ISymbol[] getGrammar();

    /**
     * Gets list of symbols containing providen symbol.
     *
     * @param symbol Symbol code to find in non-terminal symbols
     * @return
     */
    abstract ISymbol[] getSymbolsWith(ISymbol symbol);

    /**
     * Gets grammar code of providen symbol.
     *
     * @param symbol Symbol
     * @return Code of symbol or -1
     */
    Terminal getCode(Token symbol) {
        for (Terminal t : Terminal.values()) {
            if (t.grammarEquals(symbol)) {
                return t;
            }
        }
        return null;
    }
}