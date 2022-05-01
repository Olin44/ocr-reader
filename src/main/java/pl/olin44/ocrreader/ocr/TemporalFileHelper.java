package pl.olin44.ocrreader.ocr;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TemporalFileHelper {

    public Path writeContentToTemporalFile(MultipartFile multipartFile, String fileSuffix) {
        try {
            Path file = createTemporalImageFile(fileSuffix);
            try (OutputStream outputStream = Files.newOutputStream(file)) {
                outputStream.write(multipartFile.getBytes());
            }
            return file;
        } catch (IOException ioException) {
            throw new IoError("Error while writing to file", ioException);
        }
    }

    public Path createTemporalFile(String filePrefix, String fileSuffix) throws IOException {
        return Files.createTempFile(filePrefix, fileSuffix);
    }

    public Path createTemporalImageFile(String fileSuffix) throws IOException {
        return createTemporalFile("image", fileSuffix);
    }
}
