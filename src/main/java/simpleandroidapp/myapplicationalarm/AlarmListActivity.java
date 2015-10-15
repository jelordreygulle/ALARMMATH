package simpleandroidapp.myapplicationalarm;
/**
 * Created by Jelord Rey Gulle on 10/9/2015. Pra add og list sa mga alarms
 */
    import android.app.ListActivity;
    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ListView;

public class AlarmListActivity extends ListActivity {

    private AlarmListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_alarm_list);
        adapter = new AlarmListAdapter(this, DBHelper.getInstance(this).getAllAlarms());

        LayoutInflater inflater = this.getLayoutInflater();
        View header = inflater.inflate(R.layout.alarm_list_header,null);
        ListView lv = getListView();
        lv.addHeaderView(header);

        setListAdapter(adapter);

        Button addAlarm = (Button)findViewById(R.id.addAlarmBtn);
        addAlarm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startEditAlarmActivity(-1);
            }

        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_OK)
        {
            adapter.setAlarms(DBHelper.getInstance(this).getAllAlarms());
            if(adapter.isEmpty())
            {
                Log.d("ArrayListActivity", "Empty adapter");
                adapter.notifyDataSetInvalidated();
            }
            else
                adapter.notifyDataSetChanged();

        }
    }


    public void setAlarmEnabled(long id, boolean enabled)
    {
        Log.d("setAlarmEnabled", "We are here");
        AlarmManagerHelper.cancelAlarms(this);

        Alarm alarm = DBHelper.getInstance(this).getAlarm(id);
        alarm.enabled = enabled;
        DBHelper.getInstance(this).updateAlarm(alarm);

        AlarmManagerHelper.setAlarms(this);
    }


    public void startEditAlarmActivity(long id)
    {
        Intent intent = new Intent(this, EditAlarmActivity.class);
        intent.putExtra("id", id);
        startActivityForResult(intent, 0);
    }
}



