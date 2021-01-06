package test;

import branch.Branch;
import core.Core;
import gitobject.Commit;

import java.io.File;
import java.io.IOException;

public class UnitTestCheckout {
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


        core.Branch.branch("test");
        core.Branch.branch("xyh0105", "0a46325bfc3c70f6a4b0d902cf2772982288b94b");
        core.Branch.branch_d("test");
        core.Checkout.checkout("xyh0105");
        core.Checkout.checkout_b("xyh");
        core.Branch.branch();
        //master.getCommitHistory();
    }
}
