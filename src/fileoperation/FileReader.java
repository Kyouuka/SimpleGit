package fileoperation;

import repository.Repository;

import java.io.*;
import java.util.ArrayList;

public class FileReader {
    /**
     * Get every line in the given file.
     * @param value
     * @return
     */
    public static ArrayList<String> readByBufferReader(String value) throws FileNotFoundException {
        InputStream is = new ByteArrayInputStream(value.getBytes());
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        ArrayList<String> stringList =  new ArrayList<>();
        try{

            String line=br.readLine();
            while (line!=null){
                stringList.add(line);
                line=br.readLine();
            }

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                br.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringList;
    }

    /**
     * Get the format of the object. The param "line" is a line in a tree object, like"100644 blob *** a.txt"
     * @param line
     * @return
     */
    public static String readObjectFmt(String line){
        String [] arr = line.split("\\s+");
        return arr[1];
    }

    /**
     * Get the value of the object.
     * @param line
     * @return
     */
    public static String readObjectKey(String line){
        String [] arr = line.split("\\s+");
        return arr[2];
    }

    /**
     * Get the filename of the object.
     * @param line
     * @return
     */
    public static String readObjectFileName(String line){
        String [] arr = line.split("\\s+");
        return arr[3];
    }

    /**
     * Get the tree from a commit value.
     * @param value
     * @return
     * @throws FileNotFoundException
     */
    public static String readCommitTree(String value) throws FileNotFoundException {
        ArrayList<String> stringList = readByBufferReader(value);
        String [] arr = stringList.get(0).split("\\s+");
        return arr[1];
    }
    public static String readCommitParent(String value) throws FileNotFoundException {
        ArrayList<String> stringList = readByBufferReader(value);
        String [] arr = stringList.get(1).split("\\s+");
        return (arr.length > 1) ? arr[1] : null;
    }
    public static String readCommitAuthor(String value) throws FileNotFoundException {
        ArrayList<String> stringList = readByBufferReader(value);
        String [] arr = stringList.get(2).split("\\s+");
        String author = arr[1];
        for(int i = 2; i < arr.length; i++){ author += " " + arr[i]; }
        return author;
    }
    public static String readCommitter(String value) throws FileNotFoundException{
        ArrayList<String> stringList = readByBufferReader(value);
        String [] arr = stringList.get(3).split("\\s+");
        String committer = arr[1];
        for(int i = 2; i < arr.length; i++){ committer += " " + arr[i]; }
        return committer;
    }
    public static String readCommitMsg(String value) throws FileNotFoundException{
        ArrayList<String> stringList = readByBufferReader(value);
        return stringList.get(4);
    }

}
