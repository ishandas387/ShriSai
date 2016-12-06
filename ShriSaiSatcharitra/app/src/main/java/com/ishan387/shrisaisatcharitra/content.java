package com.ishan387.shrisaisatcharitra;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class content extends AppCompatActivity {
    TextView t;
    RelativeLayout r;
    String f;
    SharedPreferences settings;
    SeekBar sizecontrol,bcontrol;
    ImageView n, p;
    //private SeekBar brightbar;

    //Variable to store brightness value
    private int brightness;
    //Content resolver used as a handle to the system's settings
    private ContentResolver cResolver;
    //Window object, that will store a reference to the current window
    private Window window;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        t = (TextView) findViewById(R.id.con);
        r = (RelativeLayout) findViewById(R.id.back);
        n = (ImageView) findViewById(R.id.next);
        p = (ImageView) findViewById(R.id.prev);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);




        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                f= null;

            }
            else {
                f= extras.getString("FNAME");
                // d = extras.getString("DATE");
            }
        } else {
            f= (String) savedInstanceState.getSerializable("FNAME");
            // d= (String) savedInstanceState.getSerializable("DATE");
        }

        settings = getSharedPreferences("MyPref", MODE_PRIVATE);

        if (settings.contains("back"))
        {
            if (settings.getString("back","").equals("white"))
            {
                r.setBackgroundColor(getResources().getColor(R.color.white));
                t.setTextColor(getResources().getColor(R.color.black));


            }
            else{
                r.setBackgroundColor(getResources().getColor(R.color.black));
                t.setTextColor(getResources().getColor(R.color.white));



            }
        }

        loadDataFromAsset(f);

        sizecontrol = (SeekBar) findViewById(R.id.seekBar);
        bcontrol = (SeekBar) findViewById(R.id.seekBarb);
        //Get the content resolver
        cResolver = getContentResolver();

        //Get the current window
        window = getWindow();
       bcontrol.setMax(255);
        //Set the seek bar progress to 1
        bcontrol.setKeyProgressIncrement(1);


       try
        {
            //Get the current system brightness
            brightness = System.getInt(cResolver, System.SCREEN_BRIGHTNESS);
        }
        catch (SettingNotFoundException e)
        {
            //Throw an error case it couldn't be retrieved
            //Log.e("Error", "Cannot access system brightness");
          e.printStackTrace();
        }



        sizecontrol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
                t.setTextSize(TypedValue.COMPLEX_UNIT_DIP,progressChanged);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                /*Toast.makeText(content.this, "seek bar progress:" + progressChanged,
                        Toast.LENGTH_SHORT).show();*/

                sizecontrol.setVisibility(View.INVISIBLE);
                n.setVisibility(View.VISIBLE);
                p.setVisibility(View.VISIBLE);

            }
        });

        bcontrol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                //Set the minimal brightness level
                //if seek bar is 20 or any value below
                if(progress<=20)
                {
                    //Set the brightness to 20
                    brightness=20;
                }
                else //brightness is greater than 20
                {
                    //Set brightness variable based on the progress bar
                    brightness = progress;
                }
                //Calculate the brightness percentage
                //float perc = (brightness /(float)255)*100;
                //Set the brightness percentage
                //txtPerc.setText((int)perc +" %");
                System.putInt(cResolver,System.SCREEN_BRIGHTNESS, brightness);
                //Get the current window attributes
                WindowManager.LayoutParams layoutpars = window.getAttributes();
                //Set the brightness of this window
                layoutpars.screenBrightness = brightness / (float)255;
                //Apply attribute changes to this window
                window.setAttributes(layoutpars);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Set the system brightness using the brightness variable value

                bcontrol.setVisibility(View.INVISIBLE);
                n.setVisibility(View.VISIBLE);
                p.setVisibility(View.VISIBLE);


            }
        });

    }

    public void loadDataFromAsset(String f) {
        // load text
        try {
            // get input stream for text
            f= f.replace("Chapter ","");
            InputStream is = getAssets().open("ch/"+f+".txt");
            // check size
            int size = is.available();
            // create buffer for IO
            byte[] buffer = new byte[size];
            // get data to buffer
            is.read(buffer);
            // close stream
            is.close();
            // set result to TextView
            t.setText(new String(buffer));
        } catch (IOException ex) {
            return;
        }

       /* if (Build.VERSION.SDK_INT >= 11) {

            MenuItem item = menu.findViewById(R.id.share);
            item.setVisible(false);
            this.invalidateOptionsMenu();
        }*/

        final int finalF = Integer.parseInt(f);
        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalF<51)
                {
                    loadDataFromAsset(Integer.toString(finalF + 1));
                }
                else
                {
                    Toast.makeText(content.this, "This is the last chapter",
                            Toast.LENGTH_SHORT).show();


                }

            }
        });
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalF>1)
                {
                    loadDataFromAsset(Integer.toString(finalF - 1));
                }
                else
                {

                    Toast.makeText(content.this, "This is the first chapter",
                            Toast.LENGTH_SHORT).show();

                }


            }
        });


    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.plus) {
            sizecontrol.setVisibility(View.VISIBLE);
            n.setVisibility(View.INVISIBLE);
            p.setVisibility(View.INVISIBLE);

             }
        if (id == R.id.switchback) {
            settings = getSharedPreferences("MyPref", MODE_PRIVATE);

            if (settings.contains("back"))
            {
                if (settings.getString("back","").equals("white"))
                {
                    r.setBackgroundColor(getResources().getColor(R.color.black));
                    t.setTextColor(getResources().getColor(R.color.white));
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("back", "dark");

                    // Commit the edits!
                    editor.commit();

                }
                else{
                    r.setBackgroundColor(getResources().getColor(R.color.white));
                    t.setTextColor(getResources().getColor(R.color.black));
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("back", "white");

                    // Commit the edits!
                    editor.commit();

                }
            }










        }
        if (id == R.id.minus) {
            settings = getSharedPreferences("MyPref", MODE_PRIVATE);
            if (settings.contains("book"))
            {
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("book",f );

                // Commit the edits!
                editor.commit();
                Toast.makeText(content.this, "Bookmarked",
                        Toast.LENGTH_SHORT).show();

            }
            else
            {
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("book", f);

                // Commit the edits!
                editor.commit();
                Toast.makeText(content.this, "Bookmarked",
                        Toast.LENGTH_SHORT).show();
            }
        }
        if(id == R.id.bright)
        {
            bcontrol.setVisibility(View.VISIBLE);
            n.setVisibility(View.INVISIBLE);
            p.setVisibility(View.INVISIBLE);


        }

       /* if(id == R.id.share)

        {
            if (Build.VERSION.SDK_INT >= 11)
            {
                if (t.isTextSelectable()==false)
                {
                    t.setTextIsSelectable(true);
                    Toast.makeText(content.this, "select the part you want to share",
                            Toast.LENGTH_SHORT).show();
                }
            }



        }*/

        return super.onOptionsItemSelected(item);
    }


}
