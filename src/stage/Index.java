package stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import core.JitDiff;
import repository.Repository;
import fileoperation.*;
import gitobject.*;

public class Index {
	private LinkedList<String[]> indexList = new LinkedList<>();	//storing file records. String[] includes blob key, file path and time stamp.
																	//The time stamp is only used for comparison and cannot be printed in jit command. 
	private static String path = Repository.getGitDir(); 			//repo path
	
	/**
	 * Constructor. Creating index file in repo if it doesn't exist. Otherwise load its content.
	 * @throws IOException
	 */
	public Index() throws IOException {
		if(!new File(path + File.separator + "index").exists()) {
			FileCreation.createFile(path, "index", "");
		}else {
			indexList = loadIndex();
		}	
	}
	
	public LinkedList<String[]> getIndexList() {
		return indexList;
	}
	
	/**
	 * Load the index to `.jit/index`
	 * @throws IOException
	 * @return LinkedList<String[]> list
	 */
	public LinkedList<String[]> loadIndex() throws IOException {
		if(new File(path + File.separator + "index").exists()) {
			File indexFile = new File(path + File.separator + "index");
			FileInputStream is = new FileInputStream(indexFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
	        LinkedList<String[]> list =  new LinkedList<>();
	        try{
	            String line = br.readLine();
	            while (line != null){
	            	String[] arr = line.split(" ");
	                list.add(arr);
	                line = br.readLine();
	            }
	        }catch (IOException e){
	            e.printStackTrace();
	        }finally {
	            try {
	                br.close();
	                is.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return list;
		}else {
			throw new IOException("Index file doesn't exists.");
		}
	}
	
	/**
	 * Save the index to `.jit/index` by row
	 * @throws IOException 
	 */
	public void writeIndex() throws IOException {
		if(new File(path + File.separator + "index").exists()) {
			File indexFile = new File(path + File.separator + "index");
	        FileWriter fw = new FileWriter(indexFile);
	        for(String[] arr : indexList) {
	        	String str = arrayToString(arr);
	            fw.write(str);
	            fw.write("\n");
	        }
	        fw.flush();
	        fw.close();
		}
	}
	
	/**
	 * Insert a file record into indexList.
	 * @param file
	 * @throws Exception
	 */
	public void insertIndex(File file) throws Exception {
		//Generate a new record
		String[] record = new String[3];
		Blob blob = new Blob(file);
		String workDir = Repository.getWorkTree(); 
		String fileName = file.getPath().substring(workDir.length() + 1);
		
		Date date = new Date();
		//long time = date.getTime();
		long time = file.lastModified();
		String timeStr = String.valueOf(time); 
		
		record[0] = blob.getKey();
		record[1] = fileName;
		record[2] = timeStr;
		indexList.add(record);
		
		//Save file(blob object) into local repo.
		blob.compressWrite();
	}	
	
	/**
	 * Check whether the file is in index or not and return the list index. '-1' means no such file in index.
	 * @param file
	 * @return int
	 */
	public int inIndex(File file) {
		String fileName = file.getPath();
		for(int i = 0; i < indexList.size(); i++) {
			if(indexList.get(i)[1].equals(fileName)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Remove a file record from the index
	 * @param file
	 */
	public void removeFileRecord(File file) {
		int idx = inIndex(file);
		if(idx != -1) {
			indexList.remove(idx);
		}
	}
	
	/**
	 * Update the time stamp of an unchanged file.
	 * @param file
	 * @throws IOException 
	 */
	public void updateIndex(File file) throws IOException {
		int idx = inIndex(file);
		if(idx != -1) {
			String[] record = indexList.get(idx);
			String blobId = record[0];

			if(!JitDiff.diffExist(blobId, file)) {
				String timeStr = String.valueOf(file.lastModified());
				indexList.get(idx)[2] = timeStr;
			}
		}
	}

	/**
	 * Clear the stage.
	 */
	public void clear() {
		File file = new File(path + File.separator + "index");
		FileDeletion.deleteContent(file);
	}
    
    /**
     * Convert String[] to String, for writing a record into index file.
     * @param String[] arr
     * @return String res
     */
    public String arrayToString(String[] arr) {
    	StringBuilder sb = new StringBuilder();
    	for (int i = 0; i < arr.length; i++) {
    	    sb.append(arr[i] + " ");
    	}
    	String res = sb.toString().substring(0, sb.length() - 1);
    	return res;
    }
}
