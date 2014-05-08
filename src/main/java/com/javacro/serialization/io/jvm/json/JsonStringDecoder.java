package com.javacro.serialization.io.jvm.json;

import java.io.UnsupportedEncodingException;

public class JsonStringDecoder {
    private static int hexToInt(final byte value) {
        if (value >= '0' && value <= '9') return value - 0x30;
        if (value >= 'A' && value <= 'F') return value - 0x37;
        if (value >= 'a' && value <= 'f') return value - 0x57;
        throw new IllegalArgumentException("Could not parse unicode escape, expected a hexadecimal digit, got '" + value + "'");
    }

    public static String decode(final byte[] buffer, final int offset) {
        // early "check" for null
        final int length = buffer.length;

        // check if offset is positive and less than buffer length
        if ((offset & 0x7fffffff) >= length) {
            throw new IndexOutOfBoundsException("Provided offset was invalid!");
        }

        // At this point, buffer cannot be empty or null, it is safe to read first character
        int current = offset;
        if (buffer[current++] != '"') {
            throw new IllegalArgumentException("JSON string did must start with a double quote!");
        }

        // If the buffer contains an ASCII string (no high bit set) without any escape codes "\n", "\t", etc...,
        // there is no need to instantiate any temporary buffers, we just decode the original buffer directly
        // via ISO-8859-1 encoding since it is the fastest encoding which is guaranteed to retain all ASCII characters
        while (true) {
            if (current >= length) {
                throw new IllegalArgumentException("JSON string was not closed with a double quote!");
            }

            final byte bc = buffer[current++];
            if (bc == '"') {
                try {
                    return new String(buffer, offset + 1, current - offset - 2, "ISO-8859-1");
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
        int index = --current - offset - 1;
        char[] chars = new char[index + 256];

        // copy all the ASCII characters so far
        for (int i = index; i >= 0; i--) {
            chars[i] = (char) buffer[offset + i + 1];
        }

        while (true) {
            if (current >= length) {
                throw new IllegalArgumentException("JSON string was not closed with a double quote!");
            }

            int bc = buffer[current++];
            if (bc == '"') {
                return new String(chars, 0, index);
            }

            // if we're running out of space, double the buffer capacity
            if (index >= chars.length - 3) {
                final char[] newChars = new char[chars.length << 1];
                System.arraycopy(chars, 0, newChars, 0, index);
                chars = newChars;
            }

            if (bc == '\\') {
                bc = buffer[current++];

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
                            (hexToInt(buffer[current++]) << 12) +
                            (hexToInt(buffer[current++]) <<  8) +
                            (hexToInt(buffer[current++]) <<  4) +
                             hexToInt(buffer[current++]); break;

                    default: throw new IllegalArgumentException("Could not parse String, got invalid escape combination '\\" + bc + "'");
                }
            }
            else if ((bc & 0x80) != 0) {
                final int u2 = buffer[current++];
                if ((bc & 0xE0) == 0xC0) {
                    bc = ((bc & 0x1F) << 6) + (u2 & 0x3F);
                }
                else {
                    final int u3 = buffer[current++];
                    if ((bc & 0xF0) == 0xE0) {
                        bc = ((bc & 0x0F) << 12) + ((u2 & 0x3F) <<  6) + (u3 & 0x3F);
                    }
                    else {
                        final int u4 = buffer[current++];
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
                            chars[index++] = (char) ((sup >>> 10) + 0xd800);
                            chars[index++] = (char) ((sup & 0x3ff) + 0xdc00);
                        }
                    }
                }
            }

            chars[index++] = (char) bc;
        }
    }
}
