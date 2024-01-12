package com.intuit.craftdemo.service.impl;

import com.intuit.craftdemo.service.IFileService;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


@Service
public class FileServiceImpl implements IFileService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations operations;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        ObjectId fileName=gridFsTemplate.store(
                file.getInputStream(), file.getName(), file.getContentType());
        return fileName.toString();
    }
    @Override
    public byte[] downloadFile(String fileName) throws IOException{
        if(fileName.isBlank()) {return null;}
        System.out.println("Finding data with id :"+ fileName);
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileName)));
        if(file!=null) {
            System.out.println("GRIDfsFiLe is : "+ file.getFilename() + "Content as "+ file.getObjectId());
            return operations.getResource(file).getInputStream().readAllBytes();
        }
        return null;
    }
}
