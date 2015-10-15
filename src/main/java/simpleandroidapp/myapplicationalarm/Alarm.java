package simpleandroidapp.myapplicationalarm;
import android.net.Uri;
/**
 * Created by Jelord Rey Gulle on 10/9/2015. Main activity ni mao ni nag set sa mga functions connected sa activity like pag create og
 * alarmn
 */
public class Alarm {
    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;


    public String name;
    public long id = -1;
    public int hour;
    public int minute;
    public int numCorrectNeeded;
    public boolean enabled;
    public Uri tone;

    private boolean days[];


    public Alarm()
    {
        days = new boolean[7];
    }

    public void setDay(int day, boolean isEnabled)
    {
        days[day] = isEnabled;
    }


    public boolean getDay(int day)
    {
        return days[day];
    }
}
