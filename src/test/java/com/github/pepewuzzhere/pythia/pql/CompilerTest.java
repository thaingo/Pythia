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

import com.github.pepewuzzhere.pythia.Context;
import com.github.pepewuzzhere.pythia.DB;
import com.github.pepewuzzhere.pythia.datamodel.hashmap.KeySpace;
import com.github.pepewuzzhere.pythia.pql.command.*;
import static org.junit.Assert.assertTrue;
import org.junit.*;

/**
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 */
public class CompilerTest {

    public CompilerTest() {
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
        DB.INSTANCE.dropDB();
    }

    @Test
    public void testCompile() throws Exception {
        Compiler compiler = new Compiler(new FSALexer(), new LL1Grammar());
        Context ctx = new Context();
        DB.INSTANCE.addKeySpace(new KeySpace("Test"));
        ctx.setActualKeySpace("Test");

        assertTrue(
            compiler.compile("KEYSPACE Test", ctx)
                instanceof CreateKeySpaceCommand
        );
        assertTrue(
            compiler.compile("KILL Test", ctx)
                instanceof DropKeySpaceCommand
        );
        assertTrue(
            compiler.compile("USE Test", ctx)
                instanceof UseKeySpaceCommand
        );
        assertTrue(
            compiler.compile("CREATE COLUMNFAMILY Test", ctx)
                instanceof CreateColumnFamilyCommand
        );
        assertTrue(
            compiler.compile("DROP COLUMNFAMILY Test", ctx)
                instanceof DropColumnFamilyCommand
        );
        assertTrue(
            compiler.compile("INSERT INTO Test(KEY=Pepe,name=Piotr)", ctx)
                instanceof InsertCommand
        );
        assertTrue(
            compiler.compile("UPDATE Test SET KEY=Pepe,name=Piotr", ctx)
                instanceof UpdateCommand
        );
        assertTrue(
            compiler.compile("DELETE FROM Test WHERE KEY=Pepe", ctx)
                instanceof DeleteCommand
        );
        assertTrue(
            compiler.compile("SELECT FROM Test WHERE KEY=Pepe", ctx)
                instanceof SelectCommand
        );
    }
}
