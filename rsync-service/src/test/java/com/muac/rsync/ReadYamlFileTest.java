package com.muac.rsync;

import org.junit.Test;

import java.io.File;
import java.util.Map;

public class ReadYamlFileTest {

    @Test
    public void readYamlFile() {
        ExecuteShellComand obj = new ExecuteShellComand();

        Map<String, Double> fe = obj.getPercentOccupancies(new File("src/test/resources/application.yml"), "FE");

        System.out.println("fe = " + fe);
    }


}
