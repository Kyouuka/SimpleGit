package gitobject;

import java.io.*;
import sha1.SHA1;
import repository.Repository;

public class Commit extends GitObject{
	String tree; 		// the sha1 value of present committed tree
	String parent; 		// the sha1 value of the parent commit
	String author; 		// the author's name, email, timestamp and timezone
	String committer; 	// the committer's info
	String message; 	// the commit memo
	
	/**
	 * Constructor
	 * @param 
	 * 注：author, committer, message参数在git commit命令里通过scanner创建 
	 * @throws Exception
	 */
	public Commit(String path, String author, String committer, String message) throws Exception {
		this.fmt = "commit"; 	//type of object
		this.path = path; 		// path of the present committed tree
		this.tree = new Tree(path, new File(path)).getKey();
		this.parent = getLastCommit() == null? "" : getLastCommit(); //null means there is no parent commit.
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
		//if(isValidCommit()) {
			//wtite()
		//}
	}
	
	/**
	 * Generate the hash value of this commit.
	 * @return key
	 * */
	public String genKey() throws Exception {
		String key = SHA1.getHash(value);
		return key;
	}
	
	/**
	 * Determine if committed content is changed. If not, the commit is illegal.
	 * @return boolean tree != parent
	 */
	public boolean isValidCommit() {
		return tree != parent;
	}
	
	/**
	 * Get the parent commit from the HEAD file.
	 * Attention: branch is not used in this code. Waiting for an update.
	 * @return
	 * @throws IOException
	 */
	public String getLastCommit() throws IOException {
		File headFile = new File(Repository.getRepoPath() + "\\HEAD");
		if(!headFile.exists()) {
			return null;
		}else {
	        InputStreamReader in = new InputStreamReader(new FileInputStream(headFile));
	        BufferedReader br = new BufferedReader(in);
	        return br.readLine();
		}
	}
	
}
