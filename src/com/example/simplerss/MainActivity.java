package com.example.simplerss;

import java.util.List;

import org.mcsoxford.rss.*;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.example.simplerss.DialogUrl.DialogCallBack;
import com.example.simplerss.FeedCursorAdapter.ViewHolder;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends SherlockFragmentActivity implements DialogCallBack {
	
    private ListView listView;
    private DataHelper dataHelper;
    private DataHelperUrl dataHelperUrl;
    private FeedCursorAdapter adapter;
    private static final String TAG = MainActivity.class.getName();
    
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_TITLE = "title";
    
    DialogUrl dialogUrl;
    
    private String url = "http://riotpixels.com/feed/";
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dialogUrl = new DialogUrl();
		dialogUrl.setDialogCallBack(this);
		dataHelper = new DataHelper(getApplicationContext());
		dataHelperUrl = new DataHelperUrl(getApplicationContext());
		listView = (ListView) findViewById(R.id.listView);	
		init();

	}
	
    @Override
    protected void onResume() {
        super.onResume();
       refreshList();
    }
	

	private void refreshList() {
		new LoadFeeds().execute();	
	}
	
	private void init() {
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
                ViewHolder holder = (ViewHolder) view.getTag();
                Intent intent = new Intent(MainActivity.this, RssDetailActivity.class);
                intent.putExtra(EXTRA_TITLE, holder.getTitle());
                intent.putExtra(EXTRA_URL, holder.getLink());
                startActivity(intent);
            }
        });
        refreshAdapter();
    }
	
    private void refreshAdapter() {
        Cursor feedCursor = dataHelper.getFeedCursor();
        adapter = new FeedCursorAdapter(MainActivity.this, feedCursor, true);
        listView.setAdapter(adapter);
    }
    
   	
	 private class LoadFeeds extends AsyncTask<Object, Void, Integer>{
		 
		public RSSFeed feed;
		private ProgressDialog progressDialog;
		
		 @Override
	        protected void onPreExecute() {
	            if (adapter.isEmpty()) {
	                progressDialog = new ProgressDialog(MainActivity.this);
	                progressDialog.setMessage(getString(R.string.cache_empty));
	                progressDialog.show();
	            }
	        }
		
		@Override
		protected Integer doInBackground(Object... params){

			try {
				RSSReader reader = new RSSReader();	 
				feed = reader.load(url);
				List<RSSItem> rssItems = feed.getItems();
				Log.i("Size of RSSItem", String.valueOf(rssItems.size()) );
				if (rssItems.isEmpty()) {					
	               return 0;
				}
					
				dataHelper.cleanOldFeeds();

					for (RSSItem rssItem : rssItems) {
						Log.i("RSS Reader", rssItem.getTitle());
						//rssItem.getThumbnails().get(0).getUrl();
						dataHelper.insertFeedItem(rssItem);
					}					
				
				} catch (RSSReaderException e) {
				e.printStackTrace();
				return 0;
				}
	        return 1;
			}
		
		@Override
        protected void onPostExecute(Integer result) {
          if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (result == 1) {
                refreshAdapter();
            } else {
                showError();
            }
        }
		
		private void showError() {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(R.string.no_network).setTitle(R.string.info).setCancelable(false)
                    .setNeutralButton(R.string.accept, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
		
	 }	 
	 
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	       MenuInflater inflater = getSupportMenuInflater();
	        inflater.inflate(R.menu.main, menu);

	        return true;
	    }
	 
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case R.id.action_set_url:
	        	dialogUrl.show(getSupportFragmentManager(), "dlg1");
	            return true;
	        case R.id.action_refresh:
	        	refreshList();
	        	refreshAdapter();
	            return true;
	        case R.id.action_list_url:
	        	Intent intentUrl = new Intent(this, RssListActivity.class);
	        	startActivity(intentUrl);
	            return true;
	        default:

	        }
	        return super.onOptionsItemSelected(item);
	    }
	 
	 @Override
	 public void setUrl(RssResource resource){
		 this.url = resource.getUrl();
		 dataHelperUrl.insertResource(resource);
		 refreshList();
     	 refreshAdapter();
	 }

}
