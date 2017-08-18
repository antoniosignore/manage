package com.muac.vcenter;

import com.muac.beans.MMCResponse;
import com.muac.beans.MMCVms;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class VSphereController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(VSphereController.class);

    final
    VirtualMachineCollector collector;

    @Autowired
    public VSphereController(VirtualMachineCollector collector) {
        this.collector = collector;
    }

    @RequestMapping(value="/mmc/vms",method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public ResponseEntity<MMCResponse> getAllVms(HttpServletRequest request){
        collector.process();
        MMCVms all = new MMCVms(collector.machineDTOs);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }
}
