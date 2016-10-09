package DataInput;

/**
 * 用于存储距离的数据格式*/
public class DisMap {
	double distance;
	int lable;
	public DisMap(double d,int l){
		this.distance = d;
		this.lable = l;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public int getLable() {
		return lable;
	}
	public void setLable(int lable) {
		this.lable = lable;
	}
	
}
