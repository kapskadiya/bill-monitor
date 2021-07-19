package com.kashyap.homeIdeas.billmonitor.resource;

import com.kashyap.homeIdeas.billmonitor.builder.BillBuilder;
import com.kashyap.homeIdeas.billmonitor.builder.PaymentDetailBuilder;
import com.kashyap.homeIdeas.billmonitor.dto.BillDto;
import com.kashyap.homeIdeas.billmonitor.dto.PaymentDetailDto;
import com.kashyap.homeIdeas.billmonitor.model.Bill;
import com.kashyap.homeIdeas.billmonitor.model.BillType;
import com.kashyap.homeIdeas.billmonitor.model.PaymentDetail;
import com.kashyap.homeIdeas.billmonitor.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/bill")
public class BillResource {

    @Autowired
    private BillService service;

    @PostMapping(value = "")
    public ResponseEntity<Boolean> save(@RequestBody BillDto dto) {
        final Bill bill = this.prepareBill(dto);

        return ResponseEntity.ok().body(service.save(bill));
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
