package fileoperation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileCreation {
    public static void createFile(String parentPath, String filename, String text) throws IOException {
        if(!new File(parentPath).isDirectory()){
            throw new IOException("The path doesn't exist!");
        }

        File file = new File(parentPath + File.separator + filename);
        if(file.isDirectory()) {
            throw new IOException(filename + " already exists, and it's not a file.");
        }
        if(file.exists()) {
            throw new IOException(filename + " already exists!");
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(text);
        fileWriter.close();

    }
    public static void createDirectory(String parentPath, String... paths) throws IOException{
        if(!new File(parentPath).isDirectory()){
            throw new IOException("The path doesn't exist!");
        }
        String path = parentPath;
        FileDeletion.deleteFile(path + File.separator + paths[0]);
        for(int i = 0; i < paths.length; i++) {
            path += File.separator + paths[i];
        }
        new File(path).mkdirs();
    }
}
