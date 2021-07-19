package com.kashyap.homeIdeas.billmonitor.resource;

import com.kashyap.homeIdeas.billmonitor.service.AttachmentService;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping(value = "/rest/attachment")
public class AttachmentResource {

    @Autowired
    private AttachmentService service;

    @PostMapping(value = "")
    public ResponseEntity<Boolean> uploadAttachment(HttpServletRequest request, @RequestPart("files") MultipartFile[] files) {
        final boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (isMultipart) {
            try {
                final boolean result = service.uploadFiles(files);

                return ResponseEntity.ok().body(result);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ResponseEntity.badRequest().body(false);
    }
}
