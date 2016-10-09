package DataInput;

import java.util.Comparator;
import java.util.List;


public class DataComparator implements Comparator<Node_Data>{
	public int dim=0;
	/**
	 * 输入维度构造器
	 * @param dim 需要比较的维度
	 */
	public DataComparator(int dim) {
		this.dim=dim;
	}
	
	@Override
	public int compare(Node_Data o1, Node_Data o2) {
		if (o1.getData().get(dim)>o2.getData().get(dim)){
			return 1;
		}
		else if (o1.getData().get(dim)<o2.getData().get(dim)) {
			return -1;
		}
		else {
			return 0;
		}		
	}			
}
