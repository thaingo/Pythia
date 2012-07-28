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
import java.util.List;
import java.util.Objects;

/**
 * Parse tree is tree structure with grammar symbols.
 *
 * It is output of syntax parser and is used to final compilation of PQL.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public class ParseTree {

    private final ISymbol symbol;
    private final Token token;
    private final List<ParseTree> childrens;

    /**
     * Creates tree node.
     *
     * @param symbol symbol of token
     * @param token token attached to tree node
     */
    public ParseTree(final ISymbol symbol, final Token token) {
        childrens = new ArrayList<>();
        this.symbol = symbol;
        this.token = token;
    }

    /**
     * Adds new childs to tree node.
     *
     * @param symbol symbol of token
     * @param token token to attach to node
     */
    public void add(final ISymbol symbol, final Token token) {
        childrens.add(new ParseTree(symbol, token));
    }

     /**
     * Adds new childs to tree node.
     *
     * @param children values of new child
     */
    public void add(final ParseTree... children) {
        for (int i = 0; i < children.length; ++i) {
            childrens.add(children[i]);
        }
    }

    /**
     * Gets childerns list of this node.
     *
     * @return childrens list
     */
    public List<ParseTree> getChildrens() {
        return new ArrayList<>(childrens);
    }

    /**
     * Gets symbol of this node
     *
     * @return symbol attached to this node
     */
    public ISymbol getSymbol() {
        return symbol;
    }

    /**
     * Gets token of this node
     *
     * @return token attached to this node
     */
    public Token getToken() {
        return token;
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ParseTree)) {
            return false;
        }
        ParseTree t = (ParseTree)obj;
        return symbol == t.getSymbol()
                && (token != null ? token.equals(t.getToken())
                                  : null == t.getToken())
                && (childrens.equals(t.getChildrens()));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.symbol);
        hash = 89 * hash + Objects.hashCode(this.token);
        hash = 89 * hash + Objects.hashCode(this.childrens);
        return hash;
    }

}
