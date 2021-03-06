package test;

import branch.Branch;
import core.Core;
import gitobject.Commit;
import repository.Repository;

import java.io.File;
import java.io.IOException;

public class UnitTestReset {
    public static void main(String[] args) throws Exception {
        Core.init("/Users/xyh/worktree");
        String currPath = "/Users/xyh/IdeaProjects/SimpleGit";

        File dir = new File(currPath);
        Commit first = new Commit(currPath + File.separator + "testfiles", "xyh", "xyh", "1");
        Branch master = new Branch("master", first.getKey());
        master.writeBranch();

        Commit second = new Commit(currPath + "/testfiles/testtree1", "xyh", "xyh", "2");
        master.update(second.getKey());
        master.writeBranch();

        Commit third = new Commit(currPath + "/testfiles/testtree2", "xyh", "xyh", "3");
        master.update(third.getKey());
        master.writeBranch();

        master.getCommitHistory();
        //core.Reset.reset_hard();
        //core.Reset.reset_hard_commit("0a46325bfc3c70f6a4b0d902cf2772982288b94b");
    }
}
