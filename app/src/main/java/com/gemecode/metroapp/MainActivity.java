package com.gemecode.metroapp;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mumayank.com.airlocationlibrary.AirLocation;

public class MainActivity extends AppCompatActivity implements AirLocation.Callback {
    TextView countText, timeText, priceText, directionText, interchangeText, direction2Text, routeView;
    static  String sCount="", sTime="", price="", direction="", interchange="", direction2="", routeInfo = "";
    static int count, count1, count2, time, nCount;
    static String hour, minute;
    static byte shortestR, tallestR;
    TextView interchangeT, direction2T, anotherT;
    ImageView nearestIcon, exchangeIcon, locationIcon, locationIcon2;
    TextView nearbyText;
    TextView shortestBtn;
    ImageView anotherRouteBtn;
    Button calcButton, clearButton, timeButton;
    SharedPreferences pref;


    //timeLeftButton
    static int startPointIndex, endPointIndex, times;
    String hh, mm, h, m;
    boolean timeLeftStart = false;
    double latStart;
    double lonStart;
    double latEnd;
    double lonEnd;
    double distanceStart2End;


    //get Data from Database
    static List<String> allStations;
    static List<Double> latList;
    static List<Double> longList;


    //get my location
    AirLocation airLocation;
    String location;

    //distance
    double distanceLocation2End;


    //get some distances between my location and another locations
    static int i, index;
    static double  dis, distance;
    static double lat1, lon1, lat2, lon2;





//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //getting my information <<core of program>> depending on my inputs (startPoint, endPoint)
    static String startPoint, endPoint;
    static int fromIndex, toIndex, medIndex;
    static List<String> line1;
    static List<String> line2;
    static List<String> line3;


    //sameLine
    static List<String> theLine;     //same line


    //defLines
    static List<String> part1;      //def lines part 1
    static List<String> part2;      //def lines part 2
    static ArrayList<String> medPoint = new ArrayList<>();
    static ArrayList<String> route1 = new ArrayList<>();
    static ArrayList<String> route2 = new ArrayList<>();
    static ArrayList<String> theRoute = new ArrayList<>();


    static ArrayList<String> defCount = new ArrayList<>();
    static ArrayList<Integer> defCountNum = new ArrayList<>();
    static ArrayList<String> defTime = new ArrayList<>();
    static ArrayList<String> defPrice = new ArrayList<>();
    static ArrayList<String> defDirection1 = new ArrayList<>();
    static ArrayList<String> defInterchange= new ArrayList<>();
    static ArrayList<String> defDirection2 = new ArrayList<>();
    static ArrayList<ArrayList<String>> defRoutes = new ArrayList<>();


////////////////////////////////////<< create spinner >>////////////////////////////////////////////
    // Initialize variable
    TextView textview1;
    TextView textview2;
    ArrayList<String> arrayList;
    Dialog dialog;
    String prefLang;
    String language="en";


//////////////////////////////////////////////<onCreate>////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview1=findViewById(R.id.testView1);
        textview2=findViewById(R.id.testView2);
        countText = findViewById(R.id.countText);
        timeText = findViewById(R.id.timeText);
        priceText = findViewById(R.id.priceText);
        directionText = findViewById(R.id.directionText);
        interchangeText = findViewById(R.id.interchangeText);
        direction2Text = findViewById(R.id.direction2Text);
        routeView = findViewById(R.id.routeView);
        routeView.setMovementMethod(new ScrollingMovementMethod());
        calcButton = findViewById(R.id.calcButton);
        clearButton = findViewById(R.id.clearButton);
        timeButton = findViewById(R.id.timeButton);
        nearbyText = findViewById(R.id.nearbyText);
        nearestIcon = findViewById(R.id.nearestIcon);
        exchangeIcon = findViewById(R.id.exchangeIcon);
        locationIcon = findViewById(R.id.locationIcon);
        locationIcon2 = findViewById(R.id.locationIcon2);
        shortestBtn = findViewById(R.id.shortestBtn);
        anotherRouteBtn = findViewById(R.id.anotherRouteBtn);
        interchangeT = findViewById(R.id.interchangeT);
        direction2T = findViewById(R.id.direction2T);
        anotherT = findViewById(R.id.anotherT);


        pref = getPreferences(MODE_PRIVATE);
        prefLang =  pref.getString("prefLang", "en");
        Intent intent = getIntent();
        language = intent.getStringExtra("languagePref");
        if(language.equals(""))
            language = "en";

        sCount = pref.getString("count", "");
        nCount = pref.getInt("nCount", 0);
        sTime = pref.getString("time", "");
        price = pref.getString("price", "");
        direction = pref.getString("direction", "");
        interchange = pref.getString("interchange", "");
        direction2 = pref.getString("direction2", "");
        routeInfo = pref.getString("routeInfo", "");
        time = pref.getInt("prefTime", 0);


        countText.setText(sCount);
        timeText.setText(sTime);
        priceText.setText(price);
        directionText.setText(direction);
        interchangeText.setText(interchange);
        direction2Text.setText(direction2);
        routeView.setText(routeInfo);
        if(!sCount.equals("")){
            clearButton.setEnabled(true);
            timeButton.setEnabled(true);
        }
        if(!interchange.equals("")){
            interchangeT.setVisibility(View.VISIBLE);
            direction2T.setVisibility(View.VISIBLE);
            interchangeText.setVisibility(View.VISIBLE);
            direction2Text.setVisibility(View.VISIBLE);
            shortestBtn.setVisibility(View.VISIBLE);
            anotherRouteBtn.setVisibility(View.VISIBLE);
            anotherT.setVisibility(View.VISIBLE);
        }




        //get all_stations and Lines depending on language
        if(language.equals("en")){
            allStations = StationsDatabase.getInstance(this).stationsDAO().selectAllStationsEN();
            line1= StationsDatabase.getInstance(this).stationsDAO().selectLine1EN();
            line2= StationsDatabase.getInstance(this).stationsDAO().selectLine2EN();
            line3= StationsDatabase.getInstance(this).stationsDAO().selectLine3EN();
            if(!language.equals(prefLang))
                clearing();
        }
        else if (language.equals("ar")) {
            allStations = StationsDatabase.getInstance(this).stationsDAO().selectAllStationsAR();
            line1= StationsDatabase.getInstance(this).stationsDAO().selectLine1AR();
            line2= StationsDatabase.getInstance(this).stationsDAO().selectLine2AR();
            line3= StationsDatabase.getInstance(this).stationsDAO().selectLine3AR();
            if(!language.equals(prefLang))
                clearing();
        }


        //get latitude and longitude
        latList = StationsDatabase.getInstance(this).stationsDAO().selectAllLatitude();
        longList = StationsDatabase.getInstance(this).stationsDAO().selectAllLongitude();



///////////////////////////////////////<< create spinner >>/////////////////////////////////////////

        // assign variable

        // set value in array list
        arrayList= (ArrayList<String>) allStations;

        textview1.setOnClickListener(v -> {
            // Initialize dialog
            dialog=new Dialog(MainActivity.this);

            // set custom dialog
            dialog.setContentView(R.layout.dialog_searchable_spinner);

            // set custom height and width
            dialog.getWindow().setLayout(650,800);

            // set transparent background
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // show dialog
            dialog.show();

            //alert dialog color
            dialog.getWindow().setBackgroundDrawableResource(R.color.appDialogColor);


            // Initialize and assign variable
            EditText editText=dialog.findViewById(R.id.edit_text);
            ListView listView=dialog.findViewById(R.id.list_view);

            // Initialize array adapter
            ArrayAdapter<String> adapter=new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,arrayList);

            // set adapter
            listView.setAdapter(adapter);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            listView.setOnItemClickListener((parent, view, position, id) -> {
                // when item selected from list
                // set selected item on textView
                textview1.setText(adapter.getItem(position));

                // Dismiss dialog
                dialog.dismiss();
            });
        });



        textview2.setOnClickListener(v -> {
            // Initialize dialog
            dialog=new Dialog(MainActivity.this);

            // set custom dialog
            dialog.setContentView(R.layout.dialog_searchable_spinner);

            // set custom height and width
            dialog.getWindow().setLayout(650,800);

            // set transparent background
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // show dialog
            dialog.show();

            //alert dialog color
            dialog.getWindow().setBackgroundDrawableResource(R.color.appDialogColor);

            // Initialize and assign variable
            EditText editText=dialog.findViewById(R.id.edit_text);
            ListView listView=dialog.findViewById(R.id.list_view);

            // Initialize array adapter
            ArrayAdapter<String> adapter=new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,arrayList);

            // set adapter
            listView.setAdapter(adapter);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            listView.setOnItemClickListener((parent, view, position, id) -> {
                // when item selected from list
                // set selected item on textView
                textview2.setText(adapter.getItem(position));

                // Dismiss dialog
                dialog.dismiss();
            });
        });

    }

/////////////////////////////////////////<< End on create>>/////////////////////////////////////////


    @Override
    protected void onPause() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("count",sCount);
        editor.putInt("nCount",nCount);
        editor.putString("time",sTime);
        editor.putString("price",price);
        editor.putString("direction",direction);
        editor.putString("interchange",interchange);
        editor.putString("direction2",direction2);
        editor.putString("routeInfo", routeInfo);
        editor.putInt("prefTime", time);
        editor.putString("prefLang", language);

        editor.apply();
        super.onPause();
    }



///////////////////////////////////////////// Views ////////////////////////////////////////////////

    @SuppressLint("SetTextI18n")
    public void calc(View view) {
        calculation();
    }

    @SuppressLint("SetTextI18n")
    public void exchange(View view) {
        YoYo.with(Techniques.FadeIn).duration(500).playOn(exchangeIcon);
        if (textview1.getText()==""||textview2.getText()==""){
            Toast.makeText(this, R.string.pleaseSelectYourStations, Toast.LENGTH_SHORT).show();
            return;
        }
        YoYo.with(Techniques.BounceInUp).duration(1000).playOn(textview1);//BounceInUp
        YoYo.with(Techniques.BounceInDown).duration(1000).playOn(textview2);//BounceInDown
        exchanging();

    }

    public void clear(View view) {
        clearing();
    }

    //showing start station on map
    public void location(View view) {
        YoYo.with(Techniques.FadeIn).duration(500).playOn(locationIcon);
        if(textview1.getText().toString().equals(""))
            Toast.makeText(this, R.string.pleaseSelectFromStation, Toast.LENGTH_SHORT).show();
        else{
            location = "https://google.com/maps/place/"+
                    latList.get(allStations.indexOf(textview1.getText().toString()))+","+
                    longList.get(allStations.indexOf(textview1.getText().toString()))+
                    "?q="+textview1.getText().toString()+"+metro+station+egypt"+
                    "z=200";
            Intent in=new Intent(this,MapActivity.class);
            in.putExtra("key",location);
            startActivity(in);
        }
    }

    //showing end station on map
    public void location2(View view) {
        YoYo.with(Techniques.FadeIn).duration(500).playOn(locationIcon2);
        if(textview2.getText().toString().equals(""))
            Toast.makeText(this, R.string.pleaseSelectToStation, Toast.LENGTH_SHORT).show();
        else{
            location = "https://google.com/maps/place/"+
                    latList.get(allStations.indexOf(textview2.getText().toString()))+","+
                    longList.get(allStations.indexOf(textview2.getText().toString()))+
                    "?q="+textview2.getText().toString()+"+metro+station+egypt"+
                    "z=200";
            Intent in=new Intent(this,MapActivity.class);
            in.putExtra("key",location);
            startActivity(in);
        }
    }

    public void timeLeft(View view) {
        timeLeftStart = true;
        //get my location lat,lon
        airLocation = new AirLocation(this,this, true,
                0,"");
        airLocation.start();
        //distance and time between start station and end station
        startPointIndex = allStations.indexOf(startPoint);
        endPointIndex = allStations.indexOf(endPoint);

        latStart = latList.get(startPointIndex);
        lonStart = longList.get(startPointIndex);
        latEnd = latList.get(endPointIndex);
        lonEnd = longList.get(endPointIndex);
        distanceStart2End = distance(latStart, latEnd, lonStart, lonEnd);
        times = time;

        //distance and time left between my location and destination station
        distanceLocation2End = distance(lat1, latEnd, lon1, lonEnd);
        int timeLeft = (int) ((distanceLocation2End * times) / distanceStart2End);


        //typing of time left
        if (timeLeft / 60 > 0) {
            hh = "0" + (timeLeft - (timeLeft % 60)) / 60;
            h=""+(timeLeft - (timeLeft % 60)) / 60;
            if((timeLeft % 60)<9){
                mm = "0" + timeLeft % 60;
            }
            else{
                mm =""+ timeLeft % 60;
            }
            m = "" + timeLeft % 60;
        }
        else {
            hh = "00";
            h = "";
            if(timeLeft<9){
                mm = "0" + timeLeft;
            }
            else{
                mm = "" + timeLeft;
            }
            m = "" + timeLeft;
        }
        if(language.equals("en"))
            Toast.makeText(this, " time left is "+ hh+"h" +  " : " + mm + "m ", Toast.LENGTH_SHORT).show();
        else if(language.equals("ar"))
            Toast.makeText(this, " الوقت المتبقي "+h+"ساعه"+" : "+m+"دقيقه ", Toast.LENGTH_SHORT).show();
    }

    //showing nearest station on spinner
    public void nearestStation(View view) {
        timeLeftStart = false;
        airLocation = new AirLocation(this,this, true,
                0,"");
        airLocation.start();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        airLocation.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFailure(@NonNull AirLocation.LocationFailedEnum locationFailedEnum) {
        Toast.makeText(this, R.string.errorFetchingLocation, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSuccess(@NonNull ArrayList<Location> arrayList) {
        lat1 = arrayList.get(0).getLatitude();
        lon1 = arrayList.get(0).getLongitude();

        if(timeLeftStart)
            return;

        nearest();

        YoYo.with(Techniques.FadeIn).duration(500).playOn(nearestIcon);
        YoYo.with(Techniques.FadeIn).duration(500).playOn(nearbyText);
        YoYo.with(Techniques.BounceIn).duration(1000).playOn(textview1);
        textview1.setText(allStations.get(index));
        if(language.equals("en"))
            Toast.makeText(this, "nearest station ("+allStations.get(index)+
                    ") "+Math.round(distance)+" km", Toast.LENGTH_LONG).show();

        else if(language.equals("ar"))
            Toast.makeText(this, "أقرب محطة لك ("+allStations.get(index)+
                    ") "+Math.round(distance)+" كم", Toast.LENGTH_LONG).show();
    }
    public void nearest(){
        i=0;
        index=0;
        dis=10000000.0;
        //find nearest station by distance
        while (i<latList.size()){
            lat2 = latList.get(i);
            lon2 = longList.get(i);
            distance=distance(lat1, lat2, lon1, lon2);
            if(distance<dis){
                dis=distance;
                index=i;
            }
            i++;
        }
    }


    public void anotherRoute(View view) {
        YoYo.with(Techniques.Flash).duration(500).playOn(interchangeText);
        if(language.equals("en"))
            YoYo.with(Techniques.FadeInLeft).duration(500).playOn(routeView);
        else if(language.equals("ar"))
            YoYo.with(Techniques.FadeInRight).duration(500).playOn(routeView);

        routeInfo="";
        sCount = defCount.get(tallestR);
        sTime = defTime.get(tallestR);
        interchange = defInterchange.get(tallestR);
        countText.setText(defCount.get(tallestR));
        timeText.setText(defTime.get(tallestR));
        priceText.setText(defPrice.get(tallestR));
        directionText.setText(defDirection1.get(tallestR));
        interchangeText.setText(defInterchange.get(tallestR));
        direction2Text.setText(defDirection2.get(tallestR));
        for (int i = 0; i <= defCountNum.get(tallestR); i++) {
            routeInfo += "\n   ⚫  "+defRoutes.get(tallestR).get(i);
            if(i<defCountNum.get(tallestR))
                routeInfo += "\n    │";
        }
        routeView.setText(routeInfo);

        if (defCount.get(shortestR).equals(defCount.get(tallestR))){
            shortestBtn.setVisibility(View.INVISIBLE);
            byte temp;
            temp = tallestR;
            tallestR = shortestR;
            shortestR = temp;
            Toast.makeText(this, "The other Rote is the same length", Toast.LENGTH_SHORT).show();
        }
        else{
            anotherRouteBtn.setEnabled(false);
            anotherT.setEnabled(false);
            shortestBtn.setEnabled(true);
        }


    }

    public void shortest(View view) {
        YoYo.with(Techniques.Flash).duration(500).playOn(interchangeText);
        if(language.equals("en"))
            YoYo.with(Techniques.FadeInRight).duration(500).playOn(routeView);
        else if(language.equals("ar"))
            YoYo.with(Techniques.FadeInLeft).duration(500).playOn(routeView);

        routeInfo="";
        sCount = defCount.get(shortestR);
        sTime = defTime.get(shortestR);
        interchange = defInterchange.get(shortestR);

        countText.setText(defCount.get(shortestR));
        timeText.setText(defTime.get(shortestR));
        priceText.setText(defPrice.get(shortestR));
        directionText.setText(defDirection1.get(shortestR));
        interchangeText.setText(defInterchange.get(shortestR));
        direction2Text.setText(defDirection2.get(shortestR));
        for (int i = shortestR; i <= defCountNum.get(shortestR); i++) {
            routeInfo += "\n   ⚫  "+defRoutes.get(shortestR).get(i);
            if(i< defCountNum.get(shortestR))
                routeInfo += "\n    │";
        }
        routeView.setText(routeInfo);
        shortestBtn.setEnabled(false);
        anotherRouteBtn.setEnabled(true);
        anotherT.setEnabled(true);

    }

//////////////////////////////////////////// Functions /////////////////////////////////////////////

    @SuppressLint("SetTextI18n")
    public void calculation(){
        routeInfo="";
        startPoint = textview1.getText().toString();
        endPoint = textview2.getText().toString();

        //no stations
        if (startPoint.equalsIgnoreCase("") || endPoint.equalsIgnoreCase("")){
            Toast.makeText(this, R.string.pleaseSelectYourStationss, Toast.LENGTH_SHORT).show();
            return;
        }

        //same stations
        else if (startPoint.equals(endPoint)){
            Toast.makeText(this, R.string.youAreInTheSameStation, Toast.LENGTH_SHORT).show();
            return;
        }


        //<< same line >>//
        else if(
                line1.contains(startPoint) && line1.contains(endPoint) ||
                line2.contains(startPoint) && line2.contains(endPoint) ||
                line3.contains(startPoint) && line3.contains(endPoint)
        ) {
            if (line1.contains(startPoint) && line1.contains(endPoint))
                theLine = line1;
            else if (line2.contains(startPoint) && line2.contains(endPoint))
                theLine = line2;
            else if (line3.contains(startPoint) && line3.contains(endPoint))
                theLine = line3;
            sameLineExecution();

            interchange="";
            direction2="";

            countText.setText(sCount);
            timeText.setText(sTime);
            priceText.setText(price);
            directionText.setText(direction);
            interchangeText.setText(interchange);
            direction2Text.setText(direction2);

            interchangeT.setVisibility(View.INVISIBLE);
            direction2T.setVisibility(View.INVISIBLE);
            interchangeText.setVisibility(View.INVISIBLE);
            direction2Text.setVisibility(View.INVISIBLE);
            shortestBtn.setVisibility(View.INVISIBLE);
            anotherRouteBtn.setVisibility(View.INVISIBLE);
            anotherT.setVisibility(View.INVISIBLE);

        }
////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////


        //<< different lines >>//
        else {

            interchangeT.setVisibility(View.VISIBLE);
            direction2T.setVisibility(View.VISIBLE);
            interchangeText.setVisibility(View.VISIBLE);
            direction2Text.setVisibility(View.VISIBLE);
            shortestBtn.setVisibility(View.VISIBLE);
            anotherRouteBtn.setVisibility(View.VISIBLE);
            anotherT.setVisibility(View.VISIBLE);
            shortestBtn.setEnabled(false);
            anotherRouteBtn.setEnabled(true);
            anotherT.setEnabled(true);
            medPoint.clear();
            defCount.clear();
            defCountNum.clear();
            defTime.clear();
            defPrice.clear();
            defDirection1.clear();
            defInterchange.clear();
            defDirection2.clear();
            defRoutes.clear();
            if (line1.contains(startPoint)) {
                part1 = line1;
                if (line2.contains(endPoint)) {
                    part2 = line2;
                    if(language.equals("en")){
                        medPoint.add("sadat");
                        medPoint.add("al-shohadaa");
                    }else if(language.equals("ar")){
                        medPoint.add("السادات");
                        medPoint.add("الشهداء");
                    }
                }
                else if (line3.contains(endPoint)) {
                    part2 = line3;
                    if(language.equals("en")){
                        medPoint.add("naser");
                        medPoint.add("naser");
                    }else if(language.equals("ar")){
                        medPoint.add("جمال عبد الناصر");
                        medPoint.add("جمال عبد الناصر");
                    }
                }
            }
            else if (line2.contains(startPoint)) {
                part1 = line2;
                if (line1.contains(endPoint)) {
                    part2 = line1;
                    if(language.equals("en")){
                        medPoint.add("sadat");
                        medPoint.add("al-shohadaa");
                    }else if(language.equals("ar")){
                        medPoint.add("السادات");
                        medPoint.add("الشهداء");
                    }

                }
                else if (line3.contains(endPoint)) {
                    part2 = line3;
                    if(language.equals("en")){
                        medPoint.add("cairo university");
                        medPoint.add("ataba");
                    }else if(language.equals("ar")){
                        medPoint.add("جامعة القاهرة");
                        medPoint.add("عتبة");
                    }

                }
            }
            else if (line3.contains(startPoint)) {
                part1 = line3;
                if (line1.contains(endPoint)) {
                    part2 = line1;
                    if(language.equals("en")){
                        medPoint.add("naser");
                        medPoint.add("naser");
                    }else if(language.equals("ar")){
                        medPoint.add("جمال عبد الناصر");
                        medPoint.add("جمال عبد الناصر");
                    }
                }
                else if (line2.contains(endPoint)) {
                    part2 = line2;
                    if(language.equals("en")){
                        medPoint.add("cairo university");
                        medPoint.add("ataba");
                    }else if(language.equals("ar")){
                        medPoint.add("جامعة القاهرة");
                        medPoint.add("عتبة");
                    }
                }
            }
            defLinesExecution();

            if(defInterchange.contains("naser") || defInterchange.contains("جمال عبد الناصر")){
                shortestBtn.setVisibility(View.INVISIBLE);
                anotherRouteBtn.setVisibility(View.INVISIBLE);
                anotherT.setVisibility(View.INVISIBLE);
            }


            sCount = defCount.get(shortestR);
            nCount = defCountNum.get(shortestR);
            sTime = defTime.get(shortestR);
            interchange = defInterchange.get(shortestR);

            countText.setText(defCount.get(shortestR));
            timeText.setText(defTime.get(shortestR));
            priceText.setText(defPrice.get(shortestR));
            directionText.setText(defDirection1.get(shortestR));
            interchangeText.setText(defInterchange.get(shortestR));
            direction2Text.setText(defDirection2.get(shortestR));
            theRoute = defRoutes.get(shortestR);

            routeInfo ="";
            for (int i = 0; i <= defCountNum.get(shortestR); i++) {
                routeInfo += "\n   ⚫  "+theRoute.get(i);
                if(i<defCountNum.get(shortestR))
                    routeInfo += "\n    │";
            }

            shortestBtn.setEnabled(false);
        }

        routeView.setText(routeInfo);
        calcButton.setEnabled(false);
        clearButton.setEnabled(true);
        timeButton.setEnabled(true);
    }//main calculation function

    public void exchanging (){
        startPoint = textview1.getText().toString();
        endPoint = textview2.getText().toString();
        textview1.setText(endPoint);
        textview2.setText(startPoint);
        calcButton.setEnabled(true);
    }

    public void clearing(){
        textview1.setText("");
        textview2.setText("");
        countText.setText("");
        timeText.setText("");
        priceText.setText("");
        directionText.setText("");
        interchangeText.setText("");
        direction2Text.setText("");
        routeView.setText("");
        sCount="";
        sTime="";
        price="";
        direction="";
        interchange="";
        direction2="";
        routeInfo="";
        time=0;
        clearButton.setEnabled(false);
        timeButton.setEnabled(false);
        shortestBtn.setEnabled(false);
        anotherRouteBtn.setEnabled(false);
        anotherT.setEnabled(false);
        calcButton.setEnabled(true);
    }


    //calculate the distances between my location and all the stations
    public static double distance(double lat1, double lat2, double lon1, double lon2) {
        // math تحتوي وحدة
        // toRadians على الدالة
        // والتي تحوّل الزوايا من نظام الدرجات إلى نظام نصف القطر
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // صيغة هافرساين
        double dLon = lon2 - lon1;
        double dLat = lat2 - lat1;
        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dLon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // نصف قطر الكرة الأرضية بوحدات الكيلومتر
        double r = 6371;

        // حساب النتيجة
        return(c * r);
    }



    // SAME LINE Execution Function
    public void sameLineExecution(){
        theRoute.clear();
        sameLine(startPoint, endPoint);

        //count
        if(language.equals("en"))
            sCount=""+count+" Stations";
        else if(language.equals("ar"))
            sCount=""+count+" محطات";


        //time
        time = count * 2;
        if (time / 60 > 0) {
            hour = "0" + (time - (time % 60)) / 60;
            if((time % 60)<9)
                minute = "0" + time % 60;
            else
                minute =""+ time % 60;
        }
        else {
            if(time<9){
                hour = "00";
                minute = "0" + time;
            }
            else{
                hour = "00";
                minute = "" + time;
            }
        }
        if(language.equals("en"))
            sTime =" "+hour + "h" + " : " + minute + "m ";
        else if(language.equals("ar"))
            sTime =" "+hour + "س" + " : " + minute + "د ";


        //price
        if (count < 9)
            price = "5";
        else if (count < 16)
            price = "10";
        else
            price = "15";
        if(language.equals("en"))
            price = " "+price+" EGP ";
        else if(language.equals("ar"))
            price = " "+price+" ج.م ";


    }

    // SAME LINE Function
    public void sameLine(String startPoint, String endPoint) {
        fromIndex = theLine.indexOf(startPoint);
        toIndex = theLine.indexOf(endPoint);

        //direction 1
        if (toIndex > fromIndex) {
            if(language.equals("en")){
                if (theLine == line1)
                    direction = " El-Marg ";
                else if (theLine == line2)
                    direction = " Shobra-El-Kheima ";
                else if (theLine == line3)
                    direction = " Cairo-University ";
            }else if(language.equals("ar")){
                if (theLine == line1)
                    direction = " المرج ";
                else if (theLine == line2)
                    direction = " شبرا الخيمة ";
                else if (theLine == line3)
                    direction = " جامعة القاهرة ";
            }


            //count
            count = toIndex - fromIndex;

            //route
            theRoute .addAll(theLine.subList(fromIndex, toIndex + 1));
            for (int i = 0; i <= count; i++) {
                routeInfo += "\n   ⚫  "+theRoute.get(i);
                if(i<count)
                    routeInfo += "\n    │";
            }
        }

        //direction 2
        else {
            if(language.equals("en")){
                if (theLine == line1)
                    direction = " Helwan ";
                else if (theLine == line2)
                    direction = " El-Mounib ";
                else if (theLine == line3)
                    direction = " Adly-Mansour ";
            }else if(language.equals("ar")){
                if (theLine == line1)
                    direction = " حلوان ";
                else if (theLine == line2)
                    direction = " المنيب ";
                else if (theLine == line3)
                    direction = " عدلي منصور ";
            }


            //count
            count = fromIndex - toIndex;


            //route
            List<String> route = theLine.subList(toIndex, fromIndex + 1);
            ArrayList<String> temp = new ArrayList<>(route);
            Collections.reverse(temp);
            theRoute.addAll(temp);
            for (int i = 0; i <= count; i++) {
                routeInfo += "\n   ⚫  "+theRoute.get(i);
                if(i<count)
                    routeInfo += "\n    │";
            }
        }

    }


////////////////////////////////////////////////////////////////////////////////////////////////////


    // DIFFERENT LINE Execution Function
    public void defLinesExecution(){
        for (int j = 0, medPointSize = medPoint.size(); j < medPointSize; j++) {
            String value = medPoint.get(j);
            theRoute.clear();
            route1.clear();
            route2.clear();
            p1(startPoint, value);
            p2(value, endPoint);

            //interchange
            defInterchange.add(medPoint.get(j));


            //route
            theRoute = route1;
            theRoute.addAll(route2);
            ArrayList<String> temp = new ArrayList<>(theRoute);
            defRoutes.add(temp);




            //count
            count = count1 + count2;
            if(language.equals("en"))
                sCount=""+count+" Stations";
            else if(language.equals("ar"))
                sCount=""+count+" محطات";
            defCountNum.add(count);
            defCount.add(sCount);


            //time
            time = (count * 2) + 5;
            if (time / 60 > 0) {
                hour = "0" + (time - (time % 60)) / 60;
                if ((time % 60) < 9)
                    minute = "0" + time % 60;
                else
                    minute = "" + time % 60;
            }
            else {
                if (time < 9) {
                    hour = "00";
                    minute = "0" + time;
                } else {
                    hour = "00";
                    minute = "" + time;
                }
            }
            if(language.equals("en"))
                sTime =" "+hour + "h" + " : " + minute + "m ";
            else if(language.equals("ar"))
                sTime =" "+hour + "س" + " : " + minute + "د ";
            defTime.add(sTime);

            //price
            if (count < 9)
                price = "5";
            else if (count < 16)
                price = "10";
            else
                price = "15";
            if(language.equals("en"))
                price = " "+price+" EGP ";
            else if(language.equals("ar"))
                price = " "+price+" ج.م ";
            defPrice.add(price);


        }
    }
    // DIFFERENT LINE Function 1
    public void p1(String startPoint, String medPoint) {
        fromIndex = part1.indexOf(startPoint);
        medIndex = part1.indexOf(medPoint);

        // route1, direction 1
        if (fromIndex < medIndex) {
            if(language.equals("en")){
                if (part1 == line1)
                    direction = " El-Marg ";
                else if (part1 == line2)
                    direction = " Shobra-El-Kheima ";
                else if (part1 == line3)
                    direction = " Adly-Mansour ";
            }
            else if(language.equals("ar")){
                if (part1 == line1)
                    direction = " المرج ";
                else if (part1 == line2)
                    direction = " شبرا الخيمة ";
                else if (part1 == line3)
                    direction = " عدلي منصور ";
            }
            defDirection1.add(direction);

            count1 = medIndex - fromIndex;
            route1.addAll(part1.subList(fromIndex, medIndex + 1));

            shortestR=0;
            tallestR=1;

        }


        // route1, direction 2
        else if (medIndex < fromIndex) {
            if(language.equals("en")){
                if (part1 == line1)
                    direction = " Helwan ";
                else if (part1 == line2)
                    direction = " El-Mounib ";
                else if (part1 == line3)
                    direction = " Cairo-University ";

            }
            else if(language.equals("ar")){
                if (part1 == line1)
                    direction = " حلوان ";
                else if (part1 == line2)
                    direction = " المنيب ";
                else if (part1 == line3)
                    direction = " جامعة القاهرة ";


            }
            defDirection1.add(direction);

            count1 = fromIndex - medIndex;
            route1.addAll(part1.subList(medIndex, fromIndex + 1));
            ArrayList<String> temp = new ArrayList<>(route1);
            Collections.reverse(temp);
            route1 = temp;


            shortestR=1;
            tallestR=0;

        }

        System.out.println("*********************************************************************");
        System.out.println(route1);
        System.out.println("*********************************************************************");



    }

    // DIFFERENT LINE Function 2
    public void p2(String medPoint, String endPoint) {
        medIndex = part2.indexOf(medPoint);
        toIndex = part2.indexOf(endPoint);

        // route2, direction 1
        if (medIndex < toIndex) {
            if(language.equals("en")){
                if (part2 == line1)
                    direction2 = " El-marg ";
                else if (part2 == line2)
                    direction2 = " Shobra-El-Kheima ";
                else if (part2 == line3)
                    direction2 = " Adly-Mansour ";
            }
            else if(language.equals("ar")){
                if (part2 == line1)
                    direction2 = " المرج ";
                else if (part2 == line2)
                    direction2 = " شبرا الخيمة ";
                else if (part2 == line3)
                    direction2 = " عدلي منصور ";
            }
            defDirection2.add(direction2);

            count2 = toIndex - medIndex;
            route2.addAll(part2.subList(medIndex + 1, toIndex + 1));

        }


        // route2, direction 2
        else if (toIndex < medIndex) {
            if(language.equals("en")){
                if (part2 == line1)
                    direction2 = " Helwan ";
                else if (part2 == line2)
                    direction2 = " El-Mounib ";
                else if (part2 == line3)
                    direction2 = " Cairo-University ";

            }
            else if(language.equals("ar")){
                if (part2 == line1)
                    direction2 = " حلوان ";
                else if (part2 == line2)
                    direction2 = " المنيب ";
                else if (part2 == line3)
                    direction2 = " جامعة القاهرة ";

            }
            defDirection2.add(direction2);

            count2 = medIndex - toIndex;
            route2.addAll(part2.subList(toIndex, medIndex));
            ArrayList<String> temp = new ArrayList<>(route2);
            Collections.reverse(temp);
            route2 = temp;

        }

        System.out.println("*********************************************************************");
        System.out.println(route2);
        System.out.println("*********************************************************************");


    }



}

