package core;

import branch.Branch;

import java.io.IOException;

public class Log {
    private String tree; //当前的commitId
    private int logNumber = 100; //默认读取100条历史

    public Log(String commitId) {
        this.tree = commitId;
    }

    /**
     * print 100 history records for the current branch
     * @throws Exception
     */
    public void logPrint(String branchName) throws Exception {
        Branch branch = new branch.Branch(branchName);
        branch.getCommitHistory(logNumber);
    }

    /**
     * print history records for the current branch, the number of records is 'time'.
     * @throws Exception
     */
    public void logPrint(String branchName, int time) throws Exception {
        Branch branch = new branch.Branch(branchName);
        branch.getCommitHistory(time);
    }

    /**
     * print history records for the current branch after a commit
     * @throws Exception
     */
    public void logPrint(String branchName, String commitId) throws Exception{
        Branch branch = new branch.Branch(branchName);
        branch.getCommitHistory(commitId);
    }


}
