package com.example.hrbs;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class booksapphire extends AppCompatActivity implements SeatAdapter.OnSeatClickListener {
    private RecyclerView recyclerView;
    private SeatAdapter seatAdapter;
    private List<Seat> seatList;
    private DatabaseReference databaseReference;
    private Button bookSapphirebtn;
    private Seat selectedSeat;

    public void setCount(int count) {

        this.count = count;
    }

    private int count;

    public void setIndex(String s) {
        this.index = s;
    }

    String index;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booksapphire);

        recyclerView = findViewById(R.id.recyclerView);
        seatList = new ArrayList<>();
        sharedPreferences=getSharedPreferences("LoginPrefs",MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference("seats");

        // Programmatically create seats if they are not already in Firebase
        int seats = 1;
        for (int i = 0; i < 5; i++) {
            for (int seatNumber = 1; seatNumber <= 10; seatNumber++) {
                String seatLabel = String.valueOf(seats);
                Seat seat = new Seat(seatLabel, true, false, "room@gmail.com", 4); // Set email to null and count to 4

                DatabaseReference seatRef = databaseReference.child("seat" + seats);
                seatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            seatRef.setValue(seat) // Only set value if it doesn't exist
                                    .addOnSuccessListener(aVoid ->
                                            Toast.makeText(booksapphire.this, "Seat initialized!", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e ->
                                            Toast.makeText(booksapphire.this, "Failed to initialize seat", Toast.LENGTH_SHORT).show());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(booksapphire.this, "Error initializing seats", Toast.LENGTH_SHORT).show();
                    }
                });

                seats++;
            }
        }
        seatAdapter = new SeatAdapter(seatList, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 10));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spacingInPixels));
        recyclerView.setAdapter(seatAdapter);

        // Listen for seat updates from Firebase
        bookSapphirebtn=findViewById(R.id.bookSapphireButton);
        bookSapphirebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(booksapphire.this,index, Toast.LENGTH_SHORT).show();
                DatabaseReference selectedSeatRef = databaseReference.child("seat" + index);
                HashMap<String,Object> seatFB=new HashMap<>();
                seatFB.put("available",false);
                seatFB.put("count",count-1);
                seatFB.put("selected",true);
                seatFB.put("userEmail",sharedPreferences.getString("gmail",""));
                selectedSeatRef.updateChildren(seatFB)
                        .addOnSuccessListener(aVoid ->
                        {
                            Toast.makeText(booksapphire.this,"Success",Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e ->
                        {
                            Toast.makeText(booksapphire.this,"Not Booked",Toast.LENGTH_SHORT).show();
                        });
            }
        });

        loadSeatsFromFirebase();
    }

    private void loadSeatsFromFirebase() {
        // Listen for real-time updates to seat data in Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                seatList.clear(); // Clear the list to avoid duplication
                for (DataSnapshot seatSnapshot : snapshot.getChildren()) {
                    Seat seat = seatSnapshot.getValue(Seat.class);
                    if (seat != null) {
                        seatList.add(seat);
                    }
                }
                seatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(booksapphire.this, "Failed to load seats", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onSeatClick(Seat seat) {
        if (seat.isAvailable()) {
//            databaseReference.child("seat" + seat.getSeatNumber()).setValue(seat)
//                    .addOnSuccessListener(aVoid -> {
//                        Toast.makeText(this, "Selected seat: " + seat.getSeatNumber(), Toast.LENGTH_SHORT).show();
//                    })
//                    .addOnFailureListener(e -> {
//                        Toast.makeText(this, "Failed to reserve seat", Toast.LENGTH_SHORT).show();
//                    });
//            if(selectedSeat!=null && selectedSeat!=seat)
//            {
//                selectedSeat.setSelected(false);
//            }
            if (selectedSeat != null && selectedSeat != seat) {
                int prevIndex=seatList.indexOf(selectedSeat);
                selectedSeat.setSelected(false);
                seatAdapter.notifyItemChanged(prevIndex);

            }

            seat.setSelected(true);
           int currIndex=seatList.indexOf(seat);
            seatAdapter.notifyItemChanged(currIndex);
            selectedSeat = seat;
            setIndex(String.valueOf(seat.getSeatNumber()));
            setCount(seat.getCount());
        } else {
            Toast.makeText(this, "Seat already reserved", Toast.LENGTH_SHORT).show();
        }
    }


}
