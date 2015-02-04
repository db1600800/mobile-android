package com.compoment.listview;



import com.android_demonstrate_abstractcode.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

//http://www.cnblogs.com/noTice520/archive/2012/02/10/2345057.html
/**
 *implements OnRefreshLoadingMoreListener
 *RefreshAndLoadMoreAbilityForList refreshAndLoadMoreAbilityForList ;
 *refreshAndLoadMoreAbilityForList=new RefreshAndLoadMoreAbilityForList(context,listView);
 *refreshAndLoadMoreAbilityForList.setOnRefreshListener(this);
 *
 *	refreshAndLoadMoreAbilityForList.onRefreshComplete();
 *  refreshAndLoadMoreAbilityForList.onLoadMoreComplete();
 *
 * */

public class RefreshAndLoadMoreAbilityForList implements OnScrollListener {

	public ListView listView = null;

	// 拖拉ListView枚举所有状态
	private enum DListViewState {
		LV_NORMAL, // 普通状态
		LV_PULL_REFRESH, // 下拉状态（为超过mHeadViewHeight）
		LV_RELEASE_REFRESH, // 松开可刷新状态（超过mHeadViewHeight）
		LV_LOADING;// 加载状态

	}

	private View mHeadView;// 头部headView
	private View mFootView;
	private Button mFootBtn;
	private ProgressBar mFootProgressBar;
	private TextView mRefreshTextview; // 刷新msg（mHeadView）
	private TextView mLastUpdateTextView;// 更新事件（mHeadView）
	private ImageView mArrowImageView;// 下拉图标（mHeadView）
	private ProgressBar mHeadProgressBar;// 刷新进度体（mHeadView）

	private int mHeadViewWidth; // headView的宽（mHeadView）
	private int mHeadViewHeight;// headView的高（mHeadView）

	private Animation animation, reverseAnimation;// 旋转动画，旋转动画之后旋转动画.

	private int mFirstItemIndex = -1;// 当前视图能看到的第一个项的索引

	// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private boolean mIsRecord = false;

	public int mStartY, mMoveY;// 按下是的y坐标,move时的y坐标

	private DListViewState mlistViewState = DListViewState.LV_NORMAL;// 拖拉状态.(自定义枚举)

	private final static int RATIO = 2;// 手势下拉距离比.

	private boolean mBack = false;// headView是否返回.

	private OnRefreshLoadingMoreListener onRefreshLoadingMoreListener;// 下拉刷新接口（自定义）

	private boolean isScroller = true;// 是否屏蔽ListView滑动。

	public int lastVisibleIndex = 0;// 最后可见条目的索引

	public RefreshAndLoadMoreAbilityForList(Context context, ListView listViews) {
		listView = listViews;
		listView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent ev) {
				// TODO Auto-generated method stub

				return onTouchEvent(ev);
			}
		});
		listView.setOnScrollListener(this);// ListView滚动监听

		initDragListView(context);
	}

	// 注入下拉刷新接口
	public void setOnRefreshListener(
			OnRefreshLoadingMoreListener onRefreshLoadingMoreListener) {
		this.onRefreshLoadingMoreListener = onRefreshLoadingMoreListener;
	}

	/***
	 * 初始化ListView
	 */
	public void initDragListView(Context context) {

		String time = "1994.12.05";// 更新时间

		initHeadView(context, time);// 初始化该head.

		initFootView(context);
	}

	public void initFootView(Context context) {

		mFootView = LayoutInflater.from(context).inflate(
				R.layout.listview_footer, null);
		mFootBtn = (Button) mFootView.findViewById(R.id.bt_load);
		mFootBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mFootProgressBar.setVisibility(View.VISIBLE);// 将进度条可见
				mFootBtn.setVisibility(View.GONE);// 按钮不可见

				if (onRefreshLoadingMoreListener != null) {
					onRefreshLoadingMoreListener.onLoadMore();
				}
			}
		});
		mFootProgressBar = (ProgressBar) mFootView.findViewById(R.id.pg);
		listView.addFooterView(mFootView);
	}

	/***
	 * 初始话头部HeadView
	 *
	 * @param context
	 *            上下文
	 * @param time
	 *            上次更新时间
	 */
	public void initHeadView(Context context, String time) {
		mHeadView = LayoutInflater.from(context).inflate(
				R.layout.listview_head, null);
		mArrowImageView = (ImageView) mHeadView
				.findViewById(R.id.head_arrowImageView);
		mArrowImageView.setMinimumWidth(60);

		mHeadProgressBar = (ProgressBar) mHeadView
				.findViewById(R.id.head_progressBar);

		mRefreshTextview = (TextView) mHeadView
				.findViewById(R.id.head_tipsTextView);

		mLastUpdateTextView = (TextView) mHeadView
				.findViewById(R.id.head_lastUpdatedTextView);
		// 显示更新事件
		mLastUpdateTextView.setText("最近更新:" + time);

		measureView(mHeadView);
		// 获取宽和高
		mHeadViewWidth = mHeadView.getMeasuredWidth();
		mHeadViewHeight = mHeadView.getMeasuredHeight();

		listView.addHeaderView(mHeadView, null, false);// 将初始好的ListView
														// add进拖拽ListView
		// 在这里我们要将此headView设置到顶部不显示位置.
		mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);

		initAnimation();// 初始化动画
	}

	/***
	 * 初始化动画
	 */
	private void initAnimation() {
		// 旋转动画
		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());// 匀速
		animation.setDuration(250);
		animation.setFillAfter(true);// 停留在最后状态.
		// 反向旋转动画
		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(250);
		reverseAnimation.setFillAfter(true);
	}

	/***
	 * 作用：测量 headView的宽和高.
	 *
	 * @param child
	 */
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	/***
	 * touch 事件监听
	 */

	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		// 按下
		case MotionEvent.ACTION_DOWN:
			doActionDown(ev);
			break;
		// 移动
		case MotionEvent.ACTION_MOVE:
			doActionMove(ev);
			break;
		// 抬起
		case MotionEvent.ACTION_UP:
			doActionUp(ev);
			break;
		default:
			break;
		}
		/***
		 * 如果是ListView本身的拉动，那么返回true，这样ListView不可以拖动.
		 * 如果不是ListView的拉动，那么调用父类方法，这样就可以上拉执行.
		 */
		if (isScroller) {
			return false;
		} else {
			return true;
		}

	}

	/***
	 * 摁下操作
	 *
	 * 作用：获取摁下是的y坐标
	 *
	 * @param event
	 */
	void doActionDown(MotionEvent event) {
		if (mIsRecord == false && mFirstItemIndex == 0) {
			mStartY = (int) event.getY();

			mIsRecord = true;
		}
	}

	/***
	 * 拖拽移动操作
	 *
	 * @param event
	 */
	void doActionMove(MotionEvent event) {
		mMoveY = (int) event.getY();// 获取实时滑动y坐标
		// 检测是否是一次touch事件.
		if (mIsRecord == false && mFirstItemIndex == 0) {
			mStartY = (int) event.getY();
			mIsRecord = true;
		}
		/***
		 * 如果touch关闭或者正处于Loading状态的话 return.
		 */
		if (mIsRecord == false || mlistViewState == DListViewState.LV_LOADING) {
			return;
		}
		// 向下啦headview移动距离为y移动的一半.（比较友好）
		int offset = (mMoveY - mStartY) / RATIO;

		switch (mlistViewState) {
		// 普通状态
		case LV_NORMAL: {
			// 如果<0，则意味着上滑动.
			if (offset > 0) {
				// 设置headView的padding属性.
				mHeadView.setPadding(0, offset - mHeadViewHeight, 0, 0);
				switchViewState(DListViewState.LV_PULL_REFRESH);// 下拉状态
			}

		}
			break;
		// 下拉状态
		case LV_PULL_REFRESH: {
			listView.setSelection(0);// 时时保持在顶部.
			// 设置headView的padding属性.
			mHeadView.setPadding(0, offset - mHeadViewHeight, 0, 0);
			if (offset < 0) {
				/***
				 * 要明白为什么isScroller = false;
				 */
				isScroller = false;
				switchViewState(DListViewState.LV_NORMAL);// 普通状态
				Log.e("jj", "isScroller=" + isScroller);
			} else if (offset > mHeadViewHeight) {// 如果下拉的offset超过headView的高度则要执行刷新.
				switchViewState(DListViewState.LV_RELEASE_REFRESH);// 更新为可刷新的下拉状态.
			}
		}
			break;
		// 可刷新状态
		case LV_RELEASE_REFRESH: {
			listView.setSelection(0);// 时时保持在顶部
			// 设置headView的padding属性.
			mHeadView.setPadding(0, offset - mHeadViewHeight, 0, 0);
			// 下拉offset>0，但是没有超过headView的高度.那么要goback 原装.
			if (offset >= 0 && offset <= mHeadViewHeight) {
				mBack = true;
				switchViewState(DListViewState.LV_PULL_REFRESH);
			} else if (offset < 0) {
				switchViewState(DListViewState.LV_NORMAL);
			} else {

			}
		}
			break;
		default:
			return;
		}
		;
	}

	/***
	 * 手势抬起操作
	 *
	 * @param event
	 */
	public void doActionUp(MotionEvent event) {
		mIsRecord = false;// 此时的touch事件完毕，要关闭。
		isScroller = true;// ListView可以Scrooler滑动.
		mBack = false;
		// 如果下拉状态处于loading状态.
		if (mlistViewState == DListViewState.LV_LOADING) {
			return;
		}
		// 处理相应状态.
		switch (mlistViewState) {
		// 普通状态
		case LV_NORMAL:

			break;
		// 下拉状态
		case LV_PULL_REFRESH:
			mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
			switchViewState(mlistViewState.LV_NORMAL);
			break;
		// 刷新状态
		case LV_RELEASE_REFRESH:
			mHeadView.setPadding(0, 0, 0, 0);
			switchViewState(mlistViewState.LV_LOADING);
			onRefresh();// 下拉刷新
			break;
		}

	}

	// 切换headview视图
	private void switchViewState(DListViewState state) {

		switch (state) {
		// 普通状态
		case LV_NORMAL: {
			mArrowImageView.clearAnimation();// 清除动画
			mArrowImageView
					.setImageResource(R.drawable.ic_launcher);//换成需要的图片
		}
			break;
		// 下拉状态
		case LV_PULL_REFRESH: {
			mHeadProgressBar.setVisibility(View.GONE);// 隐藏进度条
			mArrowImageView.setVisibility(View.VISIBLE);// 下拉图标
			mRefreshTextview.setText("下拉可以刷新");
			mArrowImageView.clearAnimation();// 清除动画

			// 是有可刷新状态（LV_RELEASE_REFRESH）转为这个状态才执行，其实就是你下拉后在上拉会执行.
			if (mBack) {
				mBack = false;
				mArrowImageView.clearAnimation();// 清除动画
				mArrowImageView.startAnimation(reverseAnimation);// 启动反转动画
			}
		}
			break;
		// 松开刷新状态
		case LV_RELEASE_REFRESH: {
			mHeadProgressBar.setVisibility(View.GONE);// 隐藏进度条
			mArrowImageView.setVisibility(View.VISIBLE);// 显示下拉图标
			mRefreshTextview.setText("松开获取更多");
			mArrowImageView.clearAnimation();// 清除动画
			mArrowImageView.startAnimation(animation);// 启动动画
		}
			break;
		// 加载状态
		case LV_LOADING: {
			Log.e("!!!!!!!!!!!", "convert to IListViewState.LVS_LOADING");
			mHeadProgressBar.setVisibility(View.VISIBLE);
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.GONE);
			mRefreshTextview.setText("载入中...");
		}
			break;
		default:
			return;
		}
		// 切记不要忘记时时更新状态。
		mlistViewState = state;

	}

	/***
	 * 下拉刷新
	 */
	private void onRefresh() {
		if (onRefreshLoadingMoreListener != null) {
			onRefreshLoadingMoreListener.onRefresh();
		}
	}

	/***
	 * 下拉刷新完毕
	 */
	public void onRefreshComplete() {
		mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);// 回归.
		switchViewState(mlistViewState.LV_NORMAL);//
	}

	public void onLoadMoreComplete() {
		mFootProgressBar.setVisibility(View.GONE);// 将进度条可见
		mFootBtn.setVisibility(View.VISIBLE);// 按钮不可见

	}


	/***
	 * ListView 滑动监听
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 滑到底部,自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& lastVisibleIndex == listView.getCount()) {

			if (onRefreshLoadingMoreListener != null) {
				onRefreshLoadingMoreListener.onLoadMore();
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mFirstItemIndex = firstVisibleItem;

		// 计算最后可见条目的索引
		lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
	}

	/***
	 * 自定义接口
	 */
	public interface OnRefreshLoadingMoreListener {
		/***
		 * // 下拉刷新执行
		 */
		void onRefresh();

		/***
		 * 点击加载更多
		 */
		void onLoadMore();
	}

}