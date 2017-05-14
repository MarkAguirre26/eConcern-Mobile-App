package com.ebox.ebox;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;

public class MessageActivity extends AppCompatActivity {

    SwipeRefreshLayout pullToRefresh;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        this.setTitle("Records");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        //Initialize controls
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);
        lv = (ListView) findViewById(R.id.lv_record);
        //Get records from database
        LoadRecords();

        //Listview Item click listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                variable.Selected_Message = ((TextView) view.findViewById(R.id.txt_cn_list)).getText().toString();
                variable.Selected_Message_subject = ((TextView) view.findViewById(R.id.txt_subject_list)).getText().toString();
                startActivity(new Intent(getApplicationContext(), ContentActivity.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });
        //Refresh list when pull down.
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                LoadRecords();
                pullToRefresh.setRefreshing(false);
            }
        });

    }

    private void LoadRecords() {

        FillList<Content_Model> f = new FillList<Content_Model>();
        f.FillSListview(getApplicationContext(), new Object[]{R.id.txt_cn_list, R.id.txt_subject_list}, new String[]{"cn", "subject_name"}, R.layout.row_content_list, lv, Content_Model.class, variable.url_record, Request.Method.POST, new String[]{"ID", "Sender_cn"}, new String[]{"%", variable.Current_user_cn});

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            gotoMainActivity();
        }
        return true;
    }

    private void gotoMainActivity() {
        startActivity(new Intent(getApplicationContext(), EmptyActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        gotoMainActivity();
    }

}
