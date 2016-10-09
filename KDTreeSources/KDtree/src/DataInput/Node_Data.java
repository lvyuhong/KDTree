package DataInput;

import java.util.ArrayList;
import java.util.List;
/**
 * KD树节点数据
 * 不直接存储数据，
 * 而是存储data_set的行
 * */
public class Node_Data {
	private ArrayList<Integer> data = new ArrayList<Integer>();
	private int label;
	public Node_Data(){}
	public Node_Data(ArrayList<Integer> data,int label){
		this.data = data;
		this.label = label;
	}
	public ArrayList<Integer> getData() {
		return data;
	}
	public void setData(ArrayList<Integer> data) {
		this.data = data;
	}
	public int getLabel() {
		return label;
	}
	public void setLabel(int label) {
		this.label = label;
	}			
}
