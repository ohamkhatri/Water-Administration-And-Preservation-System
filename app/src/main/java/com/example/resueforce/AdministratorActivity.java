package com.example.resueforce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class AdministratorActivity extends AppCompatActivity {
    TextView t3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        ArrayList<String> list=new ArrayList<>();
        ArrayAdapter adapter= new ArrayAdapter<String>(this,R.layout.list_item,list);
        t3=findViewById(R.id.WaterLevelText1);

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

                    t3.setText(res.substring(0,5)+"L");






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


    }
}