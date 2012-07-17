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

import com.github.pepewuzzhere.pythia.PythiaException;
import com.github.pepewuzzhere.pythia.datamodel.IDataModel;
import com.github.pepewuzzhere.pythia.datamodel.hashmap.HashMapDataModel;
import com.github.pepewuzzhere.pythia.storage.IStorage;
import com.github.pepewuzzhere.pythia.storage.SerializationStorage;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server of Pythia DataBase.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public class Server {

    /**
     * Count of maximum connections to server.
     */
    public final static int MAX_CONNECTIONS = 100;

    /**
     * Root of pythia data storage
     */
    public final static String ROOT =
            System.getProperty("java.io.tmpdir") + "pythia";

    private final ServerSocket server;
    private final ExecutorService service;
    private final IDataModel model;
    private final IStorage storage;

    /**
     * Create server listening to chosen port.
     *
     * @param socket Server socket
     * @param model Pythia data model implementation
     * @param storage Storage used in this instance of pythia
     * @throws IOException
     */
    public Server(ServerSocket socket, IDataModel model, IStorage storage)
            throws IOException
    {
        server = socket;
        service = Executors.newFixedThreadPool(Server.MAX_CONNECTIONS);
        this.model = model;
        this.storage = storage;
    }

    /**
     * Run server
     *
     * @throws IOException
     * @throws PythiaException
     */
    public void go() throws IOException, PythiaException {
        storage.read(ROOT, model);
        try {
            do {
                service.execute(
                    new ServerThread(
                        server.accept(), model, storage
                    )
                );
            } while (!server.isClosed());
        } catch (Exception e) {
            service.shutdownNow();
            throw new RuntimeException(e);
        }
    }

    /**
     * Entry point of server application
     *
     * @param args Command line arguments
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Server pythia = new Server(
            new ServerSocket(4444),
            new HashMapDataModel(),
            new SerializationStorage()
        );
        pythia.go();
    }

}