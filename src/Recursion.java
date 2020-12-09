package JavaGit;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import JavaGit.GitObject; //这里假设调用Change类的B方法，输入一个InputStream类型，可以返回string类型的hash值

public class Recursion {
    public static String pathForTempFile = "/Users/yjl/Downloads/tempF"; //设置tree的value时，暂时存储用的文件的路径
    public static String dfs(String path, int layer) throws Exception {
        // 使用SHA1哈希/摘要算法，用于更新当前目录的hash值, 读入是字节数组，输出也是字节数组
        MessageDigest complete = MessageDigest.getInstance("SHA-1");

        File dir = new File(path);
        File[] fs = dir.listFiles();
        //接下来对文件和文件夹按照名称进行排序
        List fileList = Arrays.asList(fs);
        Collections.sort(fileList, new Comparator<File>() {
            @Override //表示重写方法
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1; //文件夹优先，-1表示前者小，1表示前者大
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return o1.getName().compareTo(o2.getName());
            }
        });


        String mode, name, hashValue, type, total; //注解见下面用到这些变量的地方
        File tempFile = new File(pathForTempFile); //暂存文件，由于目录的hash文件暂时不知道它的value是多少，直到子文件和子目录全部遍历，所以用一个文件暂存
        /*if (tempFile.exists()) // 判断文件是否存在
        {
            tempFile.delete(); // 存在则先删除
        }*/
        tempFile.createNewFile(); // 再创建，确保这个时候暂存文件是空的
        FileWriter  out=new FileWriter (tempFile);
        BufferedWriter bw= new BufferedWriter(out);

        for(int i = 0; i < fs.length; i++) {
            name = fs[i].getName(); //文件本来的名字
            if(fs[i].isFile()) {
                mode = "100644"; //mode对应的是第一个内容
                type = "blob"; //对应的是文件还是目录
                InputStream s = new FileInputStream(fs[i]);
                hashValue = GitObject.hashObject(s, "blob"); //hash之后得到的hash值
                total = mode + " " + type + " " + hashValue + " " + name + "\r\n";//组合出来的是某一行的内容，包括换行
                bw.write(total);//将一个文件（目录）信息作为一行写入暂存的文件
            }
            if(fs[i].isDirectory()) {
                mode = "040000";
                type = "tree";
                hashValue = dfs(path + File.separator + fs[i].getName(),layer + 1);
                total = mode + " " + type + " " + hashValue + " " + name + "\r\n";//组合出来的是某一行的内容，包括换行
                bw.write(total);//将一个文件（目录）信息作为一行写入暂存的文件
            }

        }

        bw.close();
        InputStream s = new FileInputStream(tempFile);
        hashValue = GitObject.hashObject(s,"tree");
        if (layer == 1) System.out.println(hashValue); //只输出根目录的hash值
        //tempFile.delete();
        return hashValue; //返回值也是hash值
    }



    public static void main(String[] args) {
        Scanner input=new Scanner(System.in);
        System.out.println("Enter a route:");
        String path = input.nextLine();
        input.close();
        try {
            String res = dfs(path, 1);
            //System.out.println(res); //res对象属于冗余功能，和dfs内部的打印功能选择一个就可以
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
