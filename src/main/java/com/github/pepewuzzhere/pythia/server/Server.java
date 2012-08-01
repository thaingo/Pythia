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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    public static final int MAX_CONNECTIONS = 100;

    /**
     * Root of pythia data storage
     */
    public static final String ROOT =
            System.getProperty("java.io.tmpdir") + "pythia";

    private final ServerSocket server;
    private final ExecutorService service;
    private final IDataModel model;
    private final IStorage storage;
    private final ScheduledExecutorService scheduler;

    private class Scheduler implements Runnable {

        @Override
        public void run() {
            try {
                Server.this.storage.write(Server.ROOT);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

    }

    /**
     * Create server listening to chosen port.
     *
     * @param socket server socket
     * @param model pythia data model implementation
     * @param storage storage used in this instance of pythia
     * @throws IllegalArgumentException if socket, model or storage are null
     */
    public Server(ServerSocket socket, IDataModel model, IStorage storage) {
        if (socket == null || model == null || storage == null) {
            throw new IllegalArgumentException(
                    "Socket, data model and storage are required");
        }
        server = socket;
        service = Executors.newFixedThreadPool(Server.MAX_CONNECTIONS);
        scheduler = Executors.newScheduledThreadPool(1);
        this.model = model;
        this.storage = storage;
    }

    /**
     * Run server listener.
     *
     * @throws IOException if something is wrong with {@link IStorage#read} or
     *                     {@link ServerSocket#accept}
     * @throws PythiaException if something is wrong with {@link IStorage#read}
     */
    public void go() throws IOException, PythiaException {
        storage.read(ROOT, model);
        scheduler.scheduleWithFixedDelay(
                new Scheduler(), 0, 30, TimeUnit.SECONDS);
        try {
            do {
                service.execute(new ServerThread(server.accept(), model));
            } while (!server.isClosed());
        } catch (Exception e) {
            service.shutdownNow();
            scheduler.shutdown();
            throw new RuntimeException(e);
        }
    }

    /**
     * Entry point of server application
     *
     * @param args command line arguments
     * @throws Exception if something is wrong with server
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Pythia - Start");
        final Server pythia = new Server(
            new ServerSocket(4444),
            new HashMapDataModel(),
            new SerializationStorage()
        );
        pythia.go();
    }

}