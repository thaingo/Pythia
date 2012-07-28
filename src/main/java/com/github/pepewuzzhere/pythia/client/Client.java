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

package com.github.pepewuzzhere.pythia.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Sample client for Pythia data base.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public class Client {

    private final Receiver receiver;
    private final PrintStream out;

    /**
     * Creates client application for pythia database.
     *
     * @param socket socket instance - connection to server
     * @throws IOException if something is wrong with {@link PrintStream}
     */
    public Client(final Socket socket) throws IOException {
        out = new PrintStream(socket.getOutputStream());

        this.receiver = new Receiver(socket);
    }

    /**
     * Creates receiver and starts receiving
     */
    public void startReceiving() {
        receiver.start();

        // for testing purposes - force one run method invocation
        receiver.run();
    }

    /**
     * Sends message to server.
     *
     * @param cmd the message that is send to server
     * @throws IOException if something is wrong with
     *                     client - server communication
     */
    public void send(final String cmd) throws IOException {
        out.println(cmd);
    }

    /**
     * Client start point
     *
     * @param args arguments from command line
     * @throws Exception if starting client is impossible
     */
    public static void main(String[] args) throws Exception {
        String cmd = "";

        final Client client = new Client(new Socket("localhost", 4444));
        client.startReceiving();

        final BufferedReader read =
                new BufferedReader(new InputStreamReader(System.in));

        while (!cmd.equals("quit")) {
            cmd = read.readLine();
            client.send(cmd);
        }

    }

}