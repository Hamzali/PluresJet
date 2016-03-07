package com.example.hamzali.pluresjet;


import android.Manifest;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.mail.Quota;


public class FormActivity extends AppCompatActivity {

    private String email;
    private String name;
    private String phone;
    private String flightInfo;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Toolbar toolbar = (Toolbar) findViewById(R.id.form_toolbar);
        setSupportActionBar(toolbar);

        int index;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            index = extras.getInt("text");
        }else{
            index = 0;
        }


        Resources res = getResources();
        String array[] = res.getStringArray(R.array.infos);

        //Aciklama yazisi.
        final TextView ackYazi = (TextView)findViewById(R.id.icerik_text);
        ackYazi.setText(array[index]);

        //Banner
        int imgID;
        final ImageView bannerImg = (ImageView)findViewById(R.id.banner);
        switch (index){
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
                imgID = R.drawable.ambulans_kiralama_banner;
                break;
        }
        bannerImg.setBackgroundResource(imgID);


        //Name input.
        final EditText nameInput = (EditText)findViewById(R.id.name);

        // Phone number input.
        final EditText phoneInput =  (EditText)findViewById(R.id.phone);

        // Flight information input.
        final EditText flightInput =  (EditText)findViewById(R.id.flight_info);

        // Email input validation.
        final EditText emailValidate = (EditText)findViewById(R.id.email);



        // Request button actions.
        Button requestButton = (Button) findViewById(R.id.request_button);

        // Send email after clicked.
        requestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                flightInfo = flightInput.getText().toString().trim();
                name = nameInput.getText().toString();
                phone = phoneInput.getText().toString().trim();
                email = emailValidate.getText().toString().trim();

                if(!isValidEmail(email)) Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                else if(!isValidName(name)) Toast.makeText(getApplicationContext(), "Invalid Name", Toast.LENGTH_SHORT).show();
                else if(!isValidRequest(flightInfo)) Toast.makeText(getApplicationContext(), "Please make a request", Toast.LENGTH_SHORT).show();
                else if (!isValidPhone(phone))Toast.makeText(getApplicationContext(), "Please make a request", Toast.LENGTH_SHORT).show();
                else sendMail(context);

            }

        });


    }


    private boolean isValidPhone(CharSequence target){
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
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void sendMail(Context context){
        BackgroundMail bm = new BackgroundMail(context);
        bm.setGmailUserName("plrsmbl@gmail.com");
        bm.setGmailPassword("P3l4rSi9");
        bm.setMailTo("ops@plures.com.tr");
        bm.setFormSubject("Flight Request");
        String body = "Name: " + name + "\n" +"Email: " + email + "\n" + "Phone: " + phone + "\n\n" + "Flight Request:\n" + flightInfo;
        bm.setFormBody(body);
        bm.send(context);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.form_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_back:
                // Create new reminder.
                finish();
                return true;

            default:
                return false;
        }
    }
}
