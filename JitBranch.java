package core;

import fileoperation.FileStatus;
import gitobject.GitObject;
import repository.Repository;

import java.io.File;
import java.io.IOException;

import branch.Branch;

public class JitBranch {
    /**
     * Create a new branch and point to the given commitId
     * @param branchname
     * @param commitId
     */
    public static void branch(String branchname, String commitId) throws IOException {
        branch.Branch branch = new branch.Branch(branchname, commitId);
        branch.writeBranch();
    }
    /**
     * List all the local branch
     */
    public static void branch() throws IOException {
        if(!FileStatus.branchExist("master")){
            return;
        }
        branch.Branch curBranch = getCurBranch();
        File[] branchList = new File(Repository.getGitDir() + File.separator + "refs"
                + File.separator + "heads").listFiles();
        for(int i = 0; i < branchList.length; i++){
            if(curBranch.getBranchName().equals(branchList[i].getName())){
                System.out.print("* ");
            }
            else{
                System.out.print("  ");
            }
            System.out.println(branchList[i].getName());
        }

    }

    /**
     * Create a new branch, but stay with the current branch.
     * @param branchname
     * @throws IOException
     */
    public static void branch(String branchname) throws IOException {
        if(!FileStatus.branchExist("master")){
            System.out.println("Not a valid object name: 'master'.");
            return;
        }
        branch.Branch master = new branch.Branch("master");
        branch.Branch branch = new branch.Branch(branchname, master.getCommitId());
        branch.writeBranch();
    }

    /**
     * Delete an existed branch.
     * @param branchname
     * @throws IOException
     */
    public static void deleteBranch(String branchname) throws IOException {
        if(!FileStatus.branchExist(branchname)){
            System.out.println("error: branch " + "'" + branchname + "'" + " not found.");
        }
        if (branchname.equals(getCurBranch().getBranchName())){
            System.out.println("error: Cannot delete branch " + "'" + branchname + "'" + "checked out at " + Repository.getWorkTree());
        }
        File branch = new File(Repository.getGitDir() + File.separator + "refs" + File.separator +
                "heads" + File.separator + branchname);
        branch.delete();
    }
    
    /**
     * Get the present branch.
     * @return
     * @throws IOException
     */
    public static Branch getCurBranch() throws IOException {
        File HEAD = new File(Repository.getGitDir()+File.separator+"HEAD");
        String branchName = GitObject.getValue(HEAD).substring(16).replace("\n","");
        Branch branch = new Branch(branchName);
        return branch;
    }
}
