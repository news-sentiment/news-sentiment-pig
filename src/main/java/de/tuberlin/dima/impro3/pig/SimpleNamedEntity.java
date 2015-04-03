package de.tuberlin.dima.impro3.pig;

import java.io.IOException;
import java.util.List;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.schema.Schema;

import de.tuberlin.dima.impro3.util.PoliticalParties;

public class SimpleNamedEntity extends EvalFunc<DataBag> {

	private BagFactory bagFactory = BagFactory.getInstance();
	private TupleFactory tupleFactory = TupleFactory.getInstance();

	@Override
	public DataBag exec(Tuple input) throws IOException {
		if (input == null || input.size() == 0)
			return null;

		String sentence = (String) input.get(0);

		DataBag namedEntities = bagFactory.newDefaultBag();

		List<String> parties = PoliticalParties.getParties();
		String[] neFilter = parties.toArray(new String[parties.size()]);

		for (int i = 0; i < neFilter.length; i++) {
			if (sentence.contains(neFilter[i])) {
				namedEntities.add(tupleFactory.newTuple(neFilter[i]));
			}
		}

		return namedEntities;
	}

	@Override
	public Schema outputSchema(Schema input) {
		try {
			Schema bagSchema = new Schema();
			bagSchema.add(new Schema.FieldSchema("named_entity",
					DataType.CHARARRAY));
			return new Schema(new Schema.FieldSchema(getSchemaName(
					"named_entities", input), bagSchema, DataType.BAG));
		} catch (Exception e) {
			return null;
		}
	}

}
