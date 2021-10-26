package com.kashyap.homeIdeas.billmonitor.resource;

import com.kashyap.homeIdeas.billmonitor.constant.TimeInterval;
import com.kashyap.homeIdeas.billmonitor.dto.AmountVsTimeDto;
import com.kashyap.homeIdeas.billmonitor.dto.ApplicationResponse;
import com.kashyap.homeIdeas.billmonitor.exception.BillMonitorValidationException;
import com.kashyap.homeIdeas.billmonitor.constant.BillType;
import com.kashyap.homeIdeas.billmonitor.model.ChartValue;
import com.kashyap.homeIdeas.billmonitor.service.AnalyticsService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/rest/analytics")
public class AnalyticsResource {

    @Autowired
    private AnalyticsService service;

    @PostMapping(value = "/amountAndTime")
    public ApplicationResponse getAmountAndTime(@RequestBody AmountVsTimeDto dto) throws IOException {

        final ApplicationResponse response = new ApplicationResponse();

        if (dto == null) {
            throw new BillMonitorValidationException("Give value is empty");
        }

        final BillType type = BillType.getBillType(dto.getBillType());
        final TimeInterval timeIn = TimeInterval.getTimeDuration(dto.getTimeIn());

        final List<ChartValue> data = service.getAmountAndTime(type, timeIn);

        final String message = CollectionUtils.isEmpty(data)
                ? "Data is empty"
                : "Data Found";

        response.setSuccess(true);
        response.setMessage(message);
        response.setCode(HttpStatus.OK.value());
        response.setData(data);

        return response;
    }

    @GetMapping(value = "/maxAmountPerYear")
    public ApplicationResponse getMaxAmountPerYear(@RequestParam(name = "billType") String billType) throws IOException {

        final ApplicationResponse response = new ApplicationResponse();

        if (StringUtils.isBlank(billType)) {
            throw new BillMonitorValidationException("Bill type is empty");
        }

        final BillType type = BillType.getBillType(billType);
        final List<Map<String, Object>> data = service.getMaxAmountPerYear(type);

        final String message = CollectionUtils.isEmpty(data)
                ? "Data is empty"
                : "Data Found";

        response.setSuccess(true);
        response.setMessage(message);
        response.setCode(HttpStatus.OK.value());
        response.setData(data);

        return response;
    }
}
