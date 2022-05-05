package pl.olin44.ocrreader.ocr.image;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("text-recognition")
public class RecognizeTextFromImageController {

    private final TextFromJpgRecognizerService textFromJpgRecognizerService;

    public RecognizeTextFromImageController(TextFromJpgRecognizerService textFromJpgRecognizerService) {
        this.textFromJpgRecognizerService = textFromJpgRecognizerService;
    }

    @PostMapping(path = "/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String recognizeTextFromImage(@RequestPart MultipartFile imageFile) {
        return textFromJpgRecognizerService.recognize(imageFile);
    }
}
