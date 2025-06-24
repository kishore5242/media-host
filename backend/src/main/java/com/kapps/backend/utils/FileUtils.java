package com.kapps.backend.utils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class FileUtils {

    public static boolean isImageByExtension(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                name.endsWith(".png") || name.endsWith(".gif") ||
                name.endsWith(".bmp") || name.endsWith(".webp") ||
                name.endsWith(".tiff");
    }

    public static String getRelativePath(File rootDir, File file) {
        Path rootPath = rootDir.toPath().toAbsolutePath().normalize();
        Path filePath = file.toPath().toAbsolutePath().normalize();
        return rootPath.relativize(filePath).toString();  // platform-specific (might return `\` on Windows)
    }

    public static String getFilePathEncoded(File root, File file) {
        String relativePath = FileUtils.getRelativePath(root, file);
        return Base64.getUrlEncoder().encodeToString(relativePath.getBytes(StandardCharsets.UTF_8));
    }

    public static File getFileFromEncodedPath(String root, String encoded) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(encoded);
        String relativePath = new String(decodedBytes, StandardCharsets.UTF_8);
        return new File(root, relativePath);
    }

    public static String generateFileHashKey(File file) {
        try {
            long size = file.length();
            long lastModified = file.lastModified();
            String input = size + "-" + lastModified;
            return sha256(input);
        } catch (Exception e) {
            return null;
        }
    }

    private static String sha256(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
