package in.co.vibrant.bindalsugar.util;


public class PaddingString {

    public static String padRight(String s, int n,String pad) {
        int len=s.length();
        String data="";
        for(int i=0;i<n-len;i++)
        {
            data +=""+pad;
        }
        return s+data;
    }

    public static String padLeft(String s, int n,String pad) {
        int len=s.length();
        String data="";
        for(int i=0;i<n-len;i++)
        {
            data +=""+pad;
        }
        return data+s;
    }

    public static String padBoth(String s, int n,String pad) {
        int len=s.length();
        int blankLen=n-len;
        String data="";
        for(int i=0;i<blankLen/2;i++)
        {
            data +=""+pad;
        }

        return data+s;
    }

}