package com.abhishek.pal.xmlparser;

/**
 * Created by User on 29-05-2017.
 */

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by User on 29-05-2017.
 */

/**
 * Created by User on 22-05-2017.
 */

 public  class HttpHandler2 {

    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler2() {
    }

    public String makeServiceCall(String reqUrl) throws IOException {
        String response = null;
        BufferedReader responseReader = null;

        HttpURLConnection conn = null;
        try {
            Log.d(String.valueOf(this), "Inside connect");

            URL url = new URL(reqUrl);
            if (conn != null) conn.disconnect();
            conn= (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            Log.d(String.valueOf(this), "Inside connection");
            OutputStreamWriter outputStream = new OutputStreamWriter(conn.getOutputStream());                                    //fetch output stream
            Log.d(String.valueOf(this), "Outputstream Set");
            //BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));   //to write
            Log.d(String.valueOf(this), "Buffered Set");
            String post_data = URLEncoder.encode("tablename", "UTF-8") + "=" + URLEncoder.encode("ChennaiFlood", "UTF-8");
            Log.d(String.valueOf(this), "Inside connect with data " + post_data);
//            bufferedWriter.write(post_data);
//            bufferedWriter.flush();
//            bufferedWriter.close();
            outputStream.write(post_data);
            outputStream.flush();
            outputStream.close();
            Log.d(String.valueOf(this), "Sent");
            //conn.setDoOutput(false);
            // read the response
//            InputStream in = new BufferedInputStream(conn.getInputStream());
//            response = convertStreamToString(in);
//            in.close();
//            Log.d(String.valueOf(this), response);
            //MainActivity m = null;
            //Toast.makeText(m.getApplication(),"Errorf").show();

            InputStream inputStream = conn.getErrorStream();
            if (inputStream == null)
                inputStream = conn.getInputStream();

            // Read everything from our stream
            responseReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            Log.d(String.valueOf(this), "Trying to read");
            String inputLine;
            StringBuilder responses = new StringBuilder();

            while ((inputLine = responseReader.readLine()) != null) {
                responses.append(inputLine);
                response+=inputLine;
                Log.d(String.valueOf(this), String.valueOf(responses));
            }
            if(response.contains("Hi"))
            {
                Log.d(String.valueOf(this),"Yes");
            }
            response=responses.toString();
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

    @NonNull
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


}
