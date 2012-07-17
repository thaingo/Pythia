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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Interface for PQL grammars. Have set of terminal symbols used in grammar.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public abstract class IGrammar {

    // Terminal symbol list

    /** KEYSPACE keyword */
    @SymbolTerminal public final int KEY_KEYSPACE     = 0;
    /** KILL keyword */
    @SymbolTerminal public final int KEY_KILL         = 1;
    /** USE keyword */
    @SymbolTerminal public final int KEY_USE          = 2;
    /** CREATE keyword */
    @SymbolTerminal public final int KEY_CREATE       = 3;
    /** COLUMNFAMILY keyword */
    @SymbolTerminal public final int KEY_COLUMNFAMILY = 4;
    /** DROP keyword */
    @SymbolTerminal public final int KEY_DROP         = 5;
    /** INSERT keyword */
    @SymbolTerminal public final int KEY_INSERT       = 6;
    /** INTO keyword */
    @SymbolTerminal public final int KEY_INTO         = 7;
    /** VALUES keyword */
    @SymbolTerminal public final int KEY_VALUES       = 8;
    /** UPDATE keyword */
    @SymbolTerminal public final int KEY_UPDATE       = 9;
    /** SELECT keyword */
    @SymbolTerminal public final int KEY_SELECT       = 10;
    /** FROM keyword */
    @SymbolTerminal public final int KEY_FROM         = 11;
    /** DELETE keyword */
    @SymbolTerminal public final int KEY_DELETE       = 12;
    /** WHERE keyword */
    @SymbolTerminal public final int KEY_WHERE        = 13;
    /** KEY keyword */
    @SymbolTerminal public final int KEY_KEY          = 14;
    /** SET keyword */
    @SymbolTerminal public final int KEY_SET          = 15;
    /** ( symbol */
    @SymbolTerminal public final int SYMBOL_LPAREN    = 16;
    /** ) symbol */
    @SymbolTerminal public final int SYMBOL_RPAREN    = 17;
    /** = symbol */
    @SymbolTerminal public final int SYMBOL_EQUAL     = 18;
    /** , symbol */
    @SymbolTerminal public final int SYMBOL_COMMA     = 19;
    /** variable */
    @SymbolTerminal public final int VAR              = 20;
    /** empty symbol */
    @SymbolTerminal public final int EPSILON          = 21;
    /** end of input */
    @SymbolTerminal public final int END              = 22;

    /** count of terminal symbols */
    public final static int TERMINAL_COUNT = 23;

    /** max code of terminal symbols */
    public final static int TERMINAL_MAX = 22;

    /** symbols list */
    public final HashMap<Integer, ISymbol> symbols;

    /**
     * Creates basic PQL grammar symbols
     */
    public IGrammar() {
        symbols = new HashMap<>();
        symbols.put(KEY_KEYSPACE, new Token(TokenType.KEYWORD, "KEYSPACE"));
        symbols.put(KEY_KILL, new Token(TokenType.KEYWORD, "KILL"));
        symbols.put(KEY_USE, new Token(TokenType.KEYWORD, "USE"));
        symbols.put(KEY_CREATE, new Token(TokenType.KEYWORD, "CREATE"));
        symbols.put(
                KEY_COLUMNFAMILY, new Token(TokenType.KEYWORD, "COLUMNFAMILY"));
        symbols.put(KEY_DROP, new Token(TokenType.KEYWORD, "DROP"));
        symbols.put(KEY_INSERT, new Token(TokenType.KEYWORD, "INSERT"));
        symbols.put(KEY_INTO, new Token(TokenType.KEYWORD, "INTO"));
        symbols.put(KEY_VALUES, new Token(TokenType.KEYWORD, "VALUES"));
        symbols.put(KEY_UPDATE, new Token(TokenType.KEYWORD, "UPDATE"));
        symbols.put(KEY_SELECT, new Token(TokenType.KEYWORD, "SELECT"));
        symbols.put(KEY_FROM, new Token(TokenType.KEYWORD, "FROM"));
        symbols.put(KEY_DELETE, new Token(TokenType.KEYWORD, "DELETE"));
        symbols.put(KEY_WHERE, new Token(TokenType.KEYWORD, "WHERE"));
        symbols.put(KEY_KEY, new Token(TokenType.KEYWORD, "KEY"));
        symbols.put(KEY_SET, new Token(TokenType.KEYWORD, "SET"));
        symbols.put(SYMBOL_LPAREN, new Token(TokenType.LPAREN));
        symbols.put(SYMBOL_RPAREN, new Token(TokenType.RPAREN));
        symbols.put(SYMBOL_EQUAL, new Token(TokenType.EQUAL));
        symbols.put(SYMBOL_COMMA, new Token(TokenType.COMMA));
        symbols.put(VAR, new Token(TokenType.VARIABLE));
        symbols.put(EPSILON, new Token(TokenType.EPSILON));
    }

    /**
     * Gets list of grammar productions.
     *
     * @return List of grammar productions.
     */
    public abstract int[] getGrammar();

    /**
     * Gets list of symbols containing providen symbol.
     *
     * @param symbol Symbol code to find in non-terminal symbols
     * @return
     */
    public abstract Integer[] getSymbolsWith(int symbol);

    /**
     * Checks if symbol code is for terminal symbol.
     *
     * @param symbol Code of symbol
     * @return True if terminal, false otherwise
     * @throws PythiaException
     */
    public boolean isTerminal(int symbol) throws PythiaException {
        ISymbol s = symbols.get(symbol);
        if (s == null) {
            throw new PythiaException(PythiaError.SYMBOL_NOT_FOUND);
        }
        return s.isTerminal();
    }

    /**
     * Gets symbol for symbol code.
     *
     * @param symbol Symbol code
     * @return Symbol from symbol list
     * @throws PythiaException
     */
    public ISymbol getSymbol(int symbol) throws PythiaException {
        ISymbol s = symbols.get(symbol);
        if (s == null) {
            throw new PythiaException(PythiaError.SYMBOL_NOT_FOUND);
        }
        return s;
    }

    /**
     * Gets grammar code of providen symbol.
     *
     * @param symbol Symbol
     * @return Code of symbol or -1
     */
    public int getCode(ISymbol symbol) {
        Set<Entry<Integer, ISymbol>> set = symbols.entrySet();
        for (
            Iterator<Entry<Integer, ISymbol>> it = set.iterator();
            it.hasNext();
        ) {
            Entry<Integer, ISymbol> entry = it.next();
            ISymbol s = entry.getValue();
            if (s.grammarEquals(symbol)) {
                return entry.getKey();
            }
        }
        return -1;
    }
}