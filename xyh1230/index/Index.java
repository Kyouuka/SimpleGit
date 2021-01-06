package index;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import repository.Repository;
import fileoperation.*;
import gitobject.*;

public class Index {
    //存储文件信息，其中String[] 里面存储了blob的key，blob的文件相对路径+文件名，以及文件最终修改时间。
    //注意$ git ls-files --stage时文件修改时间不能被打印出来
    private LinkedList<String[]> indexList = new LinkedList<>();
    String path = Repository.getGitDir();

    public Index() throws IOException {
        FileCreation.createFile(path, "index", "");
    }

    public Index(String path) throws IOException {
        FileCreation.createFile(path, "index", "");
    }

    public LinkedList<String[]> getIndexList() {
        return indexList;
    }

    public void insertIndex(File file) throws Exception {
        String[] record = new String[3];
        Blob blob = new Blob(file);
        String fileName = file.getPath();
        Date date = new Date();
        long time = date.getTime();
        String timeStr = String.valueOf(time); //以秒数输出，方便后面进行时间戳比较

        record[0] = blob.getKey();
        record[1] = fileName;
        record[2] = timeStr;
        indexList.add(record);
    }

    public void updateIndex(File file) {
        //git status比较文件是否被改动：时间戳；文件内容
        //若未改动，则更新时间戳
        //若改动，用户可选择git add此文件。注意add命令要先删除老文件，再insert新文件
    }

    public void clear() {
        File file = new File(path + File.separator + "index");
        FileDeletion.deleteContent(file);
    }

    /*
     * 未被跟踪的文件（untracked file）
        已被跟踪的文件（tracked file）
        被修改但未被暂存的文件（changed but not updated或modified）
        已暂存可以被提交的文件（changes to be committed 或staged）
        自上次提交以来，未修改的文件(clean 或 unmodified)
     */
    public boolean isTracked(File file) {
        return true;
    }
}
