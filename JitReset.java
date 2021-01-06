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
    /**
     * Reset the index and head file to the last commit.
     */
    public static void resetHard() throws Exception {
        resetHardCommit(JitBranch.getCurBranch().getCommitId());
    }

    /**
     * Reset the worktree and index to a certain commit.
     * @param commitId
     * @throws IOException
     */
    public static void resetHardCommit(String commitId) throws Exception {
        Branch branch = JitBranch.getCurBranch();
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
