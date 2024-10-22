package com.example.hrbs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class loginactivity extends AppCompatActivity {


    EditText email,password;
    Button loginbtn;
    SharedPreferences sharedPreferences;
    private static final long SESSION_DURATION=90L * 24 * 60 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loginactivity);
        email=findViewById(R.id.login_email);
        password=findViewById(R.id.login_password);
        loginbtn=findViewById(R.id.loginbutton);
        sharedPreferences=getSharedPreferences("LoginPrefs",MODE_PRIVATE);
        if(isLoggedIn())
        {
            navigateToMain();
        }
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateEmail()|!validatePassword()){}
                else{
                    checkUser();
                }
            }
        });
    }

    public Boolean validateEmail()
    {
        String emailverify=email.getText().toString();
        if(emailverify.isEmpty()) {
            email.setError("Email Field Cannot Be Empty");
            return false;
        }
        else {
            email.setError(null);
            return true;
        }
    }
    public Boolean validatePassword()
    {
        String passwordverify=password.getText().toString();
        if(passwordverify.isEmpty()) {
            password.setError("Password Field Cannot Be Empty");
            return false;
        }
        else {
            password.setError(null);
            return true;
        }
    }

    public boolean isLoggedIn()
    {
        long currentTime=System.currentTimeMillis();
        long expirationTime=sharedPreferences.getLong("tokenExpiry",0);
        return currentTime<expirationTime;
    }

    private  void navigateToMain()
    {
        Intent intent=new Intent(loginactivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void checkUser()
    {
        String userEmail=email.getText().toString();
        String userPassword=password.getText().toString();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase=reference.orderByChild("email").equalTo(userEmail);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    email.setError(null);
                    String passwordDB = snapshot.getChildren().iterator().next().child("password").getValue(String.class);
                    if(Objects.equals(passwordDB,userPassword))
                    {
                        email.setError(null);
                        SessionSaver(userEmail);
                        navigateToMain();
                    }
                    else
                    {
                        password.setError("Ivalid Password");
                        password.requestFocus();
                    }
                }
                else{email.setError("Invalid Email");}
            }

            public void SessionSaver(String UserEmail)
            {
                long currentTime=System.currentTimeMillis();
                long expiryTime=currentTime+SESSION_DURATION;
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putLong("tokenExpiry",expiryTime);
                editor.apply();
                Map<String,Object> tokenData=new HashMap<>();
                tokenData.put("tokenExpiry",expiryTime);
                reference.child(userEmail.replace(".","_")).updateChildren(tokenData);
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}