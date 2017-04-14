package com.nupurbaghel.myshop;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.firebase.client.collection.LLRBNode;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import static android.R.attr.width;

public class scrollActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    private Firebase mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);

        linearLayout=(LinearLayout) findViewById(R.id.linearLayout);
        FirebaseStorage storage= FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("categories/electric2.jpg");


        for(int i=0;i<=5;i++) {
            LinearLayout parent = new LinearLayout(getApplicationContext());

            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            parent.setOrientation(LinearLayout.HORIZONTAL);

//children of parent linearlayout

            ImageView iv = new ImageView(getApplicationContext());

            //Picasso.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/my-shop-93286.appspot.com/o/Plastics%2FBuckets%20and%20mugs%2FMain.jpg?alt=media&token=3ab3cbea-39b6-4ab9-88a6-6edb3389fec3").into(iv);
            LinearLayout layout2 = new LinearLayout(getApplicationContext());

            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout2.setOrientation(LinearLayout.VERTICAL);

            float width = getResources().getDimension(R.dimen.chart_width);
            iv.setLayoutParams(new ViewGroup.LayoutParams((int) width, (int) width));

            Glide.with(this /* context */)
                    .using(new FirebaseImageLoader())
                    .load(imagesRef)
                    .into(iv);
//children of layout2 LinearLayout
            Log.i("trying to access", "yay");
            TextView tv1 = new TextView(getApplicationContext());
            TextView tv2 = new TextView(getApplicationContext());
            TextView tv3 = new TextView(getApplicationContext());
            TextView tv4 = new TextView(getApplicationContext());

            tv1.setText("Hello new");
            tv2.setText("Hello there");
            tv3.setText("Hello yay");
            tv4.setText("get lost");
            tv1.setTextColor(Color.BLACK);
            tv2.setTextColor(Color.BLACK);
            tv3.setTextColor(Color.BLACK);
            tv4.setTextColor(Color.BLACK);


            /*String uri = "@drawable/electric";
            Log.i("trying to access", uri);
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            iv.setImageDrawable(res);
            */

            layout2.addView(tv1);
            layout2.addView(tv2);
            layout2.addView(tv3);
            layout2.addView(tv4);

            parent.addView(iv);
            parent.addView(layout2);

            linearLayout.addView(parent);
        }

    }
}
