package com.intuit.craftdemo.service.impl;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileServiceImplTest {

    @Mock
    private GridFsTemplate gridFsTemplate;

    @Mock
    private GridFsOperations operations;

    @InjectMocks
    private FileServiceImpl fileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void uploadFile_Success() throws IOException {
        MultipartFile file = new MockMultipartFile("test.txt", "test.txt",
                "text/plain", "Test data".getBytes());

        ObjectId fileId = new ObjectId();
        when(gridFsTemplate.store(any(InputStream.class), eq(file.getName()), eq(file.getContentType())))
                .thenReturn(fileId);

        String result = fileService.uploadFile(file);

        assertEquals(fileId.toString(), result);
        verify(gridFsTemplate, times(1)).store(any(InputStream.class), eq(file.getName()), eq(file.getContentType()));
    }


    @Test
    void downloadFile_FileNotFound() throws IOException {
        String fileName = "test.txt";
        ObjectId fileId = new ObjectId();
        when(gridFsTemplate.findOne(any(Query.class))).thenReturn(null);
        byte[] result = fileService.downloadFile(fileId.toString());
        assertNull(result);
        verify(gridFsTemplate, times(1)).findOne(any(Query.class));
        verifyNoInteractions(operations);
    }


}
