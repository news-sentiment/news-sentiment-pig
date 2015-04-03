package de.tuberlin.dima.impro3.sentiment;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import de.tuberlin.dima.impro3.tagger.TaggedSentence;
import de.tuberlin.dima.impro3.util.ResourceManager;

import au.com.bytecode.opencsv.CSVReader;

public class SentimentParser {

	/**
	 * Singleton instance
	 */
	private static SentimentParser sentimentParser = null;

	/**
	 * List of all words and their sentiments.
	 */
	private List<Sentiment> sentiments = new ArrayList<Sentiment>();

	/**
	 * Singleton accessor.
	 * 
	 * @return SentimentParser instance
	 */
	public static SentimentParser newSentimentParser() {

		if (sentimentParser == null) {
			try {
				String sentimentPositive = ResourceManager
						.getResourcePath("/sentiws/SentiWS_v1.8c_Positive.txt");
				String sentimentNegative = ResourceManager
						.getResourcePath("/sentiws/SentiWS_v1.8c_Negative.txt");

				String sentiFiles = sentimentPositive + ";" + sentimentNegative;

				sentimentParser = new SentimentParser(sentiFiles);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sentimentParser;
	}

	/**
	 * Private constructor to make sure it's not called multiple times.
	 * 
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private SentimentParser(String sentiFiles) throws NumberFormatException,
			IOException {

		String[] sentiFilesArr = sentiFiles.split(";");
		for (String file : sentiFilesArr) {
			CSVReader reader = null;

			try {
				reader = new CSVReader(new FileReader(
						new File(file).getAbsolutePath()), '\t');

				String[] nextLine;

				while ((nextLine = reader.readNext()) != null) {
					/*
					 * Example: Aggressor|NN -0.3155 Aggressoren,Aggressors
					 * nextLine[0] => Aggressor|NN nextLine[1] => -0.3155
					 * nextLine[2] => Aggressoren,Aggressors
					 */

					ArrayList<String> words = new ArrayList<String>();

					// Add first word to words list (Aggressor)
					// Ignore word class (NN)
					words.add(nextLine[0].split("\\|")[0]);

					// Parse sentiment value (-0.3155)
					float sentiment = Float.valueOf(nextLine[1]);

					// If variations exist, add them to the word lost too.
					if (nextLine.length > 2) {
						// (Aggressoren,Aggressors)
						String[] variations = nextLine[2].split(",");
						for (String variation : variations) {
							words.add(variation);
						}
					}

					// Create new Sentiment object and add it to our list
					Sentiment s = new Sentiment(words, sentiment);
					sentiments.add(s);
				}
			} catch (IOException e) {
				System.err.println("Could not read CSV stream " + file);
				e.printStackTrace();
			}
		}
	}

	/**
	 * Searches for a single word in our Sentiment list. If the word is not in
	 * the list, returns Float.MAX_VALUE.
	 * 
	 * @param word
	 *            The word to search for
	 * @return The word's sentiment value or Float.MAX_VALUE
	 */
	public float getSentiment(String word) {
		Sentiment s = null;
		for (int i = 0; i < sentiments.size(); i++) {
			s = sentiments.get(i);
			if (s.contains(word)) {
				return s.getSentiment();
			}
		}
		return Float.MAX_VALUE;
	}

	/**
	 * Calculates the sentiment for a list of words, i.e. quote.
	 * 
	 * @param taggedSentence
	 * @return The average sentiment for all the tokens in the list
	 */
	public float computeSentimentValue(String sentence) {
		float overallSentiment = 0;

		// Count the words we find sentiments for
		int foundSentiments = 0;

		StringTokenizer tokenizer = new StringTokenizer(sentence);
		while (tokenizer.hasMoreTokens()) {
			float sentiment = newSentimentParser().getSentiment(
					tokenizer.nextToken());
			if (sentiment != Float.MAX_VALUE) {
				overallSentiment += sentiment;
				foundSentiments++;
			}
		}

		// Normalize sentiment
		return foundSentiments > 0 ? overallSentiment / foundSentiments : 0.0f;
	}

	/**
	 * Calculates the sentiment for a list of words, i.e. quote.
	 * 
	 * @param taggedSentence
	 * @return The average sentiment for all the tokens in the list
	 */
	public float computeSentimentValue(TaggedSentence taggedSentence) {

		float overallSentiment = 0;

		// Count the words we find sentiments for
		int foundSentiments = 0;

		for (String token : taggedSentence.getTokens()) {
			// Get sentiment for current word
			float sentiment = newSentimentParser().getSentiment(token);
			if (sentiment != Float.MAX_VALUE) {
				overallSentiment += sentiment;
				foundSentiments++;
			}
		}

		// Normalize sentiment
		return foundSentiments > 0 ? overallSentiment / foundSentiments : 0.0f;
	}

}
