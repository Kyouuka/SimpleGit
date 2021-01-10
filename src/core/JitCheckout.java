package core;

import fileoperation.FileStatus;

import java.io.File;
import java.io.IOException;

public class JitCheckout {
    /**
     * Recover all the files in stage to the worktree.
     * @throws IOException
     */
    public static void checkout() throws IOException {

    }

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
