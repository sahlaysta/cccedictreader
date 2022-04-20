/*
 * MIT License
 * 
 * Copyright (c) 2022 sahlaysta
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package sahlaysta.cccedict;

/**
 * Exception used by {@link CCCEDICTReader}.
 * 
 * @author sahlaysta
 * */
public class CCCEDICTReaderException extends Exception {
	
	private static final long serialVersionUID = 2081346037373792008L;

	/** Create an exception without a message.
	 * The cause remains uninitialized.
	 * @see #initCause(Throwable) */
	public CCCEDICTReaderException() {}
	
	/** Create an exception with a message.
	 * The cause remains uninitialized.
	 * @param s the message
	 * @see #initCause(Throwable) */
	public CCCEDICTReaderException(String s) {
		super(s);
	}
	
	/** Create an exception with a message and a cause.
	 * @param s the message string
	 * @param cause the cause of this error */
	public CCCEDICTReaderException(String s, Throwable cause) {
		super(s, cause);
	}
	
	/** Create an exception with a given cause, and a message
	 * of <code>cause == null ? null : cause.toString()</code>.
	 * @param cause the cause of this exception */
	public CCCEDICTReaderException(Throwable cause) {
		super(cause);
	}
}
