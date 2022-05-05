package pl.olin44.ocrreader.ocr.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.olin44.ocrreader.ocr.IoError;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class BufferedImageReader {

    public BufferedImage read(MultipartFile multipartFile, Integer pageNumber) {
        try (InputStream multipartFileInputStream = multipartFile.getInputStream();
             PDDocument pdDocument = PDDocument.load(multipartFileInputStream)) {
            PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);
            // pages are indexed from 0
            return pdfRenderer.renderImage(pageNumber - 1);
        } catch (IOException ioException) {
            throw new IoError("Error while reading buffered image from file", ioException);
        }
    }

    public List<BufferedImage> read(MultipartFile multipartFile) {
        try (InputStream multipartFileInputStream = multipartFile.getInputStream();
             PDDocument pdDocument = PDDocument.load(multipartFileInputStream)) {
            PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);
            List<BufferedImage> bufferedImages = new ArrayList<>();
            for (int pageNumber = 0; pageNumber < pdDocument.getNumberOfPages(); pageNumber++) {
                bufferedImages.add(pdfRenderer.renderImage(pageNumber));
            }
            return bufferedImages;
        } catch (IOException e) {
            throw new IoError("Error while reading buffered images from file", e);
        }
    }
}
