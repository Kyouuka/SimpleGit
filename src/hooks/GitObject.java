package JavaGit.hooks;

import java.io.*;
import java.security.MessageDigest;

public class GitObject {
    //fmt是object的种类
    String fmt;
    //objectPath是object的绝对路径
    String objectPath;
    //objectName是object的文件名
    String objectName;

    /*构造函数*/
    public GitObject(String fmt, String objectPath){
        this.fmt = fmt;
        this.objectPath = objectPath;
    }
    /*构造函数*/
    public GitObject(String objectName){
        this.objectName = objectName;
    }
    /*计算字符串的hash值*/
    public static byte[] SHA1Checksum(InputStream is) throws Exception{
        //用SHA1方法计算hash值
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("SHA-1");
        int numRead = 0;
        while(numRead != -1) {
            numRead = is.read(buffer);
            if(numRead > 0) {
                complete.update(buffer,0,numRead);
            }
        }
        is.close();
        return complete.digest();  //返回字节数组
    }

    /* 函数名称：hashObject
     * 函数参数：InputStream is是输入流
     *         String objectType是object的类型
     * 函数功能：根据输入流生成相应的object内容，并计算相应的hash值，在objects文件夹中创建对应的object文件
     * 函数输出：String类型，object文件的文件名
     */
    public static String hashObject(InputStream is, String objectType) throws Exception {

        /*计算文件内容*/
        byte[] bytes = new byte[0];
        bytes = new byte[is.available()];
        is.read(bytes);
        String fileContents = new String(bytes);
        fileContents = objectType + '\u0020' + fileContents.length() + '\u0020' + fileContents;
        //System.out.println(fileContents);

        /*计算文件hash值*/
        InputStream inputStream = new ByteArrayInputStream(fileContents.getBytes());;
        byte[] sha1 = SHA1Checksum(inputStream);
        String fileName = "";
        for(int j = 0; j < sha1.length; j++) {
            fileName += Integer.toString(sha1[j]&0xFF, 16);
        }

        /*生成相应的object文件*/
        writeObject(fileName, fileContents);
        return fileName;
    }

    /* 函数名称：writeObject
     * 函数参数：String name是object的文件名
     *         String data是object的文件内容，即类型+文件大小+文件内容
     * 函数功能：根据输入的文件名和文件内容生成相应的object文件。
     * 注：目前只是适用于创建绝对路径下的object文件，如果需要创建文件需要修改绝对地址
     */
    public static void writeObject(String name, String data) throws Exception {
        String path = "/Users/yjl/Downloads/Java_programming/JAVA-course/src/JavaGit/objects";//这边设置为objects的目录
        File file = new File( path + File.separator + name);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(data);
        fileWriter.close();
    }
//    public String createObject(String data) throws Exception {
//        int lenOfData = data.length();  //获取数据的长度
//        data = this.fmt + ' ' + lenOfData + data;    //根据object的种类和数据长度来更新data内容
//        File file = new File("tmpFile");
//        //创建一个新的文件存储当前目录的value
//
//        FileWriter fileWriter = new FileWriter(file);
//        fileWriter.write(data);
//        fileWriter.close();
//        //关闭文件写入器
//        FileInputStream fs = new FileInputStream(file);
//        //创建文件输入流
//
//        byte[] sha1 = SHA1Checksum(fs);
//        file.delete();
//        String result = "";
//        for(int j = 0; j < sha1.length; j++) {
//            result += Integer.toString(sha1[j]&0xFF, 16);
//            //计算目录的key值
//        }
//        writeObject(result, data);
//        return result;
//    }
    public static void main(String[] args) throws Exception {

    }
}
