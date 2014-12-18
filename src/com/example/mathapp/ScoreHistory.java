package com.example.mathapp;

import android.app.Activity;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ScoreHistory extends Activity
{

	private DatabaseHelper db;
	private SQLiteCursor constantsCursor;
	private SimpleCursorAdapter adapter;
	private ListView scoreListView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score_history);

		db = DatabaseHelper.getInstance(getApplicationContext());

		constantsCursor = (SQLiteCursor) db.getReadableDatabase().rawQuery("SELECT _id, "
				+ DatabaseHelper.DATE
				+ ", "
				+ DatabaseHelper.NUMBER_CORRECT
				+ ", "
				+ DatabaseHelper.NUMBER_INCORRECT
				+ " FROM "
				+ DatabaseHelper.TABLE + " ORDER BY " + DatabaseHelper.DATE, null);

		adapter = new SimpleCursorAdapter(this, R.layout.row, constantsCursor, new String[] {
				DatabaseHelper.DATE, DatabaseHelper.NUMBER_CORRECT,
				DatabaseHelper.NUMBER_INCORRECT }, new int[] { R.id.date,
				R.id.numberCorrect, R.id.numberIncorrect }, 0);

		scoreListView = (ListView) findViewById(R.id.scoreList);
		scoreListView.setAdapter(adapter);
		scoreListView.setOnCreateContextMenuListener(this);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.scoreList)
		{
			menu.add("Remove Recorded Score");
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		db.deleteRow(info.id);
		constantsCursor.requery();
		adapter.notifyDataSetChanged();
		return super.onContextItemSelected(item);
	}
}
