package com.acheprovas.activitys.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acheprovas.R;

public class ArrayAdapterProvas extends ArrayAdapter<String> {
	
	private int resource;

	public ArrayAdapterProvas(Context context, int textViewResourceId, ArrayList<String> array) {
		super(context, textViewResourceId, array);
		this.resource = textViewResourceId;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		
		LinearLayout vView;
		
		// Infla a view se a mesma vier como nula
		if (view == null) {
			vView = new LinearLayout(getContext());

			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(resource, vView, true);

		} else {
			vView = (LinearLayout) view;
		}
		
		TextView tv1 = (TextView) vView.findViewById(R.id.tvNome);
//		tv1.setText(getItem(pos).get(Constants.TAG_NOME));
		tv1.setText(getItem(pos));
		
		return vView;

	}
}
