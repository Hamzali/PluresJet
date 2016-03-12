package com.example.hamzali.pluresjet;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class FormActivity extends AppCompatActivity implements  OnMapReadyCallback{

    private final Context context = this;
    private String email;
    private String name;
    private String phone;
    private String flightInfo;

    // ATTENTION: This was auto-generated to implement the App Indexing API.
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int index;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            index = extras.getInt("text");
        } else {
            index = 0;
        }

        if (index == 5) {

            setContentView(R.layout.iletisim_layout);

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.

            if(isOnline()){
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map_fragment);
                mapFragment.getMapAsync(this);
            }

        }else{
            setContentView(R.layout.activity_form);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.form_toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().show();


        Resources res = getResources();
        String array[] = res.getStringArray(R.array.infos);

        //Banner
        int imgID;
        final ImageView bannerImg = (ImageView) findViewById(R.id.banner);
        switch (index) {
            case 0:
                imgID = R.drawable.ozel_ucak_banner;
                break;
            case 1:
                imgID = R.drawable.ambulans_kiralama_banner;
                break;
            case 2:
                imgID = R.drawable.hava_kargo_banner;
                break;
            case 3:
                imgID = R.drawable.ucak_bilet_banner;
                break;
            case 4:
                imgID = R.drawable.turizim_banner;
                break;
            default:
                imgID = R.drawable.iletisim_banner;
                break;
        }
        //bannerImg.setBackgroundResource(imgID);
        bannerImg.setImageResource(imgID);

        //Aciklama yazisi.
        final TextView ackYazi = (TextView) findViewById(R.id.icerik_text);
        ackYazi.setText(array[index]);


        //Name input.
        final EditText nameInput = (EditText) findViewById(R.id.name);

        // Phone number input.
        final EditText phoneInput = (EditText) findViewById(R.id.phone);

        // Flight information input.
        final EditText flightInput = (EditText) findViewById(R.id.flight_info);

        // Email input validation.
        final EditText emailValidate = (EditText) findViewById(R.id.email);


        // Request button actions.
        Button requestButton = (Button) findViewById(R.id.request_button);

        // Send email after clicked.
        assert requestButton != null;
        requestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                flightInfo = flightInput.getText().toString().trim();
                name = nameInput.getText().toString();
                phone = phoneInput.getText().toString().trim();
                email = emailValidate.getText().toString().trim();

                if (!isValidName(name))
                    Toast.makeText(getApplicationContext(), "Invalid Name", Toast.LENGTH_SHORT).show();
                else if (!isValidEmail(email))
                    Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                else if (!isValidPhone(phone))
                    Toast.makeText(getApplicationContext(), "Invalid Phone number", Toast.LENGTH_SHORT).show();
                else if (!isValidRequest(flightInfo))
                    Toast.makeText(getApplicationContext(), "Please make a request", Toast.LENGTH_SHORT).show();
                else sendMail(context);
            }

        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onMapReady(GoogleMap map) {
        Log.v("MAP", "WORKS");
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(context);
        try {
            addresses = geocoder.getFromLocationName("Istanbul", 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Address address = addresses.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.addMarker(new MarkerOptions().position(latLng));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f));
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private boolean isValidPhone(CharSequence target) {
        return !TextUtils.isEmpty(target);
    }

    private boolean isValidRequest(CharSequence target) {
        return !TextUtils.isEmpty(target);
    }

    private boolean isValidName(CharSequence target) {
        boolean isAlphabet = Pattern.compile("^[a-zA-Z ]+$").matcher(target).matches();
        return !TextUtils.isEmpty(target) && isAlphabet;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void sendMail(Context context) {
        BackgroundMail bm = new BackgroundMail(context);
        bm.setGmailUserName("plrsmbl@gmail.com");
        bm.setGmailPassword("P3l4rSi9");
        bm.setMailTo("ops@plures.com.tr");
        bm.setFormSubject("Flight Request");
        String body = "Name: " + name + "\n" + "Email: " + email + "\n" + "Phone: " + phone + "\n\n" + "Flight Request:\n" + flightInfo;
        bm.setFormBody(body);

        int t  = bm.send(context);
        setContentView(R.layout.thank_you_layout);
        TextView text = (TextView) findViewById(R.id.tesekurler_text_view);
        String msg = "";
        if(t == 0){
            msg = getString(R.string.talep_alindi);
        }else if(t == 1){
            msg = getString(R.string.talep_alinamadi);
        }
        text.setText(msg);
    }

    public void callIntent(View view) {
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

    public void exit(View view) {
        finish();
        System.exit(0);
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

        if (item.getItemId() == android.R.id.home) {
            finish();
            System.exit(0);
        }

        switch (id){
            case R.id.action_about:

                return true;
            case R.id.lang_tr:
                // Change language to turkish.
                return true;

            case R.id.lang_eng:
                // Change language to english.
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Form Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.hamzali.pluresjet/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Form Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.hamzali.pluresjet/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}