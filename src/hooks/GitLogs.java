package JavaGit.hooks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

//本类用于实现记录head的历史和每个分支的历史
public class GitLogs {
    public void CreateLogs(String branchName, String PreCommitID, String CommitID) throws Exception{
        //这个方法配合GitRefs的CreateBranch方法使用，本方法在logs中产生一个文件用来记录branch的历史
        //如果已经有了就直接在后面写
        String path = "../logs/refs/heads/";
        File file = new File(path + branchName); //创建一个文件，文件名就是branch名
        if (!file.exists()) { //如果文件不存在，就创建它
            file.createNewFile();
        }
        FileWriter out=new FileWriter (file,true);
        BufferedWriter bw= new BufferedWriter(out);
        bw.write(PreCommitID+" "+CommitID+"\r\n"); //写入上次和当前的CommitID，并换行
        bw.close();
    }

    public void HeadLogs(String branchName) throws Exception{
        //这个方法配合CreateLogs方法使用，每次把HEAD的文件清空，然后复制当前branch的logs即可
        String path = "../logs/refs/";
        File dest = new File(path + "HEAD"); //创建一个文件，文件名就是HEAD
        File sour = new File(path + "heads/" + branchName);
        //下面的操作就是把指定branch的内容复制到HEAD内
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(sour);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }
}
