package de.tuberlin.dima.impro3.pig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.schema.Schema;

public class NamedEntity extends EvalFunc<DataBag> {

	private BagFactory bagFactory = BagFactory.getInstance();
	private TupleFactory tupleFactory = TupleFactory.getInstance();

	@Override
	public DataBag exec(Tuple input) throws IOException {
		if (input == null || input.size() == 0)
			return null;

		DataBag namedEntities = bagFactory.newDefaultBag();

		Tuple t1 = (Tuple) input.get(0);
		DataBag tokensBag = (DataBag) t1.get(0);
		DataBag neBag = (DataBag) t1.get(1);

		String[] tokens = new String[(int) tokensBag.size()];
		String[] ne = new String[(int) neBag.size()];

		int ti = 0;
		for (Iterator iterator = tokensBag.iterator(); iterator.hasNext();) {
			Tuple token = (Tuple) iterator.next();
			tokens[ti] = (String) token.get(0);
			ti++;
		}

		int ni = 0;
		for (Iterator iterator = neBag.iterator(); iterator.hasNext();) {
			Tuple neTag = (Tuple) iterator.next();
			ne[ni] = (String) neTag.get(0);
			ni++;
		}

		ArrayList<String> nelist = new ArrayList<String>();
		String composedToken = "";

		for (int i = 0; i < ne.length; i++) {

			if (ne[i].equals("I-ORG") || ne[i].equals("ORG")) {
				composedToken += tokens[i] + " ";
			}

			if (ne[i].equals("O") || i == ne.length - 1) {
				if (composedToken != "") {
					nelist.add(composedToken.trim());
				}

				composedToken = "";
			}

		}

		for (String tok : nelist) {
			namedEntities.add(tupleFactory.newTuple(tok));
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
