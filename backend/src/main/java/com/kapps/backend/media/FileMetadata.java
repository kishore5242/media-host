package com.kapps.backend.media;

import com.kapps.backend.utils.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Base64;

public class FileMetadata {
    private String id;
    private String name;
    private String absolutePath;
    private long size;
    private long creationTime;
    private long lastAccessTime;
    private long lastModifiedTime;
    private String extension;
    private String fileKey;

    public FileMetadata(File root, File file) {
        this.id = FileUtils.getFilePathEncoded(root, file);
        this.name = file.getName();
        this.absolutePath = file.getAbsolutePath();
        this.size = file.length();
        this.lastModifiedTime = file.lastModified(); // or new Date(file.lastModified())
        this.extension = getExtension(file.getName());
        loadExtraMetadata(file);
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex > 0 && dotIndex < filename.length() - 1)
                ? filename.substring(dotIndex + 1).toLowerCase()
                : "";
    }

    private void loadExtraMetadata(File file) {
        try {
            Path path = file.toPath();
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            this.creationTime = attrs.creationTime().toMillis();
            this.lastAccessTime = attrs.lastAccessTime().toMillis();
            Object key = attrs.fileKey();
            this.fileKey = FileUtils.generateFileHashKey(file);
        } catch (Exception e) {
            // ignore
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public long getSize() {
        return size;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public String getExtension() {
        return extension;
    }

    public String getFileKey() {
        return fileKey;
    }
}
