package com.intuit.craftdemo.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IDocumentService {

     boolean uploadDocument(String licenseNumber, MultipartFile document,String documentType);

     byte[] downloadDocument(String licenseNumber, String DocumentType);

     Map<String, String> getAllDocuments(String licenseNumber);

     List<String> getNonSubmittedDocumentsList(String licenseNumber);

     List<String> getVerificationFailedDocumentList(String licenseNumber);

}
