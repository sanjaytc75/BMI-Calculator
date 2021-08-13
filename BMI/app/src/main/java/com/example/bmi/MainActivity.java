package com.example.bmi;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    private EditText weightET, heightET;
    private RadioGroup heightOption;
    private BMIData bmiData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.bmiData = new BMIData();
        this.weightET = findViewById(R.id.weightET);
        this.heightET = findViewById(R.id.heightET);
        this.heightOption = findViewById(R.id.heightOption);
        this.heightOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                bmiData.setHeightInFeet(checkedId == R.id.feetBtn);
                heightET.setHint("Enter the weight in " + ((checkedId == R.id.feetBtn) ? "Feet" : "CMS"));
            }
        });
    }

    public void calculateBMI(View view) {
        if (this.isDataValid()) {
            this.bmiData.setWeight(Double.parseDouble(this.weightET.getText().toString().trim()));
            this.bmiData.setHeight(Double.parseDouble(this.heightET.getText().toString().trim()));
            this.updateResult(false);
        }
    }

    public void resetView(View view) {
        this.heightET.setText("");
        this.weightET.setText("");
        ((RadioButton) this.findViewById(R.id.cmsBtn)).setChecked(true);
        this.updateResult(true);
    }

    private void updateResult(boolean isForReset) {
        ((TextView) this.findViewById(R.id.resultTV)).setText(isForReset ? "" : this.bmiData.getFinalBMI());
        ((TextView) this.findViewById(R.id.bmiStatusTV)).setText(isForReset ? "" : String.valueOf(this.bmiData.getBMIString()));
        this.findViewById(R.id.bmiStatusTV).setBackgroundColor(this.bmiData.getBMIColor());
        this.findViewById(R.id.resultCard).setVisibility(isForReset ? View.GONE : View.VISIBLE);
    }

    private boolean isDataValid() {
        if (TextUtils.isEmpty(this.weightET.getText().toString().trim())) {
            this.weightET.requestFocus();
            this.weightET.setError("Please enter the weight");
            return false;
        } else if (TextUtils.isEmpty(this.heightET.getText().toString().trim())) {
            this.heightET.requestFocus();
            this.heightET.setError("Please enter the height");
            return false;
        }
        return true;
    }


    private class BMIData {
        private double height, weight;
        private boolean isHeightInFeet = false;
        private double finalBMI;

        public BMIData() {
        }

        public BMIData(double weight, double height, boolean isHeightInFeet) {
            this.weight = weight;
            this.height = height;
            this.isHeightInFeet = isHeightInFeet;
        }

        public double getHeight() {
            return this.isHeightInFeet ? ((this.height * 0.3 * 100)) : (this.height);
        }

        public void setHeight(double height) {
            this.height = height;
        }

        public double getWeight() {
            return this.weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public void setHeightInFeet(boolean heightInFeet) {
            this.isHeightInFeet = heightInFeet;
        }

        public boolean isHeightInFeet() {
            return this.isHeightInFeet;
        }

        public String getFinalBMI() {
            this.finalBMI = (((this.weight) / (this.getHeight() * this.getHeight())) * 10000);
            return new DecimalFormat("##.##").format(this.finalBMI);
        }

        public String getBMIString() {
            if (this.finalBMI < 18.5) {
                return "Under Weight";
            } else if (this.finalBMI > 18.5 && this.finalBMI < 25) {
                return "Normal";
            } else if (this.finalBMI >= 25 && this.finalBMI <= 29.9) {
                return "Over Weight";
            } else {
                return "Obese";
            }
        }

        public int getBMIColor() {
            if (this.finalBMI < 18.5) {
                return getColor(R.color.yellow);
            } else if (this.finalBMI > 18.5 && this.finalBMI < 25) {
                return getColor(R.color.green);
            } else if (this.finalBMI >= 25 && this.finalBMI <= 29.9) {
                return getColor(R.color.amber);
            } else {
                return getColor(R.color.red);
            }
        }
    }
}