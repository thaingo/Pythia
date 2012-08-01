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
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton represents Pythia database instance.
 *
 * Consists of keyspaces map and it is root of data model tree.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public enum DB {

    /**
     * Instance of pythia database - entry to root of pythia data model.
     */
    INSTANCE;

    /*
     * Keyspaces list
     */
    private final Map<String, IKeySpace> keySpaces = new HashMap<>();

    /**
     * Adds new keyspace with unique name or throws exception.
     *
     * @param keySpace keyspace to add
     * @throws PythiaException if keyspace already exists
     * @throws IllegalArgumentException if keyspace object is null
     */
    public void addKeySpace(final IKeySpace keySpace)
            throws PythiaException
    {
        if (keySpace == null) {
            throw new IllegalArgumentException("KeySpace object is required");
        }
        if (keySpaces.containsKey(keySpace.getName())) {
            throw new PythiaException(PythiaError.KEY_ALREADY_EXISTS);
        } else {
            keySpaces.put(keySpace.getName(), keySpace);
        }
    }

    /**
     * Gets keyspace by name.
     *
     * @param name name of keyspace
     * @return keyspace with provided name or null
     */
    public IKeySpace getKeySpace(final String name) {
        return keySpaces.get(name);
    }

    /**
     * Drop keyspace with providen name.
     *
     * @param name name of keyspace
     * @throws PythiaException if keyspace doesn't exists.
     */
    public void dropKeySpace(final String name) throws PythiaException {
        if (keySpaces.containsKey(name)) {
            keySpaces.remove(name);
        } else {
            throw new PythiaException(PythiaError.DATA_NOT_FOUND);
        }
    }

    /**
     * Returns list of actual keyspaces.
     *
     * @return list of actual keyspaces.
     */
    public Map<String, IKeySpace> getKeySpaces() {
        return new HashMap<>(keySpaces);
    }

    /**
     * Clears all cached data - use only for testing purposes!
     */
    public void dropDB() {
        keySpaces.clear();
    }

}