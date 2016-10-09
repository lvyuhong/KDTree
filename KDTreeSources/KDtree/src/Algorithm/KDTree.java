package Algorithm;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import DataInput.DataComparator;
import DataInput.DataComparator2;
import DataInput.DisMap;
import DataInput.KDNode;
import DataInput.Node_Data;
import DataInput.RangeData;
import DataInput.RangeSpace;
import DataInput.TestData;
import DataInput.TrainData;
import DataInput.TrainItems;

public class KDTree {
	private int dimension;	//表示数组有多少维
	private KDNode rootNode; //表示根节点	
	private ArrayList<Node_Data> trainlist=new ArrayList<Node_Data>(); //训练数据集，用来建树的数据集
	private TrainData train=new TrainData(); //测试数据集，用来搜索K近邻的数据集
	private TestData test=new TestData(); //测试数据集，用来搜索K近邻的数据集
	private ArrayList<Integer> result = new ArrayList<Integer>(); //存储测试集的label
	ArrayList<Integer> colIndex = new ArrayList<Integer>();//按照从方差大到小的顺序存放属性的下标
	RangeSpace rangeMap;
	/**
	 * 数构造器
	 * @param list 需要构造KD树的数据
	 */
	public KDTree(File trainfile,File testfile){
		TrainItems trainItems = new TrainItems(trainfile);
		this.trainlist = trainItems.getTrainItems();
		//this.train=new TrainData(trainfile);
		this.test = new TestData(testfile);
		dimension=trainlist.get(0).getData().size();
		rangeMap = new RangeSpace(dimension,0,255);
		//rootNode.setRange(rangeMap);
	}
	/**@title KDTreeMiner
	 * @description 住挖掘过程
	 * */
	public void KDTreeMiner(){
		//计算各属性方差的大小		
		ArrayList<ArrayList<Double>> sdv = CalSDV(trainlist);
		DataComparator2 comparator = new DataComparator2(0);
		Collections.sort(sdv,comparator);
		Collections.reverse(sdv);		
		for(int i=0;i<sdv.size();i++){
			double index = sdv.get(i).get(1);
			colIndex.add((int)index);
		}
		//按照属性的方差大小生成KD树
		System.out.println("开始构建KD树...");
		setRootNode(BuildKDTree(trainlist,0,rangeMap));
		System.out.println("构建KD树完成");
		//对KD树进行K近邻搜索	
		System.out.println("开始搜索最近邻...");
		for(int i=0;i<test.getTestData().size();i++){
			ArrayList<Integer> data = test.getTestData().get(i);
			int label = findKNearst(data);
			result.add(label);
			System.out.println("计算第"+i+"条数据");
		}
		System.out.println("搜索最近邻结束");		
		
	}
	
	/**@title CalSDV
	 * @description 计算各属性方差大小
	 * @return <sdv,id>
	 * */
	public ArrayList<ArrayList<Double>> CalSDV(ArrayList<Node_Data> list){
		ArrayList<ArrayList<Double>> SDVlist = new ArrayList<ArrayList<Double>>(); //(value,id)
		for(int i=0;i<dimension;i++){			
			ArrayList<Double> item = new ArrayList<Double>();
			DescriptiveStatistics statistics = new DescriptiveStatistics();
			for(int j=0;j<list.size();j++){
				statistics.addValue(list.get(j).getData().get(i));
			}
			double dv = statistics.getStandardDeviation();
			item.add(dv);
			item.add((double)i);
			SDVlist.add(item);
		}
		return SDVlist;
	}
	/**
	 * @description 创建树
	 * @param train 数据集， dim colIndex.get(0)
	 * @return KDNode*/
	public KDNode BuildKDTree(ArrayList<Node_Data> train,int index,RangeSpace rangeMap){
		//找出最大维
		int dim = colIndex.get(index);				
		//找出这一维的中位数
		DataComparator comparator = new DataComparator(dim);
		Collections.sort(train,comparator);
		int split = (train.size()-1)/2;
		int splitValue = train.get(split).getData().get(dim);
		//分为左右2个节点
		ArrayList<Node_Data> sameSplitElement=new ArrayList<>(); //等于中位数的数据列表
		ArrayList<Node_Data> leftSplitElement=new ArrayList<>();//小于中位数的数据列表
		ArrayList<Node_Data> rightSplitElement=new ArrayList<>();//大于中位数的数据列表		
		RangeData range = new RangeData();
		for(Node_Data data : train){
			if(data.getData().get(dim)<splitValue){
				leftSplitElement.add(data);
				range = new RangeData(0, splitValue);
			}else if(data.getData().get(dim)>splitValue){
				rightSplitElement.add(data);
				range = new RangeData(splitValue, 255);
			}else {
				sameSplitElement.add(data);
			}
		}
		RangeSpace LeftRange = calRange(leftSplitElement);
		RangeSpace RightRange = calRange(rightSplitElement);
		KDNode node = new KDNode(sameSplitElement,leftSplitElement, rightSplitElement, dim, splitValue,rangeMap);				
		//递归建树
		int nextIndex = index+1;
		if(nextIndex>=colIndex.size()){
			nextIndex = 0;
		}
		if(leftSplitElement.size() > 1){
			node.setLeft(BuildKDTree(leftSplitElement,nextIndex,LeftRange));
			KDNode left = node.getLeft();
			left.setFather(node);
			left.flag = 0;
			leftSplitElement.clear();
		}
		if(rightSplitElement.size() > 1){
			node.setRight(BuildKDTree(rightSplitElement, nextIndex,RightRange));
			KDNode right = node.getRight();
			right.setFather(node);
			right.flag = 1;
			rightSplitElement.clear();
		}
		return node;
	}
	/**@title calRange
	 * @description 计算数据集的空间
	 * @return RangeSpace*/
	public RangeSpace calRange(ArrayList<Node_Data> nodelist){
		RangeSpace range = new RangeSpace();
		for(int i=0;i<dimension;i++){			
			int min = Integer.MAX_VALUE;
			int max = Integer.MIN_VALUE;
			for(int j=0;j<nodelist.size();j++){
				int data = nodelist.get(j).getData().get(i);
				if(data < min){
					min = data;
				}
				if(data > max){
					max = data;
				}
			}
			RangeData rangeData = new RangeData(min,max);
			range.rangeMap.put(i, rangeData);
		}
		return range;
	}
	/**@title findKNearst
	 * @description 计算K近邻节点
	 * @return*/
	public int findKNearst(ArrayList<Integer> data){
		ArrayList<KDNode> nodelist = new ArrayList<KDNode>();
		KDNode node=findNodeAndRecord(data, rootNode,nodelist);//最终的叶子节点
		KDNode nearestNode = node;
		int label = 0;
		DisMap mindis = calDistance(data, node.nodes);
		while(node.getFather() != null){
			KDNode father = node.getFather();//node的父节点
			//找到当前最近节点
			DisMap dis = calDistance(data,father.nodes);
			if(dis.getDistance()<mindis.getDistance()){
				nearestNode = father;
				mindis = dis;
				label = mindis.getLable();
			}
			//找到兄弟节点
			KDNode brother = new KDNode();
			if(node.flag==0){
				brother = father.getRight();
			}else {
				brother = father.getLeft();
			}
			//判断以data的中心，mindis为半径的区域（range1）是否和brother区域（range2）相交
			if(brother != null){
				boolean isInsert = isInsert(brother, data, mindis.getDistance());
				if(isInsert){
					KDNode insertNode = brother;
					while(insertNode != null){										
						boolean isLeft = isInsert(insertNode.getLeft(),data,mindis.getDistance());
						boolean isRight = isInsert(insertNode.getRight(),data,mindis.getDistance());
						if(isLeft){
							insertNode = insertNode.getLeft();
						}else if(isRight){
							insertNode = insertNode.getRight();
						}else{
							break;
						}
					}
					mindis = calDistance(data, insertNode.getFather().nodes);								
				}
			}
			node = father;			
		}
		return label;
	}
	/**@title findNodeAndRecord
	 * @description 查找当前数据点在kd树中的位置
	 * @param data 要查找的数据点， node 查找的起始节点
	 * @return node 终节点
	 * */
	public KDNode findNodeAndRecord(ArrayList<Integer> data,KDNode node,ArrayList<KDNode> nodelist){
		while(true){
			nodelist.add(node);
			if (data.get(node.dim)>=node.splitValue) {
				if(node.getRight()==null){
					break;
				}
				node=node.getRight();
			}
			else{
				if(node.getLeft()==null){
					break;
				}
				node=node.getLeft();
			}
		}
		return node;
	}
	/**@title
	 * @description 计算2点之间的欧拉距离
	 * @return DisMap*/
	public DisMap calDistance(ArrayList<Integer> data,ArrayList<Node_Data> nodes){
		double min=Double.MAX_VALUE;
		int label=0;
		for(int k=0;k<nodes.size();k++){
			double d = 0;
			ArrayList<Integer> data2 = nodes.get(k).getData();
			for(int i=0;i<data.size();i++){
				d += Math.pow((data.get(i)-data2.get(i)), 2);
			}
			if(d<min){
				min = d;
				label = nodes.get(k).getLabel();
			}
		}
		min = Math.sqrt(min);
		DisMap disMap = new DisMap(min, label);
		return disMap;
	}
	/**@title isInsert
	 * @description 是否相交的判断，如果相交，就返回另一边的节点进行求解
	 *              通过判断data到node分割平面的距离来判断是否相交
	 * @param node 判断是否与node节点相交，data 要计算距离的数据，mindis当前最小距离
	 * @return*/
	public boolean isInsert(KDNode node,ArrayList<Integer> data,double mindis){
		if (node != null) {
			int dim = node.dim;
			double dis = Math.abs(node.getFather().splitValue - data.get(dim));
			if(dis >= mindis){
				return false;
			}
			else {
				return true;
			}
		}else{
			return false;
		}
	}
	/**@title isInterset2
	 * @description 是否相交的判断，如果相交，就返回另一边的节点进行求解
	 *              通过连接2个中线点，来判断
	 * @param data 要计算距离的数据，mindis当前最小距离，range 兄弟节点的空间范围
	 * @return boolean*/
	public boolean isInterset2(ArrayList<Integer> data,double mindis,RangeSpace range){
		//找到range的中心点
		ArrayList<Double> rangeCenter = new ArrayList<Double>();
		for(int i=0;i<dimension;i++){
			RangeData rangeData = range.rangeMap.get(i);
			double mindle = (rangeData.min+rangeData.max)/2;
			rangeCenter.add(mindle);
		}
		//找到data和rangecenter两点连线上距离data点的距离为mindis的点(point)
		ArrayList<Double> point = new ArrayList<Double>();
		double length = 0;//线段长度
		for(int i=0;i<dimension;i++){
			length += Math.pow((data.get(i)-rangeCenter.get(i)), 2);
		}
		length = Math.sqrt(length);
		if(length==0){
			return true;
		}
		for(int i=0;i<dimension;i++){
			double x = (rangeCenter.get(i)-data.get(i))*mindis/length;
			point.add(x);
		}
		//判断point点是否在range空间内
		int i=0;
		while(i<dimension){
			if(point.get(i)<range.rangeMap.get(i).max && point.get(i)>range.rangeMap.get(i).min){
				i++;
			}
		}
		if(i==dimension){
			return true;
		}else {
			return false;
		}
	}
	/*****************************getter and setter******************************/
	public KDNode getRootNode() {
		return rootNode;
	}
	public void setRootNode(KDNode rootNode) {
		this.rootNode = rootNode;
	}
	
	public int getDimension() {
		return dimension;
	}
	public void setDimension(int dimension) {
		this.dimension = dimension;
	}
	public ArrayList<Integer> getResult() {
		return result;
	}
	public void setResult(ArrayList<Integer> result) {
		this.result = result;
	}
	/******************************测试代码**************************************/
	public static void main(String[] args){
		File trainfile = new File("data/train_sub.csv");
		File testfile = new File("data/test_sub.csv");
		KDTree tree = new KDTree(trainfile, testfile);
		tree.KDTreeMiner();		
	}
}
