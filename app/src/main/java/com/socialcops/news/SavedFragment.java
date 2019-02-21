package com.socialcops.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class SavedFragment extends Fragment {

    String API_KEY = "7f2dd350357d4a9b90873fc6b07f7535";
    ListView listNews;
    String filename="SocialCopsNewsAPPSaved";
    TextView loader;
    FileOutputStream outputStream;
    FileInputStream inputStream;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    View v;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    static final String NEWS_SOURCE="name";
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_saved, container, false);
        listNews = (ListView) v.findViewById(R.id.SavedNews);
        loader = (TextView) v.findViewById(R.id.snloader);
        listNews.setEmptyView(loader);
            File file = new File(getContext().getFilesDir(), filename);

            try {
                inputStream = new FileInputStream(file);
                objectInputStream = new ObjectInputStream(inputStream);
                Variables.Saved = (ArrayList<HashMap<String, String>>) objectInputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (Variables.Saved != null&&!Variables.Saved.isEmpty()) {
                SavedNewsAdapter adapter = new SavedNewsAdapter(getActivity(), Variables.Saved);
                listNews.setAdapter(adapter);
            }
            else{
                Toast.makeText(getContext(), "No Saved News", Toast.LENGTH_LONG).show();
            }
            listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(getActivity(),DetailsActivity.class);
                i.putExtra(KEY_URL, Variables.Saved.get(+position).get(KEY_URL));
                i.putExtra(KEY_AUTHOR, Variables.Saved.get(+position).get(KEY_AUTHOR));
                i.putExtra(KEY_TITLE, Variables.Saved.get(+position).get(KEY_TITLE));
                i.putExtra(KEY_DESCRIPTION, Variables.Saved.get(+position).get(KEY_DESCRIPTION));
                i.putExtra(KEY_PUBLISHEDAT, Variables.Saved.get(+position).get(KEY_PUBLISHEDAT));
                startActivity(i);
            }
        });
        FloatingActionButton fab=v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(getContext().getFilesDir(), filename);
                try {
                    Variables.Saved.clear();
                    outputStream=new FileOutputStream(file);
                    objectOutputStream=new ObjectOutputStream(outputStream);
                    objectOutputStream.writeObject(Variables.Saved);
                    outputStream.close();
                    FragmentTransaction ftr = getFragmentManager().beginTransaction();
                    ftr.detach(SavedFragment.this).attach(SavedFragment.this).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return v;
    }

}
