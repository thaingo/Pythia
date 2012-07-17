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

import com.github.pepewuzzhere.pythia.DB;
import com.github.pepewuzzhere.pythia.PythiaError;
import com.github.pepewuzzhere.pythia.PythiaException;
import com.github.pepewuzzhere.pythia.datamodel.IColumnFamily;
import com.github.pepewuzzhere.pythia.datamodel.IKeySpace;
import java.util.HashMap;
import java.util.Stack;

/**
 * Adaptation of {@link com.github.pepewuzzhere.pythia.datamodel.IKeySpace}
 * using <code>java.util.HashMap</code>.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public class KeySpace implements IKeySpace {

    private final String name;
    private final HashMap<String, IColumnFamily> columns = new HashMap<>();
    private boolean isDirty;

    /**
     * Sets name for keyspace.
     *
     * @param name Name of keyspace.
     */
    public KeySpace(String name) {
        this.name = name;
        isDirty = true;
    }

    @Override
    public void addColumnFamily(String name, IColumnFamily columnFamily)
            throws PythiaException
    {
        if (columns.containsKey(name)) {
            throw new PythiaException(PythiaError.KEY_ALREADY_EXISTS);
        } else {
            columns.put(name, columnFamily);
        }
    }

    @Override
    public IColumnFamily getColumnFamily(String name) {
        return columns.get(name);
    }

    @Override
    public void dropColumnFamily(String name) throws PythiaException {
        if (columns.containsKey(name)) {
            columns.remove(name);
            if (DB.INSTANCE.columnFamilyToDelete.containsKey(this.name)) {
                DB.INSTANCE.columnFamilyToDelete.get(this.name).push(name);
            } else {
                DB.INSTANCE.columnFamilyToDelete.put(
                        this.name, new Stack<String>());
                DB.INSTANCE.columnFamilyToDelete.get(this.name).push(name);
            }

        } else {
            throw new PythiaException(PythiaError.DATA_NOT_FOUND);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isDirty() {
        return isDirty;
    }

    @Override
    public void setClean() {
        isDirty = false;
    }

    @Override
    public HashMap<String, IColumnFamily> getColumnFamilies() {
        return columns;
    }

}
