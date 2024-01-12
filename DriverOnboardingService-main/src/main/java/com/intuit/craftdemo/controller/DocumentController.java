package com.intuit.craftdemo.controller;

import com.intuit.craftdemo.entities.Driver;
import com.intuit.craftdemo.exception.DocumentUploadException;
import com.intuit.craftdemo.exception.DriverNotFoundException;
import com.intuit.craftdemo.exception.FileSizeExceededException;
import com.intuit.craftdemo.repository.DocumentRepository;
import com.intuit.craftdemo.repository.DriverRepository;
import com.intuit.craftdemo.service.IDocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("document")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DocumentController {

    final IDocumentService documentService;

    final DriverRepository driverRepository;

    final DocumentRepository documentRepository;

    private static final Logger log = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    public DocumentController(IDocumentService documentService, DriverRepository driverRepository, DocumentRepository documentRepository) {
        this.documentService = documentService;
        this.driverRepository = driverRepository;
        this.documentRepository = documentRepository;
    }

    @PostMapping("/uploadDocuments/{documentType}/{licenseNumber}")
    public ResponseEntity<String>  uploadDocument(
               @PathVariable("documentType") String documentType,
                @PathVariable("licenseNumber") String licenseNumber,
                @RequestPart("document") MultipartFile documentFile,
                Model model) {
            if(documentFile.getSize() > 1024*1024)
            {
                throw new FileSizeExceededException("FileSize is more than 1MB");
            }

            if(documentFile.getOriginalFilename()!=null && !documentFile.getOriginalFilename().endsWith("pdf"))
            {
                return  ResponseEntity.badRequest().body("file type extension not supported. " +
                        "ONly PDF supported");
            }

            Driver driver = driverRepository.findByLicenseNumber(licenseNumber);
            if (driver == null) {
                throw new DriverNotFoundException("Driver not found with license number: " + licenseNumber);
            }
            if (documentFile.isEmpty()) {
                throw new DocumentUploadException("Document is empty");
            }
        //fileTYpe, size of the file
            if (documentService.uploadDocument(licenseNumber, documentFile, documentType)) {
                log.debug("Document Type: {}, License Number: {}", documentType, licenseNumber);
                return ResponseEntity.ok("Document uploaded successfully");
            } else {
                throw new DocumentUploadException("Failed to upload document");
            }
        }

    @GetMapping("/{licenseNumber}/{documentType}")
    public ResponseEntity<byte[]> downloadDocument(
            @PathVariable String licenseNumber,
            @PathVariable String documentType) throws IOException {
        Driver driver = driverRepository.findByLicenseNumber(licenseNumber);
        if (driver == null) {
            throw new DriverNotFoundException("Driver not found with license number: " + licenseNumber);
        }
        byte[] fileContent = documentService.downloadDocument(licenseNumber,documentType);
        if(fileContent == null)
        {
         return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", documentType);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileContent.length)
                .body(fileContent);
    }


    @GetMapping("/downloadAllDocuments/{licenseNumber}")
    public ResponseEntity<byte[]> downloadAllDocuments(@PathVariable String licenseNumber) throws IOException {
        Map<String, String> documentMap = documentService.getAllDocuments(licenseNumber);

        if (documentMap == null || documentMap.isEmpty()) {
            log.debug("No documents found for licenseNumber : {}",licenseNumber);
            return ResponseEntity.notFound().build();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (Map.Entry<String, String> entry : documentMap.entrySet()) {
            String documentType = entry.getKey();
            String fileName = entry.getValue();
            if(!fileName.isEmpty()) {
                baos.write(("Document Type: " + documentType + "\n\n").getBytes());
                byte[] documentContent = documentService.downloadDocument(licenseNumber, documentType);
                if (documentContent != null) {
                    baos.write(documentContent);
                    baos.write("\n\n".getBytes()); //Adding a separator in between docs
                }
            }
        }

        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .body(baos.toByteArray());
    }

}

