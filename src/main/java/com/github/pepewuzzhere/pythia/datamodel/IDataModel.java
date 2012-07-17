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

import java.nio.ByteBuffer;

/**
 * DataModel is used to create concrete implementatons of data model objects.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public interface IDataModel {

    /**
     * Creates concrete implementation of {@link IKeySpace}
     *
     * @param name Name of keyspace
     * @return Concrete implementation of <code>IKeySpace</code>
     */
    IKeySpace createKeySpace(String name);

    /**
     * Creates concrete implementation of {@link IColumnFamily}
     *
     * @return Concrete implementation of <code>IColumnFamily</code>
     */
    IColumnFamily createColumnFamily();

    /**
     * Creates concrete implementation of {@link IColumn}
     *
     * @param key Key of column
     * @return Concrete implementation of <code>IColumn</code>
     */
    IColumn createColumn(ByteBuffer key);

    /**
     * Creates concrete implementation of {@link IRow}
     * @param key Key of row
     * @return Concrete implementation of <code>IRow</code>
     */
    IRow createRow(ByteBuffer key);
}
