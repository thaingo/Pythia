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

import com.github.pepewuzzhere.pythia.PythiaError;
import com.github.pepewuzzhere.pythia.PythiaException;
import com.github.pepewuzzhere.pythia.datamodel.IColumnFamily;
import com.github.pepewuzzhere.pythia.datamodel.IKeySpace;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Adaptation of {@link com.github.pepewuzzhere.pythia.datamodel.IKeySpace}
 * using <code>java.util.HashMap</code>.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
class KeySpace implements IKeySpace {

    private final String name;
    private final Map<String, IColumnFamily> columns;

    private boolean isDirty;

    /**
     * Sets name for keyspace.
     *
     * @param name name of keyspace
     * @throws IllegalArgumentException if keyspace name is empty
     */
    KeySpace(final String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Keyspace name is required");
        }
        this.name = name;
        isDirty = true;
        columns = new ConcurrentHashMap<>();
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException if name is empty or ColumnFamily is null
     */
    @Override public void addColumnFamily(
        final String name, final IColumnFamily columnFamily
    ) throws PythiaException {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("ColumnFamily name is required");
        }
        if (columnFamily == null) {
            throw new IllegalArgumentException(
                    "ColumnFamily object is required");
        }
        if (columns.containsKey(name)) {
            throw new PythiaException(PythiaError.KEY_ALREADY_EXISTS);
        } else {
            columns.put(name, columnFamily);
        }
    }

    @Override
    public IColumnFamily getColumnFamily(final String name) {
        return columns.get(name);
    }

    @Override
    public void dropColumnFamily(final String name) throws PythiaException {
        if (columns.containsKey(name)) {
            columns.remove(name);
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
    public Map<String, IColumnFamily> getColumnFamilies() {
        return new HashMap<>(columns);
    }

}