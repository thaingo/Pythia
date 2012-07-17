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
import com.github.pepewuzzhere.pythia.datamodel.IKeySpace;
import com.github.pepewuzzhere.pythia.datamodel.hashmap.KeySpace;
import com.github.pepewuzzhere.pythia.pql.LL1Grammar;
import com.github.pepewuzzhere.pythia.pql.ParseTree;
import com.github.pepewuzzhere.pythia.pql.Token;
import com.github.pepewuzzhere.pythia.pql.TokenType;
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
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInterpret() throws Exception {
        LL1Grammar grammar = new LL1Grammar();

        ParseTree[] input = new ParseTree[10];

        input[0] = new ParseTree(grammar.getSymbol(grammar.STMT_START));
        ParseTree stmt0 = new ParseTree(
                grammar.getSymbol(grammar.STMT_CREATE_KEYSPACE));
        input[0].add(stmt0);
        stmt0.add(
            new Token(TokenType.KEYWORD, "KEYSPACE"),
            new Token(TokenType.VARIABLE, "Test")
        );

        input[1] = new ParseTree(grammar.getSymbol(grammar.STMT_START));
        ParseTree stmt1 = new ParseTree(
                grammar.getSymbol(grammar.STMT_DROP_KEYSPACE));
        input[1].add(stmt1);
        stmt1.add(
            new Token(TokenType.KEYWORD, "KILL"),
            new Token(TokenType.VARIABLE, "Test")
        );

        input[2] = new ParseTree(grammar.getSymbol(grammar.STMT_START));
        ParseTree stmt2 = new ParseTree(
                grammar.getSymbol(grammar.STMT_USE_KEYSPACE));
        input[2].add(stmt2);
        stmt2.add(
            new Token(TokenType.KEYWORD, "USE"),
            new Token(TokenType.VARIABLE, "Test")
        );

        input[3] = new ParseTree(grammar.getSymbol(grammar.STMT_START));
        ParseTree stmt3 = new ParseTree(
                grammar.getSymbol(grammar.STMT_CREATE_COLUMNFAMILY));
        input[3].add(stmt3);
        stmt3.add(
            new Token(TokenType.KEYWORD, "CREATE"),
            new Token(TokenType.KEYWORD, "COLUMNFAMILY"),
            new Token(TokenType.VARIABLE, "Test")
        );

        input[4] = new ParseTree(grammar.getSymbol(grammar.STMT_START));
        ParseTree stmt4 = new ParseTree(
                grammar.getSymbol(grammar.STMT_DROP_COLUMNFAMILY));
        input[4].add(stmt4);
        stmt4.add(
            new Token(TokenType.KEYWORD, "DROP"),
            new Token(TokenType.KEYWORD, "COLUMNFAMILY"),
            new Token(TokenType.VARIABLE, "Test")
        );

        input[5] = new ParseTree(grammar.getSymbol(grammar.STMT_START));
        ParseTree stmt5 = new ParseTree(grammar.getSymbol(grammar.STMT_INSERT));
        input[5].add(stmt5);
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

        input[6] = new ParseTree(grammar.getSymbol(grammar.STMT_START));
        ParseTree stmt6 = new ParseTree(grammar.getSymbol(grammar.STMT_UPDATE));
        input[6].add(stmt6);
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

        input[7] = new ParseTree(grammar.getSymbol(grammar.STMT_START));
        ParseTree stmt7 = new ParseTree(grammar.getSymbol(grammar.STMT_SELECT));
        input[7].add(stmt7);
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

        input[8] = new ParseTree(grammar.getSymbol(grammar.STMT_START));
        ParseTree stmt8 = new ParseTree(grammar.getSymbol(grammar.STMT_DELETE));
        input[8].add(stmt8);
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
        input[9] = new ParseTree(grammar.getSymbol(grammar.STMT_START));
        ParseTree stmt9 = new ParseTree(grammar.getSymbol(grammar.WHERE));
        input[9].add(stmt9);

        IKeySpace keySpace = new KeySpace("Test2");

        Context ctx = new Context();
        DB.INSTANCE.addKeySpace(keySpace);
        ctx.setActualKeySpace("Test2");

        StartStmtInterpreter interpreter = new StartStmtInterpreter();

        assertTrue(
            interpreter.interpret(grammar, input[0], ctx)
                instanceof CreateKeySpaceCommand
        );
        assertTrue(
            interpreter.interpret(grammar, input[1], ctx)
                instanceof DropKeySpaceCommand
        );
        assertTrue(
            interpreter.interpret(grammar, input[2], ctx)
                instanceof UseKeySpaceCommand
        );
        assertTrue(
            interpreter.interpret(grammar, input[3], ctx)
                instanceof CreateColumnFamilyCommand
        );
        assertTrue(
            interpreter.interpret(grammar, input[4], ctx)
                instanceof DropColumnFamilyCommand
        );
        assertTrue(
            interpreter.interpret(grammar, input[5], ctx)
                instanceof InsertCommand
        );
        assertTrue(
            interpreter.interpret(grammar, input[6], ctx)
                instanceof UpdateCommand
        );
        assertTrue(
            interpreter.interpret(grammar, input[7], ctx)
                instanceof SelectCommand
        );
        assertTrue(
            interpreter.interpret(grammar, input[8], ctx)
                instanceof DeleteCommand
        );

        // check for syntax error
        boolean wasThrown = false;
        try {
            interpreter.interpret(grammar, input[9], ctx);
        } catch (PythiaException e) {
            wasThrown = true;
        }
        assertTrue(wasThrown);

    }
}
