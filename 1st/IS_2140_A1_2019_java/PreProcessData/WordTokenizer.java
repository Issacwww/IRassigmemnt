package PreProcessData;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is for INFSCI 2140 in 2019
 * 
 * TextTokenizer can split a sequence of text into individual word tokens.
 */
public class WordTokenizer {
	// Essential private methods or variables can be added.
	private int curPos,maxPos;
	//store the tokens after splitting
	List<String> tokens;
	//the regex pattern of English terms
	String pattern = "(?:[A-Z]\\.)+" +
			"|\\$?\\d+(?:\\.\\d+)?%?" +
			"|\\w+(?:[-']\\w+)*" +
			"|\\.\\.\\." +
			"|(?:[-.,;\"'?():_`<>{}+&@!^#*\\[\\]])";
	// YOU MUST IMPLEMENT THIS METHOD.
	public WordTokenizer( char[] texts ) {
		// Tokenize the input texts.
		String text = String.valueOf(texts);
		// match the pattern using regex
		tokens = new ArrayList<>();
		Matcher m = Pattern.compile(pattern)
				.matcher(text);
		//store all the tokens
		while (m.find()) {
			tokens.add(m.group());
		}
		curPos = -1;
		maxPos = tokens.size();
	}
	
	// YOU MUST IMPLEMENT THIS METHOD.
	public char[] nextWord() {
		// Return the next word in the document.
		// Return null, if it is the end of the document.
		curPos += 1;
		if(curPos == maxPos)
			return null;
		else return tokens.get(curPos).toCharArray();
	}
	
}
