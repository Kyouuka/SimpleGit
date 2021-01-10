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

public class JitStatus {
    private String branchName;
    private ArrayList<String> modifiedList = new ArrayList<>();
    private ArrayList<String> createdList = new ArrayList<>();
    private ArrayList<String> modifiedNotStaged = new ArrayList<>();
    private ArrayList<String> createdNotStaged = new ArrayList<>();
    private LinkedList<String[]> indexList;
    private String commitId;


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
     * git status command, first compare the stage then the working directory with last commit.
     * @throws IOException
     */
    public void showStatus() throws IOException {
        Commit commit = new Commit(commitId);
        showStatusCache();
        System.out.println("On branch "+branchName);
        System.out.println("Changes to be committed: ");
        printList(modifiedList);
        printList(createdNotStaged);
        Tree tree = new Tree(commit.getTree());
        showStatusWorkingArea(Repository.getWorkTree(), tree);

        System.out.println("Changes not staged for commit:");
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
        Tree tree = new Tree(commit.getTree());
        System.out.println("tree: " + commit.getTree());
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
            if(fs[i].getName().equals(".jit")){continue;}
            temp = isExist(fs[i], list);
            File tempFile = fs[i];
            if(temp!=-1){
                if(fs[i].isFile()) { 
                    File hashFileAft = new File(Repository.getGitDir() + File.separator + "objects" +  File.separator + FileReader.readObjectKey(list.get(temp)));
                    FileInputStream is = new FileInputStream(hashFileAft);
                    byte[] output = ZLibUtils.decompress(is);
                    is.close();
                    String aftStr = new String(output);
                    String befStr = GitObject.getValue(fs[i]);
                    if (!aftStr.equals(befStr)) {
                        modifiedNotStaged.add(fs[i].getPath());
                    }
                }else{
                    Tree treeChild = new Tree(FileReader.readObjectKey(list.get(temp)));
                    showStatusWorkingArea(path + File.separator + fs[i].getName(), treeChild);
                }
                break;
            }else{
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
        String[] route = record[1].split(File.separator);
        for(int i = 1; i < route.length; i++){
            String value = tree.getValue();
            ArrayList<String> list = FileReader.readByBufferReader(value);
            for (int j = 0; j < list.size(); j++){
                String name = FileReader.readObjectFileName(list.get(j));
                if (name.equals(".jit")){continue;}
                if(name.equals(route[i])){ 
                    if(i == route.length - 1){ 
                        File hashFileBef = new File(Repository.getGitDir() + File.separator + "objects" + File.separator + FileReader.readObjectKey(list.get(j)));
                        FileInputStream is = new FileInputStream(hashFileBef);
                        byte[] output = ZLibUtils.decompress(is);
                        is.close();
                        String befStr = new String(output);
                        File after = new File(record[1]);
                        String aftStr = new GitObject().getValue(after);
                        if(!befStr.equals(aftStr)){
                            modifiedList.add(record[1]);
                        }
                        break;
                    }else{
                        tree = new Tree(FileReader.readObjectKey(list.get(j)));
                        break; 
                    }
                }
            }
            if(i == route.length - 1){ 
                createdList.add(record[1]);
            }
        }
    }

}
