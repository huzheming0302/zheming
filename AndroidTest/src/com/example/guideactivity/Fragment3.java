package com.example.guideactivity;

import com.example.androidtest.LoginActivity;
import com.example.androidtest.MainActivity;
import com.example.androidtest.QueryActivity;
import com.example.androidtest.R;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class Fragment3 extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.fragment3, container, false);
		Button login = (Button)view.findViewById(R.id.login);
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
			}
		});
		return view;
	}

}
