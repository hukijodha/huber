package com.intuit.craftdemo.service.impl;

import com.intuit.craftdemo.entities.Driver;
import com.intuit.craftdemo.enums.DriverStatus;
import com.intuit.craftdemo.exception.DocumentsNotFoundException;
import com.intuit.craftdemo.repository.DocumentRepository;
import com.intuit.craftdemo.repository.DriverRepository;
import com.intuit.craftdemo.service.IDocumentService;
import com.intuit.craftdemo.service.IFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements IDocumentService {

    private final DriverRepository driverRepository;
    private final DocumentRepository documentRepository;

    private final IFileService fileService;

    private final EventProducerService publisherMessageService;

    private static final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);

    @Autowired
    public DocumentServiceImpl(DriverRepository driverRepository, DocumentRepository documentRepository, IFileService fileService, EventProducerService publisherMessageService) {
        this.driverRepository = driverRepository;
        this.documentRepository= documentRepository;
        this.fileService = fileService;
        this.publisherMessageService = publisherMessageService;
    }


    @Override
    @Transactional
    public boolean uploadDocument(String licenseNumber, MultipartFile document, String documentType) {
       Driver driver = driverRepository.findByLicenseNumber(licenseNumber);
       Long id = driver.getId();
       Map<String, String> documents = driver.getDocuments();
       boolean documentUploaded = false;
           if(documents.get(documentType)!=null)
           {
               try {
               String fileName = fileService.uploadFile(document);
               documentRepository.updateDocumentName(id,documentType,fileName);
               documentUploaded=true;
                   if(getNonSubmittedDocumentsList(licenseNumber).isEmpty()
                           && driver.getStatus().toString().equalsIgnoreCase(DriverStatus.APPLICATION_SUBMITTED.toString()) )
                   {
                       driverRepository.updateDriverStatusById(driver.getId(), DriverStatus.ALL_DOCUMENTS_SUBMITTED.toString());
                       publisherMessageService.producePublishingEvent(driver,
                               documentRepository.findAllDocumentsByDriverId(driver.getId()));
                   }
               } catch(Exception e)
              {
                log.error("got error while uploading {}", e.getMessage());
              }
           }
           else{
               throw new DocumentsNotFoundException("This document type is not valid");
           }
        return documentUploaded;
    }

    @Override
    public byte[] downloadDocument(String licenseNumber, String documentType) {
       log.debug("Downloading [ {} ] Document for licenseNumber {}", documentType,licenseNumber);
        Driver driver = driverRepository.findByLicenseNumber(licenseNumber);
        Long id = driver.getId();
        Map<String, String> documents = driver.getDocuments();
        if(documents.get(documentType)!=null && !documents.get(documentType).isEmpty())
        {
            Optional<String> docName =  documentRepository.findDocumentFileNameByDriverIdAndDocumentType(id,documentType);
            String fileName = docName.orElse(null);
            log.debug("Looking for document with _id {} in MongoDB-filesDb: ", fileName);
            if(fileName!=null) {
                try {
                    return fileService.downloadFile(fileName);
                } catch (IOException e) {
                   log.error("Error in downloading the file {}  : {}",fileName,e.getMessage());
                }
            }
        }
        else{
           log.info("No file present for docType {} for licenseNumber {}" , documentType, licenseNumber);
            return null;
        }
       return null;
    }

    @Override
    public Map<String, String> getAllDocuments(String licenseNumber) {
        log.info("Getting list of all documents for license Number {}",licenseNumber);
        Driver driver = driverRepository.findByLicenseNumber(licenseNumber);
        return driver.getDocuments();
    }

    @Override
    public List<String> getNonSubmittedDocumentsList(String licenseNumber) {
        Driver driver = driverRepository.findByLicenseNumber(licenseNumber);
        List<String> requiredDocumentsPending = new ArrayList<>();
        for (Map.Entry<String, String> entry : driver.getDocuments().entrySet())
        {
            String documentType = entry.getKey();
            String fileName = entry.getValue();
            if(fileName.isEmpty())
            {
                requiredDocumentsPending.add(documentType);
            }
        }
        return requiredDocumentsPending;
    }

    @Override
    public List<String> getVerificationFailedDocumentList(String licenseNumber) {
        Driver driver = driverRepository.findByLicenseNumber(licenseNumber);
        return documentRepository.findDocumentTypesWithVerificationFailedStatusByDriverId(driver.getId());
    }


}
