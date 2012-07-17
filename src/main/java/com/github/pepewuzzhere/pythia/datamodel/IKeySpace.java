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
package com.github.pepewuzzhere.pythia.datamodel;

import com.github.pepewuzzhere.pythia.PythiaException;
import java.util.HashMap;

/**
 * KeySpace has similar meaning like database name in relational databases.
 * It is set of {@link IColumnFamily}. Before any operation on Pythia database
 * current keyspace should be determined. KeySpace should have unique name
 * in Pytha database.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public interface IKeySpace {

    /**
     * Adds new column family to keyspace. Name should be unique or exception
     * will be thrown.
     *
     * @param name Name of column family
     * @param columnFamily Column family to add
     * @throws PythiaException
     */
    void addColumnFamily(String name, IColumnFamily columnFamily)
            throws PythiaException;

    /**
     * Gets column family by specified name.
     *
     * @param name Name of column family
     * @return <code>IColumnFamily</code> or null
     */
    IColumnFamily getColumnFamily(String name);

    /**
     * Drops column family from this keyspace.
     *
     * @param name Name of column family
     * @throws PythiaException
     */
    void dropColumnFamily(String name) throws PythiaException;

    /**
     * Gets name of this keyspace, should be final and unique.
     * @return Name of keyspace
     */
    String getName();

    /**
     * Checks if keyspace should be saved in storage.
     *
     * @return Should be saved?
     */
    boolean isDirty();

    /**
     * Sets this keyspace as saved and up to date.
     */
    void setClean();

    /**
     * Gets list of colum families in this keyspace.
     *
     * @return List of column families
     */
    HashMap<String, IColumnFamily> getColumnFamilies();
}
