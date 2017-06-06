package com.aseman.bbk;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class ListModel {
    
    private  String Name="";
    private  String Date="";
    private  String Min="";
    private  String Max="";
    private  String AvgDay="";
    private  String Change="";
    private  String AvgMonth="";
    private	 String PId="";
    private	 String Id="";

    /*********** Set Methods ******************/
    public void setName(String Name)
    {
        this.Name = Name;
    }

    public void setDate(String Date)
    {
        this.Date = Date;
    }

    public void setMin(String Min)
    {
        this.Min = Min;
    }

    public void setMax(String Max)
    {
        this.Max = Max;
    }

    public void setAvgDay(String AvgDay)
    {
        this.AvgDay = AvgDay;
    }

    public void setChange(String Change)
    {
        this.Change = Change;
    }

    public void setAvgMonth(String AvgMonth)
    {
        this.AvgMonth = AvgMonth;
    }

    public void setPId(String PId)
    {
        this.PId = PId;
    }
    public void setId(String Id)
    {
        this.Id = Id;
    }
    /*********** Get Methods ****************/

    public String getName()
    {
        return this.Name;
    }

    public String getDate()
    {
        return this.Date;
    }

    public String getMin()
    {
        return this.Min;
    }

    public String getMax()
    {
        return this.Max;
    }

    public String getAvgDay()
    {
        return this.AvgDay;
    }

    public String getChange()
    {
        return this.Change;
    }

    public String getAvgMonth()
    {
        return this.AvgMonth;
    }

    public String getPId()
    {
        return this.PId;
    }
    public String getId()
    {
        return this.Id;
    }
}
class Item
{
	String ShopID;
	String proID;
	String Name;
	String Price;
	String Desc;
	String Count;
	String Discount;
	Bitmap image;
}

class Companies {
    
    public  String ID="";
    public  String Full_Name="";
    public  String Joinery="";
    public  String Activity="";
    public  String Activity2="";
}
class Company {

    public  String ID="";
    public  String Full_Name="";
    public  String P_Name="";
    public  String Joinery="";
    public  String Activity="";
    public  String Activity2="";
    public  String Number="";
    public  String Year="";
    public  String Website="";
    public  String Phone="";
    public  String Fax="";
    public  String P_Count="";
    public  String Resume="";
    public  String Medal="";
}

class ImageList {

    public ImageView iv;
    public  String Path="";
}
class Section {
    
    public  String ID="";
    public  String Title="";
    public  String Parnet="";
    public  String Type="";
    public  String Ordering="";
}

class Status {
    
    public  String Code="";
    public  String Message="";
}    


class Product {
	
    public  String ID="";
    public  String Title="";
    public  String SectionID="";
    public  String Price="";
    public  String Currency="";
    public  String Field1="";
    public  String Field2="";
    public  String Company_ID="";
    public  String Company_Name="";
    public  String Phones="";
    
}

class ProductDetail {

    public  String ID="";
    public  String Title="";
    public  String SectionID="";
    public  String Section="";
    public  String Price="";
    public  String Unit="";
    public  String Discount="";
    public  String Company_ID="";
    public  String Company_Name="";
    public  String Company_Joinery="";
    public  String Company_p1_Name="";
    public  String Company_Number="";
    public  String Company_Year="";
    public  String Company_Website="";
    public  String Company_Phones="";
    public  String Company_Fax="";
    public  String User_ID="";
    public  String Tax="";
    public  String Shoping_Cost="";
    public  String After_Sales="";
    public  String Time_Delivery="";
    public  String Catalog="";
    public  String Introduct="";
}

class UserProfile {
	
    public  String ID="";
    public  String Name="";
    public  String Title="";
    public  String Password="";
    public  String eMail="";
    public  String Mobile="";
    public  String Tel="";
    public  String Gender="";
    public  String State="";
    public  String City="";
    public  String Address="";
    public  String PCode="";
    public  String NewsLetter="";
    public  String LastLogon="";
    public  String RegDate="";
    public  String Log="";
   
}

class Order {
    public  String ID="";
    public  String OrderDate="";
    public  String TrackNum="";
    public  String InvoiceNum="";
    public  String Status="";
    public  String Total="";
}

class OrderDetail {
    public  String ProID="";
    public  String ProName="";
    public  String InvNo="";
    public  String Count="";
    public  String Price="";
    public  String Total="";
}

class Invoice {
    public  String ID="";
    public  String InvoiceDate="";
    public  String Amount="";
    public  String InvoiceNum="";
    public  String Status="";
    public  String OrderID="";
    public  String TransactionDate="";
    public  String Tref="";
}
class CheckOut{
    public  String ID="";
    public  String Count="";
    public  String CID="";
}
