package com.muac.vcenter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VSphereConfiguration {

    @Value("${vcenter.host}")
    public String host;

    @Value("${vcenter.user}")
    public String user;

    @Value("${vcenter.password}")
    public String password;

}
