package com.anmi.ocr.tesseract;

import org.bytedeco.javacpp.BytePointer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Objects;

import static org.bytedeco.javacpp.lept.*;
import static org.bytedeco.javacpp.tesseract.TessBaseAPI;

class TesseractPocTest {

    @Test
    void shouldRecogniseTextFromInputImageWhenTesseractApiUsed() {
        BytePointer outText;
        PIX image;
        try (TessBaseAPI api = new TessBaseAPI()) {
            // Initialize tesseract-ocr with English
            String tessdataPath = "tessdata";
            if (api.Init(tessdataPath, "ENG") != 0) {
                System.err.println("Could not initialize tesseract.");
                System.exit(1);
            }
            // Open input image with leptonica library
            URL imageResource = Thread.currentThread().getContextClassLoader().getResource("test.png");
            String filePath = Objects.requireNonNull(imageResource).getPath();
            image = pixRead(filePath);
            if (image == null) {
                throw new IllegalStateException("Image is null");
            }
            api.SetImage(image);
            // Get OCR result
            outText = api.GetUTF8Text();

            Assertions.assertNotNull(outText);

            System.out.println("OCR output:\n" + outText.getString());
            // Destroy used object and release memory
            api.End();
        }
        outText.deallocate();
        pixDestroy(image);
    }
}