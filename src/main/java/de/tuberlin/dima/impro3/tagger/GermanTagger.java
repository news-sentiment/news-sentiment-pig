package de.tuberlin.dima.impro3.tagger;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import de.tuberlin.dima.impro3.util.ResourceManager;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations.AnswerAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.util.CoreMap;

public class GermanTagger {

	private static StanfordCoreNLP sentenceSplitter = null;
	private static StanfordCoreNLP pipeline = null;
	private static MaxentTagger tagger = null;
	private static AbstractSequenceClassifier<CoreLabel> classifier = null;

	public static GermanTagger newSentenceSplitter() {
		if (sentenceSplitter == null) {
			Properties props = new Properties();
			props.put("annotators", "tokenize, ssplit");

			sentenceSplitter = new StanfordCoreNLP(props);
		}

		return new GermanTagger();
	}

	public static GermanTagger newSentenceTagger()
			throws ClassNotFoundException, IOException, URISyntaxException {
		if (pipeline == null) {
			String classifierFile = ResourceManager
					.getResourcePath("/ner/dewac_175m_600.crf.ser.gz");

			classifier = CRFClassifier.getClassifier(classifierFile);

			Properties props = new Properties();
			props.put("annotators", "tokenize, ssplit");

			pipeline = new StanfordCoreNLP(props);
		}

		return new GermanTagger();
	}

	public String[] splitSentences(String text) {

		List<String> sentenceList = new ArrayList<String>();

		Annotation document = new Annotation(text);

		sentenceSplitter.annotate(document);

		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		for (CoreMap sentence : sentences) {
			sentenceList.add(sentence.toString());
		}

		String[] sentenceArray = new String[sentenceList.size()];

		sentenceList.toArray(sentenceArray);

		return sentenceArray;
	}

	public TaggedSentence tagSentence(String sentence) {

		List<TaggedWord> taggedWords = new ArrayList<TaggedWord>();
		List<CoreLabel> coreLabels = new ArrayList<CoreLabel>();

		List<String> words = new ArrayList<String>();
		List<String> nerLabels = new ArrayList<String>();

		List<List<HasWord>> hasWords = tagger.tokenizeText(new StringReader(
				sentence));

		for (List<HasWord> s : hasWords) {
			coreLabels.addAll(classifier.classifySentence(s));
		}

		for (List<HasWord> sent : hasWords) {
			for (HasWord hasWord : sent) {
				words.add(hasWord.word());
			}
		}

		for (CoreLabel cl : coreLabels) {
			nerLabels.add(cl.getString(AnswerAnnotation.class));
		}

		return new TaggedSentence(words.toArray(new String[words.size()]),
				nerLabels.toArray(new String[nerLabels.size()]));
	}

}
