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
import com.github.pepewuzzhere.pythia.pql.ISymbol;
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
class StartStmtInterpreter implements IInterpreter {

    @Override
    public Object interpret(
            final ParseTree node, final Context ctx) throws PythiaException
    {
        ParseTree n = node.getChildrens().get(0);

        IInterpreter intr = null;
        ISymbol code = n.getSymbol();
        if (code == LL1Grammar.NonTerminal.STMT_INSERT) {
            intr = new InsertInterpreter();
        }
        if (code == LL1Grammar.NonTerminal.STMT_UPDATE) {
            intr = new UpdateInterpreter();
        }
        if (code == LL1Grammar.NonTerminal.STMT_CREATE_COLUMNFAMILY) {
            intr = new CreateColumnFamilyInterpreter();
        }
        if (code == LL1Grammar.NonTerminal.STMT_DROP_COLUMNFAMILY) {
            intr = new DropColumnFamilyInterpreter();
        }
        if (code == LL1Grammar.NonTerminal.STMT_DROP_KEYSPACE) {
            intr = new DropKeySpaceInterpreter();
        }
        if (code == LL1Grammar.NonTerminal.STMT_CREATE_KEYSPACE) {
            intr = new CreateKeySpaceInterpreter();
        }
        if (code == LL1Grammar.NonTerminal.STMT_SELECT) {
            intr = new SelectInterpreter();
        }
        if (code == LL1Grammar.NonTerminal.STMT_DELETE) {
            intr = new DeleteInterpreter();
        }
        if (code == LL1Grammar.NonTerminal.STMT_USE_KEYSPACE) {
            intr = new UseKeySpaceInterpreter();
        }
        if (intr != null) {
            return intr.interpret(n, ctx);
        } else {
            throw new PythiaException(PythiaError.INTERPRETER_NOT_FOUND);
        }
    }

}