package core;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import mapreduce.NodeDegreeOfAttentionMapper;
import mapreduce.NodeDegreeOfAttentionReducer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class Driver {

	public static int currentTimeStamp;

	public static void main(String[] args) throws IOException,
			InterruptedException, ClassNotFoundException {

		// Reading in the command line input
		String inputPath = args[0];
		String outputPath = args[1];
		currentTimeStamp = Integer.parseInt(args[2]);
		String intermediateOutputPath = "dblp_data/intermediate_files";

		// creating new job to compute the degree of attention of a node
		Job computeDoAJob = new Job();
		computeDoAJob.setJarByClass(Driver.class);

		// setting the input path which points to the path of the hypergraph
		// file path on the HDFS.
		FileInputFormat.addInputPath(computeDoAJob, new Path(inputPath));

		// setting the output path where the output will be written
		FileOutputFormat.setOutputPath(computeDoAJob, new Path(
				intermediateOutputPath));

		// setting the job name.
		computeDoAJob.setJobName("Compute_Degree_Of_Attention_Job_"
				+ System.currentTimeMillis());

		computeDoAJob.setInputFormatClass(TextInputFormat.class);
		computeDoAJob.setOutputFormatClass(TextOutputFormat.class);

		computeDoAJob.setMapperClass(NodeDegreeOfAttentionMapper.class);
		computeDoAJob.setReducerClass(NodeDegreeOfAttentionReducer.class);

		computeDoAJob.setMapOutputKeyClass(IntWritable.class);
		computeDoAJob.setMapOutputValueClass(IntWritable.class);

		computeDoAJob.setNumReduceTasks(8);

		computeDoAJob.setOutputKeyClass(Text.class);
		computeDoAJob.setOutputValueClass(Text.class);

		Configuration conf = new Configuration();
		try {
			FileSystem hdfs = FileSystem.get(conf);
			FileUtil.copyMerge(hdfs, new Path(intermediateOutputPath), hdfs,
					new Path(outputPath), false, conf, null);
			DistributedCache.addCacheFile(new URI(outputPath), conf);
		} catch (IOException e) {
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		computeDoAJob.waitForCompletion(true);
	}
}
