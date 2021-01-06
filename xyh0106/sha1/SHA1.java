package sha1;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public abstract class SHA1 {

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
        return complete.digest();
    }
    public static String getHash(byte[] sha1){
        String hashValue = "";
        for(int j = 0; j < sha1.length; j++) {
            hashValue += Integer.toString((sha1[j]>>4)&0x0F, 16) + Integer.toString(sha1[j]&0x0F, 16);
        }
        return hashValue;
    }
    public static String getHash(File file) throws Exception {
        byte[] sha1 = SHA1Checksum(new FileInputStream(file));
        return getHash(sha1);
    }
    public static String getHash(String value) throws Exception {
        byte[] sha1 = SHA1Checksum(new ByteArrayInputStream(value.getBytes()));
        return getHash(sha1);
    }
}
