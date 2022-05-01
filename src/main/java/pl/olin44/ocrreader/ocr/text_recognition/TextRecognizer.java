package pl.olin44.ocrreader.ocr.text_recognition;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import pl.olin44.ocrreader.ocr.text_recognition.ErrorWhileProcessingImageException;

import java.nio.file.Path;

@Service
public class TextRecognizer {

    private final Tesseract tesseract;

    public TextRecognizer(Tesseract tesseract) {
        this.tesseract = tesseract;
    }

    public String getTextFromImage(Path path) {
        try {
            return tesseract.doOCR(path.toFile());
        } catch (TesseractException tesseractException) {
            throw new ErrorWhileProcessingImageException("Error while processing image: " + path.getFileName(),
                    tesseractException);
        }
    }
}
