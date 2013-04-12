package com.cdac.mobilecontest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_spinner_dropdown_item;
import static java.util.Arrays.asList;

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
        Intent taxRefundIntent = new Intent(getApplicationContext(), TaxRefundStatusActivity.class);
        String panNumber = this.panNumber.getText().toString();
        taxRefundIntent.putExtra("panNumber", panNumber.toUpperCase());
        taxRefundIntent.putExtra("assessmentYear",
                assessmentYears.get(assessmentYear.getSelectedItemPosition()));
        startActivity(taxRefundIntent);
    }

    private List<String> years() {
        assessmentYears = asList("2004-2005", "2005-2006", "2006-2007", "2007-2008", "2008-2009",
                "2009-2010", "2010-2011");
        return assessmentYears;
    }
}
