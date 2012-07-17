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
import com.github.pepewuzzhere.pythia.storage.IStorage;
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
public class ServerThread implements Runnable {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintStream out;
    private final Context ctx;
    private final IDataModel model;
    private final IStorage storage;

    /**
     * Creates server thread. Needs socket from server and used data model.
     *
     * @param socket Socket instance
     * @param model Used data model
     * @param storage Storage used in this instance of pythia
     * @throws IOException
     */
    public ServerThread(Socket socket, IDataModel model, IStorage storage)
            throws IOException
    {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintStream(socket.getOutputStream());
        ctx = new Context();
        this.model = model;
        this.storage = storage;
    }

    @Override
    public void run() {
        try {
            String command;
            Compiler compiler = new Compiler(new FSALexer(), new LL1Grammar());
            do {
                try {
                    command = in.readLine();
                    System.out.println("Command: " + command);
                    if (command != null) {
                        IDBCommand cmd = compiler.compile(command, ctx);
                        Object response = cmd.execute(model);
                        if (response != null) {
                            out.println(response.toString());
                        }
                        storage.write(Server.ROOT);
                        out.println("OK");
                    }
                } catch (PythiaException e) {
                    out.println(e.getMessage());
                }
                out.flush();
            } while (!socket.isClosed());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}