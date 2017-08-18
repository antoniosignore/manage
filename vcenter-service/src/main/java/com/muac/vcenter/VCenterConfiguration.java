package com.muac.vcenter;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VCenterConfiguration {

    @Value("${vcenter.url}")
    public String url;

    @Value("${vcenter.user}")
    public String user;

    @Value("${vcenter.password}")
    public String password;


}
