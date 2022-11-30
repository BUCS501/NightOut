package com.example.nightout.ui.account;



import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nightout.DB;
import com.example.nightout.MainActivity;
import com.example.nightout.R;
import com.example.nightout.SignUp;

import java.util.ArrayList;
import java.util.List;

public class Acccount extends AppCompatActivity {

    RecyclerView recyclerView;

    List<Recycle> recycleList;
    DB dB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_acccount);
        Button btn = findViewById(R.id.button2);
        dB = new DB(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Acccount.this, MainActivity.class));
            }
        });

        recyclerView = findViewById(R.id.recyclerView);

        initData();
        initRecyclerView();

        TextView textemail = findViewById(R.id.textView2);
        textemail.setText(SignUp.user);
        TextView textname = findViewById(R.id.textView4);
        textname.setText(dB.getdataname((SignUp.user)));
        TextView textplace = findViewById(R.id.textView3);
        if (SignUp.placer.equals("")){
            textplace.setText("None");
        }else {
            textplace.setText(dB.getdataloc(SignUp.user));
        }
    }

    private void initRecyclerView() {
        RecycleAdapter recycleAdapter = new RecycleAdapter(recycleList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
