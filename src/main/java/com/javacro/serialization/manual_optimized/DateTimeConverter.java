package com.javacro.serialization.manual_optimized;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.joda.time.DateTime;

public class DateTimeConverter {
	public static DateTime minDate = DateTime.parse("1-1-1");
	
	public static void serializeNullable(DateTime value, Writer sw) throws IOException {
		if (value == null) 
			sw.write("null");
		else
			serialize(value, sw);
	}

	public static void serialize(DateTime value, Writer sw) throws IOException {
		sw.write('"');
		sw.write(value.toString());
		sw.write('"');
	}

	public static DateTime deserialize(final Reader sr, final char[] buffer, int nextToken) throws IOException {
		if (nextToken != '"') throw new IOException("Expecting '\"' at position " + ManualJson.positionInStream(sr) + ". Found " + (char)nextToken);
		int i = 0;
		nextToken = sr.read();
		for (; i < buffer.length && nextToken != '"'; i++, nextToken = sr.read())
			buffer[i] = (char)nextToken;
		return DateTime.parse(new String(buffer, 0, i));
	}
}
