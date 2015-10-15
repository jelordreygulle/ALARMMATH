
package simpleandroidapp.myapplicationalarm;
/**
 * Created by Jelord Rey Gulle on 10/9/2015. function ni para pag prompt sa alarm details
 * pag na set na nmo ang alarm tas ni andar na siya kato emo mga makita og ma click was made
 * possible by this function
 */

import java.util.Calendar;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MathMeAwakeAlarmScreen extends Activity {

    private MediaPlayer mPlayer;


    TextView name;
    TextView time;
    TextView prompt;
    TextView input;
    TextView status;
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    Button button7;
    Button button8;
    Button button9;
    Button buttonEnter;
    Button buttonClear;

    int hour, minute;


    MathProblem problem;

    int numNeeded, numCorrect;

    Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_me_awake_math);


        name = (TextView)findViewById(R.id.alarm_screen_name_header);
        prompt = (TextView)findViewById(R.id.alarm_screen_prompt);
        time = (TextView)findViewById(R.id.alarm_screen_time);
        input = (TextView)findViewById(R.id.alarm_screen_input);
        status = (TextView)findViewById(R.id.alarm_screen_status);
        button0 = (Button)findViewById(R.id.alarm_screen_btn_0);
        button1 = (Button)findViewById(R.id.alarm_screen_btn_1);
        button2 = (Button)findViewById(R.id.alarm_screen_btn_2);
        button3 = (Button)findViewById(R.id.alarm_screen_btn_3);
        button4 = (Button)findViewById(R.id.alarm_screen_btn_4);
        button5 = (Button)findViewById(R.id.alarm_screen_btn_5);
        button6 = (Button)findViewById(R.id.alarm_screen_btn_6);
        button7 = (Button)findViewById(R.id.alarm_screen_btn_7);
        button8 = (Button)findViewById(R.id.alarm_screen_btn_8);
        button9 = (Button)findViewById(R.id.alarm_screen_btn_9);
        buttonEnter = (Button)findViewById(R.id.alarm_screen_btn_enter);
        buttonClear = (Button)findViewById(R.id.alarm_screen_btn_clear);

        numCorrect = 0;
        cal = Calendar.getInstance();


        numNeeded = getIntent().getIntExtra(AlarmManagerHelper.NUM_NEEDED, 1);
        name.setText(getIntent().getStringExtra(AlarmManagerHelper.NAME));
        String tone = getIntent().getStringExtra(AlarmManagerHelper.TONE);
        status.setText("Answer " + String.valueOf(numNeeded - numCorrect) + " more to disable alarm");

        hour = getIntent().getIntExtra(AlarmManagerHelper.HOUR, 0);
        minute = getIntent().getIntExtra(AlarmManagerHelper.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        time.setText(DateUtils.formatDateTime(this, cal.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME));


        input.setText("");


        problem = new MathProblem();
        prompt.setText(problem.toString());


        mPlayer = new MediaPlayer();
        try
        {
            if (tone != null && !tone.equals(""))
            {
                Uri toneUri = Uri.parse(tone);
                if(toneUri != null)
                {
                    mPlayer.setDataSource(this, toneUri);
                    mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                    mPlayer.setLooping(true);
                    mPlayer.prepare();
                    mPlayer.start();
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.math_me_awake_math, menu);
        return true;
    }


    public void keypadInput(View v)
    {

        switch(v.getId())
        {
            case (R.id.alarm_screen_btn_0):
                input.append("0");
                break;
            case (R.id.alarm_screen_btn_1):
                input.append("1");
                break;
            case (R.id.alarm_screen_btn_2):
                input.append("2");
                break;
            case (R.id.alarm_screen_btn_3):
                input.append("3");
                break;
            case (R.id.alarm_screen_btn_4):
                input.append("4");
                break;
            case (R.id.alarm_screen_btn_5):
                input.append("5");
                break;
            case (R.id.alarm_screen_btn_6):
                input.append("6");
                break;
            case (R.id.alarm_screen_btn_7):
                input.append("7");
                break;
            case (R.id.alarm_screen_btn_8):
                input.append("8");
                break;
            case (R.id.alarm_screen_btn_9):
                input.append("9");
                break;
            case (R.id.alarm_screen_btn_clear):
                input.setText("");;
                break;
        }
    }


    public void submit(View v)
    {
        try
        {
            if(Integer.parseInt(input.getText().toString()) == problem.getAns())
            {
                Toast.makeText(getBaseContext(), "Correct!", Toast.LENGTH_SHORT).show();
                input.setText("");
                numCorrect++;
                status.setText("Answer " + String.valueOf(numNeeded - numCorrect) + " more to disable alarm ");

                if(numCorrect >= numNeeded)
                {
                    AlarmManagerHelper.setAlarms(this);
                    mPlayer.stop();
                    finish();
                }
                problem = new MathProblem();
                prompt.setText(problem.toString());
            }
            else
            {
                Toast.makeText(getBaseContext(), "Incorrect, answer was " + problem.getAns() + ".", Toast.LENGTH_SHORT).show();
                input.setText("");
                problem = new MathProblem();
                prompt.setText(problem.toString());
            }

        }


        catch(NumberFormatException numEx)
        {
            Toast.makeText(getBaseContext(), "Invalid input", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);


    }

}
