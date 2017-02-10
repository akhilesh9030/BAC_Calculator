package com.example.bolla.baccalculator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SeekBar sbar;
    TextView alcohol_perc_display;
    TextView bac_value_display;
    Button add_drink_button;
    Button save_button;
    Button reset_button;
    EditText enter_weight_text;
    RadioGroup rg;
    Switch sw;
    ProgressBar pb;
    TextView status;
    String saved_gender;
    Double saved_weight = 0.0;
    Double saved_gender_r = 0.0;
    Double accumulated_BAC = 0.0;
    Double current_BAC = 0.0;
    Double current_r = 0.0;
    Double current_weight = 0.0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(Boolean.TRUE);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        sbar = (SeekBar) findViewById(R.id.alcoholSlider);
        alcohol_perc_display = (TextView) findViewById(R.id.alcoholPerc);
        add_drink_button = (Button) findViewById(R.id.addDrinkButton);
        enter_weight_text = (EditText) findViewById(R.id.enterWeight);
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        sw = (Switch) findViewById(R.id.genderSwitch);
        bac_value_display = (TextView) findViewById(R.id.bacLevelValue);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        save_button = (Button) findViewById(R.id.saveButton);
        reset_button = (Button) findViewById(R.id.resetButton);
        status = (TextView) findViewById(R.id.yourStatusValue);



        add_drink_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ounces = 1;
                Double alcohol_perc = sbar.getProgress()*5/100.0;
                int i = rg.getCheckedRadioButtonId();

                if(i == R.id.rB1){ounces = 1;}
                else if(i == R.id.rB2){ounces = 5;}
                else if(i == R.id.rB3){ounces = 12;}

                if(saved_weight == 0.0 || saved_gender_r == 0.0){
                    enter_weight_text.setError("Enter the weight in lbs");
                }
                else{
                    current_BAC = (ounces * alcohol_perc * 6.24)/(saved_weight * saved_gender_r);
                    accumulated_BAC = accumulated_BAC + current_BAC;
                    bac_value_display.setText(accumulated_BAC.toString());
                    enter_weight_text.setText(saved_weight.toString());
                    if(saved_gender.equals("F")){
                    sw.setChecked(false);}
                    else{
                        sw.setChecked(true);
                    }
                    Toast.makeText(getApplicationContext(),"A drink has been added",Toast.LENGTH_SHORT).show();


                    if(accumulated_BAC >= 0.25){
                        pb.setProgress(100);
                        add_drink_button.setEnabled(false);
                        save_button.setEnabled(false);
                        Toast.makeText(getApplicationContext(),"No more drinks for you",Toast.LENGTH_SHORT).show();
                    }
                    else{

                        pb.setProgress((int) (accumulated_BAC*400));

                    }

                    if(accumulated_BAC <= 0.08){
                     status.setText("You're safe");
                        status.setBackgroundColor(Color.parseColor("#FF418707"));
                    }
                    else if(accumulated_BAC > 0.08 && accumulated_BAC < 0.20){
                    status.setText("Be Careful");
                        status.setBackgroundColor(Color.parseColor("#FFEE670D"));
                    }
                    else{
                        status.setText("Over the Limit!");
                        status.setBackgroundColor(Color.parseColor("#FFF62E23"));

                    }
                }

            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sw.isChecked()){
                    current_r = 0.68; //Men
                    saved_gender = "M";
                }
                else {
                    current_r = 0.55; //Women
                    saved_gender = "F";
                }
                try {
                    current_weight = Double.parseDouble(enter_weight_text.getText().toString());
                    if(accumulated_BAC == 0.0){

                        saved_gender_r = current_r;
                        saved_weight = current_weight;
                        Toast.makeText(getApplicationContext(),"Your Information is Saved",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        current_BAC = (accumulated_BAC * saved_weight * saved_gender_r) / (current_weight * current_r);

                        saved_gender_r = current_r;
                        saved_weight = current_weight;
                        accumulated_BAC = current_BAC;
                        Toast.makeText(getApplicationContext(),"Your Information is Saved",Toast.LENGTH_SHORT).show();


                        if (accumulated_BAC >= 0.25) {
                            pb.setProgress(100);
                            add_drink_button.setEnabled(false);
                            save_button.setEnabled(false);
                            Toast.makeText(getApplicationContext(),"No more drinks for you",Toast.LENGTH_SHORT).show();

                        } else {
                            pb.setProgress((int) (accumulated_BAC * 400));

                        }
                        bac_value_display.setText(accumulated_BAC.toString());

                    }
                }
                catch (Exception e){
                    enter_weight_text.setError("Enter the weight in lbs");
                }




            }
        });


        sbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                alcohol_perc_display.setText(5*i+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sbar.setProgress(1);
                saved_gender_r = 0.0;
                saved_weight = 0.0;
                add_drink_button.setEnabled(true);
                save_button.setEnabled(true);
                enter_weight_text.setText("");
                RadioButton radioButton = (RadioButton) findViewById(R.id.rB1);
                radioButton.setChecked(true);
                Toast.makeText(getApplicationContext(),"Your Information is Reset",Toast.LENGTH_SHORT).show();
                accumulated_BAC = 0.0;
                pb.setProgress(0);
                bac_value_display.setText("0.00");
                status.setText("You're Safe ");
                status.setBackgroundColor(Color.parseColor("#FF418707"));
                sw.setChecked(false);
            }
        });


    }
}
