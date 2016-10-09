package DataInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RangeSpace {
	public Map<Integer, RangeData> rangeMap = new HashMap<Integer, RangeData>();// map<index,range>
	public RangeSpace(){}
	public RangeSpace(Map<Integer, RangeData> map) {
		rangeMap = map;
	}
	public RangeSpace(int dim,int min,int max){
		for(int i=0;i<dim;i++){
			RangeData rangeData = new RangeData(min, max);
			rangeMap.put(i, rangeData);
		}
	}
	public void changeSpace(int index,RangeData range){
		rangeMap.remove(index);
		rangeMap.put(index, range);
	}
	public void addRange(int index,RangeData range){
		rangeMap.put(index, range);
	}
	
}
