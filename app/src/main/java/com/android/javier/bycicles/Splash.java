package com.android.javier.bycicles;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by reyes on 11/04/2017.
 */

public class Splash extends AppCompatActivity {
    TextView tex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        tex = (TextView)findViewById(R.id.title);
        Typeface font = Typeface.createFromAsset(getAssets(),"Roboto-Medium.ttf");
        tex.setText("Bicycles");


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {


            Intent i=new Intent(Splash.this,Navigation.class);
                startActivity(i);



            }


    },3000);


    }


}