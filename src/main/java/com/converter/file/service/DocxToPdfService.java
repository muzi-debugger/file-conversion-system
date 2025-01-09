package com.converter.file.service;

import com.converter.file.domain.File;
import com.converter.file.repository.FileRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DocxToPdfService {

    private static final Logger log = LoggerFactory.getLogger(DocxToPdfService.class);
    private static final String DOWNLOAD_DIR = "src/main/resources/downloads/";
    private final FileRepository fileRepository;
    private final SqsService sqsService;

    public DocxToPdfService(FileRepository fileRepository, SqsService sqsService) {
        this.fileRepository = fileRepository;
        this.sqsService = sqsService;
    }

    public void transformToPdf(java.io.File docxFile) throws Exception {
        java.io.File pdfFile = new java.io.File(DOWNLOAD_DIR + docxFile.getName().replace(".docx", ".pdf"));

        try (
            FileOutputStream pdfStream = new FileOutputStream(pdfFile);
            FileInputStream fis = new FileInputStream(docxFile);
            XWPFDocument document = new XWPFDocument(fis)
        ) {
            // Initialize iText Document and PdfWriter
            Document pdfDocument = new Document();
            PdfWriter.getInstance(pdfDocument, pdfStream);
            pdfDocument.open();

            // Iterate through paragraphs of the DOCX file and write to PDF
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                pdfDocument.add(new Paragraph(paragraph.getText()));
            }
            pdfDocument.close();

            File fileEntity = new File();
            fileEntity.setFileName(pdfFile.getName());
            fileEntity.setFileType("application/pdf");
            fileEntity.setLastModified(LocalDate.now());
            fileEntity.setConverted(true);
            fileEntity.setCategory("document");
            fileRepository.save(fileEntity);

            log.info("PDF created successfully at: " + pdfFile.getPath());

            // Read pdf content and send to SQS
            byte[] pdfContent = Files.readAllBytes(pdfFile.toPath());
            sqsService.SendToSqsQueue(fileEntity, pdfContent);
            log.info("Successfully sent message to SQS: {}", pdfFile.getPath());
        } catch (Exception e) {
            log.error("Error during DOCX to PDF transformation: " + e.getMessage());
            throw e;
        }
    }
}
