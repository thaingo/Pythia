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

package com.github.pepewuzzhere.pythia.pql.command;

import com.github.pepewuzzhere.pythia.DB;
import com.github.pepewuzzhere.pythia.PythiaError;
import com.github.pepewuzzhere.pythia.PythiaException;
import com.github.pepewuzzhere.pythia.datamodel.IColumn;
import com.github.pepewuzzhere.pythia.datamodel.IColumnFamily;
import com.github.pepewuzzhere.pythia.datamodel.IDataModel;
import com.github.pepewuzzhere.pythia.datamodel.IKeySpace;
import com.github.pepewuzzhere.pythia.datamodel.IRow;
import java.nio.ByteBuffer;

/**
 * Command updates data in row existing in column family.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public class UpdateCommand implements IDBCommand {

    /*
     * Name of column family of updated row
     */
    private final String columnFamily;

    /*
     * Name of used keyspace
     */
    private final String keySpace;

    /*
     * Value of updated row key
     */
    private final ByteBuffer rowKey;

    /*
     * Updated keys
     */
    private final ByteBuffer[] keys;

    /*
     * New values of keys
     */
    private final ByteBuffer[] values;

    /**
     * Sets all parameters used to update existing row in column family.
     *
     * @param columnFamily name of column family
     * @param keySpace used keyspace
     * @param rowKey key of updated row
     * @param keys list of keys in row to update (must be same length as values)
     * @param values list of new values in row (must be same length as keys)
     */
    public UpdateCommand(
        final String columnFamily, final String keySpace,
        final ByteBuffer rowKey, final ByteBuffer[] keys,
        final ByteBuffer[] values
    ) {
        this.columnFamily = columnFamily;
        this.keySpace     = keySpace;
        this.rowKey       = rowKey;

        this.keys = new ByteBuffer[keys.length];
        this.values = new ByteBuffer[values.length];

        System.arraycopy(keys, 0, this.keys, 0, keys.length);
        System.arraycopy(values, 0, this.values, 0, values.length);
    }

    /**
     * Updates existing row in column family.
     *
     * @param model <code>IDataModel</code> implementation
     * @throws PythiaException if there is a problem with row updating
     */
    @Override
    public Object execute(final IDataModel model) throws PythiaException {

        if (keys.length != values.length) {
            throw new PythiaException(PythiaError.INVALID_ARGUMENS);
        }

        final IKeySpace space = DB.INSTANCE.getKeySpace(keySpace);

        // keyspace check
        if (space != null) {
            IColumnFamily table = space.getColumnFamily(columnFamily);

            // column family check
            if (table != null) {
                IRow row = table.getRow(rowKey);

                // row check
                if (row == null) {
                    throw new PythiaException(PythiaError.DATA_NOT_FOUND);
                }

                // add key:values pairs as new column list
                IColumn column;
                for (int i = 0; i < keys.length; ++i) {
                    column = row.getColumn(keys[i]);

                    // update existing column
                    if (column != null) {
                        row.updateColumn(keys[i], values[i]);
                    } else { // or add new
                        final IColumn newColumn = model.createColumn(keys[i]);
                        newColumn.setValue(values[i]);
                        row.addColumn(newColumn);
                    }
                }

            } else {
                throw new PythiaException(PythiaError.DATA_NOT_FOUND);
            }
        } else {
            throw new PythiaException(PythiaError.DATA_NOT_FOUND);
        }
        return null;
    }

    // test only
    public String getKeySpace() {
        return keySpace;
    }

    // test only
    public String getColumnFamily() {
        return columnFamily;
    }

    // test only
    public ByteBuffer getRowKey() {
        return ByteBuffer.wrap(rowKey.array().clone());
    }

    // test only
    public ByteBuffer[] getKeys() {
        ByteBuffer[] copy = new ByteBuffer[keys.length];
        System.arraycopy(keys, 0, copy, 0, keys.length);
        return copy;
    }

     // test only
    public ByteBuffer[] getValues() {
        ByteBuffer[] copy = new ByteBuffer[values.length];
        System.arraycopy(values, 0, copy, 0, values.length);
        return copy;
    }

}