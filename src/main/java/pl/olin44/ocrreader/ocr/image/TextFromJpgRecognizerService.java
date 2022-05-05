package pl.olin44.ocrreader.ocr.image;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.olin44.ocrreader.ocr.IoError;
import pl.olin44.ocrreader.ocr.UnsupportedDataTypeException;
import pl.olin44.ocrreader.ocr.text_recognition.TextRecognizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Service
public class TextFromJpgRecognizerService {

    private static final String JPG_FILE_EXTENSION = "jpg";

    private static final String PNG_FILE_EXTENSION = "png";

    private final TextRecognizer textRecognizer;

    public TextFromJpgRecognizerService(TextRecognizer textRecognizer) {
        this.textRecognizer = textRecognizer;
    }

    public String recognize(MultipartFile multipartFile) {
        return switch (getFileExtension(multipartFile)) {
            case JPG_FILE_EXTENSION, PNG_FILE_EXTENSION -> readFromImage(multipartFile);
            default -> throw new UnsupportedDataTypeException();
        };
    }

    private String getFileExtension(MultipartFile multipartFile) {
        return FilenameUtils.getExtension(multipartFile.getOriginalFilename());
    }

    private String readFromImage(MultipartFile multipartFile) {
        try (InputStream imageInputStream = multipartFile.getInputStream()) {
            BufferedImage bufferedImage = ImageIO.read(imageInputStream);
            return textRecognizer.getTextFromImage(bufferedImage);
        } catch (IOException e) {
            throw new IoError("Error while reading file", e);
        }
    }
}
