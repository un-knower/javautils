package com.cenrise.commons.log;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.LoggingEvent;

public class DgfRollAppender extends FileAppender {

    private long overTime;
    private long interval;// 以秒为单位间隔时间

    static class TimestampFileHolder {
        String fileName;

        /**
         * 保留原文件的后缀信息
         * 
         * @return
         */
        String generateFile() {
            int index = fileName.lastIndexOf(".");
            if (index == -1) {
                return fileName + "-" + System.currentTimeMillis();
            }
            return fileName.substring(0, index) + "-" + System.currentTimeMillis() + fileName.substring(index);
        }

    }

    TimestampFileHolder holder = new TimestampFileHolder();

    public void setInterval(long interval) {
        this.interval = interval;
        this.overTime = System.currentTimeMillis() + interval * 1000;
    }

    protected long maxFileSize = 10 * 1024 * 1024;

    private long nextRollover = 0;

    public DgfRollAppender() {
        super();
    }

    public DgfRollAppender(Layout layout, String filename, boolean append) throws IOException {
        super(layout, filename, append);
    }

    public DgfRollAppender(Layout layout, String filename) throws IOException {
        super(layout, filename);
    }

    public long getMaximumFileSize() {
        return maxFileSize;
    }

    public void rollOver() {

        if (qw != null) {
            long size = ((CountingQuietWriter) qw).getCount();
            nextRollover = size + maxFileSize;
        }

        if (overTime != 0L) {
            overTime = System.currentTimeMillis() + interval * 1000;
        }

        try {

            qw.close();// 关闭日志文件
            this.setFile(holder.fileName, false, bufferedIO, bufferSize);
            nextRollover = 0;
        } catch (IOException e) {
            LogLog.error("setFile(" + fileName + ", false) call failed.", e);
        }
    }

    public synchronized void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize)
            throws IOException {
        if (holder.fileName == null) {
            holder.fileName = fileName;
        }

        super.setFile(holder.generateFile(), append, this.bufferedIO, this.bufferSize);
        if (append) {
            File f = new File(fileName);
            ((CountingQuietWriter) qw).setCount(f.length());
        }
    }

    public void setMaximumFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public void setMaxFileSize(String value) {
        maxFileSize = OptionConverter.toFileSize(value, maxFileSize + 1);
    }

    protected void setQWForFiles(Writer writer) {
        this.qw = new CountingQuietWriter(writer, errorHandler);
    }

    /**
     * This method differentiates RollingFileAppender from its super class.
     * 
     * @since 0.9.0
     */
    protected void subAppend(LoggingEvent event) {
        if (timeOver() || sizeOver()) {
            rollOver();
        }

        // super.subAppend(event);
        StringBuffer buffer = new StringBuffer();
        String message = this.layout.format(event);
        buffer.append(message);

        this.qw.write(buffer.toString());

        if (shouldFlush(event)) {
            this.qw.flush();
        }
    }

    boolean sizeOver() {
        if (fileName != null && qw != null) {
            long size = ((CountingQuietWriter) qw).getCount();
            return size >= maxFileSize && size >= nextRollover;
        }
        return false;
    }

    boolean timeOver() {
        if (overTime != 0L) {
            return overTime < System.currentTimeMillis();
        }
        return false;
    }

}
