package com.example.hrbs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ToiletIssues extends AppCompatActivity {

    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference reference=database.getReference("Issues").child("toiletIssues");
    OverAllIssues overAllIssues;
    TextInputEditText name,rollno,issues;
    Spinner spinnerIssues;
    Button book;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_toilet_issues);
        book=findViewById(R.id.bookIssue);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name=findViewById(R.id.nameIssueCreator);
                rollno=findViewById(R.id.rollNoIssueCreator);
                issues=findViewById(R.id.issueDescription);
                String nameOfIssueCreator=name.getText().toString();
                String rollNoOfIssueCreator=rollno.getText().toString();
                String issuebyCreator=issues.getText().toString();
                overAllIssues=new OverAllIssues(nameOfIssueCreator,rollNoOfIssueCreator,null,issuebyCreator);

                reference.child(rollNoOfIssueCreator).setValue(overAllIssues)
                        .addOnSuccessListener(aVoid->
                        {
                            Toast.makeText(ToiletIssues.this,"Success",Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e->
                        {
                            Toast.makeText(ToiletIssues.this,"Not Filled",Toast.LENGTH_SHORT).show();
                        });

            }
        });



    }
}