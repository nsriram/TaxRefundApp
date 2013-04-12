package com.cdac.mobilecontest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.util.EntityUtils.toString;

public class TaxRefundStatusActivity extends Activity {

    public static final String REFUND_STATUS_URL = "https://tin.tin.nsdl.com/oltas/servlet/TaxRefundStatus";
    private ProgressDialog progressDialog;
    private String panNumber;
    private String assessmentYear;
    private Elements status;
    private int responseCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent launchingIntent = getIntent();
        Bundle extras = launchingIntent.getExtras();
        panNumber = (String) extras.get("panNumber");
        assessmentYear = (String) extras.get("assessmentYear");

        this.progressDialog = ProgressDialog.show(this, "Fetching ...", "Refund Status...", true, true,
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
        new DownloadTask().execute("");
    }

    private void handleResponse() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
        setContentView(R.layout.refund_status);

        TextView headerView = (TextView) findViewById(R.id.refund_header);
        headerView.setText(panNumber + " (" + assessmentYear + ")");
        TextView paymentMode = (TextView) findViewById(R.id.payment_mode);
        TextView referenceNumber = (TextView) findViewById(R.id.reference_number);
        TextView refundStatus = (TextView) findViewById(R.id.refund_status);
        TextView refundDate = (TextView) findViewById(R.id.refund_date);

        if (responseCode == HttpStatus.SC_OK) {
            if (status.size() > 0) {
                Elements data = status.select("td");
                if (data.size() >= 4) {
                    paymentMode.setText(data.get(0).text());
                    referenceNumber.setText(data.get(1).text());
                    refundStatus.setText(data.get(2).text());
                    refundDate.setText(data.get(3).text());
                }
            } else {
                refundStatus.setText("Refund request NOT FOUND for " + panNumber + ". \n\n" +
                        "(a) Your assessing officer has not sent this refund to Refund Banker.\n\n" +
                        "(b) If this refund has been sent by your Assessing Officer within the last week, you may wait for a week and again check status.\n" +
                        "\n" +
                        " Please contact your Assessing Officer.");
            }
        }
    }

    private class DownloadTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(REFUND_STATUS_URL);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            nameValuePairs.add(new BasicNameValuePair("panNumber", panNumber));
            nameValuePairs.add(new BasicNameValuePair("assessmentYear", assessmentYear));
            HttpResponse response = null;
            HttpEntity entity=null;
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = httpclient.execute(httppost);
                responseCode = response.getStatusLine().getStatusCode();
                entity = response.getEntity();
                if (entity != null) {
                    try {
                        Document doc = Jsoup.parse(EntityUtils.toString(entity));
                        status = doc.select("table.statusTable");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseCode;
        }

        protected void onPostExecute(Integer responseCode) {
            TaxRefundStatusActivity.this.handleResponse();
        }
    }
}
