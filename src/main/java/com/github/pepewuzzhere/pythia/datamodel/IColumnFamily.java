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
 * <code>ColumnFamily</code> is map of {@link IRow}. It is similar to table
 * in relational databases.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public interface IColumnFamily extends Serializable {

    /**
     * Adds new row to column family. Row has unique key in column family.
     *
     * @param row New row added to column family.
     * @throws PythiaException
     */
    void addRow(IRow row) throws PythiaException;

    /**
     * Gets row by key.
     *
     * @param key Key of row
     * @return Row or null if not found
     */
    IRow getRow(ByteBuffer key);

    /**
     * Updates column value in specified row.
     *
     * @param key Key of row
     * @param columnKey Key of column in row to update
     * @param columnValue New value of column
     *
     * @throws PythiaException
     */
    void updateRow(
            ByteBuffer key, ByteBuffer columnKey, ByteBuffer columnValue)
            throws PythiaException;

    /**
     * Deletes row in this column family.
     *
     * @param key Key of row to delete
     * @throws PythiaException
     */
    void deleteRow(ByteBuffer key) throws PythiaException;

    /**
     * Checks if this column family should be saved in storage.
     *
     * @return Should be saved?
     */
    boolean isDirty();

    /**
     * Sets this column family as saved and up to date.
     */
    void setClean();

    /**
     * Sets this column family as dirty - should be saved to disc.
     */
    void setDirty();
}
