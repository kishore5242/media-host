package com.kapps.backend.media.service;

import com.kapps.backend.media.FileMetadata;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class MediaService {

    @Value("${app.media.root}")
    private String mediaRoot;

    public List<FileMetadata> listFiles() {
        File rootDir = new File(mediaRoot);
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            throw new IllegalArgumentException("Invalid media root: " + mediaRoot);
        }

        // No extension filter means list everything
        Collection<File> files = FileUtils.listFiles(rootDir, null, true);
        return files.stream()
                .map(file -> new FileMetadata(new File(mediaRoot), file))
                .collect(Collectors.toList());
    }

    public FileMetadata getFile(String id) {
        File file = com.kapps.backend.utils.FileUtils.getFileFromEncodedPath(mediaRoot, id);
        return new FileMetadata(new File(mediaRoot), file);
    }


}
