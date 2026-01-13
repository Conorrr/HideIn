package dev.hidein;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PdfInjector {

    private static final float FONT_SIZE = 1f; // tiny
    private static final float X_POS = 0f;
    private static final float Y_POS = 0f;

    public byte[] inject(byte[] pdfBytes, String text) throws IOException {
        try (var doc = Loader.loadPDF(pdfBytes)) {
            if (doc.getNumberOfPages() == 0) {
                throw new IllegalArgumentException("PDF has no pages");
            }

            PDPage firstPage = doc.getPage(0);

            try (var cs = new PDPageContentStream(doc, firstPage, PDPageContentStream.AppendMode.APPEND, true, true)) {
                cs.beginText();
                cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE);
                cs.setNonStrokingColor(1f, 1f, 1f); // white
                cs.newLineAtOffset(X_POS, Y_POS);
                cs.showText(text);
                cs.endText();
            }

            var out = new ByteArrayOutputStream();
            doc.save(out);
            return out.toByteArray();
        }
    }
}
