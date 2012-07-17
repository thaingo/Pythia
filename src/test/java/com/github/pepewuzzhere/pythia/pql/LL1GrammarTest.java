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

import com.github.pepewuzzhere.pythia.PythiaException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.*;

/**
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 */
public class LL1GrammarTest {

    public LL1GrammarTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetProductions() throws PythiaException {
        IGrammar grammar = new LL1Grammar();
        int[] productions = grammar.getGrammar();

        for (int i = 0; i < productions.length; ++i) {
            NonTerminal production =
                    (NonTerminal)grammar.getSymbol(productions[i]);
            assertFalse(production.isTerminal());
        }
    }

    @Test
    public void testGetSymbolsWith() throws PythiaException {
        LL1Grammar grammar = new LL1Grammar();
        Integer[] with = grammar.getSymbolsWith(grammar.WHERE);

        assertTrue(
            ifSameValuesArrays(
                with,
                new Integer[] {
                    grammar.STMT_DELETE,
                    grammar.STMT_SELECT

                }
            )
        );
    }

    private boolean ifSameValuesArrays(Object[] a1, Object[] a2) {
        if (a1.length == a2.length) {
            for (int i = 0; i < a1.length; ++i) {
                boolean contains = false;
                for (int j = 0; j < a2.length; ++j) {
                    if (a2[j].equals(a1[i])) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

}
