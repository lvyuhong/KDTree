package DataInput;

import java.util.ArrayList;
import java.util.List;

public class DataMap {
	public List<Integer> data = new ArrayList<Integer>();
	public int label;
	public DataMap(List<Integer> data,int label){
		this.data = data;
		this.label = label;
	}
}
