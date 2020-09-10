package org.nit.monitorserver.byteConversion;

public class test {
    public static void main(String[] args) {
        ByteConversion byteConversion = new ByteConversion();
        byte[] bytes = {1,1,1,1,1,1,1,1};
        System.out.println(bytes);
        String a = byteConversion.bytes2String(bytes);
        System.out.println(a);
    }
}
