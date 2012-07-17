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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/**
 * Implementation of top-down parser algorithm for LL(1) grammars.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
class TopDownParser implements IParser {

    private final LL1Grammar grammar;
    private final Production[][] parseTable;

    /**
     * Creates top-down parser for LL(1) grammar
     *
     * @param grammar LL(1) grammar
     */
    TopDownParser(LL1Grammar grammar) {
        this.grammar = grammar;
        parseTable = new Production[LL1Grammar.NONTERMINAL_MAX + 1]
                                   [LL1Grammar.TERMINAL_MAX + 1];
        try {
            buildParseTable();
        } catch (PythiaException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ParseTree parse(Token[] tokens) throws PythiaException {
        // check arguments
        if (tokens == null || tokens.length == 0) {
            throw new PythiaException(PythiaError.INVALID_ARGUMENS);
        }

        // init stack
        Stack<Integer> ss = new Stack<>();
        ss.push(grammar.END);
        ss.push(grammar.STMT_START);

        // init parse tree and symbol S
        Stack<ParseTree> treeNodes = new Stack<>();
        ParseTree tree = null;
        ParseTree node = null;
        treeNodes.push(tree);
        int S;

        // init next token on input - a
        int i = 0;
        Token a;

        // code of symbol that ends current parse tree node (end of production)
        Stack<Integer> endNodeIndex = new Stack<>();
        endNodeIndex.push(ss.size());

        // while stack is not empty
        while (!ss.isEmpty()) {
            // get symbol from stack
            while (
                !endNodeIndex.isEmpty() &&
                (ss.size() == endNodeIndex.get(endNodeIndex.size() - 1))
            ) {
                node = treeNodes.pop();
                endNodeIndex.pop();
            }
            S = ss.pop();

            // if end of grammar - end parsing
            if (S == grammar.END) {
                break;
            }

            if (S == grammar.EPSILON) {
                continue;
            }

            int inputCode;
            // get next input token
            if (i < tokens.length) {
                a = tokens[i];
                // get grammar code of input token
                inputCode = grammar.getCode(a);
            } else {
                a = null;
                inputCode = grammar.END;
            }

            // get current parse tree node

            // if S is terminal
            if (grammar.isTerminal(S)) {
                // if S == a - go to next token
                if (S == inputCode) {
                    if (node != null) {
                        node.add(a); // add to parse tree
                        i++;
                    } else {
                        throw new PythiaException(PythiaError.SYNTAX_ERROR);
                    }
                } else { // syntax error
                    throw new PythiaException(PythiaError.SYNTAX_ERROR);
                }
            } else { // get production of S and put it on stack
                try {
                    Production p = parseTable[S][inputCode];
                    if (p != null) {
                        endNodeIndex.push(ss.size());
                        int[] symbols = p.getSymbols();
                        for (int j = symbols.length - 1; j >= 0; --j) {
                            ss.push(symbols[j]);
                        }

                        // build tree node
                        ParseTree newNode = new ParseTree(grammar.getSymbol(S));

                        if (tree == null) {
                            tree = newNode;
                        } else {
                            node.add(newNode);
                        }
                        treeNodes.push(node);
                        node = newNode;
                    } else { // syntax error
                        throw new PythiaException(PythiaError.SYNTAX_ERROR);
                    }
                } catch(ArrayIndexOutOfBoundsException e) {
                    throw new PythiaException(PythiaError.SYNTAX_ERROR);
                }
            }

        }

        return tree;
    }

    /**
     * Create parse table for this LL(1) grammar.
     *
     * @return Parse table T[A, a] = P, where:
     *         A - non terminal symbol,
     *         a - token,
     *         P - production
     */
    final Production[][] buildParseTable() throws PythiaException {
        int[] productions = grammar.getGrammar();
        // for each grammar rules P: A -> w
        for (int i = 0; i < productions.length; ++i) {
            NonTerminal P = (NonTerminal)grammar.getSymbol(productions[i]);
            // for each production  p of w
            Production[] w = P.getProductions();
            for (int j = 0; j < w.length; ++j) {
                Production p = w[j];
                // for each a in First(p)
                Integer[] first = first(p.getSymbols());
                boolean hasEpsilon = false;
                for (int k = 0; k < first.length; ++k) {
                    // Table[A, a] = p
                    parseTable[productions[i]][first[k]] = p;
                    if (first[k] == grammar.EPSILON) {
                        hasEpsilon = true;
                    }
                }
                if (hasEpsilon) {
                    Integer[] follow = follow(productions[i]);
                    for (int k = 0; k < follow.length; ++k) {
                        parseTable[productions[i]][follow[k]] = p;
                    }
                    parseTable[productions[i]][grammar.END] = p;
                }
            }
        }

        return parseTable;
    }

    /**
     * Gets token list that could start providen production
     *
     * @param symbol Production symbol list
     * @return List of token
     * @throws PythiaException
     */
    Integer[] first(int[] symbol) throws PythiaException {
        ArrayList<Integer> first = new ArrayList<>();
        if (symbol != null && symbol.length != 0) {
            int j = 0;
            int s = grammar.EPSILON;
            while (s == grammar.EPSILON && (j < symbol.length)) {
                s = symbol[j];
                j++;

            }
            if (!grammar.isTerminal(s)) {
                NonTerminal t  = (NonTerminal)grammar.getSymbol(s);
                Production[] p = t.getProductions();
                for (int i = 0; i < p.length; ++i) {
                    first.addAll(
                        Arrays.asList(
                            first(p[i].getSymbols())
                        )
                    );
                }
            } else {
                first.add(s);
            }
        }
        return first.toArray(new Integer[0]);
    }

    /**
     * Gets token list that could follow providen non-terminal symbol
     *
     * @param symbol Production symbol list
     * @return List of token symbol codes
     * @throws PythiaException
     */
    Integer[] follow(int symbol) throws PythiaException {
        ArrayList<Integer> follow = new ArrayList<>();

        Integer[] contains = grammar.getSymbolsWith(symbol);

        // for each non-terminal symbol containing provided symbol
        for (int i = 0; i < contains.length; ++i) {
            NonTerminal current = (NonTerminal)grammar.getSymbol(contains[i]);
            Production[] productions = current.getProductions();

            // for each production of this non-terminal symbol
            for (int j = 0; j < productions.length; ++j) {

                // if this produciton has this symbol
                if (productions[j].hasSymbol(symbol)) {

                    // get next non-epsilon symbol
                    int s = productions[j].nextSymbol(symbol);
                    while ((s != -1) && (s == grammar.EPSILON)) {
                        s = productions[j].nextSymbol(s);
                    }
                    // if s exists FOLLOW(symbol) includes FIRST(s)
                    if (s != -1) {
                        follow.addAll(
                            Arrays.asList(first(new int[] {s}))
                        );
                    } else {
                        // is s not exists FOLLOW(symbol) includes
                        // FOLLOW(production, that contains symbol)
                        follow.addAll(Arrays.asList(follow(contains[i])));
                    }
                }
            }
        }

        return follow.toArray(new Integer[0]);
    }

}
