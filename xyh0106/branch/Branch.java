package branch;
import fileoperation.FileReader;
import fileoperation.FileStatus;
import gitobject.Commit;
import gitobject.GitObject;
import repository.Repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Branch {
    String branchName = "master";
    protected String commitId;

    public String getBranchName(){
        return branchName;
    }
    public String getCommitId(){
        return commitId;
    }

    public Branch(String branchName, String commitId){
        this.branchName = branchName;
        this.commitId = commitId;
    }

    /**
     * Constructor. Construct a new object with existed branch. Used in the checkout command.
     * @param branchName
     * @throws IOException
     */
    public Branch(String branchName) throws IOException {

        if(!FileStatus.branchExist(branchName)){
            System.out.println("This branch doesn't exist");
            return;
        }
        String path = Repository.getGitDir() + File.separator + "refs" + File.separator + "heads";
        this.branchName = branchName;
        File currBranchFile = new File(path + File.separator + branchName);
        if(currBranchFile.isFile()){
            commitId = GitObject.getValue(currBranchFile);
        }
        else{
            throw new IOException("This branch doesn't exist");
        }
    }

    /**
     * Make the current branch point to a new commit.
     * @param commitId
     */
    public void update(String commitId){
        this.commitId = commitId;
    }


    /**
     * Alter the HEAD file, switch to another branch.
     * @throws IOException
     */
    public void writeHead() throws IOException {

        File HEAD = new File(Repository.getGitDir() + File.separator + "HEAD");
        FileWriter fileWriter = new FileWriter(HEAD);
        fileWriter.write("ref: refs/heads/" + branchName);
        fileWriter.close();
    }

    /**
     * Write commitId in the branch file.(for example, "refs/heads/master")
     * @throws IOException
     */
    public void writeBranch() throws IOException {
        String path = Repository.getGitDir() + File.separator + "refs" + File.separator + "heads";
        File currBranchFile = new File(path + File.separator + branchName);
        FileWriter fileWriter = new FileWriter(currBranchFile);
        fileWriter.write(commitId);
        fileWriter.close();
    }


    /**
     * Get the commit history of the current branch, limit the number of records.
     */
    public void getCommitHistory(int number) throws Exception {
        new Commit(commitId).logCommitHistory(number);
    }

    /**
     * Get the commit history of the current branch
     */
    public void getCommitHistory() throws Exception {
        new Commit(commitId).logCommitHistory();
    }
    /**
     * Get the commit history of the current branch after a commit.
     */
    public void getCommitHistory(String commitId) throws Exception {
        new Commit(this.commitId).logCommitHistory(commitId);
    }
    /**
     * Reset the branch to a certain commit.
     * @param commitId
     * @throws IOException
     */
    public void reset(String commitId) throws IOException {
        update(commitId);
        writeBranch();
    }

}
