package fileoperation;

import repository.Repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.LinkedList;

public class FileStatus {
    /**
     * To check if the branch exists.
     * @param branchname
     * @return
     * @throws FileNotFoundException
     */
    public static boolean branchExist(String branchname)throws FileNotFoundException {
        File branch = new File(Repository.getGitDir() + File.separator + "refs"
                + File.separator + "heads" + File.separator + branchname);

        return branch.isFile();
    }

    /**
     * Compare the timestamp of the file to see if it is changed.
     * @param path
     * @param filename
     * @param indexList
     * @return
     * @throws FileNotFoundException
     */
    public static boolean fileChanged(String path, String filename, LinkedList<String[]> indexList) throws FileNotFoundException{
        boolean change = false;
        File file = new File(path + File.separator + filename);
        if(!file.isFile()){
            throw new FileNotFoundException(filename + " is not a file!");
        }
        String timeStamp = new Date(file.lastModified()).toString();
        for(int i = 0; i < indexList.size(); i++){
            if(indexList.get(i)[1].equals(filename)){
                change = indexList.get(i)[2].equals(timeStamp);
                break;
            }
        }
        return change;
    }
}
