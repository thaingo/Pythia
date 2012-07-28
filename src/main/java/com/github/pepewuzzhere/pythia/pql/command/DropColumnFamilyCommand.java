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
import com.github.pepewuzzhere.pythia.datamodel.IDataModel;
import com.github.pepewuzzhere.pythia.datamodel.IKeySpace;

/**
 * Command drops column family from provided keyspace.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public class DropColumnFamilyCommand implements IDBCommand {

    /*
     * Name of dropped column family
     */
    private final String name;

    /**
     * Keyspace of dropped column family
     */
    private final String keySpace;

    /**
     * Sets all parameters used for dropping column family.
     *
     * @param name name of column family to drop
     * @param keySpace name of used keyspace
     */
    public DropColumnFamilyCommand(final String name, final String keySpace) {
        this.name     = name;
        this.keySpace = keySpace;
    }

    /**
     * Drops column family in provided keyspace
     *
     * @param model <code>IDataModel</code> implementation
     * @throws PythiaException if there is a problem with dropping column family
     */
    @Override
    public Object execute(final IDataModel model) throws PythiaException {
        final IKeySpace space = DB.INSTANCE.getKeySpace(keySpace);

        if (space != null) {
            space.dropColumnFamily(name);
        } else {
            throw new PythiaException(PythiaError.DATA_NOT_FOUND);
        }
        return null;
    }

    // test only
    public String getName() {
        return name;
    }

    // test only
    public String getKeySpace() {
        return keySpace;
    }

}
