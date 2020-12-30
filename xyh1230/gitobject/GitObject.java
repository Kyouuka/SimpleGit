package gitobject;
import repository.Repository;
import zlib.ZLibUtils;

import java.io.*;

public class GitObject {

    protected String fmt;                  //type of object
    protected String key;                  //key of object
    protected String mode;                 //mode of object
    protected static String path = Repository.getGitDir() + File.separator + "objects";          //absolute path of objects
    protected String value;                //value of object

    public String getFmt(){
        return fmt;
    }
    public String getKey() { return key; }
    public String getMode(){
        return mode;
    }
    public String getPath() {
        return path;
    }
    public String getValue(){
        return value;
    }


    public GitObject(){}
    /**
     * get the content(value) of file
     * @param file
     * @return String
     * @throws IOException
     */
    public static String getValue(File file) throws IOException {
        FileInputStream is = new FileInputStream(file);
        byte[] bytes = new byte[0];
        bytes = new byte[is.available()];
        is.read(bytes);
        String fileContents = new String(bytes);
        return fileContents;
    }
    /**
     * Write the object to the local repository.
     * @throws Exception
     */
    public void writeObject() throws Exception{
        File file = new File(path + File.separator + key);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(value);
        fileWriter.close();
    }

    /**
     * Compress the value utilizing ZLib and write to local.
     * @throws Exception
     */
    public void compressWrite() throws Exception{
        byte[] data = ZLibUtils.compress(value.getBytes());
        FileOutputStream fos = new FileOutputStream(path + File.separator + key);
        fos.write(data);
        fos.close();
    }

}
