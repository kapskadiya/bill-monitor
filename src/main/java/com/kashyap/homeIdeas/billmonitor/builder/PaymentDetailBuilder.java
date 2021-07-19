package com.kashyap.homeIdeas.billmonitor.builder;

import com.kashyap.homeIdeas.billmonitor.model.PaymentDetail;
import org.apache.commons.lang3.StringUtils;

public class PaymentDetailBuilder {

    private final PaymentDetail paymentDetail;

    public PaymentDetailBuilder(){
        paymentDetail = new PaymentDetail();
    }

    public PaymentDetailBuilder setId(String id) {
        if (StringUtils.isNotBlank(id)) {
            this.paymentDetail.setId(id);
        }
        return this;
    }

    public PaymentDetailBuilder setMethod(String method) {
        if (StringUtils.isNotBlank(method)) {
            this.paymentDetail.setMethod(method);
        }
        return this;
    }

    public PaymentDetailBuilder setPlatform(String platform) {
        if (StringUtils.isNotBlank(platform)) {
            this.paymentDetail.setPlatform(platform);
        }
        return this;
    }

    public PaymentDetailBuilder setPayBy(String payBy) {
        if (StringUtils.isNotBlank(payBy)) {
            this.paymentDetail.setPayBy(payBy);
        }
        return this;
    }

    public PaymentDetail build() {
        return this.paymentDetail;
    }

}
