package test;

import branch.Branch;
import core.Core;
import core.JitDiff;
import fileoperation.FileCreation;
import gitobject.Commit;
import gitobject.Tree;
import repository.Repository;
import stage.Index;

import java.io.File;

public class UnitTestCheckout {
    public static void main(String[] args) throws Exception {
        Core.init("/Users/xyh/worktree");
        String currPath = "/Users/xyh/IdeaProjects/SimpleGit";
////
////        File dir = new File(currPath);
        Commit first = new Commit(currPath + "/testfiles/testtree1", "xyh", "xyh", "1");

        Branch master = new Branch("master", first.getKey());
        master.writeBranch();

        Commit second = new Commit(currPath + "/testfiles/testtree1", "xyh", "xyh", "1");
        if(second.isValidCommit()){
            master.update(second.getKey());
            master.writeBranch();
        }

        //System.out.println(first.getKey());
        //master.getCommitHistory();

        Commit third = new Commit(Repository.getWorkTree() + "/testtree2", "xyh", "xyh", "3");
        if(third.isValidCommit()){
            master.update(third.getKey());
            master.writeBranch();
        }

        FileCreation.createFile(Repository.getWorkTree() + "/testtree2", "testblob2", "nishisb");

        Commit fourth = new Commit(Repository.getWorkTree() + "/testtree2", "xyh", "xyh", "4");
        if(fourth.isValidCommit()){
            master.update(fourth.getKey());
            master.writeBranch();
        }
        master.getCommitHistory();
        Tree t1 = new Tree(new Commit(master.getCommitId()).getTree());
        Tree t2 = new Tree(new Commit(new Commit(master.getCommitId()).getParent()).getTree());
        System.out.println(t1.getValue());
        System.out.println(t2.getValue());

        Index index = new Index();


//        File file2 = new File("/Users/xyh/IdeaProjects/SimpleGit/testfiles/testblob2.txt");
//        index.insertIndex(file1);
//        index.insertIndex(file2);
//
        JitDiff diff = new JitDiff();
        diff.compareCommit(third.getKey(),fourth.getKey(), Repository.getWorkTree()+"/testtree2");
        //System.out.println(fourth.getKey());

//        core.Branch.branch("test");
//        core.Branch.branch("xyh0105", "0a46325bfc3c70f6a4b0d902cf2772982288b94b");
//        core.Branch.branch_d("test");
//        core.Checkout.checkout("xyh0105");
//        //core.Checkout.checkout_b("xyh");
//        core.Branch.branch();
//
//        master.getCommitHistory();
    }
}
