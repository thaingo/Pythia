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
package com.github.pepewuzzhere.pythia.datamodel.hashmap;

import com.github.pepewuzzhere.pythia.PythiaException;
import com.github.pepewuzzhere.pythia.datamodel.IColumn;
import com.github.pepewuzzhere.pythia.datamodel.IColumnFamily;
import com.github.pepewuzzhere.pythia.datamodel.IRow;
import java.io.*;
import java.nio.ByteBuffer;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 */
public class ColumnFamilyTest {

    public ColumnFamilyTest() {
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
    public void testAddNewRow() {
        IColumnFamily table = new ColumnFamily();
        ByteBuffer key      = ByteBuffer.wrap("Test key".getBytes());
        IRow row            = new Row(key);

        try {
            table.addRow(row);
        } catch (PythiaException e) {
            fail(e.getMessage());
        }

        assertEquals(table.getRow(key), row);
    }

    @Test
    public void testAddRowIfAlreadyExists() {
        IColumnFamily table = new ColumnFamily();
        ByteBuffer key      = ByteBuffer.wrap("Test key".getBytes());
        IRow row            = new Row(key);
        IRow row2           = new Row(key);

        boolean exceptionWasThrown = false;
        try {
            table.addRow(row);
            table.addRow(row2);
        } catch (PythiaException e) {
            exceptionWasThrown = true;
        }

        assertTrue(exceptionWasThrown);
    }

    @Test
    public void testUpdateRow() {
        IColumnFamily table = new ColumnFamily();
        ByteBuffer key      = ByteBuffer.wrap("Test key".getBytes());
        ByteBuffer key2     = ByteBuffer.wrap("Test key2".getBytes());
        ByteBuffer value    = ByteBuffer.wrap("Test value".getBytes());
        ByteBuffer newValue = ByteBuffer.wrap("Test value2".getBytes());
        IRow row            = new Row(key);
        IColumn column      = new Column(key2);

        column.setValue(value);
        row.addColumn(column);
        try {
            table.addRow(row);
            table.updateRow(key, key2, newValue);
        } catch (PythiaException e) {
            fail(e.getMessage());
        }

        assertEquals(table.getRow(key).getColumn(key2).getValue(), newValue);
    }

    @Test
    public void testUpdateRowIfRowNotExists() {
        IColumnFamily table = new ColumnFamily();
        ByteBuffer key      = ByteBuffer.wrap("Test key".getBytes());
        ByteBuffer key2     = ByteBuffer.wrap("Test key2".getBytes());
        ByteBuffer value    = ByteBuffer.wrap("Test value".getBytes());
        ByteBuffer newValue = ByteBuffer.wrap("Test value2".getBytes());
        IRow row            = new Row(key);
        IColumn column      = new Column(key2);

        column.setValue(value);
        row.addColumn(column);
        boolean exceptionWasThrown = false;
        try {
            table.updateRow(key2, key2, newValue);
        } catch (PythiaException e) {
            exceptionWasThrown = true;
        }

        assertTrue(exceptionWasThrown);
    }

    @Test
    public void testUpdateRowIfColumnNotExists() {
        IColumnFamily table = new ColumnFamily();
        ByteBuffer key      = ByteBuffer.wrap("Test key".getBytes());
        ByteBuffer key2     = ByteBuffer.wrap("Test key2".getBytes());
        ByteBuffer value    = ByteBuffer.wrap("Test value".getBytes());
        ByteBuffer newValue = ByteBuffer.wrap("Test value2".getBytes());
        IRow row            = new Row(key);
        IColumn column      = new Column(key2);

        column.setValue(value);
        row.addColumn(column);
        boolean exceptionWasThrown = false;
        try {
            table.addRow(row);
            table.updateRow(key, key, newValue);
        } catch (PythiaException e) {
            exceptionWasThrown = true;
        }

        assertTrue(exceptionWasThrown);
    }

    @Test
    public void testDeleteRow() {
        IColumnFamily table = new ColumnFamily();
        ByteBuffer key      = ByteBuffer.wrap("Test key".getBytes());
        IRow row            = new Row(key);

        try {
            table.addRow(row);
            table.deleteRow(key);
        } catch (PythiaException e) {
            fail(e.getMessage());
        }

        assertEquals(table.getRow(key), null);
    }

    @Test
    public void testDeleteRowIfNotExists() {
        IColumnFamily table = new ColumnFamily();
        ByteBuffer key      = ByteBuffer.wrap("Test key".getBytes());

        boolean exceptionWasThrown = false;
        try {
            table.deleteRow(key);
        } catch (PythiaException e) {
            exceptionWasThrown = true;
        }

        assertTrue(exceptionWasThrown);
    }

    @Test
    public void testSerialize() throws Exception {
        ByteBuffer key      = ByteBuffer.wrap("Test key".getBytes());
        Column column       = new Column(key);
        ByteBuffer newValue = ByteBuffer.wrap("Test value2".getBytes());
        column.setValue(newValue);

        Row row = new Row(ByteBuffer.wrap("Test row key".getBytes()));
        row.addColumn(column);

        ColumnFamily col = new ColumnFamily();
        col.addRow(row);

        String tmpFile = System.getProperty("java.io.tmpdir") + "/tmp";

        ObjectOutputStream out = new ObjectOutputStream(
            new FileOutputStream(tmpFile)
        );
        out.writeObject(col);

        ObjectInputStream in = new ObjectInputStream(
            new FileInputStream(tmpFile)
        );

        ColumnFamily readed = (ColumnFamily)in.readObject();
        assertEquals(col, readed);

        File f = new File(tmpFile);
        f.delete();
    }
}
