package com.kashyap.homeIdeas.billmonitor.builder;

import com.kashyap.homeIdeas.billmonitor.model.PaymentDetail;
import com.kashyap.homeIdeas.billmonitor.constant.PaymentMethodType;
import com.kashyap.homeIdeas.billmonitor.constant.PaymentStatus;
import com.kashyap.homeIdeas.billmonitor.constant.PaymentType;
import org.apache.commons.lang3.StringUtils;

/**
 * This is the payment detail builder which can help to build the PaymentDetail object using chain pattern.
 * @author Kashyap Kadiya
 * @since 2021-06
 */
public class PaymentDetailBuilder {

    private final PaymentDetail paymentDetail;

    public PaymentDetailBuilder(){
        paymentDetail = new PaymentDetail();
    }

    public PaymentDetailBuilder setTransactionId(String id) {
        if (StringUtils.isNotBlank(id)) {
            this.paymentDetail.setTransactionId(id);
        }
        return this;
    }

    public PaymentDetailBuilder setMethod(String method) {
        if (StringUtils.isNotBlank(method)) {
            this.paymentDetail.setMethod(PaymentMethodType.getPaymentMethodType(method));
        }
        return this;
    }

    public PaymentDetailBuilder setStatus(String status) {
        if (StringUtils.isNotBlank(status)) {
            this.paymentDetail.setStatus(PaymentStatus.getPaymentStatus(status));
        }
        return this;

    }

    public PaymentDetailBuilder setType(String type) {
        if (StringUtils.isNotBlank(type)) {
            this.paymentDetail.setType(PaymentType.getPaymentType(type));
        }
        return this;
    }

    public PaymentDetailBuilder setMethodNumber(String number) {
        if (StringUtils.isNotBlank(number)) {
            this.paymentDetail.setMethodNumber(number);
        }
        return this;
    }

    public PaymentDetail build() {
        return this.paymentDetail;
    }

}
