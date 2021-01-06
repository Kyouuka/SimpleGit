package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import core.JitInit;
import stage.*;

public class UnitTestIndex {

	public static void main(String[] args) throws Exception {
		JitInit.init("F:\\\\CoursesFiles\\\\爪哇\\\\test");
		
		//testing initialization
		Index index = new Index();
		
		//testing insertIndex
		File file = new File("F:\\CoursesFiles\\爪哇\\test\\test1.txt");
	    try {
	        FileWriter writer = new FileWriter(file);
	        writer.write("Wubba lubba dub dub");
	        writer.close();
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
		index.insertIndex(file);
		LinkedList<String[]> list = index.getIndexList();
		for(String[] arr : list) {
			System.out.println(Arrays.toString(arr));
		}
		
		//testing writeIndex
		index.writeIndex();
		
		//testing inIndex
		int inIdx = index.inIndex(file);
		System.out.println(inIdx);
		
		//testing removeFileRecord
		index.removeFileRecord(file);
		index.writeIndex();
		
		//testing clear
		index.clear();
		
		//testing loadIndex
		Index index2 = new Index();
		
		//testing updateIndex
	    try {
	        FileWriter writer = new FileWriter(file);
	        try {
	        	TimeUnit.SECONDS.sleep(10);
	        } catch (InterruptedException ie){}
	        
	        writer.write("Wubba lubba dub dub");
	        writer.close();
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
		index2.updateIndex(file);
		index2.writeIndex();
		
		//testing data structure 
		
		LinkedList<String[]> list2 = index2.getIndexList();
		for(String[] arr : list2) {
			System.out.println(Arrays.toString(arr));
		}
		
	}

}
