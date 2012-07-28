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
import com.github.pepewuzzhere.pythia.datamodel.IColumn;
import com.github.pepewuzzhere.pythia.datamodel.IColumnFamily;
import com.github.pepewuzzhere.pythia.datamodel.IDataModel;
import com.github.pepewuzzhere.pythia.datamodel.hashmap.*;
import java.nio.ByteBuffer;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 */
public class InsertCommandTest {

    public InsertCommandTest() {
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
        ByteBuffer[] keys   = new ByteBuffer[] {};
        ByteBuffer[] values = new ByteBuffer[] {};
        ByteBuffer rowKey   = ByteBuffer.wrap("Piotr".getBytes());

        IDBCommand command = new InsertCommand(
            "Users", "Test", rowKey, keys, values
        );

        boolean wasThrown = false;
        try {
            command.execute(new HashMapDataModel());
        } catch(PythiaException e) {
            wasThrown = true;
        }

        assertTrue(wasThrown);
    }

    @Test
    public void testInvalidArgumentList() {
        IDataModel model = new HashMapDataModel();
        ByteBuffer[] keys = new ByteBuffer[] {
            ByteBuffer.wrap("Name".getBytes())
        };
        ByteBuffer[] values = new ByteBuffer[] {};
        ByteBuffer rowKey = ByteBuffer.wrap("Piotr".getBytes());

        IDBCommand command = new InsertCommand(
            "Users", "Test", rowKey, keys, values
        );

        boolean wasThrown = false;
        try {
            DB.INSTANCE.addKeySpace(model.createKeySpace("Test"));
            command.execute(model);
        } catch(PythiaException e) {
            wasThrown = true;
        }

        assertTrue(wasThrown);
    }

    @Test
    public void testExecuteIfColumnFamilyNotExists() {
        IDataModel model = new HashMapDataModel();
        ByteBuffer[] keys = new ByteBuffer[] {
            ByteBuffer.wrap("Name".getBytes())
        };
        ByteBuffer[] values = new ByteBuffer[] {
            ByteBuffer.wrap("Piotr".getBytes())
        };
        ByteBuffer rowKey = ByteBuffer.wrap("Piotr".getBytes());

        IDBCommand command = new InsertCommand(
            "Users", "Test", rowKey, keys, values
        );

        boolean wasThrown = false;
        try {
            DB.INSTANCE.addKeySpace(model.createKeySpace("Test"));
            command.execute(model);
        } catch(PythiaException e) {
            wasThrown = true;
        }

        assertTrue(wasThrown);
    }

    @Test
    public void testExecuteIfRowNotExists() {
        IDataModel model = new HashMapDataModel();
        ByteBuffer[] keys = new ByteBuffer[] {
            ByteBuffer.wrap("Name".getBytes())
        };
        ByteBuffer[] values = new ByteBuffer[] {
            ByteBuffer.wrap("Piotr".getBytes())
        };
        ByteBuffer rowKey = ByteBuffer.wrap("Piotr".getBytes());

        IDBCommand command = new InsertCommand(
            "Users", "Test", rowKey, keys, values
        );

        try {
            DB.INSTANCE.addKeySpace(model.createKeySpace("Test"));
            DB.INSTANCE.getKeySpace("Test")
                       .addColumnFamily("Users", model.createColumnFamily());
            command.execute(model);
        } catch(PythiaException e) {
            fail(e.getMessage());
        }

        assertEquals(
            DB.INSTANCE.getKeySpace("Test")
                       .getColumnFamily("Users")
                       .getRow(rowKey)
                       .getColumn(keys[0])
                       .getKey(),
            keys[0]
        );
        assertEquals(
            DB.INSTANCE.getKeySpace("Test")
                       .getColumnFamily("Users")
                       .getRow(rowKey)
                       .getColumn(keys[0])
                       .getValue(),
            values[0]
        );
    }

    @Test
    public void testExecuteIfRowExists() {

        IDataModel model = new HashMapDataModel();
        ByteBuffer[] keys = new ByteBuffer[] {
            ByteBuffer.wrap("Name".getBytes())
        };
        ByteBuffer[] values = new ByteBuffer[] {
            ByteBuffer.wrap("Piotr".getBytes())
        };
        ByteBuffer rowKey = ByteBuffer.wrap("Piotr".getBytes());

        IDBCommand command = new InsertCommand(
            "Users", "Test", rowKey, keys, values
        );
        IColumnFamily columnFamily = model.createColumnFamily();
        IColumn column = model.createColumn(keys[0]);

        try {
            columnFamily.addRow(model.createRow(rowKey));
            columnFamily.getRow(rowKey).addColumn(column);

            DB.INSTANCE.addKeySpace(model.createKeySpace("Test"));
            DB.INSTANCE.getKeySpace("Test")
                       .addColumnFamily("Users", columnFamily);

            assertEquals(
                DB.INSTANCE.getKeySpace("Test")
                           .getColumnFamily("Users")
                           .getRow(rowKey)
                           .getColumn(keys[0])
                           .getValue(),
                null
            );

            command.execute(model);
        } catch(PythiaException e) {
            fail(e.getMessage());
        }

        assertEquals(
            DB.INSTANCE.getKeySpace("Test")
                       .getColumnFamily("Users")
                       .getRow(rowKey)
                       .getColumn(keys[0])
                       .getKey(),
            keys[0]
        );
        assertEquals(
            DB.INSTANCE.getKeySpace("Test")
                       .getColumnFamily("Users")
                       .getRow(rowKey)
                       .getColumn(keys[0])
                       .getValue(),
            values[0]
        );
    }
}
