package de.tuberlin.dima.impro3.pig;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.pig.FilterFunc;
import org.apache.pig.data.Tuple;

public class SentenceFilter extends FilterFunc {
	
	static int MAXIMUM_SENTENCE_LENGTH = 40;
	static int MAXIMUM_SENTENCE_CHARACTER_LENGTH = 1000;
	
	@Override
	public Boolean exec(Tuple input) throws IOException {
		if (input == null || input.size() == 0)
			return null;

		String sentence = (String) input.get(0);
		
		if (new StringTokenizer(sentence).countTokens() > MAXIMUM_SENTENCE_LENGTH
				|| sentence.length() > MAXIMUM_SENTENCE_CHARACTER_LENGTH) {
			return false;
		}
		
		return true;
	}

}
