package core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import branch.Branch;
import stage.*;
import gitobject.*;
import repository.Repository;

public class JitCommit {
    /**
     * Creating commit and write it to repository.
     * @throws Exception
     */
    public void commit() throws Exception {
        String tree = buildTree().getKey();
        String author = System.getProperty("user.name") + " " + String.valueOf(new Date().getTime());
        String committer = System.getProperty("user.name");

        System.out.println("Please enter the commit message: ");
        Scanner sc = new Scanner(System.in);
        String message = sc.nextLine();

        Commit commit = new Commit(tree, author, committer, message);
        commit.compressWrite();

        updateBranch(commit);
        sc.close();
    }
    
    /**
     * Creates an IndexTree using the current index.
     * @return
     * @throws IOException 
     */
    public static HashMap<String, IndexTreeEl> buildIndexTree() throws IOException {
    	HashMap<String, IndexTreeEl> root = new HashMap<>();
    	Index idxObject = new Index();
    	LinkedList<String[]> idxList = idxObject.getIndexList();
    	
    	for(String[] record : idxList) {
    		String dir = record[1]; //file path
    		//String separator = File.separator; //for Windows
    		String separator = "/|\\\\"; //for Windows and Linux
    		List<String> splitedPath = Arrays.asList(dir.split(separator)); //element is a file's name or a dir's name
    		HashMap<String, IndexTreeEl> cur = root;
    		for(int i = 0; i < splitedPath.size() - 1; i++) { //dirs
    			String str = splitedPath.get(i);
    			if(cur.containsKey(str)) {
    				cur = cur.get(str).indexTree;
    			}else {
    				HashMap<String, IndexTreeEl> t = new HashMap<>();
    				cur.put(str, new IndexTreeEl(str, "", t)); //insert a dir
    				cur = t;
    			}
    		}
    		String fileName = splitedPath.get(splitedPath.size() - 1); //the last one is file
    		cur.put(fileName, new IndexTreeEl(fileName, record[0])); //insert a file
    	}
    	
    	return root;
    }
    
    /**
     * Creates a tree object using the current index. 
     * @throws Exception 
     */
    public static Tree buildTree() throws Exception {
    	HashMap<String, IndexTreeEl> indexTree = buildIndexTree();
    	Tree tree = new Tree(indexTree);
    	return tree;
    }

	public void updateBranch(Commit commit) throws IOException {
		String commitId = commit.getKey();
		String path = Repository.getGitDir() + File.separator + "refs" + File.separator + "heads";
		File file = new File(path);
		if(file.list().length == 0) { //master branch hasn't been created.
			branch.Branch master = new branch.Branch("master", commitId);
			master.writeBranch();
		}

	}
}
