package pl.olin44.ocrreader.ocr.text_recognition;

import net.sourceforge.tess4j.TesseractException;

public class ErrorWhileProcessingImageException extends RuntimeException {

    public ErrorWhileProcessingImageException(String message, TesseractException tesseractException) {
        super(message, tesseractException);
    }
}
