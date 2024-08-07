package org.cloudfoundry.samples.music.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CupsController {

    Logger logger = LoggerFactory.getLogger(CupsController.class);

    @Autowired
    private Environment env;


    @GetMapping("/cups/{service_instance}/{name}")
    ResponseEntity<String> get(@PathVariable("name") String name,
                               @PathVariable("service_instance") String serviceInstance) throws IOException {

        String fullPropertyName = "vcap.services." + serviceInstance + ".credentials." + name;
        logger.info("Requested secret for " + serviceInstance + " " + name);

        logger.info("Resolved property name: " + fullPropertyName);

        String val = env.getProperty(fullPropertyName);

        if (val != null) {
            logger.info("Request for param found");
            return new ResponseEntity<>(val, HttpStatus.OK);
        } else {
            logger.info("Request for param not found");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

    }

}