package core;

import branch.Branch;
import fileoperation.FileDeletion;
import gitobject.Blob;
import gitobject.Commit;
import gitobject.GitObject;
import gitobject.Tree;
import stage.Index;
import repository.Repository;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Core {
    /**
     * Create a .jit repository in the workTree. If a .jit directory already exists, then remove and recreate one.
     * @param workTree
     * @throws IOException
     */
    public static void init(String workTree) throws IOException {

        Repository repo = new Repository(workTree);
        if(repo.exist()){
            if(repo.isDirectory()){
                FileDeletion.deleteFile(repo.getGitDir());
            }
            else if(repo.isFile()){
                throw new IOException(".jit is a file, please check");
            }
        }
        repo.createRepo();
        System.out.println("Jit repository has been initiated successfully.");
    }

    /**
     * Get the hash of a given file. If "-w" is given(write == true), then write it in the object.
     * @param file
     * @param write
     * @throws Exception
     */
    public static void hashObject(File file, boolean write) throws Exception {
        Blob blob = new Blob(file);
        System.out.println(blob.getKey());
        if(write){
            blob.compressWrite();
        }
    }




    public static Branch getCurBranch() throws IOException {
        File HEAD = new File(Repository.getGitDir()+File.separator+"HEAD");
        String branchName = GitObject.getValue(HEAD).substring(16).replace("\n","");
        Branch branch = new Branch(branchName);
        return branch;
    }
    public static Commit getCurCommit() throws IOException{
        return new Commit(getCurBranch().getCommitId());
    }
    public static Tree getCurTree() throws IOException{
        return new Tree(getCurCommit().getTree());
    }
    public static String buildTree(LinkedList<String[]> indexList){
        for(int i = 0; i < indexList.size(); i++){

        }
        return "";
    }


    public static void commitToIndex(String commitId) throws IOException {
        Commit commit  = new Commit(commitId);


    }




    public void Log(){}




    public void Status(){}
}
