package mapreduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import infrastructure.NodeIdPair;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import core.Driver;

public class InitialHyperedgeMapper extends
		Mapper<LongWritable, Text, NodeIdPair, DoubleWritable> {

	private static String valueString;

	private static double weight;

	@Override
	public void map(LongWritable key, Text value, Context context) {
		valueString = value.toString();
		String[] splits = valueString.split("\t");
		String[] authorNames = splits[1].split(",");
		String[] timeStamps = splits[2].split(",");
		List<Integer> sortedAuthorList = getSortedListFromStringArray(authorNames);
		List<Integer> sortedTimeStampList = getSortedListFromStringArray(timeStamps);
		weight = getHyperedgeWeight(sortedTimeStampList,
				Driver.currentTimeStamp, sortedAuthorList.size());
		DoubleWritable hyperedgeWeight = new DoubleWritable(weight);

		for (int i = 0; i < sortedAuthorList.size() - 1; i++) {
			int node1Id = sortedAuthorList.get(i);
			for (int j = i + 1; j < sortedAuthorList.size(); j++) {
				int node2Id = sortedAuthorList.get(j);
				try {
					context.write(new NodeIdPair(node1Id, node2Id),
							hyperedgeWeight);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public double getHyperedgeWeight(List<Integer> sortedTimeStampList,
			int currentTimeStamp, int cardinality) {
		double weight = 0;
		for (int i = 0; i < sortedTimeStampList.size(); i++) {
			int difference = currentTimeStamp - sortedTimeStampList.get(i);
			if (difference < 0) {
				break;
			}
			weight += Math.exp(-1 * difference);
		}
		return weight / cardinality;
	}

	public List<Integer> getSortedListFromStringArray(String[] authorNames) {
		List<Integer> authorList = new ArrayList<Integer>();
		for (int i = 0; i < authorNames.length; i++) {
			authorList.add(Integer.parseInt(authorNames[i]));
		}
		Collections.sort(authorList);
		return authorList;
	}

}
