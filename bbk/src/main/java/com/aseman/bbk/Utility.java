package com.aseman.bbk;

import java.text.DecimalFormat;

public class Utility {

	public static String GetMoney(String prc)
	{
		DecimalFormat format= new DecimalFormat("#,###,###,### ریال");
        long p=0;
        try {
        		p=Integer.parseInt(prc);
        }
        catch(Exception e)
        { p=0;}
        return format.format(p);
	}
}
