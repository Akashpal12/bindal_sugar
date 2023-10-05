package in.co.vibrant.bindalsugar.BluetoothPrint;


public class ESC {

    public static String RESETPRINTER		="<0x1B><0x40>";

    public static String NEWLINE			="<0x0A>";
    public static String PAGEMODE			="<0x1B><0x4C>";
    public static String PAGEPRINT			="<0x1B><0x0C>";
    public static String STANDARDMODE		="<0x1B><0x53>";

    public static String DOUBLESTRIKEON		="<0x1B><0x47><0x01>";
    public static String DOUBLESTRIKEOFF	="<0x1B><0x47><0x00>";

    public static String UPSIDEDOWNON		="<0x1B><0x7B><0x01>";
    public static String UPSIDEDOWNOFF		="<0x1B><0x7B><0x00>";

    public static String TABSTART			="<0x1B><0x44><0x08><0x10><0x18><0x00><0x09>";
    public static String TABSET				="<0x09>";


    public static String INVERTCOLOR		="<0x1D><0x42><0x01>";
    public static String RESETINVERTCOLOR	="<0x1D><0x42><0x00>";

    public static String UPSIDEDOWN			="<0x1B><0x7B><0x01>";
    public static String RESETUPSIDEDOWN	="<0x1B><0x7B><0x00>";

    public static String PRINTLOGO			="<0xPG>";
    public static String PRINTCUSTOMFONT			="<0xFN>";

  //  public static String MINIMUMFONT = "<0x1B><0x21><0x01>";
    public static String MINIMUMLINESPACE = "<0x1B><0x33><0x18>";

    public static String setLINEFEED(int lenght){ 	//Temporary
        //length range: 0 to 255
        String hexStr = appendZero(Integer.toString(lenght,16));
        String cmd="<0x1B><0x64><0x"+hexStr+">";
        return cmd;
    }

    public static String setPAPERFEED(int lenght){ 	//Temporary
        //length range: 0 to 255
        String hexStr =appendZero(Integer.toString(lenght,16));
        String cmd="<0x1B><0x4A><0x"+hexStr+">";
        return cmd;
    }


    public static String setLINESPACE(int lenght){ 	//Permanent
        //length range: 0 to 255
        String hexStr = appendZero(Integer.toString(lenght,16));
        String cmd="<0x1B><0x33><0x"+hexStr+">";
        return cmd;
    }
    public static String RESETLINESPACE	="<0x1B><0x32>";


    public static String setCHARASPACE(int lenght){ //Permanent
        //length range: 0 to 255
        String hexStr = appendZero(Integer.toString(lenght,16));
        String cmd="<0x1B><0x20><0x"+hexStr+">";
        return cmd;
    }
    public static String RESETCHARASPACE	="<0x1B><0x20><0x00>";


    public static String setCHARASTYLE(int font, int bold, int doubleheight, int doublewidth, int underline){ //Permanent
        //0 for OFF; 1 for ON
        String binary=underline+"0"+doublewidth+doubleheight+bold+"00"+font;
        int decimal = Integer.parseInt(binary,2);
        String hexStr = appendZero(Integer.toString(decimal,16));
        String cmd="<0x1B><0x21><0x"+hexStr+">";
        return cmd;
    }

    public static String setCHARASTYLENew(int font, int bold, int doubleheight, int doublewidth){ //Permanent
        //0 for OFF; 1 for ON
        String binary="0"+doublewidth+doubleheight+bold+"00"+font;
        int decimal = Integer.parseInt(binary,2);
        String hexStr = appendZero(Integer.toString(decimal,16));
        String cmd="<0x1B><0x21><0x"+hexStr+">";
        return cmd;
    }
    public static String RESETCHARASTYLE	="<0x1B><0x21><0x00>";


    public static String setCHARASIZE(int lenght){ //Permanent
        //length range: 1 to 8
        String hexStr = appendZero(String.valueOf(11*(lenght-1)));
        String cmd="<0x1D><0x21><0x"+hexStr+">";
        return cmd;
    }
    public static String RESETCHARASIZE   ="<0x1D><0x21><0x00>";


    public static String setROTATION(int degree){ //Permanent
        //degree range: 0,1,2
        String cmd="<0x1B><0x56><0x0"+degree+">";
        return cmd;
    }
    public static String RESETROTATION		="<0x1B><0x56><0x00>";


    public static String setALIGNMENT(int align){ //Permanent
        //align left:0, center:1, right:2
        String cmd="<0x1B><0x61><0x0"+align+">";
        return cmd;
    }
    public static String RESETALIGNMENT		="<0x1B><0x61><0x00>";



    public static String setPRINTPOSITION(int position){ //Permanent
        //position range: 0 to 384
        String hexStr=appendHex(position);
        String cmd="<0x1B><0x24>"+hexStr;
        return cmd;
    }
    public static String RESETPRINTPOSITION		="<0x1B><0x24><0x00><0x00>";


    public static String printCOLUMN(String[] column, Integer[] margin, Integer[] align ){
        //print line length is equal to the sum total of column margins
        //align left:0, center:1, right:2
        String cmd = "";
        String leftStr = "",centerStr="",rightStr = "";
        Boolean flag = true;
        int nullcheck = 0;

        try {
            for ( int i = 0; i < column.length; i++ )
            {
                if (column[i].equals( "" )){
                    column[i] = " ";
                    nullcheck +=1;
                }
            }
            if ( nullcheck == column.length ) return cmd;


            while (flag) {

                for (int j = 0; j < margin.length; j++) {
                    leftStr = "";centerStr="";rightStr = "";
                    for (int k = 0; k < margin[j]; k++) {
                        if (align[j]==0) {
                            if (k<column[j].length()) {
                                leftStr += column[j].charAt(k);
                            } else {
                                leftStr += " ";
                            }
                            if (k==margin[j]-1) {
                                if (leftStr.charAt(0)==' ') {
                                    leftStr=leftStr.substring(1,leftStr.length())+" ";
                                }
                                cmd += leftStr;
                            }
                        }
                        if (align[j]==1) {
                            if (k<column[j].length()) {
                                centerStr += column[j].charAt(k);
                            } else {
                                if (centerStr.length()%2==0) {
                                    centerStr += " ";
                                }else {
                                    centerStr = " "+centerStr;
                                }
                            }
                            if (k==margin[j]-1) {
                                cmd += centerStr;
                            }
                        }
                        if (align[j]==2) {
                            if (k<column[j].length()) {
                                rightStr += column[j].charAt(k);
                            } else {
                                cmd += " ";
                            }
                            if (k==margin[j]-1) {
                                if (rightStr.charAt(rightStr.length()-1)==' ') {
                                    rightStr=" "+rightStr.substring(0,rightStr.length()-1);
                                }
                                cmd += rightStr;
                            }
                        }
                    }
                }
                flag = false;
                for (int j = 0; j < margin.length; j++) {

                    if (column[j].length()>margin[j]) {
                        column[j]= column[j].substring(margin[j], column[j].length());
                        flag=true;
                    } else {
                        String space="";
                        for (int j2 = 0; j2 <column[j].length(); j2++) {
                            space +=" ";
                        }
                        column[j]= space;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            cmd = "Exptn!";
        }
        return cmd;
    }


    public static String printCOLUMNFORMAT(String[] column, Integer[] margin,
                                           Integer[] align, String[] format ){
        //set format command using functions such as "setCHARASTYLE","setCHARASPACE" etc or use "<hex command>"
        //formatting works only in page mode
        String cmd = "";
        Boolean flag = true;
        int nullcheck = 0;

        try {

            for ( int i = 0; i < column.length; i++ )
            {
                if (column[i].equals( "" )){
                    column[i] = " ";
                    nullcheck +=1;
                }
            }
            if ( nullcheck == column.length ) return cmd;

            while (flag) {

                for (int j = 0; j < margin.length; j++) {
                    String leftStr = "",centerStr="",rightStr = "",rightspace="";
                    for (int k = 0; k < margin[j]; k++) {
                        if (align[j]==0)
                        {
                            if (k<column[j].length()) {
                                leftStr += column[j].charAt(k);
                            } else {
                                leftStr += " ";
                            }
                            if (k==margin[j]-1) {
                                if (leftStr.charAt(0)==' ') {
                                    leftStr=leftStr.substring(1,leftStr.length())+" ";
                                }
                                if ( format[j].length()>0 )
                                {
                                    leftStr=format[j]+leftStr;
                                }
                                cmd += leftStr;
                            }
                        }
                        if ( align[j]==1 )
                        {
                            if (k<column[j].length()) {
                                centerStr += column[j].charAt(k);
                            } else {
                                if (centerStr.length()%2==0) {
                                    centerStr += " ";
                                }else {
                                    centerStr = " "+centerStr;
                                }
                            }
                            if (k==margin[j]-1) {
                                if ( format[j].length()>0 )
                                {
                                    leftStr=format[j]+centerStr;
                                }
                                cmd += centerStr;
                            }
                        }
                        if ( align[j]==2 )
                        {
                            if (k<column[j].length()) {
                                rightStr += column[j].charAt(k);
                            } else {
                                rightspace += " ";
                            }
                            if (k==margin[j]-1) {
                                if (rightStr.charAt(rightStr.length()-1)==' ') {
                                    rightStr=" "+rightStr.substring(0,rightStr.length()-1);
                                }
                                rightStr = rightspace+rightStr;
                                if ( format[j].length()>0 )
                                {
                                    rightStr=format[j]+rightStr;
                                }
                                cmd += rightStr;
                            }
                        }
                    }
                }
                flag = false;
                for (int j = 0; j < margin.length; j++) {

                    if (column[j].length()>margin[j]) {
                        column[j]= column[j].substring(margin[j], column[j].length());
                        flag=true;
                    } else {
                        String space="";
                        for (int j2 = 0; j2 <column[j].length(); j2++) {
                            space +=" ";
                        }
                        column[j]= space;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            cmd = "Exception!";
        }
        return cmd;
    }


    public static String addSpace(int lenght){
        String space="";
        for ( int i = 0; i < lenght; i++ )
        {
            space +=" ";
        }
        return space;
    }

    public static String drawPattern(String symbol, int lenght){
        String line="";
        for ( int i = 0; i < lenght; i++ )
        {
            line +=symbol;
        }
        return line;
    }

    private static String appendZero(String hex){
        if (hex.length()==1) {
            hex="0"+hex;
        }
        return hex;
    }

    private static String appendHex(int position){
        String hexStr="";
        if (position>256) {
            hexStr="<0x"+position%256+">"+"<0x0"+position/256+">";
        } else {
            hexStr="<0x"+appendZero(Integer.toString(position,16))+">"+"<0x00>";
        }
        return hexStr;
    }





}
