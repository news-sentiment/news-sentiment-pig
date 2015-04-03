package de.tuberlin.dima.impro3.tagger;

import java.util.Arrays;

/**
 * This class represents a tagged sentence.
 */
public class TaggedSentence {

	/* tokens array of a sentence */
	private String[] tokens;

	/* named entities of a sentence */
	private String[] ne;

	public TaggedSentence(String[] tokens, String[] ne) {
		this.tokens = tokens;
		this.ne = ne;
	}

	public String[] getTokens() {
		return tokens;
	}

	public void setTokens(String[] tokens) {
		this.tokens = tokens;
	}

	public String[] getNe() {
		return ne;
	}

	public void setNe(String[] ne) {
		this.ne = ne;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(ne);
		result = prime * result + Arrays.hashCode(tokens);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaggedSentence other = (TaggedSentence) obj;
		if (!Arrays.equals(ne, other.ne))
			return false;
		if (!Arrays.equals(tokens, other.tokens))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TaggedSentence [tokens=" + Arrays.toString(tokens) + ", ne="
				+ Arrays.toString(ne) + "]";
	}

}
