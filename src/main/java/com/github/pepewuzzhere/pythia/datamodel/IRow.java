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
import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Row is map of columns and is part of {@link IColumnFamily}. Row is similar
 * to row in relational databases (with <code>ColumnFamily</code>
 * similarity to table). Row has also a key that is used to find it in column
 * family.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public interface IRow extends Serializable {

    /**
     * Gets a key of row. Key is unique and final, should be set
     * in constructor.
     *
     * @return Key stored in {@link java.nio.ByteBuffer}.
     */
    ByteBuffer getKey();

    /**
     * Adds column to this row. Column should have unique key or existing
     * column will be changed.
     *
     * @param column New column added to row
     */
    void addColumn(IColumn column);

    /**
     * Gets column by key.
     *
     * @param key Key of column
     * @return Column if exists or null
     */
    IColumn getColumn(ByteBuffer key);

    /**
     * Updates value in existing column.
     *
     * @param key Key of column
     * @param value New value of column
     * @throws PythiaException
     */
    void updateColumn(ByteBuffer key, ByteBuffer value) throws PythiaException;

    /**
     * Deletes column in this row with given key.
     *
     * @param key Key of column
     * @throws PythiaException
     */
    void deleteColumn(ByteBuffer key) throws PythiaException;
}