package com.example.nightout.ui.events;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nightout.R;
import com.example.nightout.api.ImageRetrievalThread;
import com.example.nightout.api.TicketmasterRetrievalThread;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventFragment extends Fragment {


    private ListView lvEvents;
    private ListAdapter lvAdapter;
    private ArrayList<Event> events;

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
        // create an executor service to run the thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // Calls the Yelp API and sets the restaurants array to the results
        executor.execute(new TicketmasterRetrievalThread(this));
        executor.shutdown();
        while (!executor.isTerminated()) {
            // wait for the thread to finish
        }
        // terminate executor
        executor.shutdownNow();
        System.out.println();
    }

    @Override
    public void onStart() {
        super.onStart();
        lvEvents = (ListView) getView().findViewById(R.id.lvEvents);
        lvAdapter = new EventAdapter(getActivity(), events);
        lvEvents.setAdapter(lvAdapter);
        System.out.println();
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