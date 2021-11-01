package com.kashyap.homeIdeas.billmonitor.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
public interface AttachmentService {

    boolean addAttachments(String billId, MultipartFile[] files) throws IOException;

    boolean removeByBillId(String billId);

    boolean removeByAttachmentIds(String billId, List<String> attachmentIds) throws IOException;

    String createAttachmentDetailId(String billId);

    String createAttachmentId(String billId, String fileName);
}
