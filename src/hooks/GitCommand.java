package JavaGit.hooks;

import java.io.File;

public class GitCommand {
    //rootPath用来表示文件的根目录
    private String rootPath = "";
    //branchNow表示当下的branch，默认是master
    private String branchNow = "master";

    //设定根目录
    public void setPath(String rootPath){
        this.rootPath = rootPath;
    }

    //返回根目录，主要用于函数调用
    public String getPath(){
        return rootPath;
    }

    //实现git init
    public void gitInit(){

    }

    //依据路径来实现git add
    public void gitAdd(String path){

    }

    //实现git commit,message表示commit的注释
    public void gitCommit(String message){

    }

    //实现分支转换，如果没有该分支则创建分支并把当前的head指向的commit复制过去；如果有的话就把head指向该branch的head,最主要是文件和目录的更新
    public void gitCheckout(String branch){

    }

    //显示当前branch（用*标注）以及所有的branch
    public void gitBranch(){
        GitRefs.printBranch(branchNow);
    }

    //查看当前的状态
    public void gitStatus(){

    }

    //查看工作区和暂存区的区别
    public void gitDiff(){

    }

    //查看历史，number表示要查看的历史数目，默认为50条
    public void gitLog(int number){

    }

    //回滚commit,默认是上一条记录，或者指定commitID回滚
    public void gitReset(String commitID){

    }

    //git merge 和 stash 真的要做吗？？？？？？？？？？
    //把branch_2merge到branch_1中
    public void gitMerge(String branch_1, String branch_2){

    }
}
