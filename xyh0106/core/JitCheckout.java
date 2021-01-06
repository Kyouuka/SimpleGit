package core;

import fileoperation.FileCreation;
import fileoperation.FileDeletion;
import fileoperation.FileStatus;
import repository.Repository;

import java.io.File;
import java.io.IOException;

public class JitCheckout {

    /**
     * Switch to an existed branch.
     * @param branchname
     * @throws IOException
     */
    public static void checkout(String branchname) throws IOException {
        if(!FileStatus.branchExist("master")){
            System.out.print("You are on a branch yet to be born");
            return;
        }
        if(!FileStatus.branchExist(branchname)){
            System.out.println("pathspec " + "'" + branchname + "'" + " did not match any file(s) known to git");
        }
        branch.Branch branch = new branch.Branch(branchname);
        branch.writeHead();

        File[] worktree = new File(Repository.getWorkTree()).listFiles();
        for(int i = 0; i < worktree.length;i++){
            if(!worktree[i].getName().equals(".jit")){
                FileDeletion.deleteFile(worktree[i]);
            }
        }
        FileCreation.recoverWorkTree(Core.getCurTree(), Repository.getWorkTree());
    }

    /**
     * Create a new branch and switch to it.
     * @param branchname
     * @throws IOException
     */
    public static void buildCheckout(String branchname)throws IOException {
        JitBranch.branch(branchname);
        checkout(branchname);
    }
}
