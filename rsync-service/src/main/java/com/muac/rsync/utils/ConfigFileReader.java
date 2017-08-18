package com.muac.rsync.utils;

import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

@Service
public class ConfigFileReader {

    public Map<String, Object> load(String filename) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        Map<String, Object> config;
        config = (Map<String, Object>) yaml.load(new FileReader(new File(filename)));
        return config;
    }

}
