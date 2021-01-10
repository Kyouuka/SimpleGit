package repository;
import fileoperation.FileCreation;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Repository {
    private static String workTree;	//working directory
    private static String gitDir;	//jit repository path

    /**
     * Constructor
     */
    public Repository(){
        if(gitDir == ""){
            System.out.println("The repository does not exist!");
        }
    }
    
    /**
     * Constructor
     * @param path
     * @throws IOException
     */
    public Repository(String path) throws IOException {
        this.workTree = path;
        this.gitDir = path + File.separator + ".jit";
    }

    public static String getGitDir() {
        return gitDir;
    }

    public static String getWorkTree() {
        return workTree;
    }
    
    /**
     * Helper functions.
     * @return
     */
    public boolean exist(){ return new File(gitDir).exists(); }

    public boolean isFile(){ return new File(gitDir).isFile(); }

    public boolean isDirectory(){ return new File(gitDir).isDirectory(); }


    /**
     * Create the repository and files and directories inside.
     * @return boolean
     * @throws IOException
     */
    public void createRepo() throws IOException {

        //Creating workTree/.jit
        File file = new File(gitDir);
        file.mkdir();

        //Creating default dirs
        FileCreation.createDirectory(gitDir, "objects");
        FileCreation.createDirectory(gitDir, "branches");
        FileCreation.createDirectory(gitDir, "hooks");
        FileCreation.createDirectory(gitDir, "info");
        FileCreation.createDirectory(gitDir, "logs");
        FileCreation.createDirectory(gitDir, "refs", "heads");
        FileCreation.createDirectory(gitDir, "refs", "tags");
        
        //Creating default files
        FileCreation.createFile(gitDir, "HEAD", "ref: refs/heads/master\n");
        FileCreation.createFile(gitDir, "description", "Unnamed repository; edit this file 'description' to name the repository.\n");
        FileCreation.createFile(gitDir, "config", getDefaultConfig());

    }
    
    /**
     * Getting default configuration
     * @return
     */
    public String getDefaultConfig() {
        String default_config = "[core]\n"
                + "        repositoryformatversion = 0\n"
                + "        filemode = gitDir\n"
                + "        bare = false\n"
                + "        logallrefupdates = gitDir\n"
                + "        ignorecase = gitDir\n"
                + "        precomposeunicode = gitDir";
        return default_config;
    }
}
