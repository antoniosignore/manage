package com.muac.rsync;

import org.junit.Test;

public class SystemTest {

    @Test
    public void testPing() {

        ExecuteShellComand obj = new ExecuteShellComand();

        String domainName = "google.com";

        String command = "ping -c 3 " + domainName;

        //in windows
        //String command = "ping -n 3 " + domainName;

        //   String output = obj.executeCommand(command);

        // System.out.println("size:" + output.length() + "\n->" + output);

    }
}
