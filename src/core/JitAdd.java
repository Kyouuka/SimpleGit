package core;

import java.io.File;
import java.io.IOException;

import stage.Index;

public class JitAdd {
	/**
	 * Add files to index.
	 * 'jit add' equals to two commands: 'jit hash-object<file>' and 'jit update-index --add<file>'.
	 * @param file
	 * @throws Exception
	 */
    public static void add(File file) throws Exception {
    	if(!file.exists()) {
    		throw new IOException("File doesn't exist.");
    	}
    	
    	Index idxObject = new Index();
    	if(file.isFile()){
			if(idxObject.inIndex(file) > -1) {	//If file record exists in index, rewrite it.
				idxObject.removeFileRecord(file);
			}
			idxObject.insertIndex(file);
			idxObject.writeIndex();
		}
    	else if(file.isDirectory()){
			File[] fs = file.listFiles();
			for(int i = 0; i < fs.length; i++){
				add(fs[i]);
			}
		}
    }
}
