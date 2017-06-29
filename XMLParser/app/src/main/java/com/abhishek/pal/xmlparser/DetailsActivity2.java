package com.abhishek.pal.xmlparser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static java.net.URLEncoder.encode;

public class DetailsActivity2 extends AppCompatActivity {

    private static String timestamp="0";
    Integer count=0;
    private String TAG = DetailsActivity.class.getSimpleName();
    ImageView imageView;
    private ListView lv;
    ArrayList<HashMap<String, String>> tweetlist;
//    String url = "https://abhipal1997.000webhostapp.com/Calamities/Event1/f1/tweets.json";
    String url = "http://10.2.1.100/";
    String event,id,image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);
        imageView = (ImageView) findViewById(R.id.grid_item_image);
        Bundle bundle = getIntent().getExtras();
        image = bundle.getString("image");
        event = bundle.getString("event_name");
        id = bundle.getString("id");
        Glide.with(this).load(url+"databaselink/"+event+"/"+image).into(imageView);
        lv = (ListView) findViewById(R.id.title);
        tweetlist = new ArrayList<>();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        new GetTweets().execute();
    }

    private class GetTweets extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

                String jsonStr = null;
                String[] data={"tablename",event,"BID",id,"timestamp", timestamp};
                try {
                    jsonStr = makeServiceCall(url+"getTweets.php",data,3);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.e(TAG, "Response from url: " + jsonStr);

                if (jsonStr != null) {
                    try {
                        //JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                        //JSONArray disasters = jsonObj.getJSONArray("disaster");
                        JSONArray dissy=new JSONArray(jsonStr);
                        //timestamp=dissy.get(0).toString();
                        //Log.d(TAG,timestamp);
                        //HashMap<String,String> d=new HashMap<>();
                        JSONObject jsonObject;
                        for (int i = 1; i < dissy.length(); i++) {
                            jsonObject= (JSONObject) dissy.get(i);
                            Log.d(TAG,jsonObject.getString("ID"));
                            Log.d(TAG,jsonObject.getString("Text"));
                            String name = jsonObject.getString("ID");
//
                            String imagedata = jsonObject.getString("Text");
//
//                        // tmp hash map for single contact
                            HashMap<String, String> disaster = new HashMap<String, String>();
                            //String j=name;

                        //j= new StringBuilder().append((String) k).append(j).toString();
                            disaster.put("text", imagedata);
                            count++;
                            disaster.put("tweets", count.toString()+". "+imagedata);
                        // adding contact to contact list
                        tweetlist.add(disaster);
//


                        }
                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

                    }
                } else {

                    Log.e(MainActivity.class.getSimpleName(), "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't get json from server. Check LogCat for possible errors!",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }

                return null;
            }


//                HttpHandler sh = new HttpHandler();
//
//            // Making a request to url and getting response
//            String jsonStr = null;
//
//                jsonStr = sh.makeServiceCall(url);
//
//            Log.e(TAG, "Response from url: " + jsonStr);
//
//            if (jsonStr != null) {
//                try {
//
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//
//                    // Getting JSON Array node
//                    JSONArray disasters = jsonObj.getJSONArray("tweets");
//
//                    // looping through All Contacts
//                    for (int i = 0; i < disasters.length(); i++) {
//                        JSONObject c = disasters.getJSONObject(i);
//
////                        //String id = c.getString("id");
//                        String name = c.getString("id");
//
//                        String imagedata = c.getString("tweet");
////
////                        // tmp hash map for single contact
//                        HashMap<String, String> disaster = new HashMap<String, String>();
//                        String j=name;
//
//                        //j= new StringBuilder().append((String) k).append(j).toString();
//                        disaster.put("image", name);
//                        count++;
//                        disaster.put("tweet", count.toString()+". "+imagedata);
//                        // adding contact to contact list
//                        tweetlist.add(disaster);
//                    }
//                } catch (final JSONException e) {
//                    Log.e(TAG, "Json parsing error: " + e.getMessage());
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(),
//                                    "Json parsing error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG)
//                                    .show();
//                        }
//                    });
//
//                }
//            } else {
//
//                Log.e(TAG, "Couldn't get json from server.");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(),
//                                "Couldn't get json from server. Check LogCat for possible errors!",
//                                Toast.LENGTH_LONG)
//                                .show();
//                    }
//                });
//
//            }
//
//
//            return null;
//        }


        public String makeServiceCall(String reqUrl, String[] data,int noofdata) throws IOException {
            String response = "";
            BufferedReader responseReader = null;

            HttpURLConnection conn = null;
            try {
                Log.d(String.valueOf(this), "Inside connect");

                String post_data="";
                for(int encoder=0;encoder<(noofdata*2);encoder++)
                {
                    post_data+=encode(data[encoder++], "UTF-8") + "=" + encode(data[encoder], "UTF-8");
                    if(encoder!=((noofdata*2)-1)){
                        post_data+="&";
                    }
                }
                URL url = new URL(reqUrl+"?"+post_data);
                if (conn != null) conn.disconnect();
                conn= (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                Log.d(String.valueOf(this), "Inside connect with data " + post_data+" to "+ reqUrl);
                Log.d(String.valueOf(this), "Sent");

                InputStream inputStream = conn.getErrorStream();
                if (inputStream == null)
                    inputStream = conn.getInputStream();

                // Read everything from our stream
                responseReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                Log.d(String.valueOf(this), "Trying to read");
                String inputLine;
                //StringBuilder responses = new StringBuilder();
                response=convertStreamToString(inputStream);
                inputStream.close();
                //responseReader.close();
                //conn.disconnect();

            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (ProtocolException e) {
                Log.e(TAG, "ProtocolException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
                //responseReader.close();
            }
            finally {
                conn.disconnect();
                responseReader.close();
            }
            return response;
        }



        private String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    DetailsActivity2.this, tweetlist,
                    R.layout.list_item2, new String[]{"text"}, new int[]{R.id.tweet});

            lv.setAdapter(adapter);
            cancel(true);
        }

    }


}