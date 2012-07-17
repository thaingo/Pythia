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

package com.github.pepewuzzhere.pythia;

import com.github.pepewuzzhere.pythia.datamodel.IKeySpace;

/**
 * User context - storage of individual user data.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public class Context {

    private IKeySpace actualKeySpace = null;

    /**
     * Sets actual keyspace whitch is used by other commands.
     *
     * @param name Name of actual keyspace
     * @return Object that represents actual keyspace.
     * @throws PythiaException 
     */
    public IKeySpace setActualKeySpace(String name) throws PythiaException {
        IKeySpace keySpace = DB.INSTANCE.getKeySpace(name);
        if (keySpace != null) {
            actualKeySpace = keySpace;
            return keySpace;
        } else {
            throw new PythiaException(PythiaError.DATA_NOT_FOUND);
        }
    }

    /**
     * Gets actual keyspace.
     *
     * @return Keyspace
     */
    public IKeySpace getActualKeySpace() {
        return actualKeySpace;
    }
}