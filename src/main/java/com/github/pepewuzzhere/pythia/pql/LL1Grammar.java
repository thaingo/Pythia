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
import java.util.List;

/**
 * Definition of LL(1) Grammar used by PQL.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public class LL1Grammar extends IGrammar {

    /**
     * Enmeration of nonterminal symbols used in ll1 grammar
     */
    public static enum NonTerminal implements ISymbol {
        /** {@literal <create_keyspace_stmt> ::= <KEYSPACE> <VAR>} */
        STMT_CREATE_KEYSPACE(
            new Production(Terminal.KEY_KEYSPACE, Terminal.VAR)
        ),
        /** {@literal <drop_keyspace_stmt> ::= <KILL> <VAR>} */
        STMT_DROP_KEYSPACE(
            new Production(Terminal.KEY_KILL, Terminal.VAR)
        ),
        /** {@literal <use_keyspace_stmt> ::= <USE> <VAR>} */
        STMT_USE_KEYSPACE(
            new Production(Terminal.KEY_USE, Terminal.VAR)
        ),
        /**
         * {@literal
         * <create_columnfamily_stmt> ::= <CREATE> <COLUMNFAMILY> <VAR>
         * }
         */
        STMT_CREATE_COLUMNFAMILY(
            new Production(
                Terminal.KEY_CREATE, Terminal.KEY_COLUMNFAMILY, Terminal.VAR)
        ),
        /** {@literal
         * <drop_columnfamily_stmt> ::= <DROP> <COLUMNFAMILY> <VAR>
         * }
         */
        STMT_DROP_COLUMNFAMILY(
            new Production(
                Terminal.KEY_DROP, Terminal.KEY_COLUMNFAMILY, Terminal.VAR)
        ),
        /** {@literal
         * <key_values_list'> ::= ,<var><key_values_list'>|Epsilon
         * }
         */
        KEY_VALUES_LIST_PRIM(
            new Production(
                Terminal.EPSILON
            )
        ),
        /** {@literal <key_values_list> ::= <KEY>=<VAR><key_values_list'>} */
        KEY_VALUES_LIST(
            new Production(
                Terminal.KEY_KEY, Terminal.SYMBOL_EQUAL,
                Terminal.VAR, KEY_VALUES_LIST_PRIM
            )
        ),
        /** {@literal
         * <insert_stmt> ::= <INSERT><INTO><VAR>(<key_values_list>)
         * }
         */
        STMT_INSERT(
            new Production(
                Terminal.KEY_INSERT, Terminal.KEY_INTO, Terminal.VAR,
                Terminal.SYMBOL_LPAREN, KEY_VALUES_LIST, Terminal.SYMBOL_RPAREN
            )
        ),
        /** {@literal <where_stmt> ::= <WHERE><KEY>=<VAR>} */
        WHERE(
            new Production(
                Terminal.KEY_WHERE, Terminal.KEY_KEY, Terminal.SYMBOL_EQUAL,
                Terminal.VAR
            )
        ),
        /** {@literal <update_stmt> ::= <UPDATE><VAR><SET><key_values_list>} */
        STMT_UPDATE(
            new Production(
                Terminal.KEY_UPDATE, Terminal.VAR, Terminal.KEY_SET,
                KEY_VALUES_LIST
            )
        ),
        /** {@literal <delete_stmt> ::= <DELETE><FROM><VAR><where_stmt>} */
        STMT_DELETE(
            new Production(
                Terminal.KEY_DELETE, Terminal.KEY_FROM, Terminal.VAR, WHERE
            )
        ),
        /** {@literal <select_stmt> ::= <SELECT><FROM><VAR><where_stmt>} */
        STMT_SELECT(
            new Production(
                Terminal.KEY_SELECT, Terminal.KEY_FROM, Terminal.VAR, WHERE
            )
        ),
        /**
         * {@literal
         * <start_stmt> ::= <create_keyspace_stmt> | <drop_keyspace_stmt> |
         * <use_keyspace_stmt> | <create_columnfamily_stmt> |
         * <drop_columnfamily_stmt> | <insert_stmt> | <update_stmt> |
         * <delete_stmt> | <select_stmt>
         * }
         */
        STMT_START(
            new Production(STMT_CREATE_KEYSPACE),
            new Production(STMT_DROP_KEYSPACE),
            new Production(STMT_USE_KEYSPACE),
            new Production(STMT_CREATE_COLUMNFAMILY),
            new Production(STMT_DROP_COLUMNFAMILY),
            new Production(STMT_INSERT),
            new Production(STMT_UPDATE),
            new Production(STMT_SELECT),
            new Production(STMT_DELETE)
        );

        static {
            // recurent call solving
            KEY_VALUES_LIST_PRIM.addProduction(
                new Production(
                    Terminal.SYMBOL_COMMA, Terminal.VAR, Terminal.SYMBOL_EQUAL,
                    Terminal.VAR, NonTerminal.KEY_VALUES_LIST_PRIM
                )
            );
        }

        private final List<Production> productions;

        /**
         * Creates nonterminal symbol. NonTerminal symbol consists productions
         * list used to create this symbol from terminal
         * and nonterminal symbols.
         *
         * @param productions List of productions
         */
        NonTerminal(Production... productions) {
            this.productions = new ArrayList<>(10);
            this.productions.addAll(Arrays.asList(productions));
        }

        /**
         * Gets productions list of this nonterminal symbol.
         *
         * @return List of symbols
         */
        Production[] getProductions() {
            return productions.toArray(new Production[0]);
        }

        void addProduction(final Production production) {
            this.productions.add(production);
        }

        @Override
        public boolean isTerminal() {
            return false;
        }
    }

    private static final ISymbol[] GRAMMAR = new ISymbol[] {
        NonTerminal.STMT_CREATE_KEYSPACE,
        NonTerminal.STMT_DROP_KEYSPACE,
        NonTerminal.STMT_USE_KEYSPACE,
        NonTerminal.STMT_CREATE_COLUMNFAMILY,
        NonTerminal.STMT_DROP_COLUMNFAMILY,
        NonTerminal.KEY_VALUES_LIST_PRIM,
        NonTerminal.KEY_VALUES_LIST,
        NonTerminal.STMT_INSERT,
        NonTerminal.WHERE,
        NonTerminal.STMT_UPDATE,
        NonTerminal.STMT_DELETE,
        NonTerminal.STMT_SELECT,
        NonTerminal.STMT_START
    };

    @Override
    public ISymbol[] getGrammar() {
        return Arrays.copyOf(GRAMMAR, GRAMMAR.length);
    }

    @Override
    public ISymbol[] getSymbolsWith(final ISymbol symbol) {
        final List<ISymbol> with = new ArrayList<>(GRAMMAR.length);

        // for each GRAMMAR rules P: A -> w
        for (int i = 0; i < GRAMMAR.length; ++i) {
            if ((GRAMMAR[i] == symbol) || symbol.isTerminal()) {
                continue;
            }
            final NonTerminal t = (NonTerminal)GRAMMAR[i];

            // for each production of this GRAMMAR rule
            final Production[] p = t.getProductions();
            for (int j = 0; j < p.length; ++j) {
                final ISymbol[] s = p[j].getSymbols();

                // check if this production contains symbol to find
                for (int k = 0; k < s.length; ++k) {
                    if (s[k] == symbol) {
                        with.add(GRAMMAR[i]);
                    }
                }
            }
        }
        return with.toArray(new ISymbol[0]);
    }

}
