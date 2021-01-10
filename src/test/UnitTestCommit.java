package test;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

import core.JitAdd;
import core.JitCommit;
import core.JitInit;
import gitobject.Commit;
import gitobject.Tree;

public class UnitTestCommit {

	public static void main(String[] args) throws Exception {
		JitInit.init("F:\\CoursesFiles\\爪哇\\test");
		
		String path = "F:\\CoursesFiles\\爪哇\\test\\testfiles";
		File file = new File(path);
		JitAdd.add(file);
		
		JitCommit jc = new JitCommit();
		//jc.commit();
        Tree tree = jc.buildTree();
        String author = System.getProperty("user.name") + " " + String.valueOf(new Date().getTime());
        String committer = System.getProperty("user.name");

        System.out.println("Please enter the commit message: ");
        Scanner sc = new Scanner(System.in);
        String message = sc.nextLine();

        Commit commit = new Commit(tree, author, committer, message);
        
        System.out.println(commit.getKey() + " " + commit.getAuthor());

        jc.updateBranch(commit);
        
        Commit commit2 = new Commit(path, author, committer, message);
        System.out.println(commit.getKey());
        
        sc.close();
	}

}
