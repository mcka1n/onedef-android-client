package com.hacknplay.onedef_android_client;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
            //

            return rootView;
        }
    }
}
