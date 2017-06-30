package com.android.javier.bycicles;

import android.app.Activity;
import android.app.SearchManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.actions.SearchIntents;
import com.google.android.gms.vision.text.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

public class Voice extends AppCompatActivity {
    private Button bu;
    private TextView tex,tex2;
    private EditText edi;
    private final int REQ_CODE_VOZ = 143;
    Intent i;
    Toolbar too ;

    BluetoothAdapter bluetooth;
    ListView list;
    ArrayList mArrayAdapter = new ArrayList();
    public ArrayList <BluetoothDevice> arraydevices = new ArrayList<>();
    public DeviceListAdapter lisdevice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        bu = (Button)findViewById(R.id.button2);
        tex = (TextView)findViewById(R.id.textView2);
        tex2 = (TextView)findViewById(R.id.textView3);
        too = (Toolbar)findViewById(R.id.my_toolbar);
        edi=(EditText)findViewById(R.id.input_password);

        setSupportActionBar(too);
        too.setTitle("Modos");
        bu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                abreMin();
            }
        });










    }





    private void abreMin() {
        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent. LANGUAGE_MODEL_FREE_FORM);

        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Establece modo...");

        try{
            startActivityForResult(i, REQ_CODE_VOZ);


        }
        catch (ActivityNotFoundException act){


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode){
            case REQ_CODE_VOZ:
            {
                if(resultCode ==RESULT_OK && null !=data){
                    ArrayList<String> vozenIntent = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    //edi.setText(vozenIntent.get(0));
                  //  tex.setText(vozenIntent.get(0));
                    if(vozenIntent.get(0).equals("caro")) {
                   //     String s = tex.getText().toString();
                    //    Intent v = new Intent(this, Voice.class);
                      //  v.putExtra("adios", s);
                    }


                }

            }
            break;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bluetooth, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bluetooth:

                return true;
            case R.id.action_settings:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }







}


