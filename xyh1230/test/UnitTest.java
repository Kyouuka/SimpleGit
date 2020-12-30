package test;
import branch.Branch;
import core.Core;
import gitobject.*;
import repository.Repository;
import fileoperation.*;
import zlib.ZLibUtils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UnitTest {
    public static void main(String[] args) throws IOException {
//        File currDirectory = new File("");//设定为当前文件夹
        Core.Init("/Users/xyh/worktree");

        try{
//
//            String currPath = currDirectory.getAbsolutePath();
//            Repository currRepo = new Repository(currPath);
//
////            InputStream is = new FileInputStream(new File(Repository.getGitDir()+"/objects/5e80d10df30b7101125112084d08ca3db47daf6a"));
////            byte[] output = ZLibUtils.decompress(is);
////            System.out.println(new String(output));
//
//            //下面的测试在master分支上提交了三个commit， 并且最后回滚至第一个commit（后两个commit消失），并且可以查看分支历史
//            //但是，每次运行的时候，需要把.jit/refs/heads/master 这个文件删掉，否则在查看历史提交的时候会选择最新一次的commit而不是从头来过
//            File dir = new File(currPath + File.separator + "testfiles");
//            Commit first = new Commit(currPath + File.separator + "testfiles", "xyh", "xyh", "1");
//            Branch master = new Branch("master", first.getKey());
//            master.writeBranch();
//
//            Commit second = new Commit(currPath + "/testfiles/testtree1", "xyh", "xyh", "2");
//            master.update(second.getKey());
//            master.writeBranch();
//
//            Branch test = new Branch("test", master.getCommitId());
//            test.writeBranch();
//            test.checkout();
//
//
//            Commit third = new Commit(currPath + "/testfiles/testtree2", "xyh", "xyh", "3");
//            test.update(third.getKey());
//            test.writeBranch();
//
//            master.reset("831127d5ef0c2c56a2bb6e619e3b658e13ea0c2a");
//            test.getCommitHistory();



//            Tree t = new Tree(dir);
//            t.compressWrite();
//            File file[] = dir.listFiles();
//
//            for(int i = 0; i < file.length; i++){
//                if(file[i].isFile()){
//                    Blob blob = new Blob(file[i]);
//                    blob.compressWrite();
//                }
//
//                else if(file[i].isDirectory()){
//                    Tree tree = new Tree(file[i]);
//                    tree.compressWrite();
//                }
//            }
        }catch(Exception e){
            e.getStackTrace();
        }
    }
}
