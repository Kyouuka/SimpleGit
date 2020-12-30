package gitobject;



import sha1.SHA1;
import zlib.ZLibUtils;
import fileoperation.FileReader;

import java.io.*;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.util.*;


public class Tree extends GitObject{
    protected ArrayList<GitObject> treeList;
    public void listTree(){

    }
    public ArrayList<GitObject> getTreeList(){
        return treeList;
    }

    public Tree(){
        fmt = "tree";
    }
    public Tree(File file) throws Exception {
        this.treeList = new ArrayList<>();
        this.fmt = "tree";
        this.mode = "040000";
        this.value = "";
        genKey(file);
    }

    /**
     * Construct a tree object with treeId and its path.
     * @param Id
     * @param Id
     * @throws IOException
     */
    public Tree(String Id) throws IOException {
        this.treeList = new ArrayList<>();
        fmt = "tree";
        mode = "040000";
        try{
            File file = new File(path + File.separator + Id);
            if(file.exists()){
                key = Id;
                FileInputStream is = new FileInputStream(file);
                byte[] output = ZLibUtils.decompress(is);
                is.close();
                value = new String(output);
                genTreeList();
            }
            else{
                throw new IOException();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Generate treelist from the value of an existed tree object.
     * @throws IOException
     */
    public void genTreeList() throws IOException {
        ArrayList<String> list = FileReader.readByBufferReader(value);
        for(int i = 0; i < list.size(); i++){

            if(FileReader.readObjectFmt(list.get(i)).equals("blob")){
                Blob blob = new Blob(FileReader.readObjectKey(list.get(i)));
                treeList.add(blob);
            }
            else{
                Tree tree = new Tree(FileReader.readObjectKey(list.get(i)));
                treeList.add(tree);
            }
        }
    }
    /**
     * sort the files in a certain order.
     * @param fs
     * @return List
     */
    public List sortFile(File[] fs){
        List fileList = Arrays.asList(fs);
        Collections.sort(fileList, new Comparator<File>() {
            @Override //表示重写方法
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1; //文件夹优先，-1表示前者小，1表示前者大
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return o1.getName().compareTo(o2.getName());
            }
        });
        return fileList;
    }

    /**
     * Generate the key of a tree object.
     * @param dir
     * @return String
     * @throws Exception
     */
    public String genKey(File dir) throws Exception{
        File[] fs = dir.listFiles();
        List fileList = sortFile(fs);

        for(int i = 0; i < fs.length; i++){
            if(fs[i].isDirectory()){
                Tree tree = new Tree(fs[i]);
                treeList.add(tree);
                tree.compressWrite();
                value += "040000 tree " + tree.genKey(fs[i]) + "\u0020" + fs[i].getName() + "\n";
            }
            else if(fs[i].isFile()){
                Blob blob = new Blob(fs[i]);
                treeList.add(blob);
                blob.compressWrite();
                value += "100644 blob " + blob.genKey(fs[i]) + "\u0020" + fs[i].getName() + "\n";

            }
        }
        key = SHA1.getHash(value);
        return key;
    }
    @Override
    public String toString(){
        return "040000 tree " + key;
    }

}
