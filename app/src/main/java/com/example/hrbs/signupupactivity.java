package com.example.hrbs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signupupactivity extends AppCompatActivity {

    EditText signupName,signupEmail,signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    SharedPreferences sharedPreferences;
    FirebaseDatabase database;//Since We are realtime database
    DatabaseReference reference;//for reference
    @SuppressLint("MissingInflatedId")//Incase If the id is not found
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signupupactivity);
        signupName=findViewById(R.id.signup_name);
        signupEmail=findViewById(R.id.signup_email);
        signupPassword=findViewById(R.id.signup_password);
        loginRedirectText=findViewById(R.id.redirectText);
        signupButton=findViewById(R.id.signupbtn);
        sharedPreferences=getSharedPreferences("LoginPrefs",MODE_PRIVATE);
        if(isLoggedIn())
        {
            navigate();
        }
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database=FirebaseDatabase.getInstance();
                reference=database.getReference("users");
                String name=signupName.getText().toString();
                String email=signupEmail.getText().toString();
                String password=signupPassword.getText().toString();
                HelperClass helperClass=new HelperClass(name,email,password,"null");
                reference.child(email.replace(".","_")).setValue(helperClass);
                Intent intent=new Intent(signupupactivity.this,loginactivity.class);
                startActivity(intent);
            }
        });
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(signupupactivity.this,loginactivity.class);
                startActivity(intent);
            }
        });

    }
    public boolean isLoggedIn()
    {
        long currentTime=System.currentTimeMillis();
        long expirationTime=sharedPreferences.getLong("tokenExpiry",0);
        return currentTime<expirationTime;
    }
    public void navigate()
    {
        Intent intent=new Intent(signupupactivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}