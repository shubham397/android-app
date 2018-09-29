package com.example.user.trendy.Bag;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.trendy.Bag.Db.AddToCart_Model;
import com.example.user.trendy.Bag.Db.DBHelper;
import com.example.user.trendy.Payu_Utility.AppEnvironment;
import com.example.user.trendy.Payu_Utility.AppPreference;
import com.example.user.trendy.Payu_Utility.MyApplication;
import com.example.user.trendy.R;
import com.example.user.trendy.Util.Constants;
import com.example.user.trendy.Util.SharedPreference;
import com.google.gson.JsonObject;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class PayUMoneyActivity extends AppCompatActivity implements View.OnClickListener {

    EditText emailedit, mobile, amountedit, discount;
    LinearLayout paynowbtn;
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    Button btnsubmit1, btncancel;
    RadioButton btnradonline, btnradcod;
    String emailstring, totalamount, coupon,firstname="",lastname="",address1="",city="",state="",country="",zip="",phone="";
    TextView txtpayamount, t_pay, discount_price;
    CardView apply_discount;
    LinearLayout discount_layout;
    int i = 0;
    private String dynamicKey = "";
    DBHelper db;
    List<AddToCart_Model> cartlist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_umoney);

        db = new DBHelper(this);

        cartlist = db.getCartList();
        totalamount = SharedPreference.getData("total", getApplicationContext());
        emailstring = SharedPreference.getData("email", getApplicationContext());



        if(getIntent()!=null){
            firstname=getIntent().getStringExtra("firstname");
            lastname=getIntent().getStringExtra("lastname");
            emailstring=getIntent().getStringExtra("email");
            address1=getIntent().getStringExtra("s_area");
            city=getIntent().getStringExtra("s_city");
            state=getIntent().getStringExtra("s_state");
            country=getIntent().getStringExtra("s_country");
            zip=getIntent().getStringExtra("s_pincode");

        }
        emailedit = (EditText) findViewById(R.id.payuemail);
        mobile = (EditText) findViewById(R.id.payumobile);
        amountedit = (EditText) findViewById(R.id.payuamount);
        discount = findViewById(R.id.discount);
        apply_discount = findViewById(R.id.apply_discount);
        discount_price = findViewById(R.id.discount_price);
        t_pay = findViewById(R.id.t_pay);
        discount_layout = findViewById(R.id.discount_layout);

        paynowbtn = (LinearLayout) findViewById(R.id.paynowbtn);
        paynowbtn.setOnClickListener(this);
        apply_discount.setOnClickListener(this);

        emailedit.setText(emailstring);
        amountedit.setText(totalamount);
        postOrder();
    }

    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment1(final PayUmoneySdkInitializer.PaymentParam paymentParam) {
        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");
        AppEnvironment appEnvironment = ((MyApplication) getApplication()).getAppEnvironment();
        stringBuilder.append(appEnvironment.salt());

//        Logger.LogError("hashsequence",stringBuilder.toString());
        String hash = hashCal("SHA-512", stringBuilder.toString());
      /*  AppEnvironment appEnvironment = ((BaseApplication) getApplication()).getAppEnvironment();
        stringBuilder.append(appEnvironment.salt());

        //String hash = hashCal(stringBuilder.toString());*/
        paymentParam.setMerchantHash(hash);

        return paymentParam;
    }

    public static String hashCal(String type, String hashString) {
        StringBuilder hash = new StringBuilder();
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(type);
            messageDigest.update(hashString.getBytes());
            byte[] mdbytes = messageDigest.digest();
            for (byte hashByte : mdbytes) {
                hash.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash.toString();
    }

    private void launchPayUMoneyFlow() {
        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        //Use this to set your custom text on result screen button
        // payUmoneyConfig.setDoneButtonText(((EditText) findViewById(R.id.status_page_et)).getText().toString());

        //Use this to set your custom title for the activity
        //payUmoneyConfig.setPayUmoneyActivityTitle(((EditText) findViewById(R.id.activity_title_et)).getText().toString());

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = 0;
        try {
            amount = Double.parseDouble(amountedit.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        String txnId = System.currentTimeMillis() + "";
        String phone = mobile.getText().toString();
        String productName = "product name";
        String firstName = "marcony";
        String email = emailedit.getText().toString();
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        AppEnvironment appEnvironment = ((MyApplication) getApplication()).getAppEnvironment();
        builder.setAmount(amount)
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(appEnvironment.debug())
                .setKey(appEnvironment.merchant_Key())
                .setMerchantId(appEnvironment.merchant_ID());

        try {
            mPaymentParams = builder.build();

            /*
             * Hash should always be generated from your server side.
             * */
            /* generateHashFromServer(mPaymentParams);*/

            /**
             * Do not use below code when going live
             * Below code is provided to generate hash from sdk.
             * It is recommended to generate hash from server side only.
             * */
            mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);

            if (AppPreference.selectedTheme != -1) {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PayUMoneyActivity.this, AppPreference.selectedTheme, false);
            } else {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PayUMoneyActivity.this, R.style.AppTheme_default, false);
            }

        } catch (Exception e) {
            // some exception occurred
            Log.e("Message", e.getStackTrace().toString());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            paynowbtn.setEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction

                } else {
                    //Failure Transaction
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();

                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("Payu's Data : " + payuResponse + "\n\n\n Merchant's Data: " + merchantResponse)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }).show();

            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d("PAYU", "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d("PAYU", "Both objects are null!");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.online:
                btnradonline.setChecked(true);
                btnradcod.setChecked(false);
                break;

            case R.id.cod:
                btnradcod.setChecked(true);
                btnradonline.setChecked(false);
                break;
            case R.id.paynowbtn:
                showCustomDialog1();
                break;
            case R.id.apply_discount:
                discount();
                break;


        }
    }

    private void discount() {
        if (i == 0) {
            coupon = discount.getText().toString();
            if (coupon.trim().length() != 0) {
                if (Integer.parseInt(totalamount) > 50) {
                    int a = Integer.parseInt(totalamount) - 50;
                    totalamount = String.valueOf(a);

                    t_pay.setText(totalamount);
                    discount_price.setText("50");
                    discount_layout.setVisibility(View.VISIBLE);
                }
            }
            i = 1;
        }


    }

    protected void showCustomDialog1() {
        // TODO Auto-generated method stub

        final Dialog dialog = new Dialog(PayUMoneyActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.paybywalletorbankdata);

        btnsubmit1 = (Button) dialog.findViewById(R.id.res_pay_submit);
        btncancel = (Button) dialog.findViewById(R.id.res_pay_cancel);
        txtpayamount = (TextView) dialog.findViewById(R.id.pay_amount);
        btnradonline = (RadioButton) dialog.findViewById(R.id.online);
        btnradcod = (RadioButton) dialog.findViewById(R.id.cod);

        txtpayamount.setText(totalamount);
        btnradonline.setOnClickListener(this);
        btnradcod.setOnClickListener(this);

        dialog.setCanceledOnTouchOutside(true);

        btnsubmit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("dismiss", "dialog dismiss");
                dialog.dismiss();

                if (btnradonline.isChecked()) {
                    launchPayUMoneyFlow();
                } else {
//cms
                }

            }
        });


        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // progressDialog.dismiss();
                dialog.dismiss();
                if (getApplicationContext() != null) {
                    finish();
                    startActivity(new Intent(PayUMoneyActivity.this, PayUMoneyActivity.class));
                }
            }
        });
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();
    }

    public void postOrder() {
phone=mobile.getText().toString().trim();
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", emailstring);
            jsonBody.put("financial_status", "pending");




            JSONArray line_items = new JSONArray();
            JSONObject items = new JSONObject();

            for (int i = 0; i < cartlist.size(); i++) {
                String product_varient_id = cartlist.get(i).getProduct_varient_id();
                Integer quantity = cartlist.get(i).getQty();
                Log.e("type", product_varient_id);
                items.put("variant_id", "5823671107611");
                items.put("quantity", quantity);
            }
            line_items.put(items);
            jsonBody.put("line_items", line_items);



            JSONArray note = new JSONArray();
            JSONObject notes = new JSONObject();

            notes.put("name", "paypal");
            notes.put("value", "78233011");

            line_items.put(items);
            jsonBody.put("note_attributes", note);

            JSONObject shipping = new JSONObject();
            shipping.put("first_name", firstname);
            shipping.put("last_name", lastname);
            shipping.put("address1", address1);
            shipping.put("phone", phone);
            shipping.put("city", city);
            shipping.put("province", state);
            shipping.put("country", country);
            shipping.put("zip", zip);
            jsonBody.put("shipping_address", shipping);


            JSONObject billingaddress = new JSONObject();
            billingaddress.put("first_name", firstname);
            billingaddress.put("last_name", lastname);
            billingaddress.put("address1", address1);
            billingaddress.put("phone", phone);
            billingaddress.put("city", city);
            billingaddress.put("province", state);
            billingaddress.put("country", country);
            billingaddress.put("zip", zip);
            jsonBody.put("billing_address", billingaddress);


            Log.d("check JSON", jsonBody.toString());


            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.postcreateorder, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        String msg = obj.getString("msg");

                        Log.e("msg", "" + msg);
                        if (msg.equals("success")) {
                            Iterator keys = obj.keys();
                            Log.e("Keys", "" + String.valueOf(keys));

                            while (keys.hasNext()) {
                                dynamicKey = (String) keys.next();
                                Log.d("Dynamic Key", "" + dynamicKey);
                                if(dynamicKey.equals("order")){
                                    JSONObject order=obj.getJSONObject("order");
                                    String orderid=order.getString("id");
                                    Log.e("orderid",orderid);

                                }
                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
//                        return requestBody == null;
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    //TODO if you want to use the status code for any other purpose like to handle 401, 403, 404
                    String statusCode = String.valueOf(response.statusCode);
                    //Handling logic
                    return super.parseNetworkResponse(response);
                }
//                @Override
//                protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                    String responseString = "";
//                    if (response != null) {
//                        responseString = String.valueOf(response.statusCode);
//                        // can get more details such as response.headers
//                    }
//                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
