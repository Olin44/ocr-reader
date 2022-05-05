package pl.olin44.ocrreader.ocr.common;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public final class ExtensionResolver {
    private ExtensionResolver() {
    }

    public static String resolve(MultipartFile multipartFile) {
        return FilenameUtils.getExtension(multipartFile.getOriginalFilename());
    }
}
