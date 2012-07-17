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
package com.github.pepewuzzhere.pythia.server;

import com.github.pepewuzzhere.pythia.DB;
import com.github.pepewuzzhere.pythia.datamodel.hashmap.HashMapDataModel;
import com.github.pepewuzzhere.pythia.storage.SerializationStorage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.Socket;
import static org.junit.Assert.assertEquals;
import org.junit.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 */
public class ServerThreadTest {

    public ServerThreadTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        DB.INSTANCE.dropDB();
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
        DB.INSTANCE.dropDB();
    }

    @Test
    public void testRun() throws Exception {

        final Socket socket = mock(Socket.class);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ByteArrayInputStream in = new ByteArrayInputStream(
            "KEYSPACE test".getBytes()
        );

        when(socket.getOutputStream()).thenReturn(out);
        when(socket.getInputStream()).thenReturn(in);
        when(socket.isClosed()).thenReturn(Boolean.TRUE);

        ServerThread thread = new ServerThread(
                socket, new HashMapDataModel(), new SerializationStorage());

        thread.run();

        assertEquals(
            "OK" + System.getProperty("line.separator"),
            new String(out.toByteArray())
        );
    }
}
