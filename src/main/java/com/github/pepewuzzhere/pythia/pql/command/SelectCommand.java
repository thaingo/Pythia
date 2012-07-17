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
import com.github.pepewuzzhere.pythia.datamodel.IColumnFamily;
import com.github.pepewuzzhere.pythia.datamodel.IDataModel;
import com.github.pepewuzzhere.pythia.datamodel.IKeySpace;
import java.nio.ByteBuffer;

/**
 * Command selects rows from column family.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public class SelectCommand implements IDBCommand {

    /**
     * Name of selected column family
     */
    public final String columnFamily;

    /**
     * Name of used keyspace
     */
    public final String keySpace;

    /**
     * Value of selected row key
     */
    public final ByteBuffer rowKey;

    /**
     * Sets all parameters used to select rows from column family.
     *
     * @param columnFamily Name of column family
     * @param keySpace Used keyspace
     * @param rowKey Key of row
     */
    public SelectCommand(
            String columnFamily, String keySpace, ByteBuffer rowKey
    ) {
        this.columnFamily = columnFamily;
        this.keySpace     = keySpace;
        this.rowKey       = rowKey;
    }

    /**
     * Deletes existing row from column family.
     *
     * @param model <code>IDataModel</code> implementation
     * @return <code>IRow</code> - selected row
     * @throws PythiaException
     */
    @Override
    public Object execute(IDataModel model) throws PythiaException {

        IKeySpace space = DB.INSTANCE.getKeySpace(keySpace);

        // keyspace check
        if (space != null) {
            IColumnFamily table = space.getColumnFamily(columnFamily);

            // column family check
            if (table != null) {
                return table.getRow(rowKey);
            } else {
                throw new PythiaException(PythiaError.DATA_NOT_FOUND);
            }
        } else {
            throw new PythiaException(PythiaError.DATA_NOT_FOUND);
        }
    }
}