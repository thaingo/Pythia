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

package com.github.pepewuzzhere.pythia.pql.interpreter.ll1;

import com.github.pepewuzzhere.pythia.Context;
import com.github.pepewuzzhere.pythia.PythiaError;
import com.github.pepewuzzhere.pythia.PythiaException;
import com.github.pepewuzzhere.pythia.pql.IGrammar;
import com.github.pepewuzzhere.pythia.pql.LL1Grammar;
import com.github.pepewuzzhere.pythia.pql.ParseTree;
import com.github.pepewuzzhere.pythia.pql.interpreter.IInterpreter;

/**
 * Interpreter of node with {@link LL1Grammar#STMT_START} symbol.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public class StartStmtInterpreter implements IInterpreter {

    @Override
    public Object interpret(IGrammar grammar, ParseTree node, Context ctx)
            throws PythiaException
    {
        LL1Grammar g = new LL1Grammar();
        ParseTree n = node.getChildrens().get(0);

        int code = g.getCode(n.getSymbol());
        IInterpreter intr = null;
        if (code == g.STMT_INSERT) {
            intr = new InsertInterpreter();
        }
        if (code == g.STMT_UPDATE) {
            intr = new UpdateInterpreter();
        }
        if (code == g.STMT_CREATE_COLUMNFAMILY) {
            intr = new CreateColumnFamilyInterpreter();
        }
        if (code == g.STMT_DROP_COLUMNFAMILY) {
            intr = new DropColumnFamilyInterpreter();
        }
        if (code == g.STMT_DROP_KEYSPACE) {
            intr = new DropKeySpaceInterpreter();
        }
        if (code == g.STMT_CREATE_KEYSPACE) {
            intr = new CreateKeySpaceInterpreter();
        }
        if (code == g.STMT_SELECT) {
            intr = new SelectInterpreter();
        }
        if (code == g.STMT_DELETE) {
            intr = new DeleteInterpreter();
        }
        if (code == g.STMT_USE_KEYSPACE) {
            intr = new UseKeySpaceInterpreter();
        }
        if (intr != null) {
            return intr.interpret(grammar, n, ctx);
        } else {
            throw new PythiaException(PythiaError.INTERPRETER_NOT_FOUND);
        }
    }

}