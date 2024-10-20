package com.example.hrbs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    CardView sapphire,emerald,pearl,coral,ruby,diamond;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        sapphire=findViewById(R.id.sapphireCard);
        emerald=findViewById(R.id.emeraldCard);
        pearl=findViewById(R.id.pearlCard);
        coral=findViewById(R.id.coralCard);
        ruby=findViewById(R.id.rubyCard);
        diamond=findViewById(R.id.diamondCard);

        sapphire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,sapphire.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

    }
}