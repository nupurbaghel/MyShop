package com.nupurbaghel.myshop;


import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import static com.nupurbaghel.myshop.CheckOutActivity.details;
import static com.nupurbaghel.myshop.HomeActivity.map;
import static com.nupurbaghel.myshop.HomeActivity.mycart;


public class GenerateMail {
    float TotalCost=0;
    int i=1;
    public String generate(Context context, String Uid){
        ManageCart manageCart= new ManageCart();
        HashMap<String,String>mycart =new HashMap<>();
        mycart = (HashMap<String, String>) manageCart.LoadCart(context);
        String msg="<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "table {\n" +
                "    font-family: arial, sans-serif;\n" +
                "    border-collapse: collapse;\n" +
                "    width: 100%;\n" +
                "}\n" +
                "\n" +
                "td, th {\n" +
                "    border: 1px solid #dddddd;\n" +
                "    text-align: left;\n" +
                "    padding: 8px;\n" +
                "}\n" +
                "\n" +
                "tr:nth-child(even) {\n" +
                "    background-color: #dddddd;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>\n" +
                "<b> Username &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: &nbsp;</b> "+details.get("name") +" <br>\n" +
                "<b> User Address : </b>&nbsp; "+ details.get("address") +" <br>\n" +
                "<b> User Email &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: </b>&nbsp; "+ details.get("email") +" <br>\n" +
                "<b> User Contact &nbsp;: </b>&nbsp; "+ details.get("phone") +" <br>\n" +
                "</p>\n" +
                "<table>\n" +
                "  <tr>\n" +
                "    <th>S.No</th>\n" +
                "    <th>Product Name</th>\n" +
                "    <th>Product Id</th>\n" +
                "    <th>Price </th>\n" +
                "    <th>Discount </th>\n" +
                "    <th>Net Price Per Item</th>\n" +
                "    <th>Quantity </th>\n" +
                "  </tr>\n" ;

        for(Map.Entry cartItem: mycart.entrySet()) {
            Map<String,String> mymap = map.get("product").get(cartItem.getKey());

            String price=mymap.get("price");
            String discount=mymap.get("discount");
            String quantity=cartItem.getValue().toString();
            msg= msg+ "  <tr>\n" +
                    "    <td>"+ Integer.toString(i) +"</td>\n" +
                    "    <td>"+ mymap.get("name") +"</td>\n" +
                    "    <td>"+ cartItem.getKey() +"</td>\n" +
                    "    <td> Rs "+ price +"</td>\n" +
                    "    <td>"+  discount +"%</td>\n" +
                    "    <td> Rs "+ findCost(price,discount,quantity)+"</td>\n" +
                    "    <td>"+ quantity +" </td>\n" +
                    "  </tr>\n" ;

        }

        msg= msg + "<tr><th></th><th>Total Cost</th><th> Rs "+ Float.toString(TotalCost) +" </th><th></th></tr>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>\n" ;

        return msg;
    }


    public String findCost(String price,String discount, String quantity){
        float Price = Float.parseFloat(price);
        float Discount = Float.parseFloat(discount);

        float cost = (float) (Price - Discount * 0.01 * Price);
        TotalCost = TotalCost + cost * (float)Integer.parseInt(quantity);
        return Float.toString(cost);
    }

}
