package simpleandroidapp.myapplicationalarm;
/**
 * Created by Jelord Rey Gulle on 10/9/2015.
 */
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AlarmListAdapter extends BaseAdapter
{


    private Context mContext;
    private List<Alarm> mAlarms;

    public AlarmListAdapter(Context context, List<Alarm> alarms)
    {
        mContext = context;
        mAlarms = alarms;
    }


    public void setAlarms(List<Alarm> alarms)
    {
        mAlarms = alarms;
    }


    @Override
    public int getCount()
    {
        if(mAlarms != null)
            return mAlarms.size();
        return 0;
    }


    @Override
    public Object getItem(int index)
    {
        if(mAlarms != null)
            return mAlarms.get(index);
        return null;
    }


    @Override
    public long getItemId(int index)
    {
        if(mAlarms != null)
            return mAlarms.get(index).id;
        return 0;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent)
    {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.alarm_list_row, parent, false);
        }


        Alarm alarm = (Alarm) getItem(position);
        TextView alarmTime = (TextView)view.findViewById(R.id.alarmTime);


        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, alarm.hour);
        cal.set(Calendar.MINUTE, alarm.minute);
        String time = DateUtils.formatDateTime(mContext, cal.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME);
        alarmTime.setText(time);

        TextView alarmName = (TextView)view.findViewById(R.id.alarmName);
        alarmName.setText(alarm.name);


        TextView days[] = new TextView[7];

        days[0] = (TextView)view.findViewById(R.id.sunday);
        days[1] = (TextView)view.findViewById(R.id.monday);
        days[2] = (TextView) view.findViewById(R.id.tuesday);
        days[3] = (TextView)view.findViewById(R.id.wednesday);
        days[4] = (TextView)view.findViewById(R.id.thursday);
        days[5] = (TextView)view.findViewById(R.id.friday);
        days[6] = (TextView)view.findViewById(R.id.saturday);

        setDayColor(days, alarm);

        ToggleButton alarmToggle = (ToggleButton)view.findViewById(R.id.alarmToggle);
        alarmToggle.setChecked(alarm.enabled);

        alarmToggle.setTag(Long.valueOf(alarm.id));


        alarmToggle.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Log.d("onCheckChanged", "we are here");
                Log.d("onCheckChange", String.valueOf(((Long)buttonView.getTag()).longValue()));
                ((AlarmListActivity)mContext).setAlarmEnabled(((Long)buttonView.getTag()), isChecked);
            }

        });


        view.setTag(Long.valueOf(alarm.id));


        view.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                Log.d("tag", view.getTag().toString());
                
                ((AlarmListActivity)mContext).startEditAlarmActivity(((Long)view.getTag()).longValue());
            }

        });

        return view;
    }


    private void setDayColor(TextView days[], Alarm alarm)
    {
        for(int i = 0; i < days.length; i++)
        {
            if(alarm.getDay(i))
                days[i].setTextColor(mContext.getResources().getColor(R.color.green));
            else
                days[i].setTextColor(mContext.getResources().getColor(R.color.gray));
        }
    }
}

