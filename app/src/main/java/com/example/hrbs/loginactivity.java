package com.example.hrbs;

import android.content.Intent;
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

import java.util.Objects;

public class loginactivity extends AppCompatActivity {


    EditText email,password;
    Button loginbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loginactivity);
        email=findViewById(R.id.login_email);
        password=findViewById(R.id.login_password);
        loginbtn=findViewById(R.id.loginbutton);
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
                        Intent intent=new Intent(loginactivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        password.setError("Ivalid Password");
                        password.requestFocus();
                    }
                }
                else{email.setError("Invalid Email");}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}