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

package com.github.pepewuzzhere.pythia.pql;

import com.github.pepewuzzhere.pythia.Context;
import com.github.pepewuzzhere.pythia.PythiaException;
import com.github.pepewuzzhere.pythia.pql.command.IDBCommand;
import com.github.pepewuzzhere.pythia.pql.interpreter.IInterpreter;
import com.github.pepewuzzhere.pythia.pql.interpreter.InterpreterFactory;

/**
 * Compiler of PQL language.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public class Compiler {

    private final ILexer lexer;
    private final IParser parser;
    private final IGrammar grammar;

    /**
     * Create compiler for PQL language.
     *
     * @param lexer lexer implementation to use
     * @param grammar grammar of PQL to use
     * @throws PythiaException if parser for pql couldn't be created
     * @throws IllegalArgumentException if lexer or grammar are null
     */
    public Compiler(final ILexer lexer, final IGrammar grammar)
            throws PythiaException
    {
        if (lexer == null || grammar == null) {
            throw new IllegalArgumentException(
                    "Lexer and grammar are required");
        }
        this.lexer  = lexer;
        this.grammar = grammar;
        this.parser = ParserFactory.factory(grammar);
    }

    /**
     * Compile PQL code and return command to execute.
     *
     * @param command source to compile
     * @param ctx context of execution
     * @return command to execute
     * @throws PythiaException if there are some compilation error
     */
    public IDBCommand compile(final String command, final Context ctx)
            throws PythiaException
    {
        // tokenize - parse - interpret
        final ParseTree node = parser.parse(
                lexer.tokenize(command, new TableDrivenTokenIterator()));
        final IInterpreter interpreter =
                InterpreterFactory.factory(grammar, node);
        return (IDBCommand)interpreter.interpret(node, ctx);
    }
}