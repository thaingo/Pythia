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
 * Column is the smallest increment of data in Pythia.
 *
 * It's a tuple containinga key and a value. Key should be unique in
 * {@link IRow}. Column must have a key but value can be set dynamically.
 * Every data in Pythia are stored as {@link java.nio.ByteBuffer}.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public interface IColumn {

    /**
     * Gets a key of key:value pair.
     *
     * Key is unique and final, should be set in constructor.
     *
     * @return key stored in {@link java.nio.ByteBuffer}.
     */
    ByteBuffer getKey();

    /**
     * Gets a value of key:value pair.
     *
     * Value is optional and is connected with key.

     * @return Value stored in {@link java.nio.ByteBuffer}.
     */
    ByteBuffer getValue();

    /**
     * Sets new value and returns old.
     *
     * @param value new value connected to key in this column.
     * @return old value of this column.
     */
    ByteBuffer setValue(final ByteBuffer value);
}