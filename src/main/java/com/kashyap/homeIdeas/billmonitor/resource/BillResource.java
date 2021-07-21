package com.kashyap.homeIdeas.billmonitor.resource;

import com.kashyap.homeIdeas.billmonitor.builder.BillBuilder;
import com.kashyap.homeIdeas.billmonitor.builder.PaymentDetailBuilder;
import com.kashyap.homeIdeas.billmonitor.dto.BillDto;
import com.kashyap.homeIdeas.billmonitor.dto.PaymentDetailDto;
import com.kashyap.homeIdeas.billmonitor.model.Bill;
import com.kashyap.homeIdeas.billmonitor.model.BillType;
import com.kashyap.homeIdeas.billmonitor.model.PaymentDetail;
import com.kashyap.homeIdeas.billmonitor.service.AttachmentService;
import com.kashyap.homeIdeas.billmonitor.service.BillService;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping(value = "/rest/bill")
public class BillResource {

    private static final Logger log = LoggerFactory.getLogger(BillResource.class);

    @Autowired
    private BillService service;

    @Autowired
    private AttachmentService attachmentService;

    @PostMapping(value = "/add")
    public ResponseEntity<String> save(@RequestBody BillDto dto) {
        final Bill bill = this.prepareBill(dto);

        return ResponseEntity.ok().body(service.save(bill));
    }

    @PostMapping(value = "/add/withAttachment")
    public ResponseEntity<String> saveWithAttachment(HttpServletRequest request,
                                                      @RequestPart(value = "billDto") BillDto dto,
                                                      @RequestPart(value = "files") MultipartFile[] files) {

        final Bill bill = this.prepareBill(dto);
        final String billId = service.save(bill);

        if (StringUtils.isBlank(billId)) {
            log.error("bill save failed.");
            return ResponseEntity.ok().body(StringUtils.EMPTY);
        }

        final boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            try {
                final boolean result = attachmentService.addAttachments(billId, files);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok().body(billId);
    }

    private Bill prepareBill(BillDto dto) {
        final PaymentDetailDto paymentDetailDto = dto.getPaymentDetail();
        final PaymentDetail paymentDetail = new PaymentDetailBuilder()
                .setId(paymentDetailDto.getId())
                .setMethod(paymentDetailDto.getMethod())
                .setPayBy(paymentDetailDto.getPayBy())
                .setPlatform(paymentDetailDto.getPlatform())
                .build();

        return new BillBuilder()
                .setOrgName(dto.getOrgName())
                .setCustomerName(dto.getCustomerName())
                .setType(BillType.getBillType(dto.getType()))
                .setAmountToBePay(dto.getAmountToBePay())
                .setIssueDate(dto.getIssueDate())
                .setDueDate(dto.getDueDate())
                .setPaymentDetail(paymentDetail)
                .setBillingDurationInDays(dto.getBillingDurationInDays())
                .setMetadata(dto.getMetadata())
                .build();
    }
}
