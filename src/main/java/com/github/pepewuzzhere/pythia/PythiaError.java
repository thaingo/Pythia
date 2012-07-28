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
 * Standard error codes for PythiaException
 *
 * @author Piotr 'pepe' Picheta <piotr.pepe.picheta@gmail.com>
 * @version %I%, %G%
 * @since 1.0
 */
public enum PythiaError {

    /**
     * Error - key should be unique
     */
    KEY_ALREADY_EXISTS {

        @Override
        public String toString() {
            return "Provided key already exists.";
        }
    },

    /**
     * Error - interpreter for used grammar doesn't existis
     */
    INTERPRETER_NOT_FOUND {

        @Override
        public String toString() {
            return "Couldn't find interpreter for provided grammar.";
        }
    },

    /**
     * Error - invalid arguments
     */
    INVALID_ARGUMENS {

        @Override
        public String toString() {
            return "Invalid arguments.";
        }
    },

    /**
     * Error - parser for used grammar doesn't existis
     */
    PARSER_NOT_FOUND {

        @Override
        public String toString() {
            return "Couldn't find parser for provided grammar.";
        }
    },

    /**
     * Error - unknown symbol in PQL command
     */
    SYMBOL_NOT_FOUND {

        @Override
        public String toString() {
            return "Unknown symbol in PQL command.";
        }
    },

    /**
     * Error - syntax error i PQL command
     */
    SYNTAX_ERROR {

        @Override
        public String toString() {
            return "Syntax error.";
        }
    },

    /**
     * Error - searched data doesn't exists
     */
    DATA_NOT_FOUND {

        @Override
        public String toString() {
            return "Data for given key was not found.";
        }
    };

}