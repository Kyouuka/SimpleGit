package fileoperation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import gitobject.Blob;
import gitobject.GitObject;
import gitobject.Tree;

public class FileCreation {
	/**
	 * Create a file
	 * @param parentPath
	 * @param filename
	 * @param text
	 * @throws IOException
	 */
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
    
    /**
     * Create a dir.
     * @param parentPath
     * @param paths
     * @throws IOException
     */
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
    
    /**
     * Recover files in working directory from a tree.
     * @param t
     * @param parentTree
     * @throws IOException
     */
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
