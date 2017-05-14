package com.ebox.ebox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.json.JsonConverter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentActivity extends AppCompatActivity {

    ListView lv;
    TextView txt_path_detail, txt_cn_detail, txt_sender_detail, txt_subject_detail, txt_message_detail, txt_date_detail;
    ImageView img_deatil;
   // WebView webview_deatil;
    ProgressDialog progressDialog;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        this.setTitle(variable.Selected_Message_subject);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        txt_cn_detail = (TextView) findViewById(R.id.txt_cn_detail);
        txt_sender_detail = (TextView) findViewById(R.id.txt_sender_detail);
        txt_subject_detail = (TextView) findViewById(R.id.txt_subject_detail);
        txt_message_detail = (TextView) findViewById(R.id.txt_message_detail);
        txt_date_detail = (TextView) findViewById(R.id.txt_date_detail);
        txt_path_detail = (TextView) findViewById(R.id.txt_path_detail);
        // lv = (ListView) findViewById(R.id.lv_detail);
        txt_path_detail = (TextView) findViewById(R.id.txt_path_detail);
        img_deatil = (ImageView) findViewById(R.id.img_deatil);
       // webview_deatil = (WebView) findViewById(R.id.webview_deatil);
        LoadRecord();


    }

    void LoadRecord() {
        //  FillList<Content_Model> f = new FillList<Content_Model>();
        //   f.FillSListview(getApplicationContext(), new Object[]{R.id.txt_cn_detail, R.id.txt_sender_detail, R.id.txt_subject_detail, R.id.txt_message_detail, R.id.txt_date_detail, R.id.txt_path_detail}, new String[]{"cn", "Sender_name", "subject_name", "Message", "File_path", "Date_created"}, R.layout.row_content_view, lv, Content_Model.class, variable.url_selected, Request.Method.POST, new String[]{"cn"}, new String[]{variable.Selected_Message});
        progressDialog.show();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

                final StringRequest stringRequest = new StringRequest(Request.Method.POST, variable.url_selected, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (response.contains("NoData")) {

                            Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
                        } else {
                            ArrayList<Content_Model> jsonObjects = new JsonConverter<Content_Model>().toArrayList(response, Content_Model.class);
                            for (int i = 0; i <= jsonObjects.size() - 1; i++) {
                                txt_cn_detail.setText(jsonObjects.get(i).cn);
                                txt_sender_detail.setText(jsonObjects.get(i).Sender_name);
                                txt_sender_detail.setText(jsonObjects.get(i).subject_name);
                                txt_message_detail.setText(jsonObjects.get(i).Message);
                                txt_date_detail.setText(jsonObjects.get(i).Date_created);
                                //     txt_path_detail.setText(jsonObjects.get(i).File_path);
                                path = variable.url_photo + jsonObjects.get(i).File_path;
                                //  Log.d("PATH", path);
                                 Picasso.with(getApplicationContext()).load(path).into(img_deatil);
                               // startWebView(path);
                                break;
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("cn", variable.Selected_Message);
                        return params;
                    }
                };
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            gotoMessageActivity();
        }
        return true;
    }


    private void gotoMessageActivity() {
        startActivity(new Intent(getApplicationContext(), MessageActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        gotoMessageActivity();
    }
}
