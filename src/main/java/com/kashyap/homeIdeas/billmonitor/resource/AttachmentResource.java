package com.kashyap.homeIdeas.billmonitor.resource;

import com.kashyap.homeIdeas.billmonitor.dto.AttachmentRemoveDto;
import com.kashyap.homeIdeas.billmonitor.service.AttachmentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * This is the Attachment resource which can help to manage attachment related operations. like Add, Delete
 * @author Kashyap Kadiya
 * @since 2021-06
 */
@RestController
@RequestMapping(value = "/rest/attachment")
public class AttachmentResource {

    private static final Logger log = LoggerFactory.getLogger(AttachmentResource.class);

    @Autowired
    private AttachmentService service;

    @PostMapping(value = "/upload")
    public ResponseEntity<Boolean> add(HttpServletRequest request,
                                                    @RequestPart(value = "files") MultipartFile[] files,
                                                    @RequestPart(value = "billId") String billId) {
        if (StringUtils.isBlank(billId)) {
            log.info("billId is empty or null.");
            return ResponseEntity.badRequest().body(false);
        }

        final boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            try {
                final boolean result = service.addAttachments(billId, files);
                return ResponseEntity.ok().body(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.badRequest().body(false);
    }

    @DeleteMapping(value = "/remove/billId/{billId}")
    public ResponseEntity<Boolean> removeByBillId(@PathVariable String billId) {
        if (StringUtils.isBlank(billId)) {
            log.info("billId is empty or null.");
            return ResponseEntity.badRequest().body(false);
        }

        final boolean isRemoved = service.removeByBillId(billId);
        return ResponseEntity.ok().body(isRemoved);
    }

    @DeleteMapping(value = "/remove")
    public ResponseEntity<Boolean> remove(@RequestBody AttachmentRemoveDto removeDto) throws IOException {
        if (StringUtils.isBlank(removeDto.getBillId())) {
            log.info("billId is empty or null.");
            return ResponseEntity.badRequest().body(false);
        }

        final boolean isRemoved = service.removeByAttachmentIds(removeDto.getBillId(),
                                                                removeDto.getAttachmentIds());
        return ResponseEntity.ok().body(isRemoved);
    }
}
