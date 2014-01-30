package com.acheprovas.activitys.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acheprovas.R;
import com.acheprovas.libs.Constants;

public class ArrayAdapterGenerico extends ArrayAdapter<HashMap<String, String>> {
	
	private int resource;
	private Context context;

	public ArrayAdapterGenerico(Context context, int textViewResourceId, ArrayList<HashMap<String, String>> array) {
		super(context, textViewResourceId, array);
		this.resource = textViewResourceId;
		this.context = context;
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
//		TextView tv2 = (TextView) vView.findViewById(R.id.tvDesc);
		
		tv1.setText(getItem(pos).get(Constants.TAG_NOME));
//		tv2.setText(getItem(pos).get(Constants.TAG_DESC));
		
		return vView;

	}
}
