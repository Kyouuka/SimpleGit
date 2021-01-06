package test;

import branch.Branch;
import core.Core;
import core.Diff;
import fileoperation.FileReader;
import gitobject.Commit;
import gitobject.Tree;
import index.Index;
import repository.Repository;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class UnitTestDiff {
    public static void main(String[] args) throws Exception {
        /*String befPath = "/Users/yjl/Downloads/text1.txt";
        String aftPath = "/Users/yjl/Downloads/text2.txt";
        File befFile = new File(befPath);
        File aftFile = new File(aftPath);
        boolean flag = Diff.diffExist(befFile, aftFile);
        System.out.println(flag);
        Diff.compareFile(befFile, aftFile);*/

        File currDirectory = new File("/Users/yjl/Downloads/testForGit");//设定为当前文件夹
        Core.Init("/Users/yjl/Downloads/testForGit");
        String currPath = currDirectory.getAbsolutePath();
        Repository currRepo = new Repository(currPath);
        File dir = new File(currPath + File.separator + "testfiles");

        Commit first = new Commit(currPath + File.separator + "testfiles", "xyh", "xyh", "1");
        Branch master = new Branch("master", first.getKey());
        master.writeBranch();


       /*File newFile = new File(currPath + File.separator + "testfiles/create.txt");
        newFile.createNewFile();
        FileOutputStream outSTr = new FileOutputStream(newFile);
        BufferedOutputStream Buff = new BufferedOutputStream(outSTr);
        Buff.write("测试java 文件操作\n".getBytes());
        Buff.flush();
        Buff.close();*/

        //Commit second = new Commit(currPath + "/testfiles/testtree1", "xyh", "xyh", "2");
        Commit second = new Commit(currPath + "/testfiles", "xyh", "xyh", "2");
        //System.out.println("Second Tree is "+ second.getValue());
        //master.update(second.getKey());
        //master.writeBranch();
        //System.out.println("author is "+ FileReader.readCommitAuthor(second.getValue()));



        Commit third = new Commit(currPath + "/testfiles/testtree2", "xyh", "xyh", "3");
        master.update(third.getKey());
        master.writeBranch();

        //Tree tree = new Tree(new File("/Users/yjl/Downloads/testForGit/"));

        Index index = new Index();
        File file1 = new File("/Users/yjl/Downloads/testForGit/text1.txt");
        File file2 = new File("/Users/yjl/Downloads/testForGit/text2.txt");
        index.insertIndex(file1);
        index.insertIndex(file2);

        Diff diff = new Diff();
        //diff.compareCache(index.getIndexList(), second.getKey());
        Commit fourth = new Commit(first.getKey());
        Tree tree1 = new Tree(second.getTree());
        Tree tree2 = new Tree(fourth.getTree());
        System.out.println(tree1.getValue());
        System.out.println(tree2.getValue());
        diff.compareCommit(first.getKey(),third.getKey());
        /*Commit commit1 = new Commit(first.getKey());
        Commit commit2 = new Commit(third.getKey());
        Tree tree1 = new Tree(commit1.getTree());
        Tree tree2 = new Tree(commit2.getTree());
        System.out.println("");*/
        //System.out.println(diff.diffExist(second.getKey(), file1));
    }
}
