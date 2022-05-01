package pl.olin44.ocrreader.ocr.pdf;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.olin44.ocrreader.ocr.IoError;
import pl.olin44.ocrreader.ocr.TemporalFileHelper;
import pl.olin44.ocrreader.ocr.UnsupportedDataTypeException;
import pl.olin44.ocrreader.ocr.text_recognition.TextRecognizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TextFromPdfRecognizerService {

    private static final String PDF_EXTENSION = "pdf";

    private static final String PDF_FILE_SUFFIX = ".pdf";

    public static final String JPG_FILE_SUFFIX = ".jpg";

    public static final String JPG_FILE_EXTENSION = "jpg";

    private final TemporalFileHelper temporalFileHelper;

    private final TextRecognizer textRecognizer;

    private final BufferedImageReader bufferedImageReader;

    public TextFromPdfRecognizerService(TemporalFileHelper temporalFileHelper,
                                        TextRecognizer textRecognizer,
                                        BufferedImageReader bufferedImageReader) {
        this.temporalFileHelper = temporalFileHelper;
        this.textRecognizer = textRecognizer;
        this.bufferedImageReader = bufferedImageReader;
    }

    public String recognize(MultipartFile multipartFile) {
        validateExtension(multipartFile);
        return readAllPages(multipartFile);
    }

    public String recognize(MultipartFile multipartFile, Integer pageNumber) {
        validateExtension(multipartFile);
        if (pageNumber == null || pageNumber < 0) {
            throw new NoSuchPageError("Page number can't be negative value or null");
        }
        return readFromPage(multipartFile, pageNumber);
    }

    private void validateExtension(MultipartFile multipartFile) {
        String extension = getFileExtension(multipartFile);
        if(!Objects.equals(extension, PDF_EXTENSION)) {
            throw new UnsupportedDataTypeException();
        }
    }

    private String getFileExtension(MultipartFile multipartFile) {
        return FilenameUtils.getExtension(multipartFile.getOriginalFilename());
    }

    private String readAllPages(MultipartFile multipartFile) {
        Path path = temporalFileHelper.writeContentToTemporalFile(multipartFile, PDF_FILE_SUFFIX);
        return bufferedImageReader.readBufferedImages(path).stream()
                .map(this::saveBufferedImageToFile)
                .map(textRecognizer::getTextFromImage)
                .collect(Collectors.joining("\n"));
    }

    private String readFromPage(MultipartFile multipartFile, Integer pageNumber) {
        Path path = temporalFileHelper.writeContentToTemporalFile(multipartFile, PDF_FILE_SUFFIX);
        BufferedImage bufferedImage = bufferedImageReader.readBufferedImage(pageNumber, path);
        return textRecognizer.getTextFromImage(saveBufferedImageToFile(bufferedImage));
    }

    public Path saveBufferedImageToFile(BufferedImage bufferedImage) {
        try {
            Path path = temporalFileHelper.createTemporalImageFile(JPG_FILE_SUFFIX);
            ImageIO.write(bufferedImage, JPG_FILE_EXTENSION, path.toFile());
            return path;
        } catch (IOException ioException) {
            throw new IoError("Error while saving bufferedImage to file", ioException);
        }
    }
}
