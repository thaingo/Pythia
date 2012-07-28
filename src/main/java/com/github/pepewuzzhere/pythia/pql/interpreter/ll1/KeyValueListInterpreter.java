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
import com.github.pepewuzzhere.pythia.pql.interpreter.IInterpreter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Interpreter of node with {@link LL1Grammar#KEY_VALUES_LIST} symbol.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
class KeyValueListInterpreter implements IInterpreter {

    @Override
    public Object interpret(
            final ParseTree node, final Context ctx) throws PythiaException
    {
        final List<String> list = new ArrayList<>();
        for (ParseTree n : node.getChildrens()) {
            if (n.getSymbol() == Terminal.VAR
               || n.getSymbol() == Terminal.KEY_KEY
            ) {
                list.add(n.getToken().getValue());
            }
            if (n.getSymbol() == LL1Grammar.NonTerminal.KEY_VALUES_LIST_PRIM) {
                KeyValueListInterpreter intr = new KeyValueListInterpreter();
                list.addAll(
                    Arrays.asList(
                        (String[])intr.interpret(n, ctx)
                    )
                );
            }
        }

        return list.toArray(new String[0]);
    }

}
