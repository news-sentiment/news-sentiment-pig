package de.tuberlin.dima.impro3.pig;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.schema.Schema;

import de.tuberlin.dima.impro3.tagger.GermanTagger;

public class SentenceSplitter extends EvalFunc<DataBag> {

	private BagFactory bagFactory = BagFactory.getInstance();
	private TupleFactory tupleFactory = TupleFactory.getInstance();
	private GermanTagger sentenceTagger = GermanTagger.newSentenceSplitter();

	@Override
	public DataBag exec(Tuple input) throws IOException {
		if (input == null || input.size() == 0)
			return null;

		String newsBody = (String) input.get(0);

		String[] sentences = sentenceTagger.splitSentences(newsBody);

		DataBag bag = bagFactory.newDefaultBag();

		for (String sentence : sentences) {
			bag.add(tupleFactory.newTuple(sentence));
		}

		return bag;
	}

	@Override
	public Schema outputSchema(Schema input) {
		try {
			Schema bagSchema = new Schema();
			bagSchema
					.add(new Schema.FieldSchema("sentence", DataType.CHARARRAY));

			return new Schema(new Schema.FieldSchema(
					getSchemaName(null, input), bagSchema, DataType.BAG));
		} catch (Exception e) {
			return null;
		}

	}

}
