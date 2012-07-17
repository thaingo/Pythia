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
import com.github.pepewuzzhere.pythia.datamodel.IColumnFamily;
import com.github.pepewuzzhere.pythia.datamodel.IKeySpace;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 */
public class KeySpaceTest {

    public KeySpaceTest() {
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
    public void testAddNewColumnFamily() {
        IKeySpace ks      = new KeySpace("Test");
        IColumnFamily col = new ColumnFamily();

        try {
            ks.addColumnFamily("Users", col);
        } catch(PythiaException e) {
            fail(e.getMessage());
        }

        assertEquals(ks.getColumnFamily("Users"), col);
    }

    @Test
    public void testAddColumnFamilyIfAlreadyExists() {
        IKeySpace ks      = new KeySpace("Test");
        IColumnFamily col = new ColumnFamily();

        boolean wasThrown = false;
        try {
            ks.addColumnFamily("Users", col);
            ks.addColumnFamily("Users", col);
        } catch(PythiaException e) {
            wasThrown = true;
        }

        assertTrue(wasThrown);
    }

    @Test
    public void testDropColumnFamily() {
        IKeySpace ks      = new KeySpace("Test");
        IColumnFamily col = new ColumnFamily();

        try {
            ks.addColumnFamily("Users", col);
            ks.dropColumnFamily("Users");
        } catch(PythiaException e) {
            fail(e.getMessage());
        }

        assertEquals(ks.getColumnFamily("Users"), null);
    }

    @Test
    public void testDropColumnFamilyIfNotExists() {
        IKeySpace ks      = new KeySpace("Test");
        IColumnFamily col = new ColumnFamily();

        boolean wasThrown = false;
        try {
            ks.dropColumnFamily("Users");
        } catch(PythiaException e) {
            wasThrown = true;
        }

        assertTrue(wasThrown);
    }

    @Test
    public void testGetName() {
        IKeySpace ks = new KeySpace("Test");

        assertEquals(ks.getName(), "Test");
    }
}
