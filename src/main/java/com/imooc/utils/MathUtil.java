package com.imooc.utils;

public class MathUtil {

    private static final Double THRESHOLD = 0.01;

    public static Boolean equals(Double d1, Double d2){
        Double result = Math.abs(d1-d2);
        if (result < THRESHOLD) {
            return true;
        } else {
            return false;
        }
    }
}
