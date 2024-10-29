package com.example.hrbs;

import static com.example.hrbs.R.id.nav_home;
import static com.example.hrbs.R.id.nav_search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Issues extends AppCompatActivity {

    CardView hostel,room,toilet,food,others;
    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_issues);
      BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigation);
      bottomNavigationView.setSelectedItemId(nav_search);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId()==nav_search){
                    return true;
                }
                if(item.getItemId()==nav_home){
                    Intent intent=new Intent(Issues.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
//                    overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                    return true;
                }

                return true;
            }
        });






        hostel=findViewById(R.id.hostelIssues);
        room=findViewById(R.id.roomsIssues);
        toilet=findViewById(R.id.toiletIssues);
        food=findViewById(R.id.foodIssues);
        others=findViewById(R.id.otherIssues);
        hostel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Issues.this, hostelIssue.class);
                startActivity(intent);
            }
        });
        room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Issues.this, RoomIssues.class);
                startActivity(intent);
            }
        }); toilet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Issues.this, ToiletIssues.class);
                startActivity(intent);
            }
        }); food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Issues.this, FoodIssues.class);
                startActivity(intent);
            }
        });
        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Issues.this, OtherIssues.class);
                startActivity(intent);
            }
        });

    }
}