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
package com.github.pepewuzzhere.pythia;

/**
 * Exception class used in Pythia.
 *
 * Could be used with provided error codes {@link PythiaError}.
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public class PythiaException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor used to provide custom message.
     *
     * @param msg message of exception
     */
    public PythiaException(final String msg) {
        super(msg);
    }

    /**
     * Constructor used to provide predefined error code.
     *
     * @param code error code
     */
    public PythiaException(final PythiaError code) {
        super(code.toString());
    }

}