package com.risahabhapps.zomatoapiinternshala;

//Using Zomato API Create an Android App that
//        1.displays information for restaurants for a given location from Zomato.
//        2.It should have a search bar where user can enter a search string, and pressing on search should search for that restaurant on zomato.
//
//        api
//        1 - geolocation done
//        2 - locations done
//        3 - Search


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.Listener;
import com.google.gson.Gson;
import com.koonat.easyfont.EditText;
import com.risahabhapps.zomatoapiinternshala.Adapters.RestaurantAdapter;
import com.risahabhapps.zomatoapiinternshala.POJO.RestautantPOJO.Restaurant;
import com.risahabhapps.zomatoapiinternshala.POJO.LocationSugPOJO.LocationSuggestion;
import com.risahabhapps.zomatoapiinternshala.POJO.LocationSugPOJO.LocationSuggestions;
import com.risahabhapps.zomatoapiinternshala.POJO.RestautantPOJO.RestaurantPOJO;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.easywaylocation.EasyWayLocation.LOCATION_SETTING_REQUEST_CODE;

public class RestaurantSearchActivity extends AppCompatActivity implements Listener {
    EasyWayLocation easyWayLocation;
    double lat,lon,newlat,newlon;
    String address,entityid;
    AutoCompleteTextView addressedit;
    boolean repeat=true;
    ArrayAdapter<String> addressadapter;
    List<String> addresslist;
    RecyclerView recycler;
    EditText search;
    LottieAnimationView gpsicon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_search);
        getSupportActionBar().hide();
        // initialization
        easyWayLocation = new EasyWayLocation(this, false,this);
        addresslist=new ArrayList<String>();
        addressedit=findViewById(R.id.address);
        search=findViewById(R.id.search);
        gpsicon=findViewById(R.id.gpsicon);
        addressedit.setSelectAllOnFocus(true);
        recycler =findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        //textwatcher
        textWatcher();

    }
// get current location coordinates
    @Override
    public void locationOn() {
        Log.d(TAG, "locationOn:");
    }

    @Override
    public void currentLocation(Location location) {
               newlat=location.getLatitude();
               newlon=location.getLongitude();
        if (repeat){
            try {
                lat = location.getLatitude();
                lon = location.getLongitude();
                geocode(Constants.API+"geocode?"+"lat="+lat+"&lon="+lon);
            }catch (Exception e){

            }
        }
    }

    @Override
    public void locationCancelled() {
       // Toast.makeText(this, "Location Cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOCATION_SETTING_REQUEST_CODE:
                easyWayLocation.onActivityResult(resultCode);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        easyWayLocation.startLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        easyWayLocation.endUpdates();

    }
    public void textWatcher(){
        addressedit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                locations(Constants.API+"locations?"+"lat="+lat+"&lon="+lon+"&query="+addressedit.getText().toString()+"&count=6");


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search(Constants.API+"search?"+"lat="+lat+"&lon="+lon+"&q="+search.getText().toString()+"&entity_id="+entityid+"&count="+25);
                System.out.println("yhi hai na "+Constants.API+"search?"+"lat="+lat+"&lon="+lon+"&q="+search.getText().toString()+"&entity_id="+entityid+"&count="+25);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //Fetching Data
    public void geocode(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        System.out.println(response);
                        repeat=false;
                        com.risahabhapps.zomatoapiinternshala.POJO.GeoPOJO.Location location=new Gson().fromJson(response, com.risahabhapps.zomatoapiinternshala.POJO.GeoPOJO.Location.class);
                        try {
                            gpsicon.setSpeed(0.1f);
                            address = location.getLocation().getTitle();//address
                            entityid =location.getLocation().getEntityId().toString();//entity id
                            addressedit.setText(address);
                        }catch (Exception e){
                            Log.d(TAG, "onResponseerror: "+e);
                        }

                        search(Constants.API+"search?"+"lat="+lat+"&lon="+lon+"&q="+search.getText().toString()+"&entity_id="+entityid+"&count="+25);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof NetworkError) {
                        } else if (error instanceof ServerError) {
                            Toast.makeText(RestaurantSearchActivity.this, "Server Error please retry........", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(RestaurantSearchActivity.this, "Authentication Failure please retry........", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                        } else if (error instanceof NoConnectionError) {
                            Toast.makeText(RestaurantSearchActivity.this, "No Internet please retry........", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError) {
                            Toast.makeText(RestaurantSearchActivity.this, "Api limit exceeded please retry after some time........", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("user-key",Constants.USER_KEY);
                return map;

            }
        };
        queue.add(postRequest);

    }

    public void getCurrentLocation(View view) {
        try {
            gpsicon.setSpeed(1f);
            geocode(Constants.API+"geocode?"+"lat="+newlat+"&lon="+newlon);
        }catch (Exception e){

        }

    }

    public void locations(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        System.out.println(response);
                        repeat=false;
                        try {
                            LocationSuggestions locationSuggestions = new Gson().fromJson(response, LocationSuggestions.class);
                            LocationSuggestion[] locationSuggestion=new Gson().fromJson(new Gson().toJson(locationSuggestions.getLocationSuggestions()),LocationSuggestion[].class);
                            List<LocationSuggestion> address_list = new ArrayList<LocationSuggestion>(Arrays.asList(locationSuggestion));

                            addresslist.clear();
                            for (int i=0;i<address_list.size();i++){
                                System.out.println(address_list.get(i));
                                addresslist.add(address_list.get(i).getTitle());
                             //   Toast.makeText(RestaurantSearchActivity.this, ""+addresslist.get(i), Toast.LENGTH_SHORT).show();
                            }

                            addressadapter = new ArrayAdapter<String>
                                    (getApplicationContext(), android.R.layout.select_dialog_item, addresslist);
                            addressedit.setAdapter(addressadapter);
                            addressedit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                     address =addresslist.get(i);//address
                                     entityid =locationSuggestion[i].getEntityId().toString();//entity id
                                    lat=address_list.get(i).getLatitude();
                                    lon=address_list.get(i).getLongitude();
                                    search(Constants.API+"search?"+"lat="+lat+"&lon="+lon+"&q="+search.getText().toString()+"&entity_id="+entityid+"&count="+25);
                                }
                            });
                            addressadapter.notifyDataSetChanged();



                        }catch (Exception e){
                            Log.d(TAG, "onResponseerror: "+e);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof NetworkError) {
                        } else if (error instanceof ServerError) {
                            Toast.makeText(RestaurantSearchActivity.this, "Server Error please retry........", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(RestaurantSearchActivity.this, "Authentication Failure please retry........", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                        } else if (error instanceof NoConnectionError) {
                        } else if (error instanceof TimeoutError) {
                        }

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("user-key",Constants.USER_KEY);
                return map;

            }
        };
        queue.add(postRequest);

    }

    public void search(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        System.out.println("ihi haray na "+response);
                        RestaurantPOJO restaurantPOJO= new Gson().fromJson(response, RestaurantPOJO.class);
                        Restaurant[] restaurantSuggestion=new Gson().fromJson(new Gson().toJson(restaurantPOJO.getRestaurants()),Restaurant[].class);
                        System.out.println("Yhi hai...."+new Gson().toJson(restaurantPOJO.getRestaurants()));
                        List<Restaurant> restaurant_list = new ArrayList<Restaurant>(Arrays.asList(restaurantSuggestion));
                        recycler.setAdapter(new RestaurantAdapter(getApplicationContext(), restaurant_list));
                        try {
                           // Toast.makeText(RestaurantSearchActivity.this, ""+entityid, Toast.LENGTH_SHORT).show();

                        }catch (Exception e){
                            Log.d(TAG, "onResponseerror: "+e);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof NetworkError) {
                        } else if (error instanceof ServerError) {
                            Toast.makeText(RestaurantSearchActivity.this, "Server Error please retry........", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(RestaurantSearchActivity.this, "Authentication Failure please retry........", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                        } else if (error instanceof NoConnectionError) {
                        } else if (error instanceof TimeoutError) {
                        }

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("user-key",Constants.USER_KEY);
                return map;

            }
        };
        queue.add(postRequest);

    }




}
