package core;

import java.io.File;
import java.io.IOException;

import stage.Index;

public class JitRemove {
	/**
	 * Remove file record only in index file.
	 * If the object in repo also needs to be remove, please use "jit commit" after this command.
	 * Jit command cannot delete files in working directory. 
	 * @param file
	 * @throws IOException
	 */
	public static void remove(File file) throws IOException {
		Index idxObject = new Index();
		idxObject.removeFileRecord(file);
		idxObject.writeIndex();
	}
}
