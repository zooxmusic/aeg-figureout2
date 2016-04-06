package com.aeg.controller;

import com.aeg.model.TransferResult;
import com.aeg.transfer.service.TransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TransferController {

    @Autowired
    private TransferService transferService;

    @RequestMapping(value = "/transfer", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public TransferResult transfer(@RequestParam(value = "partner", defaultValue="") String partner) {
        try {
            transferService.transfer(partner);
        }catch(Exception e) {
            log.error(e.getMessage());
            return TransferResult.failed();
        }
        return TransferResult.successful();
    }
}
