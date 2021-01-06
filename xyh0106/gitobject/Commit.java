package gitobject;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import fileoperation.FileReader;
import sha1.SHA1;
import repository.Repository;
import zlib.ZLibUtils;

public class Commit extends GitObject{
    protected String tree; 		// the sha1 value of present committed tree
    protected String parent; 		// the sha1 value of the parent commit
    protected String author; 		// the author's name, email, timestamp and timezone
    protected String committer; 	// the committer's info
    protected String message; 	// the commit memo
    protected String timestamp; //timestamp

    public String getParent(){return parent;}
    public String getTree(){return tree;}
    public String getAuthor(){return author;}
    public String getCommitter(){return committer;}
    public String getMessage(){return message;}
    public String getTimestamp(){ return timestamp;}

    public Commit(){}
    /**
     * Constructor
     * @param
     * author, committer, message参数在git commit命令里通过scanner创建
     * @throws Exception
     */
    public Commit(String treePath, String author, String committer, String message) throws Exception {
        this.fmt = "commit"; 	//type of object
        /******************************/
        this.tree = new Tree(new File(treePath)).getKey();  //?
        /******************************/
        this.parent = getLastCommit() == null ? "" : getLastCommit(); //null means there is no parent commit.
        this.author = author;
        this.committer = committer;
        this.message = message;

        /*Content of this commit, like this:
         *tree bd31831c26409eac7a79609592919e9dcd1a76f2
         *parent d62cf8ef977082319d8d8a0cf5150dfa1573c2b7
         *author xxx  1502331401 +0800
         *committer xxx  1502331401 +0800
         *修复增量bug
         * */
        this.value = "tree " + this.tree + "\nparent " + this.parent+ "\nauthor " + this.author + "\ncommitter " + this.committer + "\n" + this.message;
        //写commit命令的时候记得一定要先判断是否合法再write commit object
        genKey();

        if(isValidCommit()) {
            compressWrite();
        }

        //获取commit的时间戳
    }
    public Commit(String commitId) throws IOException {
        fmt = "commit";
        key = commitId;
        String path = Repository.getGitDir() + File.separator + "objects";
        File file = new File(path + File.separator + commitId);
        if(file.isFile()){

            FileInputStream is = new FileInputStream(file);
            byte[] output = ZLibUtils.decompress(is);
            is.close();
            value = new String(output);
            tree = FileReader.readCommitTree(value);
            parent = FileReader.readCommitParent(value);
            author = FileReader.readCommitAuthor(value);
            committer = FileReader.readCommitter(value);
            message = FileReader.readCommitMsg(value);
        }
    }
    /**
     * Generate the hash value of this commit.
     * @return key
     * */
    public String genKey() throws Exception {
        key = SHA1.getHash(value);
        return key;
    }


    /**
     * Determine if committed content is changed. If not, the commit is illegal.
     * @return boolean tree != parent
     */
    public boolean isValidCommit() throws IOException {
        Commit parentContent = new Commit(parent);
        String parentTreeKey = parentContent.getTree();
        return (parentTreeKey == null || !parentTreeKey.equals(tree));
    }

    /**
     * Get the parent commit from the HEAD file.
     * Attention: branch is not used in this code. Waiting for an update.
     * @return
     * @throws IOException
     */
    public static String getLastCommit() throws IOException {

        File HEAD = new File(Repository.getGitDir() + File.separator + "HEAD");

        String path = getValue(HEAD).substring(5).replace("\n","");
        File branchFile = new File(Repository.getGitDir() + File.separator + path);

        if(branchFile.isFile()) {
            return getValue(branchFile);
        }
        else {
            return null;
        }
    }

    /**
     * Use the linked list to get the history of the current commit history
     * @throws IOException
     */
    public void logCommitHistory() throws IOException {
        System.out.println(getValue());
        String parentId = getParent();
        while(parentId != null){
            Commit commit = new Commit(parentId);
            System.out.println(commit.getValue());
            parentId = commit.getParent();
        }
    }
    /**
     * Use the linked list to get the history of the current commit history, limit the number of history records.
     * @throws IOException
     */
    public void logCommitHistory(int number) throws IOException{
        int time = 1;
        System.out.println(getValue());
        String parentId = getParent();
        while(parentId != null & time < number){
            Commit commit = new Commit(parentId);
            System.out.println(commit.getValue());
            parentId = commit.getParent();
            time++;
        }
    }
    /**
     * Use the linked list to get the history of the current commit history， show history after a commit.
     * @throws IOException
     */
    public void logCommitHistory(String topCommitId) throws IOException{
        System.out.println(getValue());
        if (getKey().equals(topCommitId)){return;}
        String parentId = getParent();
        while(parentId != null){
            Commit commit = new Commit(parentId);
            System.out.println(commit.getValue());
            parentId = commit.getParent();
            if(commit.getKey().equals(topCommitId)){break;}
        }
    }
}

