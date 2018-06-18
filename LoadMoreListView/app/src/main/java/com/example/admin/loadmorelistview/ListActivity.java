package com.example.admin.loadmorelistview;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
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

public class ListActivity extends AppCompatActivity {

    Customadapter customadapter;
    ListView lv;
    ArrayList<MonAn> mangMonAn;
    ImageButton btnback;
    int page = 0;
    String url ="http://192.168.1.3:3000/api/";
    View footerview;
    boolean isLoading = false;
    boolean limitdata = false;
    mHandler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        btnback = (ImageButton) findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Anhxa();
        GetData(page);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Toast.makeText(ListActivity.this,mangMonAn.get(i).TenMonAn + "",Toast.LENGTH_SHORT).show();
            }
        });
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstItem, int visibleItem, int totalItem) {
                if (absListView.getLastVisiblePosition() == totalItem - 1 && totalItem != 0 && isLoading == false){
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    private void GetData(int Page) {
        RequestQueue requestQueue = Volley.newRequestQueue(ListActivity.this);
        String duongdan = url + String.valueOf(Page);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(duongdan, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null && response.length() !=0){
//                    lv.removeFooterView(footerview);
                    for (int i=0; i<response.length(); i++)
                    {
                        try {
                            JSONObject jsonObjectMonAn = response.getJSONObject(i);
                            int id = jsonObjectMonAn.getInt("id");
                            String tenmonan = jsonObjectMonAn.getString("ten");
                            String hinhanh = jsonObjectMonAn.getString("hinh");
                            mangMonAn.add(new MonAn(id,tenmonan,hinhanh));
                            customadapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }else {
//                    lv.removeFooterView(footerview);
                    limitdata = true;
                    Toast.makeText(ListActivity.this,"Đã hết dữ liệu để load",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void Anhxa() {
        lv = (ListView) findViewById(R.id.Listview);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        footerview = inflater.inflate(R.layout.footer_view,null);
        mangMonAn = new ArrayList<>();
        customadapter = new Customadapter(ListActivity.this,mangMonAn);
        lv.setAdapter(customadapter);
        mHandler = new mHandler();
    }
    public class mHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0 :
                    lv.addFooterView(footerview);
                    Log.d("test", "handleMessage: xxx" );
                    break;
                case 1 :
                    GetData(++page);
                    isLoading = false;
                    break;

            }
        }
    }
    public  class ThreadData extends Thread{
        @Override
        public void run() {
            super.run();
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
        }
    }
}
