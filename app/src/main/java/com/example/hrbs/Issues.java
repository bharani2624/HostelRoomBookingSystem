package com.example.hrbs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Issues extends AppCompatActivity {

    CardView hostel,room,toilet,food,others;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_issues);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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