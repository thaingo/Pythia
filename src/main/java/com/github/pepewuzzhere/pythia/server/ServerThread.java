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

package com.github.pepewuzzhere.pythia.server;

import com.github.pepewuzzhere.pythia.Context;
import com.github.pepewuzzhere.pythia.PythiaException;
import com.github.pepewuzzhere.pythia.datamodel.IDataModel;
import com.github.pepewuzzhere.pythia.pql.Compiler;
import com.github.pepewuzzhere.pythia.pql.FSALexer;
import com.github.pepewuzzhere.pythia.pql.LL1Grammar;
import com.github.pepewuzzhere.pythia.pql.command.IDBCommand;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Server thread to handle user of Pythia data base.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
class ServerThread implements Runnable {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintStream out;
    private final Context ctx;
    private final IDataModel model;

    /**
     * Creates server thread.
     *
     * Needs socket from server and used data model.
     *
     * @param socket socket instance
     * @param model data model to use
     * @throws IOException if something is wrong with i/o
     * @throws IllegalArgumentException if socket or, model are null
     */
    ServerThread(final Socket socket, final IDataModel model)
            throws IOException
    {
        if (socket == null || model == null) {
            throw new IllegalArgumentException(
                    "Socket and data model are required");
        }
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintStream(socket.getOutputStream());
        ctx = new Context();
        this.model = model;
    }

    @Override
    public void run() {
        try {
            String command = "";
            final Compiler compiler =
                    new Compiler(new FSALexer(), new LL1Grammar());

            while (command != null && !socket.isClosed()) {
                command = in.readLine();
                try {

                    System.out.println("Command: " + command);
                    if (command != null) {
                        final IDBCommand cmd = compiler.compile(command, ctx);
                        Object response = cmd.execute(model);
                        if (response != null) {
                            out.println(response.toString());
                        }

                        out.println("OK");
                    }
                } catch (PythiaException e) {
                    out.println(e.getMessage());
                }

                out.flush();
            }
        } catch (IOException | PythiaException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}