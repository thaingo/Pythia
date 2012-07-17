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

import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 */
public class ProductionTest {

    public ProductionTest() {
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
    public void testHasSymbol() {
        IGrammar grammar = new LL1Grammar();
        Production p1 = new Production(
                grammar.KEY_COLUMNFAMILY, grammar.KEY_USE);

        assertTrue(p1.hasSymbol(grammar.KEY_USE));
        assertFalse(p1.hasSymbol(grammar.KEY_CREATE));
    }

    @Test
    public void testNextSymbol() {
        IGrammar grammar = new LL1Grammar();
        Production p1 = new Production(
                grammar.KEY_COLUMNFAMILY, grammar.KEY_USE);

        assertEquals(-1, p1.nextSymbol(grammar.KEY_USE));
        assertEquals(
                grammar.KEY_USE, p1.nextSymbol(grammar.KEY_COLUMNFAMILY));
    }

    @Test
    public void testEquals() {
        IGrammar grammar = new LL1Grammar();
        Production p1 = new Production(
                grammar.KEY_COLUMNFAMILY, grammar.KEY_USE);
        Production p2 = new Production(
                grammar.KEY_COLUMNFAMILY, grammar.KEY_USE);
        Production p3 = new Production(grammar.KEY_COLUMNFAMILY);

        assertTrue(p1.equals(p2));
        assertTrue(p1.equals(p1));
        assertTrue(p2.equals(p1));
        assertFalse(p3.equals(p2));
    }

}
