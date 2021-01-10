package test;

import core.Core;
import fileoperation.FileStatus;
import repository.Repository;
import stage.Index;

import java.io.File;
import java.io.IOException;

public class UnitTestAdd {
    public static void main(String[] args) throws Exception {
        Core.init("/Users/xyh/worktree");


        core.JitAdd.add(new File(Repository.getWorkTree() + "/a.txt"));
        core.JitAdd.add(new File( Repository.getWorkTree() + "/testblob1.txt"));
        System.out.println(FileStatus.branchExist("master"));
        //core.JitCommit.commit();
        //core.JitAdd.add(new File( Repository.getWorkTree() + "/testtree1"));
        //core.JitAdd.add(new File( "/Users/xyh/IdeaProjects/SimpleGit/testfiles"));

        //System.out.println(new Index().getIndexList().size());
    }
}
