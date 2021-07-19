package com.kashyap.homeIdeas.billmonitor.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AttachmentService {

    boolean uploadFiles(MultipartFile[] files) throws IOException;
}
