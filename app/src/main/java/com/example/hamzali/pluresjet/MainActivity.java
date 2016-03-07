package com.example.hamzali.pluresjet;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity{

    final Context context = this;
// Main activity
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

    }
    public void callIntent(View view){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:05452068560"));

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }

    public void exit(View view){
        finish();
    }

    public void requestForm(View view, int info){

        Intent intent = new Intent(this, FormActivity.class);
        intent.putExtra("text", info);
        startActivity(intent);
    }

    public void ucakKiralama(View view){
        requestForm(view, 0);
    }

    public void ambulansKiralama(View view){
        requestForm(view, 1);
    }

    public void havaKargo(View view){
        requestForm(view, 2);
    }

    public void ucakBilet(View view){
        requestForm(view, 3);
    }

    public void turizm(View view){
        requestForm(view, 4);

    }

    public void iletisim(View view){
        requestForm(view, 5);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_about:
                // Create new reminder.
                Log.d(getLocalClassName(), "About");

                return true;

            case R.id.action_exit:
                //Exit
                Intent intent = new Intent(this, FormActivity.class);
                startActivity(intent);
                return true;

            default:
                return false;
        }
    }

}
