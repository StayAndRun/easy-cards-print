package com.karatitza;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.styledxmlparser.resolver.font.BasicFontProvider;
import com.itextpdf.svg.converter.SvgConverter;
import com.itextpdf.svg.processors.impl.SvgConverterProperties;
import com.karatitza.project.MeasureUtils;

import java.io.*;
import java.net.MalformedURLException;

import static java.text.MessageFormat.format;

public class Main {

    public static final String TEMP_FILES_RELATE_PATH = format("{0}print{0}temp", File.separator);
    public static final String SOURCE_FILES_RELATE_PATH = format("{0}decks", File.separator);

    public static void main(String[] args) throws IOException {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(pipedOutputStream);
        PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);
        dataOutputStream.writeLong(42);
        dataOutputStream.close();

        byte[] bytes = out.toByteArray();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream dataInputStream = new DataInputStream(pipedInputStream);
        long resultLong = dataInputStream.readLong();
        System.out.println(resultLong);
    }

    private static void convertSvgFileToPdf(Document document) throws IOException {
        File initialFile = new File("./src/main/resources/test-simple.svg");
        InputStream targetStream = new FileInputStream(initialFile);
        document.getPdfDocument().addNewPage();
        SvgConverterProperties svgConverterProperties = new SvgConverterProperties();
        svgConverterProperties.setFontProvider(new BasicFontProvider());
        SvgConverter.drawOnDocument(targetStream, document.getPdfDocument(), 1, svgConverterProperties);
    }

    private static void drawImageFromFile(Document document) throws MalformedURLException {
        ImageData imageData = ImageDataFactory.create(
                "F:/PNP/KDM/Апдейт 1.6/Изображения для карт/2F19BC1CFE57DD66D98D00300D4443765BE7DBA3_Perfect Bone.png"
        );
        Image image = new Image(imageData);
        image.setHeight(MeasureUtils.millimetersToPoints(91));
        document.add(image);
    }

    private static void drawRectangle(Document document) {
        PdfPage pdfPage = document.getPdfDocument().addNewPage();
        Rectangle rectangle = new Rectangle(MeasureUtils.millimetersToPoints(59), MeasureUtils.millimetersToPoints(91));
        Rectangle rectangle1 = new Rectangle(MeasureUtils.millimetersToPoints(59), MeasureUtils.millimetersToPoints(91));
        rectangle.setX(MeasureUtils.millimetersToPoints(10));
        rectangle.setY(MeasureUtils.millimetersToPoints(10));
        PdfCanvas pdfCanvas = new PdfCanvas(pdfPage)
                .setStrokeColor(ColorConstants.BLACK)
                .setFillColor(ColorConstants.YELLOW)
                .rectangle(rectangle)
                .rectangle(rectangle1)
                .stroke();
    }

}
