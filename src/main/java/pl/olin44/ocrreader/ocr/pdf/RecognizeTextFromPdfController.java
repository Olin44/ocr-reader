package pl.olin44.ocrreader.ocr.pdf;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("text-recognition")
public class RecognizeTextFromPdfController {

    private final TextFromPdfRecognizerService textFromPdfRecognizerService;

    public RecognizeTextFromPdfController(TextFromPdfRecognizerService textFromPdfRecognizerService) {
        this.textFromPdfRecognizerService = textFromPdfRecognizerService;
    }

    @PostMapping(path = "/pdf", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String recognizeTextFromAllPages(@RequestPart MultipartFile pdfFile) {
        return textFromPdfRecognizerService.recognize(pdfFile);
    }

    @PostMapping(path = "/pdf/{pageNumber}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String recognizeTextFromImage(@RequestPart MultipartFile pdfFile,
                                         @RequestParam Integer pageNumber) {
        return textFromPdfRecognizerService.recognize(pdfFile, pageNumber);
    }
}