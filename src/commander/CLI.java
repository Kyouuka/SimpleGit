package commander;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import branch.Branch;
import core.*;
import gitobject.Commit;
import repository.Repository;
import stage.Index;


public class CLI {	
	
	/**
	 * Command 'jit init'
	 * @param args
	 * @throws IOException
	 */
	public static void jitInit(String[] args) throws IOException {
		String path = "";
		if(args.length <= 2) { //get default working path
			path = new File(".").getCanonicalPath();
			JitInit.init(path);
		}else if(args[2].equals("-help")){ //see help
			System.out.println("usage: jit init [<path>] [-help]\r\n" +
					"\r\n" +
					"jit init [<path>]:	Create an empty jit repository or reinitialize an existing one in the path or your default working directory.");
		}else {
			path = args[2];
			if(!new File(path).isDirectory()) { //if the working path input is illegal
				System.out.println(path + "is not a legal directory. Please init your reposiroty again. See 'jit init -help'.");
			}else {
				JitInit.init(path);
			}
		}
	}
	
	/**
	 * Command 'jit add'. Add file(s) or directories to stage area
	 * @param args
	 */
	public static void jitAdd(String[] args) {
		String workDir = Repository.getWorkTree();
		
		if(args.length <= 2 || (args.length > 2 && args[2].equals("-help"))) {
			System.out.println("usage: jit add <file1> [<file2>...] [-help]\r\n" +
					"\r\n" +
					"jit add <file1> [<file2>...]: Add file(s) to stage.");
		}else {
			for(int i = 2; i < args.length; i++) {
				String fileName = args[i];
				File file = new File(workDir + File.separator + fileName);
				try {
					JitAdd.add(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Command 'jit remove'. Remove file record(s) from stage.
	 * @param args
	 */
	public static void jitRemove(String[] args) {
		String workDir = Repository.getWorkTree();
		
		if(args.length <= 2 || (args.length > 2 && args[2].equals("-help"))) {
			System.out.println("usage: jit remove <file1> [<file2>...] [-help]\r\n" +
					"\r\n" +
					"jit remove <file1> [<file2>...]: Remove file(s) from stage.");
		}else {
			for(int i = 2; i < args.length; i++) {
				String fileName = args[i];
				File file = new File(workDir + File.separator + fileName);
				try {
					JitRemove.remove(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Command 'jit commit'. Commit files from stage to repository.
	 * @param args
	 * @throws Exception 
	 */
	public static void jitCommit(String[] args) throws Exception {
		if(args.length <= 2) {
			JitCommit jc = new JitCommit();
			jc.commit();
		}else if(args[2].equals("-help")){
			System.out.println("usage: jit commit [-help]\r\n" +
					"\r\n" +
					"jit commit: Commit file(s) from stage to repository.");
		}
	}
	
	/**
	 * Command 'jit branch'
	 * 'jit branch [branch-name]'
	 * 'jit branch [branch] [commit]' 
	 * 'jit branch -d [branch-name]'
	 * @param args
	 * @throws IOException 
	 */
	public static void jitBranch(String[] args) throws IOException {
		if(args.length < 2 || (args.length > 2 && args[2].equals("-help"))) { //'jit branch -help'
			System.out.println("usage: jit branch [branch-name] [-d branch-name] [-help]\r\n" +
					"\r\n" +
					"jit branch: List all local branches.\r\n" +
					"\r\n" + 
					"jit branch [branch-name]: Create the branch.\r\n" +
					"\r\n" +
					"jit branch [branch-name] [commit]: Create the branch and point it to the commit.\r\n" +
					"\r\n" +
					"jit branch -d [branch-name]: Delete the branch.");
		}else if(args.length == 2) { //'jit branch'
			JitBranch.branch();
		}else if(args.length == 3) { //'jit branch [branch-name]'
			String branchName = args[2];
			JitBranch.branch(branchName);
		}else if(args.length == 4 && args[2].equals("-d")) { //'jit branch -d [branch-name]'
			String branchName = args[3];
			JitBranch.deleteBranch(branchName);
		}else if(args.length == 4) { //'jit branch [branch] [commit]'
			String branchName = args[2];
			String commitId = args[3];
			JitBranch.branch(branchName, commitId);
		}
	}
	
	/**
	 * Command 'jit checkout [branch]
	 * 'jit checkout -b [branch]'
	 * @param args
	 * @throws IOException 
	 */
	public static void jitCheckout(String[] args) throws IOException {
		if(args.length <= 2 || (args.length > 2 && args[2].equals("-help"))) { //'jit checkout -help'
			System.out.println("usage: jit checkout [branch-name] [-b branch-name] [-help]\r\n" +
					"\r\n" +
					"jit checkout [branch]: Switch to the branch.\r\n" +
					"\r\n" + 
					"jit checkout -b [branch]: Delete the branch.");
		}else if(args.length == 3) { //'jit checkout [branch]'
			String branchName = args[2];
			JitCheckout.checkout(branchName);
			System.out.println("Switched to " + branchName);
		}else if(args.length == 4 && args[2].equals("-b")) {
			String branchName = args[3];
			JitCheckout.buildCheckout(branchName);
			System.out.println("Built and switched to " + branchName);
		}
	}
	
	
	/**
	 * Command 'jit status'. Compare file(s) changed in working directory.
	 * @param args
	 * @throws IOException 
	 */
	public static void jitStatus(String[] args) throws IOException {
		if(args.length <= 2) {
			Branch branch = JitBranch.getCurBranch();
			String branchName = branch.getBranchName();
			JitStatus status = new JitStatus();
			status.setBranchName(branchName);
			Index index = new Index();
			status.setIndexList(index.getIndexList());
			status.setCommitId(branch.getCommitId());
			status.showStatus();
		}else if(args[2].equals("-help")){
			System.out.println("usage: jit status [-help]\r\n" +
					"\r\n" +
					"jit status: Compare file(s) changed in working directory.");
		}
	}
	
	/**
	 * Command 'jit log': Show log of the present branch  
	 * 'jit log HEAD [commit]': Show changes after the commit
	 * 'jit log [number]': Show [number] commits' logs
	 * @param args
	 * @throws Exception 
	 */
	public static void jitLog(String[] args) throws Exception {
		Branch branch = JitBranch.getCurBranch();
		String branchName = branch.getBranchName();
		
		if(args.length == 2) { //'jit log'
			JitLog.logPrint(branchName);
		}else if (args.length == 3 && args[2].equals("-help")) {
			System.out.println("usage: jit log [HEAD commit] [number] [-help]\r\n" +
					"\r\n" +
					"jit log: Show log of the present branch.\r\n" +
					"\r\n" + 
					"jit log HEAD [commit]: Show changes after the commit.\r\n" +
					"\r\n" +
					"jit log [number]: Show [number] commits' logs.");
		}else if(args.length == 3) { //'jit log [number]'
			int times = Integer.valueOf(args[2]);
			JitLog.logPrint(branchName, times);
		}else if(args.length == 4 && args[2].equals("HEAD")) { //'jit log HEAD [commit]'
			String commitId = args[3];
			JitLog.logPrint(branchName, commitId);
		}
	}
	
	/**
	 * Command 'jit diff --cached': Show the difference between stage and the last commit.
	 * 'jit diff [first-branch] [second-branch]': Show the difference between two commits.
	 * @param args
	 * @throws IOException 
	 */
	public static void jitDiff(String[] args) throws IOException {
		if(args.length < 3 || (args.length >= 3 && args[2].equals("-help"))) {
			System.out.println("usage: jit diff [--cached] [branch-name1] [branch-name2] [-help]\r\n" +
					"\r\n" +
					"jit diff --cached: Show the difference between stage and the last commit.\r\n" +
					"\r\n" + 
					"jit diff [first-branch] [second-branch]: Show the difference between two commits.");
		}else if(args[2].equals("--cached")) { //'jit diff --cached'
			String lastCmt = Commit.getLastCommit();
			Index idx = new Index();
			LinkedList<String[]> indexList = idx.getIndexList();
			JitDiff.compareCache(indexList, lastCmt);
		}else if(args.length == 4) { //'jit diff [first commitId] [second commitId]'
			String branch1Name = args[2];
			String branch2Name = args[3];
			Branch branch1 = new Branch(branch1Name);
			Branch branch2 = new Branch(branch2Name);
			String cmt1 = branch1.getCommitId();
			String cmt2 = branch2.getCommitId();
			JitDiff.compareCommit(cmt1, cmt2);
		}
	}
	
	/**
	 * Command 'jit reset --hard'
	 * 'jit reset --hard [commit]'
	 * @param args
	 * @throws Exception 
	 */
	public static void jitReset(String[] args) throws Exception {
		if(args.length <= 2 || args[2].equals("-help")) {
			System.out.println("usage: jit reset [--hard] [--hard commit-id] [-help]\r\n" +
					"\r\n" +
					"jit reset --hard: Reset the index and head file to the last commit.\r\n" +
					"\r\n" + 
					"jit reset --hard [commit]: Reset the worktree and index to a certain commit.");
		}else if(args.length == 3 && args[2].equals("--hard")) {
			JitReset.resetHard();
		}else if(args.length == 4 && args[2].equals("--hard")) {
			String commitId = args[3];
			JitReset.resetHardCommit(commitId);
		}
	}
	
	/**
	 * Command 'jit help'.
	 */
	public static void jitHelp() {
		System.out.println("usage: jit [--version] [--help] [-C <path>] [-c name=value]\r\n" + 
				"           [--exec-path[=<path>]] [--html-path] [--man-path] [--info-path]\r\n" + 
				"           [-p | --paginate | --no-pager] [--no-replace-objects] [--bare]\r\n" + 
				"           [--git-dir=<path>] [--work-tree=<path>] [--namespace=<name>]\r\n" + 
				"           <command> [<args>]\r\n" + 
				"\r\n" + 
				"These are common Jit commands used in various situations:\r\n" + 
				"\r\n" + 
				"start a working area\r\n" + 
				"   init       Create an empty Jit repository or reinitialize an existing one\r\n" + 
				"\r\n" + 
				"work on the current change\r\n" + 
				"   add        Add file contents to the index\r\n" + 
				"   reset      Reset current HEAD to the specified state\r\n" + 
				"   rm         Remove files from the working tree and from the index\r\n" + 
				"\r\n" + 
				"examine the history and state\r\n" + 
				"   log        Show commit logs\r\n" + 
				"   status     Show the working tree status\r\n" + 
				"\r\n" + 
				"grow, mark and tweak your common history\r\n" + 
				"   branch     List, create, or delete branches\r\n" + 
				"   checkout   Switch branches or restore working tree files\r\n" + 
				"   commit     Record changes to the repository\r\n" + 
				"   diff       Show changes between commits, commit and working tree, etc\r\n" + 
				"   merge      Join two or more development histories together\r\n" + 
				"\r\n" + 
				"'jit help -a' and 'jit help -g' list available subcommands and some\r\n" + 
				"concept guides. See 'jit help <command>' or 'jit help <concept>'\r\n" + 
				"to read about a specific subcommand or concept.");
	}
	
	public static void main(String[] args) throws Exception {
		if(args.length <= 1 || args[1].equals("help")) {
			jitHelp();
		}else {
			if(args[1].equals("init")) {
				jitInit(args);
			}else if(args[1].equals("add")){
				jitAdd(args);
			}else if(args[1].equals("remove")) {
				jitRemove(args);
			}else if(args[1].equals("commit")) {
				jitCommit(args);
			}else if(args[1].equals("branch")) {
				jitBranch(args);
			}else if(args[1].equals("checkout")) {
				jitCheckout(args);
			}else if(args[1].equals("status")) {
				jitStatus(args);
			}else if(args[1].equals("log")) {
				jitLog(args);
			}else if(args[1].equals("diff")) {
				jitDiff(args);
			}else if(args[1].equals("reset")) {
				jitReset(args);
			}else {
				System.out.println("jit: " + args[1] + "is not a git command. See 'git help'.");
			}
		}
	}
}
