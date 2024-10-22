package com.example.hrbs;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class booksapphire extends AppCompatActivity implements SeatAdapter.OnSeatClickListener {
    private RecyclerView recyclerView;
    private SeatAdapter seatAdapter;
    private List<Seat> seatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booksapphire);

        recyclerView = findViewById(R.id.recyclerView);

        // Initialize seat data
        seatList = new ArrayList<>();

        // Generate seat numbers (A1 to A10, B1 to B10, C1 to C10, etc.)
        char[] rows = {'A', 'B', 'C', 'D', 'E'};  // For 5 rows (A to E)
        int seats=1;
        for (char row : rows) {
            for (int seatNumber = 1; seatNumber <= 10; seatNumber++) {  // 10 seats per row
                String seatLabel =  String.valueOf(seats);
                seats++;
                seatList.add(new Seat(seatLabel, true, false));  // All seats available initially
            }
        }

        // Initialize the SeatAdapter
        seatAdapter = new SeatAdapter(seatList, this);

        // Set GridLayoutManager with 10 columns for 10 seats per row
        recyclerView.setLayoutManager(new GridLayoutManager(this, 10));

        // Add spacing between the seats
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);  // Adjust in dimens.xml
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spacingInPixels));

        // Set the adapter
        recyclerView.setAdapter(seatAdapter);

    }

    @Override
    public void onSeatClick(Seat seat) {
        // Handle seat click
        Toast.makeText(this, "Selected seat: " + seat.getSeatNumber(), Toast.LENGTH_SHORT).show();
    }
}
