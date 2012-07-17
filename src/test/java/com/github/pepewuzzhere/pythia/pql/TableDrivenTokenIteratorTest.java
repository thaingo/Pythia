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
import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 */
public class TableDrivenTokenIteratorTest {

    public TableDrivenTokenIteratorTest() {
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
    public void testNextIfSourceIsNull() throws PythiaException {
        ITokenIterator it = new TableDrivenTokenIterator();
        Token token       = it.next();

        assertNull(token);
    }

    @Test
    public void testNextIfSourceHasUnknownCharacter() {
        ITokenIterator it = new TableDrivenTokenIterator();
        String src        = "CREATE $";

        it.setSource(src);

        boolean wasThrown = false;
        try {
            while (it.hasNext()) {
                it.next();
            }
        } catch (PythiaException e) {
            wasThrown = true;
        }
        assertTrue(wasThrown);
    }

    @Test
    public void testNextIfSourceIsWellFormed() {
        ITokenIterator it = new TableDrivenTokenIterator();
        String src = "CREATE KEYSPACE Test";

        it.setSource(src);

        ArrayList<Token> tokens = new ArrayList<>();
        try {
            while (it.hasNext()) {
                tokens.add(it.next());
            }
        } catch (PythiaException e) {
            fail(e.getMessage());
        }

        Token[] expecteds = new Token[] {
            new Token(TokenType.KEYWORD, "CREATE"),
            new Token(TokenType.KEYWORD, "KEYSPACE"),
            new Token(TokenType.VARIABLE, "Test")
        };

        assertArrayEquals(expecteds, tokens.toArray(new Token[1]));
    }

    @Test
    public void testNextIfSourceIsWellFormedWithLookup() {
        ITokenIterator it = new TableDrivenTokenIterator();
        String src = "INSERT INTO Test (KEY, name, surname) "
                   + "VALUES(\"1\",\"2\",\"3\")";

        it.setSource(src);

        ArrayList<Token> tokens = new ArrayList<>();
        try {
            while (it.hasNext()) {
                tokens.add(it.next());
            }
        } catch (PythiaException e) {
            fail(e.getMessage());
        }

        Token[] expecteds = new Token[] {
            new Token(TokenType.KEYWORD, "INSERT"),
            new Token(TokenType.KEYWORD, "INTO"),
            new Token(TokenType.VARIABLE, "Test"),
            new Token(TokenType.LPAREN),
            new Token(TokenType.KEYWORD, "KEY"),
            new Token(TokenType.COMMA),
            new Token(TokenType.VARIABLE, "name"),
            new Token(TokenType.COMMA),
            new Token(TokenType.VARIABLE, "surname"),
            new Token(TokenType.RPAREN),
            new Token(TokenType.KEYWORD, "VALUES"),
            new Token(TokenType.LPAREN),
            new Token(TokenType.VARIABLE, "1"),
            new Token(TokenType.COMMA),
            new Token(TokenType.VARIABLE, "2"),
            new Token(TokenType.COMMA),
            new Token(TokenType.VARIABLE, "3"),
            new Token(TokenType.RPAREN),
        };

        assertArrayEquals(expecteds, tokens.toArray(new Token[1]));
    }

    @Test
    public void testNextInSourceWithQuotes() {
        ITokenIterator it = new TableDrivenTokenIterator();
        String src        = "SELECT FROM Test WHERE KEY=\"Siema koledzy\"";

        it.setSource(src);

        ArrayList<Token> tokens = new ArrayList<>();
        try {
            while (it.hasNext()) {
                tokens.add(it.next());
            }
        } catch (PythiaException e) {
            fail(e.getMessage());
        }

        Token[] expecteds = new Token[] {
            new Token(TokenType.KEYWORD, "SELECT"),
            new Token(TokenType.KEYWORD, "FROM"),
            new Token(TokenType.VARIABLE, "Test"),
            new Token(TokenType.KEYWORD, "WHERE"),
            new Token(TokenType.KEYWORD, "KEY"),
            new Token(TokenType.EQUAL),
            new Token(TokenType.VARIABLE, "Siema koledzy")
        };

        assertArrayEquals(expecteds, tokens.toArray(new Token[1]));
    }

}
