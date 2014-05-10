package com.javacro.serialization.manual_optimized;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class NumberConverter {
	public static void serializeDoubleNullable(final Double value, final Writer sw) throws IOException {
		if (value == null) 
			sw.write("null");
		else 
			sw.write(value.toString());
	}

	public static void serializeDouble(final double value, final Writer sw) throws IOException {
		sw.write(Double.toString(value));
	}
	public static double deserializeDouble(final Reader sr, final char[] buffer, final int[] tokens, int nextToken) throws IOException {
		int ind = 0;
		do {
			buffer[ind++] = (char)nextToken;
			nextToken = sr.read();
		} while (ind < buffer.length && nextToken != ',' && nextToken != '}' && nextToken != ']');
		tokens[0] = nextToken;
		return Double.parseDouble(new String(buffer, 0, ind));
	}
	public static void serializeIntNullable(final Integer value, final Writer sw) throws IOException {
		if (value == null) 
			sw.write("null");
		else 
			sw.write(value.toString());
	}

	public static void serializeInt(final int value, final Writer sw) throws IOException {
		sw.write(Integer.toString(value));
	}
	public static int deserializeInt(final Reader sr, final char[] buffer, final int[] tokens, int nextToken) throws IOException {
		int ind = 0;
		do {
			buffer[ind++] = (char)nextToken;
			nextToken = sr.read();
		} while (ind < buffer.length && nextToken != ',' && nextToken != '}' && nextToken != ']');
		tokens[0] = nextToken;
		return Integer.parseInt(new String(buffer, 0, ind));
	}
	
	public static int deserializeInt(final char[] buffer, final int len) throws IOException {
		return Integer.parseInt(new String(buffer, 0, len));
	}	
}
