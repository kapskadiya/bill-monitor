package com.kashyap.homeIdeas.billmonitor.resource;

import com.kashyap.homeIdeas.billmonitor.builder.BillBuilder;
import com.kashyap.homeIdeas.billmonitor.builder.PaymentDetailBuilder;
import com.kashyap.homeIdeas.billmonitor.dto.ApplicationResponse;
import com.kashyap.homeIdeas.billmonitor.dto.BillDto;
import com.kashyap.homeIdeas.billmonitor.dto.PaymentDetailDto;
import com.kashyap.homeIdeas.billmonitor.exception.BillMonitorValidationException;
import com.kashyap.homeIdeas.billmonitor.model.Bill;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        service.save(bill);

        response.setSuccess(true);
        response.setMessage("resource saved successfully");
        response.setCode(HttpStatus.CREATED.value());
        return response;
    }

    @PutMapping("/update")
    public ApplicationResponse update(@RequestBody BillDto dto) {
        final ApplicationResponse response = new ApplicationResponse();

        final Bill bill = this.prepareBill(dto);
        service.update(bill);

        response.setSuccess(true);
        response.setMessage("resource updated successfully");
        response.setCode(HttpStatus.NO_CONTENT.value());
        return response;
    }

    @PostMapping(value = "/bulk/create")
    public ApplicationResponse bulkCreate(@RequestBody List<BillDto> dtoList) throws IOException {
        final ApplicationResponse response = new ApplicationResponse();

        if (CollectionUtils.isEmpty(dtoList)) {
           throw new BillMonitorValidationException("Given data is empty");
        }

        final List<Bill> billLIst = dtoList
                .stream()
                .map(this::prepareBill)
                .collect(Collectors.toList());

        service.bulkSave(billLIst);

        response.setSuccess(true);
        response.setMessage("resource saved successfully");
        response.setCode(HttpStatus.CREATED.value());
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

        final List<Bill> billList = service.getByIsDeleted(false);

        response.setSuccess(true);
        response.setMessage("Data Found");
        response.setData(billList);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @GetMapping(value = "/getDeletedBills")
    public ApplicationResponse getDeletedBills() {
        final ApplicationResponse response = new ApplicationResponse();

        final List<Bill> billList = service.getByIsDeleted(true);

        response.setSuccess(true);
        response.setMessage("Data Found");
        response.setData(billList);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @GetMapping(value = "/get")
    public ApplicationResponse get(@RequestParam(value = "id", required = false) String id,
                                   @RequestParam(value = "billId", required = false) String billId,
                                   @RequestParam(value = "customerId", required = false) String customerId) {
        final ApplicationResponse response = new ApplicationResponse();

        if (StringUtils.isBlank(id) && StringUtils.isBlank(billId) && StringUtils.isBlank(customerId)) {
            throw new BillMonitorValidationException("Given data is empty");
        }

        final List<Bill> billList = new ArrayList<>();

        if (StringUtils.isNotBlank(id)) {
            billList.add(service.getById(id));
        } else if (StringUtils.isNotBlank(billId)) {
            billList.add(service.getByBillId(billId));
        } else if (StringUtils.isNotBlank(customerId)) {
            billList.addAll(service.getByCustomerId(customerId));
        }

        response.setSuccess(true);
        response.setMessage("Data Found");
        response.setData(billList);
        response.setCode(HttpStatus.OK.value());
        return response;

    }

    @DeleteMapping(value = "/delete/billId")
    public ApplicationResponse deleteById(@RequestParam(value = "billId") String billId) throws IOException {
        final ApplicationResponse response = new ApplicationResponse();

        if (StringUtils.isBlank(billId)) {
            log.error("BillId is empty");
            throw new BillMonitorValidationException("Given Data is empty");
        }
        service.deleteById(billId);

        response.setSuccess(true);
        response.setMessage("Bill deleted successfully");
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @DeleteMapping(value = "/delete/customerId")
    public ApplicationResponse deleteByCustomerId(@RequestParam(value = "customerId") String customerId) throws IOException {
        final ApplicationResponse response = new ApplicationResponse();

        if (StringUtils.isBlank(customerId)) {
            log.error("serviceNo is empty");
            throw new BillMonitorValidationException("Given Data is empty");
        }
        service.deleteByCustomerId(customerId);

        response.setSuccess(true);
        response.setMessage("Bill deleted successfully");
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    private Bill prepareBill(BillDto billDto) {

        final BillDto dto = Optional.ofNullable(billDto)
                .orElseThrow(() -> new BillMonitorValidationException("Given data is empty"));

        final Bill bill = new BillBuilder()
                .setBillId(dto.getBillId())
                .setOrgName(dto.getOrgName())
                .setCustomerId(dto.getCustomerId())
                .setType(dto.getType())
                .setTotalAmount(dto.getTotalAmount())
                .setIssueDate(dto.getIssueDate())
                .setDueDate(dto.getDueDate())
                .setPayDate(dto.getPayDate())
                .setTotalAmountAfterExpiry(dto.getTotalAmountAfterExpiry())
                .setExtraInfo(dto.getExtraInfo())
                .build();

        if(dto.getPaymentDetail() != null) {
            final PaymentDetailDto paymentDetailDto = dto.getPaymentDetail();
            final PaymentDetail paymentDetail = new PaymentDetailBuilder()
                    .setTransactionId(paymentDetailDto.getTransactionId())
                    .setMethod(paymentDetailDto.getMethod())
                    .setStatus(paymentDetailDto.getStatus())
                    .setMethodNumber(paymentDetailDto.getMethodNumber())
                    .setType(paymentDetailDto.getType())
                    .build();

            bill.setPaymentDetail(paymentDetail);
        }

        return Optional.ofNullable(bill)
                .orElseThrow(() -> new BillMonitorValidationException("Data is not prepared properly"));
    }
}
