package tv.anandhkumar.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText city;
    TextView result;
    TextView coordinates;

    //https://api.openweathermap.org/data/2.5/weather?q=Madurai&appid=371b1f63e82bb1786efcdc21cb49100b

    String baseURL = "https://api.openweathermap.org/data/2.5/weather?q=";
    String API = "&appid=371b1f63e82bb1786efcdc21cb49100b";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        city = findViewById(R.id.getcity);
        result = findViewById(R.id.result);
        coordinates = findViewById(R.id.coordinates);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(city.getText().toString().equals("")){

                    Toast.makeText(getApplicationContext(),"Please enter your city",Toast.LENGTH_SHORT).show();
                }
                else{
                    String myURL = baseURL + city.getText().toString() + API;
                    Log.i("URL", "URL: " + myURL);

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    Log.i("JSON", "JSON: " + jsonObject);

                                    try {
                                        String info = jsonObject.getString("weather");
                                        Log.i("INFO", "INFO: "+ info);

                                        JSONArray ar = new JSONArray(info);

                                        for (int i = 0; i < ar.length(); i++){
                                            JSONObject parObj = ar.getJSONObject(i);

                                            String myWeather = parObj.getString("main");
                                            result.setText(myWeather);
                                            Log.i("ID", "ID: " + parObj.getString("id"));
                                            Log.i("MAIN", "MAIN: " + parObj.getString("main"));
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                    try {
                                        String coor = jsonObject.getString("coord");
                                        Log.i("COOR", "COOR: " + coor);
                                        JSONObject co = new JSONObject(coor);

                                        String lon = co.getString("lon");
                                        String lat = co.getString("lat");

                                        Log.i("LON", "LON: " + lon);
                                        Log.i("LAT", "LAT: " + lat);

                                        coordinates.setText("Latitude:   "+lat+"\n"+"Longitude: "+lon);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            },

                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Log.i("Error", "Something went wrong" + volleyError);

                                }
                            }


                    );
                    MySingleton.getInstance(MainActivity.this).addToRequestQue(jsonObjectRequest);

                }

            }
        });


    }
}
