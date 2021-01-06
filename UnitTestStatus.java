package test;

import branch.Branch;
import core.Core;
import core.Status;
import fileoperation.FileReader;
import gitobject.Commit;
import gitobject.GitObject;
import gitobject.Tree;
import index.Index;
import repository.Repository;

import java.io.File;

public class UnitTestStatus {
    public static void main(String[] args) throws Exception {
        File currDirectory = new File("/Users/yjl/Downloads/testForGit");//设定为当前文件夹
        Core.Init("/Users/yjl/Downloads/testForGit");
        String currPath = currDirectory.getAbsolutePath();
        Repository currRepo = new Repository(currPath);
        File dir = new File(currPath + File.separator + "testfiles");

        Commit first = new Commit(currPath + File.separator + "testfiles", "xyh", "xyh", "1");
        Branch master = new Branch("master", first.getKey());
        master.writeBranch();

        Commit second = new Commit(currPath + "/testfiles/testtree1", "xyh", "xyh", "2");
        master.update(second.getKey());
        master.writeBranch();
        //System.out.println("author is "+FileReader.readCommitAuthor(second.getValue()));

        Commit third = new Commit(currPath + "/testfiles/testtree2", "xyh", "xyh", "3");
        master.update(third.getKey());
        master.writeBranch();

        /*File testFile = new File("/Users/yjl/Downloads/testForGit/.jit/objects/541c9cb0106f763ad9dc13414abd389091b7f614");
        if(testFile.isFile()){
            System.out.println("testFile is a file");
            System.out.println(new GitObject().getValue(testFile));
        }else{
            System.out.println("Nooooooooo!");
        }*/

        /*Commit fourth = new Commit(second.getKey());
        System.out.println("value   "+fourth.getValue());*/

        System.out.println("-------------------------");

        Index index = new Index();
        File file1 = new File("/Users/yjl/Downloads/testForGit/text1.txt");
        File file2 = new File("/Users/yjl/Downloads/testForGit/text2.txt");
        index.insertIndex(file1);
        index.insertIndex(file2);

        /*File newFile = new File("/Users/yjl/Downloads/testForGit/testfiles/testtree1/create.txt");
        File newDir = new File("/Users/yjl/Downloads/testForGit/testfiles/testtree1/create");
        newFile.createNewFile();
        newDir.mkdir();*/

        //Tree rootTree = new Tree(new File("/Users/yjl/Downloads/testForGit/testfiles/"));
        Tree rootTree = new Tree(new File("/Users/yjl/Downloads/testForGit/testfiles/testtree1/"));
        Tree TreeTest = new Tree("31c219cf1bae85dfc0b29358d1df1b064098f8e5");


        Status status = new Status();
        status.setBranchName("master");
        status.setIndexList(index.getIndexList());
        status.setCommitId(second.getKey());
        status.showStatus();
        System.out.println("Test Over");
    }
}
