package com.kashyap.homeIdeas.billmonitor.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kashyap.homeIdeas.billmonitor.dto.ApplicationResponse;
import com.kashyap.homeIdeas.billmonitor.exception.BillMonitorValidationException;
import com.kashyap.homeIdeas.billmonitor.model.BillType;
import com.kashyap.homeIdeas.billmonitor.service.BillTypeService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.BILL_DELETED_SUCCESSFULLY;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.DATA_INVALID;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.RESOURCE_SAVED_SUCCESSFULLY;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.RESOURCE_NOT_SAVED_SUCCESSFULLY;

@RestController
@RequestMapping(value = "/rest/billtype")
public class BillTypeResource {

    @Autowired
    private BillTypeService service;

    final ObjectMapper om = new ObjectMapper();

    @Operation(summary = "Get all bill types")
    @GetMapping(value = "/getAll")
    public ApplicationResponse getBillTypes() {
        final ApplicationResponse response = new ApplicationResponse();

        final List<BillType> billTypeList = service.getAllBillType();

        response.setData(billTypeList);
        response.setSuccess(true);
        response.setMessage(BILL_DELETED_SUCCESSFULLY);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @Operation(summary = "save bill type")
    @PostMapping(value = "/save")
    public ApplicationResponse save(@RequestBody String data) throws JsonProcessingException {
        final ApplicationResponse response = new ApplicationResponse();
        if (StringUtils.isBlank(data)) {
            throw new BillMonitorValidationException(DATA_INVALID+ " Data:"+data);
        }

        final JsonNode jsonNode = om.readTree(data);

        if (jsonNode.has("name")) {
            final String name = jsonNode.get("name").asText();
            final BillType billType = new BillType();

            billType.setId(name);
            billType.setType(name);

            service.save(billType);

            response.setSuccess(true);
            response.setMessage(RESOURCE_SAVED_SUCCESSFULLY);
            response.setCode(HttpStatus.OK.value());
            return response;
        }

        response.setSuccess(false);
        response.setMessage(RESOURCE_NOT_SAVED_SUCCESSFULLY);
        response.setCode(HttpStatus.OK.value());
        return response;
    }
}
