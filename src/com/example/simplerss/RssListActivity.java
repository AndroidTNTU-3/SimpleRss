package com.example.simplerss;

import com.example.simplerss.FeedCursorAdapter.ViewHolder;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class RssListActivity extends Activity {
	
	private ListView listView;
	DataHelperUrl dataHelperUrl;
	UrlCursorAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rss_list);
		dataHelperUrl = new DataHelperUrl(getApplicationContext());
		listView = (ListView) findViewById(R.id.listViewUrl);	
		init();	
	}
	
	private void init() {
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
                ViewHolder holder = (ViewHolder) view.getTag();
                /*Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(EXTRA_TITLE, holder.getTitle());
                intent.putExtra(EXTRA_URL, holder.getLink());
                startActivity(intent);*/
            }
        });
        refreshAdapter();
    }
	
	
    private void refreshAdapter() {
        Cursor rssListCursor = dataHelperUrl.getCursor(DbHelper.RESOURCE_TABLE);
        adapter = new UrlCursorAdapter(RssListActivity.this, rssListCursor, true);
        listView.setAdapter(adapter);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rss_list, menu);
		return true;
	}

}
