package DataInput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TrainItems {
	public ArrayList<Node_Data> trainItems = new ArrayList<Node_Data>();
	public TrainItems(){}
	public TrainItems(File file){
		CalculateTrain(file);
	}
	/**
	 * 解析文件获取train数据
	 * */
	public void CalculateTrain(File file){		
		BufferedReader br = null;
		try {
			 InputStream in = new FileInputStream(file);
			 InputStreamReader inr = new InputStreamReader(in);	    	 
	    	 br = new BufferedReader(inr);  
	         String tempString = br.readLine();  
	         System.out.println("train.csv is reading..."); 
	         int line = 0;
	         while ((tempString = br.readLine()) != null) {  	               
	             Node_Data item = new Node_Data();
	        	 String[] temp = tempString.split(",");
	             item.setLabel(Integer.parseInt(temp[0]));
	             ArrayList<Integer> data = new ArrayList<Integer>();
	             for(int i=1;i<temp.length;i++){
	            	 //String s = temp[i].equals("0") ? "0" : "1";
	            	 data.add(Integer.parseInt(temp[i]));
	             }
	             item.setData(data);
	             trainItems.add(item);
	         }  
	         inr.close(); 
	         br.close();
	         System.out.println("train.csv read over");
	     } catch (Throwable e) {  
	         e.printStackTrace();  
	     } 
		
	}
	public ArrayList<Node_Data> getTrainItems() {
		return trainItems;
	}
	public void setTrainItems(ArrayList<Node_Data> trainItems) {
		this.trainItems = trainItems;
	}
	
}
