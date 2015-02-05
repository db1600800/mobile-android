package com.compoment.dateselect;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.android_demonstrate_abstractcode.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;



/**
 * 开始&结束 日期(点击弹出日期选择器)
 * 
 */
public class OnDateSelectClickListener implements OnClickListener {

	TextView txttime;
	Activity context;
	WheelMain wheelMain;
	SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy年MM月dd日");
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	TextView startDateTextView ;
	TextView endDateTextView;
	public OnDateSelectClickListener(Activity context,TextView startDateTextView ,TextView endDateTextView)
	{
		this.context= context;
		this.startDateTextView=startDateTextView;
		this.endDateTextView=endDateTextView;
	}
	
	public OnDateSelectClickListener(Activity context,TextView startDateTextView )
	{
		this.context= context;
		this.startDateTextView=startDateTextView;
	
	}

	@Override
	public void onClick(final View v) {

	
		txttime = (TextView) v;
		

		LayoutInflater inflater = LayoutInflater.from(context);
		final View timepickerview = inflater.inflate(
				R.layout.ipo_dialog_date_picker, null);
		ScreenInfo screenInfo = new ScreenInfo(context);
		wheelMain = new WheelMain(timepickerview);// 设置日期控件
		wheelMain.screenheight = screenInfo.getHeight();
		String time = txttime.getText().toString();
		try {
			time = dateFormat.format(sdfYMD.parse(time));// 日期格式转换
															// yyyy年MM月dd日
															// from
															// yyyy-MM-dd
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
			try {
				calendar.setTime(dateFormat.parse(time));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		wheelMain.initDateTimePicker(year, month, day);
		new AlertDialog.Builder(context)
				.setTitle("选择时间")
				.setView(timepickerview)
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									if (startDateTextView!=null && endDateTextView!=null && v.getId() == startDateTextView.getId()) {

										Date endDate = sdfYMD
												.parse(endDateTextView
														.getText()
														.toString());
										int compareTo = dateFormat.parse(
												wheelMain.getTime())
												.compareTo(endDate);

										if (compareTo < 0 || compareTo == 0) {// 开始时间小于结束时间
											txttime.setText(sdfYMD.format(dateFormat
													.parse(wheelMain
															.getTime())));// 开始日期
										} else {// 开始时间大于结束时间
											Toast.makeText(context,
													"开始时间不能大于结束时间.",
													Toast.LENGTH_SHORT)
													.show();
										}
									} else if(startDateTextView!=null && endDateTextView!=null && v.getId() == endDateTextView.getId()){
										Date startDate = sdfYMD
												.parse(startDateTextView
														.getText()
														.toString());
										int compareTo = dateFormat.parse(
												wheelMain.getTime())
												.compareTo(startDate);

										if (compareTo < 0) {// 开始时间小于结束时间
											Toast.makeText(context,
													"结束时间不能小于开始时间.",
													Toast.LENGTH_SHORT)
													.show();

										} else {// 开始时间大于结束时间
											txttime.setText(sdfYMD.format(dateFormat
													.parse(wheelMain
															.getTime())));// 开始日期
										}
									}else if (startDateTextView!=null && endDateTextView==null && v.getId() == startDateTextView.getId()) {

									
								
											txttime.setText(sdfYMD.format(dateFormat
													.parse(wheelMain
															.getTime())));// 开始日期
										
									}

								} catch (ParseException e) {
									e.printStackTrace();
								}
							}
						})
				.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).show();
	}

}