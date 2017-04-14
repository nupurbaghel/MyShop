package com.nupurbaghel.myshop;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Iterator;
import java.util.Map;

import static android.R.attr.button;
import static com.nupurbaghel.myshop.R.id.gridLayout;
import static com.nupurbaghel.myshop.R.id.linearLayout;

public class HomeActivity extends AppCompatActivity {
    int count = 0;
    private Firebase mRef;
    GridLayout gridLayout;
    static Map<String, Map<String, Map<String, String>>> map;

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

            default:return false;
        }

    }
    public void Logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,MainActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        gridLayout=(GridLayout) findViewById(R.id.gridLayout);
        gridLayout.removeAllViews();
        mRef=new Firebase("https://my-shop-93286.firebaseio.com/");

        final ValueEventListener valueEventListener = mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                findCount(dataSnapshot);
                displayInGridView();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void callScrollActivity(View view){
        startActivity(new Intent(this,scrollActivity.class));
    }

    public void findCount(DataSnapshot dataSnapshot){
        map = dataSnapshot.getValue(Map.class);
        Iterator<Map.Entry<String, Map<String, Map<String, String>>>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Log.i("Level 0",Integer.toString(count));
            Map.Entry<String, Map<String, Map<String, String>>> pair = iterator.next();
            Log.i("Printing key",pair.getKey());
            if (pair.getKey() == "category") {

                Iterator<Map.Entry<String, Map<String, String>>> it2 = pair.getValue().entrySet().iterator();
                while (it2.hasNext()) {
                    Map.Entry<String, Map<String, String>> pair2 = it2.next();
                    if (!pair2.getKey().contains("-")) {
                        count++;
                    }
                }
            }
        }
        Log.i("Printing Count",Integer.toString(count));
    }

    public void displayInGridView(){
        FirebaseStorage storage= FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef;
        gridLayout.setColumnCount(2);

        float paading = getResources().getDimension(R.dimen.paading);
        if(count%2==0) {
            gridLayout.setRowCount(count / 2);
        }
        else{
            gridLayout.setRowCount((count / 2) + 1) ;
        }

        for(int i=1;i<=count;i++) {
            LinearLayout parent = new LinearLayout(getApplicationContext());
            TextView tv1 = new TextView(getApplicationContext());
            tv1.setGravity(Gravity.CENTER);
            parent.setId(i+100);

            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            parent.setOrientation(LinearLayout.VERTICAL);
            String imageURL=map.get("category").get(Integer.toString(i)).get("img");

            imagesRef = storageRef.child(imageURL);
            Log.i("Printing image url",imageURL);
            tv1.setText(map.get("category").get(Integer.toString(i)).get("title"));
            ImageView iv = new ImageView(getApplicationContext());

            float width = getResources().getDimension(R.dimen.chart_width);
            iv.setLayoutParams(new ViewGroup.LayoutParams((int) width, (int) width));
            iv.setPadding((int)paading,(int)paading,(int)paading,(int)paading);

            Glide.with(getApplicationContext() /* context */)
                    .using(new FirebaseImageLoader())
                    .load(imagesRef)
                    .into(iv);

            Log.i("trying to access", "yay");

            tv1.setTextColor(Color.BLACK);

            parent.addView(iv);
            parent.addView(tv1);


            gridLayout.setPadding((int)paading,(int)paading,(int)paading,(int)paading);
            gridLayout.addView(parent);

            final int finalI = i;
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("AppDebug","button clicked");
                    Intent x= new Intent(HomeActivity.this,scrollActivity.class);
                    Log.i("ButtonHome", String.valueOf(finalI));
                    x.putExtra("button",String.valueOf(finalI));
                    startActivity(x);

                }
            });
        }
    }


}
