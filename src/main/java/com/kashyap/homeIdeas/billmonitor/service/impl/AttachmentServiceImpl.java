package com.kashyap.homeIdeas.billmonitor.service.impl;

import com.kashyap.homeIdeas.billmonitor.model.Attachment;
import com.kashyap.homeIdeas.billmonitor.model.AttachmentDetail;
import com.kashyap.homeIdeas.billmonitor.repostiory.AttachmentRepository;
import com.kashyap.homeIdeas.billmonitor.service.AttachmentService;
import com.kashyap.homeIdeas.billmonitor.service.BillService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
@Service
public class AttachmentServiceImpl implements AttachmentService {

    private static final Logger log = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    @Autowired
    private AttachmentRepository repository;

    @Autowired
    private BillService billService;

    public boolean addAttachments(String billId, MultipartFile[] files) throws IOException {
        AttachmentDetail attachmentDetail = new AttachmentDetail();
        List<Attachment> attachmentList = new ArrayList<>();

        if (StringUtils.isBlank(billId)) {
            log.info("billId is empty or null.");
            return false;
        }

        final boolean isBillExist = billService.isExist(billId);

        if (!isBillExist) {
            log.info("bill does not exist in the application.");
            return false;
        }

        final String attachmentDetailId = this.createAttachmentDetailId(billId);
        final Optional<AttachmentDetail> tempAttachmentDetail = repository.findById(attachmentDetailId);

        if (tempAttachmentDetail.isPresent()) {
            attachmentDetail = tempAttachmentDetail.get();
            attachmentList = attachmentDetail.getAttachments();
        } else {
            attachmentDetail.setId(attachmentDetailId);
            attachmentDetail.setBillId(billId);
        }

        if (files != null) {
            final String filePath = "/bill/attachments/";

            long totalSize = 0L;
            for (MultipartFile file: files) {
                totalSize += file.getSize();
            }

            attachmentDetail.setTotalAttachmentSize(totalSize);

            for (MultipartFile file : files) {

                if (file.isEmpty()) {
                    continue;
                }

                Attachment attachment = new Attachment();
                attachment.setId(this.createAttachmentId(billId, file.getOriginalFilename()));

                attachment.setSize(file.getSize());
                final String fileNameWithDivider =file.getOriginalFilename().replace("\\","/");

                final String fileExtension = fileNameWithDivider.substring(fileNameWithDivider.lastIndexOf("/") + 1);
                attachment.setExtension(fileExtension);

                attachment.setType(file.getContentType());

                final File dir = new File(filePath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                final Path path = Paths.get(dir + "/" + file.getOriginalFilename());

                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                attachment.setFilePath(path.toString());
                attachmentList.add(attachment);
            }
            attachmentDetail.setAttachments(attachmentList);

            final AttachmentDetail savedAttachmentDetail = repository.save(attachmentDetail);
            return StringUtils.isNotBlank(savedAttachmentDetail.getId());
        }
        return false;
    }

    @Override
    public boolean removeByBillId(String billId) {
        if (StringUtils.isBlank(billId)) {
            log.info("billId is empty or null.");
            return false;
        }

        repository.deleteById(this.createAttachmentDetailId(billId));
        return true;
    }

    @Override
    public boolean removeByAttachmentIds(String billId, List<String> attachmentIds) throws IOException {
        if (StringUtils.isBlank(billId)) {
            log.info("billId is empty or null.");
            return false;
        }

        final Optional<AttachmentDetail> existingAttachmentDetailOption = repository.findById(
                this.createAttachmentDetailId(billId));

        if (existingAttachmentDetailOption.isPresent()) {
            final AttachmentDetail existingAttachmentDetail = existingAttachmentDetailOption.get();
            final List<Attachment> attachmentList = existingAttachmentDetail.getAttachments();

            final List<Attachment> newAttachmentList = attachmentList.stream()
                    .filter(attachment -> !attachmentIds.contains(attachment.getId()))
                    .collect(Collectors.toList());

            existingAttachmentDetail.setAttachments(newAttachmentList);
            repository.save(existingAttachmentDetail);
            return true;
        }
        return false;
    }

    @Override
    public String createAttachmentDetailId(String billId) {
        return "attachment_"+billId;
    }

    @Override
    public String createAttachmentId(String billId, String fileName) {
        return billId+"_"+fileName;
    }

}
