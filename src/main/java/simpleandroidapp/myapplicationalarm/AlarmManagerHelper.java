

package simpleandroidapp.myapplicationalarm;
/**
 * Created by Jelord Rey Gulle on 10/9/2015.
 */

import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmManagerHelper extends BroadcastReceiver
{

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String HOUR = "hour";
    public static final String MINUTE = "minute";
    public static final String NUM_NEEDED = "numNeeded";
    public static final String TONE = "tone";


    @Override
    public void onReceive(Context context, Intent intent)
    {
        setAlarms(context);
    }


    public static void setAlarms(Context context)
    {

        cancelAlarms(context);


        List<Alarm>alarms = DBHelper.getInstance(context).getAllAlarms();
        Log.d("AlarmManager", "alarms empty: " + alarms.isEmpty());

        if(!alarms.isEmpty())
        {

            for(Alarm alarm : alarms)
            {
                if(alarm.enabled)
                {
                    PendingIntent pIntent = createPendingIntent(context, alarm);

                   Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, alarm.hour);
                    cal.set(Calendar.MINUTE, alarm.minute);
                    cal.set(Calendar.SECOND, 00);


                    final int curHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                    final int curMin = Calendar.getInstance().get(Calendar.MINUTE);
                    final int curDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);


                    int dayCheck = curDay;

                    for (int i = 0; i < 8; i++)
                    {

                        if(alarm.getDay(dayCheck-1))
                        {

                            if((dayCheck == curDay) && (alarm.hour >= curHour)
                                    && !(alarm.hour == curHour && alarm.minute <= curMin))
                            {
                                cal.set(Calendar.DAY_OF_WEEK, dayCheck);
                                setAlarm(context, cal, pIntent);
                                Log.d("Se today", cal.getTime().toString());
                                break;
                            }

                            if(i == 7 && ((dayCheck == curDay) && ((alarm.hour < curHour)
                                    || (alarm.hour == curHour && alarm.minute <= curMin))))
                            {
                                cal.set(Calendar.DAY_OF_WEEK, dayCheck);
                                cal.add(Calendar.WEEK_OF_YEAR, 1);
                                setAlarm(context, cal, pIntent);
                                Log.d("Set para next week", cal.getTime().toString());
                                break;
                            }


                            if(dayCheck > curDay)
                            {
                                cal.set(Calendar.DAY_OF_WEEK, dayCheck);
                                setAlarm(context, cal, pIntent);
                                Log.d("Set para next week", cal.getTime().toString());
                                break;
                            }


                            if(dayCheck < curDay)
                            {
                                cal.set(Calendar.DAY_OF_WEEK, dayCheck);
                                cal.add(Calendar.WEEK_OF_YEAR, 1);
                                setAlarm(context, cal, pIntent);
                                Log.d("Set para next week", cal.getTime().toString());
                                break;
                            }
                        }


                        if(dayCheck == Calendar.SATURDAY)
                            dayCheck = Calendar.SUNDAY;
                        else
                            dayCheck++;
                    }
                }
            }
        }
    }


    public static void setAlarm(Context context, Calendar cal, PendingIntent pIntent)
    {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pIntent);
    }


    public static void cancelAlarms(Context context)
    {
        List<Alarm> alarms = DBHelper.getInstance(context).getAllAlarms();

        if(alarms != null)
        {
            for(Alarm alarm : alarms)
            {
                if(alarm.enabled)
                {
                    try
                    {
                        PendingIntent pIntent = createPendingIntent(context, alarm);
                        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        am.cancel(pIntent);
                    }
                    catch(Exception e)
                    {
                        Log.e("Cancel Alarms", "Found error ", e);
                    }
                }
            }
        }
    }


    private static PendingIntent createPendingIntent(Context context, Alarm alarm)
    {
        Intent intent = new Intent(context, MathMeAwakeAlarmScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ID, alarm.id);
        intent.putExtra(NAME, alarm.name);
        intent.putExtra(HOUR, alarm.hour);
        intent.putExtra(MINUTE, alarm.minute);
        intent.putExtra(NUM_NEEDED, alarm.numCorrectNeeded);
        intent.putExtra(TONE, alarm.tone.toString());

        return PendingIntent.getActivity(context, (int)alarm.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
