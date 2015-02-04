package com.compoment.pageToPage;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;







import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 *
 * 
 * 
 * ��fragment���л�ҳ�� 
 *   activity = (BaseFragmentActivity) getActivity();
					activity.popFragment();
  
  
  	BaseFragmentActivity activity = (BaseFragmentActivity) getActivity();
									Fragment fragment = new OrderTypelist();
									if(fragment!=null && activity!=null)
									activity.popToFragment(fragment);

  	Fragment fragment = new  OrderDetail();
    BaseFragmentActivity activity =(BaseFragmentActivity) getActivity();
    if (activity != null)
    activity.pushFragment(fragment);


public class ActivityContactGroupAddContact extends BaseFragmentActivity{
	@Override
	public InitBean onBaseFragmentActivityCreate() {
		// TODO Auto-generated method stub
		InitBean initBean=new InitBean();
		initBean.modelNames=new String[] { "model1" };
		initBean.modelFirstFragment=new GroupAddContactFragment();
	
		initBean.containViewLayout=R.layout.layout_group_addgroup;
		initBean.fragmentViewLayout=R.id.contentLayout;
		return initBean;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

	}
}
 
 * page_to_page.xml�ļ�
  <?xml version="1.0" encoding="utf-8"?>
    <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    >
    
   <FrameLayout
        android:id="@+id/contentLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
   >
   </FrameLayout>
</RelativeLayout>
 * 
 * 
 * 
 * 
 * 
 * */


public abstract class BaseFragmentActivity extends FragmentActivity {

	private String currentModuleName = null;
	private Fragment currFragment = null;
	private Map<String, Stack<Fragment>> moduleStack = new HashMap<String, Stack<Fragment>>();/* ��ģ���Fragmentջ��Ϣ */

	int fragmentViewLayout;
	/**
	 * String[] modelNames = new String[] { "model1" };
	 * */
	abstract public InitBean onBaseFragmentActivityCreate();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InitBean initBean=onBaseFragmentActivityCreate();
		
		this.setContentView(initBean.containViewLayout);
		fragmentViewLayout=initBean.fragmentViewLayout;
		String[] modelNames = initBean.modelNames;//new String[] { "model1" };
      
		Stack<Fragment> stack = null;
		for (int i = 0; i < modelNames.length; i++) {
			stack = new Stack<Fragment>();
			moduleStack.put(modelNames[i], stack);
		}

		currentModuleName=modelNames[0];
	
		turnModule(currentModuleName);
		
		pushFragment(initBean.modelFirstFragment);
		
		
	}

	@Override
	protected void onNewIntent(Intent intent) {
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (null != currFragment) {
			currFragment.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	/**
	 * �˳�Activity
	 */
	public void exitActivity() {
		clean();
		finish();
	}

	/**
	 * �����Դ
	 */
	private void clean() {
		// FragmentManager���Ƴ�   currFragment
		if (currFragment != null) {
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.remove(currFragment);
			ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
			ft.commit();
		}

		// ���stack��  BaseFragment  clean()   ������stack����������;
		Stack<Fragment> stack = null;
		Set<String> keySet = moduleStack.keySet();
		Iterator<String> it = keySet.iterator();

		while (it.hasNext()) {
			stack = moduleStack.get(it.next());
			if (null != stack) {

				cleanFragmentStack(stack);

				stack.clear();
			}
			it.remove();
		}

	}

	/**
	 *  ���stack�� BaseFragment clean()
	 * 
	 * 
	 */
	private void cleanFragmentStack(Stack<Fragment> stack) {
		// TODO Auto-generated method stub
		for (int i = 0; i < stack.size(); i++) {
			BaseFragment fragment = (BaseFragment) stack.pop();
			fragment.clean();
		}
	}

	
	public  void turnModule(String moduleName) {
		turnModule( moduleName,null); 
	}
	
	/**
	 * �л�ģ��  ȡ��currentModule  ���ǰstack�� BaseFragment.clean()
	 * 
	 * @param index
	 */
	public  void turnModule(String moduleName,BaseFragment modelFirstFragment) {

		currentModuleName = moduleName;
		Stack<Fragment> stack = moduleStack.get(currentModuleName);
		Stack<Fragment> realeaseStack = stack;

		Fragment newFragment = null;

		 if (null == stack) {
		stack = new Stack<Fragment>();
		moduleStack.put(moduleName, stack);
		 }

		if (stack.isEmpty()) {
         
			if(modelFirstFragment!=null)
			stack.push(modelFirstFragment);
		} else {
			newFragment = stack.peek();
		}

		Fragment oldFragment = currFragment;
		currFragment = newFragment;

		// ���stack�� BaseFragment.clean()
		if (null != realeaseStack) {
			cleanFragmentStack(realeaseStack);
		}

	}

	/**
	 * ���أ��Ƴ�Framgment
	 */
	public void popFragment() {
		popFragment( null);
	}


	/**
	 * ���أ��Ƴ�Framgment
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param intent
	 */
	public void popFragment( Intent intent) {
		Stack<Fragment> stack = moduleStack.get(currentModuleName);

		if (null == stack || stack.size() <= 1) {
			exitActivity();
			return;
		}

		Fragment oldFragment = stack.pop();

		currFragment = stack.peek();

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.remove(oldFragment);
		ft.replace(fragmentViewLayout, currFragment);
		ft.show(currFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();

		// �����ڴ�
		((BaseFragment) oldFragment).clean();

	}

	/**
	 * ���ص�ָ����Fragmentҳ��
	 * 
	 * @param fragment
	 */
	public void popToFragment(Fragment fragment) {
		Stack<Fragment> stack = moduleStack.get(currentModuleName);

		if (null == fragment) {
			return;
		}

		// ��ʾ
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(fragmentViewLayout, fragment);
		ft.show(fragment);

		ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
		ft.commit();

		String currentFragmentName = fragment.getClass().getName();
		// ����
		while (!stack.isEmpty()) {
			BaseFragment tempFragment = (BaseFragment) stack.peek();
			if (null != fragment) {
				String tempClassName = tempFragment.getClass().getName();

				if (!tempClassName.equals(currentFragmentName)) {
					tempFragment.clean();

					stack.pop();
				} else {
					break;
				}
			}
		}
	}

	/**
	 * �ڵ�ǰ��ģ����ʷ�в���ָ����Ķ���
	 * 
	 * @param clazz
	 * @return
	 */
	public Fragment findFragment(Class clazz) {
		Stack<Fragment> stack = moduleStack.get(currentModuleName);
		return findFragment(stack, clazz);
	}

	/**
	 * ��Stack�в���ָ����Ķ���
	 * 
	 * @param clazz
	 * @return
	 */
	private Fragment findFragment(Stack stack, Class clazz) {

		Iterator<Fragment> iterator = stack.iterator();
		while (iterator.hasNext()) {
			Fragment tempFragment = iterator.next();

			String tempClassName = tempFragment.getClass().getName();

			if (tempClassName.equals(clazz.getName())) {
				return tempFragment;
			}
		}

		return null;
	}

	/**
	 * �л�Framgment
	 * 
	 * @param fragment
	 */
	public void changeFragment(BaseFragment oldFragment,
			BaseFragment newFragment) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		if (oldFragment != null) {
			ft.hide(oldFragment);

			// ft.remove(oldFragment);
		}

		ft.replace(fragmentViewLayout, newFragment);
		ft.show(newFragment);

		ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
		ft.commit();

	}

	/**
	 * ����Framgment
	 * 
	 * @param fragment
	 */
	public void pushFragment(Fragment fragment) {
		Fragment oldFragment = currFragment;

		Stack<Fragment> stack = moduleStack.get(currentModuleName);
		stack.push(fragment);
		currFragment = fragment;

		changeFragment((BaseFragment) oldFragment,
				(BaseFragment) currFragment);
		
		
	}

	/**
	 * ����Framgment
	 * 
	 * @param fragment
	 */
	public void pushFragment(String moduleName, Fragment fragment) {

		if (!moduleName.equals(currentModuleName)) {
			turnModule(moduleName);
		}

		Fragment oldFragment = currFragment;

		Stack<Fragment> stack = moduleStack.get(currentModuleName);
		stack.push(fragment);
		currFragment = fragment;
		
		changeFragment((BaseFragment) oldFragment,
				(BaseFragment) currFragment);

	}
	
	

	

	/* �Ƿ����ڴ��?�ؼ���Ϣ */
	private boolean handleReturnKey = false;
	/* ���һ�ε�����ؼ��ʱ�� */
	private long lastTouchResutnKeyTime = 0;

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long currentTime = SystemClock.currentThreadTimeMillis();

			if (currentTime - lastTouchResutnKeyTime < 3 * 1000
					&& handleReturnKey) {

				lastTouchResutnKeyTime = currentTime;

			} else {
				handleReturnKey = true;
				Stack<Fragment> stack = moduleStack.get(currentModuleName);

				if (null == stack) {
					exitActivity();
				} else {
					if (null != currFragment) {
						boolean result = ((BaseFragment) currFragment)
								.onBackKeyUp();

						if (!result) {
							popFragment();
						}
						if (stack.size() == 0) {
							exitActivity();
						}
					}
				}
				handleReturnKey = false;
			}
		}

		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
	
	
	/**
	 * String[] modelNames = new String[] { "model1" };
	 * */
	public   class InitBean
	{
		public String[] modelNames;
		public BaseFragment modelFirstFragment;
		/**R.layout.layout_group*/
		public int containViewLayout;
		public int fragmentViewLayout;
	}

}
