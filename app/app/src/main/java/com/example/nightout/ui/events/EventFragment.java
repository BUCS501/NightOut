package com.example.nightout.ui.events;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nightout.R;
import com.example.nightout.api.ImageRetrievalThread;
import com.example.nightout.api.TicketmasterRetrievalThread;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    private ListView lvEvents;
    private ListAdapter lvAdapter;
    private ArrayList<Event> events;
    private Spinner spinner;
    private String current_latitude;
    private String current_longitude;

    public static EventFragment newInstance(String param1, String param2) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String latitude = sharedPreferences.getString("pin_latitude", null);
        String longitude = sharedPreferences.getString("pin_longitude", null);
        if (latitude == null && longitude == null) {
            // get device current location if pin hasn't been set
            latitude = sharedPreferences.getString("device_latitude", null);
            longitude = sharedPreferences.getString("device_longitude", null);
        }
        if (latitude == null && longitude == null) {
            // if device location is not available, set to default location
            latitude = "42.3601";
            longitude = "-71.0589";
        }
        current_latitude = latitude;
        current_longitude = longitude;

        // create an executor service to run the thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // Calls the Yelp API and sets the restaurants array to the results
        executor.execute(new TicketmasterRetrievalThread(this,current_latitude,current_longitude));
        executor.shutdown();
        while (!executor.isTerminated()) {
            // wait for the thread to finish
        }
        // terminate executor
        executor.shutdownNow();
        System.out.println();

        // Storing data into SharedPreferences
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        String eventsListString = new Gson().toJson(events);
        myEdit.putString("current_events", eventsListString);
        myEdit.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        lvEvents = (ListView) getView().findViewById(R.id.lvEvents);
        lvAdapter = new EventAdapter(getActivity(), events);
        lvEvents.setAdapter(lvAdapter);

        spinner = (Spinner) getView().findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new String[]{"All", "Concert", "Sports", "Theater"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Event event = events.get(i);
                Intent intent = new Intent(getActivity(), DetailedEventActivity.class);
                intent.putExtra("eventID", event.getId());
                startActivity(intent);
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        update lv by making api calls
        String selected = parent.getItemAtPosition(position).toString();

        if (selected.equals("All")) {
            // create an executor service to run the thread
            ExecutorService executor = Executors.newSingleThreadExecutor();
            // Calls the Yelp API and sets the restaurants array to the results
            executor.execute(new TicketmasterRetrievalThread(this,current_latitude,current_longitude));
            executor.shutdown();
            while (!executor.isTerminated()) {
                // wait for the thread to finish
            }
            // terminate executor
            executor.shutdownNow();
            System.out.println();
        } else {
            // create an executor service to run the thread
            ExecutorService executor = Executors.newSingleThreadExecutor();
            // Calls the Yelp API and sets the restaurants array to the results
            executor.execute(new TicketmasterRetrievalThread(this,current_latitude,current_longitude, selected));
            executor.shutdown();
            while (!executor.isTerminated()) {
                // wait for the thread to finish
            }
            // terminate executor
            executor.shutdownNow();
            System.out.println();

        }
        lvAdapter = new EventAdapter(getActivity(), events);
        lvEvents.setAdapter(lvAdapter);

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}


class EventAdapter extends BaseAdapter {
    private final ArrayList<Event> events;
    private Context aContext;

    public EventAdapter(Context context, ArrayList<Event> eventList) {
        this.aContext = aContext;
        this.events = eventList;


    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.event_item, parent, false);
        }
        Event event = events.get(position);
        TextView name = view.findViewById(R.id.tvEventName);
        name.setText(event.getName());
        TextView date = view.findViewById(R.id.tvEventDate);
        date.setText(event.getDate());
        TextView time = view.findViewById(R.id.tvEventTime);
        time.setText(event.getTime());
        TextView location = view.findViewById(R.id.tvEventLocation);
        location.setText(event.getLocation());
        ImageView image = view.findViewById(R.id.imgEvent);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new ImageRetrievalThread(events.get(position).getImage(), image));
        executor.shutdown();
        while (!executor.isTerminated()) {
            // wait for the thread to finish
        }
        executor.shutdownNow();
        return view;
    }
}