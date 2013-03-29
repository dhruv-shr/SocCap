package mapreduce;

import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class NodeDegreeOfAttentionReducer extends
		Reducer<IntWritable, IntWritable, Text, Text> {

	@Override
	public void reduce(IntWritable key, Iterable<IntWritable> values,
			Context context) throws IOException, InterruptedException {
		int count = 0;
		for (IntWritable value : values) {
			count++;
		}
		double value = 1.0 / count;

		// Adjusting key length for indexing
		String keyStr = Integer.toString(key.get());
		int keyLen = keyStr.length();
		StringBuffer keyBuffer = new StringBuffer();
		if (keyLen == 1) {
			keyBuffer.append("000000");
		} else if (keyLen == 2) {
			keyBuffer.append("00000");
		} else if (keyLen == 3) {
			keyBuffer.append("0000");
		} else if (keyLen == 4) {
			keyBuffer.append("000");
		} else if (keyLen == 5) {
			keyBuffer.append("00");
		} else if (keyLen == 6) {
			keyBuffer.append("0");
		}
		keyBuffer.append(keyStr);

		// to make the value of the degree of attention of the node of a fixed
		// length for indexing purpose.
		DecimalFormat df = new DecimalFormat("#.#####");
		String val = df.format(value);
		StringBuffer buffer = new StringBuffer(val);
		if (val.length() == 1) {
			buffer.append(".00000");
		} else if (val.length() == 3) {
			buffer.append("0000");
		} else if (val.length() == 4) {
			buffer.append("000");
		} else if (val.length() == 5) {
			buffer.append("00");
		} else if (val.length() == 6) {
			buffer.append("0");
		}

		context.write(new Text(keyBuffer.toString()), new Text(buffer
				.toString()));

		// the output file from this reducer has to be put into the distributed
		// cache and used for weight calculation of edges later.

	}

}
