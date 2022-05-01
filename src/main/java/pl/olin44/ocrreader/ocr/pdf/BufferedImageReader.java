package pl.olin44.ocrreader.ocr.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import pl.olin44.ocrreader.ocr.IoError;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class BufferedImageReader {

    public BufferedImage readBufferedImage(Integer pageNumber, Path path) {
        try (PDDocument pdDocument = PDDocument.load(path.toFile())) {
            PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);
            if (pdDocument.getNumberOfPages() < pageNumber) {
                throw new NoSuchPageError("No such page in document. This document has only " + pdDocument.getNumberOfPages() + " pages");
            }
            return pdfRenderer.renderImage(pageNumber);
        } catch (IOException ioException) {
            throw new IoError("Error while reading buffered image from file " + path.getFileName(), ioException);
        }
    }

    public List<BufferedImage> readBufferedImages(Path path) {
        try (PDDocument pdDocument = PDDocument.load(path.toFile())) {
            PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);
            List<BufferedImage> bufferedImages = new ArrayList<>();
            for (int pageNumber = 0; pageNumber < pdDocument.getNumberOfPages(); pageNumber++) {
                bufferedImages.add(pdfRenderer.renderImage(pageNumber));
            }
            return bufferedImages;
        } catch (IOException ioException) {
            throw new IoError("Error while reading buffered images from file " + path.getFileName(), ioException);
        }
    }
}
