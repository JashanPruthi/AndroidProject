package com.example.androidgroupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageryNasaActivity extends AppCompatActivity {
    AsyncTask<String, Integer, String> nasaImg;
    ProgressBar mProgressBar;
    TextView tt;
    TextView th;
    Button data;
    double la;
    double lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagery_nasa);
        mProgressBar = findViewById(R.id.progressBar);
         tt = findViewById(R.id.sh);
         th = findViewById(R.id.sha);
        Intent fromMain = getIntent(); // using intent to go to different activity
        // value in the string
       la =fromMain.getDoubleExtra("shubham", 0);
       lng =fromMain.getDoubleExtra("sharma", 0);
        nasaImg = new NasaImagery();
       // Saving url in the string
        String ur = "http://dev.virtualearth.net/REST/V1/Imagery/Map/Birdseye/"+la+","+lng+"/20?dir=180&ms=500,500&key=AnBLrUpzidXWn25-HE-WmfVmd0QGYfAC8SWc2BSzTFTi2VqebHjb14It1VrPTnfN";
      // Used to call the async task
        nasaImg.execute(ur);

        data = findViewById(R.id.datas);





    }


    private class NasaImagery extends AsyncTask<String, Integer, String> {
        Bitmap image = null;
        String ss = "";
        URL url ;
        String line = null;
        String date ="";
        String imgUrl;

        @Override
        protected String doInBackground(String... args) {
            try {
                 url = new URL(args[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
               InputStream response = urlConnection.getInputStream();

               // used to read the dat from the url
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();



                String fileName = date+".png";

                // Downloading the image fron the given Url
                    URL urlImg = new URL(args[0]);
                    HttpURLConnection connection = (HttpURLConnection) urlImg.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        image = BitmapFactory.decodeStream(connection.getInputStream());
                        FileOutputStream outputStream = openFileOutput(fileName , Context.MODE_PRIVATE);
                        image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                        outputStream.flush();
                        outputStream.close();
                    //}
                }
                return "s";
                    // Catchhing the different exception
            } catch (MalformedURLException mfe) {
                ss = "Malformed URL exception";
            } catch (FileNotFoundException e) {
                ss = "Can not find file.";
            } catch (IOException ioe) {
                ss = "IO Exception. Is the Wifi connected?";
            }
            //What is returned here will be passed as a parameter to onPostExecute:
            return ss;


        }

        public void onProgressUpdate(Integer... args) {

        }

        //Type3
        // After background method the data is passed here
        public void onPostExecute(String fromDoInBackground) {

            ImageView nasaImg = findViewById(R.id.imageView3);
            nasaImg.setImageBitmap(image);



          // Setting the data from url in the text Views
            String sd= new Double(lng).toString();
            th.setText("longitude -:" +sd);

            String ss = new Double(la).toString();
            tt.setText("latiTude -:" +ss);

            mProgressBar.setVisibility(View.INVISIBLE);

            data.setOnClickListener(click ->{
                Intent database = new Intent(ImageryNasaActivity.this, MainDatabase.class);
               LinearLayout ln = findViewById(R.id.real);
                //
                database.putExtra("s",tt.getText().toString());
                database.putExtra("h",th.getText().toString());


                database.putExtra("b",url.toString());

                startActivityForResult(database,400);
                //Toast.makeText(ImageryNasaActivity.this,"Save to the database",Toast.LENGTH_LONG).show();
                Snackbar.make(ln,R.string.sa,Snackbar.LENGTH_LONG).show(); // to show the information using the snackbar
            });


        }


    }
    // used to come to the previous activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 500) {
            finish();
        }
    }

}
