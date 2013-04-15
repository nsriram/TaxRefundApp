package com.cdac.mobilecontest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeActivity extends Activity {
    private ArrayAdapter<String> spinnerAdapter;

    private EditText panNumber;
    private Spinner assessmentYear;
    private List<String> assessmentYears = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        assessmentYears = years();
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, assessmentYears);
        panNumber = (EditText) findViewById(R.id.pan_number);
        assessmentYear = (Spinner) findViewById(R.id.assessment_year);
        assessmentYear.setAdapter(spinnerAdapter);
    }

    public void getRefund(View view) throws Exception {
        String panNumberValue = this.panNumber.getText().toString();
        if (!"".equals(panNumberValue)) {
            Intent taxRefundIntent = new Intent(getApplicationContext(), TaxRefundStatusActivity.class);
            taxRefundIntent.putExtra("panNumber", panNumberValue.toUpperCase());
            taxRefundIntent.putExtra("assessmentYear",
                    assessmentYears.get(assessmentYear.getSelectedItemPosition()));
            startActivity(taxRefundIntent);
        } else {
            Toast.makeText(this, R.string.warning_message, Toast.LENGTH_LONG).show();
        }
    }

    private List<String> years() {
        int current = 2004;

        Calendar calendar = Calendar.getInstance();
        int end = calendar.get(Calendar.YEAR);
        assessmentYears = new ArrayList<String>();
        while (current < end) {
            assessmentYears.add("" + current + "-" + (current + 1));
            current++;
        }
        return assessmentYears;
    }
}
