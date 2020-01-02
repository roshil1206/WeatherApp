package com.example.waetherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    Button get;
    EditText city;
    TextView result;
    //https://api.openweathermap.org/data/2.5/weather?q=Paris&appid=46ae2b91ce57cec5e9df8d16ebc3b697
    String baseurl="https://api.openweathermap.org/data/2.5/weather?q=";
    String API ="&appid=46ae2b91ce57cec5e9df8d16ebc3b697";
    String URL;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get=findViewById(R.id.getweatherButton);
        city=findViewById(R.id.enterCityEditText);
        result=findViewById(R.id.WeatherReport);
        requestQueue = Singleton.getInstance(this).getMrequestqueue();
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(city.getText().toString()==null)
                {
                    city.setError("Enter City");
                }
                else
                {
                    String City = city.getText().toString();
                    URL=baseurl+City+API;
                    Log.i("URL","URL  "+URL);
                    result.setText("");
                    getdata();

                }
            }
        });
    }
    public void getdata()
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("weather");
                    JSONObject weather = jsonArray.getJSONObject(0);
                    String abc = weather.getString("main");
                    JSONObject main = response.getJSONObject("main");
                    float temp = (float) main.getDouble("temp");
                    temp-=272.15;
                    int t = (int)temp;
                    int humidity = main.getInt("humidity");
                    JSONObject wind = response.getJSONObject("wind");
                    float speed = (float) wind.getDouble("speed");
                    speed*=3.6;
                    result.setText(String.valueOf(t)+"° C");
                    result.append("\n"+abc);
                    result.append("\nHumidity: "+t+" %");
                    result.append("\nWind: "+speed+"  KMPH");

                        Log.i("U","COMP");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);

    }
}
