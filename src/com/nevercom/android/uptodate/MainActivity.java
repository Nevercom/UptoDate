package com.nevercom.android.uptodate;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity {

	private WebView webView;
	private String path;
	private MenuItem searchWidgetItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LoadUTD();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		boolean isLight = true;
		int menuItemId = menu
				.add("Search")
				.setIcon(
						isLight ? R.drawable.ic_search_inverse
								: R.drawable.ic_search)
				.setActionView(R.layout.collapsible_edittext)
				.setShowAsActionFlags(
						MenuItem.SHOW_AS_ACTION_ALWAYS
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)
				.getItemId();
		
		
		


		
		AutoCompleteTextView searchBar = (AutoCompleteTextView) menu.findItem(menuItemId)
		.getActionView().findViewById(R.id.etSearch);		
		// Get the string array
		String[] countries = getResources().getStringArray(R.array.drugs_array);
		// Create the adapter and set it to the AutoCompleteTextView 
		ArrayAdapter<String> adapter = 
		        new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
		searchBar.setAdapter(adapter);
		
//		EditText searchBar = (EditText) menu.findItem(menuItemId)
//				.getActionView().findViewById(R.id.etSearch);
		searchWidgetItem = menu.findItem(menuItemId);
		searchBar
				.setOnEditorActionListener(new EditText.OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH) {
							// Perform a Search
							performSearch(v.getText().toString(), 3);
							// Hide the Soft Keyboard
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
							// Collapse the ActionView (Search Bar)
							searchWidgetItem.collapseActionView();
							v.setText("");
							return true;
						}
						return false;
					}
				});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_about:
			final AlertDialog aboutDialog = new AlertDialog.Builder(
					MainActivity.this)
					.setTitle(getString(R.string.about_up_to_date))
					.setMessage(getString(R.string.about_message))
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setNeutralButton("OK",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int button) {
									dialog.cancel();
								}
							})

					.create();
			aboutDialog.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onSearchRequested() {
		searchWidgetItem.expandActionView();
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO: onBackPressed() implementation can be used also, but in android
		// 2.0+
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (webView.canGoBack()) {
				webView.goBack();
			} else {
				moveTaskToBack(true);
				// finish();
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void LoadUTD() {
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		path = Environment.getExternalStorageDirectory()
				+ getString(R.string.data_path);
		File htmlFile = new File(path);
		if (htmlFile.exists()) {
			webView.loadUrl("file://" + path + "/UpToDate.htm");
		} else {
			webView.loadData("Not Found", "text/html", null);
		}

	}

	protected void performSearch(String searchTerm, int menuID) {
		webView.loadUrl("file://" + path + "/contents/search.htm?search="
				+ searchTerm + "&menu=" + menuID);
	}
}
