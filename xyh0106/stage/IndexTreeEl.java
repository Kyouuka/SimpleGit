package stage;

import java.util.HashMap;

/**
 * An element in index tree, including a file or a directory.
 * 'IndexTree == null' means this is a file.
 *
 */
public class IndexTreeEl {
    public String fileName;
    public String blobId;
    public HashMap<String, IndexTreeEl> indexTree;

    /**
     * Constructing IndexTreeEl for a directory.
     * @param fileName
     * @param blobId
     * @param indexTree
     */
    public IndexTreeEl(String fileName, String blobId, HashMap<String, IndexTreeEl> indexTree) {
        this.fileName = fileName;
        this.blobId = blobId;
        this.indexTree = indexTree;
    }

    /**
     * Constructing IndexTreeEl for a file.
     * @param fileName
     * @param blobId
     */
    public IndexTreeEl(String fileName, String blobId) {
        this.fileName = fileName;
        this.blobId = blobId;
        this.indexTree = null;
    }
}
