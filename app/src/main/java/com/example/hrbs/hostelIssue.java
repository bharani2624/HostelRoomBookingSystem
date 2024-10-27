package com.example.hrbs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class hostelIssue extends AppCompatActivity {

    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference reference=database.getReference("Issues").child("hostelIssues");
    OverAllIssues overAllIssues;
    TextInputEditText name,rollno,issues;
    Spinner spinnerIssues;
    Button book;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hostel_issue);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        book=findViewById(R.id.bookIssue);
        spinnerIssues = findViewById(R.id.roomIssueSpinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.hostelissues,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIssues.setAdapter(adapter);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name=findViewById(R.id.nameIssueCreator);
                rollno=findViewById(R.id.rollNoIssueCreator);
                issues=findViewById(R.id.issueDescription);
                String nameOfIssueCreator=name.getText().toString();
                String rollNoOfIssueCreator=rollno.getText().toString();
                String type=spinnerIssues.toString();
                String issuebyCreator=issues.getText().toString();
                overAllIssues=new OverAllIssues(nameOfIssueCreator,rollNoOfIssueCreator,type,issuebyCreator);

                reference.child(rollNoOfIssueCreator).setValue(overAllIssues)
                        .addOnSuccessListener(aVoid->
                        {
                            Toast.makeText(hostelIssue.this,"Success",Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e->
                        {
                            Toast.makeText(hostelIssue.this,"Not Filled",Toast.LENGTH_SHORT).show();
                        });

            }
        });



    }
}