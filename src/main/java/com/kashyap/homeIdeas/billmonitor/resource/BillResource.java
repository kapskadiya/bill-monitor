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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping(value = "/get/id/{billId)")
    public ResponseEntity<Bill> getById(@PathVariable String billId) {
        if (StringUtils.isBlank(billId)) {
            log.error("BillId is empty");
            return ResponseEntity.badRequest().body(new Bill());
        }
        return ResponseEntity.ok().body(service.getById(billId));
    }

    @GetMapping(value = "/get/customerId/{customerId}")
    public ResponseEntity<List<Bill>> getByCustomerId(@PathVariable String customerId) {
        if (StringUtils.isBlank(customerId)) {
            log.error("CustomerId is empty");
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }
        return ResponseEntity.ok().body(service.getByCustomerId(customerId));
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<Bill>> getByCustomerName(@PathVariable String customerName) {
        if (StringUtils.isBlank(customerName)) {
            log.error("CustomerId is empty");
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }
        return ResponseEntity.ok().body(service.getByCustomerName(customerName));
    }


    public ResponseEntity<String> update(@RequestBody BillDto dto) {
        if (dto == null) {
            log.error("dto is empty");
            return ResponseEntity.badRequest().body("values are empty");
        }
        final Bill bill = this.prepareBill(dto);
        return ResponseEntity.ok().body(service.update(bill));
    }

    @DeleteMapping(value = "/remove/id/{billId}")
    public void remove(@PathVariable String billId) {
        if (StringUtils.isBlank(billId)) {
            log.error("BillId is empty");
            return;
        }
        service.remove(billId);
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
                .setCustomerId(dto.getCustomerId())
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
