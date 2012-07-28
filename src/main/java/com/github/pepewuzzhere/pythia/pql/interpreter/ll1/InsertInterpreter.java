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
import com.github.pepewuzzhere.pythia.PythiaException;
import com.github.pepewuzzhere.pythia.pql.LL1Grammar;
import com.github.pepewuzzhere.pythia.pql.ParseTree;
import com.github.pepewuzzhere.pythia.pql.Terminal;
import com.github.pepewuzzhere.pythia.pql.command.InsertCommand;
import com.github.pepewuzzhere.pythia.pql.interpreter.IInterpreter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Interpreter of node with {@link LL1Grammar#STMT_INSERT} symbol.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
class InsertInterpreter implements IInterpreter {

    @Override
    public Object interpret(
            final ParseTree node, final Context ctx) throws PythiaException
    {
        String columnFamily = "";
        String[] list = null;
        for (ParseTree n : node.getChildrens()) {
            if (n.getSymbol() == Terminal.VAR) {
                columnFamily = n.getToken().getValue();
            }
            if (n.getSymbol() == LL1Grammar.NonTerminal.KEY_VALUES_LIST) {
                KeyValueListInterpreter intr = new KeyValueListInterpreter();
                list = (String[])intr.interpret(n, ctx);
            }
        }
        final List<ByteBuffer> keys = new ArrayList<>();
        final List<ByteBuffer> values = new ArrayList<>();
        for (int i = 2; i < list.length; i += 2) {
            keys.add(ByteBuffer.wrap(list[i].getBytes()));
            values.add(ByteBuffer.wrap(list[i + 1].getBytes()));
        }

        return new InsertCommand(
            columnFamily,
            ctx.getActualKeySpace() != null
                ? ctx.getActualKeySpace().getName()
                : null,
            ByteBuffer.wrap(list[1].getBytes()),
            keys.toArray(new ByteBuffer[0]),
            values.toArray(new ByteBuffer[0])
        );
    }

}
