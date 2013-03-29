package infrastructure;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

public class NodeIdPair implements Writable, WritableComparable<NodeIdPair>,
		Serializable {

	private int node1Id;

	private int node2Id;

	public int getNode1Id() {
		return node1Id;
	}

	public int getNode2Id() {
		return node2Id;
	}

	public NodeIdPair(int n1Id, int n2Id) {
		if (n1Id - n2Id < 0) {
			this.node1Id = n1Id;
			this.node2Id = n2Id;
		} else {
			this.node1Id = n2Id;
			this.node2Id = n1Id;
		}
	}

	public void setNode1Id(int node1Id) {
		if (this.node2Id - node1Id < 0) {
			this.node1Id = this.node2Id;
			this.node2Id = node1Id;
		} else {
			this.node1Id = node1Id;
		}
	}

	public void setNode2Id(int node2Id) {
		if (this.node1Id - node2Id > 0) {
			this.node1Id = node2Id;
			this.node2Id = this.node1Id;
		} else {
			this.node2Id = node2Id;
		}
	}

	@Override
	public void readFields(DataInput dataInput) throws IOException {
		int n1 = dataInput.readInt();
		int n2 = dataInput.readInt();
		if (n1 - n2 < 0) {
			this.node1Id = n1;
			this.node2Id = n2;
		} else {
			this.node1Id = n2;
			this.node2Id = n1;
		}
	}

	@Override
	public void write(DataOutput dataOutput) throws IOException {
		dataOutput.writeInt(node1Id);
		dataOutput.writeInt(node2Id);
	}

	@Override
	public int compareTo(NodeIdPair otherNodeIdPair) {
		if (this.node1Id - otherNodeIdPair.getNode1Id() == 0) {
			if (this.node2Id - otherNodeIdPair.getNode2Id() == 0) {
				return 0;
			} else if (this.node2Id - otherNodeIdPair.getNode2Id() < 0) {
				return -1;
			}
			return 1;
		} else if (this.node1Id - otherNodeIdPair.getNode1Id() < 0) {
			return -1;
		} else {
			return 1;
		}
	}

}
