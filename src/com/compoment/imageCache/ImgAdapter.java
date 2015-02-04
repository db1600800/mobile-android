package com.compoment.imageCache;

import com.android_demonstrate_abstractcode.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class ImgAdapter extends BaseAdapter {

	private static final String TAG = "ImgAdapter";
	/** 列表是否滑动中，如果是滑动状态则仅从内存中获取图片，否则开启线程去外存或网络获取图片。 */
	private boolean isScrolling = false;

	public void setFlagIsScrolling(boolean isScrolling) {
		this.isScrolling = isScrolling;
	}

	private ImageLoader mImageLoader;
	private int mCount;
	private Context mContext;
	private String[] urlArrays;

	public ImgAdapter(int count, Context context, String[] url) {
		this.mCount = count;
		this.mContext = context;
		urlArrays = url;
		mImageLoader = new ImageLoader(context);
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			// convertView = LayoutInflater.from(mContext).inflate(
			// R.layout.list_item, null);
			// viewHolder = new ViewHolder();
			// viewHolder.mTextView = (TextView) convertView
			// .findViewById(R.id.tv_tips);
			// viewHolder.mImageView = (ImageView) convertView
			// .findViewById(R.id.iv_image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String url = "";
		url = urlArrays[position % urlArrays.length];

		viewHolder.mImageView.setImageResource(R.drawable.ic_launcher);

		int defaultImg = R.drawable.ic_launcher;

		mImageLoader.setImgToImageView(url, viewHolder.mImageView, defaultImg,
				isScrolling);
		viewHolder.mTextView.setText("--" + position + "--IDLE ||TOUCH_SCROLL");

		return convertView;
	}

	static class ViewHolder {
		TextView mTextView;
		ImageView mImageView;
	}
}