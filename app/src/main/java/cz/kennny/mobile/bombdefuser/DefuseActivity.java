package cz.kennny.mobile.bombdefuser;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DefuseActivity extends AppCompatActivity
{
    /** Stored text for title shown during countdown phase */
    private String titleText;
    /** Code which defuses the bomb */
    private String codeText;
    /** Total amount of seconds */
    private int secondsTotal;
    /** Remaining amount of seconds */
    private int secondsRemaining;
    /** Penalty for submitting bad code (in seconds) */
    private int penaltySeconds;
    /** Is the colon sign shown right now? */
    private boolean tickFlag;
    /** Text to display after defusing */
    private String finishText;

    /** Text entered into output field */
    private String enteredText;

    /** "Scope-fucker" for lambda inherited handler */
    private final DefuseActivity that = this;

    /** Resources for numbers (0-9) */
    private int numberResources[] = {R.drawable.n0, R.drawable.n1, R.drawable.n2, R.drawable.n3,
            R.drawable.n4,R.drawable.n5,R.drawable.n6,R.drawable.n7,R.drawable.n8,R.drawable.n9};

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            // Show/hide the colon
            ImageView iv = (ImageView)findViewById(R.id.digitMiddle);
            if (tickFlag)
            {
                iv.setVisibility(View.VISIBLE);
                // in this phase, we also decrease the amount of seconds
                secondsRemaining--;
            }
            else
                iv.setVisibility(View.INVISIBLE);
            // switch colon flag
            tickFlag = !tickFlag;

            // Sanity checks
            if (secondsRemaining < 0)
                secondsRemaining = 0;
            if (secondsRemaining >= 5999)
                secondsRemaining = 5999;

            // retrieve references to digit ImageView components on canvas
            ImageView digits[] = new ImageView[4];
            digits[0] = (ImageView)findViewById(R.id.digit1);
            digits[1] = (ImageView)findViewById(R.id.digit2);
            digits[2] = (ImageView)findViewById(R.id.digit3);
            digits[3] = (ImageView)findViewById(R.id.digit4);

            // convert to min/sec fields
            int mins = secondsRemaining / 60;
            int secs = secondsRemaining % 60;

            // choose proper digits
            digits[0].setImageResource(numberResources[mins / 10]);
            digits[1].setImageResource(numberResources[mins % 10]);
            digits[2].setImageResource(numberResources[secs / 10]);
            digits[3].setImageResource(numberResources[secs % 10]);

            // if we reached zero, make it BOOOOOOM!!!
            if (secondsRemaining == 0)
            {
                // stop timer stuff
                handler.removeCallbacks(countdownRunnable);
                // invoke new activity
                Intent in = new Intent(that, FailedActivity.class);
                startActivity(in);
            }
        }
    };

    /** runnable for countdown */
    Runnable countdownRunnable = null;

    @Override
    protected void onStop()
    {
        super.onStop();

        Log.i("Defuser", "Countdown stopped");
        if (handler != null)
            handler.removeCallbacks(countdownRunnable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Headless view - no toolbar, no statusbar, just activity contents
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_defuse);

        // retrieve intent
        Intent src = getIntent();
        if (src == null)
            return;

        // retrieve passed fields
        titleText = src.getStringExtra("titletext");
        codeText = src.getStringExtra("codetext");
        finishText = src.getStringExtra("finishtext");

        TextView tv = (TextView)findViewById(R.id.bombTitleText);
        if (tv != null)
            tv.setText(titleText);

        // convert time to seconds
        try
        {
            secondsTotal = Integer.parseInt(src.getStringExtra("timemintext"))*60 + Integer.parseInt(src.getStringExtra("timesectext"));
        }
        catch (NumberFormatException ex)
        {
            secondsTotal = 60;
        }

        // convert penalty to seconds
        try
        {
            penaltySeconds = Integer.parseInt(src.getStringExtra("penaltysecs"));
        }
        catch (NumberFormatException ex)
        {
            penaltySeconds = 0;
        }

        // reset timer
        tickFlag = true;
        secondsRemaining = secondsTotal;

        // create runnable
        countdownRunnable = new Runnable() {
            @Override
            public void run()
            {
                // signalize UI thread handler - needs to be done this way since no thread but the
                // main could change stuff in user interface
                handler.obtainMessage(1).sendToTarget();

                // do not repeat when timer passed
                if (secondsRemaining <= 0)
                    return;

                // repeat
                handler.postDelayed(this, 500);
            }
        };
        // start timer
        handler.postDelayed(countdownRunnable, 500);

        // reset code and stuff
        tv = (TextView)findViewById(R.id.codeTextField);
        if (tv == null)
            return;
        tv.setText("");
        enteredText = "";
    }

    /**
     * Updates text stored in enteredText into UI element
     */
    public void updateEnteredText()
    {
        TextView tv = (TextView)findViewById(R.id.codeTextField);
        if (tv == null)
            return;
        tv.setText(enteredText);

        // make underlay color OK again
        tv = (TextView)findViewById(R.id.codeUnderlay);
        tv.setBackgroundColor(getResources().getColor(R.color.colorCodeOK));
    }

    /**
     * Appends string to output
     * @param s String to be appended
     */
    public void appendEnteredString(String s)
    {
        // do not allow texts longer than 10 characters
        if (enteredText.length() + s.length() > 10)
            return;
        // append and update UI
        enteredText = enteredText + s;
        updateEnteredText();
    }

    public void button0Pressed(View v) { appendEnteredString("0"); }
    public void button1Pressed(View v) { appendEnteredString("1"); }
    public void button2Pressed(View v) { appendEnteredString("2"); }
    public void button3Pressed(View v) { appendEnteredString("3"); }
    public void button4Pressed(View v) { appendEnteredString("4"); }
    public void button5Pressed(View v) { appendEnteredString("5"); }
    public void button6Pressed(View v) { appendEnteredString("6"); }
    public void button7Pressed(View v) { appendEnteredString("7"); }
    public void button8Pressed(View v) { appendEnteredString("8"); }
    public void button9Pressed(View v) { appendEnteredString("9"); }
    public void buttonWPressed(View v) { appendEnteredString("W"); }
    public void buttonBackspacePressed(View v)
    {
        if (enteredText.length() > 0)
            enteredText = enteredText.substring(0, enteredText.length()-1);

        updateEnteredText();
    }
    public void buttonDeletePressed(View v)
    {
        enteredText = "";

        updateEnteredText();
    }

    public void buttonSubmitPressed(View v)
    {
        // if the code matches...
        if (enteredText.equals(codeText))
        {
            // stop timer
            handler.removeCallbacks(countdownRunnable);
            // and move to success activity
            Intent in = new Intent(this, FinishActivity.class);
            in.putExtra("remainingsecs", secondsRemaining);
            in.putExtra("finishtext", finishText);
            startActivity(in);
        }
        else // the code does not match
        {
            // update timer with penalty
            tickFlag = false;
            secondsRemaining -= penaltySeconds;
            if (secondsRemaining < 0)
                secondsRemaining = 0;
            // refresh timer
            handler.obtainMessage(1).sendToTarget();

            // and change background color to "errorneous"
            TextView tv = (TextView)findViewById(R.id.codeUnderlay);
            tv.setBackgroundColor(getResources().getColor(R.color.colorCodeERROR));
        }
    }
}
