package com.shwetabh.ghost.studentdemo;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shwetabh.ghost.studentdemo.adater.ListViewAdapter;
import com.shwetabh.ghost.studentdemo.model.Details;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static com.shwetabh.ghost.studentdemo.Utils.util.url_Main;
import static com.shwetabh.ghost.studentdemo.Utils.util.url_create;
import static com.shwetabh.ghost.studentdemo.Utils.util.url_update;

public class MainActivity extends AppCompatActivity {
    private String nextLink;
    private ProgressBar progressBar;
    ListViewAdapter adapter;
    int count = 2;

    boolean flag_loading = false;
    //the URL having the json data
    private static final String JSON_URL = url_Main;

    //listview object
    ListView listView;

    //the hero list where we will store all the hero objects after parsing json
    List<Details> detailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Student Record");

        //initializing listview and hero list
        listView = (ListView) findViewById(R.id.list);
        detailsList = new ArrayList<>();
        //creating custom adapter object
        //adding the adapter to listview
        //   arrayList.remove([INDEX]);
        //  arrayAdapter.notifyDataSetChanged();
        adapter = new ListViewAdapter(detailsList, getApplicationContext());
        listView.setAdapter(adapter);
        loadHeroList(JSON_URL);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0)


                    if (flag_loading == false) {
                        flag_loading = true;
                        nextLink = nextLink.replace("update", "student");
                        loadHeroList(nextLink);
                    }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String name = detailsList.get(position).getName();
                String idd = detailsList.get(position).getId();
                String roll = detailsList.get(position).getRoll();
                String clas = detailsList.get(position).getClas();
                String subject = detailsList.get(position).getSubject();
                String marks = detailsList.get(position).getMarks();

                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.update, null);
                final EditText etName = (EditText) alertLayout.findViewById(R.id.et_username);
                final EditText etId = (EditText) alertLayout.findViewById(R.id.et_id);
                final EditText etRoll = (EditText) alertLayout.findViewById(R.id.et_roll);
                final EditText etClass = (EditText) alertLayout.findViewById(R.id.et_class);
                final EditText etSubject = (EditText) alertLayout.findViewById(R.id.et_subject);
                final EditText etMarks = (EditText) alertLayout.findViewById(R.id.et_marks);

                etName.setText(name);
                etId.setText(idd);
                etRoll.setText(roll);
                etClass.setText(clas);
                etSubject.setText(subject);
                etMarks.setText(marks);


                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Update Record");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
                Button submit = (Button) alertLayout.findViewById(R.id.submit);
                Button cancel_action = (Button) alertLayout.findViewById(R.id.cancel_action);

                final AlertDialog dialog = alert.create();
                cancel_action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String mname = etName.getText().toString();
                        String mid = etId.getText().toString();
                        String mroll = etRoll.getText().toString();
                        String mclass = etClass.getText().toString();
                        String idsubject = etSubject.getText().toString();
                        String idmarks = etMarks.getText().toString();

                        if (TextUtils.isEmpty(mname.trim())) {
                            etName.setError("Name field can't be empty");

                        } else {

                            postNewComment(getApplication(), mname, mid, mroll, mclass, idsubject, idmarks);
                            Toast.makeText(getBaseContext(), "Username: " + mname + " Record Updated", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                            Intent intent = new Intent(MainActivity.this,MainActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();

            }
        });
        //this method will fetch and parse the data

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.dialog, null);
                final EditText etName = (EditText) alertLayout.findViewById(R.id.et_username);
                //  final EditText etId = (EditText) alertLayout.findViewById(R.id.et_id);
                final EditText etRoll = (EditText) alertLayout.findViewById(R.id.et_roll);
                final EditText etClass = (EditText) alertLayout.findViewById(R.id.et_class);
                final EditText etSubject = (EditText) alertLayout.findViewById(R.id.et_subject);
                final EditText etMarks = (EditText) alertLayout.findViewById(R.id.et_marks);
                Button submit = (Button) alertLayout.findViewById(R.id.submit);
                Button cancel_action = (Button) alertLayout.findViewById(R.id.cancel_action);

                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);


                alert.setTitle("Add New Record");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);


                final AlertDialog dialog = alert.create();
                cancel_action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String mname = etName.getText().toString();
                        //   String mid = etId.getText().toString();
                        String mroll = etRoll.getText().toString();
                        String mclass = etClass.getText().toString();
                        String idsubject = etSubject.getText().toString();
                        String idmarks = etMarks.getText().toString();

                        if (!TextUtils.isEmpty(mname.trim())) {

                            postNewComment(getApplication(), mname, mroll, mclass, idsubject, idmarks);
                            Toast.makeText(getBaseContext(), "Username: " + mname + " New Record Added", Toast.LENGTH_SHORT).show();

                            dialog.dismiss();


                        } else {
                            etName.setError("Name field can't be empty");
                            Toast.makeText(MainActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();

            }
        });

    }


    private void loadHeroList(final String nextLinks) {

        //getting the progressbar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //making the progressbar visible
        progressBar.setVisibility(View.VISIBLE);
        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, nextLinks,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        progressBar.setVisibility(View.INVISIBLE);
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            nextLink = obj.getString("links");

                            JSONArray heroArray = obj.getJSONArray("results");
                            //now looping through all the elements of the json array
                            for (int i = 0; i < heroArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                Details details = new Details();
                                details.setName(heroObject.getString("name"));
                                details.setId(heroObject.getString("id"));
                                details.setClas(heroObject.getString("class"));
                                details.setMarks(heroObject.getString("marks"));
                                details.setRoll(heroObject.getString("roll"));
                                details.setSubject(heroObject.getString("subject"));
                                //adding the details to herolist
                                detailsList.add(details);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (!nextLinks.equalsIgnoreCase(null)) {
                            flag_loading = false;

                        }

                        adapter.notifyDataSetChanged();
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        // Toast.makeText(getApplicationContext(), "Unable to load URl "+nextLinks +error.getMessage() , Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this, "No more data to show", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

    private void postNewComment(Application application, final String mname, final String mid, final String mroll, final String mclass, final String idsubject, final String idmarks) {

        // mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(application);
        StringRequest sr = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // mPostCommentResponse.requestCompleted();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mPostCommentResponse.requestEndedWithError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", mname);
                params.put("id", mid);
                params.put("roll", mroll);
                params.put("class_name", mclass);
                params.put("subject", idsubject);
                params.put("marks", idmarks);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);

    }


    private void postNewComment(Application application, final String mname, final String mroll, final String mclass, final String idsubject, final String idmarks) {

        // mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(application);
        StringRequest sr = new StringRequest(Request.Method.POST, url_create, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // mPostCommentResponse.requestCompleted();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mPostCommentResponse.requestEndedWithError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", mname);
                params.put("roll", mroll);
                params.put("class_name", mclass);
                params.put("subject", idsubject);
                params.put("marks", idmarks);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }


}