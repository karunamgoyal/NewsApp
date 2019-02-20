package com.socialcops.news;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SearchFragment extends Fragment {

    String API_KEY = "7f2dd350357d4a9b90873fc6b07f7535"; // ### YOUE NEWS API HERE ###
    ListView listNews;
    ProgressBar loader;
    EditText searchView;
    SearchNewsAdapter adapter;
    View v;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    static final String KEY_AUTHOR = "author";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_URL = "url";
    static final String KEY_URLTOIMAGE = "urlToImage";
    static final String KEY_PUBLISHEDAT = "publishedAt";
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_search, container, false);
        listNews = (ListView) v.findViewById(R.id.searchNews);
        loader = (ProgressBar) v.findViewById(R.id.sloader);
        listNews.setEmptyView(loader);
        searchView =getActivity().findViewById(R.id.searchText);
        if(searchView.getText().toString()!=null&&!searchView.getText().toString().equals("")){
            String s[]=searchView.getText().toString().split(" ");
            String s1="";
            int i=0;
            for(String x:s){
                if(i==0) {
                    s1 += x;
                    i++;
                }
                else
                    s1=s1+"+"+x;
            }
            Variables.SEARCH=s1;
        }
        else{
            Variables.SEARCH="india";
        }
        searchView.setOnKeyListener(new View.OnKeyListener() {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
        // If the event is a key-down event on the "enter" button
        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
           (keyCode == KeyEvent.KEYCODE_ENTER)) {
            if(Function.isNetworkAvailable(container.getContext()))
                {
                    if(searchView.getText().toString()!=null&&!searchView.getText().toString().equals("")){
                       String s[]=searchView.getText().toString().split(" ");
                        String s1="";
                        int i=0;
                        for(String x:s){
                            if(i==0) {
                                s1 += x;
                                i++;
                            }
                            else
                                s1=s1+"+"+x;
                        }
                        Variables.SEARCH=s1;
                        System.out.println("Thisis line under this");
                    }
                    else{
                        Variables.SEARCH="india";
                    }
                    DownloadNews newsTask = new DownloadNews();
                    newsTask.execute();
                    FragmentTransaction ftr = getFragmentManager().beginTransaction();
                    ftr.detach(SearchFragment.this).attach(SearchFragment.this).commit();
                }else{
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
          return true;
        }
        return false;
        }
    });
        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        if(Function.isNetworkAvailable(container.getContext()))
        {
            DownloadNews newsTask = new DownloadNews();
            newsTask.execute();
        }else{
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }


        return v;
    }
    private void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

    class DownloadNews extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected String doInBackground(String... args) {
            String xml = "";

            String urlParameters = "";
            xml = Function.excuteGet("https://newsapi.org/v2/everything?q="+Variables.SEARCH+"&apiKey="+API_KEY, urlParameters);
            return  xml;
        }
        @Override
        protected void onPostExecute(String xml) {

            if(xml.length()>10){ // Just checking if not empty

                try {
                    JSONObject jsonResponse = new JSONObject(xml);
                    JSONArray jsonArray = jsonResponse.optJSONArray("articles");
                    System.out.println("Hello "+Variables.SEARCH);
                    dataList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_AUTHOR, jsonObject.optString(KEY_AUTHOR).toString());
                        map.put(KEY_TITLE, jsonObject.optString(KEY_TITLE).toString());
                        map.put(KEY_DESCRIPTION, jsonObject.optString(KEY_DESCRIPTION).toString());
                        map.put(KEY_URL, jsonObject.optString(KEY_URL).toString());
                        map.put(KEY_URLTOIMAGE, jsonObject.optString(KEY_URLTOIMAGE).toString());
                        map.put(KEY_PUBLISHEDAT, jsonObject.optString(KEY_PUBLISHEDAT).toString());
                        dataList.add(map);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                }

                 adapter = new SearchNewsAdapter(getActivity(), dataList);
                listNews.setAdapter(adapter);

                listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent i = new Intent(getActivity(),DetailsActivity.class);
                        i.putExtra("url", dataList.get(+position).get(KEY_URL));
                        startActivity(i);
                    }
                });

            }else{
                Toast.makeText(getContext(), "No news found", Toast.LENGTH_SHORT).show();
            }
        }



    }



}
