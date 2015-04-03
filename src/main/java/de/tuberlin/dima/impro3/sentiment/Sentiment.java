package de.tuberlin.dima.impro3.sentiment;

import java.util.ArrayList;

public class Sentiment {

	/**
	 * Represents one line of the sentiment files. List of a word and all of its
	 * variations.
	 */
	private ArrayList<String> words;

	/**
	 * Sentiment value for all these words.
	 */
	private float sentiment;

	/**
	 * @param words
	 *            List of words which have the same sentiment
	 * @param sentiment
	 *            Sentiment value for all the words
	 */
	public Sentiment(ArrayList<String> words, float sentiment) {
		this.words = words;
		this.sentiment = sentiment;
	}

	public float getSentiment() {
		return this.sentiment;
	}

	/**
	 * Checks if a word is in the list.
	 * 
	 * @param word
	 *            The word you are looking for
	 * @return True if word was found, otherwise false
	 */
	public boolean contains(String word) {
		return this.words.contains(word);
	}

	@Override
	public String toString() {
		return this.words.toString() + ", " + this.sentiment;
	}

}
