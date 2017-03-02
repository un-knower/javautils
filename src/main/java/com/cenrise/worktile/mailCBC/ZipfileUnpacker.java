package com.cenrise.worktile.mailCBC;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.vfs2.AllFileSelector;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;

/**
 * vfs2实现的文件解压功能
 *
 * @author dongpo.jia
 */
public class ZipfileUnpacker {
    private final FileSystemManager fileSystemManager;
    private final URI packLocation;

    public ZipfileUnpacker(final URI packLocation) throws FileSystemException {
        this.fileSystemManager = VFS.getManager();
        this.packLocation = packLocation;
    }

    //    @Override
    public void unpack(final File outputDir) throws IOException {
        outputDir.mkdirs();

        final FileObject packFileObject = fileSystemManager.resolveFile(packLocation.toString());
        try {
            final FileObject zipFileSystem = fileSystemManager.createFileSystem(packFileObject);
            try {
                fileSystemManager.toFileObject(outputDir).copyFrom(zipFileSystem, new AllFileSelector());
            } finally {
                zipFileSystem.close();
            }
        } finally {
            packFileObject.close();
        }
    }

    public static void main(String args[]) {
        String url = "/Users/yp-tc-m-2684/Workspaces/IdeaProjectsTong/JavaMailSample/tmp/SHOP.105110054111509.20170223.zip";
        try {
            URI uri = new URI(url);
            //URI uri = new URI("file:" + url);
            String outputuri = "/Users/yp-tc-m-2684/Workspaces/IdeaProjectsTong/JavaMailSample/tmp/tmp_SHOP.105110054111509.20170223";
            Const.createDirFiles(outputuri);
            File file = new File(outputuri);
            new ZipfileUnpacker(uri).unpack(file);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (FileSystemException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}