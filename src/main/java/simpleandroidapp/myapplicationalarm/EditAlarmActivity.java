package simpleandroidapp.myapplicationalarm;
/**
 * Created by Jelord Rey Gulle on 10/9/2015. Function para pag edit sa alarm nga nabuhat na
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class EditAlarmActivity extends Activity implements OnClickListener
{

    private Alarm alarm;

    private Context mContext;


    private EditText editName;
    private TimePicker timePicker;
    private CheckBox chkSun, chkMon, chkTue, chkWed, chkThu, chkFri, chkSat;
    private Button btnDelete, btnCancel, btnSave;
    private TextView txtToneSelect;
    private NumberPicker numPicker;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);
        mContext = getApplicationContext();


        editName = (EditText) findViewById(R.id.edit_alarm_name);
        timePicker = (TimePicker) findViewById(R.id.edit_alarm_time_picker);
        chkSun = (CheckBox) findViewById(R.id.edit_alarm_repeat_sun);
        chkMon = (CheckBox) findViewById(R.id.edit_alarm_repeat_mon);
        chkTue = (CheckBox) findViewById(R.id.edit_alarm_repeat_tue);
        chkWed = (CheckBox) findViewById(R.id.edit_alarm_repeat_wed);
        chkThu = (CheckBox) findViewById(R.id.edit_alarm_repeat_thu);
        chkFri = (CheckBox) findViewById(R.id.edit_alarm_repeat_fri);
        chkSat = (CheckBox) findViewById(R.id.edit_alarm_repeat_sat);

        btnDelete = (Button) findViewById(R.id.edit_alarm_delete);
        btnCancel = (Button) findViewById(R.id.edit_alarm_cancel);
        btnSave = (Button) findViewById(R.id.edit_alarm_save);
        txtToneSelect = (TextView) findViewById(R.id.edit_alarm_tone_select);

        btnDelete.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        numPicker = (NumberPicker) findViewById(R.id.edit_alarm_num_picker);
        numPicker.setMinValue(1);
        numPicker.setMaxValue(12);
        numPicker.setValue(1);


        long id = getIntent().getExtras().getLong("id");


        if(id == -1)
        {
            alarm = new Alarm();
        }
        else
        {

            alarm = DBHelper.getInstance(this).getAlarm(id);


            editName.setText(alarm.name);
            timePicker.setCurrentHour(alarm.hour);
            timePicker.setCurrentMinute(alarm.minute);
            chkSun.setChecked(alarm.getDay(0));
            chkMon.setChecked(alarm.getDay(1));
            chkTue.setChecked(alarm.getDay(2));
            chkWed.setChecked(alarm.getDay(3));
            chkThu.setChecked(alarm.getDay(4));
            chkFri.setChecked(alarm.getDay(5));
            chkSat.setChecked(alarm.getDay(6));
            numPicker.setValue(alarm.numCorrectNeeded);
            txtToneSelect.setText(RingtoneManager.getRingtone(this, alarm.tone).getTitle(this));

        }

        txtToneSelect.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                view.setBackgroundColor(getResources().getColor(R.color.lt_blue));;
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            switch(requestCode)
            {

                case 1:
                {
                    alarm.tone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    txtToneSelect.setText(RingtoneManager.getRingtone(this, alarm.tone).getTitle(this));

                    break;
                }
                default:break;
            }
        }
    }


    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {


            case R.id.edit_alarm_save:
            {
                updateAlarm();
                AlarmManagerHelper.cancelAlarms(this);



                if(alarm.id<0)
                {
                    DBHelper.getInstance(this).createAlarm(alarm);
                }
                else
                    DBHelper.getInstance(this).updateAlarm(alarm);

                AlarmManagerHelper.setAlarms(this);
                setResult(RESULT_OK);
                finish();
            }
            break;



            case R.id.edit_alarm_delete:
            {
                deleteAlarm();
            }
            break;

            case R.id.edit_alarm_cancel:
                finish();
                break;
        }

    }



    private void updateAlarm()
    {
        alarm.name=editName.getText().toString();
        alarm.hour=timePicker.getCurrentHour();
        alarm.minute=timePicker.getCurrentMinute();
        alarm.setDay(0, chkSun.isChecked());
        alarm.setDay(1, chkMon.isChecked());
        alarm.setDay(2, chkTue.isChecked());
        alarm.setDay(3, chkWed.isChecked());
        alarm.setDay(4, chkThu.isChecked());
        alarm.setDay(5, chkFri.isChecked());
        alarm.setDay(6, chkSat.isChecked());
        alarm.numCorrectNeeded = numPicker.getValue();
    }



    private void deleteAlarm()
    {

        final long alarmId = alarm.id;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete this alarm?");
        builder.setMessage("Are you sure you want to delete this alarm that you've set?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", null);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int choice)
            {
                AlarmManagerHelper.cancelAlarms(getApplicationContext());
                DBHelper.getInstance(mContext).deleteAlarm(alarmId);
                Log.d("Edit ALARM", "ALARM Deleted");
                AlarmManagerHelper.setAlarms(getApplicationContext());
                setResult(RESULT_OK);
                finish();
            }
        }).show();
    }



    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save changes?");
        builder.setMessage("Would you like to save changes to this current alarm?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int choice)
            {
                finish();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int choice)
            {
                updateAlarm();
                AlarmManagerHelper.cancelAlarms(mContext);


                if(alarm.id<0)
                {
                    DBHelper.getInstance(mContext).createAlarm(alarm);
                }
                else
                    DBHelper.getInstance(mContext).updateAlarm(alarm);

                AlarmManagerHelper.setAlarms(mContext);
                setResult(RESULT_OK);
                finish();
            }
        }).show();

    }



}
