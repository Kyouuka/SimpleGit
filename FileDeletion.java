package fileoperation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileDeletion {
    /**
     * Delete file.
     * @param file
     */
	public static void deleteFile(File file){
        if(!file.exists())return;
        if(file.isFile())file.delete();
        else{
            File[] fileList = file.listFiles();
            for(int i = 0; i < fileList.length; i++){
                deleteFile(fileList[i]);
            }
        }
    }
    
	/**
	 * Delete file.
	 * @param path
	 */
    public static void deleteFile(String path){
        deleteFile(new File(path));
    }
    
    /**
     * Delete the content of a file.
     * @param file
     */
    public static void deleteContent(File file) {
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
