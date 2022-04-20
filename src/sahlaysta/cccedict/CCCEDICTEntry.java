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

import java.util.List;

/**
 * CC-CEDICT dictionary entry.
 * 
 * @author sahlaysta
 * */
public class CCCEDICTEntry {
	
	
	//constructor
	
	/** Initializes a new instance of the class.
	 * @param traditional the Traditional Chinese variant
	 * @param simplified the Simplified Chinese variant
	 * @param pronunciation the pronunciation in pinyin
	 * @param definitions the English definitions */
	public CCCEDICTEntry(
			String traditional,
			String simplified,
			String pronunciation,
			List<String> definitions) {
		this.traditional = traditional;
		this.simplified = simplified;
		this.pronunciation = pronunciation;
		this.definitions = definitions;
	}
	
	/** Returns a cedict_ts.u8 string representation
	 * of this entry.
	 * @return a cedict_ts.u8 string representation
	 * of this entry */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(traditional);
		sb.append(' ');
		sb.append(simplified);
		sb.append(" [");
		sb.append(pronunciation);
		sb.append("] /");
		for (String definition: definitions) {
			sb.append(definition);
			sb.append('/');
		}
		return sb.toString();
	}
	
	
	//public operations
	
	/** Returns the Traditional Chinese variant.
	 * @return the Traditional Chinese varaint */
	public String getTraditional() {
		return traditional;
	}
	
	/** Returns the Simplified Chinese variant.
	 * @return the Simplified Chinese varaint */
	public String getSimplified() {
		return simplified;
	}
	
	/** Returns the pronunciation in pinyin.
	 * @return the pronunciation in pinyin */
	public String getPronunciation() {
		return pronunciation;
	}
	
	/** Returns the English definitions.
	 * @return the English definitions */
	public List<String> getDefinitions() {
		return definitions;
	}
	

	/** The Traditional Chinese variant. */
	private final String traditional;
	
	/** The Simplified Chinese variant. */
	private final String simplified;
	
	/** The pronunciation in pinyin. */
	private final String pronunciation;
	
	/** The English definitions. */
	private final List<String> definitions;
}
