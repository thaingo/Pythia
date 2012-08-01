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
package com.github.pepewuzzhere.pythia.storage;

import com.github.pepewuzzhere.pythia.Context;
import com.github.pepewuzzhere.pythia.DB;
import com.github.pepewuzzhere.pythia.datamodel.IColumnFamily;
import com.github.pepewuzzhere.pythia.datamodel.IDataModel;
import com.github.pepewuzzhere.pythia.datamodel.hashmap.HashMapDataModel;
import com.github.pepewuzzhere.pythia.pql.command.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 */
public class SerializationStorageTest {

    private final static String ROOT =
            System.getProperty("java.io.tmpdir") + "pythia";

    public SerializationStorageTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        // create root folder
        File root = new File(ROOT);
        root.mkdirs();

        // delete root folder
        try {
            doDelete(new File(ROOT));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DB.INSTANCE.dropDB();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        DB.INSTANCE.dropDB();
        // create root folder
        File root = new File(ROOT);
        root.mkdirs();
    }

    @After
    public void tearDown() {
        DB.INSTANCE.dropDB();
        // delete root folder
        try {
            doDelete(new File(ROOT));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testRead() throws Exception {
        IDataModel model = new HashMapDataModel();
        // write data to disc
        File db = new File(ROOT + "/Test");
        db.mkdirs();

        db = new File(ROOT + "/Test2");
        db.mkdirs();

        IColumnFamily columnFamily = model.createColumnFamily();
        columnFamily.addRow(
            model.createRow(ByteBuffer.wrap("pepe".getBytes()))
        );
        try (ObjectOutputStream out =
                new ObjectOutputStream(
                    new FileOutputStream(
                        ROOT + "/Test/Users"
                    )
                )
        ) {
            out.writeObject(columnFamily);
            out.flush();
        }

        IColumnFamily columnFamily2 = model.createColumnFamily();
        columnFamily2.addRow(
            model.createRow(ByteBuffer.wrap("hello".getBytes()))
        );
        try (ObjectOutputStream out =
                new ObjectOutputStream(
                    new FileOutputStream(
                        ROOT + "/Test2/Topics"
                    )
                )
        ) {
            out.writeObject(columnFamily2);
            out.flush();
        }

        // read data from storage
        IStorage storage = new SerializationStorage();
        storage.read(ROOT, new HashMapDataModel());

        assertNotNull(DB.INSTANCE.getKeySpace("Test"));
        assertNotNull(DB.INSTANCE.getKeySpace("Test2"));

        assertEquals(
            DB.INSTANCE.getKeySpace("Test").getColumnFamily("Users"),
            columnFamily
        );
        assertEquals(
            DB.INSTANCE.getKeySpace("Test2").getColumnFamily("Topics"),
            columnFamily2
        );
    }

    @Test
    public void testWrite() throws Exception {

        // create test data base
        Context ctx = new Context();
        IDBCommand[] commands = new IDBCommand[] {
            new CreateKeySpaceCommand("Test"),
            new UseKeySpaceCommand("Test", ctx),
            new CreateColumnFamilyCommand("Users", "Test"),
            new InsertCommand(
                "Users",
                "Test",
                ByteBuffer.wrap("pepe".getBytes()),
                new ByteBuffer[] {
                    ByteBuffer.wrap("name".getBytes()),
                },
                new ByteBuffer[] {
                    ByteBuffer.wrap("Piotr".getBytes()),
                }
            ),
            new CreateColumnFamilyCommand("Comments", "Test"),
            new InsertCommand(
                "Comments",
                "Test",
                ByteBuffer.wrap("pepe".getBytes()),
                new ByteBuffer[] {
                    ByteBuffer.wrap("content".getBytes()),
                },
                new ByteBuffer[] {
                    ByteBuffer.wrap("testtest".getBytes()),
                }
            ),
            new CreateKeySpaceCommand("Test2"),
            new UseKeySpaceCommand("Test2", ctx),
            new CreateColumnFamilyCommand("Posts", "Test2"),
            new InsertCommand(
                "Posts",
                "Test2",
                ByteBuffer.wrap("first".getBytes()),
                new ByteBuffer[] {
                    ByteBuffer.wrap("content".getBytes()),
                },
                new ByteBuffer[] {
                    ByteBuffer.wrap("blablabla".getBytes()),
                }
            ),
            new CreateColumnFamilyCommand("Topics", "Test2"),
            new InsertCommand(
                "Topics",
                "Test2",
                ByteBuffer.wrap("new topic".getBytes()),
                new ByteBuffer[] {
                    ByteBuffer.wrap("subject".getBytes()),
                },
                new ByteBuffer[] {
                    ByteBuffer.wrap("testtest".getBytes()),
                }
            )
        };

        IDataModel model = new HashMapDataModel();
        for (int i = 0; i < commands.length; ++i) {
            commands[i].execute(model);
        }

        // write data to storage
        IStorage storage = new SerializationStorage();
        storage.write(ROOT);

        // test keyspaces
        File path = new File(ROOT);
        String[] list = path.list();
        Arrays.sort(list);
        assertArrayEquals(
            new String[] {
                "Test",
                "Test2"
            },
            list
        );

        // test column families
        File keyspace1 = new File(ROOT + "/Test");
        String[] keyspace1List = keyspace1.list();
        Arrays.sort(keyspace1List);
        assertArrayEquals(
            new String[] {
                "Comments",
                "Users"
            },
            keyspace1List
        );

        ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(ROOT + "/Test/Comments"));
        assertEquals(
            DB.INSTANCE.getKeySpace("Test").getColumnFamily("Comments"),
            (IColumnFamily)in.readObject()
        );
        in.close();

        in = new ObjectInputStream(
                new FileInputStream(ROOT + "/Test/Users"));
        assertEquals(
            DB.INSTANCE.getKeySpace("Test").getColumnFamily("Users"),
            (IColumnFamily)in.readObject()
        );

        File keyspace2 = new File(ROOT + "/Test2");
        String[] keyspace2List = keyspace2.list();
        Arrays.sort(keyspace2List);
        assertArrayEquals(
            new String[] {
                "Posts",
                "Topics"
            },
            keyspace2List
        );

        in.close();

        in = new ObjectInputStream(
                new FileInputStream(ROOT + "/Test2/Posts"));
        assertEquals(
            DB.INSTANCE.getKeySpace("Test2").getColumnFamily("Posts"),
            (IColumnFamily)in.readObject()
        );

        in.close();

        in = new ObjectInputStream(
                new FileInputStream(ROOT + "/Test2/Topics"));
        assertEquals(
            DB.INSTANCE.getKeySpace("Test2").getColumnFamily("Topics"),
            (IColumnFamily)in.readObject()
        );

        // test updating column family
        IDBCommand insertNew = new InsertCommand(
            "Posts",
            "Test2",
            ByteBuffer.wrap("second".getBytes()),
            new ByteBuffer[] {
                ByteBuffer.wrap("content".getBytes()),
            },
            new ByteBuffer[] {
                ByteBuffer.wrap("blablabla".getBytes()),
            }
        );
        insertNew.execute(model);
        storage.write(ROOT);

        in.close();

        in = new ObjectInputStream(
                new FileInputStream(ROOT + "/Test2/Posts"));
        assertEquals(
            DB.INSTANCE.getKeySpace("Test2").getColumnFamily("Posts"),
            (IColumnFamily)in.readObject()
        );

        in.close();

        // test deleting column family
        IDBCommand delete = new DropColumnFamilyCommand("Users", "Test");
        delete.execute(model);
        storage.write(ROOT);
        File deleted = new File(ROOT + "/Test/Users");
        assertFalse(deleted.exists());

        // test deleting keyspace
        IDBCommand drop = new DropKeySpaceCommand("Test2");
        drop.execute(model);
        storage.write(ROOT);
        File droped = new File(ROOT + "/Test2");
        assertFalse(droped.exists());

    }

    private static void doDelete(File path) throws IOException {
        if (path.isDirectory()) {
            for (File child : path.listFiles()) {
                doDelete(child);
            }
        }
        if (!path.delete()) {
            throw new IOException("Could not delete " + path);
        }
    }

}
