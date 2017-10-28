package com.abhishek.pal.xmlparser;

//    private boolean isNetworkAvailable() {
//        boolean haveConnectedWifi = false;
//        boolean haveConnectedMobile = false;
//
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
//        for (NetworkInfo ni : netInfo) {
//            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
//                if (ni.isConnected())
//                    haveConnectedWifi = true;
//            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
//                if (ni.isConnected())
//                    haveConnectedMobile = true;
//        }
//        return haveConnectedWifi || haveConnectedMobile;
//    }
//
//    private void showDialog()
//    {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Connect to wifi or quit")
//                .setCancelable(false)
//                .setPositiveButton("Connect to WIFI", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//                    }
//                })
//                .setPositiveButton("Connect to mobile data",new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
//                    }
//                })
//                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        //this.finishAffinity();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            finishAndRemoveTask ();
//                        }
//                        else
//                        {
//                            //getActivity().finish();
//                            System.exit(0);
//                        }
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;


public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    //public Handler handler;

    private ProgressDialog pDialog;
    private ListView lv;
    public Boolean isLoaded = false;
    Integer count = 1;
    // URL to get contacts JSON
    //private static String url = "http://abhipal1997.000webhostapp.com/disaster2.json";
    private static String url = "http://10.2.1.100/getLocalTables.php";
    //ArrayList<ListItem> disasterList;
    ArrayList<HashMap<String, String>> disList;
    String[] names;
    //disList.clear();
    //public SimpleTarget<Bitmap> bitmap=null;
    //ArrayList<Bitmap> bitmapArray;
    //ImageView image;
//    private Timer myTimer;
//    private static Boolean runornot = false;
    Handler handler;
    Thread feedthread;
    //ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //names=new String[10];
         //disasterList = new ArrayList<>();
        //bitmapArray = new ArrayList<>();
        disList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);


        feedthread = new Thread() {
            @Override
            public void run() {
                super.run();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "UI", Toast.LENGTH_SHORT).show();

                        new GetContacts().execute();

                    }
                });
                lv.postDelayed(this,60000);
            }
        };
        lv.postDelayed(feedthread,10);


    }

    @Override
    protected void onStop() {
        super.onStop();
        lv.removeCallbacks(feedthread);
        Log.e(MainActivity.class.getSimpleName(), "In OnStop");
        }


    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait while we fetch data from the Server...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    //JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    //JSONArray disasters = jsonObj.getJSONArray("disaster");
                    JSONArray dissy = new JSONArray(jsonStr);

//                    d.put("name", "Hello");
//                    d.put("count", count.toString());
                    if(count>0){
                        disList.clear();
                        count=0;
                    }
                    //disList.add(0,d);
                    //disList.add("name","Hello");
                    for (int i = 0; i < dissy.length(); i++) {
                        HashMap<String, String> d = new HashMap<>();
                        //JSONObject c = dissy.getJSONObject(i);
                        //String name = c.getString("name");
                        String name=dissy.get(i).toString();
                        d.put("name", dissy.get(i).toString());
                        d.put("count", count.toString());
                        //d.put("name",name);
                        //disList.add(d);
                        disList.add(i,d);
                        //names[i]=name;
                        //Log.e(TAG, String.valueOf(disList.get(i)));
                        count++;
                    }
                    // looping through All Contacts
//                    for (int i = 0; i < disasters.length(); i++) {
//                        JSONObject c = disasters.getJSONObject(i);
//
//                        //String id = c.getString("id");
//                        String name = c.getString("name");
//                        String imagedata = c.getString("imagedata");
//                        String date = c.getString("date");
//                        String imageurl = c.getString("imageurl");
//
//                        // Phone node is JSON Object
////                        JSONObject phone = c.getJSONObject("phone");
////                        String mobile = phone.getString("mobile");
////                        String home = phone.getString("home");
////                        String office = phone.getString("office");
//
//                        // tmp hash map for single contact
//                        ListItem disaster = new ListItem();
//
//                        // adding each child node to HashMap key => value
//                        disaster.setDate(date);
//                        disaster.setName(name);
//                        disaster.setNo_of_img(imagedata);
//                        disaster.setUrl(imageurl);
//
//                        // adding contact to contact list
//                        disasterList.add(disaster);
//                    }
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

                Log.e(TAG, "Couldn't get json from server.");
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

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            //isLoaded = true;
            //  finalcall();
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, disList,
                    R.layout.list_item, new String[]{"name"}, new int[]{R.id.name});


            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    //ListItem newsData = (ListItem) lv.getItemAtPosition(position);
                    //Toast.makeText(MainActivity.this, "Selected :" + " " + newsData, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), GridViewActivity.class);
                    //intent.putExtra("event_name",newsData.getName());
                    //intent.putExtra("event_name", lv.getItemIdAtPosition(position));
                    intent.putExtra("event_name", disList.get(position).get("name"));
                    startActivity(intent);
                }
            });

            cancel(true);
        }
    }
//public void finalcall(){
// lv.setAdapter(new CustomListAdapter(this, disasterList,bitmapArray));
//    ListAdapter adapter = new SimpleAdapter(
//            MainActivity.this, disList,
//            R.layout.list_item, new String[]{"tweet"}, new int[]{R.id.name});
//
//    lv.setAdapter(adapter);
//
//    lv.setAdapter((ListAdapter) disList);
//    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//    @Override
//    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
//        //ListItem newsData = (ListItem) lv.getItemAtPosition(position);
//        //Toast.makeText(MainActivity.this, "Selected :" + " " + newsData, Toast.LENGTH_LONG).show();
//        Intent intent=new Intent(getApplicationContext(), GridViewActivity.class);
//        //intent.putExtra("event_name",newsData.getName());
//        intent.putExtra("event_name",lv.getItemIdAtPosition(position));
//        startActivity(intent);
//    }

//});

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "In on Resume");
    }


}
