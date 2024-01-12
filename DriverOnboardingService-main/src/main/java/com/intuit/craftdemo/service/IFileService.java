package com.intuit.craftdemo.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileService {

    public String uploadFile(MultipartFile file) throws IOException;

    public byte[] downloadFile(String fileName)throws IOException;
}
