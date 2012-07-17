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
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 */
public class ParseTreeTest {

    public ParseTreeTest() {
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
    public void testAddChild() {
        Token[] tokens = new Token[] {
            new Token(TokenType.LPAREN),
            new Token(TokenType.KEYWORD, "KEY"),
            new Token(TokenType.COMMA),
            new Token(TokenType.VARIABLE, "Test"),
            new Token(TokenType.RPAREN)
        };

        ParseTree root = new ParseTree(tokens[0]);
        assertEquals(tokens[0], root.getSymbol());

        root.add(tokens[1], tokens[2]);

        ArrayList<ParseTree> childrens = root.getChildrens();
        assertEquals(childrens.get(0).getSymbol(), tokens[1]);
        assertEquals(childrens.get(1).getSymbol(), tokens[2]);
    }

    @Test
    public void testEquals() {
        ParseTree p1 = new ParseTree(new Token(TokenType.LPAREN));
        p1.add(new Token(TokenType.RPAREN));

        ParseTree p2 = new ParseTree(new Token(TokenType.LPAREN));
        p2.add(new Token(TokenType.RPAREN));

        ParseTree p3 = new ParseTree(new Token(TokenType.LPAREN));

        assertTrue(p1.equals(p2));
        assertTrue(p1.equals(p1));
        assertTrue(p2.equals(p1));
        assertFalse(p3.equals(p2));
    }
}

