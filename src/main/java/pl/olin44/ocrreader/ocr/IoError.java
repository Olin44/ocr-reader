package pl.olin44.ocrreader.ocr;

import java.io.IOException;

public class IoError extends RuntimeException {

    public IoError(String message, IOException ioException) {
        super(message, ioException);
    }
}
