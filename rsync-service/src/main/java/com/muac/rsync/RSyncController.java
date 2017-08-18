package com.muac.rsync;

import com.muac.beans.MMCMountPoint;
import com.muac.beans.MMCMountPoints;
import com.muac.utils.CommandUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@RestController
public class RSyncController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(RSyncController.class);

    private final ExecuteShellComand command;

    @Autowired
    public RSyncController(ExecuteShellComand command) {
        this.command = command;
    }

    @RequestMapping(value = "/mountpoints/{box}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public ResponseEntity<MMCMountPoints> folder(@PathVariable("box") String box) {
        try {
            File file = ResourceUtils.getFile("classpath:rsync.yml");
            MMCMountPoints mountPoints = (MMCMountPoints) new MMCMountPoints();
            Map<String, Double> percentOccupancies = command.getPercentOccupancies(file, box);
            Set<String> strings = percentOccupancies.keySet();
            for (Iterator<String> iterator = strings.iterator(); iterator.hasNext(); ) {
                String next = iterator.next();
                Double aDouble = percentOccupancies.get(next);
                MMCMountPoint mp = new MMCMountPoint();
                mp.setName(next);
                mp.setHostname(CommandUtils.executeCommand("hostname").trim());
                mp.setPercent(aDouble);
                mountPoints.getMmcMountPoints().add(mp);
            }
            return new ResponseEntity<>(mountPoints, HttpStatus.OK);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    /*@RequestMapping(value="/mmc*//**",method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
     public ResponseEntity<MMCResponse> serve(HttpServletRequest request) {
     String restOfTheUrl = (String) request.getAttribute(
     HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
     log.debug("restOfTheUrl = " + restOfTheUrl);
     MMCScalarResponse mmcResponse = new MMCScalarResponse();
     mmcResponse.setKey(OidUtils.convertToDotNotation("1.1.1.1.1.1"));
     mmcResponse.setValue("88");
     ResponseEntity<MMCResponse> mmcResponseResponseEntity =
     new ResponseEntity<>(mmcResponse, HttpStatus.OK);
     log.debug("===============================>>>> "+mmcResponseResponseEntity.toString());
     return mmcResponseResponseEntity;
     }
     */
}
