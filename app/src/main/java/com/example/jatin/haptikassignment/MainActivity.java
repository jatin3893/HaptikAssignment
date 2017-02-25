package com.example.jatin.haptikassignment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private String mEndPointUrl = "http://haptik.mobi/android/test_data/";
    MessagesFragment mMessagesFragment = null;
    SummaryFragment mSummaryFragment = null;
    MessageManager mMessageManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            new GetMessagesTask(this).execute(new URL(mEndPointUrl));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mMessagesFragment = new MessagesFragment();
        mSummaryFragment = new SummaryFragment();
        adapter.addFragment(mMessagesFragment, "Messages");
        adapter.addFragment(mSummaryFragment, "Summary");
        viewPager.setAdapter(adapter);
    }

    private void setMessages(String msg) {
        try {
            mMessageManager = new MessageManager(new JSONObject(msg));
            mMessagesFragment.setMessageManager(mMessageManager);
            mSummaryFragment.setMessageManager(mMessageManager);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    /*
    * Communicates with the EndPoint and sends the contents to MainActivity for further processing
    */
    private class GetMessagesTask extends AsyncTask<URL, Void, String> {
        MainActivity mActivity;
        GetMessagesTask(MainActivity activity) {
            this.mActivity = activity;
        }
        @Override
        protected String doInBackground(URL... params) {
            for (URL url: params) {
                try {
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    if (connection.getResponseCode() == 200) {
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        return response.toString();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                mActivity.setMessages(s);
            }
        }
    }
}
