package com.cenrise.mailcbc;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.cache.WeakRefFilesCache;
import org.apache.commons.vfs2.impl.DefaultFileSystemManager;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;

import java.io.File;
import java.io.IOException;

/**
 * 获取FileObject的工具方法
 *
 * @author dongpo.jia
 */
public class SFTPClientVFS {
    private static final SFTPClientVFS SFTP_CLIENT_VFS = new SFTPClientVFS();

    public static SFTPClientVFS getInstance() {
        return SFTP_CLIENT_VFS;
    }

    private final DefaultFileSystemManager fsm;

    private SFTPClientVFS() {
        fsm = new StandardFileSystemManager();
        try {
            fsm.setFilesCache(new WeakRefFilesCache());
            fsm.init();
        } catch (FileSystemException e) {
            e.printStackTrace();
        }

        // Install a shutdown hook to make sure that the file system manager is closed
        // This will clean up temporary files in vfs_cache
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (fsm != null) {
                    try {
                        fsm.close();
                    } catch (Exception ignored) {
                        // Exceptions can be thrown due to a closed classloader
                    }
                }
            }
        }));
    }

    public FileSystemManager getFileSystemManager() {
        return fsm;
    }

    public static FileObject getFileObject(String vfsFilename) throws Exception {
        try {
            FileSystemManager fsManager = getInstance().getFileSystemManager();
            boolean relativeFilename = true;
            String[] schemes = fsManager.getSchemes();
            for (int i = 0; i < schemes.length && relativeFilename; i++) {
                if (vfsFilename.startsWith(schemes[i] + ":")) {
                    relativeFilename = false;
                    // We have a VFS URL, load any options for the file system driver
                    //fsOptions = buildFsOptions( space, fsOptions, vfsFilename, schemes[i] );
                }
            }

            String filename;
            if (vfsFilename.startsWith("\\\\")) {
                File file = new File(vfsFilename);
                filename = file.toURI().toString();
            } else {
                if (relativeFilename) {
                    File file = new File(vfsFilename);
                    filename = file.getAbsolutePath();
                } else {
                    filename = vfsFilename;
                }
            }

            FileObject fileObject = null;
            return fsManager.resolveFile(filename);
        } catch (IOException e) {
            throw new Exception("Unable to get VFS File object for filename '"
                    + cleanseFilename(vfsFilename) + "' : " + e.getMessage());
        }
    }

    /**
     * Private method for stripping password from filename when a FileObject
     * can not be obtained.
     * getFriendlyURI(FileObject) or getFriendlyURI(String) are the public
     * methods.
     */
    private static String cleanseFilename(String vfsFilename) {
        return vfsFilename.replaceAll(":[^:@/]+@", ":<password>@");
    }
}
