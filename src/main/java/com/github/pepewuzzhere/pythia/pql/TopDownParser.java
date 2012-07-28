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
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;


/**
 * Implementation of top-down parser algorithm for LL(1) grammars.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
class TopDownParser implements IParser {

    private static final LL1Grammar GRAMMAR = new LL1Grammar();

    public static final
            Map<LL1Grammar.NonTerminal, EnumMap<Terminal, Production>>
            PARSE_TABLE = new EnumMap<>(LL1Grammar.NonTerminal.class);

    static {
        buildParseTable();
        Collections.unmodifiableMap(PARSE_TABLE);
    }

    @Override
    public ParseTree parse(Token[] tokens) throws PythiaException {
        // check arguments
        if (tokens == null || tokens.length == 0) {
            throw new PythiaException(PythiaError.INVALID_ARGUMENS);
        }

        // init stack
        final Stack<ISymbol> ss = new Stack<>();
        ss.push(Terminal.END);
        ss.push(LL1Grammar.NonTerminal.STMT_START);

        // init parse tree and symbol s
        final Stack<ParseTree> treeNodes = new Stack<>();
        ParseTree tree = null;
        ParseTree node = null;
        treeNodes.push(tree);
        ISymbol s;

        // init next token on input - a
        int i = 0;
        Token a;

        // code of symbol that ends current parse tree node (end of production)
        final Stack<Integer> endNodeIndex = new Stack<>();
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
            s = ss.pop();

            // if end of GRAMMAR - end parsing
            if (s == Terminal.END) {
                break;
            }

            if (s == Terminal.EPSILON) {
                continue;
            }

            Terminal inputCode;
            // get next input token
            if (i < tokens.length) {
                a = tokens[i];
                // get GRAMMAR symbol of input token
                inputCode = GRAMMAR.getCode(a);
            } else {
                a = null;
                inputCode = Terminal.END;
            }

            // get current parse tree node

            // if s is terminal
            if (s.isTerminal()) {
                // if s == a - go to next token
                if (s.equals(inputCode)) {
                    if (node != null) {
                        node.add(s, a); // add to parse tree
                        i++;
                    } else {
                        throw new PythiaException(PythiaError.SYNTAX_ERROR);
                    }
                } else { // syntax error
                    throw new PythiaException(PythiaError.SYNTAX_ERROR);
                }
            } else { // get production of s and put it on stack
                try {
                    Production p = PARSE_TABLE.get((LL1Grammar.NonTerminal)s)
                                              .get(inputCode);
                    if (p != null) {
                        endNodeIndex.push(ss.size());
                        ISymbol[] symbols = p.getSymbols();
                        for (int j = symbols.length - 1; j >= 0; --j) {
                            ss.push(symbols[j]);
                        }

                        // build tree node
                        ParseTree newNode = new ParseTree(s, null);

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
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new PythiaException(PythiaError.SYNTAX_ERROR);
                }
            }

        }

        return tree;
    }

    /*
     * Create parse table for this LL(1) GRAMMAR.
     *
     * Parse table T[A, a] = prod, where:
     *     A - non terminal symbol,
     *     a - token,
     *     prod - production
     */
    private static void buildParseTable() {
        final ISymbol[] productions = GRAMMAR.getGrammar();
        // for each GRAMMAR rules prod: A -> w
        for (int i = 0; i < productions.length; ++i) {
            if (productions[i].isTerminal()) {
                continue;
            }
            LL1Grammar.NonTerminal prod = (LL1Grammar.NonTerminal)productions[i];
            // for each production p of w
            Production[] w = prod.getProductions();
            for (int j = 0; j < w.length; ++j) {
                Production p = w[j];
                // for each a in First(p)
                Terminal[] first = first(p.getSymbols());
                boolean hasEpsilon = false;
                for (int k = 0; k < first.length; ++k) {
                    // Table[A, a] = p

                    if (!PARSE_TABLE.containsKey(prod)) {
                        PARSE_TABLE.put(
                            prod,
                            new EnumMap<Terminal, Production>(Terminal.class)
                        );
                    }

                    PARSE_TABLE.get(prod).put(first[k], p);

                    if (first[k] == Terminal.EPSILON) {
                        hasEpsilon = true;
                    }
                }
                if (hasEpsilon) {
                    Terminal[] follow = follow(prod);
                    for (int k = 0; k < follow.length; ++k) {
                        PARSE_TABLE.get(prod).put(follow[k], p);
                    }
                    PARSE_TABLE.get(prod).put(Terminal.END, p);
                }
            }
        }
    }

    /**
     * Gets token list that could start providen production
     *
     * @param symbol production symbol list
     * @return list of token
     */
    static Terminal[] first(ISymbol[] symbol) {
        List<Terminal> first = new ArrayList<>();
        if (symbol != null && symbol.length != 0) {
            int j = 0;
            ISymbol s = Terminal.EPSILON;
            while (s == Terminal.EPSILON && (j < symbol.length)) {
                s = symbol[j];
                j++;

            }
            if (!s.isTerminal()) {
                LL1Grammar.NonTerminal t  = (LL1Grammar.NonTerminal)s;
                final Production[] p = t.getProductions();
                for (int i = 0; i < p.length; ++i) {
                    first.addAll(
                        Arrays.asList(
                            first(p[i].getSymbols())
                        )
                    );
                }
            } else {
                first.add((Terminal)s);
            }
        }
        return first.toArray(new Terminal[0]);
    }

    /**
     * Gets token list that could follow providen non-terminal symbol
     *
     * @param symbol production symbol list
     * @return list of token symbol codes
     */
    static Terminal[] follow(LL1Grammar.NonTerminal symbol) {
        final List<ISymbol> follow = new ArrayList<>();

        final ISymbol[] contains = GRAMMAR.getSymbolsWith(symbol);

        // for each non-terminal symbol containing provided symbol
        for (int i = 0; i < contains.length; ++i) {
            LL1Grammar.NonTerminal current =
                    (LL1Grammar.NonTerminal)contains[i];
            Production[] productions = current.getProductions();

            // for each production of this non-terminal symbol
            for (int j = 0; j < productions.length; ++j) {

                // if this produciton has this symbol
                if (productions[j].hasSymbol(symbol)) {

                    // get next non-epsilon symbol
                    ISymbol s = productions[j].nextSymbol(symbol);
                    while ((s != null) && (s == Terminal.EPSILON)) {
                        s = productions[j].nextSymbol(s);
                    }
                    // if s exists FOLLOW(symbol) includes FIRST(s)
                    if (s != null) {
                        follow.addAll(
                            Arrays.asList(first(new ISymbol[] {s}))
                        );
                    } else {
                        // is s not exists FOLLOW(symbol) includes
                        // FOLLOW(production, that contains symbol)
                        follow.addAll(
                            Arrays.asList(
                                follow((LL1Grammar.NonTerminal)contains[i])
                            )
                        );
                    }
                }
            }
        }

        return follow.toArray(new Terminal[0]);
    }

}
