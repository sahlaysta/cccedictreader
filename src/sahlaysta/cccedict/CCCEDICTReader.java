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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Processes a CC-CEDICT cedict_ts.u8 source.
 * 
 * @author sahlaysta
 * */
public class CCCEDICTReader {
	
	
	//constructor
	
	/** Initializes a new instance of the class.
	 * @param inputStream the CC-CEDICT cedict_ts.u8 data
	 * @throws CCCEDICTReaderException if a processing error occurs
	 * @throws NullPointerException if {@code inputStream} is {@code null} */
	public CCCEDICTReader(InputStream inputStream)
			throws CCCEDICTReaderException {
		Objects.requireNonNull(inputStream);
		init(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
	}
	
	/** Initializes a new instance of the class.
	 * @param reader the CC-CEDICT cedict_ts.u8 data
	 * @throws CCCEDICTReaderException if a processing error occurs
	 * @throws NullPointerException if {@code reader} is {@code null} */
	public CCCEDICTReader(Reader reader)
			throws CCCEDICTReaderException {
		Objects.requireNonNull(reader);
		init(reader);
	}
	
	
	//public operations
	
	/** Returns the cedict_ts.u8 input source.
	 * @return the cedict_ts.u8 input source */
	public Reader getReader() {
		return r;
	}
	
	/** Returns the cedict_ts.u8 header description.
	 * @return the cedict_ts.u8 header description */
	public String getHeaderDescription() {
		return headerDesc;
	}
	
	/** Returns the cedict_ts.u8 header map.
	 * @return the cedict_ts.u8 header map */
	public Map<String, String> getHeaderMap() {
		return headerMap;
	}
	
	/** Returns the next entry, or {@code null} if none.
	 * @return the next entry, or {@code null} if none
	 * @throws CCCEDICTReaderException if a processing error occurs */
	public CCCEDICTEntry nextEntry() throws CCCEDICTReaderException {
		if (ended)
			return null;
		
		if (!peeked && !nextChar(NORMAL))
			return null;
		
		if (c == '\n')
			if (!nextChar(NORMAL))
				return null;

		StringBuilder sb = new StringBuilder();
		String trad, smpl, pronunciation;
		List<String> defs = new ArrayList<>();
		
		//traditional
		sb.append(c);
		while (true) {
			nextChar(LINE);
			if (c == ' ')
				break;
			sb.append(c);
		}
		nextChar(LINE);
		trad = sb.toString();
		
		//simplified
		sb.setLength(0);
		sb.append(c);
		while (true) {
			nextChar(LINE);
			if (c == ' ')
				break;
			sb.append(c);
		}
		nextChar(LINE);
		smpl = sb.toString();
		
		//pronunciation
		if (c != '[') {
			throw new CCCEDICTReaderException(
				"Expected '[' char at " + locationMsg());
		}
		nextChar(LINE);
		sb.setLength(0);
		sb.append(c);
		while (true) {
			nextChar(LINE);
			if (c == ']')
				break;
			sb.append(c);
		}
		nextChar(LINE);
		pronunciation = sb.toString();
		
		//definitions
		nextChar(LINE);
		if (c != '/') {
			throw new CCCEDICTReaderException(
				"Expected '/' char at " + locationMsg());
		}
		while (true) {
			nextChar(NORMAL);
			if (ended || c == '\r' || c == '\n')
				break;
			sb.setLength(0);
			sb.append(c);
			while (true) {
				nextChar(LINE);
				if (c == '/') {
					defs.add(sb.toString());
					break;
				}
				sb.append(c);
			}
		}
		defs = Collections.unmodifiableList(
			Arrays.asList(
				defs.toArray(
					new String[defs.size()])));
		
		peeked = false;
		
		//entry
		return new CCCEDICTEntry(
			trad, smpl, pronunciation, defs);
	}
	
	
	//initializations
	
	void init(Reader reader) throws CCCEDICTReaderException {
		r = reader;
		
		//read header
		StringBuilder sb = new StringBuilder();
		while (true) {
			// '#'
			nextChar();
			if (c != '#')
				break;
			
			// ' '
			nextChar();
			if (c != ' ' && c != '!') {
				throw new CCCEDICTReaderException(
					"Bad header, expected space char"
					+ " or '!' char after '#' char.");
			}
			
			//leading space
			if (c == ' ')
				nextChar();
			
			//read line
			while (true) {
				sb.append(c);
				nextChar();
				if (c == '\n') {
					sb.append('\n');
					break;
				}
			}
		}
		peeked = true;
		
		//parse
		String mapIndSeq = "\n!";
		int mapInd = sb.indexOf(mapIndSeq);
		//header description
		headerDesc = sb.substring(0, mapInd).trim();
		//header map
		Map<String, String> map = new LinkedHashMap<>();
		int i = mapInd + mapIndSeq.length() - 1;
		while (true) {
			// ' '
			char c2 = sb.charAt(++i);
			if (c2 != ' ') {
				throw new CCCEDICTReaderException(
					"Bad header, expected space char"
					+ " after '!' char.");
			}
			i++;
			
			//read key
			String errMsg = "Bad header map, missing equal sign";
			int equalSignInd = sb.indexOf("=", i);
			if (equalSignInd == -1)
				throw new CCCEDICTReaderException(errMsg);
			String key = sb.substring(i, equalSignInd);
			if (key.indexOf('\n') != -1)
				throw new CCCEDICTReaderException(errMsg);
			
			//read value
			int carRetInd = sb.indexOf("\r", equalSignInd);
			int newLineInd = sb.indexOf("\n", equalSignInd);
			int valueEndInd =
				carRetInd > newLineInd ? carRetInd : newLineInd - 1;
			String value = sb.substring(equalSignInd + 1, valueEndInd);
			
			//add to map
			map.put(key, value);
			
			//index
			i = sb.indexOf("!", valueEndInd);
			if (i == -1)
				break;
		}
		
		headerMap = Collections.unmodifiableMap(map);
	}
	
	//advance reader to next char
	static final int NORMAL = 0x1;
	static final int READ_FULLY = 0x2;
	static final int LINE = 0x3;
	boolean nextChar() throws CCCEDICTReaderException {
		return nextChar(READ_FULLY);
	}
	boolean nextChar(int mode) throws CCCEDICTReaderException {
		//read char
		int nextC;
		try {
			nextC = r.read();
			if (nextC == '\n')
				incrementLine();
			else
				incrementCol();
		} catch (Exception e) {
			throw new CCCEDICTReaderException(e);
		}
		
		//set char
		if (nextC == -1) {
			ended = true;
			if (mode == READ_FULLY || mode == LINE) {
				throw new CCCEDICTReaderException(
					"Unexpected end of input.");
			}
		}
		else if (nextC == '\n' && mode == LINE) {
			throw new CCCEDICTReaderException(
				"Unexpected line break at " + locationMsg());
		}
		else {
			c = (char)nextC;
		}
		
		return !ended;
	}
	
	//exception
	String locationMsg() {
		boolean lineMaxed = line == Long.MAX_VALUE;
		boolean colMaxed = col == Long.MAX_VALUE;
		long lineVal = line;
		long colVal = col;
		if (colVal == 0) lineVal--;//line break
		return
			"line " +
				(lineMaxed ? "?" : lineVal) +
				
			(colVal != 0 ?
				(", column " +
					(colMaxed ? "?" : colVal))
				: "") +
			
			".";
	}
	
	//increment line and col
	void incrementLine() {
		if (line != Long.MAX_VALUE)
			line++;
		col = 0;
	}
	void incrementCol() {
		if (col != Long.MAX_VALUE)
			col++;
	}
	
	//position in input
	long line = 1, col = 0;
	
	//input source
	Reader r;
	
	//current char
	char c;
	
	//end of input
	boolean ended = false;
	
	//true if peeked into the next char of the next line
	boolean peeked = false;
	
	//cedict_ts.u8 header description
	String headerDesc;
	
	//cedict_ts.u8 header description map
	Map<String, String> headerMap;
}
