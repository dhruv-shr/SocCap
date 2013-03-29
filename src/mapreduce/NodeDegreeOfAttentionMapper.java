package mapreduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class NodeDegreeOfAttentionMapper extends
		Mapper<LongWritable, Text, IntWritable, IntWritable> {

	public static String valueString;

	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		valueString = value.toString();
		String[] splits = valueString.split("\t");
		IntWritable hyperedgeId = new IntWritable(Integer.parseInt(splits[0]));
		// we can look for time stamps here also.
		String[] authorIdArray = splits[1].split(",");
		List<Integer> authorIdIntegerList = getIntegerListFromStringArray(authorIdArray);
		for (Iterator<Integer> authorListIterator = authorIdIntegerList
				.iterator(); authorListIterator.hasNext();) {
			context.write(new IntWritable(authorListIterator.next()),
					hyperedgeId);
		}

	}

	public List<Integer> getIntegerListFromStringArray(String[] authorNames) {
		List<Integer> authorList = new ArrayList<Integer>();
		for (int i = 0; i < authorNames.length; i++) {
			authorList.add(Integer.parseInt(authorNames[i]));
		}
		return authorList;
	}
}
