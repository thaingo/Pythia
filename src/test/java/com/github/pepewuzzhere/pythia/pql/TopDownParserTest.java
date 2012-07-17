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
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 */
public class TopDownParserTest {

    public TopDownParserTest() {
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
    public void testFirst() throws PythiaException {
        LL1Grammar grammar = new LL1Grammar();
        TopDownParser parser = new TopDownParser(grammar);

        Integer[] first = parser.first(new int[] {
            grammar.SYMBOL_LPAREN, grammar.KEY_KEY
        });
        Integer[] expected = new Integer[] {grammar.SYMBOL_LPAREN};
        assertArrayEquals(first, expected);

        first = parser.first(new int[] {
            grammar.STMT_START,
            grammar.SYMBOL_LPAREN,
            grammar.KEY_KEY
        });
        expected = new Integer[] {
            grammar.KEY_KEYSPACE, grammar.KEY_KILL, grammar.KEY_USE,
            grammar.KEY_CREATE, grammar.KEY_DROP, grammar.KEY_INSERT,
            grammar.KEY_UPDATE, grammar.KEY_SELECT, grammar.KEY_DELETE
        };
        assertArrayEquals(first, expected);
    }

    @Test
    public void testFollow() throws PythiaException {
        LL1Grammar grammar = new LL1Grammar();
        TopDownParser parser = new TopDownParser(grammar);
        int[] rules = grammar.getGrammar();

        Integer[] follow = parser.follow(grammar.KEY_VALUES_LIST);
        Integer[] expected = new Integer[] {
            grammar.SYMBOL_RPAREN,

        };
        assertArrayEquals(expected, follow);

    }

    @Test
    public void testBuildParseTable() throws PythiaException {
        LL1Grammar grammar = new LL1Grammar();
        TopDownParser parser = new TopDownParser(grammar);

        Production[][] table = parser.buildParseTable();

        assertEquals(
            table[grammar.STMT_START][grammar.KEY_INSERT],
            new Production(grammar.STMT_INSERT)
        );
        assertEquals(
            table[grammar.STMT_INSERT][grammar.KEY_INSERT],
            new Production(
                grammar.KEY_INSERT, grammar.KEY_INTO, grammar.VAR,
                grammar.SYMBOL_LPAREN, grammar.KEY_VALUES_LIST,
                grammar.SYMBOL_RPAREN
            )
        );
    }

    @Test
    public void testParse() throws PythiaException {
        LL1Grammar grammar = new LL1Grammar();

        Token[][] input = new Token[][] {
            new Token[] {
                 new Token(TokenType.KEYWORD, "KEYSPACE"),
                 new Token(TokenType.VARIABLE, "Test")
            },
            new Token[] {
                 new Token(TokenType.KEYWORD, "KILL"),
                 new Token(TokenType.VARIABLE, "Test")
            },
            new Token[] {
                 new Token(TokenType.KEYWORD, "USE"),
                 new Token(TokenType.VARIABLE, "Test")
            },
            new Token[] {
                 new Token(TokenType.KEYWORD, "CREATE"),
                 new Token(TokenType.KEYWORD, "COLUMNFAMILY"),
                 new Token(TokenType.VARIABLE, "Test")
            },
            new Token[] {
                 new Token(TokenType.KEYWORD, "DROP"),
                 new Token(TokenType.KEYWORD, "COLUMNFAMILY"),
                 new Token(TokenType.VARIABLE, "Test")
            },
            new Token[] {
                 new Token(TokenType.KEYWORD, "INSERT"),
                 new Token(TokenType.KEYWORD, "INTO"),
                 new Token(TokenType.VARIABLE, "Test"),
                 new Token(TokenType.LPAREN),
                 new Token(TokenType.KEYWORD, "KEY"),
                 new Token(TokenType.EQUAL),
                 new Token(TokenType.VARIABLE, "pepe"),
                 new Token(TokenType.COMMA),
                 new Token(TokenType.VARIABLE, "name"),
                 new Token(TokenType.EQUAL),
                 new Token(TokenType.VARIABLE, "Piotr"),
                 new Token(TokenType.RPAREN)
            },
            new Token[] {
                 new Token(TokenType.KEYWORD, "UPDATE"),
                 new Token(TokenType.VARIABLE, "Test"),
                 new Token(TokenType.KEYWORD, "SET"),
                 new Token(TokenType.KEYWORD, "KEY"),
                 new Token(TokenType.EQUAL),
                 new Token(TokenType.VARIABLE, "pepe"),
                 new Token(TokenType.COMMA),
                 new Token(TokenType.VARIABLE, "name"),
                 new Token(TokenType.EQUAL),
                 new Token(TokenType.VARIABLE, "Piotr")
            },
            new Token[] {
                 new Token(TokenType.KEYWORD, "SELECT"),
                 new Token(TokenType.KEYWORD, "FROM"),
                 new Token(TokenType.VARIABLE, "Test"),
                 new Token(TokenType.KEYWORD, "WHERE"),
                 new Token(TokenType.KEYWORD, "KEY"),
                 new Token(TokenType.EQUAL),
                 new Token(TokenType.VARIABLE, "pepe")
            },
            new Token[] {
                 new Token(TokenType.KEYWORD, "DELETE"),
                 new Token(TokenType.KEYWORD, "FROM"),
                 new Token(TokenType.VARIABLE, "Test"),
                 new Token(TokenType.KEYWORD, "WHERE"),
                 new Token(TokenType.KEYWORD, "KEY"),
                 new Token(TokenType.EQUAL),
                 new Token(TokenType.VARIABLE, "pepe")
            },
        };

        ParseTree[] expected = new ParseTree[9];

        expected[0] = new ParseTree(grammar.getSymbol(grammar.STMT_START));
        ParseTree stmt0 = new ParseTree(
                grammar.getSymbol(grammar.STMT_CREATE_KEYSPACE));
        expected[0].add(stmt0);
        stmt0.add(
            new Token(TokenType.KEYWORD, "KEYSPACE"),
            new Token(TokenType.VARIABLE, "Test")
        );

        expected[1] = new ParseTree(grammar.getSymbol(grammar.STMT_START));
        ParseTree stmt1 = new ParseTree(
                grammar.getSymbol(grammar.STMT_DROP_KEYSPACE));
        expected[1].add(stmt1);
        stmt1.add(
            new Token(TokenType.KEYWORD, "KILL"),
            new Token(TokenType.VARIABLE, "Test")
        );

        expected[2] = new ParseTree(grammar.getSymbol(grammar.STMT_START));
        ParseTree stmt2 = new ParseTree(
                grammar.getSymbol(grammar.STMT_USE_KEYSPACE));
        expected[2].add(stmt2);
        stmt2.add(
            new Token(TokenType.KEYWORD, "USE"),
            new Token(TokenType.VARIABLE, "Test")
        );

        expected[3] = new ParseTree(grammar.getSymbol(grammar.STMT_START));
        ParseTree stmt3 = new ParseTree(
                grammar.getSymbol(grammar.STMT_CREATE_COLUMNFAMILY));
        expected[3].add(stmt3);
        stmt3.add(
            new Token(TokenType.KEYWORD, "CREATE"),
            new Token(TokenType.KEYWORD, "COLUMNFAMILY"),
            new Token(TokenType.VARIABLE, "Test")
        );

        expected[4] = new ParseTree(grammar.getSymbol(grammar.STMT_START));
        ParseTree stmt4 = new ParseTree(
                grammar.getSymbol(grammar.STMT_DROP_COLUMNFAMILY));
        expected[4].add(stmt4);
        stmt4.add(
            new Token(TokenType.KEYWORD, "DROP"),
            new Token(TokenType.KEYWORD, "COLUMNFAMILY"),
            new Token(TokenType.VARIABLE, "Test")
        );

        expected[5] = new ParseTree(grammar.getSymbol(grammar.STMT_START));
        ParseTree stmt5 = new ParseTree(grammar.getSymbol(grammar.STMT_INSERT));
        expected[5].add(stmt5);
        ParseTree keyValue =
                new ParseTree(grammar.getSymbol(grammar.KEY_VALUES_LIST));
        ParseTree nextValue = new ParseTree(
                grammar.getSymbol(grammar.KEY_VALUES_LIST_PRIM));
        nextValue.add(
            new ParseTree(new Token(TokenType.COMMA)),
            new ParseTree(new Token(TokenType.VARIABLE, "name")),
            new ParseTree(new Token(TokenType.EQUAL)),
            new ParseTree(new Token(TokenType.VARIABLE, "Piotr")),
            new ParseTree(grammar.getSymbol(grammar.KEY_VALUES_LIST_PRIM))
        );
        keyValue.add(
            new ParseTree(new Token(TokenType.KEYWORD, "KEY")),
            new ParseTree(new Token(TokenType.EQUAL)),
            new ParseTree(new Token(TokenType.VARIABLE, "pepe")),
            nextValue
        );
        stmt5.add(
            new ParseTree(new Token(TokenType.KEYWORD, "INSERT")),
            new ParseTree(new Token(TokenType.KEYWORD, "INTO")),
            new ParseTree(new Token(TokenType.VARIABLE, "Test")),
            new ParseTree(new Token(TokenType.LPAREN)),
            keyValue,
            new ParseTree(new Token(TokenType.RPAREN))
        );

        expected[6] = new ParseTree(grammar.getSymbol(grammar.STMT_START));
        ParseTree stmt6 = new ParseTree(grammar.getSymbol(grammar.STMT_UPDATE));
        expected[6].add(stmt6);
        ParseTree keyValue2 =
                new ParseTree(grammar.getSymbol(grammar.KEY_VALUES_LIST));
        ParseTree nextValue2 = new ParseTree(
                grammar.getSymbol(grammar.KEY_VALUES_LIST_PRIM));
        nextValue2.add(
            new ParseTree(new Token(TokenType.COMMA)),
            new ParseTree(new Token(TokenType.VARIABLE, "name")),
            new ParseTree(new Token(TokenType.EQUAL)),
            new ParseTree(new Token(TokenType.VARIABLE, "Piotr")),
            new ParseTree(grammar.getSymbol(grammar.KEY_VALUES_LIST_PRIM))
        );
        keyValue2.add(
            new ParseTree(new Token(TokenType.KEYWORD, "KEY")),
            new ParseTree(new Token(TokenType.EQUAL)),
            new ParseTree(new Token(TokenType.VARIABLE, "pepe")),
            nextValue2
        );
        stmt6.add(
            new ParseTree(new Token(TokenType.KEYWORD, "UPDATE")),
            new ParseTree(new Token(TokenType.VARIABLE, "Test")),
            new ParseTree(new Token(TokenType.KEYWORD, "SET")),
            keyValue2
        );

        expected[7] = new ParseTree(grammar.getSymbol(grammar.STMT_START));
        ParseTree stmt7 = new ParseTree(grammar.getSymbol(grammar.STMT_SELECT));
        expected[7].add(stmt7);
        ParseTree where = new ParseTree(grammar.getSymbol(grammar.WHERE));
        where.add(
            new Token(TokenType.KEYWORD, "WHERE"),
            new Token(TokenType.KEYWORD, "KEY"),
            new Token(TokenType.EQUAL),
            new Token(TokenType.VARIABLE, "pepe")
        );
        stmt7.add(
            new ParseTree(new Token(TokenType.KEYWORD, "SELECT")),
            new ParseTree(new Token(TokenType.KEYWORD, "FROM")),
            new ParseTree(new Token(TokenType.VARIABLE, "Test")),
            where
        );

        expected[8] = new ParseTree(grammar.getSymbol(grammar.STMT_START));
        ParseTree stmt8 = new ParseTree(grammar.getSymbol(grammar.STMT_DELETE));
        expected[8].add(stmt8);
        ParseTree where2 = new ParseTree(grammar.getSymbol(grammar.WHERE));
        where2.add(
            new Token(TokenType.KEYWORD, "WHERE"),
            new Token(TokenType.KEYWORD, "KEY"),
            new Token(TokenType.EQUAL),
            new Token(TokenType.VARIABLE, "pepe")
        );
        stmt8.add(
            new ParseTree(new Token(TokenType.KEYWORD, "DELETE")),
            new ParseTree(new Token(TokenType.KEYWORD, "FROM")),
            new ParseTree(new Token(TokenType.VARIABLE, "Test")),
            where2
        );

        TopDownParser parser = new TopDownParser(grammar);
        for (int i = 0; i < input.length; ++i) {
            try {
                assertEquals(
                        "Token stream nr: " + i,
                        expected[i], parser.parse(input[i]));
            } catch (PythiaException e) {
                fail("Token stream nr: " + i + " " + e.getMessage());
            }
        }

        // check for syntax error
        boolean wasThrown = false;
        try {
            parser.parse(new Token[] {
                new Token(TokenType.KEYWORD, "FROM"),
                new Token(TokenType.VARIABLE, "Test"),
                new Token(TokenType.KEYWORD, "WHERE"),
            });
        } catch (PythiaException e) {
            wasThrown = true;
        }
        assertTrue(wasThrown);


    }
}
