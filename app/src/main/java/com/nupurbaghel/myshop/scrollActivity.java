package com.nupurbaghel.myshop;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.collection.LLRBNode;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.Map;

import static android.R.attr.button;
import static android.R.attr.centerX;
import static android.R.attr.centerY;
import static android.R.attr.layout_centerHorizontal;
import static android.R.attr.layout_centerInParent;
import static android.R.attr.layout_centerVertical;
import static android.R.attr.width;
import static com.nupurbaghel.myshop.HomeActivity.map;

public class scrollActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    int count=0;
    int categoryNo;
    private Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);

        categoryNo = getIntent().getIntExtra("categoryNo",1);
        Log.i("Category No", String.valueOf(categoryNo));

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
        Iterator<Map.Entry<String, Map<String, Map<String, String>>>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Log.i("Level 0",Integer.toString(count));
            Map.Entry<String, Map<String, Map<String, String>>> pair = iterator.next();
            Log.i("Printing key",pair.getKey());
            if (pair.getKey() == "category") {

                Iterator<Map.Entry<String, Map<String, String>>> it2 = pair.getValue().entrySet().iterator();
                while (it2.hasNext()) {
                    Map.Entry<String, Map<String, String>> pair2 = it2.next();
                    if (pair2.getKey().contains(categoryNo+"-")) {
                        count++;
                    }
                }
            }
        }
        Log.i("Subcategories",Integer.toString(count));


    }

    public void displayInLayout(){
        FirebaseStorage storage= FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef;

        for(int i=1;i<=count;i++) {
            LinearLayout parent = new LinearLayout(getApplicationContext());

            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            parent.setOrientation(LinearLayout.HORIZONTAL);

            String imageURL= map.get("category").get(categoryNo+"-"+Integer.toString(i)).get("img");

            imagesRef = storageRef.child(imageURL);
            Log.i("Printing image url",imageURL);
            //tv1.setText(map.get("category").get(Integer.toString(i)).get("title"));

            ImageView iv = new ImageView(getApplicationContext());

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

            final int finalI = i;
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("AppDebug","Some subcatclicked");
                    Intent x= new Intent(scrollActivity.this,ProductActivity.class);
                    Log.i("SubcatInScroll", String.valueOf(finalI));
                    x.putExtra("subCategoryNo",finalI);
                    x.putExtra("categoryNo",String.valueOf(categoryNo));
                    startActivity(x);

                }
            });

        }
    }
}
