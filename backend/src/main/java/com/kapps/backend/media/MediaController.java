package com.kapps.backend.media;

import com.kapps.backend.media.service.MediaService;
import com.kapps.backend.media.service.ThumbnailService;
import com.kapps.backend.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    @Value("${app.media.root}")
    private String mediaRoot;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private ThumbnailService thumbnailService;

    @GetMapping
    public List<FileMetadata> listFiles() {
        return mediaService.listFiles();
    }

    @GetMapping("/{id}")
    public FileMetadata getFile(@PathVariable("id") String id) {
        return mediaService.getFile(id);
    }

    @GetMapping("/{id}/thumbnail")
    public ResponseEntity<ByteArrayResource> getThumbnail(@PathVariable String id) throws IOException {

        File originalFile = FileUtils.getFileFromEncodedPath(mediaRoot, id);

        // Generate or retrieve thumbnail
        File thumbnail = thumbnailService.generateThumbnail(originalFile);

        if(thumbnail == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        // Read and return the thumbnail as byte array
        byte[] bytes = Files.readAllBytes(thumbnail.toPath());
        ByteArrayResource resource = new ByteArrayResource(bytes);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // or detect based on file extension
        headers.setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS));

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

}
