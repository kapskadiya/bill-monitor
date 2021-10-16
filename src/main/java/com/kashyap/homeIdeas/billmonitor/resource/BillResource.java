package com.kashyap.homeIdeas.billmonitor.resource;

import com.kashyap.homeIdeas.billmonitor.builder.BillBuilder;
import com.kashyap.homeIdeas.billmonitor.builder.PaymentDetailBuilder;
import com.kashyap.homeIdeas.billmonitor.dto.ApplicationResponse;
import com.kashyap.homeIdeas.billmonitor.dto.BillDto;
import com.kashyap.homeIdeas.billmonitor.dto.Failure;
import com.kashyap.homeIdeas.billmonitor.dto.PaymentDetailDto;
import com.kashyap.homeIdeas.billmonitor.model.Bill;
import com.kashyap.homeIdeas.billmonitor.model.BillType;
import com.kashyap.homeIdeas.billmonitor.model.PaymentDetail;
import com.kashyap.homeIdeas.billmonitor.service.AttachmentService;
import com.kashyap.homeIdeas.billmonitor.service.BillService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/rest/bill")
public class BillResource {

    private static final Logger log = LoggerFactory.getLogger(BillResource.class);

    @Autowired
    private BillService service;

    @Autowired
    private AttachmentService attachmentService;

    @PostMapping(value = "/create")
    public ApplicationResponse create(@RequestBody BillDto dto) {
        final ApplicationResponse response = new ApplicationResponse();
        final Bill bill = this.prepareBill(dto);

        try {
            service.save(bill);
        } catch (IllegalArgumentException iae) {
            final Failure failure = new Failure();
            failure.setReason(iae.getMessage());
            failure.setException(iae.toString());
            response.setFailure(failure);
            response.setHttpCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }
        final Map<String, Object> success = new HashMap<>();
        success.put("isSaved", true);
        success.put("message", "resource saved successfully");
        response.setSuccess(success);

        response.setHttpCode(HttpStatus.CREATED.value());
        return response;
    }

    @PutMapping("/update")
    public ApplicationResponse update(@RequestBody BillDto dto) {
        final ApplicationResponse response = new ApplicationResponse();
        if (dto == null) {
            final Failure failure = new Failure();
            failure.setReason("Request is not valid");
            response.setFailure(failure);
            response.setHttpCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }
        final Bill bill = this.prepareBill(dto);
        try {
            service.update(bill);
        } catch (IllegalArgumentException iae) {
            final Failure failure = new Failure();
            failure.setReason(iae.getMessage());
            failure.setException(iae.toString());
            response.setFailure(failure);
            response.setHttpCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }
        final Map<String, Object> success = new HashMap<>();
        success.put("isSaved", true);
        success.put("message", "resources saved successfully");
        response.setSuccess(success);

        response.setHttpCode(HttpStatus.OK.value());
        return response;
    }

    @PostMapping(value = "/bulk/create")
    public ApplicationResponse bulkCreate(@RequestBody List<BillDto> dtoList) throws IOException {
        final ApplicationResponse response = new ApplicationResponse();
        if (CollectionUtils.isEmpty(dtoList)) {
            final Failure failure = new Failure();
            failure.setReason("Request is not valid");
            response.setFailure(failure);
            response.setHttpCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }
        final List<Bill> billLIst = dtoList
                .stream()
                .map(this::prepareBill)
                .collect(Collectors.toList());

        try {
            service.bulkSave(billLIst);
        } catch (IllegalArgumentException iae) {
            final Failure failure = new Failure();
            failure.setReason(iae.getMessage());
            failure.setException(iae.toString());
            response.setFailure(failure);
            response.setHttpCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }
        final Map<String, Object> success = new HashMap<>();
        success.put("isSaved", true);
        success.put("message", "resources saved successfully");
        response.setSuccess(success);

        response.setHttpCode(HttpStatus.CREATED.value());
        return response;
    }

    @PostMapping(value = "/add/withAttachment")
    public ResponseEntity<String> saveWithAttachment(HttpServletRequest request,
                                                      @RequestPart(value = "billDto") BillDto dto,
                                                      @RequestPart(value = "files") MultipartFile[] files) {

        final Bill bill = this.prepareBill(dto);
        service.save(bill);

//        if (StringUtils.isBlank(billId)) {
//            log.error("bill save failed.");
//            return ResponseEntity.ok().body(StringUtils.EMPTY);
//        }

        final boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            try {
                final boolean result = attachmentService.addAttachments(bill.getId(), files);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok().body(bill.getId());
    }

    @GetMapping(value = "/getNonDeletedBills")
    public ApplicationResponse getNonDeletedBills() {
        final ApplicationResponse response = new ApplicationResponse();
 //       final List<BillDto> billDtoList = new ArrayList<>();
        final List<Bill> billList;

        try {
            billList = new ArrayList<>(service.getAll());
//            if (CollectionUtils.isNotEmpty(billList)) {
//                billDtoList.addAll(billList.stream().map())
//            }
        } catch (IllegalArgumentException iae) {
            final Failure failure = new Failure();
            failure.setReason(iae.getMessage());
            failure.setException(iae.toString());
            response.setFailure(failure);
            response.setHttpCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }
        final Map<String, Object> success = new HashMap<>();
        success.put("nonDeletedBills", billList);
        response.setSuccess(success);

        response.setHttpCode(HttpStatus.CREATED.value());
        return response;
    }

    @GetMapping(value = "/getDeletedBills")
    public ApplicationResponse getDeletedBills() {
        final ApplicationResponse response = new ApplicationResponse();
        //       final List<BillDto> billDtoList = new ArrayList<>();
        final List<Bill> billList;

        try {
            billList = new ArrayList<>(service.getAll());
//            if (CollectionUtils.isNotEmpty(billList)) {
//                billDtoList.addAll(billList.stream().map())
//            }
        } catch (IllegalArgumentException iae) {
            final Failure failure = new Failure();
            failure.setReason(iae.getMessage());
            failure.setException(iae.toString());
            response.setFailure(failure);
            response.setHttpCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }
        final Map<String, Object> success = new HashMap<>();
        success.put("deletedBills", billList);
        response.setSuccess(success);

        response.setHttpCode(HttpStatus.CREATED.value());
        return response;
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
                .setTransactionId(paymentDetailDto.getTransactionId())
                .setMethod(paymentDetailDto.getMethod())
                .setStatus(paymentDetailDto.getStatus())
                .setMethodNumber(paymentDetailDto.getMethodNumber())
                .setType(paymentDetailDto.getType())
                .build();

        return new BillBuilder()
                .setId(dto.getBillId())
                .setOrgName(dto.getOrgName())
                .setUserId(dto.getUserId())
                .setServiceId(dto.getServiceId())
                .setType(dto.getType())
                .setTotalAmount(dto.getTotalAmount())
                .setIssueDate(dto.getIssueDate())
                .setDueDate(dto.getDueDate())
                .setPayDate(dto.getPayDate())
                .setPaymentDetail(paymentDetail)
                .setTotalAmountAfterExpiry(dto.getTotalAmountAfterExpiry())
                .setExtraInfo(dto.getExtraInfo())
                .build();
    }
}
