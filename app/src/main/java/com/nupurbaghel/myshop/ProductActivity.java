package com.nupurbaghel.myshop;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import static android.R.attr.layout_centerVertical;
import static com.nupurbaghel.myshop.HomeActivity.map;

public class ProductActivity extends AppCompatActivity {

    LinearLayout linearLayout2;
    int categoryNo,subCategoryNo;
    String imageURL;
    private Firebase mRef;
    String[] prodIds;
    int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        linearLayout2= (LinearLayout) findViewById(R.id.linearLayoutProduct);
        if(linearLayout2==null){
            Log.i("Linear layout","Not found");
        }

        subCategoryNo = getIntent().getIntExtra("subCategoryNo",1);
        //Log.i("SubcatInProduct", String.valueOf(subCategoryNo));

        categoryNo = getIntent().getIntExtra("button",1);
        //Log.i("Category No", String.valueOf(categoryNo));

        mRef=new Firebase("https://my-shop-93286.firebaseio.com/");
        final ValueEventListener valueEventListener2 = mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                findCount(dataSnapshot);
                displayInLayout();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        FirebaseStorage storage= FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("categories/electric2.jpg");

    }


    public void findCount(DataSnapshot dataSnapshot){
        count=Integer.parseInt( map.get("category").get(categoryNo+"-"+subCategoryNo).get("total"));
        Log.i("TotalCountoOfProducts",Integer.toString(count));
        prodIds=map.get("category").get(categoryNo+"-"+subCategoryNo).get("products").split(",");
        for(String w:prodIds){
            Log.i("ProductIds",w);
        }
    }

    public void displayInLayout(){
        FirebaseStorage storage= FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef;

        for(String w:prodIds) {


            LinearLayout parent = new LinearLayout(getApplicationContext());
            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            parent.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout layout2 = new LinearLayout(getApplicationContext());
            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout2.setOrientation(LinearLayout.VERTICAL);

            imageURL= map.get("product").get(w).get("img");
            imagesRef = storageRef.child(imageURL);
            Log.i("Printing image url",imageURL);
            ImageView iv = new ImageView(getApplicationContext());
            float width = getResources().getDimension(R.dimen.chart_width);
            iv.setLayoutParams(new ViewGroup.LayoutParams((int) width, (int) width));
            Glide.with(this )
                    .using(new FirebaseImageLoader())
                    .load(imagesRef)
                    .into(iv);

            TextView name = new TextView(getApplicationContext());
            TextView company = new TextView(getApplicationContext());
            TextView price = new TextView(getApplicationContext());
            TextView discount = new TextView(getApplicationContext());
            Button btn = new Button(getApplicationContext());

            String name_= map.get("product").get(w).get("name");
            String company_= map.get("product").get(w).get("company");
            String price_= map.get("product").get(w).get("price");
            String discount_= map.get("product").get(w).get("discount");

            name.setText("Product : " +name_);
            company.setText("Company : " +company_);
            price.setText("Price : "+price_);
            discount.setText("Discount : "+discount_);
            name.setTextColor(Color.BLACK);
            company.setTextColor(Color.BLACK);
            price.setTextColor(Color.BLACK);
            discount.setTextColor(Color.BLACK);
            btn.setText("Add to cart");

            layout2.addView(name);
            layout2.addView(company);
            layout2.addView(price);
            layout2.addView(discount);
            layout2.addView(btn);
            layout2.setGravity(layout_centerVertical);
            layout2.setPadding(50,50,50,50);
            parent.addView(iv);
            parent.addView(layout2);
            Log.i("Adding layout",w);

            linearLayout2.addView(parent);

        }
    }
}
