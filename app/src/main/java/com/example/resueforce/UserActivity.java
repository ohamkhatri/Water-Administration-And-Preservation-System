package com.example.resueforce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

public class UserActivity extends AppCompatActivity {


    CardView Insights;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Insights = (CardView) findViewById(R.id.c3);
        TextView t1;
        TextView t2;

        ArrayList<String> list=new ArrayList<>();
        ArrayAdapter adapter= new ArrayAdapter<String>(this,R.layout.list_item,list);
        t1=findViewById(R.id.WaterLevelText);
        t2=findViewById(R.id.TimeText);


        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("UsersData").child("DMHvPknBIqP7WpuyQFj51l3kMzk1");

        reference.limitToLast(21).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){




                if (dataSnapshot.exists()) {
                    // Assuming your data is a list of strings
                    list.clear();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                        list.add(snapshot.getValue().toString());

                    }
                    Collections.reverse(list);

                    String dist=list.get(1).toString().substring(10,15);
                    double l=Double.parseDouble(dist);
                    double r= 4.5-(103.81*l*0.001);
                    String res= r+"";

                    t1.setText(res.substring(0,5)+"L");


                    String time1=list.get(1).toString().substring(69,79);
                    int time=Integer.parseInt(time1);

                    Date date = new Date(time*1000L); // *1000 is to convert seconds to milliseconds
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy "); // the format of your date
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));

                    String d= sdf.format(date);
                    t2.setText(d);

                    Log.d("hug", "Value is: " + list.get(1).toString().substring(69,79));

                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("err", "DataSnapshot is null or does not exist.");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){

            }
        });

        Insights.setOnClickListener(view ->
        {
            Intent i = new Intent(UserActivity.this, UserHistory.class);
            startActivity(i);
        });






    }
    private void refresh(int milliseconds){
        final Handler handler = new Handler();
        final Runnable runnable=new Runnable() {
            @Override
            public void run() {

            }
        };



    }
}




