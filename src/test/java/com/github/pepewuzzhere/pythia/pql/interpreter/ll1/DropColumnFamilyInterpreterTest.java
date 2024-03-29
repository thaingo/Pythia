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
import com.github.pepewuzzhere.pythia.pql.command.DropColumnFamilyCommand;
import com.github.pepewuzzhere.pythia.pql.interpreter.IInterpreter;
import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 */
public class DropColumnFamilyInterpreterTest {

    public DropColumnFamilyInterpreterTest() {
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
        ParseTree stmt = new ParseTree(
                LL1Grammar.NonTerminal.STMT_DROP_COLUMNFAMILY, null);
        stmt.add(
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

        IDataModel model = new HashMapDataModel();
        IKeySpace keySpace = model.createKeySpace("Test");

        Context ctx = new Context();
        DB.INSTANCE.addKeySpace(keySpace);
        ctx.setActualKeySpace("Test");

        IInterpreter interpreter = new DropColumnFamilyInterpreter();
        DropColumnFamilyCommand cmd =
                (DropColumnFamilyCommand)interpreter.interpret(stmt, ctx);

        assertEquals(cmd.getName(), "Test2");
        assertEquals(cmd.getKeySpace(), "Test");
    }
}
