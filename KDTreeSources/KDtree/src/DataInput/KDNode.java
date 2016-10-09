package DataInput;

import java.util.ArrayList;
import java.util.Arrays;

public class KDNode {	
	private KDNode left=null; //kd树的左节点	
	private KDNode right=null; //kd树的右节点
	private KDNode father = null;; // 父节点
	private RangeSpace rangeMap = new RangeSpace(); //节点的空间范围
	public int flag; //0:左节点，1:右节点
	public int dim; //表示KD树的分割维度	
	public double splitValue; //表示KD树的分割维度的值	
	public ArrayList<Node_Data> nodes; //中位值数据点
	public ArrayList<Node_Data> leftUnClassifyNode; //左边尚未分割的值	
	public ArrayList<Node_Data> rightUnClassifyNode; //右边尚未分割的值
	public KDNode(){}
	/**
	 * KDnode节点构造器 
	 * @param node 这个节点的数据
	 * @param left 左边没有分类的数据
	 * @param right 右边还没有分类的数据
	 * @param dim 分类的维度
	 * @param splitValue 分类的数值
	 */
	public KDNode(ArrayList<Node_Data> nodes,ArrayList<Node_Data> left,ArrayList<Node_Data> right,int dim,double splitValue,RangeSpace rangeMap){				
		this.nodes = nodes;
		this.leftUnClassifyNode=left;
		this.rightUnClassifyNode=right;
		this.dim=dim;
		this.splitValue=splitValue;
		this.rangeMap = rangeMap;
	}
	public KDNode getLeft() {
		return left;
	}
	public void setLeft(KDNode left) {
		this.left = left;
	}
	public KDNode getRight() {
		return right;
	}
	public void setRight(KDNode right) {
		this.right = right;
	}
	public KDNode getFather() {
		return father;
	}
	public void setFather(KDNode father) {
		this.father = father;
	}
	public RangeSpace getRange() {
		return rangeMap;
	}
	public void setRange(RangeSpace range) {
		this.rangeMap = range;
	}
	
		
}
