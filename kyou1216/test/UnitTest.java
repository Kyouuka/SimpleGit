package test;
import gitobject.*;
import repository.Repository;


import java.io.File;
import java.io.FileInputStream;

public class UnitTest {
	public static void testCommit(String path) throws Exception {
		Commit cmt = new Commit(path, "Rick", "Morty", "test commit");
		if(cmt.isValidCommit()) {
			cmt.writeObject();
		}
		System.out.println(cmt.getValue());
	}
	
    public static void main(String args[]){
        File currDirectory = new File("");//设定为当前文件夹
        try{

            String currPath = currDirectory.getAbsolutePath();
            Repository currRepo = new Repository(currPath);

            File dir = new File(currPath + File.separator + "testfiles");
            File file[] = dir.listFiles();

            for(int i = 0; i < file.length; i++){
                if(file[i].isFile()){
                    Blob blob = new Blob(currRepo.getRepoPath()+File.separator+"objects",file[i]);
                    blob.compressWrite();
                }

                else if(file[i].isDirectory()){
                    Tree tree = new Tree(currRepo.getRepoPath()+File.separator+"objects", file[i]);
                    tree.compressWrite();
                }
            }
            
            testCommit(currRepo.getRepoPath());
        }catch(Exception e){
            e.getStackTrace();
        }
    }
}
