package com.example.mathapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MathActivity extends Activity implements OnClickListener
{
    private TextView number1Text;
    private TextView number2Text;
    private TextView correctAnswersText;
    private TextView incorrectAnswersText;
    private int number1;
    private int number2;
    private ArrayList<Integer> answers;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button playAgainButton;
    private MyCountDownTimer timer = null;
    private TextView timeLeftText;
    private int correctAnswer;
    private int correctAnswers = 0;
    private int incorrectAnswers = 0;
    private String operator;
    private TextView operatorText;
    private int timeLeft = 0;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);

        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(this);

        playAgainButton = (Button) findViewById(R.id.playAgainButton);
        playAgainButton.setOnClickListener(this);
        playAgainButton.setEnabled(false);
        playAgainButton.setVisibility(View.INVISIBLE);

        number1Text = (TextView) findViewById(R.id.number1);
        number2Text = (TextView) findViewById(R.id.number2);
        correctAnswersText = (TextView) findViewById(R.id.correctAnswersText);
        incorrectAnswersText = (TextView) findViewById(R.id.incorrectAnswersText);
        operatorText = (TextView) findViewById(R.id.operator);
        timeLeftText = (TextView) findViewById(R.id.timeLeft);

        if (savedInstanceState == null)
        {
            if (this.getIntent().hasExtra("operator"))
            {
                operator = this.getIntent().getStringExtra("operator");
                operatorText.setText(operator);
            }
            randomize();
        }
    }

    @Override
    protected void onResume()
    {
        if (timeLeft == 0)
        {
            setupTimer();
        }
        else
        {
            setupTimer(timeLeft);
        }
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        if (timer != null)
        {
            timer.cancel();
            timer = null;
        }
        super.onPause();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.main_menu:
            {
                finish();
                break;
            }
            case R.id.preferences:
            {
                Intent intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        timeLeft = savedInstanceState.getInt("timeLeft");
        timeLeftText.setText(Integer.toString(timeLeft));

        number1 = savedInstanceState.getInt("number1");
        number1Text.setText(Integer.toString(number1));

        operator = savedInstanceState.getString("operator");
        operatorText.setText(operator);

        number2 = savedInstanceState.getInt("number2");
        number2Text.setText(Integer.toString(number2));

        correctAnswer = savedInstanceState.getInt("correctAnswer");

        correctAnswers = savedInstanceState.getInt("correctAnswers");
        correctAnswersText.setText(Integer.toString(correctAnswers));

        incorrectAnswers = savedInstanceState.getInt("incorrectAnswers");
        incorrectAnswersText.setText(Integer.toString(incorrectAnswers));

        answers = savedInstanceState.getIntegerArrayList("answers");
        button1.setText(Integer.toString(answers.get(0)));
        button1.setBackgroundResource(android.R.drawable.btn_default);
        button1.setEnabled(savedInstanceState.getBoolean("isButton1Enabled"));
        button2.setText(Integer.toString(answers.get(1)));
        button2.setEnabled(savedInstanceState.getBoolean("isButton2Enabled"));
        button2.setBackgroundResource(android.R.drawable.btn_default);
        button3.setText(Integer.toString(answers.get(2)));
        button3.setBackgroundResource(android.R.drawable.btn_default);
        button3.setEnabled(savedInstanceState.getBoolean("isButton3Enabled"));

        if (savedInstanceState.getInt("isPlayAgainButtonVisible") == 0)
        {
            playAgainButton.setVisibility(View.VISIBLE);
        }
        else
        {
            playAgainButton.setVisibility(View.INVISIBLE);
        }

        if (savedInstanceState.getBoolean("isPlayAgainButtonEnabled"))
        {
            playAgainButton.setEnabled(true);
            timeLeftText.setVisibility(View.INVISIBLE);
        }
        else
        {
            setupTimer(timeLeft);
            playAgainButton.setEnabled(false);
        }

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putBoolean("isButton1Enabled", button1.isEnabled());
        outState.putBoolean("isButton2Enabled", button2.isEnabled());
        outState.putBoolean("isButton3Enabled", button3.isEnabled());
        outState.putInt("number1", number1);
        outState.putString("operator", operator);
        outState.putInt("number2", number2);
        outState.putInt("correctAnswer", correctAnswer);
        outState.putInt("correctAnswers", correctAnswers);
        outState.putInt("incorrectAnswers", incorrectAnswers);
        outState.putIntegerArrayList("answers", answers);
        outState.putInt("timeLeft", timeLeft);
        outState.putBoolean("isPlayAgainButtonEnabled", playAgainButton.isEnabled());
        if (playAgainButton.isShown())
        {
            outState.putInt("isPlayAgainButtonVisible", View.VISIBLE);
        }
        else
        {
            outState.putInt("isPlayAgainButtonVisible", View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button1:
            {
                buttonClickChecker(button1);
                break;
            }
            case R.id.button2:
            {
                buttonClickChecker(button2);
                break;
            }
            case R.id.button3:
            {
                buttonClickChecker(button3);
                break;
            }
            case R.id.playAgainButton:
            {
                onCreate(null);
                onResume();
                correctAnswers = 0;
                incorrectAnswers = 0;
                break;
            }
        }
        correctAnswersText.setText(Integer.toString(correctAnswers));
        incorrectAnswersText.setText(Integer.toString(incorrectAnswers));
    }

    private void buttonClickChecker(Button btn)
    {
        if (btn.getText().equals(Integer.toString(correctAnswer)))
        {
            correctAnswers++;
            randomize();
        }
        else
        {
            btn.setEnabled(false);
            incorrectAnswers++;
        }
    }

    public void randomize()
    {
        number1 = (int) (Math.random() * 5) + 1;
        number1Text.setText(Integer.toString(number1));

        number2 = (int) (Math.random() * 5) + 1;
        number2Text.setText(Integer.toString(number2));

        answers = new ArrayList<Integer>();

        if (operator.equals("+"))
        {
            answers.add(number1 + number2);
            randomGenerator(2, 10);
        }
        else if (operator.equals("-"))
        {
            answers.add(number1 - number2);
            randomGenerator(0, 4);
        }
        else if (operator.equals("*"))
        {
            answers.add(number1 * number2);
            randomGenerator(2, 25);
        }
        else if (operator.equals("/"))
        {
            answers.add(number1 / number2);
            randomGenerator(1, 5);
        }

        Collections.shuffle(answers);

        button1.setText(Integer.toString(answers.get(0)));
        button1.setBackgroundResource(android.R.drawable.btn_default);
        button1.setEnabled(true);

        button2.setText(Integer.toString(answers.get(1)));
        button2.setBackgroundResource(android.R.drawable.btn_default);
        button2.setEnabled(true);

        button3.setText(Integer.toString(answers.get(2)));
        button3.setBackgroundResource(android.R.drawable.btn_default);
        button3.setEnabled(true);
    }

    public void randomGenerator(int max, int min)
    {
        correctAnswer = answers.get(0);

        int temp;

        do
        {
            temp = (int) (Math.random() * (max - min)) + min;

        }
        while (temp == answers.get(0));
        answers.add(temp);
        do
        {
            temp = (int) (Math.random() * (max - min)) + min;
        }
        while (temp == answers.get(0) || temp == answers.get(1));
        answers.add(temp);
    }

    private void setupTimer(int timeLeft)
    {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean timerEnabled = prefs.getBoolean("checkbox_preference", false);

        if (timerEnabled)
        {
            this.timeLeft = timeLeft;
            timer = new MyCountDownTimer((timeLeft + 1) * 1000, 1000);
            timer.start();
            timeLeftText.setVisibility(View.VISIBLE);
        }
        else
        {
            if (timer != null)
            {
                timer.cancel();
                timer = null;
            }
            timeLeftText.setVisibility(View.INVISIBLE);
        }
    }

    private void setupTimer()
    {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean timerEnabled = prefs.getBoolean("checkbox_preference", false);

        if (timerEnabled)
        {
            timeLeft = Integer.parseInt(prefs.getString("list_preference", "30"));
            timer = new MyCountDownTimer((timeLeft + 1) * 1000, 1000);
            timer.start();
            timeLeftText.setVisibility(View.VISIBLE);
        }
        else
        {
            if (timer != null)
            {
                timer.cancel();
                timer = null;
            }
            timeLeftText.setVisibility(View.INVISIBLE);
        }
    }

    public class MyCountDownTimer extends CountDownTimer
    {

        public MyCountDownTimer(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisUntilFinished)
        {
            timeLeft = (int) millisUntilFinished / 1000;
            timeLeftText.setText("Seconds Remaining: " + timeLeft);
        }

        public void onFinish()
        {
            timer = null;
            timeLeft = 0;
            DatabaseHelper.getInstance(getApplicationContext()).recordScore(correctAnswers, incorrectAnswers);
            timeLeftText.setVisibility(View.INVISIBLE);
            playAgainButton.setEnabled(true);
            playAgainButton.setVisibility(View.VISIBLE);
            button1.setEnabled(false);
            button2.setEnabled(false);
            button3.setEnabled(false);
        }

    }
}