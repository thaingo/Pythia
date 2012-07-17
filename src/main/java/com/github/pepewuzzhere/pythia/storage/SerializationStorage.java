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

import com.github.pepewuzzhere.pythia.DB;
import com.github.pepewuzzhere.pythia.PythiaException;
import com.github.pepewuzzhere.pythia.datamodel.IColumnFamily;
import com.github.pepewuzzhere.pythia.datamodel.IDataModel;
import com.github.pepewuzzhere.pythia.datamodel.IKeySpace;
import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Implementation of storage using object serialization. Data are written using
 * this routines:
 *  - every keyspace has own folder
 *  - column family is written as serialized object to file
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public class SerializationStorage implements IStorage {

    @Override
    public void read(String root, IDataModel model)
            throws IOException, PythiaException
    {
        File db = new File(root);
        if (db != null && db.exists()) {
            for (File f : db.listFiles()) {
                if (f != null && f.isDirectory()) {
                    DB.INSTANCE.addKeySpace(model.createKeySpace(f.getName()));
                    for (File cf : f.listFiles()) {
                        try (
                            ObjectInputStream in = new ObjectInputStream(
                                new FileInputStream(cf))
                        ) {
                            try {
                                DB.INSTANCE.
                                    getKeySpace(f.getName()).addColumnFamily(
                                    cf.getName(), (IColumnFamily)in.readObject()

                                );
                            } catch (ClassNotFoundException ex) {
                                throw new PythiaException(ex.getMessage());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public synchronized void write(String root) throws IOException {

        // foreach keyspace
        Set<Map.Entry<String, IKeySpace>> keyspaces =
                DB.INSTANCE.keySpaces.entrySet();
        for (
            Iterator<Map.Entry<String, IKeySpace>> it = keyspaces.iterator();
            it.hasNext();
        ) {
            Map.Entry<String, IKeySpace> k = it.next();
            IKeySpace keyspace = k.getValue();
            // create folder if needed
            if (keyspace.isDirty()) {
                File dir = new File(root + "/" + k.getKey());
                dir.mkdirs();
                keyspace.setClean();
            }

            // foreach column family in keyspace
            Set<Map.Entry<String, IColumnFamily>> columnfamilies =
                keyspace.getColumnFamilies().entrySet();
            for (
                Iterator<Map.Entry<String, IColumnFamily>> it2 =
                        columnfamilies.iterator();
                it2.hasNext();
            ) {
                Map.Entry<String, IColumnFamily> c = it2.next();
                IColumnFamily columnfamily = c.getValue();
                // save to disc if needed
                if (columnfamily.isDirty()) {
                    try (ObjectOutputStream out =
                            new ObjectOutputStream(
                                new FileOutputStream(
                                    root + "/" + k.getKey() + "/" + c.getKey()
                                )
                            )
                    ) {
                        out.writeObject(columnfamily);
                        out.flush();
                    }

                    columnfamily.setClean();
                }
            }

            // delete column families
            Stack<String> cfToDel =
                    DB.INSTANCE.columnFamilyToDelete.get(k.getKey());
            if (cfToDel != null) {
                while (!cfToDel.isEmpty()) {
                    doDelete(new File(
                            root + "/" + k.getKey() + "/" + cfToDel.pop()));
                }
            }
        }

        // delete keyspaces
        while (!DB.INSTANCE.keyspaceToDelete.isEmpty()) {
            doDelete(new File(root + "/" + DB.INSTANCE.keyspaceToDelete.pop()));
        }
    }

    private void doDelete(File path) throws IOException {
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