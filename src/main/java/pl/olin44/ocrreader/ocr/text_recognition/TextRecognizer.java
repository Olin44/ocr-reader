package pl.olin44.ocrreader.ocr.text_recognition;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class TextRecognizer {

    private final Tesseract tesseract;

    public TextRecognizer(Tesseract tesseract) {
        this.tesseract = tesseract;
    }

    public String getTextFromImage(BufferedImage bufferedImage) {
        try {
            return tesseract.doOCR(bufferedImage);
        } catch (TesseractException tesseractException) {
            throw new ErrorWhileProcessingImageException("Error while processing image", tesseractException);
        }
    }
}
