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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.*;

/**
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 */
public class NonTerminalTest {

    public NonTerminalTest() {
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
    public void testEquals() {
        IGrammar grammar = new LL1Grammar();
        NonTerminal t1 = new NonTerminal(
            new Production(grammar.KEY_COLUMNFAMILY, grammar.KEY_DROP),
            new Production(grammar.KEY_COLUMNFAMILY, grammar.KEY_KILL)
        );
        NonTerminal t2 = new NonTerminal(
            new Production(grammar.KEY_COLUMNFAMILY, grammar.KEY_DROP),
            new Production(grammar.KEY_COLUMNFAMILY, grammar.KEY_KILL)
        );
        NonTerminal t3 = new NonTerminal(
            new Production(grammar.KEY_COLUMNFAMILY, grammar.KEY_KILL)
        );

        assertTrue(t1.equals(t2));
        assertTrue(t1.equals(t1));
        assertTrue(t2.equals(t1));
        assertFalse(t3.equals(t2));
    }

}
