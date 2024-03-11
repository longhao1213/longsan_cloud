package com.longsan.jvm;

public class Test {

    public static void main(String[] args) {
        String s1 = "longsan";
        String s2 = new String("longsan");
        String s3 = s2.intern();
        System.out.println(s1 == s2);
        System.out.println(s3 == s2);
        System.out.println(s3 == s1);

        String s4 =new String("he") +new String("llo");
        String s5 = s4.intern();
        System.out.println(s4.equals(s5));

        Integer i1 = Integer.valueOf(127);
        Integer i2 = Integer.valueOf(127);
        System.out.println(i1 == i2);

        Integer i5 = new Integer(127);
        Integer i6 = new Integer(127);
        System.out.println(i5 == i6);
    }
}
