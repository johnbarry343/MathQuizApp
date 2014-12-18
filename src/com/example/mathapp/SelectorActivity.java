package com.example.mathapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectorActivity extends Activity implements OnItemClickListener
{
	private String[] operations = { "Addition", "Subtraction",
			"Multiplication", "Division", "Score History", "Preferences" };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selector);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, operations);
		ListView myList = (ListView) findViewById(R.id.listView1);
		myList.setAdapter(adapter);
		myList.setOnItemClickListener(this);

		//Initializes the database if it doesn't already exist
		DatabaseHelper.getInstance(getApplicationContext()).getReadableDatabase();

	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{
		Intent intent = null;

		switch (position)
		{
			case 0:
			{
				intent = new Intent(this, MathActivity.class);
				intent.putExtra("operator", "+");
				break;
			}
			case 1:
			{
				intent = new Intent(this, MathActivity.class);
				intent.putExtra("operator", "-");
				break;
			}
			case 2:
			{
				intent = new Intent(this, MathActivity.class);
				intent.putExtra("operator", "*");
				break;
			}
			case 3:
			{
				intent = new Intent(this, MathActivity.class);
				intent.putExtra("operator", "/");
				break;
			}
			case 4:
			{
				intent = new Intent(this, ScoreHistory.class);
				break;
			}
			case 5:
			{
				intent = new Intent(this, PreferencesActivity.class);
				break;
			}
		}

		startActivity(intent);

	}
	
	public static String getAppNameByPID(Context context, int pid){
	    ActivityManager manager 
	               = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

	    for(RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()){
	        if(processInfo.pid == pid){
	            return processInfo.processName;
	        }
	    }
	    return "";
	}

}
