package de.tuberlin.dima.impro3.tagger;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.junit.Test;

import com.google.common.base.Joiner;

public class GermanTaggerTest {

	GermanTagger pipeline;

	@Test
	public void splitSentences() {
		pipeline = GermanTagger.newSentenceSplitter();

		ArrayList<String> sentences = new ArrayList<String>();
		sentences.add("This is test sentence.");
		sentences.add("This is a sample sentence.");

		String[] retSentences = pipeline.splitSentences(Joiner.on(" ").join(
				sentences));

		assertTrue("Ther should be " + sentences.size() + "sentences",
				retSentences.length == sentences.size());

		int i = 0;
		for (String sentence : sentences) {
			assertTrue("should be the correct sentence.",
					sentence.equals(retSentences[i]));
			i++;
		}

		pipeline = null;
	}

	@Test
	public void tagSentence() throws ClassNotFoundException, IOException,
			URISyntaxException {
		pipeline = GermanTagger.newSentenceTagger();

		String sentence = "SPD hat die Wahl gewonnen.";
		TaggedSentence ts = pipeline.tagSentence(sentence);

		System.out.println(ts);

		assertTrue("There should be 6 tokens", 6 == ts.getTokens().length);
		assertTrue("SPD is an organisation!", "I-ORG".equals(ts.getNe()[0]));

		pipeline = null;
	}

}
