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
import java.nio.ByteBuffer;

/**
 * <code>ColumnFamily</code> is map of {@link IRow}.
 *
 * It is similar to table in relational databases.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public interface IColumnFamily {

    /**
     * Adds new row to column family.
     *
     * Row has unique key in column family.
     *
     * @param row new row added to column family.
     * @throws PythiaException if row already exists in column family
     */
    void addRow(final IRow row) throws PythiaException;

    /**
     * Gets row by key.
     *
     * @param key key of row
     * @return row or null if not found
     */
    IRow getRow(final ByteBuffer key);

    /**
     * Updates column value in specified row.
     *
     * @param key key of row
     * @param columnKey key of column in row to update
     * @param columnValue new value of column
     *
     * @throws PythiaException if row  or column doesn't exists
     */
    void updateRow(
        final ByteBuffer key, final ByteBuffer columnKey,
        final ByteBuffer columnValue
    ) throws PythiaException;

    /**
     * Deletes row in this column family.
     *
     * @param key key of row to delete
     * @throws PythiaException if row doesn't exists
     */
    void deleteRow(final ByteBuffer key) throws PythiaException;

    /**
     * Checks if this column family should be saved in storage.
     *
     * @return changes was made?
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
