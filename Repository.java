package repository;
import fileoperation.FileCreation;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Repository {
    private static String workTree;
    private static String gitDir;
    private String conf;

    public Repository(){
        if(gitDir == ""){
            System.out.println("The repository does not exist!");
        }
    }
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

    public boolean exist(){ return new File(gitDir).exists(); }

    public boolean isFile(){ return new File(gitDir).isFile(); }

    public boolean isDirectory(){ return new File(gitDir).isDirectory(); }

    public void remove(){

    }

    /**
     * Create the repository and files and directories inside.
     * @return boolean
     * @throws IOException
     */
    public void createRepo() throws IOException {

        /*创建workTree/.jit文件夹*/
        File file = new File(gitDir);
        file.mkdir();

        /*创建一系列基本文件夹*/
        FileCreation.createDirectory(gitDir, "objects");
        FileCreation.createDirectory(gitDir, "branches");
        FileCreation.createDirectory(gitDir, "hooks");
        FileCreation.createDirectory(gitDir, "info");
        FileCreation.createDirectory(gitDir, "logs");
        FileCreation.createDirectory(gitDir, "refs", "heads");
        FileCreation.createDirectory(gitDir, "refs", "tags");
        /*创建HEAD文件*/
        FileCreation.createFile(gitDir, "HEAD", "ref: refs/heads/master\n");
        /*创建description描述文件*/
        FileCreation.createFile(gitDir, "description", "Unnamed repository; edit this file 'description' to name the repository.\n");
        /*创建config配置文件*/
        FileCreation.createFile(gitDir, "config", getDefaultConfig());

    }
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
