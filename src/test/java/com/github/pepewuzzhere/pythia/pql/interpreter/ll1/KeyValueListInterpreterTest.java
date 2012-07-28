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
package com.github.pepewuzzhere.pythia.pql.interpreter.ll1;

import com.github.pepewuzzhere.pythia.pql.*;
import com.github.pepewuzzhere.pythia.pql.interpreter.IInterpreter;
import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 */
public class KeyValueListInterpreterTest {

    public KeyValueListInterpreterTest() {
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
    public void testInterpret() throws Exception {
        ParseTree keyValue =
                new ParseTree(LL1Grammar.NonTerminal.KEY_VALUES_LIST, null);
        ParseTree nextValue = new ParseTree(
                LL1Grammar.NonTerminal.KEY_VALUES_LIST_PRIM, null);
        nextValue.add(
            new ParseTree(
                Terminal.SYMBOL_COMMA,
                new Token(TokenType.COMMA)
            ),
            new ParseTree(
                Terminal.VAR,
                new Token(TokenType.VARIABLE, "name")
            ),
            new ParseTree(
                Terminal.SYMBOL_EQUAL,
                new Token(TokenType.EQUAL)
            ),
            new ParseTree(
                Terminal.VAR,
                new Token(TokenType.VARIABLE, "Piotr")
            ),
            new ParseTree(LL1Grammar.NonTerminal.KEY_VALUES_LIST_PRIM, null)
        );
        keyValue.add(
            new ParseTree(
                Terminal.KEY_KEY,
                new Token(TokenType.KEYWORD, "KEY")
            ),
            new ParseTree(
                Terminal.SYMBOL_EQUAL,
                new Token(TokenType.EQUAL)
            ),
            new ParseTree(
                Terminal.VAR,
                new Token(TokenType.VARIABLE, "pepe")
            ),
            nextValue
        );

        IInterpreter interpreter = new KeyValueListInterpreter();
        String[] list = (String[])interpreter.interpret(keyValue, null);

        assertEquals(list.length, 4);
        assertEquals(list[0], "KEY");
        assertEquals(list[1], "pepe");
        assertEquals(list[2], "name");
        assertEquals(list[3], "Piotr");
    }
}
