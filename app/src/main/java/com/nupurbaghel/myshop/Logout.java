package com.nupurbaghel.myshop;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import static android.content.Context.MODE_PRIVATE;
import static com.nupurbaghel.myshop.CheckOutActivity.details;
import static com.nupurbaghel.myshop.ViewCartActivity.manageCart;

public class Logout {

    public void logout(Context context){
        FirebaseAuth.getInstance().signOut();
        manageCart.clearCart(context);
        details.clear();
        String fname = "def_details.txt";
        File file = new File(context.getDir("data", MODE_PRIVATE), fname);
        try {
            ObjectOutputStream outputStream = null;
            outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(details);
            outputStream.flush();
            outputStream.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        context.startActivity(new Intent(context,MainActivity.class));
    }

}
