package com.cdac.mobilecontest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class HomeActivity extends Activity {
    public static final String REFUND_STATUS_URL = "https://tin.tin.nsdl.com/oltas/servlet/TaxRefundStatus";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void getRefund(View view) throws Exception {
        EditText panNumber = (EditText) findViewById(R.id.pan_number);
        EditText assessmentYear = (EditText) findViewById(R.id.assessment_year);

        Intent taxRefundIntent = new Intent(getApplicationContext(), TaxRefundStatusActivity.class);
        taxRefundIntent.putExtra("panNumber", panNumber.getText().toString());
        taxRefundIntent.putExtra("assessmentYear", assessmentYear.getText().toString());
        startActivity(taxRefundIntent);
    }
}
