package com.example.hrbs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.Objects;

public class booksapphire extends AppCompatActivity implements SeatAdapter.OnSeatClickListener {
    private RecyclerView recyclerView;
    private SeatAdapter seatAdapter;
    private List<Seat> seatList;
    private DatabaseReference databaseReference;
    private DatabaseReference signupReference;
    private Button bookSapphirebtn;
    private Seat selectedSeat;

    public void setCount(int count) {

        this.count = count;
    }

    int count;

    public void setIndex(String s) {
        this.index = s;
    }

    String index;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booksapphire);
        ImageView close;
        close=findViewById(R.id.closeIcon);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(booksapphire.this,sapphire.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        seatList = new ArrayList<>();
        sharedPreferences=getSharedPreferences("LoginPrefs",MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference("hostel").child("sapphire");
        signupReference=FirebaseDatabase.getInstance().getReference("users");

        int seats = 1;
        for (int i = 0; i < 5; i++) {
            for (int seatNumber = 1; seatNumber <= 10; seatNumber++) {
                String seatLabel = String.valueOf(seats);
                Seat seat = new Seat(seatLabel, true, false, "room@gmail.com1","room@gmail.com2","room@gmail.com3","room@gmail.com4", 4);

                DatabaseReference seatRef = databaseReference.child("room" + seats);
                seatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            seatRef.setValue(seat)
                                    .addOnSuccessListener(aVoid ->
                                            Toast.makeText(booksapphire.this, "Room initialized", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e ->
                                            Toast.makeText(booksapphire.this, "Failed to initialize room", Toast.LENGTH_SHORT).show());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(booksapphire.this, "Error initializing room", Toast.LENGTH_SHORT).show();
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
                DatabaseReference selectedSeatRef = databaseReference.child("room" + index);

                String sanitizedEmail=sharedPreferences.getString("gmail","").replace(".","_");
                DatabaseReference roomRef=signupReference.child(sanitizedEmail).child("roomNo");
                selectedSeatRef.child("count").get().addOnCompleteListener(task->
                {
                    if(task.isSuccessful())
                    {
                        DataSnapshot dataSnapshot=task.getResult();
                        Integer counts=dataSnapshot.getValue(Integer.class);
                        setCount(counts);

                    }
                    else
                    {
                        Toast.makeText(booksapphire.this,"FireBase Error",Toast.LENGTH_SHORT).show();
                    }
                }

                );
                signupReference.child(sanitizedEmail).child("roomNo").get().addOnCompleteListener(
                        task ->
                        {
                            if(task.isSuccessful())
                            {
                                DataSnapshot dataSnapshot= task.getResult();
                                String RoomNumber=dataSnapshot.exists()?dataSnapshot.getValue(String.class):"null";
                                if(Objects.equals(RoomNumber, "null") && count!=0)
                                {
                                    HashMap<String,Object> seatFB=new HashMap<>();
                                    seatFB.put("userEmail"+count,sharedPreferences.getString("gmail",""));
                                    count=count-1;
                                    if(count==0)
                                    {
                                        seatFB.put("available",false);
                                    }
                                    else
                                    {
                                        seatFB.put("available",true);
                                    }
                                    seatFB.put("count",count);
                                    seatFB.put("selected",false);
                                    roomRef.setValue(index);
                                    selectedSeatRef.updateChildren(seatFB)
                                            .addOnSuccessListener(aVoid->{
                                                Toast.makeText(booksapphire.this,"Successfully Booked",Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e->{
                                                Toast.makeText(booksapphire.this,"Try Again",Toast.LENGTH_SHORT).show();
                                            });

                                }
                                else if(count==0)
                                {
                                    Toast.makeText(booksapphire.this,"The Room Is Full",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(booksapphire.this,"You Have Already Booked A Room",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(booksapphire.this,"FireBase Error",Toast.LENGTH_SHORT).show();
                            }
                        }
                );



            }
        });

        loadSeatsFromFirebase();
    }

    private void loadSeatsFromFirebase() {
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
