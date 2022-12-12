package com.example.nightout.ui.events;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.nightout.R;
import com.example.nightout.api.ImageRetrievalThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SeatMapActivity extends AppCompatActivity {
    ImageView seatMapImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_map);

        seatMapImage = findViewById(R.id.seatMapImage);
        String seatMapUrl = getIntent().getExtras().getString("seatMapURL");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new ImageRetrievalThread(seatMapUrl, seatMapImage));
        executor.shutdown();
        while (!executor.isTerminated()) {
            // wait for the thread to finish
        }
        executor.shutdownNow();
    }
}