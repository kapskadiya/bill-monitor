package com.kashyap.homeIdeas.billmonitor.resource;

import com.kashyap.homeIdeas.billmonitor.builder.BillBuilder;
import com.kashyap.homeIdeas.billmonitor.builder.PaymentDetailBuilder;
import com.kashyap.homeIdeas.billmonitor.dto.ApplicationResponse;
import com.kashyap.homeIdeas.billmonitor.dto.BillDto;
import com.kashyap.homeIdeas.billmonitor.dto.PaymentDetailDto;
import com.kashyap.homeIdeas.billmonitor.exception.BillMonitorValidationException;
import com.kashyap.homeIdeas.billmonitor.model.Bill;
import com.kashyap.homeIdeas.billmonitor.model.BillType;
import com.kashyap.homeIdeas.billmonitor.model.PaymentDetail;
import com.kashyap.homeIdeas.billmonitor.service.AttachmentService;
import com.kashyap.homeIdeas.billmonitor.service.BillService;
import com.kashyap.homeIdeas.billmonitor.service.BillTypeService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
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

import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.BILL_DELETED_SUCCESSFULLY;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.BILL_ID;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.CUSTOMER_ID;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.DATA_FOUND;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.DATA_INVALID;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.DATA_NOT_PREPARED;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.ID;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.RESOURCE_SAVED_SUCCESSFULLY;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.RESOURCE_UPDATED_SUCCESSFULLY;

/**
 * This is the Bill resource which can help to manage bills related operations. like Create, Update, View, and Delete
 * @author Kashyap Kadiya
 * @since 2021-06
 */
@RestController
@RequestMapping(value = "/rest/bill")
public class BillResource {

    @Autowired
    private BillService service;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private BillTypeService billTypeService;

    @Operation(summary = "Create new bill")
    @PostMapping(value = "/create")
    public ApplicationResponse create(@RequestBody BillDto dto) {
        final ApplicationResponse response = new ApplicationResponse();

        final Bill bill = this.prepareBill(dto);
        service.save(bill);

        response.setSuccess(true);
        response.setMessage(RESOURCE_SAVED_SUCCESSFULLY);
        response.setCode(HttpStatus.CREATED.value());
        return response;
    }

    @Operation(summary = "Update existing bill")
    @PutMapping("/update")
    public ApplicationResponse update(@RequestBody BillDto dto) {
        final ApplicationResponse response = new ApplicationResponse();

        final Bill bill = this.prepareBill(dto);
        service.update(bill);

        response.setSuccess(true);
        response.setMessage(RESOURCE_UPDATED_SUCCESSFULLY);
        response.setCode(HttpStatus.NO_CONTENT.value());
        return response;
    }

    @Operation(summary = "Create new bills in bulk")
    @PostMapping(value = "/bulk/create")
    public ApplicationResponse bulkCreate(@RequestBody List<BillDto> dtoList) throws IOException {
        final ApplicationResponse response = new ApplicationResponse();

        if (CollectionUtils.isEmpty(dtoList)) {
           throw new BillMonitorValidationException(DATA_INVALID+ " Data:"+dtoList);
        }

        final List<Bill> billLIst = dtoList
                .stream()
                .map(this::prepareBill)
                .collect(Collectors.toList());

        service.bulkSave(billLIst);

        response.setSuccess(true);
        response.setMessage(RESOURCE_SAVED_SUCCESSFULLY);
        response.setCode(HttpStatus.CREATED.value());
        return response;
    }

    @Operation(summary = "Add attachment to the bill")
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

    @Operation(summary = "Get non deleted bills", hidden = true)
    @GetMapping(value = "/getNonDeletedBills")
    public ApplicationResponse getNonDeletedBills() {
        final ApplicationResponse response = new ApplicationResponse();

        final List<Bill> billList = service.getByIsDeleted(false);

        response.setSuccess(true);
        response.setMessage(DATA_FOUND);
        response.setData(billList);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @Operation(summary = "Get deleted bills", hidden = true)
    @GetMapping(value = "/getDeletedBills")
    public ApplicationResponse getDeletedBills() {
        final ApplicationResponse response = new ApplicationResponse();

        final List<Bill> billList = service.getByIsDeleted(true);

        response.setSuccess(true);
        response.setMessage(DATA_FOUND);
        response.setData(billList);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @Operation(summary = "Get bill using one of the keyword:ESId, BillId, CustomerId")
    @GetMapping(value = "/get")
    public ApplicationResponse get(@RequestParam(value = ID, required = false) String id,
                                   @RequestParam(value = BILL_ID, required = false) String billId,
                                   @RequestParam(value = CUSTOMER_ID, required = false) String customerId) {
        final ApplicationResponse response = new ApplicationResponse();

        if (StringUtils.isBlank(id) && StringUtils.isBlank(billId) && StringUtils.isBlank(customerId)) {
            throw new BillMonitorValidationException(DATA_INVALID);
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
        response.setMessage(DATA_FOUND);
        response.setData(billList);
        response.setCode(HttpStatus.OK.value());
        return response;

    }

    @Operation(summary = "Delete bill using billId")
    @DeleteMapping(value = "/delete/billId")
    public ApplicationResponse deleteById(@RequestParam(value = BILL_ID) String billId) throws IOException {
        final ApplicationResponse response = new ApplicationResponse();

        if (StringUtils.isBlank(billId)) {
            throw new BillMonitorValidationException(DATA_INVALID+ " Data:"+billId);
        }
        service.deleteById(billId);

        response.setSuccess(true);
        response.setMessage(BILL_DELETED_SUCCESSFULLY);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @Operation(summary = "Delete bill using customerId")
    @DeleteMapping(value = "/delete/customerId")
    public ApplicationResponse deleteByCustomerId(@RequestParam(value = CUSTOMER_ID) String customerId) throws IOException {
        final ApplicationResponse response = new ApplicationResponse();

        if (StringUtils.isBlank(customerId)) {
            throw new BillMonitorValidationException(DATA_INVALID+ " Data:"+customerId);
        }
        service.deleteByCustomerId(customerId);

        response.setSuccess(true);
        response.setMessage(BILL_DELETED_SUCCESSFULLY);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @Operation(summary = "Delete bill using es id")
    @DeleteMapping(value = "/delete/id")
    public ApplicationResponse deleteByESId(@RequestParam(value = "id") String id) throws IOException {
        final ApplicationResponse response = new ApplicationResponse();

        if (StringUtils.isBlank(id)) {
            throw new BillMonitorValidationException(DATA_INVALID+ " Data:"+id);
        }
        service.deleteByESId(id);

        response.setSuccess(true);
        response.setMessage(BILL_DELETED_SUCCESSFULLY);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    private Bill prepareBill(BillDto billDto) {

        final BillDto dto = Optional.ofNullable(billDto)
                .orElseThrow(() -> new BillMonitorValidationException(DATA_INVALID));

        final BillType type = billTypeService.getBillType(dto.getType());

        final Bill bill = new BillBuilder()
                .setBillId(dto.getBillId())
                .setOrgName(dto.getOrgName())
                .setCustomerId(dto.getCustomerId())
                .setType(type.getId())
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
                .orElseThrow(() -> new BillMonitorValidationException(DATA_NOT_PREPARED));
    }
}
