package gitobject;

import sha1.SHA1;
import zlib.ZLibUtils;

import java.io.*;

public class Blob extends GitObject{
    public String getFmt(){
        return fmt;
    }
    public String getMode(){
        return mode;
    }
    public String getPath() {
        return path;
    }
    public String getValue(){
        return value;
    }
    public String getKey() { return key; }

    public Blob(){
        this.fmt = "blob";
        this.mode = "100644";
    }

    public Blob(File file) throws Exception {
        fmt = "blob";
        mode = "100644";
        value = getValue(file);
        genKey(file);
    }

    /**
     * Construct a blob object from an existed file in .jit/objects.
     * @param Id
     * @throws IOException
     */
    public Blob(String Id) throws IOException {
        this.path = path;
        fmt = "blob";
        mode = "100644";
        try{
            File file = new File(path + File.separator + Id);
            if(file.exists()){
                key = Id;
                FileInputStream is = new FileInputStream(file);
                byte[] output = ZLibUtils.decompress(is);
                value = new String(output);
            }
            else{
                throw new IOException();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Generate key from file.
     * @param file
     * @return String
     * @throws Exception
     */
    public String genKey(File file) throws Exception {
        key = SHA1.getHash(value);
        return key;
    }
    @Override
    public String toString(){
        return "100644 blob " + key;
    }
}
