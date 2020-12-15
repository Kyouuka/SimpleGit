package gitobject;



import sha1.SHA1;
import zlib.ZLibUtils;

import java.io.*;
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
    public Tree(String path, File file) throws Exception {
        this.treeList = new ArrayList<>();
        this.path = path;
        this.fmt = "tree";
        this.mode = "040000";
        this.value = "";
        genKey(file);
    }
    public Tree(String path, String Id) throws IOException {
        this.path = path;
        fmt = "tree";
        mode = "040000";
        try{
            File file = new File(path + File.separator + Id);
            if(file.exists()){
                key = Id;
                FileInputStream is = new FileInputStream(file);
                byte[] output = ZLibUtils.decompress(is);
                value = new String(output);
            }
            else{
                throw new IOException();
            }
        }catch (Exception e){
            e.printStackTrace();
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
                Tree tree = new Tree(getPath(), fs[i]);
                treeList.add(tree);
                value += "040000 tree " + tree.genKey(fs[i]) + "\u0020" + fs[i].getName() + "\n";
            }
            else if(fs[i].isFile()){
                Blob blob = new Blob(getPath(), fs[i]);
                treeList.add(blob);
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
