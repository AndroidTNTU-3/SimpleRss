package com.example.simplerss;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedCursorAdapter extends CursorAdapter{
	
    public static class ViewHolder {
        private TextView titleTextView;
        private TextView descriptionTextView;
        private ImageView thumbImageView;
        private String link;

        public String getLink() {
            return link;
        }

        public String getTitle() {
            return titleTextView.getText().toString();
        }
    }
    
    private LayoutInflater inflater;
    
       
	public FeedCursorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		inflater = LayoutInflater.from(context);
	}
	
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        
		String title = Utils.getStringColumn(cursor, DbHelper.FEED_TITLE);
		String description = Utils.getStringColumn(cursor, DbHelper.FEED_DESCRIPTION);
		String thumbnail = Utils.getStringColumn(cursor, DbHelper.FEED_THUMBNAIL);
		String link = Utils.getStringColumn(cursor, DbHelper.FEED_URL);
        
        holder.link = link;
        holder.titleTextView.setText(title);
        holder.descriptionTextView.setText(description);
        holder.thumbImageView.setBackgroundResource(com.example.simplerss.R.drawable.ic_rss); 
               	
	}

	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		
		View view = inflater.inflate(R.layout.row, parent, false);
		ViewHolder holder = new ViewHolder();
        holder.titleTextView = (TextView) view.findViewById(R.id.row_title);
        holder.descriptionTextView = (TextView) view.findViewById(R.id.row_description);
        holder.thumbImageView = (ImageView) view.findViewById(R.id.row_thumbnail);
        view.setTag(holder);
		return view;
	}

}
