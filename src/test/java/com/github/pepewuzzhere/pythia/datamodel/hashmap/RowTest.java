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
import com.github.pepewuzzhere.pythia.datamodel.IRow;
import java.io.*;
import java.nio.ByteBuffer;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 */
public class RowTest {

    public RowTest() {
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
    public void testAddColumnIfNotExists() {
        ByteBuffer key  = ByteBuffer.wrap("Test key".getBytes());
        ByteBuffer key2 = ByteBuffer.wrap("Test key2".getBytes());
        IRow row        = new Row(key);
        IColumn column  = new Column(key2);

        row.addColumn(column);

        assertEquals(column, row.getColumn(key2));
    }

    @Test
    public void testAddColumnIfKeyAlreadyExists() {
        ByteBuffer key  = ByteBuffer.wrap("Test key".getBytes());
        ByteBuffer key2 = ByteBuffer.wrap("Test key2".getBytes());
        IRow row        = new Row(key);
        IColumn column  = new Column(key2);
        IColumn column2 = new Column(key2);

        column2.setValue(key);

        row.addColumn(column);
        row.addColumn(column2);

        assertEquals(column, row.getColumn(key2));
        // value should be updated
        assertEquals(column.getValue(), column2.getValue());
    }

    @Test
    public void testUpdateColumn() {
        ByteBuffer key  = ByteBuffer.wrap("Test key".getBytes());
        IRow row        = new Row(key);
        ByteBuffer key2 = ByteBuffer.wrap("Test key2".getBytes());
        IColumn column  = new Column(key2);

        row.addColumn(column);

        try {
            row.updateColumn(key2, key);
        } catch (PythiaException e) {
            fail(e.getMessage());
        }

        assertEquals(row.getColumn(key2).getValue(), key);
    }

    @Test
    public void testDeleteColumn() throws PythiaException {
        ByteBuffer key  = ByteBuffer.wrap("Test key".getBytes());
        IRow row        = new Row(key);
        ByteBuffer key2 = ByteBuffer.wrap("Test key2".getBytes());
        IColumn column  = new Column(key2);

        row.addColumn(column);

        try {
            row.deleteColumn(key2);
        } catch (PythiaException e) {
            fail(e.getMessage());
        }

        assertEquals(row.getColumn(key), null);
    }

    @Test
    public void testUpdateColumnIfNotExists() {
        ByteBuffer key  = ByteBuffer.wrap("Test key".getBytes());
        IRow row        = new Row(key);
        ByteBuffer key2 = ByteBuffer.wrap("Test key2".getBytes());
        IColumn column  = new Column(key2);

        row.addColumn(column);

        boolean exceptionWasThrown = false;
        try {
            row.updateColumn(key, key);
        } catch (PythiaException e) {
            exceptionWasThrown = true;
        }

        assertTrue(exceptionWasThrown);
    }

    @Test
    public void testDeleteColumnIfNotExists() throws PythiaException {
        ByteBuffer key  = ByteBuffer.wrap("Test key".getBytes());
        IRow row        = new Row(key);
        ByteBuffer key2 = ByteBuffer.wrap("Test key2".getBytes());
        IColumn column  = new Column(key2);

        row.addColumn(column);

        boolean exceptionWasThrown = false;
        try {
            row.deleteColumn(key);
        } catch (PythiaException e) {
            exceptionWasThrown = true;
        }

        assertTrue(exceptionWasThrown);
    }

    @Test
    public void testGetKey() {
        ByteBuffer key  = ByteBuffer.wrap("Test key".getBytes());
        ByteBuffer key2 = ByteBuffer.wrap("Test key2".getBytes());
        IRow row        = new Row(key);

        assertEquals(row.getKey(), key);
    }

    @Test
    public void testSerialize() throws Exception {
        ByteBuffer key      = ByteBuffer.wrap("Test key".getBytes());
        Column column       = new Column(key);
        ByteBuffer newValue = ByteBuffer.wrap("Test value2".getBytes());
        column.setValue(newValue);

        Row row = new Row(ByteBuffer.wrap("Test row key".getBytes()));
        row.addColumn(column);

        String tmpFile = System.getProperty("java.io.tmpdir") + "/tmp";

        ObjectOutputStream out = new ObjectOutputStream(
            new FileOutputStream(tmpFile)
        );
        out.writeObject(row);

        ObjectInputStream in = new ObjectInputStream(
            new FileInputStream(tmpFile)
        );

        Row readed = (Row)in.readObject();
        assertEquals(row, readed);

        File f = new File(tmpFile);
        f.delete();
    }
}