package main;

import java.io.File;

import Algorithm.MinerKNN;

public class Test {
	public static void main(String[] args){
		File train = new File("data/train.csv");
		File test = new File("data/test.csv");
		String filepath = "data/result.csv";
		int k=10;
		MinerKNN knn = new MinerKNN(train, test, filepath,k);
		knn.KDTreeAlgo();
		System.out.println("算法结束");
	}
}
