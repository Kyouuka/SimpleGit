package test;

import java.io.IOException;

import commander.CLI;
import repository.Repository;

public class UnitTestCLI {

	public static void main(String[] args) throws Exception {
		String[] sInit = {"jit", "init", "F:\\CoursesFiles\\爪哇\\test"};
		String[] sInit2 = {"jit", "init", "-help"};
		//CLI.jitHelp();
		CLI.jitInit(sInit);
		//CLI.jitInit(sInit2);
		
		String[] sAdd = {"jit", "add", "testfiles"};
		//String[] sAdd2 = {"jit", "add", "-help"};
		CLI.jitAdd(sAdd);
		//CLI.jitAdd(sAdd2);
		//System.out.println(Repository.getWorkTree());
		
		String[] sRm = {"jit", "add", "testfiles\\test1.txt"};
		//CLI.jitRemove(sRm);
		
		String[] sCmt = {"jit", "commit"};
		String[] sCmt2 = {"jit", "commit", "-help"};
		CLI.jitCommit(sCmt);
		//CLI.jitCommit(sCmt2);
		
		String[] sBranch = {"jit", "branch"};
		String[] sBranch2 = {"jit", "branch", "testBranch"};
		String[] sBranch3 = {"jit", "branch", "-d", "testBranch"};
		String[] sBranch4 = {"jit", "branch", "-help"};
		
		//CLI.jitBranch(sBranch2);
		//CLI.jitBranch(sBranch);
		//CLI.jitBranch(sBranch3);
		//CLI.jitBranch(sBranch);
		
		String[] sCheckout = {"jit", "checkout", "testBranch"};
		String[] sCheckout2 = {"jit", "checkout", "-b", "testBranch2"};
		String[] sCheckout3 = {"jit", "checkout", "-help"};
		//CLI.jitCheckout(sCheckout);
		//CLI.jitCheckout(sCheckout2);
		
		String[] sStatus = {"jit", "status"};
		String[] sStatus2 = {"jit", "status", "-help"};
		//CLI.jitStatus(sStatus);
		
		String[] sLog = {"jit", "log"};
		String[] sLog2 = {"jit", "log", "HEAD"};
		//CLI.jitLog(sLog);
		
		CLI.jitCheckout(sCheckout2);
		String[] sAdd3 = {"jit", "add", "testfiles2"};
		CLI.jitAdd(sAdd3);
		//test command 'jit diff --cached'
		String[] sDiff = {"jit", "diff", "--chached"};
		//CLI.jitDiff(sDiff);
		CLI.jitCommit(sCmt);
		//test 'jit diff [first-branch] [second-branch]'
		String[] sDiff2 = {"jit", "diff", "master", "testBranch2"};
		//CLI.jitDiff(sDiff2);
		
		//test for reset
		String[] sReset = {"jit", "reset", "--hard"};
		//CLI.jitReset(sReset);
	}

}
