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

import com.github.pepewuzzhere.pythia.Context;
import com.github.pepewuzzhere.pythia.DB;
import com.github.pepewuzzhere.pythia.PythiaException;
import com.github.pepewuzzhere.pythia.datamodel.IDataModel;
import com.github.pepewuzzhere.pythia.datamodel.IKeySpace;
import com.github.pepewuzzhere.pythia.datamodel.hashmap.HashMapDataModel;
import com.github.pepewuzzhere.pythia.pql.*;
import com.github.pepewuzzhere.pythia.pql.command.*;
import static org.junit.Assert.assertTrue;
import org.junit.*;

/**
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 */
public class StartStmtInterpreterTest {

    public StartStmtInterpreterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        DB.INSTANCE.dropDB();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInterpret() throws Exception {
        ParseTree[] input = new ParseTree[10];

        input[0] = new ParseTree(LL1Grammar.NonTerminal.STMT_START, null);
        ParseTree stmt0 = new ParseTree(
                LL1Grammar.NonTerminal.STMT_CREATE_KEYSPACE, null);
        input[0].add(stmt0);
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

        input[1] = new ParseTree(LL1Grammar.NonTerminal.STMT_START, null);
        ParseTree stmt1 = new ParseTree(
                LL1Grammar.NonTerminal.STMT_DROP_KEYSPACE, null);
        input[1].add(stmt1);
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

        input[2] = new ParseTree(LL1Grammar.NonTerminal.STMT_START, null);
        ParseTree stmt2 = new ParseTree(
                LL1Grammar.NonTerminal.STMT_USE_KEYSPACE, null);
        input[2].add(stmt2);
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

        input[3] = new ParseTree(LL1Grammar.NonTerminal.STMT_START, null);
        ParseTree stmt3 = new ParseTree(
                LL1Grammar.NonTerminal.STMT_CREATE_COLUMNFAMILY, null);
        input[3].add(stmt3);
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
                new Token(TokenType.VARIABLE, "Test2")
            )
        );

        input[4] = new ParseTree(LL1Grammar.NonTerminal.STMT_START, null);
        ParseTree stmt4 = new ParseTree(
                LL1Grammar.NonTerminal.STMT_DROP_COLUMNFAMILY, null);
        input[4].add(stmt4);
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
                new Token(TokenType.VARIABLE, "Test2")
            )
        );

        input[5] = new ParseTree(LL1Grammar.NonTerminal.STMT_START, null);
        ParseTree stmt5 =
                new ParseTree(LL1Grammar.NonTerminal.STMT_INSERT, null);
        input[5].add(stmt5);
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
                new Token(TokenType.VARIABLE, "Test2")
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

        input[6] = new ParseTree(LL1Grammar.NonTerminal.STMT_START, null);
        ParseTree stmt6 =
                new ParseTree(LL1Grammar.NonTerminal.STMT_UPDATE, null);
        input[6].add(stmt6);
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
            new ParseTree(LL1Grammar.NonTerminal.KEY_VALUES_LIST_PRIM, null)
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
                new Token(TokenType.VARIABLE, "Test2")
            ),
            new ParseTree(
                Terminal.KEY_SET,
                new Token(TokenType.KEYWORD, "SET")
            ),
            keyValue2
        );

        input[7] = new ParseTree(LL1Grammar.NonTerminal.STMT_START, null);
        ParseTree stmt7 =
                new ParseTree(LL1Grammar.NonTerminal.STMT_SELECT, null);
        input[7].add(stmt7);
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
                new Token(TokenType.VARIABLE, "Test2")
            ),
            where
        );

        input[8] = new ParseTree(LL1Grammar.NonTerminal.STMT_START, null);
        ParseTree stmt8 =
                new ParseTree(LL1Grammar.NonTerminal.STMT_DELETE, null);
        input[8].add(stmt8);
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
                new Token(TokenType.VARIABLE, "Test2")
            ),
            where2
        );
        input[9] = new ParseTree(LL1Grammar.NonTerminal.STMT_START, null);
        ParseTree stmt9 = new ParseTree(LL1Grammar.NonTerminal.WHERE, null);
        input[9].add(stmt9);

        IDataModel model = new HashMapDataModel();
        IKeySpace keySpace = model.createKeySpace("Test2");

        Context ctx = new Context();
        DB.INSTANCE.addKeySpace(keySpace);
        ctx.setActualKeySpace("Test2");

        StartStmtInterpreter interpreter = new StartStmtInterpreter();

        assertTrue(
            interpreter.interpret(input[0], ctx)
                instanceof CreateKeySpaceCommand
        );
        assertTrue(
            interpreter.interpret(input[1], ctx)
                instanceof DropKeySpaceCommand
        );
        assertTrue(
            interpreter.interpret(input[2], ctx)
                instanceof UseKeySpaceCommand
        );
        assertTrue(
            interpreter.interpret(input[3], ctx)
                instanceof CreateColumnFamilyCommand
        );
        assertTrue(
            interpreter.interpret(input[4], ctx)
                instanceof DropColumnFamilyCommand
        );
        assertTrue(
            interpreter.interpret(input[5], ctx)
                instanceof InsertCommand
        );
        assertTrue(
            interpreter.interpret(input[6], ctx)
                instanceof UpdateCommand
        );
        assertTrue(
            interpreter.interpret(input[7], ctx)
                instanceof SelectCommand
        );
        assertTrue(
            interpreter.interpret(input[8], ctx)
                instanceof DeleteCommand
        );

        // check for syntax error
        boolean wasThrown = false;
        try {
            interpreter.interpret(input[9], ctx);
        } catch (PythiaException e) {
            wasThrown = true;
        }
        assertTrue(wasThrown);

    }
}
