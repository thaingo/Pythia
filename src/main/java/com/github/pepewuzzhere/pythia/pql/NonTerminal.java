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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * NonTerminal symbol class - consists of terminal and nonterminal symbol list.
 * Could be produced on many ways.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public class NonTerminal implements ISymbol {

    private ArrayList<Production> productions;

    /**
     * Creates nonterminal symbol. NonTerminal symbol consists productions
     * list used to create this symbol from terminal and nonterminal symbols.
     *
     * @param productions List of productions
     */
    public NonTerminal(Production... productions) {
        this.productions = new ArrayList<>();
        this.productions.addAll(Arrays.asList(productions));
    }

    /**
     * Gets productions list of this nonterminal symbol.
     *
     * @return List of symbols
     */
    public Production[] getProductions() {
        return productions.toArray(new Production[0]);
    }

    /**
     * Adds new production to list.
     *
     * @param p New production
     */
    public void addProduction(Production p) {
        productions.add(p);
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NonTerminal)) {
            return false;
        }
        Production[] p1 = ((NonTerminal) obj).getProductions();
        Production[] p2 = getProductions();

        return Arrays.deepEquals(p1, p2);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.productions);
        return hash;
    }

    @Override
    public boolean grammarEquals(ISymbol symbol) {
        return equals(symbol);
    }

}