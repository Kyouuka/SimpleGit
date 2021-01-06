package core;

import fileoperation.FileReader;
import gitobject.Commit;
import gitobject.GitObject;
import gitobject.Tree;
import repository.Repository;
import zlib.ZLibUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import static core.Diff.diffExist;

public class Status {
    private String branchName;
    private ArrayList<String> modifiedList = new ArrayList<>();
    private ArrayList<String> createdList = new ArrayList<>();
    private ArrayList<String> modifiedNotStaged = new ArrayList<>();
    private ArrayList<String> createdNotStaged = new ArrayList<>();
    private LinkedList<String[]> indexList;
    private String commitId;

    public Status(){}

    public void setBranchName(String branchName){
        this.branchName = branchName;
    }

    public void setIndexList(LinkedList<String[]> indexList){
        this.indexList = new LinkedList<String[]>(indexList);
    }

    public void setCommitId(String commitId){
        this.commitId = commitId;
    }

    public ArrayList<String> getCreatedList() {
        return createdList;
    }

    public ArrayList<String> getCreatedNotStaged() {
        return createdNotStaged;
    }

    public ArrayList<String> getModifiedList() {
        return modifiedList;
    }

    public ArrayList<String> getModifiedNotStaged() {
        return modifiedNotStaged;
    }

    /**
     * git status command, first compare the Cache zone then the working zone with last commit.
     * @throws IOException
     */
    public void showStatus() throws IOException { //输入上一次的commitId，来显示之后的变化
        Commit commit = new Commit(commitId);
        showStatusCache();
        System.out.println("On branch "+branchName);
        System.out.println("Changes to be committed: ");
        printList(modifiedList);
        printList(createdNotStaged);
        Tree tree = new Tree(commit.getTree());
        showStatusWorkingArea(Repository.getWorkTree(), tree);
        //之后要打印两个修改的队列

        System.out.println("Changes not staged for commit:");
        //filter(modifiedList, modifiedNotStaged);
        printList(modifiedNotStaged);
        printList(createdNotStaged);
    }

    /**
     * print a list of file
     * @param list
     */
    public void printList(ArrayList<String> list){
        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }
        return;
    }

    /**
     * show the difference between Cache zone and last commit
     * @throws IOException
     */
    public void showStatusCache() throws IOException {
        Commit commit = new Commit(commitId);
        //System.out.println("value is "+commit.getValue());
        //System.out.println("Tree is      "+ commit.getTree());
        Tree tree = new Tree(commit.getTree());
        for(int i = 0; i < indexList.size(); i++){
            showDiffInCache(tree, indexList.get(i));
        }
    }

    /**
     * Compare a directory with a tree, used for comparing working zone with last commit
     * @param path
     * @param tree
     * @throws IOException
     */
    public void showStatusWorkingArea(String path, Tree tree) throws IOException {
        File dir = new File(path);
        File[] fs = dir.listFiles();
        int temp;
        String value = tree.getValue();
        ArrayList<String> list = FileReader.readByBufferReader(value);
        for(int i = 0; i < fs.length; i++){
            temp = isExist(fs[i], list);
            File tempFile = fs[i];
            if(temp!=-1){
                if(fs[i].isFile()) { //如果是文件，就检查是否修改
                    File hashFileAft = new File(Repository.getGitDir() + File.separator + "objects" +  File.separator + FileReader.readObjectKey(list.get(temp)));
                    FileInputStream is = new FileInputStream(hashFileAft);
                    byte[] output = ZLibUtils.decompress(is);
                    is.close();
                    String aftStr = new String(output);
                    String befStr = GitObject.getValue(fs[i]);
                    if (!aftStr.equals(befStr)) {
                        //if()
                        modifiedNotStaged.add(fs[i].getPath());
                    }
                }else{ //如果是文件夹，则递归调用本方法
                    Tree treeChild = new Tree(FileReader.readObjectKey(list.get(temp)));
                    showStatusWorkingArea(path + File.separator + fs[i].getName(), treeChild);
                }
                break;
            }else{//找不到，说明文件或文件夹是新建的，那么可以直接打印出来文件或文件夹下的所有子文件
                //createdNotStaged.add(fs[i].getPath());
                if(fs[i].isFile()){
                    createdNotStaged.add(fs[i].getPath());}
                else{
                    addFiletoNotStagedList(fs[i]);
                }
            }
        }
    }

    /**
     * add all files under the created directory to createdList
     * @param file
     */
    public void addFiletoNotStagedList(File file){
        File[] fs = file.listFiles();
        for(int i = 0; i < fs.length; i++){
            if(fs[i].isFile()){
                createdNotStaged.add(fs[i].getPath());}
            else{
                addFiletoNotStagedList(fs[i]);}
        }
    }


    /**
     * Judge if the file is in the list of files.
     * if it exists, return index;
     * if not, return -1.
     * @param file
     * @param list
     * @return int
     */
    public int isExist(File file, ArrayList<String> list){

        for(int i = 0; i < list.size(); i++){
            if(FileReader.readObjectFileName(list.get(i)).equals(file.getName())){
                return i;
            }
        }
        return -1;
    }

    /**
     * add all files under the created directory to createdList
     * @param file
     */
    public void addFiletoStagedList(File file){
        if(file.isFile()){
            createdList.add(file.getPath());
        }else{
            File[] fs = file.listFiles();
            for(int i = 0; i < fs.length; i++){
                addFiletoStagedList(fs[i]);
            }
        }
    }

    /**
     * Find out which file is modified and which one is created, add file names to relative lists.
     * @param tree
     * @param record
     * @throws IOException
     */
    public void showDiffInCache(Tree tree, String[] record) throws IOException {
        //ArrayList<GitObject> treelist = tree.getTreeList();
        String[] route = record[1].split(File.separator);//将路径分割，这里开头是根目录，不需要
        for(int i = 1; i < route.length; i++){
            String value = tree.getValue();
            ArrayList<String> list = FileReader.readByBufferReader(value);
            for (int j = 0; j < list.size(); j++){
                String name = FileReader.readObjectFileName(list.get(j));
                if(name.equals(route[i])){ //如果相同路径找到了，那么开始往下递归路径，直到找到文件
                    if(i == route.length - 1){ //最后一位表示文件找到了
                        //File hashFileAft = new File(Repository.getGitDir() + File.separator + "objects" + File.separator +record[0]);
                        File hashFileBef = new File(Repository.getGitDir() + File.separator + "objects" + File.separator + FileReader.readObjectKey(list.get(j)));
                        FileInputStream is = new FileInputStream(hashFileBef);
                        byte[] output = ZLibUtils.decompress(is);
                        is.close();
                        String befStr = new String(output);
                        File after = new File(record[1]);
                        String aftStr = new GitObject().getValue(after);
                        if(befStr.equals(aftStr)){ //如果两个未解压文件是不同的，就需要显示不同

                        }else{
                            modifiedList.add(record[1]);
                        }
                        break;
                    }else{
                        tree = new Tree(FileReader.readObjectKey(list.get(j)));
                        break; //退出本层遍历，向下一层查找
                    }
                }
            }
            if(i == route.length - 1){ //没有找到指定目录下文件名相同的文件，那么表示该文件是被创建的，就输出该文件是被创建的。这里不打印。
                createdList.add(record[1]);
            }
        }
    }

}
