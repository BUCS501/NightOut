package com.example.nightout.ui.account;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.nightout.BuildConfig;
import com.example.nightout.DB;
import com.example.nightout.Login;
import com.example.nightout.MainActivity;
import com.example.nightout.R;
import com.example.nightout.SignUp;
import com.example.nightout.ui.restaurants.Restaurant;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AcccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AcccountFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

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

    Dialog bookmarkDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        bookmarkDialog = new Dialog(getContext());
    }

    //Set variables for onStart
    ConstraintLayout expandableView,expandableView2,expandableView3,expandableView4;
    Button arrowBtn,arrowBtn2,arrowBtn3,arrowBtn4,locationperm,delete, viewSavedRestaurantsBtn, viewSavedEventsBtn;
    CardView cardView,cardView2,cardView3,cardView4;
    DB dB;


    @Override
    public void onStart() {
        super.onStart();
        Button btn = getView().findViewById(R.id.button2);
        dB = new DB(getActivity());

        //This is the logout button, sends user back to login
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Login.class));
            }
        });
        // Use shared preferences to get the users location to display
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String latitude = sharedPreferences.getString("device_latitude", null);
        String longitude = sharedPreferences.getString("device_longitude", null);

        //grabs the email and name to populate the fields
        TextView textemail = getView().findViewById(R.id.textView20);
        textemail.setText(SignUp.user);
        TextView textname = getView().findViewById(R.id.textView4);
        textname.setText(dB.getdataname((SignUp.user)));

        //Checks to see if connected to location services if yes gives lat and long if no gives none
        TextView location = getView().findViewById(R.id.testtest);
        if (latitude == null || longitude == null){
            location.setText("None");
        }else {

            location.setText(latitude + "  " + longitude);
        }

        //Sets up the expandable and cardviews
        expandableView = getView().findViewById(R.id.expandableView);
        arrowBtn = getView().findViewById(R.id.arrowBtn);
        cardView = getView().findViewById(R.id.cardView);

        expandableView2 = getView().findViewById(R.id.expandableView2);
        arrowBtn2 = getView().findViewById(R.id.arrowBtn2);
        cardView2 = getView().findViewById(R.id.cardView2);

        expandableView3 = getView().findViewById(R.id.expandableView3);
        arrowBtn3 = getView().findViewById(R.id.arrowBtn3);
        cardView3 = getView().findViewById(R.id.cardView3);

        expandableView4 = getView().findViewById(R.id.expandableView4);
        arrowBtn4 = getView().findViewById(R.id.arrowBtn4);
        cardView4 = getView().findViewById(R.id.cardView4);

        locationperm = getView().findViewById(R.id.permission1);

        //First tab in the account fragment
        arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableView.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableView.setVisibility(View.VISIBLE);
                    arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                } else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableView.setVisibility(View.GONE);
                    arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
            }
        });

        //Button inside first tab sends you to the Android Settings
        locationperm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        //Second tab inside the account Fragment
        arrowBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableView2.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(cardView2, new AutoTransition());
                    expandableView2.setVisibility(View.VISIBLE);
                    arrowBtn2.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                } else {
                    TransitionManager.beginDelayedTransition(cardView2, new AutoTransition());
                    expandableView2.setVisibility(View.GONE);
                    arrowBtn2.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
            }
        });

        delete = getView().findViewById(R.id.delete);

        //Button for launching the Delete account menu
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        //Third tab in the account fragment, will launch the bookmarks
        arrowBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableView3.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(cardView3, new AutoTransition());
                    expandableView3.setVisibility(View.VISIBLE);
                    arrowBtn3.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                } else {
                    TransitionManager.beginDelayedTransition(cardView3, new AutoTransition());
                    expandableView3.setVisibility(View.GONE);
                    arrowBtn3.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
            }
        });

        // Button to View Saved Rest. inside Bookmarks Third Tab
        viewSavedRestaurantsBtn = getView().findViewById(R.id.viewSavedRestaurantsBtn);
        viewSavedRestaurantsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show a Popup with Rest. names
                Toast.makeText(getContext(), ("User: " + SignUp.user), Toast.LENGTH_SHORT).show();
            }
        });

        // Button to View Saved Events inside Bookmarks Third Tab
        viewSavedEventsBtn = getView().findViewById(R.id.viewSavedEventsBtn);
        viewSavedEventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show a Popup with Rest. names
                showBookmarksPopup(view);
                Toast.makeText(getContext(), ("User: " + SignUp.user), Toast.LENGTH_SHORT).show();
            }
        });

        //fourth tab in the accounts fragment
        arrowBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableView4.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(cardView4, new AutoTransition());
                    expandableView4.setVisibility(View.VISIBLE);
                    arrowBtn4.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                } else {
                    TransitionManager.beginDelayedTransition(cardView4, new AutoTransition());
                    expandableView4.setVisibility(View.GONE);
                    arrowBtn4.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_acccount, container, false);
    }

    //Displays the popup menu for account deletion
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.yesno);
        popup.show();
    }

    public void showBookmarksPopup(View v) {
        ImageButton closePopupBtn;
        bookmarkDialog.setContentView(R.layout.fragment_popup_bookmarks);
        closePopupBtn = (ImageButton) bookmarkDialog.findViewById(R.id.closePopupBtn);
        closePopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookmarkDialog.dismiss();
            }
        });
        bookmarkDialog.show();
    }

    //Deletes the users account
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                dB.deleter(SignUp.user);
                startActivity(new Intent(getActivity(), Login.class));
                return true;
            case R.id.item2:
                return true;
            default:
                return false;
        }
    }

}








