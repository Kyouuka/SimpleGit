package JavaGit.hooks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;


//本类用于记录当前所有分支指向的最近的commit
public class GitRefs {
    public void CreateBranch(String branchName, String CommitID) throws Exception {
        String path = "../refs/heads/";
        File file = new File(path + branchName); //创建一个文件，文件名就是branch名
        file.createNewFile(); // 创建，确保这个时候暂存文件是空的
        FileWriter out=new FileWriter (file);
        BufferedWriter bw= new BufferedWriter(out);
        bw.write(CommitID); //写入当前的CommitID
        bw.close();
    }

    public boolean checkBranch(String branchName){
        String path = "../refs/heads/";
        File dir = new File(path);
        File[] fs = dir.listFiles(); //底下的子文件，文件名就是各个branch
        for (int i = 0; i < fs.length; i++){
            if (fs[i].getName()== branchName){
                return true; //打印所有分支的名称，除了当前分支名已经被打印就不用打印
            }
        }
        return false;
    }

    //显示当前branch（用*标注）以及所有的branch
    public static void printBranch(String branchNow){
        String path = "../refs/heads/";
        File dir = new File(path);
        File[] fs = dir.listFiles(); //底下的子文件，文件名就是各个branch
        System.out.println("*" + branchNow);
        for (int i = 0; i < fs.length; i++){
            if (fs[i].getName()!= branchNow){
                System.out.println(fs[i].getName()); //打印所有分支的名称，除了当前分支名已经被打印就不用打印
            }
        }
    }
}
