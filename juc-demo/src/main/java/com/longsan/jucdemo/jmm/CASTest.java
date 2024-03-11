//package com.longsan.jucdemo.jmm;
//
//import sun.misc.Unsafe;
//
//public class CASTest {
//
//    public static void main(String[] args) throws Exception {
//        Entity entity = new Entity();
//
//        Unsafe unsafe = UnsafeFactory.getUnsafe();
//        long offset = UnsafeFactory.getFieldOffset(unsafe, Entity.class, "x");
//        boolean successful;
//
//        successful = unsafe.compareAndSwapInt(entity, offset, 0, 3);
//        System.out.println(successful + "\t" + entity.x);
//
//        successful = unsafe.compareAndSwapInt(entity, offset, 3, 5);
//        System.out.println(successful + "\t" + entity.x);
//
//        successful = unsafe.compareAndSwapInt(entity, offset, 3, 7);
//        System.out.println(successful + "\t" + entity.x);
//
//
//    }
//
//
//    public static class Entity{
//        int x;
//    }
//}
