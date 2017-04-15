package com.nupurbaghel.myshop;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.ViewGroup;
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

    LinearLayout linearLayout;
    int categoryNo,subCategoryNo;
    private Firebase mRef;
    int productNo=1;
    int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        linearLayout= (LinearLayout) findViewById(R.id.linearLayout);

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

        linearLayout=(LinearLayout) findViewById(R.id.linearLayout);
        FirebaseStorage storage= FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("categories/electric2.jpg");

    }


    public void findCount(DataSnapshot dataSnapshot){
        count=Integer.parseInt( map.get("category").get(categoryNo+"-"+subCategoryNo).get("total"));
        Log.i("TotalCountoOfProducts",Integer.toString(count));

    }

    public void displayInLayout(){
        FirebaseStorage storage= FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef;
        productNo=1;        //Current product no in json data

        for(int i=1;i<=count;i++) {
            LinearLayout parent = new LinearLayout(getApplicationContext());
            String subCategoryOfCurrentItem=map.get("product").get("1").get("title");
            while(!subCategoryOfCurrentItem.equals(categoryNo+"-"+subCategoryNo)){
               productNo++;
                subCategoryOfCurrentItem=map.get("product").get(Integer.toString(productNo)).get("subcategory");
            }
            Log.i("subcatfff",subCategoryOfCurrentItem);
            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            parent.setOrientation(LinearLayout.HORIZONTAL);
            Log.i("ProdNo",Integer.toString(productNo));

            String imageURL= map.get("product").get(categoryNo+"-"+Integer.toString(productNo)).get("img");

            imagesRef = storageRef.child(imageURL);
            Log.i("Printing image url",imageURL);
            ImageView iv = new ImageView(getApplicationContext());

            LinearLayout layout2 = new LinearLayout(getApplicationContext());

            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout2.setOrientation(LinearLayout.VERTICAL);

            float width = getResources().getDimension(R.dimen.chart_width);
            iv.setLayoutParams(new ViewGroup.LayoutParams((int) width, (int) width));

            Glide.with(this )
                    .using(new FirebaseImageLoader())
                    .load(imagesRef)
                    .into(iv);
//children of layout2 LinearLayout
            Log.i("trying to access", "yay");
            TextView tv1 = new TextView(getApplicationContext());
            TextView tv2 = new TextView(getApplicationContext());
            TextView tv3 = new TextView(getApplicationContext());
            TextView tv4 = new TextView(getApplicationContext());

            String title= map.get("category").get(categoryNo+"-"+Integer.toString(i)).get("title");
            tv1.setText(title);
            String decs= map.get("category").get(categoryNo+"-"+Integer.toString(i)).get("descr");
            tv2.setText(decs);
            tv1.setTextColor(Color.BLACK);
            tv1.setTextSize(30);
            tv2.setTextColor(Color.BLACK);

            layout2.addView(tv1);
            layout2.addView(tv2);
            //layout2.addView(tv3);
            //layout2.addView(tv4);
            layout2.setGravity(layout_centerVertical);
            layout2.setPadding(50,50,50,50);
            parent.addView(iv);
            parent.addView(layout2);

            linearLayout.addView(parent);

            productNo++;
        }


    }
}
