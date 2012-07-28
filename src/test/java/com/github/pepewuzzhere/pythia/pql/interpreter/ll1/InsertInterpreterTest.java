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
import com.github.pepewuzzhere.pythia.datamodel.IDataModel;
import com.github.pepewuzzhere.pythia.datamodel.IKeySpace;
import com.github.pepewuzzhere.pythia.datamodel.hashmap.HashMapDataModel;
import com.github.pepewuzzhere.pythia.pql.*;
import com.github.pepewuzzhere.pythia.pql.command.InsertCommand;
import com.github.pepewuzzhere.pythia.pql.interpreter.IInterpreter;
import java.nio.ByteBuffer;
import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 */
public class InsertInterpreterTest {

    public InsertInterpreterTest() {
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
        ParseTree stmt =
                new ParseTree(LL1Grammar.NonTerminal.STMT_INSERT, null);
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
        stmt.add(
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

        IDataModel model = new HashMapDataModel();
        IKeySpace keySpace = model.createKeySpace("Test");

        Context ctx = new Context();
        DB.INSTANCE.addKeySpace(keySpace);
        ctx.setActualKeySpace("Test");

        IInterpreter interpreter = new InsertInterpreter();
        InsertCommand cmd = (InsertCommand)interpreter.interpret(stmt, ctx);

        assertEquals(cmd.getColumnFamily(), "Test2");
        assertEquals(cmd.getKeySpace(), "Test");
        assertEquals(cmd.getRowKey(), ByteBuffer.wrap("pepe".getBytes()));
        assertEquals(cmd.getKeys()[0], ByteBuffer.wrap("name".getBytes()));
        assertEquals(cmd.getValues()[0], ByteBuffer.wrap("Piotr".getBytes()));
    }
}
