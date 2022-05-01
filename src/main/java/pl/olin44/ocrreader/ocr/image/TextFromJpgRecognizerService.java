package pl.olin44.ocrreader.ocr.image;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.olin44.ocrreader.ocr.TemporalFileHelper;
import pl.olin44.ocrreader.ocr.text_recognition.TextRecognizer;
import pl.olin44.ocrreader.ocr.UnsupportedDataTypeException;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

@Service
public class TextFromJpgRecognizerService {

    private static final String JPG_FILE_SUFFIX = ".jpg";

    private static final String PNG_FILE_SUFFIX = ".png";

    private static final String JPG_FILE_EXTENSION = "jpg";

    private static final String PNG_FILE_EXTENSION = "png";

    private final TextRecognizer textRecognizer;

    private final TemporalFileHelper temporalFileHelper;

    public TextFromJpgRecognizerService(TextRecognizer textRecognizer,
                                        TemporalFileHelper temporalFileHelper) {
        this.textRecognizer = textRecognizer;
        this.temporalFileHelper = temporalFileHelper;
    }

    public String recognize(MultipartFile multipartFile) throws IOException {
        return switch (getFileExtension(multipartFile)) {
            case JPG_FILE_EXTENSION -> readFromJpgFile(multipartFile);
            case PNG_FILE_EXTENSION -> readFromPngFile(multipartFile);
            default -> throw new UnsupportedDataTypeException();
        };
    }

    private String getFileExtension(MultipartFile multipartFile) {
        return FilenameUtils.getExtension(multipartFile.getOriginalFilename());
    }

    private String readFromJpgFile(MultipartFile multipartFile) {
        Path file = temporalFileHelper.writeContentToTemporalFile(multipartFile, JPG_FILE_SUFFIX);
        return textRecognizer.getTextFromImage(file);

    }

    private String readFromPngFile(MultipartFile multipartFile) throws IOException {
        Path path = convertPngToJpgFile(multipartFile);
        return textRecognizer.getTextFromImage(path);
    }

    private Path convertPngToJpgFile(MultipartFile multipartFile) throws IOException {
        Path originalFile = temporalFileHelper.writeContentToTemporalFile(multipartFile, PNG_FILE_SUFFIX);
        Path originalFileAsJpg = temporalFileHelper.createTemporalImageFile(JPG_FILE_SUFFIX);

        BufferedImage originalImage = ImageIO.read(originalFile.toFile());
        BufferedImage newBufferedImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        newBufferedImage.createGraphics()
                .drawImage(originalImage,
                        0,
                        0,
                        Color.WHITE,
                        null);

        ImageIO.write(newBufferedImage, JPG_FILE_EXTENSION, originalFileAsJpg.toFile());
        return originalFileAsJpg;
    }
}
