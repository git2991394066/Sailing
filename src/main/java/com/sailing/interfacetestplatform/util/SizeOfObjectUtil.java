package com.sailing.interfacetestplatform.util;

import java.lang.instrument.Instrumentation;

/**
 * @auther:张启航Sailling
 * @createDate:2022/9/23 17:05
 * @description:
 **/
public class SizeOfObjectUtil {
    private static Instrumentation inst;

    public static void premain(String agentArgs, Instrumentation instP){
        inst = instP;
    }

    public static long sizeOf(Object obj){
        return inst.getObjectSize(obj);
    }
}
