package com.example.nightout.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nightout.DB;
import com.example.nightout.MainActivity;
import com.example.nightout.R;
import com.example.nightout.SignUp;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AcccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AcccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AcccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AcccountFragment newInstance(String param1, String param2) {
        AcccountFragment fragment = new AcccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    RecyclerView recyclerView;

    List<Recycle> recycleList;
    DB dB;


    @Override
    public void onStart() {
        super.onStart();
        Button btn = getView().findViewById(R.id.button2);
        dB = new DB(getActivity());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        recyclerView = getView().findViewById(R.id.recyclerView);

        initData();
        initRecyclerView();

        TextView textemail = getView().findViewById(R.id.textView2);
        textemail.setText(SignUp.user);
        TextView textname = getView().findViewById(R.id.textView4);
        textname.setText(dB.getdataname((SignUp.user)));
        TextView textplace = getView().findViewById(R.id.textView3);
        if (SignUp.placer.equals("")){
            textplace.setText("None");
        }else {
            textplace.setText(dB.getdataloc(SignUp.user));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_acccount, container, false);
    }
    private void initRecyclerView() {
        RecycleAdapter recycleAdapter = new RecycleAdapter(recycleList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recycleAdapter);
    }

    private void initData() {
        recycleList = new ArrayList<>();
        recycleList.add(new Recycle("Settings", "Notifications:Off", "Shareable Content:On"));
        recycleList.add(new Recycle("Location Services", "Location Tracking:Allowed", "Current Location: Boston"));
        recycleList.add(new Recycle("History", "Date of Creation: 11/20/2022", "Favorites: Bleacher Bar\nJohnny's\nThe Paradise"));
        recycleList.add(new Recycle("Legal", "", "Disclaimer: The information provided on this website does not, and is not intended to, constitute legal advice; instead, all information, content, and materials available on this site are for general informational purposes only.  Information on this website may not constitute the most up-to-date legal or other information.  This website contains links to other third-party websites.  Such links are only for the convenience of the reader, user or browser; the ABA and its members do not recommend or endorse the contents of the third-party sites."));
    }
}






