package com.ishan387.shrisaisatcharitra;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

public class chaptersmain extends AppCompatActivity  {
    String [] f;
    //int[] t;
    ListView lv;
    Toolbar toolbar;
    SharedPreferences settings;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chaptersmain);
        lv = (ListView) findViewById(R.id.listchapter);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        AdView mAdView = (AdView) findViewById(R.id.adv1);
        // AdRequest a = new AdRequest.
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mDrawerList = (ListView)findViewById(R.id.navList);
        String[] osArray = { "Check Accomodation", "Wallpapers", "Aarti", "Rate" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setupDrawer();


        // Button b = (Button) findViewById(R.id.button);
        final AssetManager am = this.getAssets();
        try {
            f =  am.list("ch");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        for(int i =0 ; i<f.length;i++)
        {
            f[i]= f[i].replace(".txt","");
        }

        int[] t = new int[f.length];
        for(int j =0 ; j<f.length;j++)
        {
            t[j]= Integer.parseInt(f[j]);
        }
        Arrays.sort(t);
        for(int k=0;k<t.length;k++)
        {
            f[k] = "Chapter "+t[k];
        }


        MyCustomAdapter dataAdapter = new MyCustomAdapter(this,
                R.layout.singlerow, f);
        lv.setAdapter(dataAdapter);
       // lv.setAdapter(new ArrayAdapter<String>(this,
        //        android.R.layout.simple_list_item_1, f ));
      /*  b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(chaptersmain.this,content.class);
                startActivity(i);
            }
        });*/
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String fname1 = (String) (lv.getItemAtPosition(i));
                Toast.makeText(getApplicationContext(), fname1, Toast.LENGTH_SHORT).show();
                Intent off = new Intent(chaptersmain.this, content.class);

                settings = getSharedPreferences("MyPref", MODE_PRIVATE);

                if(settings.contains("back"))
                {

                }
                else {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("back", "white");

                    // Commit the edits!
                    editor.commit();

                }


                off.putExtra("FNAME", fname1);
                startActivity(off);
            }
        });


    }


    private void setupDrawer() {

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Options");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {

                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);




    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chaptersmain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent off = new Intent(chaptersmain.this, content.class);

            settings = getSharedPreferences("MyPref", MODE_PRIVATE);
            if(settings.contains("book"))
            {
                String ff = settings.getString("book","");

                off.putExtra("FNAME", ff);
                startActivity(off);


            }
            else {
                Toast.makeText(chaptersmain.this, "No bookmarks",
                        Toast.LENGTH_SHORT).show();


            }


        }


        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }




        return super.onOptionsItemSelected(item);
    }


    private void selectItem(int position) {
        switch(position) {
            case 1:
                Intent a = new Intent(chaptersmain.this, Acc.class);
                startActivity(a);
                break;
            case 2:
                Intent b = new Intent(chaptersmain.this, Acc.class);
                startActivity(b);
                break;
            default:
        }
    }

    private class MyCustomAdapter extends ArrayAdapter<String> {

        private ArrayList<String> clistt;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               String[] cList) {
            super(context, textViewResourceId, cList);
            this.clistt = new ArrayList<String>();
            for(int i =0 ; i<cList.length;i++)
            {
                this.clistt.add(cList[i]);

            }

        }

        private class ViewHolder {
            ImageView image;
            TextView cname;

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            // Log.v("ConvertView", String.valueOf(position));
            if (convertView == null) {

                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.singlerow, null);

                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.i);
                holder.cname = (TextView) convertView.findViewById(R.id.chname);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String c = clistt.get(position);
            holder.cname.setText(c);
            Random rand = new Random();

            // nextInt excludes the top value so we have to add 1 to include the top value
            int randomNum = rand.nextInt((3 - 1) + 1) + 1;
            //oundImage roundedImage;
            try {
                // get input stream
                InputStream ims = getAssets().open("im/"+randomNum+".jpg");
                // load image as Drawable
                Drawable d = Drawable.createFromStream(ims, null);
               // Bitmap bm = ((BitmapDrawable)d).getBitmap();
                //roundedImage = new RoundImage(bm);
                //imageView1.setImageDrawable(roundedImage);
                // set image to ImageView
                holder.image.setImageDrawable(d);
            }
            catch(IOException ex) {
                //return;
            }






            return convertView;

        }
    }

}
