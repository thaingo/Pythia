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
import com.github.pepewuzzhere.pythia.datamodel.IRow;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

/**
 * Adaptation of {@link com.github.pepewuzzhere.pythia.datamodel.IRow}
 * using <code>java.util.HashMap</code>.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
class Row implements IRow, Serializable {

    private static final long serialVersionUID = 1L;

    private final byte[] key;
    private final Map<ByteArrayWrapper, IColumn> columns = new HashMap<>();

    /**
     * Sets key of created row.
     *
     * @param key key of this row
     */
    public Row(final ByteBuffer key) {
        this.key = key.array();
    }

    @Override
    public void addColumn(final IColumn column) {
        if (columns.containsKey(toKey(column.getKey()))) {
            // update value in exiting column
            columns.get(toKey(column.getKey())).setValue(column.getValue());
        } else {
            columns.put(toKey(column.getKey()), column);
        }
    }

    @Override
    public IColumn getColumn(final ByteBuffer key) {
        return columns.get(toKey(key));
    }

    @Override
    public void updateColumn(final ByteBuffer key, final ByteBuffer value)
            throws PythiaException
    {
        if (columns.containsKey(toKey(key))) {
            columns.get(toKey(key)).setValue(value);
        } else {
            throw new PythiaException(PythiaError.DATA_NOT_FOUND);
        }
    }

    @Override
    public void deleteColumn(final ByteBuffer key) throws PythiaException {
        if (columns.containsKey(toKey(key))) {
            columns.remove(toKey(key));
        } else {
            throw new PythiaException(PythiaError.DATA_NOT_FOUND);
        }
    }

    @Override
    public ByteBuffer getKey() {
        return ByteBuffer.wrap(key);
    }

    @Override
    public String toString() {
        final StringBuilder ret = new StringBuilder();
        ret.append("{\"").append(new String(key)).append("\":{");

        Set<Map.Entry<ByteArrayWrapper, IColumn>> cols = columns.entrySet();

        for (
            Iterator<Entry<ByteArrayWrapper, IColumn>> it = cols.iterator();
            it.hasNext();
        ) {
            Entry<ByteArrayWrapper, IColumn> c = it.next();
            ret.append("\"");
            ret.append(new String(c.getValue().getKey().array()));
            ret.append("\"");
            ret.append(":");
            ret.append("\"");
            ret.append(new String(c.getValue().getValue().array()));
            ret.append("\"");
            if (it.hasNext()) {
                ret.append(",");
            }
        }
        ret.append("}}");

        return ret.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Row) {
            Row r = (Row)obj;

            return Arrays.equals(key, r.getKey().array())
                   && columns.equals(r.getColumns());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Arrays.hashCode(this.key);
        hash = 79 * hash + Objects.hashCode(this.columns);
        return hash;
    }

    /**
     * Gets list of columns in this row.
     *
     * @return list of columns
     */
    protected Map<ByteArrayWrapper, IColumn> getColumns() {
        return new HashMap<>(columns);
    }

    private ByteArrayWrapper toKey(final ByteBuffer buffer) {
        return new ByteArrayWrapper(buffer.array());
    }

}