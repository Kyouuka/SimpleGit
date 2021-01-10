package core;

import branch.Branch;

import java.io.IOException;

public class JitLog {
    private String tree;				//current commitId
    private static int logNumber = 100; //the number of log messages
    
    /**
     * Constructor
     * @param commitId
     */
    public JitLog(String commitId) {
        this.tree = commitId;
    }

    /**
     * print 100 history records for the current branch
     * @throws Exception
     */
    public static void logPrint(String branchName) throws Exception {
        Branch branch = new branch.Branch(branchName);
        branch.getCommitHistory(logNumber);
    }

    /**
     * print history records for the current branch, the number of records is 'time'.
     * @throws Exception
     */
    public static void logPrint(String branchName, int times) throws Exception {
        Branch branch = new branch.Branch(branchName);
        branch.getCommitHistory(times);
    }

    /**
     * print history records for the current branch after a commit
     * @throws Exception
     */
    public static void logPrint(String branchName, String commitId) throws Exception{
        Branch branch = new branch.Branch(branchName);
        branch.getCommitHistory(commitId);
    }


}
