package com.kashyap.homeIdeas.billmonitor.service.impl;

import com.kashyap.homeIdeas.billmonitor.model.Attachment;
import com.kashyap.homeIdeas.billmonitor.model.AttachmentDetail;
import com.kashyap.homeIdeas.billmonitor.repostiory.AttachmentRepository;
import com.kashyap.homeIdeas.billmonitor.service.AttachmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentRepository repository;

    public boolean uploadFiles(MultipartFile[] files) throws IOException {
        final AttachmentDetail attachmentDetail = new AttachmentDetail();

        if (files != null) {
            final List<Attachment> attachmentList = new ArrayList<>();
            final String filePath = "/bill/attachments/";

            for (MultipartFile file : files) {

                if (file.isEmpty()) {
                    continue;
                }

                final Attachment attachment = new Attachment();

                attachment.setSize(file.getSize());
                final String fileNameWithDivider =file.getName().replace("\\","/");

                final String fileExtension = fileNameWithDivider.substring(fileNameWithDivider.lastIndexOf("/") + 1);
                attachment.setExtension(fileExtension);

                attachment.setType(file.getContentType());

                final File dir = new File(filePath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                final Path path = Paths.get(dir + "/" + file.getOriginalFilename());

                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                attachmentList.add(attachment);
            }
            attachmentDetail.setAttachments(attachmentList);

            final AttachmentDetail savedAttachmentDetail = repository.save(attachmentDetail);
            return StringUtils.isNotBlank(savedAttachmentDetail.getId());

        }
        return false;

    }

}
