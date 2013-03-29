package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NodeDegreeOfAttentionReader {

	private static int sizeOfRecord = 16;

	/**
	 * 
	 * This function provides a way of accessing the degree of attention of a
	 * node in O(1) time. The file contains a sorted list of node ids ranging
	 * from 1 to maxNumberOfNodes and with each node is associated a double
	 * value ^.^^^^^. Works only with fixed sized records because random access
	 * to file is prohibited otherwise.
	 * 
	 * @param nodeId
	 *            - ranges from 1 to number of authors
	 * @return
	 * @throws IOException
	 */
	public static double getDegreeOfAttentionOfNode(int nodeId,
			String nodeIdDegreeOfAttnetionFilePath) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(
				nodeIdDegreeOfAttnetionFilePath));
		char[] buffer = new char[sizeOfRecord];
		reader.read(buffer, nodeId - 1, sizeOfRecord);
		return Integer.parseInt(new String(buffer).substring(8, 15));
	}

}
