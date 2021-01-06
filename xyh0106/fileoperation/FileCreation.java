package fileoperation;

import gitobject.Blob;
import gitobject.GitObject;
import gitobject.Tree;
import repository.Repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileCreation {
    public static void createFile(String parentPath, String filename, String text) throws IOException {
        if(!new File(parentPath).isDirectory()){
            throw new IOException("The path doesn't exist!");
        }

        File file = new File(parentPath + File.separator + filename);
        if(file.isDirectory()) {
            throw new IOException(filename + " already exists, and it's not a file.");
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

        for(int i = 0; i < paths.length; i++) {
            path += File.separator + paths[i];
        }
        new File(path).mkdirs();
    }
    public static void recoverWorkTree(Tree t, String parentTree) throws IOException {
        ArrayList<String> list = FileReader.readByBufferReader(t.getValue());
        ArrayList<GitObject> treeList = t.getTreeList();
        for(int i = 0; i < list.size(); i++){

            if(FileReader.readObjectFmt(list.get(i)).equals("blob")){
                Blob blob = new Blob(FileReader.readObjectKey(list.get(i)));
                String fileName = FileReader.readObjectFileName(list.get(i));
                createFile(parentTree, fileName, blob.getValue());
            }
            else{
                Tree tree = new Tree(FileReader.readObjectKey(list.get(i)));
                String dirName = FileReader.readObjectFileName(list.get(i));
                createDirectory(parentTree, dirName);
                recoverWorkTree(tree, parentTree + File.separator + dirName);
            }
        }
    }
}
