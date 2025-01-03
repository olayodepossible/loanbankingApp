package com.possible.loanbanking.utils;

import java.time.Year;
import java.util.Random;

public class AccountUtil {

    private AccountUtil(){}
    private static final Random random = new Random();
    public static String generateAccountNumber(){

        Year currentYear = Year.now();
        float min = 100000f;
        int max = 999999;

        //generate a random number between min and max
        int randNumber = (int) Math.floor(random.nextInt() * ((currentYear.getValue()+max) - min + 1) + min);
        return String.valueOf(randNumber);
    }
}
