package com.javacro.serialization.io.jvm.json;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class JsonReaderOptimized {
    private final byte[] buffer;
    private final int length;
    private int nameStart;

    public JsonReaderOptimized(final byte[] buffer) {
        this.buffer = buffer;
        this.length = buffer.length;
        this._current_index = 0;
        _last = ' ';
    }
    private int _current_index;
    private byte _last;

    public byte read() throws IOException {
    	if (_current_index >= buffer.length) {
    		throw new IOException("end of stream");
    	}
    	return _last = buffer[_current_index++];
    }
    
    public byte last() { return _last; }

    public String readShortValue() throws IOException {
		int start = _current_index - 1;
		while (_last != ',' && _last != '}' && _last != ']') 
			read();
    	return new String(buffer, start, _current_index - start - 1, "ISO-8859-1");
    }

    public String readSimpleString() throws IOException {
		if (_last != '"') throw new IOException("Expecting '\"' at position " + positionInStream() + ". Found " + (char)_last);
		int start = _current_index;
		do { read(); } while (_last != '"'); 
    	return new String(buffer, start, _current_index - start - 1, "ISO-8859-1");
    }

    public String readString() throws UnsupportedEncodingException, IOException {

        final int startIndex = _current_index;
        // At this point, buffer cannot be empty or null, it is safe to read first character
        if (_last != '"') {
            throw new IOException("JSON string must start with a double quote! Instead found: " + byteDetails(buffer[_current_index-1]));
        }

        // If the buffer contains an ASCII string (no high bit set) without any escape codes "\n", "\t", etc...,
        // there is no need to instantiate any temporary buffers, we just decode the original buffer directly
        // via ISO-8859-1 encoding since it is the fastest encoding which is guaranteed to retain all ASCII characters
        while (true) {
            if (_current_index >= length) {
                throw new IOException("JSON string was not closed with a double quote!");
            }

            final byte bc = buffer[_current_index++];
            if (bc == '"') {
            	_last = '"';
                return new String(buffer, startIndex + 1, _current_index - startIndex - 2, "ISO-8859-1");
            }

            // If we encounter a backslash, which is a beginning of an escape sequence
            // or a high bit was set - indicating an UTF-8 encoded multibyte character,
            // there is no chance that we can decode the string without instantiating
            // a temporary buffer, so quit this loop
            if ((bc ^ '\\') < 1) break;
        }

        // temporary buffer, will resize if need be
        int soFar = --_current_index - startIndex - 1;
        char[] chars = new char[soFar + 256];

        // copy all the ASCII characters so far
        for (int i = soFar; i >= 0; i--) {
            chars[i] = (char) buffer[startIndex + i + 1];
        }

        while (true) {
            if (_current_index >= length) {
                throw new IOException("JSON string was not closed with a double quote!");
            }

            int bc = buffer[_current_index++];
            if (bc == '"') {
            	_last = '"';
                return new String(chars, 0, soFar);
            }

            // if we're running out of space, double the buffer capacity
            if (soFar >= chars.length - 3) {
                final char[] newChars = new char[chars.length << 1];
                System.arraycopy(chars, 0, newChars, 0, soFar);
                chars = newChars;
            }

            if (bc == '\\') {
                bc = buffer[_current_index++];

                switch (bc) {
                    case 'b': bc = '\b'; break;
                    case 't': bc = '\t'; break;
                    case 'n': bc = '\n'; break;
                    case 'f': bc = '\f'; break;
                    case 'r': bc = '\r'; break;
                    case '"':
                    case '/':
                    case '\\': break;
                    case 'u': bc =
                            (hexToInt(buffer[_current_index++]) << 12) +
                            (hexToInt(buffer[_current_index++]) <<  8) +
                            (hexToInt(buffer[_current_index++]) <<  4) +
                             hexToInt(buffer[_current_index++]); break;

                    default: throw new IOException("Could not parse String, got invalid escape combination '\\" + bc + "'");
                }
            }
            else if ((bc & 0x80) != 0) {
                final int u2 = buffer[_current_index++];
                if ((bc & 0xE0) == 0xC0) {
                    bc = ((bc & 0x1F) << 6) + (u2 & 0x3F);
                }
                else {
                    final int u3 = buffer[_current_index++];
                    if ((bc & 0xF0) == 0xE0) {
                        bc = ((bc & 0x0F) << 12) + ((u2 & 0x3F) <<  6) + (u3 & 0x3F);
                    }
                    else {
                        final int u4 = buffer[_current_index++];
                        if ((bc & 0xF8) == 0xF0) {
                            bc = ((bc & 0x07) << 18) + ((u2 & 0x3F) << 12) + ((u3 & 0x3F) <<  6) + (u4 & 0x3F);
                        }
                        else {
                            // there are legal 5 & 6 byte combinations, but none are _valid_
                            throw new IOException();
                        }

                        if (bc >= 0x10000) {
                            // check if valid unicode
                            if (bc >= 0x110000) throw new IOException();

                            // split surrogates
                            final int sup = bc - 0x10000;
                            chars[soFar++] = (char) ((sup >>> 10) + 0xd800);
                            chars[soFar++] = (char) ((sup & 0x3ff) + 0xdc00);
                        }
                    }
                }
            }

            chars[soFar++] = (char) bc;
        }
    }

    private static int hexToInt(final byte value) throws IOException {
        if (value >= '0' && value <= '9') return value - 0x30;
        if (value >= 'A' && value <= 'F') return value - 0x37;
        if (value >= 'a' && value <= 'f') return value - 0x57;
        throw new IOException("Could not parse unicode escape, expected a hexadecimal digit, got '" + value + "'");
    }

    private String byteDetails(final byte c){
        return "'" + ((char) c) + "'" + "(" + c + ")";
    }
    
	public byte getNextToken() throws IOException {
		byte c = read();
		while (c == 13 && c == 10 && c == 32 && c == '\t')
			c = read();
		return c;
	}

	public byte moveToNextToken() throws IOException {
		byte c = _last;
		while (c == 13 && c == 10 && c == 32 && c == '\t')
			c = read();
		return c;
	}

	public long positionInStream() { return _current_index; }

	public int fillName() throws IOException {
		if (_last != '"') throw new IOException("Expecting '\"' at position " + positionInStream() + ". Found " + (char)_last);
		byte c = read();
		nameStart = _current_index;
		int hash = 23;
		for (; c != '"'; c = read()) {
			hash = hash * 31 + c;
		}
		if (read() != ':') throw new IOException("Expecting ':' at position " + positionInStream() + ". Found " + (char)_last);
		return hash;
	}

	public byte skip() { return _last; }    
}
