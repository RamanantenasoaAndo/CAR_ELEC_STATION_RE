package com.example.car_elec_station_res;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
     CollapsingToolbarLayout markertxt;
     Button btn_reservation,btn_other;
     TextView markertypes,markertypesB;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_reservation = findViewById(R.id.btn_reserver);

        btn_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Formulaire_reservation.class);
                startActivity(intent);
                finish();
            }
        });

        btn_other = findViewById(R.id.btn_other);
        btn_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog dialog = new BottomSheetDialog(MainActivity.this,R.style.BottomSheetDialogTheme);
                View bottomSheet = getLayoutInflater().inflate(R.layout.btn_other_sheet,(LinearLayout)view.findViewById(R.id.layout_other));


                View editlayout = bottomSheet.findViewById(R.id.edit_layout);
                View sharelayout = bottomSheet.findViewById(R.id.share_layout);
                View photolayout = bottomSheet.findViewById(R.id.addphoto_layout);
                View commentlayout = bottomSheet.findViewById(R.id.comment_layout);
                View favorilayout = bottomSheet.findViewById(R.id.favori_layout);

                editlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"Edit is  Cliked",Toast.LENGTH_LONG).show();
                    }
                });
                sharelayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"Share is  Cliked",Toast.LENGTH_LONG).show();
                    }
                });
                photolayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"Photo is  Cliked",Toast.LENGTH_LONG).show();
                    }
                });
                commentlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"Commentaire is  Cliked",Toast.LENGTH_LONG).show();
                    }
                });
                favorilayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"Favorit is  Cliked",Toast.LENGTH_LONG).show();
                    }
                });
                dialog.setContentView(bottomSheet);
                dialog.show();
            }
        });


        markertxt = findViewById(R.id.collapsingToolbar);
        String title = getIntent().getStringExtra("title");
        markertxt.setTitle(title);

        //for Subtitle //for typesBranchement
        markertypes = findViewById(R.id.subtitle);
        markertypesB = findViewById(R.id.typesB);
        String types = getIntent().getStringExtra("types");
        markertypes.setText(types);
        markertypesB.setText(types);



       Toolbar toolbar =findViewById(R.id.toolbarLayout);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_reser,menu);
        return true;

    }
}