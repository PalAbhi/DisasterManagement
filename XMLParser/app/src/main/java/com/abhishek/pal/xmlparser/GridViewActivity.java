package com.abhishek.pal.xmlparser;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import java.util.Collections;

import static java.net.URLEncoder.encode;

/**
 * Created by User on 24-05-2017.
 */
public class GridViewActivity extends AppCompatActivity {
    public static String timestamp="0";
    private static final String TAG = GridViewActivity.class.getSimpleName();
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    public static Boolean timetostop=false;
    //private String FEED_URL = "http://javatechig.com/?json=get_recent_posts&count=45";
    //private String FEED_URL ="http://abhipal1997.000webhostapp.com/Calamity.json";
    private String FEED_URL="http://10.2.1.100/getData.php";
    public int offset=0;
    public int length=24;
    String event;
    android.os.Handler handler,handler1;
    Thread feedthread,feedthread1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);
        event=getIntent().getExtras().getString("event_name");
        mGridView = (GridView) findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, mGridData,event);
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                GridItem item = (GridItem) parent.getItemAtPosition(position);
                //Pass the image title and url to DetailsActivity
                Intent intent = new Intent(GridViewActivity.this, DetailsActivity2.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("event_name",getIntent().getExtras().getString("event_name"));
                intent.putExtra("image", item.getImage());
                //Start details activity
                startActivity(intent);
            }
        });
        //mGridView.setOnClickListener();
        //Start download

        handler1=new Handler();

        feedthread1 = new Thread()
        {
            @Override
            public void run() {
                super.run();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "UI", Toast.LENGTH_SHORT).show();
                        Collections.sort(mGridData,new TweetSorter());
                        Collections.reverse(mGridData);
                    }
                });
                handler1.postDelayed(this, 3000);
            }
        };
        handler1.postDelayed(feedthread1, 2000);




        handler=new android.os.Handler();

        feedthread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "UI", Toast.LENGTH_SHORT).show();

                        new AsyncHttpTask().execute();

                    }
                });
                handler.postDelayed(this, 4000);
            }
        };
        handler.postDelayed(feedthread, 20);



//        new AsyncHttpTask().execute(FEED_URL);
        //mProgressBar.setVisibility(View.VISIBLE);
    }
    //Downloading data asynchronously
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                // Create Apache HttpClient
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
//                int statusCode = httpResponse.getStatusLine().getStatusCode();
//                // 200 represents HTTP OK

                String jsonStr = null;
                String[] data={"tablename",event,"timestamp",timestamp,"offset", String.valueOf(offset),"length", String.valueOf(length)};
                try {
                    jsonStr = makeServiceCall(FEED_URL,data,4);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.e(TAG, "Response from url: " + jsonStr);


//                URL url = new URL(FEED_URL);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("GET");
//                // read the response
//                InputStream in = new BufferedInputStream(conn.getInputStream());
                //int statusCode=conn.getResponseCode();
                //String response = convertStreamToString(in);

                //if (statusCode == 200) {
                if(!timetostop) {
                    parseResult(jsonStr);
                    result = 1; // Successful
                }
                else
                {
                    Log.e(TAG,"Its time to stop");
                    handler.removeCallbacks(feedthread);
                }
                    //} else {
                    //result = 0; //"Failed
                //}
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }


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
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            if (result == 1) {
                mGridAdapter.setGridData(mGridData);
            } else {
                Toast.makeText(GridViewActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
            mProgressBar.setVisibility(View.GONE);
            offset+=24;
        }
    }
    /**
     * Parsing the feed results and get the list
     * @param jsonStr
     */
    private void parseResult(String jsonStr) {
        try {
            JSONArray dissy=new JSONArray(jsonStr);
            //timestamp=dissy.get(0).toString();
            //Log.d(TAG,timestamp);
            //HashMap<String,String> d=new HashMap<>();
            JSONObject jsonObject;


            //JSONObject response = new JSONObject(jsonStr);
            //JSONArray res=new JSONArray(jsonStr);
            //timestamp=res.get(0).toString();
            //JSONArray posts = response.optJSONArray("posts");
            GridItem item = null;
            if(dissy.length()<12)
            {
                timetostop=true;
            }
            for (int i = 1; i < dissy.length(); i++) {
                JSONObject post = dissy.optJSONObject(i);
                String mag = post.optString("mag");
                //String title = post.optString("title");
                item = new GridItem();
                item.setTitle(mag);
                item.setId(post.optString("ID"));
                //JSONArray attachments = post.getJSONArray("attachment");
                //if (null != attachments && attachments.length() > 0) {
                //   JSONObject attachment = attachments.getJSONObject(0);
                //   if (attachment != null
                item.setImage(post.optString("image"));
                mGridData.add(item);
            }
            //Collections.sort(mGridData, new TweetSorter());
            } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        handler.removeCallbacks(feedthread);
    }

}


