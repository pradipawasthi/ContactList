package com.financial_hospital.listdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener {
    //Creating a List of ContactDetails
    private ArrayList<ContactDetails> listdetails, filteredDetails;

    private EditText filter;
    private RelativeLayout filter_layout;
    private ImageView hide_filter_layout;
    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CustomAdapter adapter;
    private boolean isFilterTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        hide_filter_layout = (ImageView) findViewById(R.id.ib_hideKeyboard);

        filteredDetails = new ArrayList<>();

        //Initializing our contactdetails list
        listdetails = new ArrayList<>();

        //Calling method to get data
        getData();

        filter_layout = (RelativeLayout) findViewById(R.id.filter_layout);
        filter_layout.setVisibility(View.GONE);
        hide_filter_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_layout.setVisibility(View.GONE);
                if (adapter != null) {
                    adapter.updateData(listdetails);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        filter = (EditText) findViewById(R.id.myFilter);
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = filter.getText().toString().trim();
                if (adapter != null && !query.isEmpty()) {
                    filter(query, isFilterTitle);
                } else if (adapter != null && query.isEmpty()) {
                    adapter.updateData(listdetails);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //This method will get data from the web api
    private void getData() {
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Loading Data", "Please wait...", false, false);

        //Creating a json array request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing progress dialog
                        loading.dismiss();

                        //calling method to parse json array
                        parseData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(jsonArrayRequest);
    }

    //This method will parse json data
    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            ContactDetails d = new ContactDetails();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                d.setFname(json.getString(Config.TAG_FIRST_NAME));
                d.setLname(json.getString(Config.TAG_LAST_NAME));
                d.setMobile(json.getString(Config.TAG_MOBILE));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            listdetails.add(d);
        }

        //Finally initializing our adapter
        adapter = new CustomAdapter(listdetails);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ContactDetails d = listdetails.get(position);
                Intent in = new Intent(getApplicationContext(), DetailsActivity.class);
                in.putExtra("selectedPosition", position);
                if (filter_layout.getVisibility() == View.VISIBLE)
                    in.putParcelableArrayListExtra("dataParceable", filteredDetails);//binding with limit
                else
                    in.putParcelableArrayListExtra("dataParceable", listdetails);
                startActivity(in);
                Toast.makeText(getApplicationContext(), d.getFname() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


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

        if (id == R.id.filter_title) {
            isFilterTitle = true;
            filter_layout.setVisibility(View.VISIBLE);
            filter.setText("");
            filter.setHint(R.string.action_filter_title);
            filter.requestFocus();
            return true;
        }
        if (id == R.id.filter_mob) {
            isFilterTitle = false;
            filter_layout.setVisibility(View.VISIBLE);
            filter.setText("");
            filter.setHint(R.string.action_filter_cont);
            filter.requestFocus();
            return true;
        }
        if (id == R.id.action_settings) {
            Intent in = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(in);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        Toast.makeText(getApplicationContext(), "event fire", Toast.LENGTH_LONG).show();
        return false;
    }

    public void filter(String searchedKey, boolean isFilterTitle) {
        filteredDetails.clear();
        if (isFilterTitle) {
            for (ContactDetails details : listdetails) {
                if (details.getFname().toLowerCase().contains(searchedKey.toLowerCase())) {
                    if (details.getFname().toLowerCase().startsWith(searchedKey.toLowerCase()))
                        filteredDetails.add(0, details);
                    else
                        filteredDetails.add(details);
                }
            }

        } else {
            for (ContactDetails details : listdetails) {
                if ((details.getFname().toLowerCase().contains(searchedKey.toLowerCase())) ||
                        (details.getLname().toLowerCase().contains(searchedKey.toLowerCase()) ||
                                (details.getMobile().toLowerCase().contains(searchedKey.toLowerCase())))) {
                    if ((details.getFname().toLowerCase().startsWith(searchedKey.toLowerCase())) ||
                            (details.getLname().toLowerCase().startsWith(searchedKey.toLowerCase()) ||
                                    (details.getMobile().toLowerCase().startsWith(searchedKey.toLowerCase()))))
                        filteredDetails.add(0, details);
                    else
                        filteredDetails.add(details);
                }
            }
        }
        adapter.updateData(filteredDetails);
        adapter.notifyDataSetChanged();
    }
}
