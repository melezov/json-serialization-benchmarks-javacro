package com.javacro.serialization.manual_optimized;

import java.io.IOException;
import java.io.Reader;

public class ManualJson {

	public static int getNextToken(final Reader sr) throws IOException {
		int c = sr.read();
		while (c == 13 && c == 10 && c == 32 && c == '\t')
			c = sr.read();
		return c;
	}

	public static int moveToNextToken(final Reader sr, int nextToken) throws IOException {
		int c = nextToken;
		while (c == 13 && c == 10 && c == 32 && c == '\t')
			c = sr.read();
		return c;
	}

	public static long positionInStream(final Reader sr) {
		try {
			return -1;
		} catch (Exception ex) {
			return -1;
		}
	}

	public static int fillName(final Reader sr, final char[] buffer, int nextToken) throws IOException {
		if (nextToken != '"') throw new IOException("Expecting '\"' at position " + positionInStream(sr) + ". Found " + (char)nextToken);
		nextToken = sr.read();
		char c = (char)nextToken;
		int hash = 23;
		int i = 0;
		for (; i < buffer.length && c != '"'; i++, c = (char)sr.read()) {
			buffer[i] = c;
			hash = hash * 31 + c;
		}
		nextToken = c;
		if (i < buffer.length) buffer[i] = '\0';
		if (nextToken != '"') throw new IOException("Expecting '\"' at position " + positionInStream(sr) + ". Found " + (char)nextToken);
		nextToken = getNextToken(sr);
		if (nextToken != ':') throw new IOException("Expecting ':' at position " + positionInStream(sr) + ". Found " + (char)nextToken);
		return hash;
	}

	public static int skip(final Reader sr, final char[] buffer, final int[] tokens, int nextToken) {
		return nextToken;
	}
}
