package fileoperation;

import java.io.File;

public class FileDeletion {
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
    public static void deleteFile(String path){
        deleteFile(new File(path));
    }
}
