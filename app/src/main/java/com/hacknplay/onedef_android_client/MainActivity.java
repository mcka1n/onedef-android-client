package com.hacknplay.onedef_android_client;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private ArrayAdapter<String> mOneDefAdapter;

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Add this line in order for this fragment to handle menu events.
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            //inflater.inflate(R.menu.forecastfragment, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.

            FetchThingsTask weatherTask = new FetchThingsTask();
            weatherTask.execute("94043");
            return true;

           // return super.onOptionsItemSelected(item);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            // Insert your awesomeness here

            String[] thingsArray = {
                "Tamarindo - Sunny - Surfers",
                "Manuel Antonio, CR - JustMarried - Clean",
                "Tikal, GT - Mayas - Jungle",
                "San Francisco, CA - Foggy - OldSchool",
                "Manhattan, NY - Smelly - Vintage",
                "Zona 4, GT - Hipster - Dangerous",
                "Casio - Quality - Cheap",
                "Starbucks - Coffee - Expensive",
                "New York Yankees - Sucks - GetJeterBack",
                "Ramen Soup - Delicious - Healthy"
            };

            List<String> weekThings = new ArrayList<String>(Arrays.asList(thingsArray));

            // Adapter for my fake data
            mOneDefAdapter =
                    new ArrayAdapter<String>(
                            // The current context (this fragment's parent activity)
                            getActivity(),
                            // ID of list item layout
                            R.layout.list_item_things,
                            // ID of textview to populate
                            R.id.list_item_things_textview,
                            // Forecast fake data
                            weekThings);

            ListView listView = (ListView) rootView.findViewById(R.id.listview_things);
            listView.setAdapter(mOneDefAdapter);

            // hack

            FetchThingsTask weatherTask = new FetchThingsTask();
            weatherTask.execute("94043");

            return rootView;
        }

        public class FetchThingsTask extends AsyncTask<String, Void, String[]> {

            private final String LOG_TAG = FetchThingsTask.class.getSimpleName();

            // Helper methods

            /**
             * Take the String representing the complete things in JSON Format and
             * pull out the data we need to construct the Strings needed for the wireframes.
             */
            private String[] getThingsDataFromJson(String thingsJsonStr)
                    throws JSONException {

                // These are the names of the JSON objects that need to be extracted.
                final String OWM_ID = "id";
                final String OWM_NAME = "name";
                final String OWM_DESCRIPTION = "description";

                JSONArray thingsArray = new JSONArray(thingsJsonStr);

                String[] resultStrs = new String[thingsArray.length()];
                for(int i = 0; i < thingsArray.length(); i++) {
                    Log.v(LOG_TAG, "Things array String: " + thingsArray.getJSONObject(i));

                    String id;
                    String name;
                    String description;

                    // Get the JSON object representing the thing
                    JSONObject thing = thingsArray.getJSONObject(i);

                    id = thing.getString(OWM_ID);
                    name = thing.getString(OWM_NAME);
                    description = thing.getString(OWM_DESCRIPTION);

                    resultStrs[i] = id.toString() + " - " + name.toString() + " - " + description.toString();
                    Log.v(LOG_TAG, "Ready to return array: " + resultStrs);
                }

                return resultStrs;
            }

            @Override
            protected String[] doInBackground(String... params) {

                // No params? no funny.
                if (params.length == 0){
                    return null;
                }

                // These two need to be declared outside the try/catch
                // so that they can be closed in the finally block.
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

                // Will contain the raw JSON response as a string.
                String forecastJsonStr = null;

                String key = "RrZLZQvmdBT2qXssD8Rt";
                String token = "7feme2shKUL4ydoPsniz";

                try {
                    // Construct the URL for the OneDef query
                    final String BASE_URL =
                            "https://onedef-staging.herokuapp.com/v1/things?";
                    final String KEY_PARAM = "key";
                    final String TOKEN_PARAM = "token";

                    Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                            .appendQueryParameter(KEY_PARAM, key)
                            .appendQueryParameter(TOKEN_PARAM, token)
                            .build();

                    URL url = new URL(builtUri.toString());
                    Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                    // Create the request to OneDefAPI, and open the connection
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        return null;
                    }
                    forecastJsonStr = buffer.toString();

                    Log.v(LOG_TAG, "Forecast JSON String: " + forecastJsonStr);

                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error ", e);
                    // If the code didn't successfully get the weather data, there's no point in attemping
                    // to parse it.
                    return null;
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e(LOG_TAG, "Error closing stream", e);
                        }
                    }
                }

                try {
                    return getThingsDataFromJson(forecastJsonStr);
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }

                return null;
            }

            @Override
            protected void onPostExecute(String[] result) {
                if (result != null){
                    mOneDefAdapter.clear();
                    for (String dayForecastStr : result){
                        mOneDefAdapter.add(dayForecastStr);
                    }
                }
            }

        } // FetchThingsTask

    }
}
