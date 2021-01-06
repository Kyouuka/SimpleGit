package test;

import branch.Branch;
import core.Core;
import core.Log;
import gitobject.Commit;
import repository.Repository;

import java.io.File;
import java.io.IOException;

public class UnitTestLog {
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

        Commit third = new Commit(currPath + "/testfiles/testtree2", "xyh", "xyh", "3");
        master.update(third.getKey());
        master.writeBranch();
        //System.out.println(third.getTree());

        Log log = new Log(master.getCommitId());
        //log.logPrint("master");
        //log.logPrint("master", 2);
        log.logPrint("master",first.getKey());


    }
}
