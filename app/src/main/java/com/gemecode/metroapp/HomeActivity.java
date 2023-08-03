package com.gemecode.metroapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    Button changeLang;
    SharedPreferences prefs;
    String language;


    ImageView metroLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_home);


        metroLogo = findViewById(R.id.metroLogo);


        changeLang = findViewById(R.id.changeLang);
        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show AlertDialog to display list of languages, one can be selected
                showChangeLanguageDialog();
            }
        });
    }




    //<<language button>>
    //lets create separate strings.xml for each language first
    private void showChangeLanguageDialog() {
        //array of languages to display in alert dialog
        final String[] listItems = {"English", "العربية"};
        @SuppressLint("ResourceType") androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(HomeActivity.this);
        mBuilder.setTitle(R.string.chooseLanguage);
        mBuilder.setIcon(R.drawable.ic_translation__arabic__01);
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0){
                    //English
                    setLocale("en");
                }
                else if (i==1){
                    //Arabic
                    setLocale("ar");
                }
                recreate();

                //dismiss alert dialog when language selected
                dialogInterface.dismiss();
            }
        });

        //Cancel Button
        mBuilder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        androidx.appcompat.app.AlertDialog mDialog = mBuilder.create();
        //show alert dialog
        mDialog.show();

        //cancel button color
        mDialog.getButton(mDialog.BUTTON_NEGATIVE).setTextColor(Color.argb(100,218,67,54));

        //alert dialog color
        mDialog.getWindow().setBackgroundDrawableResource(R.color.appDialogColor);

        //اللون الشفاف
//        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        //save data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    // load language saved in shared preferences
    public void loadLocale(){
        prefs = getSharedPreferences("settings", Activity.MODE_PRIVATE);
        language = prefs.getString("My_Lang", "");
        setLocale(language);
    }
    //<<language button>>



    public void metroLogo(View view) {
        YoYo.with(Techniques.RotateIn).duration(700).playOn(metroLogo);
        YoYo.with(Techniques.BounceIn).duration(1400).playOn(metroLogo);
    }

    public void start(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("languagePref",language);
        startActivity(intent);
    }
    public void metroMap(View view) {
        Intent intent = new Intent(this, ImageActivity.class);
        startActivity(intent);
    }




    // Declare the onBackPressed method when the back button is pressed this method will call
    @Override
    public void onBackPressed() {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

        // Set the message show for the Alert time
        builder.setMessage(R.string.Do_you_want_to_exit);

        // Set Alert Title
        builder.setTitle(R.string.Alert);

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton(R.string.Yes, (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            finish();
        });

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton(R.string.No, (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(Color.argb(100,218,67,54));
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(Color.argb(100,218,67,54));
    }



}