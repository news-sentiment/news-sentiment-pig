package de.tuberlin.dima.impro3.pig;

import java.io.IOException;

import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.pig.Expression;
import org.apache.pig.LoadFunc;
import org.apache.pig.LoadMetadata;
import org.apache.pig.ResourceSchema;
import org.apache.pig.ResourceStatistics;
import org.apache.pig.backend.hadoop.executionengine.mapReduceLayer.PigSplit;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsPaperJsonLoader extends LoadFunc implements LoadMetadata {

	protected RecordReader reader = null;
	private TupleFactory tupleFactory;

	@SuppressWarnings("rawtypes")
	@Override
	public InputFormat getInputFormat() throws IOException {
		return new TextInputFormat();
	}

	@Override
	public Tuple getNext() throws IOException {
		try {
			do {

				boolean next = reader.nextKeyValue();
				if (!next) {
					return null;
				}

				String jsonInput = reader.getCurrentValue().toString();
				JSONObject jsonObject = new JSONObject(jsonInput);

				if (jsonObject.has("d")) {

					JSONObject data = jsonObject.getJSONObject("d");

					if (data.has("Document_Content_News_Body")
							&& data.has("Document_Content_News_Publication_Name")) {

						String newsBody = data
								.getString("Document_Content_News_Body");
						String publicationName = data
								.getString("Document_Content_News_Publication_Name");

						Tuple tuple = tupleFactory.newTuple(2);

						tuple.set(0, publicationName);
						tuple.set(1, newsBody);

						return tuple;
					}
				}

			} while (true);

		} catch (InterruptedException e) {
			throw new IOException(e);
		} catch (JSONException e) {
			throw new IOException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void prepareToRead(RecordReader reader, PigSplit split)
			throws IOException {
		this.reader = reader;
		this.tupleFactory = TupleFactory.getInstance();
	}

	@Override
	public void setLocation(String location, Job job) throws IOException {
		FileInputFormat.setInputPaths(job, location);
	}

	@Override
	public String[] getPartitionKeys(String location, Job job)
			throws IOException {
		return null;
	}

	@Override
	public ResourceSchema getSchema(String location, Job job)
			throws IOException {
		Schema schema = new Schema();
		schema.add(new Schema.FieldSchema("publicationName", DataType.CHARARRAY));
		schema.add(new Schema.FieldSchema("newsBody", DataType.CHARARRAY));

		return new ResourceSchema(schema);
	}

	@Override
	public ResourceStatistics getStatistics(String location, Job jb)
			throws IOException {
		return null;
	}

	@Override
	public void setPartitionFilter(Expression partitionFilter)
			throws IOException {
		// NO OP
	}

}
