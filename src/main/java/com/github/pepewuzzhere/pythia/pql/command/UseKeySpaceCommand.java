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

import com.github.pepewuzzhere.pythia.Context;
import com.github.pepewuzzhere.pythia.PythiaException;
import com.github.pepewuzzhere.pythia.datamodel.IDataModel;

/**
 * Command for setting actual keyspace.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public class UseKeySpaceCommand implements IDBCommand {

    /*
     * Name of keyspace to use
     */
    private final String name;

    /*
     * Actual user context
     */
    private final Context ctx;

    /**
     * Sets all parameters used to using keyspace.
     *
     * @param name name of keyspace
     * @param ctx context of execution
     */
    public UseKeySpaceCommand(final String name, final Context ctx) {
        this.name = name;
        this.ctx = ctx;
    }

    /**
     * Creates keyspace from this command parameters.
     *
     * @param model <code>IDataModel</code> implementation
     * @return actual keyspace object
     * @throws PythiaException if there is a problem with using new keyspace
     */
    @Override
    public Object execute(final IDataModel model) throws PythiaException {
        ctx.setActualKeySpace(name);
        return null;
    }

    // test only
    public String getName() {
        return name;
    }

    // test only
    public boolean hasContext() {
        return ctx != null;
    }

}
