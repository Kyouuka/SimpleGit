package core;

import fileoperation.FileDeletion;
import repository.Repository;

import java.io.File;
import java.io.IOException;

public class Core {
    public static void Init(String workTree) throws IOException {

        Repository repo = new Repository(workTree);
        if(repo.exist()){
            if(repo.isDirectory()){
                FileDeletion.deleteFile(repo.getGitDir());
            }
            else if(repo.isFile()){
                throw new IOException(".jit is a file, please check");
            }
        }
        repo.createRepo();
        System.out.println("Jit repository has been initiated successfully.");
    }
    public void Commit(){}
    public void Branch(){}
    public void Log(){}
    public void Checkout(){}
    public void Reset(){}
    public void Status(){}
}
