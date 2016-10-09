package DataInput;

import java.util.ArrayList;
import java.util.Comparator;

public class DataComparator2 implements Comparator<ArrayList<Double>>{
	public int dim=0;
	/**
	 * 输入维度构造器
	 * @param dim 需要比较的维度
	 */
	public DataComparator2(int dim) {
		this.dim=dim;
	}
	
	@Override
	public int compare(ArrayList<Double> o1, ArrayList<Double> o2) {
		if (o1.get(dim)>o2.get(dim)){
			return 1;
		}
		else if (o1.get(dim)<o2.get(dim)) {
			return -1;
		}
		else {
			return 0;
		}		
	}			
}
