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

import com.github.pepewuzzhere.pythia.datamodel.IColumn;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Implements {@link IColumn} interface for adaptation to use in
 * <code>java.util.HashMap</code> collection.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 */
class Column implements IColumn, Serializable {

    private static final long serialVersionUID = 1L;

    private final byte[] key;
    private byte[] value;

    /**
     * Constructor requires key because <code>Column</code> must have one, only
     * value is optional.
     *
     * @param key key of this column
     */
    public Column(final ByteBuffer key) {
        this.key = key.array();
    }

    @Override
    public ByteBuffer getKey() {
        return ByteBuffer.wrap(key);
    }

    @Override
    public ByteBuffer getValue() {
        if (value != null) {
            return ByteBuffer.wrap(value);
        } else {
            return null;
        }
    }

    @Override
    public ByteBuffer setValue(final ByteBuffer value) {
        ByteBuffer oldValue = null;
        if (this.value != null) {
            oldValue = ByteBuffer.wrap(this.value);
        }
        this.value = value.array();
        return oldValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Column) {
            Column c = (Column)obj;

            return
                (c.getKey() != null
                    ? Arrays.equals(key, c.getKey().array())
                    : null == c.getKey().array()
                )

                    &&

                (c.getValue() != null
                    ? Arrays.equals(value, c.getValue().array())
                    : null == c.getValue()
                 );
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Arrays.hashCode(this.key);
        hash = 67 * hash + Arrays.hashCode(this.value);
        return hash;
    }

}