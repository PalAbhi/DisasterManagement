package com.abhishek.pal.xmlparser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DetailsActivity extends AppCompatActivity {

    private String TAG = DetailsActivity.class.getSimpleName();
    private static final int ANIM_DURATION = 600;
    private TextView tweetTextView = (TextView) findViewById(R.id.title);
    private ImageView imageView;
    private Boolean isLoaded=false;

    //    private int mLeftDelta;
//    private int mTopDelta;
//    private float mWidthScale;
//    private float mHeightScale;
//
//    private FrameLayout frameLayout;
//    private ColorDrawable colorDrawable;
//
//    private int thumbnailTop;
//    private int thumbnailLeft;
//    private int thumbnailWidth;
//    private int thumbnailHeight;
    private String id;
    private String event;
    public String tweet;
    Handler handler;
    Bundle bund;
    TextView tv;
    private ListView lv;
    ArrayList<HashMap<String, String>> tweetlist;
    String url = "https://abhipal1997.000webhostapp.com/Calamities/Event1/f1/tweet.json";
//    final Runnable r = new Runnable() {
//        public void run() {
//            Bundle bund = getIntent().getExtras();
//            Log.d(String.valueOf(this), "Inside run");
//            //Log.d(String.valueOf(this),"again back to run "+result);
//            new GetTweets().execute();
//            handler.postDelayed(this, 60000);
//        }
//    };
//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(DetailsActivity.class.getSimpleName(), "In here again");
        //Setting details screen layout
        setContentView(R.layout.activity_details_view);
        handler = new Handler();
        Bundle bund = getIntent().getExtras();
        id = bund.getString("id");
        event = bund.getString("event_name");
//        handler.postDelayed(r, 5000);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        //retrieves the thumbnail data
        Bundle bundle = getIntent().getExtras();
//        thumbnailTop = bundle.getInt("top");
//        thumbnailLeft = bundle.getInt("left");
//        thumbnailWidth = bundle.getInt("width");
//        thumbnailHeight = bundle.getInt("height");

        String title = bundle.getString("id");
        String image = bundle.getString("image");

        //initialize and set the image description
        tweetTextView.setText(title + "gcghghdtgffkuygugguggjhgujkll" + "bmmvhmvvvnvjbmjbk");
        lv = (ListView) findViewById(R.id.title);
        tweetlist = new ArrayList<>();


        //Set image url
        imageView = (ImageView) findViewById(R.id.grid_item_image);
        Glide.with(this).load(image).into(imageView);

        //Set the background color to black
//        frameLayout = (FrameLayout) findViewById(R.id.main_background);
//        colorDrawable = new ColorDrawable(Color.BLACK);
//        frameLayout.setBackground(colorDrawable);
//
//        // Only run the animation if we're coming from the parent activity, not if
//        // we're recreated automatically by the window manager (e.g., device rotation)
//        if (savedInstanceState == null) {
//            ViewTreeObserver observer = imageView.getViewTreeObserver();
//            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//
//                @Override
//                public boolean onPreDraw() {
//                    imageView.getViewTreeObserver().removeOnPreDrawListener(this);
//
//                    // Figure out where the thumbnail and full size versions are, relative
//                    // to the screen and each other
//                    int[] screenLocation = new int[2];
//                    imageView.getLocationOnScreen(screenLocation);
//                    mLeftDelta = thumbnailLeft - screenLocation[0];
//                    mTopDelta = thumbnailTop - screenLocation[1];
//
//                    // Scale factors to make the large version the same size as the thumbnail
//                    mWidthScale = (float) thumbnailWidth / imageView.getWidth();
//                    mHeightScale = (float) thumbnailHeight / imageView.getHeight();
//
//                    enterAnimation();
//
//                    return true;
//                }
//            });
//        }
        new GetTweets().execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopRepeatingTask();
    }

    //   private void stopRepeatingTask() {
//        handler.removeCallbacks(r);
//    }

    /**
     * The enter animation scales the picture in from its previous thumbnail
     * size/location.
     */
//    public void enterAnimation() {
//
//        // Set starting values for properties we're going to animate. These
//        // values scale and position the full size version down to the thumbnail
//        // size/location, from which we'll animate it back up
//        imageView.setPivotX(0);
//        imageView.setPivotY(0);
//        imageView.setScaleX(mWidthScale);
//        imageView.setScaleY(mHeightScale);
//        imageView.setTranslationX(mLeftDelta);
//        imageView.setTranslationY(mTopDelta);
//
//        // interpolator where the rate of change starts out quickly and then decelerates.
//        TimeInterpolator sDecelerator = new DecelerateInterpolator();
//
//        // Animate scale and translation to go from thumbnail to full size
//        imageView.animate().setDuration(ANIM_DURATION).scaleX(1).scaleY(1).
//                translationX(0).translationY(0).setInterpolator(sDecelerator);
//
//        // Fade in the black background
//        ObjectAnimator bgAnim = ObjectAnimator.ofInt(colorDrawable, "alpha", 0, 255);
//        bgAnim.setDuration(ANIM_DURATION);
//        bgAnim.start();
//
//    }

    /**
     * The exit animation is basically a reverse of the enter animation.
     * This Animate image back to thumbnail size/location as relieved from bundle.
     *
     * @param //endAction This action gets run after the animation completes (this is
     *                  when we actually switch activities)
     */
//    public void exitAnimation(final Runnable endAction) {
//
//        TimeInterpolator sInterpolator = new AccelerateInterpolator();
//        imageView.animate().setDuration(ANIM_DURATION).scaleX(mWidthScale).scaleY(mHeightScale).
//                translationX(mLeftDelta).translationY(mTopDelta)
//                .setInterpolator(sInterpolator).withEndAction(endAction);
//
//        // Fade out background
//        ObjectAnimator bgAnim = ObjectAnimator.ofInt(colorDrawable, "alpha", 0);
//        bgAnim.setDuration(ANIM_DURATION);
//        bgAnim.start();
//    }

//    @Override
//    public void onBackPressed() {
    //exitAnimation(new Runnable() {
//            public void run() {
//                finish();
//            }
//        });
    //}

//
//    public String connect(String id, String event_name) throws UnsupportedEncodingException {
//        String login_url;
//        login_url = "https://abhipal1997.000webhostapp.com/sendtweet.php";
//        String result = null;
//        Log.d(String.valueOf(this), "Inside connect");
//        try {
//            URL url = new URL(login_url);                                                                       //convert into url
//            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();                      //cast into http connection
//            httpURLConnection.setRequestMethod("GET");
//            //httpURLConnection.setDoInput(true);
//            httpURLConnection.setDoOutput(true);
//            Log.d(String.valueOf(this), "Inside connection");
//            OutputStreamWriter outputStream = new OutputStreamWriter(httpURLConnection.getOutputStream());                                    //fetch output stream
//            Log.d(String.valueOf(this), "Outputstream Set");
//            //BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));   //to write
//            Log.d(String.valueOf(this), "Buffered Set");
//            String post_data = URLEncoder.encode("event", "UTF-8") + "=" + URLEncoder.encode(event_name, "UTF-8") + "&"
//                    + URLEncoder.encode("bucket", "UTF-8")
//                    + "=" + URLEncoder.encode(id, "UTF-8");
//            Log.d(String.valueOf(this), "Inside connect with data " + post_data);
////            bufferedWriter.write(post_data);
////            bufferedWriter.flush();
////            bufferedWriter.close();
//            outputStream.write(post_data);
//            outputStream.flush();
//            outputStream.close();
//            Log.d(String.valueOf(this), "Sent");
////HttpClient=new DefaultHttpClient
//            httpURLConnection.setDoInput(true);
//            InputStream inputStream = httpURLConnection.getInputStream();                                       //fetch input stream
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
//            result = "";
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                result += line;
//            }
//            bufferedReader.close();
//            inputStream.close();
//            //           httpURLConnection.disconnect();
//
//        } catch (UnsupportedEncodingException e) {
//            Log.d(String.valueOf(this), "Error 1");
//            e.printStackTrace();
//        } catch (ProtocolException e) {
//            Log.d(String.valueOf(this), "Error 2");
//            e.printStackTrace();
//        } catch (MalformedURLException e) {
//            Log.d(String.valueOf(this), "Error 3");
//            e.printStackTrace();
//        } catch (IOException e) {
//            Log.d(String.valueOf(this), "Error 4");
//            e.printStackTrace();
//        }
//
//        return result;
//    }


    private class GetTweets extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler2 sh = new HttpHandler2();

            // Making a request to url and getting response
            String jsonStr = null;
            try {
                jsonStr = sh.makeServiceCall(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //String h=jsonStr;



            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null)
            {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray disasters = jsonObj.getJSONArray("tweets");

                    // looping through All Contacts
                    for (int i = 0; i <disasters.length(); i++) {
                        JSONObject c = disasters.getJSONObject(i);
//
//                        //String id = c.getString("id");
                        String name = c.getString("id");
                        String imagedata = c.getString("tweet");
//
//                        // tmp hash map for single contact
                        HashMap<String, String> disaster = new HashMap<String, String>();
//
//                        // adding each child node to HashMap key => value
//                        disaster.setDate(date);
                        //disaster.setName(name);
                        //disaster.setNo_of_img(imagedata);
//                        disaster.setUrl(imageurl);
                        disaster.put("image",name);
                        disaster.put("tweet",imagedata);
                        // adding contact to contact list
                        tweetlist.add(disaster);
                    }
                }catch (final JSONException e) {
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
            }
            else {

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
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    DetailsActivity.this, tweetlist,
                    R.layout.list_item2, new String[]{"tweet"}, new int[]{R.id.tweet});

            lv.setAdapter(adapter);
            isLoaded=true;
            cancel(true);
        }

    }
}
