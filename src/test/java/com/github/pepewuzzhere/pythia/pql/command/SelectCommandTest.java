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
package com.github.pepewuzzhere.pythia.pql.command;

import com.github.pepewuzzhere.pythia.DB;
import com.github.pepewuzzhere.pythia.PythiaException;
import com.github.pepewuzzhere.pythia.datamodel.IColumnFamily;
import com.github.pepewuzzhere.pythia.datamodel.IRow;
import com.github.pepewuzzhere.pythia.datamodel.hashmap.ColumnFamily;
import com.github.pepewuzzhere.pythia.datamodel.hashmap.HashMapDataModel;
import com.github.pepewuzzhere.pythia.datamodel.hashmap.KeySpace;
import com.github.pepewuzzhere.pythia.datamodel.hashmap.Row;
import java.nio.ByteBuffer;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 */
public class SelectCommandTest {

    public SelectCommandTest() {
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
    public void testExecuteIfKeySpaceNotExists() {
        ByteBuffer rowKey   = ByteBuffer.wrap("Piotr".getBytes());

        IDBCommand command = new SelectCommand("Users", "Test", rowKey);

        boolean wasThrown = false;
        try {
            command.execute(new HashMapDataModel());
        } catch(PythiaException e) {
            wasThrown = true;
        }

        assertTrue(wasThrown);
    }

    @Test
    public void testExecuteIfColumnFamilyNotExists() {
        ByteBuffer rowKey = ByteBuffer.wrap("Piotr".getBytes());

        IDBCommand command = new SelectCommand("Users", "Test", rowKey);

        boolean wasThrown = false;
        try {
            DB.INSTANCE.addKeySpace(new KeySpace("Test"));
            command.execute(new HashMapDataModel());
        } catch(PythiaException e) {
            wasThrown = true;
        }

        assertTrue(wasThrown);
    }

    @Test
    public void testExecuteIfRowNotExists() {
        ByteBuffer rowKey = ByteBuffer.wrap("Piotr".getBytes());

        IDBCommand command = new SelectCommand("Users", "Test", rowKey);

        IRow rows = null;
        try {
            DB.INSTANCE.addKeySpace(new KeySpace("Test"));
            DB.INSTANCE.getKeySpace("Test")
                       .addColumnFamily("Users", new ColumnFamily());
            rows = (IRow) command.execute(new HashMapDataModel());
        } catch(PythiaException e) {
            fail(e.getMessage());
        }

        assertNull(rows);
    }

    @Test
    public void testExecuteIfRowExists() {
        ByteBuffer rowKey = ByteBuffer.wrap("Piotr".getBytes());

        IDBCommand command = new SelectCommand("Users", "Test", rowKey);

        IRow rows = null;
        IColumnFamily columnFamily = new ColumnFamily();

        try {
            columnFamily.addRow(new Row(rowKey));
            DB.INSTANCE.addKeySpace(new KeySpace("Test"));
            DB.INSTANCE.getKeySpace("Test")
                       .addColumnFamily("Users", columnFamily);
            rows = (IRow)command.execute(new HashMapDataModel());
        } catch(PythiaException e) {
            fail(e.getMessage());
        }

        assertEquals(rows.getKey(), rowKey);
    }

}