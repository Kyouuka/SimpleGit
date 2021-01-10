package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import gitobject.Blob;
import gitobject.Commit;
import gitobject.GitObject;
import fileoperation.FileReader;
import gitobject.Tree;
import repository.Repository;
import zlib.ZLibUtils;

public class JitDiff {

    /**
     * Compare a file before and after a modification,return if its changed.
     *
     * @param before, after
     * @return boolean
     */
    public static boolean diffExist(File before, File after) throws IOException {
        if(before.lastModified() == after.lastModified()){ return false; }
        String befStr = new String(new GitObject().getValue(before));
        String aftStr = new String(new GitObject().getValue(after));
        ArrayList<String> befVal = FileReader.readByBufferReader(befStr);
        ArrayList<String> aftVal = FileReader.readByBufferReader(aftStr);
        return diffExist(befVal, aftVal); //这边还有问题
    }
    
    /**
     * Compare a file before and after a modification,return if its changed.
     * @param blobId
     * @param after
     * @return boolean
     * @throws IOException
     */
    public static boolean diffExist(String blobId, File after) throws IOException {
        File hashFileBef = new File(Repository.getGitDir()+ File.separator +"objects" + File.separator + blobId);
        FileInputStream is = new FileInputStream(hashFileBef);
        byte[] output = ZLibUtils.decompress(is);
        is.close();
        String befStr = new String(output);
        String aftStr = (new GitObject()).getValue(after);
        if(befStr.equals(aftStr)){
            return true;
        }else {
            return false;
        }
    }
    
    /**
     * Compare a file before and after a modification, print its difference.
     *
     * @param before, after
     * @return
     */
    public static void compareFile(File before, File after) throws IOException {
        String befStr = new String(GitObject.getValue(before));
        String aftStr = new String(GitObject.getValue(after));
        System.out.println("Below is the diff file of a/"+before.getPath()+" and b/"+ after.getPath());
        compareFile(befStr, aftStr);
    }

    public static void compareFile(String befStr, String aftStr) throws IOException {
        ArrayList<String> befVal = FileReader.readByBufferReader(befStr);
        ArrayList<String> aftVal = FileReader.readByBufferReader(aftStr);
        int[][] difference = compareFile(befVal, aftVal);
        printDiff(befVal, aftVal, difference);
    }

    /**
     * Compare a file before and after a modification, return its changes.
     *
     * @param befStr, aftStr
     * @return difference
     */
    public static int[][] compareFile(ArrayList<String> befStr, ArrayList<String> aftStr) {
        int m = aftStr.size(), n = befStr.size(); 
        int[][][] paths = new int[n + 1][][];
        int[][] dp = new int[m + 1][n + 1];

        //Myers algo
        paths[0] = new int[1][2];
        paths[0][0][0] = 0;
        paths[0][0][1] = 0;

        for (int i = 0; i <= m; ++i) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; ++j) {
            dp[0][j] = j;
            if (j == 0) {
                continue;
            }
            paths[j] = new int[paths[j - 1].length + 1][2];
            if (j > 0) {
                for (int i = 0; i <= j - 1; ++i) {
                    paths[j][i] = paths[j - 1][i];
                }
            }
            paths[j][paths[j].length - 1][0] = j;
            paths[j][paths[j].length - 1][1] = 0;
        }
        int[][] temp;
        int[][] lastPath;
        for (int i = 1; i <= m; ++i) {
            temp = paths[0].clone();
            paths[0] = new int[temp.length + 1][2];
            for (int k = 0; k < temp.length; k++) {
                paths[0][k] = temp[k].clone();
            }
            paths[0][temp.length][0] = i;
            paths[0][temp.length][1] = 0;
            for (int j = 1; j <= n; ++j) {
                temp = paths[j - 1];
                if (befStr.get(j - 1).equals(aftStr.get(i - 1)) ) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int minRes = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
                    dp[i][j] = minRes;
                    if (minRes == dp[i - 1][j - 1] + 1) {
                        paths[j] = new int[temp.length + 1][2];
                        for (int k = 0; k < temp.length; k++) {
                            paths[j][k] = temp[k].clone();
                        }
                    } else if (minRes == dp[i - 1][j] + 1) {
                        lastPath = paths[j].clone();
                        paths[j] = new int[lastPath.length + 1][2];
                        for (int k = 0; k < lastPath.length; k++) {
                            paths[j][k] = lastPath[k].clone();
                        }
                    } else {
                        paths[j] = new int[paths[j - 1].length + 1][2];
                        for (int k = 0; k < paths[j - 1].length; k++) {
                            paths[j][k] = paths[j - 1][k].clone();
                        }
                    }
                }
                paths[j][paths[j].length - 1][0] = i;
                paths[j][paths[j].length - 1][1] = j;
            }
        }

        return paths[paths.length - 1];
    }

    /**
     * Print the change of a file before and after the modification
     *
     * @param befVal, aftVal, difference
     * @return
     */
    public static void printDiff(ArrayList<String> befVal, ArrayList<String> aftVal, int[][] difference) {
        int store = 0;

        for (int i = 1; i < difference.length; i++) {
            if (difference[i-1][0] + 1 == difference[i][0] && difference[i-1][1] + 1 == difference[i][1]){
                System.out.println("   "+aftVal.get(difference[i][0] - 1));

            }else if(difference[i-1][0] + 1 == difference[i][0]){
                System.out.println("+  "+aftVal.get(difference[i][0] - 1));
                store++;
            }else{
                System.out.println("-  "+befVal.get(difference[i][1] - 1));
                store++;
            }
        }
    }

    /**
     * find out whether two lists of String have difference
     * @param befStr
     * @param aftStr
     * @return
     */
    public static boolean diffExist(ArrayList<String> befStr, ArrayList<String> aftStr){
        if(befStr.size()!=aftStr.size()){
            return true;
        }
        for(int i = 0; i < befStr.size(); i++){
            if(!befStr.get(i).equals(aftStr.get(i))){
                return true;
            }
        }
        return false;
    }

    /**
     * Compare cache zone and a commit and print the differences.
     * @param indexList, commitId
     * @throws IOException
     */
    public static void compareCache(LinkedList<String[]> indexList, String commitId) throws IOException {
        Commit commit = new Commit(commitId);
        Tree tree = new Tree(commit.getTree());
        for(int i = 0; i < indexList.size(); i++){
            compareFile(tree, indexList.get(i));
        }
    }

    /**
     * Compare two commits and print the differences.
     * @param commitId1, commitId2
     * @throws IOException
     */
    public static void compareCommit(String commitId1, String commitId2) throws IOException {
        Commit commit1 = new Commit(commitId1);
        Commit commit2 = new Commit(commitId2);
        Tree tree1 = new Tree(commit1.getTree());
        Tree tree2 = new Tree(commit2.getTree());
        System.out.println("The difference between "+commitId1+" and "+commitId2+" is:");
        compareTree(tree1, tree2, Repository.getWorkTree());
    }

    /**
     * Find out whether a file is modified or created
     * if it's modified print the difference and if it's created print the file
     * @param tree, record
     * return true if it's modified, return false if it's created.
     */
    public static boolean compareFile(Tree tree, String[] record) throws IOException {
        ArrayList<GitObject> treelist = tree.getTreeList();
        String[] route = record[1].split(File.separator);
        for(int i = 1; i < route.length; i++){
            String value = tree.getValue();
            ArrayList<String> list = FileReader.readByBufferReader(value);
            for (int j = 0; j < list.size(); j++){
                if(FileReader.readObjectFileName(list.get(j)).equals(route[i])){ 
                    if(i == route.length - 1){ 
                        File hashFileAft = new File(Repository.getGitDir() + File.separator + "objects" + File.separator +record[0]);
                        File hashFileBef = new File(Repository.getGitDir() + File.separator + "objects" + File.separator + FileReader.readObjectKey(list.get(j)));
                        if(diffExist(hashFileBef, hashFileAft)){ 
                            FileInputStream is = new FileInputStream(hashFileBef);
                            byte[] output = ZLibUtils.decompress(is);
                            is.close();
                            String befStr = new String(output);
                            is = new FileInputStream(hashFileAft);
                            output = ZLibUtils.decompress(is);
                            is.close();
                            String aftStr = new String(output);
                            System.out.println("Below is the diff file of a"+record[1]+" and b"+ record[1]);
                            compareFile(befStr, aftStr);
                            return true;
                        }else{
                            System.out.println("There is no difference between a"+record[1]+" and b"+ record[1]);
                            return false;
                        }
                    }else{
                        tree = new Tree(FileReader.readObjectKey(list.get(j)));
                        break; 
                    }
                }
            }
            if(i == route.length - 1){ 
                System.out.println("The file "+ record[1]+" is created");
            }
        }

        return false;
    }

    /**
     * Compare two trees and print differences.
     * @param tree1
     * @param tree2
     * @param parentPath
     * @throws IOException
     */
    public static void compareTree(Tree tree1, Tree tree2, String parentPath) throws IOException {
        String value1 = tree1.getValue();
        ArrayList<String> list1 = FileReader.readByBufferReader(value1);
        String value2 = tree2.getValue();
        ArrayList<String> list2 = FileReader.readByBufferReader(value2);

        for(int i = 0; i < list2.size(); i++){
            boolean findTheFile = false;
            for(int j = 0; j < list1.size(); j++){
                if(FileReader.readObjectFileName(list2.get(i)).equals(FileReader.readObjectFileName(list1.get(j)))){
                    findTheFile = true;
                if(FileReader.readObjectFmt(list2.get(i)).equals("blob")){ 
                    File hashFileAft = new File(Repository.getGitDir() + File.separator + "objects" + File.separator + FileReader.readObjectKey(list2.get(i)));
                    File hashFileBef = new File(Repository.getGitDir() + File.separator + "objects" + File.separator + FileReader.readObjectKey(list1.get(j)));
                    String filePath = parentPath + File.separator + FileReader.readObjectFileName(list2.get(i)); 
                    if(diffExist(hashFileBef, hashFileAft)){ 
                        FileInputStream is = new FileInputStream(hashFileBef);
                        byte[] output = ZLibUtils.decompress(is);
                        is.close();
                        String befStr = new String(output);
                        is = new FileInputStream(hashFileAft);
                        output = ZLibUtils.decompress(is);
                        is.close();
                        String aftStr = new String(output);
                        System.out.println("Below is the diff file of a/"+ filePath+" and b/"+ filePath);
                        compareFile(befStr, aftStr);
                        break;
                    }else{
                        System.out.println("There is no difference between a/"+ filePath+" and b/"+ filePath);
                        break;
                    }
                }else{ 
                    Tree treeChild1 = new Tree(FileReader.readObjectKey(list1.get(j)));
                    Tree treeChild2 = new Tree(FileReader.readObjectKey(list2.get(i)));
                    compareTree(treeChild1, treeChild2, parentPath + File.separator + FileReader.readObjectFileName(list2.get(i)));
                }
                    break;
                }
            }
            if(FileReader.readObjectFmt(list2.get(i)).equals("blob") && findTheFile==false){
                System.out.println("The file is created: " + parentPath + File.separator + FileReader.readObjectFileName(list2.get(i)));
            }
        }
    }
}
