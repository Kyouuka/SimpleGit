package core;

import branch.Branch;
import fileoperation.FileCreation;
import fileoperation.FileDeletion;
import gitobject.Commit;
import gitobject.Tree;
import stage.Index;
import repository.Repository;

import java.io.File;
import java.io.IOException;

public class JitReset {
//    /**
//     * Reset the certain file to previous version.If it is a directory, then reset all the files inside.
//     * @param path
//     * @throws IOException
//     */
//    public static void reset_file(String path) throws IOException {
//        File file = new File(path);
//        if(file.isFile()){
//            Tree tree = Core.getCurTree();
//            //String key = tree.find(file.getName());
//            //Index.updateIndex();
//        }
//        else if(file.isDirectory()){
//            File[] fileList = file.listFiles();
//            for(int i = 0; i < fileList.length; i++){
//                reset_file(fileList[i].getPath());
//            }
//        }
//    }

    /**
     * Reset the index and head file to the last commit.
     */
    public static void resetHard() throws Exception {
        resetHardCommit(Core.getCurBranch().getCommitId());
    }

//    /**
//     * Reset the branch to a certain commit, and reset the index.
//     * @param commitId
//     * @throws IOException
//     */
//    public static void reset_commit(String commitId) throws IOException{
//        branch.Branch branch = Core.getCurBranch();
//        branch.reset(commitId);
//        Commit commit = new Commit(commitId);
//        Tree tree = new Tree(commit.getTree());
//        tree.toIndex();
//    }
    /**
     * Reset the worktree and index to a certain commit.
     * @param commitId
     * @throws IOException
     */
    public static void resetHardCommit(String commitId) throws Exception {
        Branch branch = Core.getCurBranch();
        branch.reset(commitId);
        Commit commit = new Commit(commitId);
        Tree tree = new Tree(commit.getTree());
        /**
         * 删除工作区中的文件
         */
        File[] worktree = new File(Repository.getWorkTree()).listFiles();
        for(int i = 0; i < worktree.length;i++){
            if(!worktree[i].getName().equals(".jit")){
                FileDeletion.deleteFile(worktree[i]);
            }
        }
        FileCreation.recoverWorkTree(tree, Repository.getWorkTree());

        /**
         * 清除index中内容，再从工作区重新生成。
         */
        Index index = new Index();
        index.clear();
        File[] workTree = new File(Repository.getWorkTree()).listFiles();

        for(int i = 0; i < workTree.length; i++){
            if(!workTree[i].getName().equals(".jit"))
                JitAdd.add(workTree[i]);
        }
    }
}
