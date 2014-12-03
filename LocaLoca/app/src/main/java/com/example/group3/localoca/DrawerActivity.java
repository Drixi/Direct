package com.example.group3.localoca;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by michael on 17-11-2014.
 */
public class DrawerActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private DrawerLayout drawerLayout;
    private ListView listView;
    private String[] drawerObjects;
    private ActionBarDrawerToggle drawerListener;
    private Myadapter myAdapter;
    private MainMenuFragment contentView;
    private Toast backtoast;
    SharedPreferences userinfo;
    private MainMenuFragment MainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity_layout);
        userinfo = getSharedPreferences("userinfo", MODE_PRIVATE);

        if (savedInstanceState == null) {
            contentView = new MainMenuFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.contentFrame, contentView).commit();
        }

        drawerLayout=(DrawerLayout) findViewById(R.id.drawerlayout);
        listView =(ListView) findViewById(R.id.drawerList);
        myAdapter = new Myadapter(this);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(this);
        drawerLayout.setDrawerListener(drawerListener);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerListener= new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer,
                R.string.drawer_open, R.string.drawer_close ){
            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerListener.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void onBackPressed() {
        contentView = new MainMenuFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.contentFrame, contentView).commit();
    }

    /*public void onBackPressed() {

        if(backtoast!=null&&backtoast.getView().getWindowToken()!=null) {
            finish();
        } else {
            backtoast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);
            backtoast.show();
        }
    }*/

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerListener.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerListener.onConfigurationChanged(newConfig);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
        long id ) {
        selectItem(position);
        System.out.println(position);
        switch(position) {
            case 0:
                MapFragment buttonMap = new MapFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.contentFrame, buttonMap).addToBackStack("tag").commit();
                break;
            case 1:
                BookingFragment buttonBooking = new BookingFragment();
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.contentFrame, buttonBooking, "booking").commit();
                break;
            case 2:
                CalenderFragment buttonCalendar = new CalenderFragment();
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.contentFrame, buttonCalendar).commit();
                break;
            case 3:
                Toast.makeText(DrawerActivity.this, "This feature is currently disabled", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(DrawerActivity.this, "This feature is currently disabled", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                alertbuilderLogout();
                break;
        }
    }

    public void selectItem(int position){
        listView.setItemChecked(position, true);
    }

    public void setTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    public void alertbuilderLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DrawerActivity.this);
        builder.setMessage("Are you sure you want to log out")
                .setCancelable(false)
                .setTitle("Log out")
                .setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int id) {
                                SharedPreferences settings = DrawerActivity.this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                                settings.edit().clear().commit();
                                Toast.makeText(DrawerActivity.this, "User logged out", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(DrawerActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}

class Myadapter extends BaseAdapter {
    private Context context;
    String[] drawerObjects;
    int[] images = {R.drawable.xl_maplogo, R.drawable.xl_bookinglogo, R.drawable.xl_calendarlogo, R.drawable.xl_settingslogo, R.drawable.xl_aboutlogo, R.drawable.xl_logoutlogo};

    public Myadapter(Context context) {
        this.context=context;
        drawerObjects=context.getResources().getStringArray(R.array.drawerObjets);
    }

    @Override
    public int getCount() {
        return drawerObjects.length;
    }

    @Override
    public Object getItem(int position) {
        return drawerObjects[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.drawer_layout, parent, Boolean.parseBoolean(null));
        }
        else {
            row = convertView;
        }
        TextView titleText = (TextView) row.findViewById(R.id.textView);
        ImageView titleImage = (ImageView) row.findViewById(R.id.imageView);
        titleText.setText(drawerObjects[position]);
        titleImage.setImageResource(images[position]);
        return row;
    }
}