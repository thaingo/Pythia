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
import com.github.pepewuzzhere.pythia.datamodel.IColumn;
import com.github.pepewuzzhere.pythia.datamodel.IColumnFamily;
import com.github.pepewuzzhere.pythia.datamodel.IRow;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Adaptation of {@link com.github.pepewuzzhere.pythia.datamodel.IColumnFamily}
 * using <code>java.util.HashMap</code>.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
class ColumnFamily implements IColumnFamily, Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<ByteArrayWrapper, IRow> rows = new HashMap<>();
    private boolean isDirty;

    /**
     * Creates new column family and sets this as dirty - should be saved to
     * disc.
     */
    public ColumnFamily() {
        isDirty = true;
    }

    @Override
    public void addRow(final IRow row) throws PythiaException {
        if (rows.containsKey(toKey(row.getKey()))) {
            throw new PythiaException(PythiaError.KEY_ALREADY_EXISTS);
        } else {
            rows.put(toKey(row.getKey()), row);
        }
        setDirty();
    }

    @Override
    public IRow getRow(ByteBuffer key) {
        return rows.get(toKey(key));
    }

    @Override
    public void updateRow(
        final ByteBuffer key, final ByteBuffer columnKey,
        final ByteBuffer columnValue
    ) throws PythiaException {
        if (rows.containsKey(toKey(key))) {
            final IColumn col = rows.get(toKey(key)).getColumn(columnKey);
            if (col != null) {
                col.setValue(columnValue);
            } else {
                throw new PythiaException(PythiaError.DATA_NOT_FOUND);
            }
        } else {
            throw new PythiaException(PythiaError.DATA_NOT_FOUND);
        }
        setDirty();
    }

    @Override
    public void deleteRow(final ByteBuffer key) throws PythiaException {
        if (rows.containsKey(toKey(key))) {
            rows.remove(toKey(key));
        } else {
            throw new PythiaException(PythiaError.DATA_NOT_FOUND);
        }
        setDirty();
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
    public void setDirty() {
        isDirty = true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ColumnFamily) {
            final ColumnFamily c = (ColumnFamily)obj;

            return rows.equals(c.getRows());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.rows);
        return hash;
    }

    /*
     * Gets row list of this column family.
     *
     * @return list of column family rows
     */
    Map<ByteArrayWrapper, IRow> getRows() {
        return new HashMap<>(rows);
    }

    /*
     * Wraps {@link ByteBuffer} in {@link ByteArrayWrapper}.
     *
     * Used to serialize and compare {@link ByteBuffer}
     *
     * @param buffer {@link ByteBuffer} to wrap
     * @return wrapped buffer
     */
    private ByteArrayWrapper toKey(final ByteBuffer buffer) {
        return new ByteArrayWrapper(buffer.array());
    }

}
