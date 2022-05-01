package pl.olin44.ocrreader.ocr.pdf;

public class NoSuchPageError extends RuntimeException {
    public NoSuchPageError(String message) {
        super(message);
    }
}
