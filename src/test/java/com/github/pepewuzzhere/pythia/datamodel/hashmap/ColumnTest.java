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

import java.io.*;
import java.nio.ByteBuffer;
import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 */
public class ColumnTest {

    public ColumnTest() {
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
    public void testGetKey() {
        ByteBuffer key = ByteBuffer.wrap("Test key".getBytes());
        Column column  = new Column(key);

        assertEquals(key, column.getKey());
    }

    @Test
    public void testGetValue() {
        ByteBuffer key   = ByteBuffer.wrap("Test key".getBytes());
        ByteBuffer value = ByteBuffer.wrap("Test value".getBytes());
        Column column    = new Column(key);

        column.setValue(value);

        assertEquals(value, column.getValue());
    }

    @Test
    public void testSetValue() {
        ByteBuffer key      = ByteBuffer.wrap("Test key".getBytes());
        ByteBuffer value    = ByteBuffer.wrap("Test value".getBytes());
        Column column       = new Column(key);
        ByteBuffer newValue = ByteBuffer.wrap("Test value2".getBytes());
        ByteBuffer oldValue = column.setValue(newValue);

        assertEquals(newValue, column.getValue());
        assertEquals(oldValue, null);

        oldValue = column.setValue(key);
        assertEquals(oldValue, newValue);
    }

    @Test
    public void testSerialize() throws Exception {
        ByteBuffer key      = ByteBuffer.wrap("Test key".getBytes());
        Column column       = new Column(key);
        ByteBuffer newValue = ByteBuffer.wrap("Test value2".getBytes());
        column.setValue(newValue);

        String tmpFile = System.getProperty("java.io.tmpdir") + "/tmp";

        ObjectOutputStream out = new ObjectOutputStream(
            new FileOutputStream(tmpFile)
        );
        out.writeObject(column);

        ObjectInputStream in = new ObjectInputStream(
            new FileInputStream(tmpFile)
        );

        Column readed = (Column)in.readObject();
        assertEquals(column, readed);

        File f = new File(tmpFile);
        f.delete();
    }
}
