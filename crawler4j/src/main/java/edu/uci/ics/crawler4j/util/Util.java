/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.uci.ics.crawler4j.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.WebCrawler;

/**
 * @author Yasser Ganjisaffar
 */
public class Util {
	
    protected static final Logger logger = LoggerFactory.getLogger(Util.class);

    public static byte[] long2ByteArray(long l) {
        byte[] array = new byte[8];
        int i;
        int shift;
        for (i = 0, shift = 56; i < 8; i++, shift -= 8) {
            array[i] = (byte) (0xFF & (l >> shift));
        }
        return array;
    }

    public static byte[] int2ByteArray(int value) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (3 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }

    public static void putIntInByteArray(int value, byte[] buf, int offset) {
        for (int i = 0; i < 4; i++) {
            int valueOffset = (3 - i) * 8;
            buf[offset + i] = (byte) ((value >>> valueOffset) & 0xFF);
        }
    }

    public static int byteArray2Int(byte[] b) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i] & 0x000000FF) << shift;
        }
        return value;
    }

    public static long byteArray2Long(byte[] b) {
        int value = 0;
        for (int i = 0; i < 8; i++) {
            int shift = (8 - 1 - i) * 8;
            value += (b[i] & 0x000000FF) << shift;
        }
        return value;
    }

    public static boolean hasBinaryContent(String contentType) {
        String typeStr = (contentType != null) ? contentType.toLowerCase() : "";

        return typeStr.contains("image") || typeStr.contains("audio") ||
               typeStr.contains("video") || typeStr.contains("application");
    }

    public static boolean hasPlainTextContent(String contentType) {
        String typeStr = (contentType != null) ? contentType.toLowerCase() : "";

        return typeStr.contains("text") && !typeStr.contains("html");
    }

    /**
     * @param byteArray
     * @return a String version of the byte[]
     */
    public static String byteArray2String(byte[] byteArray) {    	
		return new String(byteArray);   	
    }
    
    /**
     * @see https://stackoverflow.com/questions/1184176/how-can-i-safely-encode-a-string-in-java-to-use-as-a-filename
     * Added by Saleem Halipoto
     * 
     * @param stringNotGoodForFileName
     * @return
     */
    public static String NormalizeStringForFilename(String stringNotGoodForFileName) {
    	char fileSep = '/'; // ... or do this portably.
    	char escape = '%'; // ... or some other legal char.
    	char atSign = '@';
    	char apostrophe = ',';
    	//String s = ...
    	int len = stringNotGoodForFileName.length();
    	StringBuilder sb = new StringBuilder(len);
    	for (int i = 0; i < len; i++) {
    	    char ch = stringNotGoodForFileName.charAt(i);
    	    if (ch < ' ' || ch >= 0x7F || ch == fileSep   // add other illegal chars right here
    	        || (ch == '.' && i == 0) // we don't want to collide with "." or ".."!
    	        || ch == '\\'	//  the backslash character has to be escaped
    	        || ch == '*'
    	        || ch == '"'
    	        || ch == '?'
    	        || ch == '|'
    	        || ch == '!'
    	        || ch == '<'
    	        || ch == '>'
    	        || ch == atSign
    	        || ch == apostrophe
    	        || ch == escape) {
    	    	sb.append(escape);
    	        if (ch < 0x10) {
    	        	sb.append('0');
    	        }
    	        //sb.append(Integer.toHexString(ch));
    	    } else if (ch == ' '){
    	    	sb.append('_');
    	    } else {
    	        sb.append(ch);
    	    }
    	}
    	System.out.println("The normalized filename is :" + sb.toString());
		return sb.toString();  	
    }
    
    public static StringBuilder AppendExtensionToFilename(StringBuilder filename, String extension) {
    	filename.append(".");
    	filename.append(extension);	
    	logger.debug("The filename has the extension added: " + filename.toString());
		return filename;
    }
}