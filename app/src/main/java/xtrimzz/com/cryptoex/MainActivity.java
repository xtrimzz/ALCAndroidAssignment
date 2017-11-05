package xtrimzz.com.cryptoex;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    // Members for handle conversion views
    private TextView eth, btc;
    private Button btn;
    private EditText et;
    private Spinner spin;
    private int index;
    private double inputValue;
    private String result[] = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



                /*----------- ##START## HANDLE CALCULATION OF CURRENCY ---- */


        et = (EditText) findViewById(R.id.et);
        btc = (TextView)findViewById(R.id.btc);
        eth = (TextView)findViewById(R.id.eth);
        btn = (Button)findViewById(R.id.btn);
        spin = (Spinner) findViewById(R.id.spin);

        //Populate the spinner values with string
        //Populate the spinner values with string
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currency, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spin.setAdapter(adapter);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                index = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btc.setText("Wait...");
                eth.setText("Wait...");

                //if user did not type a thing or type a dot
                if (et.getText().toString().length() > 0 && !et.getText().toString().equals(".")) {
                    String textValue = et.getText().toString();
                    //convert the string to double
                    inputValue = Double.parseDouble(textValue);

                    new Calculate().execute();
                }
            }
        });

                /*----------- ##END## HANDLE CALCULATION OF CURRENCY ---- */
    }









    public class  Calculate extends AsyncTask<String, String, String[]> {
        private final String TAG = Calculate.class.getSimpleName(); // Get class name for string

        @Override
        protected String[] doInBackground(String... params) {

            if (index == 0) {
                // calculate exchange for NGN
                result[0] = calcBtcEthForCurrency("NGN")[0];
                result[1] = calcBtcEthForCurrency("NGN")[1];

            }else if (index == 1){
                // calculate exchange for USD
                result[0] = calcBtcEthForCurrency("USD")[0];
                result[1] = calcBtcEthForCurrency("USD")[1];

            }else if (index == 2){
                // calculate exchange for CAD
                result[0] = calcBtcEthForCurrency("CAD")[0];
                result[1] = calcBtcEthForCurrency("CAD")[1];

            }else if (index == 3){
                // calculate exchange for EUR
                result[0] = calcBtcEthForCurrency("EUR")[0];
                result[1] = calcBtcEthForCurrency("EUR")[1];

            }else if (index == 4){
                // calculate exchange for GBP
                result[0] = calcBtcEthForCurrency("GBP")[0];
                result[1] = calcBtcEthForCurrency("GBP")[1];

            }else if (index == 5) {
                // calculate exchange for NGN
                result[0] = calcBtcEthForCurrency("INR")[0];
                result[1] = calcBtcEthForCurrency("INR")[1];

            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String[] strings) {
            if (index == 0) {
               setRateForCurrency(inputValue, "NGN");
            }else if (index == 1){
                setRateForCurrency(inputValue, "USD");
            }else if (index == 2){
                setRateForCurrency(inputValue, "CAD");
            }else if (index == 3){
                setRateForCurrency(inputValue, "EUR");
            }else if (index == 4){
                setRateForCurrency(inputValue, "GBP");
            }else if (index == 5){
                setRateForCurrency(inputValue, "INR");
            }
        }

        /*
        *Handle the JSON Connection from the api
        *
         * reUrl{String} - Get the url
         * return String - Json string*/
        public String getJson(String reUrl) {
            StringBuilder build = new StringBuilder();
            try {
                //Request
                URL url = new URL(reUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                //Build the JSON string
                String line;
                while ((line = reader.readLine()) != null) {
                    build.append(line);
                }

            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (ProtocolException e) {
                Log.e(TAG, "ProtocolException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
            return build.toString();
        }

        /*
        *Calcuate the BTC and ETC for the Currency
        *
        * currency {String} - the Currency Type
        * return String[] - BTC and ETH value*/
        public String[] calcBtcEthForCurrency(String currency){
              /*  04/11/17 - the coin type and the currency can be more than 3 eg. LINK Below

                    LINK  -> https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,DASH,ETH&tsyms=USD,INR,NGN,CAD

                    RESULT-> {"BTC":{"USD":7121.84,"INR":489386.06,"NGN":2609308.13,"CAD":9278.61},
                               "DASH":{"USD":270.92,"INR":18679.87,"NGN":99597.29,"CAD":354.16},
                               "ETH":{"USD":298.68,"INR":20471.02,"NGN":109147.36,"CAD":391}}

                 */
            String url;
            String output[] = new String[2];
            try {

                url = getJson("https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,ETH&tsyms="+currency);
                JSONObject CoinToObj;
                CoinToObj = new JSONObject(url);
                        output[0] = CoinToObj.getJSONObject("BTC").getString(currency);
                        output[1] = CoinToObj.getJSONObject("ETH").getString(currency);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return  output;
        }


        /*
        * This will calcuate and Display the Currency Rate for a Selected Coin
        *
        * ipVal {Double} - Value input by user
        * currency {String} - the Currency type*/
        public void setRateForCurrency(Double ipVal, String currency){
            double btc_to_currency,  eth_to_currency, btc_rate, eth_rate;

            //get result for Coins
            btc_to_currency = Double.parseDouble(result[0]);
            eth_to_currency = Double.parseDouble(result[1]);

            //if number value is positive perform the rate Calculation
            if (ipVal > 0) {
                btc_rate = ipVal * btc_to_currency;
                eth_rate = ipVal * eth_to_currency;

            //display rate
            btc.setText(currency + " " + btc_rate);
            eth.setText(currency + " " + eth_rate);
            }//End if
        }


    }

}