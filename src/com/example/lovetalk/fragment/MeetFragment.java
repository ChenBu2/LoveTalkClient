package com.example.lovetalk.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.avos.avoscloud.AVObject;
import com.example.lovetalk.R;
import com.example.lovetalk.adapter.MeetAdapter;
import com.example.lovetalk.enity.MeetingInfo;
import com.example.lovetalk.service.UserService;
import com.example.lovetalk.util.MyAsyncTask;

import java.util.List;

/**
 * Created by lzw on 14-9-17.
 */
public class MeetFragment extends BaseFragment implements
		AdapterView.OnItemClickListener {
	// MeetingInfo meetinfo = new MeetingInfo(context);
	GridView listView;
	MeetAdapter Adapter;
	UpdateReceiver update;
	String updateAction = "com.example.lovetalk.update";


	public class UpdateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.d("lan", "更新");
			String action = intent.getAction();
			if (action.equals(updateAction)) {
				if (Adapter != null) {
					Adapter.notifyDataSetChanged();
				}
			}
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.discover_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		update = new UpdateReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(updateAction);
		context.registerReceiver(update, filter);
		initXListView();
		headerLayout.showTitle(R.string.meet);

	}

	private void initXListView() {
		listView = (GridView) getView().findViewById(R.id.list_meeetinginfo);
		onRefresh();
	}

	private void findMeetPeople() {
		new MyAsyncTask(context, false) {
			List<AVObject> meets;

			@Override
			protected void doInBack() throws Exception {
				meets = UserService.findMeetPeople();
			}

			@Override
			protected void onSucceed() {
				for (AVObject meeting : meets) {
					MeetingInfo.addMeeting(meeting);
				}
				Adapter = new MeetAdapter(context, MeetingInfo.getList());
				listView.setAdapter(Adapter);
			}

		}.execute();
	}

	public void onRefresh() {
		// TODO Auto-generated method stub
		MeetingInfo.Clear();
		findMeetPeople();
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		context.unregisterReceiver(update);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}


}
