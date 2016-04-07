package com.aeg.controller;

import com.aeg.model.TransferResult;
import com.aeg.service.TransferService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
public class TransferController {

    @Autowired
    private TransferService transferService;

    @RequestMapping(value = "/inbound/transfer")
    public TransferResult transferAllInbound() {
        try {
            transferService.transferInbound();
        }catch(Exception e) {
            //log.error(e.getMessage());
            return TransferResult.failed(e.getMessage());
        }
        return TransferResult.successful();
    }

    @RequestMapping(value = "/inbound/transfer/{partner}")
    public TransferResult transferInbound(@PathVariable String partner) {
        try {
            transferService.transferInbound(partner);
        }catch(Exception e) {
            //log.error(e.getMessage());
            return TransferResult.failed(e.getMessage());
        }
        return TransferResult.successful();
    }

    @RequestMapping(value = "/outbound/transfer/{partner}")
    public TransferResult transferOutbound(@PathVariable String partner) {
        try {
            transferService.transferOutbound(partner);
        }catch(Exception e) {
            //log.error(e.getMessage());
            return TransferResult.failed(e.getMessage());
        }
        return TransferResult.successful();
    }

    @RequestMapping(value = "/outbound/transfer")
    public TransferResult transferAllOutbound() {
        try {
            transferService.transferOutbound();
        }catch(Exception e) {
            //log.error(e.getMessage());
            return TransferResult.failed(e.getMessage());
        }
        return TransferResult.successful();
    }

}
