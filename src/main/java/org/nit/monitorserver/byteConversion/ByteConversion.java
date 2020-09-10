package org.nit.monitorserver.byteConversion;

import sun.security.util.Length;

import javax.xml.stream.FactoryConfigurationError;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;
import java.time.temporal.TemporalAdjuster;
import java.util.Arrays;
import java.util.Date;

public class ByteConversion {
    public byte[] binaryInt=new byte[4];
    public byte[] binaryDouble=new byte[8];
    //int转字节
    public byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }

    //字节转int
    public int byteArrayToInt(byte[] bytes) {
        int value=0;
        for(int i = 0; i < 4; i++) {
            int shift= (3-i) * 8;
            value +=(bytes[i] & 0xFF) << shift;
        }
        return value;
    }

    //double转字节
    public byte[] doubleToBytes(double d) {
        long value = Double.doubleToRawLongBits(d);
        byte[] byteRet = new byte[8];
        for (int i = 0; i < 8; i++) {
            byteRet[i] = (byte) ((value >> 8 * i) & 0xff);
        }
        return byteRet;
    }

    //字节转double
    public double bytes2Double(byte[] arr) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value |= ((long) (arr[i] & 0xff)) << (8 * i);
        }
        return Double.longBitsToDouble(value);
    }

    //字节转String
    public String bytes2String(byte[] arr){
        String resString=new String(arr);
        return resString;
    }


    //字节数组合并

    public byte[] byteMerge(byte[] bytes1,byte[] bytes2) {
        byte[] bytes3=new byte[bytes1.length+bytes2.length];
        int i=0;
        for(byte bt:bytes1){
            bytes3[i]=bt;
            i++;
        }

        for(byte bt:bytes2){
            bytes3[i]=bt;
            i++;
        }
        return bytes3;
    }

    //截取字节数组
    public byte[] subByte(byte[] bt,int off,int length){
        byte[] subbt=new byte[length];
        System.arraycopy(bt,off,subbt,0,length); //复制字节数组,原数组，需要复制原数组的起始位置，目的数组，需要复制的目的数组的起始位置，复制的长度
        return subbt;
    }

    //字节转Date
    public Date BytesToDate (byte[] bytes){
        int date10 = (int)BitConverter.ToUInt16(bytes, 0);
        int year = date10 / 512 + 2000;
        int month = date10 % 512 / 32;
        int day = date10 % 512 % 32;
        return new Date(year, month, day);
    }

    public BigDecimal bytesToDecimal (byte[] bytes ){
        Long t = byteToLong(bytes);
        BigDecimal bigDecimal = new BigDecimal(t);
        return bigDecimal;
    }

    //字节转Long
    public long byteToLong(byte[] bytes) {
        long s = 0;
        long s0 = bytes[0] & 0xff;// 最低位
        long s1 = bytes[1] & 0xff;
        long s2 = bytes[2] & 0xff;
        long s3 = bytes[3] & 0xff;
        long s4 = bytes[4] & 0xff;// 最低位
        long s5 = bytes[5] & 0xff;
        long s6 = bytes[6] & 0xff;
        long s7 = bytes[7] & 0xff;

        // s0不变
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return s;
    }

    //字节转布尔
    public boolean byteToBoolean(byte[] bytes){
        if(bytes == null || bytes.length < 4){
            return false ;
        }
        int tmp = ByteBuffer.wrap(bytes,0,4).getInt();
        return (tmp == 0) ? false:true;
    }






}
