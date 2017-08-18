package com.muac.snmp.mib;

import org.junit.Assert;
import org.junit.Test;

public class ActualStatusEnumTest {

    @Test
    public void getLevel() throws Exception {


        ActualStatusEnum green = ActualStatusEnum.GREEN;

        Assert.assertEquals(1, green.ordinal());


        ActualStatusEnum green1 = Enum.valueOf(ActualStatusEnum.class, "GREEN");

        Assert.assertEquals(1, green1.value);


    }

}