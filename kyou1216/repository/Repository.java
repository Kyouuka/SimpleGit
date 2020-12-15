package repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Repository {
    private boolean isExist;
    private String workTree;
    private static String gitDir;
    private String conf;


    public Repository(String path) throws IOException {
        this.workTree = path;
        this.gitDir = path + File.separator + ".jit";
        createRepo();
    }

    /**
     * Create the directory: gitDir/.jit
     * @return
     */
    public boolean createGitDir() {
        File file = new File(gitDir);
        if(file.exists()) { //如果该文件已经存在
            if(file.isDirectory()) { //如果该文件是一个文件夹，则直接返回其值
                System.out.println("The repo already exists!");
                return true;
            }
            else {
                System.out.println(gitDir+ " is not a directory!");//catch Exception
            }
        }
        file.mkdir();
        return false;
    }

    /**
     * Create directories in the repository
     * @param mkdir
     * @param path
     * @return String
     */
    public String createRepoDir(boolean mkdir, String... path) {
        String repoDirectory = this.gitDir;
        for(int i = 0; i < path.length; i++) {
            repoDirectory = repoDirectory + File.separator + path[i]; //新文件夹的目录
            File file = new File(repoDirectory);
            if(file.exists()) { //如果该文件已经存在
                if(file.isDirectory()) { //如果该文件是一个文件夹，则直接返回其值
                    continue;
                }
                else {
                    System.out.println(repoDirectory + " is not a directory!");//catch Exception
                }
            }
            else {
                if(mkdir)  //如果文件夹不存在
                    file.mkdir();
            }
        }

        if(new File(repoDirectory).exists())return repoDirectory;
        else return null;
    }

    /**
     * Create the files the repository
     * @param path
     * @param filename
     * @param text
     * @throws IOException
     */
    public void createRepoFile(String path, String filename, String text) throws IOException {
        File file = new File(path + File.separator + filename);
        if(file.exists()) {
            if(!file.isFile()) {
                System.out.println(file.getName() + "is not file!");
                return;
            }
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(text);
        fileWriter.close();
    }

    /**
     * Create the repository and files and directories inside.
     * @return boolean
     * @throws IOException
     */
    public boolean createRepo() throws IOException {

        /*创建workTree/.jit文件夹*/
        boolean isRepoCreated = createGitDir();
        if(isRepoCreated) return false;
        /*创建一系列基本文件夹*/
        createRepoDir(true, "objects");
        createRepoDir(true, "branches");
        createRepoDir(true, "hooks");
        createRepoDir(true, "info");
        createRepoDir(true, "logs");
        createRepoDir(true, "refs", "heads");
        createRepoDir(true, "refs", "tags");
        /*创建HEAD文件*/
        createRepoFile(gitDir, "HEAD", "ref: refs/heads/master\n");
        /*创建description描述文件*/
        createRepoFile(gitDir, "description", "Unnamed repository; edit this file 'description' to name the repository.\n");
        /*创建config配置文件*/
        String config = getDefaultConfig();
        createRepoFile(gitDir, "config", config);
        return true;
    }
    public String getDefaultConfig() {
        String default_config = "[core]\n"
                + "        repositoryformatversion = 0\n"
                + "        filemode = true\n"
                + "        bare = false\n"
                + "        logallrefupdates = true\n"
                + "        ignorecase = true\n"
                + "        precomposeunicode = true";
        return default_config;
    }
    public static String getRepoPath() {
        return gitDir;
    }
    boolean exist() {
        return isExist;
    }
}
