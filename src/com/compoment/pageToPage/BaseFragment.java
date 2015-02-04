package com.compoment.pageToPage;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment{
	private final String TAG = BaseFragment.class.getSimpleName();
	

	public static class FragmentResult{
		public int requestCode=0;
		public int resultCode=0;
		public Intent data=null;
	}
	
	public static int REQUEST_CODE_INVALID=-9000;
	public static int RESULT_CODE_INVALID=-9000;
	

	protected View fragmentView = null;
	private FragmentResult fragmentResult=null;

	public void onResult(int requestCode, int resultCode, Intent data) {
		
	}


	abstract public View onBaseFragmentCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		if (container == null) {
			// Currently in a layout without a container, so no
			// reason to create our view.
			return null;
		}

		if (fragmentView == null) {
			fragmentView = onBaseFragmentCreateView(inflater, container,
					savedInstanceState);
		} else {
			ViewGroup parentView = (ViewGroup) fragmentView.getParent();
			if (parentView != null) {
				parentView.removeAllViewsInLayout();
			}
		}

		return fragmentView;
	}

	@Override
	public void onResume() {
		super.onResume();

		if (null!=fragmentResult) {
			onResult(fragmentResult.requestCode, fragmentResult.resultCode, fragmentResult.data);
		}
	}


	public void clean() {
		fragmentView = null;
	}



	public boolean onBackKeyUp(){
		return false;
	}
	


}
