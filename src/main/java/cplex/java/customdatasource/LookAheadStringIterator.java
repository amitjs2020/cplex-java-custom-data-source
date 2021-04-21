package cplex.java.customdatasource;

/**
 * Iterates over a string with the ability to look ahead one char
 * 
 * @author akjain
 *
 */
public class LookAheadStringIterator {
	String s;
	int len;
	int index;

	/**
	 * Construcs a new LookAheadStringIterator.
	 * 
	 * @param s The string to iterate over.
	 */
	public LookAheadStringIterator(String s) {
		this.s = s;
		this.index = 0;
		if (s != null) {
			this.len = s.length();
		}
	}

	/**
	 * @return The current char in the iterator.
	 */
	int currentChar() {
		if (index < len)
			return s.charAt(index);
		else
			return -1;
	}

	/**
	 * 
	 * @return The next char in the iterator.
	 */
	int nextChar() {
		if ((index + 1) < len)
			return s.charAt(index + 1);
		else
			return -1;
	}

	/**
	 * Advance to the next char.
	 */
	void next() {
		if (index < len)
			index++;
	}

	/**
	 * @return true if there are more chars available on this iterator.
	 */
	boolean available() {
		return index < len;
	}

	/**
	 * Extracts the next java identifier available from the string. The identifier
	 * starts at the current char.
	 * 
	 * After the identifier is extracted, nextChar() returns the first char after
	 * the identifier.
	 * 
	 * @return The next java identifier available.
	 */
	public String extractIdentifierName() {
		int i = index;
		while (i < len && Character.isJavaIdentifierPart(s.charAt(i))) {
			i++;
		}
		String name = s.substring(index, i);
		index = i - 1;
		return name;
	}

	/**
	 * Swallows all chars until endChar appears in the stream
	 * 
	 * @param buffer  The StringBuffer to append swallowed chars
	 * @param endChar
	 */
	void swallow(StringBuffer buffer, char endChar) {
		while (available()) {
			int c = currentChar();
			buffer.append((char) c); // append the char in every case
			if ((char) c != endChar) {
				// nothing to do, char already swallowed
			} else if ((char) nextChar() == c) {
				// case where ' or " is escaped (so '' or "")
				next();
				buffer.append((char) c);
			} else {
				break;
			}
			next();
		}
	}
}
