package com.kapps.backend.media.service;

import com.kapps.backend.utils.FileUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class ThumbnailService {

    private File outputDir;

    public ThumbnailService(@Value("${app.media.root}") String mediaRoot) {
        // Ensure thumbnail directory exists
        File outputDir = new File(mediaRoot, ".thumbnails");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        this.outputDir = outputDir;
    }

    public File generateThumbnail(File sourceImageFile) throws IOException {
        if (!FileUtils.isImageByExtension(sourceImageFile)) {
            return null;
        }

        // Define thumbnail file path
        String thumbnailName = FileUtils.generateFileHashKey(sourceImageFile);
        File thumbnailFile = new File(outputDir, thumbnailName + ".jpg");

        // Generate thumbnail only if it doesn't exist
        if (!thumbnailFile.exists()) {
            Thumbnails.of(sourceImageFile)
                    .size(200, 200) // Adjust as needed
                    .outputFormat("jpg") // Or keep original format
                    .toFile(thumbnailFile);
        }

        return thumbnailFile;
    }
}
