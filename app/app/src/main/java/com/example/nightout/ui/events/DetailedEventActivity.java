package com.example.nightout.ui.events;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nightout.BookmarksDB;
import com.example.nightout.R;
import com.example.nightout.SignUp;
import com.example.nightout.api.DetailedTicketmasterRetrievalThread;
import com.example.nightout.api.ImageRetrievalThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailedEventActivity extends AppCompatActivity {
    private DetailedEvent event;
    private ImageView eventImage;
    private ImageView seatMapImage;
    private TextView eventName;
    private TextView eventDate;
    private TextView eventTime;
    private TextView eventLocation;
    private TextView eventGenre;
    private TextView eventPrice;
    private TextView eventDescription;
    private TextView eventAddress;
    private Button websiteButton;
    private Button bookmarkButton;
    private Button seatMapButton;
    private String eventID;

    BookmarksDB BookmarksDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_event);

        // Contact the Database
        BookmarksDB = new BookmarksDB(this);

        // Gets all of the views from the layout and assigns them to variables
        initializeViews();

        // Gets the event ID from the intent of the EventList Fragment
        Intent intent = getIntent();
        eventID = intent.getExtras().getString("eventID");

        // Creates a new thread to get the event information from the Ticketmaster API
        callDetailedTicketmasterAPI();
        assert event != null;
        // Calls method to set the views to the event information
        setViews();

        // Create onClickListeners for the buttons
        websiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getEventUrl()));
                startActivity(intent);
            }
        });

        // TODO: Insert event bookmark functionality
        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BookmarksDB.saveData(SignUp.user, eventID, event.getName(), "Event")) {
                    Toast.makeText(getApplicationContext(), ("Saved: " + event.getName()), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Already Saved!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        seatMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedEventActivity.this, SeatMapActivity.class);
                intent.putExtra("seatMapURL", event.getSeatMapUrl());
                startActivity(intent);
            }
        });


    }

    private void initializeViews() {
        eventName = (TextView) findViewById(R.id.detailedEventName);
        eventDate = (TextView) findViewById(R.id.detailedEventDate);
        eventTime = (TextView) findViewById(R.id.detailedEventTime);
        eventLocation = (TextView) findViewById(R.id.detailedEventLocation);
        eventGenre = (TextView) findViewById(R.id.detailedEventGenre);
        websiteButton = (Button) findViewById(R.id.detailedEventPagebtn);
        bookmarkButton = (Button) findViewById(R.id.detailedEventRSVP);
        eventImage = (ImageView) findViewById(R.id.detailedEventImage);
        seatMapButton = (Button) findViewById(R.id.detailedEventSeatMap);
        eventPrice = (TextView) findViewById(R.id.detailedEventPrice);
        eventDescription = (TextView) findViewById(R.id.detailedEventDescription);
        eventAddress = (TextView) findViewById(R.id.detailedEventAddress);


    }

    private void callDetailedTicketmasterAPI() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new DetailedTicketmasterRetrievalThread(this, eventID));
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        executor.shutdownNow();
        System.out.println();
    }

    private void setViews() {
        eventName.setText(event.getName());
        eventDate.setText(event.getDate());
        eventTime.setText(event.getTime());
        eventLocation.setText(event.getLocation());
        eventGenre.setText("Event Type:  " + event.getSegment());

        if (event.getSeatMapUrl() == null) {
            seatMapButton.setVisibility(View.GONE);
        }
        if (event.getDescription() == null) {
            eventDescription.setVisibility(View.GONE);
        } else {
            eventDescription.setText(event.getDescription());
        }

        if (event.getPriceRange() == null) {
            eventPrice.setText("Price:  " + "N/A");
        } else {
            eventPrice.setText("Price:  " + event.getPriceRange());
        }

        eventAddress.setText(event.getAddress());


        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new ImageRetrievalThread(event.getImage(), eventImage));
        executor.shutdown();
        while (!executor.isTerminated()) {
            // wait for the thread to finish
        }
        executor.shutdownNow();

//        ExecutorService executor2 = Executors.newSingleThreadExecutor();
//
//        executor2.shutdown();
//        while (!executor2.isTerminated()) {
//            // wait for the thread to finish
//        }
//        executor2.shutdownNow();

    }

    public void setDetailedEvent(DetailedEvent event) {
        this.event = event;
    }
}