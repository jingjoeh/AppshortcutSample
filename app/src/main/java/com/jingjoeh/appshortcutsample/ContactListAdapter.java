package com.jingjoeh.appshortcutsample;

import android.content.pm.ShortcutInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jingjoeh on 9/5/2017 AD.
 */

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {

	private List<ShortcutInfo> mContacts;

	private OnItemCLickListener mOnItemCLickListener;

	public ContactListAdapter(List<ShortcutInfo> contacts) {
		mContacts = contacts;
	}


	@Override
	public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
		return new ContactViewHolder(v, mOnItemCLickListener);
	}

	@Override
	public void onBindViewHolder(ContactViewHolder holder, int position) {
		holder.bind(mContacts.get(position));
	}

	public void setOnItemCLickListener(OnItemCLickListener onItemCLickListener) {
		mOnItemCLickListener = onItemCLickListener;
	}

	@Override
	public int getItemCount() {
		return mContacts == null ? 0 : mContacts.size();
	}

	class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private TextView mNameTxt;
		private TextView mValTxt;
		private Button remove, disable;
		private OnItemCLickListener onItemCLickListener;


		public ContactViewHolder(View itemView, OnItemCLickListener onItemCLickListener) {
			super(itemView);
			this.onItemCLickListener = onItemCLickListener;
			mNameTxt = itemView.findViewById(R.id.contact_name_textview);
			mValTxt = itemView.findViewById(R.id.contact_val_textview);
			remove = itemView.findViewById(R.id.remove_button);
			disable = itemView.findViewById(R.id.disable_button);

			remove.setOnClickListener(this);
			disable.setOnClickListener(this);
		}

		public void bind(ShortcutInfo shortcutInfo) {
			mNameTxt.setText(shortcutInfo.getShortLabel());
			mValTxt.setText(shortcutInfo.getLongLabel());
		}

		@Override
		public void onClick(View view) {
			onItemCLickListener.onClick(view, getAdapterPosition());
		}
	}

	public interface OnItemCLickListener {
		void onClick(View v, int position);
	}
}
