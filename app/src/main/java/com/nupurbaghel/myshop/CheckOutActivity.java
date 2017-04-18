package com.nupurbaghel.myshop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.nupurbaghel.myshop.HomeActivity.map;
import static com.nupurbaghel.myshop.ViewCartActivity.mycart;
import static com.nupurbaghel.myshop.ViewCartActivity.netTotal;

public class CheckOutActivity extends AppCompatActivity {
    HashMap<String,String> details=new HashMap<>();
    EditText name,address,phone,email;
    String name_,address_,phone_,email_;
    Button checko;
    FirebaseUser currentFirebaseUser;
    private Firebase mref2;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.logout:
                Log.i("Clicked","logout");
                Logout();
                return true;
            case R.id.mycart:
                Log.i("Clicked","View Cart");
                startActivity(new Intent(CheckOutActivity.this,ViewCartActivity.class));
                return true;
            case R.id.checkOut:
                Log.i("Clicked","Check Out");
                startActivity(new Intent(CheckOutActivity.this,CheckOutActivity.class));
                return true;
            case R.id.allOrders:
                Log.i("Clicked","All orders");
                startActivity(new Intent(CheckOutActivity.this,AllOrders.class));
                return true;

            default:return false;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        getCart();
        if(mycart.isEmpty()==true){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(CheckOutActivity.this);
            builder1.setMessage("Please select some items first!");
            builder1.setCancelable(true);

            builder1.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            CheckOutActivity.this.finish();
                            //startActivity(new Intent(CheckOutActivity.this,HomeActivity.class));
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        name=(EditText) findViewById(R.id.name);
        address=(EditText) findViewById(R.id.address);
        email=(EditText) findViewById(R.id.email);
        phone=(EditText) findViewById(R.id.phone);
        checko=(Button) findViewById(R.id.checko);

        getDefault(name,address,email,phone);
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        checko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(CheckOutActivity.this);
                save_details(name,address,email,phone);
                builder1.setMessage("Do you really want to place the order?");
                builder1.setCancelable(true);

                builder1.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                addToFirebase();
                                clearEverything();
                            }
                        });

                builder1.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        setupToolbar();
    }

    public void save_details(EditText name,EditText address,EditText email,EditText phone){

        String fname = "def_details.txt";
        File file = new File(getDir("data", MODE_PRIVATE), fname);

        details.put("name",name.getText().toString());
        details.put("address",address.getText().toString());
        details.put("email",email.getText().toString());
        details.put("phone",phone.getText().toString());

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

    }

    public boolean getDefault(EditText name,EditText address,EditText email,EditText phone){
        String fname = "def_details.txt";
        File file = new File(getDir("data", MODE_PRIVATE), fname);

        try {
            if (file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                details = (HashMap<String, String>) ois.readObject();
                if(details.isEmpty()) {
                    Log.i("Default details stored","empty");
                    return false;
                }
                else{
                    name.setText(details.get("name"));
                    address.setText(details.get("address"));
                    email.setText(details.get("email"));
                    phone.setText(details.get("phone"));


                    name.setTextColor(Color.BLACK);
                    address.setTextColor(Color.BLACK);
                    email.setTextColor(Color.BLACK);
                    phone.setTextColor(Color.BLACK);


                    Log.i("Default loaded", details.toString());
                    return true;
                }
            }
            else{
                ObjectOutputStream outputStream = null;
                outputStream = new ObjectOutputStream(new FileOutputStream(file));
                outputStream.writeObject(details);
                outputStream.flush();
                outputStream.close();

                return false;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public boolean getCart(){
        String fname = "cart.txt";
        File file = new File(getDir("data", MODE_PRIVATE), fname);

        try {
            if (file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                mycart = (HashMap<String, String>) ois.readObject();
                if(mycart.isEmpty()) {
                    Log.i("Cart","empty");
                    return false;
                }
                else{
                    Log.i("Loaded cart ", mycart.toString());
                    return true;
                }
            }
            else{
                ObjectOutputStream outputStream = null;
                outputStream = new ObjectOutputStream(new FileOutputStream(file));
                outputStream.writeObject(mycart);
                outputStream.flush();
                outputStream.close();

                return false;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void addToFirebase(){
        Log.i("Addtofire","called");
        mref2 = new Firebase("https://my-shop-93286.firebaseio.com/orders");
        name_=name.getText().toString();
        address_=address.getText().toString();
        email_=email.getText().toString();
        phone_=phone.getText().toString();

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        String prodNos="";
        String prodFreqs="";
        Map<String,String> mymap=new HashMap<>();
        for(Map.Entry cartItem: mycart.entrySet()) {
            Log.i("Keys Found",cartItem.getKey().toString());
            prodNos=prodNos + cartItem.getKey().toString() + "," ;
            prodFreqs=prodFreqs + cartItem.getValue().toString() + ",";
        }
        mymap.put("name",name_);
        mymap.put("address",address_);
        mymap.put("email",email_);
        mymap.put("phone",phone_);
        mymap.put("prodNos",prodNos);
        mymap.put("prodFreq",prodFreqs);
        mymap.put("userId",currentFirebaseUser.getUid());
        mymap.put("netPrice",netTotal);
        mymap.put("dateTime",currentDateTimeString);

        mref2.push().setValue(mymap);
        Log.i("Addtofire","ended");

    }

    boolean clearEverything(){
        mycart.clear();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(CheckOutActivity.this);
        builder1.setMessage("Your order has been sucessfully placed");
        builder1.setCancelable(true);

        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(CheckOutActivity.this,HomeActivity.class));
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
        try {
            String fname = "cart.txt";
            File file = new File(getDir("data", MODE_PRIVATE), fname);

            ObjectOutputStream outputStream = null;
            outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(mycart);
            outputStream.flush();
            outputStream.close();
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void Logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,MainActivity.class));
    }

    void setupToolbar(){
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
