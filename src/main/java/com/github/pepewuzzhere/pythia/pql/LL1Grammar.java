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

/**
 * Definition of LL(1) Grammar used by PQL.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public class LL1Grammar extends IGrammar {

    /** <create_keyspace_stmt> ::= <KEYSPACE> <VAR> */
    @SymbolNonTerminal public final int STMT_CREATE_KEYSPACE     = 23;
    /** <drop_keyspace_stmt> ::= <KILL> <VAR> */
    @SymbolNonTerminal public final int STMT_DROP_KEYSPACE       = 24;
    /** <use_keyspace_stmt> ::= <USE> <VAR> */
    @SymbolNonTerminal public final int STMT_USE_KEYSPACE        = 25;
    /** <create_columnfamily_stmt> ::= <CREATE> <COLUMNFAMILY> <VAR> */
    @SymbolNonTerminal public final int STMT_CREATE_COLUMNFAMILY = 26;
    /** <drop_columnfamily_stmt> ::= <DROP> <COLUMNFAMILY> <VAR> */
    @SymbolNonTerminal public final int STMT_DROP_COLUMNFAMILY   = 27;
    /** <key_values_list'> ::= ,<var><key_values_list'>|Epsilon */
    @SymbolNonTerminal public final int KEY_VALUES_LIST_PRIM     = 28;
    /** <key_values_list> ::= <KEY>=<VAR><key_values_list'> */
    @SymbolNonTerminal public final int KEY_VALUES_LIST          = 29;
    /** <insert_stmt> ::= <INSERT><INTO><VAR>(<key_values_list>) */
    @SymbolNonTerminal public final int STMT_INSERT              = 30;
    /** <where_stmt> ::= <WHERE><KEY>=<VAR> */
    @SymbolNonTerminal public final int WHERE                    = 31;
    /** <update_stmt> ::= <UPDATE><VAR><SET><key_values_list> */
    @SymbolNonTerminal public final int STMT_UPDATE              = 32;
    /** <delete_stmt> ::= <DELETE><FROM><VAR><where_stmt> */
    @SymbolNonTerminal public final int STMT_DELETE              = 33;
    /** <select_stmt> ::= <SELECT><FROM><VAR><where_stmt> */
    @SymbolNonTerminal public final int STMT_SELECT              = 34;
    /**
     * <start_stmt> ::= <create_keyspace_stmt> | <drop_keyspace_stmt> |
     * <use_keyspace_stmt> | <create_columnfamily_stmt> |
     * <drop_columnfamily_stmt> | <insert_stmt> | <update_stmt> |
     * <delete_stmt> | <select_stmt>
     */
    @SymbolNonTerminal public final int STMT_START               = 35;

    /** count of nonterminal symbols */
    public static final int NONTERMINAL_COUNT = 14;

    /** max code of nonterminal symbols */
    public static final int NONTERMINAL_MAX = 35;

    private int[] grammar;

    /**
     * Create LL(1) grammar. Insert grammar of PQL.
     */
    public LL1Grammar() {
        // <create_keyspace_stmt> ::= <KEYSPACE> <VAR>
        symbols.put(STMT_CREATE_KEYSPACE, new NonTerminal(
            new Production(KEY_KEYSPACE, VAR)
        ));

        // <drop_keyspace_stmt> ::= <KILL> <VAR>
        symbols.put(STMT_DROP_KEYSPACE, new NonTerminal(
            new Production(KEY_KILL, VAR)
        ));

        // <use_keyspace_stmt> ::= <USE> <VAR>
        symbols.put(STMT_USE_KEYSPACE, new NonTerminal(
            new Production(KEY_USE, VAR)
        ));

        // <create_columnfamily_stmt> ::= <CREATE> <COLUMNFAMILY> <VAR>
        symbols.put(STMT_CREATE_COLUMNFAMILY, new NonTerminal(
            new Production(KEY_CREATE, KEY_COLUMNFAMILY, VAR)
        ));

        // <drop_columnfamily_stmt> ::= <DROP> <COLUMNFAMILY> <VAR>
        symbols.put(STMT_DROP_COLUMNFAMILY, new NonTerminal(
            new Production(KEY_DROP, KEY_COLUMNFAMILY, VAR)
        ));

        // <key_values_list'> ::= ,<var><key_values_list'>|Epsilon
        symbols.put(KEY_VALUES_LIST_PRIM, new NonTerminal(
            new Production(
                EPSILON
            ),
            new Production(
                SYMBOL_COMMA, VAR, SYMBOL_EQUAL, VAR, KEY_VALUES_LIST_PRIM
            )
        ));

        // <key_values_list> ::= <KEY>=<VAR><key_values_list'>
        symbols.put(KEY_VALUES_LIST, new NonTerminal(
            new Production(
                KEY_KEY, SYMBOL_EQUAL, VAR, KEY_VALUES_LIST_PRIM
            )
        ));

        // <insert_stmt> ::= <INSERT><INTO><VAR>(<key_values_list>)
        symbols.put(STMT_INSERT, new NonTerminal(
            new Production(
                KEY_INSERT, KEY_INTO, VAR, SYMBOL_LPAREN, KEY_VALUES_LIST,
                SYMBOL_RPAREN
            )
        ));

        // <where_stmt> ::= <WHERE><KEY>=<VAR>
        symbols.put(WHERE, new NonTerminal(
            new Production(KEY_WHERE, KEY_KEY, SYMBOL_EQUAL, VAR)
        ));

        // <update_stmt> ::= <UPDATE><VAR><SET><key_values_list>
        symbols.put(STMT_UPDATE, new NonTerminal(
            new Production(KEY_UPDATE, VAR, KEY_SET, KEY_VALUES_LIST)
        ));

        // <delete_stmt> ::= <DELETE><FROM><VAR><where_stmt>
        symbols.put(STMT_DELETE, new NonTerminal(
            new Production(KEY_DELETE, KEY_FROM, VAR, WHERE)
        ));

        // <select_stmt> ::= <SELECT><FROM><VAR><where_stmt>
        symbols.put(STMT_SELECT, new NonTerminal(
            new Production(KEY_SELECT, KEY_FROM, VAR, WHERE)
        ));

        // <start_stmt> ::= <create_keyspace_stmt> | <drop_keyspace_stmt> |
        // <use_keyspace_stmt> | <create_columnfamily_stmt> |
        // <drop_columnfamily_stmt> | <insert_stmt> | <update_stmt> |
        // <delete_stmt> | <select_stmt>
        symbols.put(STMT_START, new NonTerminal(
            new Production(STMT_CREATE_KEYSPACE),
            new Production(STMT_DROP_KEYSPACE),
            new Production(STMT_USE_KEYSPACE),
            new Production(STMT_CREATE_COLUMNFAMILY),
            new Production(STMT_DROP_COLUMNFAMILY),
            new Production(STMT_INSERT),
            new Production(STMT_UPDATE),
            new Production(STMT_SELECT),
            new Production(STMT_DELETE)
        ));

        grammar = new int[] {
            STMT_CREATE_KEYSPACE,
            STMT_DROP_KEYSPACE,
            STMT_USE_KEYSPACE,
            STMT_CREATE_COLUMNFAMILY,
            STMT_DROP_COLUMNFAMILY,
            KEY_VALUES_LIST_PRIM,
            KEY_VALUES_LIST,
            STMT_INSERT,
            WHERE,
            STMT_UPDATE,
            STMT_DELETE,
            STMT_SELECT,
            STMT_START
        };
    }

    @Override
    public int[] getGrammar() {
        return grammar;
    }

    @Override
    public Integer[] getSymbolsWith(int symbol) {
        ArrayList<Integer> with = new ArrayList<>();

        // for each grammar rules P: A -> w
        for (int i = 0; i < grammar.length; ++i) {
            if (grammar[i] == symbol) {
                continue;
            }
            NonTerminal t = (NonTerminal)symbols.get(grammar[i]);

            // for each production of this grammar rule
            Production[] p = t.getProductions();
            for (int j = 0; j < p.length; ++j) {
                int[] s = p[j].getSymbols();

                // check if this production contains symbol to find
                for (int k = 0; k < s.length; ++k) {
                    if (s[k] == symbol) {
                        with.add(grammar[i]);
                    }
                }
            }
        }
        return with.toArray(new Integer[0]);
    }

}
