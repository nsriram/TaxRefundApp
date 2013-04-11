package com.cdac.mobilecontest;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
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
import org.jsoup.nodes.Element;
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
                /*
                Document doc = Jsoup.parse(responseHTML);
                Elements status = doc.select("table.statusTable");
                Elements headers = status.select("th");
                Elements data = status.select("td"); */

                responseHTML = Uri.encode(responseHTML);
                WebView viewer = (WebView) findViewById(R.id.refund_status_webview);
                viewer.loadData(responseHTML, "text/html", "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String imageJS() {
        return "javascript:(function () {\n" +
                "  var w = \" + 200px + \";\n" +
                "  for( var i = 0; i < document.images.length; i++ ) {\n" +
                "    var img = document.images[i];\n" +
                "    if( img.width > w ) {\n" +
                "      img.height = Math.round( img.height * ( w/img.width ) );\n" +
                "      img.width = w;\n" +
                "      img.style.display='block';\n" +
                "    };\n" +
                "  }\n" +
                "})();";
    }
}
