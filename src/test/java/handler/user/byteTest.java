package handler.user;

public class byteTest {
    public static void main(final String[] args) {
        String str = "1A";
        char[] strChar=str.toCharArray();
        String result="";
        for(int i=0;i<strChar.length;i++){
            result +=Integer.toBinaryString(strChar[i]);
        }
        System.out.println(result);
//        System.out.println(s.getBytes());
    }
}
