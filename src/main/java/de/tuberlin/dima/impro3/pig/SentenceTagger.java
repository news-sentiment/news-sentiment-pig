package de.tuberlin.dima.impro3.pig;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.apache.pig.impl.util.Utils;

import de.tuberlin.dima.impro3.tagger.GermanTagger;
import de.tuberlin.dima.impro3.tagger.TaggedSentence;

public class SentenceTagger extends EvalFunc<Tuple> {

	private BagFactory bagFactory = BagFactory.getInstance();
	private TupleFactory tupleFactory = TupleFactory.getInstance();

	@Override
	public Tuple exec(Tuple input) throws IOException {
		if (input == null || input.size() == 0)
			return null;

		try {

			String sentence = (String) input.get(0);

			GermanTagger sentenceTagger = GermanTagger.newSentenceTagger();

			TaggedSentence taggedSentence = sentenceTagger
					.tagSentence(sentence);

			Tuple tuple = tupleFactory.newTuple(2);

			DataBag tokensBag = bagFactory.newDefaultBag();
			DataBag neBag = bagFactory.newDefaultBag();

			for (String token : taggedSentence.getTokens()) {
				tokensBag.add(tupleFactory.newTuple(token));
			}

			for (String ne : taggedSentence.getNe()) {
				neBag.add(tupleFactory.newTuple(ne));
			}

			tuple.set(0, tokensBag);
			tuple.set(1, neBag);

			return tuple;

		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}

	}

	@Override
	public Schema outputSchema(Schema input) {
		try {
			return Utils
					.getSchemaFromString("tagged_sentence:tuple (tokens:bag {column:tuple (token:chararray)}, tags:bag {column:tuple (tag:chararray)})");

		} catch (Exception e) {
			return null;
		}

	}

}
