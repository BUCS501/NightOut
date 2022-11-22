package com.example.nightout.ui.events;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nightout.R;
import com.example.nightout.Restaurant;
import com.example.nightout.api.TicketmasterAPI;
import com.example.nightout.databinding.FragmentEventBinding;

import java.util.ArrayList;

public class EventFragment extends Fragment {


    private ListView lvEvents;
    private ListAdapter lvAdapter;
    private ArrayList<Event> events;

    public static EventFragment newInstance(String param1, String param2) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lvAdapter = new EventAdapter(getActivity());

        View view = getView();
        lvEvents = view.findViewById(R.id.lvEvents);
        lvEvents.setAdapter(lvAdapter);


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        EventViewModel eventViewModel =
//                new ViewModelProvider(this).get(EventViewModel.class);
//
//        binding = FragmentEventBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        lvEvents= binding.lvEvents;
//        eventViewModel.getText().observe(getViewLifecycleOwner(), lvEvents::setText);
//        return root;
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}


class EventAdapter extends BaseAdapter {
    private final Event[] events;
    private Context aContext;

    public EventAdapter(Context context) {
        this.aContext = aContext;
//        TicketmasterAPI api = new TicketmasterAPI();
//        api.searchEvents("concerts", "boston", "MA",5);
//        events = TicketmasterAPI.getEvents();
        events = new Event[5];
        events[0] = new Event("Event 1", "Event 1 Description", "2023-03-02","20:00:00","Boston","10","ImageURL1");
        events[1] = new Event("Event 2", "Event 2 Description", "2023-04-02","20:00:00","Boston","10","ImageURL2");
        events[2] = new Event("Event 3", "Event 3 Description", "2023-05-02","20:00:00","Boston","10","ImageURL3");
        events[3] = new Event("Event 4", "Event 4 Description", "2023-06-02","20:00:00","Boston","10","ImageURL4");
        events[4] = new Event("Event 5", "Event 5 Description", "2023-07-02","20:00:00","Boston","10","ImageURL5");


    }

    @Override
    public int getCount() {
        return events.length;
    }

    @Override
    public Object getItem(int position) {
        return events[position];
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
        Event event = events[position];
        TextView name = view.findViewById(R.id.tvEventName);
        name.setText(event.getName());
        TextView date = view.findViewById(R.id.tvEventDate);
        date.setText(event.getDate());
        TextView time = view.findViewById(R.id.tvEventTime);
        time.setText(event.getTime());
        TextView location = view.findViewById(R.id.tvEventLocation);
        location.setText(event.getLocation());
        return view;
    }
}