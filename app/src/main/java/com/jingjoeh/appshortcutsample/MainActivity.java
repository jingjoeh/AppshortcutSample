package com.jingjoeh.appshortcutsample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ShortcutInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	public static final int TYPE_PHONE = 1;
	public static final int TYPE_EMAIL = 2;

	private ShortcutsHelper mShortcutsHelper;
	private RecyclerView mRecyclerView;
	private ContactListAdapter mContactListAdapter;
	private List<ShortcutInfo> mShortcutInfoList = new ArrayList<>();

	@Override
	protected void onStart() {
		super.onStart();
		if (getIntent() != null) {
			if (getIntent().getAction().equals("com.jingjoeh.appshortcutsample.action.ADD_NEW_CONTACT"))
				showAddDialog();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mShortcutsHelper = new ShortcutsHelper(this);

		Button addButton = findViewById(R.id.add_button);
		mRecyclerView = findViewById(R.id.recycler);

		initAdapter();
		notifyData();
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showAddDialog();
			}
		});
	}

	void initAdapter() {
		mContactListAdapter = new ContactListAdapter(mShortcutInfoList);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		mRecyclerView.setAdapter(mContactListAdapter);
		mContactListAdapter.setOnItemCLickListener(new ContactListAdapter.OnItemCLickListener() {
			@Override
			public void onClick(View v, int position) {
				switch (v.getId()) {
					case R.id.remove_button:
						mShortcutsHelper.remove(mShortcutInfoList.get(position).getId());
						notifyData();
						break;
					case R.id.disable_button:
						mShortcutsHelper.disable(mShortcutInfoList.get(position).getId());
						notifyData();
						break;
				}
			}
		});
	}


	void showAddDialog() {

		if (!mShortcutsHelper.canAddMoreShortCut()) {
			Toast.makeText(MainActivity.this, "Maximum shortcut remove someone", Toast.LENGTH_SHORT).show();
			return;
		}
		View view = View.inflate(this, R.layout.add_contact, null);
		final RadioButton phoneRadio = view.findViewById(R.id.radio_phone);
		final EditText nameEdit = view.findViewById(R.id.name_edit_text);
		final EditText valEdit = view.findViewById(R.id.val_edit_text);

		new AlertDialog.Builder(this)
				.setTitle("Add Contact")
				.setView(view)
				.setPositiveButton("Add", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						mShortcutsHelper.addDynamic(nameEdit.getText().toString(),
								valEdit.getText().toString(),
								phoneRadio.isChecked() ? TYPE_PHONE : TYPE_EMAIL);
						notifyData();
					}
				})
				.show();
	}

	void notifyData() {
		// update recycler
		mShortcutInfoList.clear();
		mShortcutInfoList.addAll(mShortcutsHelper.getShortcutsInfoList());
		mContactListAdapter.notifyDataSetChanged();
	}
}
