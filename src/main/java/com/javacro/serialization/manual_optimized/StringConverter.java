package com.javacro.serialization.manual_optimized;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class StringConverter {
	public static void serializeNullable(String value, Writer sw) throws IOException {
		if (value == null) 
			sw.write("null");
		else
			serialize(value, sw);
	}

	public static void serialize(String value, Writer sw) throws IOException {
		sw.write('"');
		char c;
		for (int i = 0; i < value.length(); i++) {
			c = value.charAt(i);
			switch (c) {
				case 0: sw.write("\\u0000"); break;
				case 1: sw.write("\\u0001"); break;
				case 2: sw.write("\\u0002"); break;
				case 3: sw.write("\\u0003"); break;
				case 4: sw.write("\\u0004"); break;
				case 5: sw.write("\\u0005"); break;
				case 6: sw.write("\\u0006"); break;
				case 7: sw.write("\\u0007"); break;
				case 8: sw.write("\\b"); break;
				case 9: sw.write("\\t"); break;
				case 10: sw.write("\\n"); break;
				case 11: sw.write("\\u000B"); break;
				case 12: sw.write("\\f"); break;
				case 13: sw.write("\\r"); break;
				case 14: sw.write("\\u000E"); break;
				case 15: sw.write("\\u000F"); break;
				case 16: sw.write("\\u0010"); break;
				case 17: sw.write("\\u0011"); break;
				case 18: sw.write("\\u0012"); break;
				case 19: sw.write("\\u0013"); break;
				case 20: sw.write("\\u0014"); break;
				case 21: sw.write("\\u0015"); break;
				case 22: sw.write("\\u0016"); break;
				case 23: sw.write("\\u0017"); break;
				case 24: sw.write("\\u0018"); break;
				case 25: sw.write("\\u0019"); break;
				case 26: sw.write("\\u001A"); break;
				case 27: sw.write("\\u001B"); break;
				case 28: sw.write("\\u001C"); break;
				case 29: sw.write("\\u001D"); break;
				case 30: sw.write("\\u001E"); break;
				case 31: sw.write("\\u001F"); break;
				case '\\': sw.write("\\\\"); break;
				case '"': sw.write("\\\""); break;
				default: sw.write(c); break;
			}
		}
		sw.write('"');
	}

	public static String deserialize(Reader sr, char[] buffer, int nextToken) throws IOException {
		if (nextToken != '"') throw new IOException("Expecting '\"' at position " + ManualJson.positionInStream(sr) + ". Found " + (char)nextToken);
		int i = 0;
		nextToken = sr.read();
		for (; nextToken != '"' && i < buffer.length; i++, nextToken = sr.read()) {
			if (nextToken == '\\') {
				nextToken = sr.read();
				switch (nextToken) {
					case (int)'\\': break;
					case (int)'"': break;
					case (int)'b': nextToken = '\b'; break;
					case (int)'t': nextToken = '\t'; break;
					case (int)'r': nextToken = '\r'; break;
					case (int)'n': nextToken = '\n'; break;
					case (int)'u':
						if (i < buffer.length - 4) {
							buffer[i] = (char)sr.read();
							buffer[i + 1] = (char)sr.read();
							buffer[i + 2] = (char)sr.read();
							buffer[i + 3] = (char)sr.read();
							nextToken = Integer.parseInt(new String(buffer, i, 4), 16);
						} else {
							char[] tmp = new char[4];
							tmp[0] = (char)sr.read();
							tmp[1] = (char)sr.read();
							tmp[2] = (char)sr.read();
							tmp[3] = (char)sr.read();
							nextToken = Integer.parseInt(new String(tmp, 0, 4), 16);
						}
						break;
					default:
						throw new IOException("Invalid char found: " + (char)nextToken);
				}
			}
			buffer[i] = (char)nextToken;
		}
		if (i < buffer.length) return new String(buffer, 0, i);
		StringBuilder sb = new StringBuilder(128);
		sb.append(buffer);
		while (nextToken != '"' && nextToken != -1) {
			if (nextToken == '\\') {
				nextToken = sr.read();
				switch (nextToken) {
					case (int)'\\': break;
					case (int)'"': break;
					case (int)'b': nextToken = '\b'; break;
					case (int)'t': nextToken = '\t'; break;
					case (int)'r': nextToken = '\r'; break;
					case (int)'n': nextToken = '\n'; break;
					case (int)'u':
						buffer[0] = (char)sr.read();
						buffer[1] = (char)sr.read();
						buffer[2] = (char)sr.read();
						buffer[3] = (char)sr.read();
						nextToken = Integer.parseInt(new String(buffer, 0, 4), 16);
						break;
					default:
						throw new IOException("Invalid char found: " + (char)nextToken);
				}
			}
			sb.append((char)nextToken);
			nextToken = sr.read();
		}
		return sb.toString();
	}
}
