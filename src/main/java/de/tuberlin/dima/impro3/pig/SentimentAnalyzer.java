package de.tuberlin.dima.impro3.pig;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

import de.tuberlin.dima.impro3.sentiment.SentimentParser;

public class SentimentAnalyzer extends EvalFunc<Float> {
		
	@Override
	public Float exec(Tuple input) throws IOException {
		if (input == null || input.size() == 0)
			return null;
		
		String sentence = (String) input.get(0);
		
		SentimentParser sentimentParser = SentimentParser.newSentimentParser();
		
		float sentiment = sentimentParser.computeSentimentValue(sentence);
		
		return sentiment;
	}

}