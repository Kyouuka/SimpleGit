package zlib;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public abstract class ZLibUtils {
	
	/**
	 * Compressing a file.
	 * @param inputData
	 * @return
	 */
    public static byte[] compress(byte[] inputData){
        byte[] output = new byte[0];

        Deflater compressor = new Deflater();

        compressor.reset();
        compressor.setInput(inputData);
        compressor.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            byte[] buf = new byte[1024];
            while(!compressor.finished()){
                int i = compressor.deflate(buf);
                bos.write(buf, 0, i);
            }
            output = bos.toByteArray();
        } catch (Exception e) {
            output = inputData;
            e.printStackTrace();
        }finally {
            try{
                bos.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        compressor.end();
        return output;
    }
    
    /**
     * Compressing and writing a file.
     * @param data
     * @param os
     */
    public static void compress(byte[] data, OutputStream os) {
        DeflaterOutputStream dos = new DeflaterOutputStream(os);

        try {
            dos.write(data, 0, data.length);

            dos.finish();

            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Decompressing a file.
     * @param data
     * @return
     */
    public static byte[] decompress(byte[] data) {
        byte[] output = new byte[0];

        Inflater decompressor = new Inflater();
        decompressor.reset();
        decompressor.setInput(data);

        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!decompressor.finished()) {
                int i = decompressor.inflate(buf);
                o.write(buf, 0, i);
            }
            output = o.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        } finally {
            try {
                o.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        decompressor.end();
        return output;
    }
    
    /**
     * Decompressing a file and converting it to byte stream.
     * @param is
     * @return
     */
    public static byte[] decompress(InputStream is) {
        InflaterInputStream iis = new InflaterInputStream(is);
        ByteArrayOutputStream o = new ByteArrayOutputStream(1024);
        try {
            int i = 1024;
            byte[] buf = new byte[i];

            while ((i = iis.read(buf, 0, i)) > 0) {
                o.write(buf, 0, i);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return o.toByteArray();
    }
}
