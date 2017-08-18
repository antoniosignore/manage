package com.muac.vcenter;

import com.muac.beans.MMCDataCenter;
import com.muac.beans.MMCResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VCenterController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(VCenterController.class);

    private final
    VCenterConnector vCenterConnector;

    @Autowired
    public VCenterController(VCenterConnector vCenterConnector) {
        this.vCenterConnector = vCenterConnector;
    }

    @RequestMapping(value = "/datacenter", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public ResponseEntity<MMCResponse> folder() {
        MMCDataCenter dataCenter = vCenterConnector.queryVCenter();
        if (dataCenter != null)
            return new ResponseEntity<>(dataCenter, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    private String convertToDotNotation(String restOfTheUrl) {
        return restOfTheUrl.replace("/", ".");
    }

}
