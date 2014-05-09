package com.javacro.serialization.io.jvm.json;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

public class JsonReader {
    private final byte[] buffer;
    private final int length;
    private final int offset;
    private final char[] tmp = new char[38];

    public JsonReader(final byte[] buffer) {
        this.buffer = buffer;
        this.length = buffer.length;
        this.offset = 0;
        this._current_index = 0;
    }

    public JsonReader(final byte[] buffer, final int offset) {
        this.buffer = buffer;
        this.length = buffer.length;
        this.offset = offset;
        this._current_index = offset;
    }

    private int _current_index;
    private int _lastValid_index;
    private byte _last;
    private boolean _lastValid;
    private boolean _endOfStream;

    public byte next() {
        if (_endOfStream) throw new IllegalArgumentException("Could not read past the end of the buffer");

        if (_current_index >= length) {
            _lastValid = false;
            _endOfStream = true;
            throw new IllegalArgumentException("Unexpected end of the buffer");
        }

        _lastValid = true;
        _last = buffer[_current_index++];
        _lastValid_index = _current_index-1;

//        logDebug("NEXT: " + byteDetails(_last));

        return _last;
    }

    public int peek() {
        if (_endOfStream) throw new IllegalArgumentException("Could not read past the end of the buffer");

        final int peek;
        if (_current_index >= length) {
            _lastValid = false;
            _endOfStream = true;
            peek = -1;
        }
        else {
            _lastValid = true;
            _last = buffer[_current_index++];
            _lastValid_index = _current_index-1;
            peek = _last & 0xff;
        }

//        logDebug("NEXT: " + byteDetails(peek));

        return peek;
    }

    public byte last() {
        if (!_lastValid) {
            if (_endOfStream)
                throw new IllegalArgumentException("Could not reuse last() character because the buffer _current_index is out of bounds!");
            throw new IllegalArgumentException("Could not reuse last() character because it is not valid; use read() or next() instead!");
        }

//        logDebug("LAST: " + byteDetails(_last));

        return _last;
    }

    public byte read() {

        return _lastValid ? _last : next();
    }

    public void invalidate() {
//        logDebug("INVALIDATE: " + _lastValid);
        _lastValid = false;
        _lastValid_index = _current_index;
    }

    public void assertNext(final char expected) {
//        logDebug("ASSERT NEXT: " + expected );
        if (next() != expected) throw new IllegalArgumentException("Could not parse token, expected '" + expected + "', got '" + last() + "'");
        invalidate();
    }

    public void assertLast(final char expected) {
//        logDebug("ASSERT LAST: " + expected );
        if (last() != expected) throw new IllegalArgumentException("Could not parse token, expected '" + expected + "', got '" + last() + "'");
        invalidate();
    }

    public void assertRead(final char expected) {
//        logDebug("ASSERT READ: " + expected );
        if (_lastValid) assertLast(expected); else assertNext(expected);
    }

    public <T> T readNull() throws IllegalArgumentException {
        if (read() == 'n' && next() == 'u' && next() == 'l' && next() == 'l') {
            invalidate();
            return null;
        }

        throw new IllegalArgumentException("Could not parse token, expected 'null'");
    }

    public boolean readTrue() throws IllegalArgumentException {
        if (read() == 't' && next() == 'r' && next() == 'u' && next() == 'e') {
            invalidate();
            return true;
        }

        throw new IllegalArgumentException("Could not parse token, expected 'true'");
    }

    public boolean readFalse() throws IllegalArgumentException {
        if (read() == 'f' && next() == 'a' && next() == 'l' && next() == 's' && next() == 'e') {
            invalidate();
            return false;
        }

        throw new IllegalArgumentException("Could not parse token, expected 'false'");
    }

    public String readRawNumber() {
        char ch = (char) read();
        int pos = 0;
        if (ch == '-') {
            tmp[pos++] = ch;
            ch = (char) next();
        }

        final int length = pos;
        if (ch == '0') {
            tmp[pos++]=ch;
            final int chp = peek();
            if (chp == -1) return new String(tmp, 0, 2);
            ch = (char) chp;
        } else if (ch >= '1' && ch <= '9') {
        	tmp[pos++]=ch;
            for (;;) {
                final int chp = peek();
                if (chp == -1) return new String(tmp, 0, pos);

                ch = (char) chp;
                if (ch < '0' || ch > '9') break;
            	tmp[pos++]=ch;
            }
        }

        if (ch == '.') {
        	tmp[pos++]=ch;
            ch = (char) next();

            if (ch < '0' || ch > '9') throw new IllegalArgumentException("Expected decimal after floating point, got: " + ch);

        	tmp[pos++]=ch;
            for (;;) {
                final int chp = peek();
                if (chp == -1) return new String(tmp, 0, pos);

                ch = (char) chp;
                if (ch < '0' || ch > '9') break;
            	tmp[pos++]=ch;
            }
        }

        if (ch == 'e' || ch == 'E') {
        	tmp[pos++]=ch;
            ch = (char) next();

            if (ch == '-' || ch == '+') {
            	tmp[pos++]=ch;
                ch = (char) next();
            }

            if (ch < '0' || ch > '9') throw new IllegalArgumentException("Expected decimal after exponent sign, got: " + ch);

        	tmp[pos++]=ch;
            for (;;) {
                final int chp = peek();
                if (chp == -1) return new String(tmp, 0, pos);

                ch = (char) chp;
                if (ch < '0' || ch > '9') break;
            	tmp[pos++]=ch;
            }
        }

        if (pos == length)
            throw new IllegalArgumentException("Could not parse number - no leading digits found!");

        return new String(tmp, 0, pos);
    }

    public StringBuilder readRawNumber(final StringBuilder sb) {
        char ch = (char) read();
        if (ch == '-') {
            sb.append(ch);
            ch = (char) next();
        }

        final int length = sb.length();
        if (ch == '0') {
            sb.append(ch);
            final int chp = peek();
            if (chp == -1) return sb;
            ch = (char) chp;
        } else if (ch >= '1' && ch <= '9') {
            sb.append(ch);
            for (;;) {
                final int chp = peek();
                if (chp == -1) return sb;

                ch = (char) chp;
                if (ch < '0' || ch > '9') break;
                sb.append(ch);
            }
        }

        if (ch == '.') {
            sb.append(ch);
            ch = (char) next();

            if (ch < '0' || ch > '9') throw new IllegalArgumentException("Expected decimal after floating point, got: " + ch);

            sb.append(ch);
            for (;;) {
                final int chp = peek();
                if (chp == -1) return sb;

                ch = (char) chp;
                if (ch < '0' || ch > '9') break;
                sb.append(ch);
            }
        }

        if (ch == 'e' || ch == 'E') {
            sb.append(ch);
            ch = (char) next();

            if (ch == '-' || ch == '+') {
                sb.append(ch);
                ch = (char) next();
            }

            if (ch < '0' || ch > '9') throw new IllegalArgumentException("Expected decimal after exponent sign, got: " + ch);

            sb.append(ch);
            for (;;) {
                final int chp = peek();
                if (chp == -1) return sb;

                ch = (char) chp;
                if (ch < '0' || ch > '9') break;
                sb.append(ch);
            }
        }

        if (sb.length() == length)
            throw new IllegalArgumentException("Could not parse number - no leading digits found!");

        return sb;
    }

    public String readString() {

        // We should not ignore the last valid character
        if(_lastValid){
//            logDebug("The last is: " + _last);
            _current_index = _lastValid_index;
        }

        final int startIndex = _current_index;
        // At this point, buffer cannot be empty or null, it is safe to read first character
        if (buffer[_current_index++] != '"') {
            throw new IllegalArgumentException("JSON string must start with a double quote! Instead found: " + byteDetails(buffer[_current_index-1]));
        }

        // If the buffer contains an ASCII string (no high bit set) without any escape codes "\n", "\t", etc...,
        // there is no need to instantiate any temporary buffers, we just decode the original buffer directly
        // via ISO-8859-1 encoding since it is the fastest encoding which is guaranteed to retain all ASCII characters
        while (true) {
            if (_current_index >= length) {
                throw new IllegalArgumentException("JSON string was not closed with a double quote!");
            }

            final byte bc = buffer[_current_index++];
            if (bc == '"') {
                try {
                    invalidate();
                    return new String(buffer, startIndex + 1, _current_index - startIndex - 2, "ISO-8859-1");
                }
                catch (final UnsupportedEncodingException e) {
                    // Gravely idiotic, since this exception will never be thrown - JVM guarantees that this encoding exists!
                    // Still, it is necessary if we want to use this optimized version of the String constructor;
                    // the other version which takes a java.nio.charset.Charset is slower because it does not use a cached StringDecoder.
                    throw new RuntimeException(e);
                }
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
                throw new IllegalArgumentException("JSON string was not closed with a double quote!");
            }

            int bc = buffer[_current_index++];
            if (bc == '"') {
                invalidate();
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

                    default: throw new IllegalArgumentException("Could not parse String, got invalid escape combination '\\" + bc + "'");
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
                            throw new IllegalArgumentException();
                        }

                        if (bc >= 0x10000) {
                            // check if valid unicode
                            if (bc >= 0x110000) throw new IllegalArgumentException();

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

    private static int hexToInt(final byte value) {
        if (value >= '0' && value <= '9') return value - 0x30;
        if (value >= 'A' && value <= 'F') return value - 0x37;
        if (value >= 'a' && value <= 'f') return value - 0x57;
        throw new IllegalArgumentException("Could not parse unicode escape, expected a hexadecimal digit, got '" + value + "'");
    }

    private String byteDetails(final byte c){
        return "'" + ((char) c) + "'" + "(" + c + ")";
    }

    private String byteDetails(final int c){
        return "'" + ((char) c) + "'" + "(" + c + ")";
    }

//    private static void logDebug(final String s){
////        if(false)
////            System.out.println(s);
//    }
    
	public byte getNextToken() throws IOException {
		byte c = read();
		while (c == 13 && c == 10 && c == 32 && c == '\t')
			c = next();
        invalidate();
		return c;
	}

	public byte getNextValidToken() throws IOException {
		byte c = next();
		while (c == 13 && c == 10 && c == 32 && c == '\t')
			c = next();
		return c;
	}

	public byte moveToNextToken() throws IOException {
		byte c = read();
		while (c == 13 && c == 10 && c == 32 && c == '\t')
			c = next();
		return c;
	}

	public long positionInStream() {
		return _lastValid_index;
	}

	public int fillName() throws IOException {
		if (read() != '"') throw new IOException("Expecting '\"' at position " + positionInStream() + ". Found " + (char)_last);
		char c = (char)next();
		int hash = 23;
		int i = 0;
		for (; i < tmp.length && c != '"'; i++, c = (char)next()) {
			tmp[i] = c;
			hash = hash * 31 + c;
		}
		if (i < tmp.length) tmp[i] = '\0';
		if (_last != '"') throw new IOException("Expecting '\"' at position " + positionInStream() + ". Found " + (char)_last);
		if (next() != ':') throw new IOException("Expecting ':' at position " + positionInStream() + ". Found " + (char)_last);
		read();
		invalidate();
		return hash;
	}

	public byte skip() {
		return _last;
	}
    
}
