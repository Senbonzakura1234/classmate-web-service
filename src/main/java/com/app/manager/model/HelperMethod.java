package com.app.manager.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static java.lang.Math.abs;

public class HelperMethod {
    private static final Logger logger = LoggerFactory.getLogger(HelperMethod.class);

    public static String getDateString(Long timeStamp){
        try {
            return (new Date(timeStamp)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
            return "";
        }
    }

    public long roundUpIntDiv(long num, long divisor) {
        var sign = (num > 0 ? 1 : -1) * (divisor > 0 ? 1 : -1);
        return sign * (abs(num) + abs(divisor) - 1) / abs(divisor);
    }

    public String upperCaseFirstChar(String input){
        if(input == null || input.isEmpty()) return "";
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public static String removeDotEmail(String input){
        var inputSplit = input.split("@", 2);
        return inputSplit.length < 2 ? inputSplit[0] :
                inputSplit[0].replace(".", "") +
                        "@" + inputSplit[1];
    }
}
