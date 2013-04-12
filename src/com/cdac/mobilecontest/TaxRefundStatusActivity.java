package com.cdac.mobilecontest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableRow;
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

import java.util.ArrayList;
import java.util.List;

public class TaxRefundStatusActivity extends Activity {

    public static final String REFUND_STATUS_URL = "https://tin.tin.nsdl.com/oltas/servlet/TaxRefundStatus";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refund_status);

        Intent launchingIntent = getIntent();
        Bundle extras = launchingIntent.getExtras();

        String panNumber = (String) extras.get("panNumber");
        String assessmentYear = (String) extras.get("assessmentYear");

        TextView headerView = (TextView) findViewById(R.id.refund_header);
        headerView.setText(panNumber + " (" + assessmentYear + ")");

        TextView paymentMode = (TextView) findViewById(R.id.payment_mode);
        TextView referenceNumber = (TextView) findViewById(R.id.reference_number);
        TextView refundStatus = (TextView) findViewById(R.id.refund_status);
        TextView refundDate = (TextView) findViewById(R.id.refund_date);

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(REFUND_STATUS_URL);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

        nameValuePairs.add(new BasicNameValuePair("panNumber", panNumber));
        nameValuePairs.add(new BasicNameValuePair("assessmentYear", assessmentYear));

        HttpResponse response = null;
        String responseHTML = null;

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseHTML = EntityUtils.toString(entity);
            }
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                Document doc = Jsoup.parse(responseHTML);
                Elements status = doc.select("table.statusTable");
                if (status.size() > 0) {
                    Elements data = status.select("td");
                    if (data.size() >= 4) {
                        paymentMode.setText(data.get(0).text());
                        referenceNumber.setText(data.get(1).text());
                        refundStatus.setText(data.get(2).text());
                        refundDate.setText(data.get(3).text());
                    }
                } else {
                    refundStatus.setText("Refund request for " + panNumber + " not found. \n\n" +
                            "(a) Your assessing officer has not sent this refund to Refund Banker.\n\n" +
                            "(b) If this refund has been sent by your Assessing Officer within the last week, you may wait for a week and again check status.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
