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
        TopDownParser parser = new TopDownParser();

        Terminal[] first = parser.first(new ISymbol[] {
            Terminal.SYMBOL_LPAREN, Terminal.KEY_KEY
        });
        Terminal[] expected = new Terminal[] {Terminal.SYMBOL_LPAREN};
        assertArrayEquals(first, expected);

        first = parser.first(new ISymbol[] {
            LL1Grammar.NonTerminal.STMT_START,
            Terminal.SYMBOL_LPAREN,
            Terminal.KEY_KEY
        });
        expected = new Terminal[] {
            Terminal.KEY_KEYSPACE, Terminal.KEY_KILL, Terminal.KEY_USE,
            Terminal.KEY_CREATE, Terminal.KEY_DROP, Terminal.KEY_INSERT,
            Terminal.KEY_UPDATE, Terminal.KEY_SELECT, Terminal.KEY_DELETE
        };
        assertArrayEquals(first, expected);
    }

    @Test
    public void testFollow() throws PythiaException {

        Terminal[] follow =
                TopDownParser.follow(LL1Grammar.NonTerminal.KEY_VALUES_LIST);
        Terminal[] expected = new Terminal[] {Terminal.SYMBOL_RPAREN};
        assertArrayEquals(expected, follow);
    }

    @Test
    public void testBuildParseTable() throws PythiaException {
        TopDownParser parser = new TopDownParser();

        assertEquals(
            parser.PARSE_TABLE
                  .get(LL1Grammar.NonTerminal.STMT_START)
                  .get(Terminal.KEY_INSERT),
            new Production(LL1Grammar.NonTerminal.STMT_INSERT)
        );
        assertEquals(
             parser.PARSE_TABLE
                   .get(LL1Grammar.NonTerminal.STMT_INSERT)
                   .get(Terminal.KEY_INSERT),
            new Production(
                Terminal.KEY_INSERT, Terminal.KEY_INTO, Terminal.VAR,
                Terminal.SYMBOL_LPAREN, LL1Grammar.NonTerminal.KEY_VALUES_LIST,
                Terminal.SYMBOL_RPAREN
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

        expected[0] = new ParseTree(LL1Grammar.NonTerminal.STMT_START, null);
        ParseTree stmt0 = new ParseTree(
            LL1Grammar.NonTerminal.STMT_CREATE_KEYSPACE,
            null
        );
        expected[0].add(stmt0);
        stmt0.add(
            new ParseTree(
                Terminal.KEY_KEYSPACE,
                new Token(TokenType.KEYWORD, "KEYSPACE")
            ),
            new ParseTree(
                Terminal.VAR,
                new Token(TokenType.VARIABLE, "Test")
            )
        );

        expected[1] = new ParseTree(LL1Grammar.NonTerminal.STMT_START, null);
        ParseTree stmt1 = new ParseTree(
            LL1Grammar.NonTerminal.STMT_DROP_KEYSPACE, null
        );
        expected[1].add(stmt1);
        stmt1.add(
            new ParseTree(
                Terminal.KEY_KILL,
                new Token(TokenType.KEYWORD, "KILL")
            ),
            new ParseTree(
                Terminal.VAR,
                new Token(TokenType.VARIABLE, "Test")
            )
        );

        expected[2] = new ParseTree(LL1Grammar.NonTerminal.STMT_START, null);
        ParseTree stmt2 = new ParseTree(
            LL1Grammar.NonTerminal.STMT_USE_KEYSPACE, null
        );
        expected[2].add(stmt2);
        stmt2.add(
            new ParseTree(
                Terminal.KEY_USE,
                new Token(TokenType.KEYWORD, "USE")
            ),
            new ParseTree(
                Terminal.VAR,
                new Token(TokenType.VARIABLE, "Test")
            )
        );

        expected[3] = new ParseTree(LL1Grammar.NonTerminal.STMT_START, null);
        ParseTree stmt3 = new ParseTree(
            LL1Grammar.NonTerminal.STMT_CREATE_COLUMNFAMILY, null
        );
        expected[3].add(stmt3);
        stmt3.add(
            new ParseTree(
                Terminal.KEY_CREATE,
                new Token(TokenType.KEYWORD, "CREATE")
            ),
            new ParseTree(
                Terminal.KEY_COLUMNFAMILY,
                new Token(TokenType.KEYWORD, "COLUMNFAMILY")
            ),
            new ParseTree(
                Terminal.VAR,
                new Token(TokenType.VARIABLE, "Test")
            )
        );

        expected[4] = new ParseTree(LL1Grammar.NonTerminal.STMT_START, null);
        ParseTree stmt4 = new ParseTree(
            LL1Grammar.NonTerminal.STMT_DROP_COLUMNFAMILY, null
        );
        expected[4].add(stmt4);
        stmt4.add(
            new ParseTree(
                Terminal.KEY_DROP,
                new Token(TokenType.KEYWORD, "DROP")
            ),
            new ParseTree(
                Terminal.KEY_COLUMNFAMILY,
                new Token(TokenType.KEYWORD, "COLUMNFAMILY")
            ),
            new ParseTree(
                Terminal.VAR,
                new Token(TokenType.VARIABLE, "Test")
            )
        );

        expected[5] = new ParseTree(LL1Grammar.NonTerminal.STMT_START, null);
        ParseTree stmt5 = new ParseTree(
                LL1Grammar.NonTerminal.STMT_INSERT, null);
        expected[5].add(stmt5);
        ParseTree keyValue =
                new ParseTree(LL1Grammar.NonTerminal.KEY_VALUES_LIST, null);
        ParseTree nextValue =
                new ParseTree(LL1Grammar.NonTerminal.KEY_VALUES_LIST_PRIM, null);
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
        stmt5.add(
            new ParseTree(
                Terminal.KEY_INSERT,
                new Token(TokenType.KEYWORD, "INSERT")
            ),
            new ParseTree(
                Terminal.KEY_INTO,
                new Token(TokenType.KEYWORD, "INTO")
            ),
            new ParseTree(
                Terminal.VAR,
                new Token(TokenType.VARIABLE, "Test")
            ),
            new ParseTree(
                Terminal.SYMBOL_LPAREN,
                new Token(TokenType.LPAREN)
            ),
            keyValue,
            new ParseTree(
                Terminal.SYMBOL_RPAREN,
                new Token(TokenType.RPAREN)
            )
        );

        expected[6] = new ParseTree(LL1Grammar.NonTerminal.STMT_START, null);
        ParseTree stmt6 =
                new ParseTree(LL1Grammar.NonTerminal.STMT_UPDATE, null);
        expected[6].add(stmt6);
        ParseTree keyValue2 =
                new ParseTree(LL1Grammar.NonTerminal.KEY_VALUES_LIST, null);
        ParseTree nextValue2 = new ParseTree(
                LL1Grammar.NonTerminal.KEY_VALUES_LIST_PRIM, null);
        nextValue2.add(
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
            new ParseTree(
                LL1Grammar.NonTerminal.KEY_VALUES_LIST_PRIM, null
            )
        );
        keyValue2.add(
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
            nextValue2
        );
        stmt6.add(
            new ParseTree(
                Terminal.KEY_UPDATE,
                new Token(TokenType.KEYWORD, "UPDATE")
            ),
            new ParseTree(
                Terminal.VAR,
                new Token(TokenType.VARIABLE, "Test")
            ),
            new ParseTree(
                Terminal.KEY_SET,
                new Token(TokenType.KEYWORD, "SET")
            ),
            keyValue2
        );

        expected[7] = new ParseTree(LL1Grammar.NonTerminal.STMT_START, null);
        ParseTree stmt7 =
                new ParseTree(LL1Grammar.NonTerminal.STMT_SELECT, null);
        expected[7].add(stmt7);
        ParseTree where = new ParseTree(LL1Grammar.NonTerminal.WHERE, null);
        where.add(
            new ParseTree(
                Terminal.KEY_WHERE,
                new Token(TokenType.KEYWORD, "WHERE")
            ),
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
            )
        );
        stmt7.add(
            new ParseTree(
                Terminal.KEY_SELECT,
                new Token(TokenType.KEYWORD, "SELECT")
            ),
            new ParseTree(
                Terminal.KEY_FROM,
                new Token(TokenType.KEYWORD, "FROM")
            ),
            new ParseTree(
                Terminal.VAR,
                new Token(TokenType.VARIABLE, "Test")
            ),
            where
        );

        expected[8] = new ParseTree(LL1Grammar.NonTerminal.STMT_START, null);
        ParseTree stmt8 =
                new ParseTree(LL1Grammar.NonTerminal.STMT_DELETE, null);
        expected[8].add(stmt8);
        ParseTree where2 = new ParseTree(LL1Grammar.NonTerminal.WHERE, null);
        where2.add(
            new ParseTree(
                Terminal.KEY_WHERE,
                new Token(TokenType.KEYWORD, "WHERE")
            ),
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
            )
        );
        stmt8.add(
            new ParseTree(
                Terminal.KEY_DELETE,
                new Token(TokenType.KEYWORD, "DELETE")
            ),
            new ParseTree(
                Terminal.KEY_FROM,
                new Token(TokenType.KEYWORD, "FROM")
            ),
            new ParseTree(
                Terminal.VAR,
                new Token(TokenType.VARIABLE, "Test")
            ),
            where2
        );

        TopDownParser parser = new TopDownParser();
        for (int i = 0; i < input.length; ++i) {
            try {
                ParseTree parse = parser.parse(input[i]);
                assertEquals(
                    "Token stream nr: " + i,
                    expected[i], parse
                );
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
