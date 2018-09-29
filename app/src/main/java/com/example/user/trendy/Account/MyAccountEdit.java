package com.example.user.trendy.Account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trendy.BuildConfig;
import com.example.user.trendy.R;
import com.example.user.trendy.Util.SharedPreference;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.Storefront;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class MyAccountEdit extends Fragment {
    EditText firstname, lastname, email, mobilenumber,password;
    String emailtext, mobiletext, firstnametext, lastnametext,passwordtext;
    private String accessToken;
    private GraphClient graphClient;
    TextView save;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.myaccountedit, container, false);

        firstname = view.findViewById(R.id.first_name);
        lastname = view.findViewById(R.id.last_name);
        email = view.findViewById(R.id.email);
        mobilenumber = view.findViewById(R.id.mobile_number);
//        password=view.findViewById(R.id.password);
        save=view.findViewById(R.id.save);


        emailtext = getArguments().getString("email");
        mobiletext = getArguments().getString("mobile");
        firstnametext = getArguments().getString("firstname");
        lastnametext = getArguments().getString("lastname");

        firstname.setText(firstnametext);
        lastname.setText(lastnametext);
        email.setText(emailtext);
        mobilenumber.setText(mobiletext);
        accessToken = SharedPreference.getData("accesstoken", getActivity());
        Log.e("accestoken", ""+accessToken);
        graphClient = GraphClient.builder(getActivity())
                .shopDomain(BuildConfig.SHOP_DOMAIN)
                .accessToken(BuildConfig.API_KEY)
                .httpCache(new File(getActivity().getCacheDir(), "/http"), 10 * 1024 * 1024) // 10mb for http cache
                .defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST.expireAfter(5, TimeUnit.MINUTES)) // cached response valid by default for 5 minutes
                .build();



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accessToken != null) {
                    emailtext=email.getText().toString().trim();
                    mobiletext=mobilenumber.getText().toString().trim();
                    firstnametext=firstname.getText().toString().trim();
                    lastnametext=lastname.getText().toString().trim();
//                    passwordtext=password.getText().toString().trim();

                   update(accessToken);
                }
            }
        });

        return view;
    }

    public void update(String accessToken) {
        Storefront.CustomerUpdateInput input = new Storefront.CustomerUpdateInput()
                .setFirstName(firstnametext)
                .setLastName(lastnametext)
                .setEmail(emailtext)
//                .setPassword(passwordtext)
                .setPhone(mobiletext);

        Storefront.MutationQuery mutationQuery1 = Storefront.mutation(mutation -> mutation


                .customerUpdate(accessToken.trim(), input, arg -> arg
                        .customer(cus -> cus
                                .phone()
                                .email()
                                .firstName()
                                .lastName()
                                .id()
                        )
                        .userErrors(userError -> userError
                                .field()
                                .message()
                        )
                )
        );
        graphClient.mutateGraph(mutationQuery1).enqueue(new GraphCall.Callback<Storefront.Mutation>() {


            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.Mutation> response) {
                if (response.data() != null) {
                    if(response.data().getCustomerUpdate().getCustomer()!=null) {
                        String phone = response.data().getCustomerUpdate().getCustomer().getPhone();
                        String firstName = response.data().getCustomerUpdate().getCustomer().getFirstName();
                        String lastName = response.data().getCustomerUpdate().getCustomer().getLastName();
                        String email = response.data().getCustomerUpdate().getCustomer().getEmail();
                        String id=response.data().getCustomerUpdate().getCustomer().getId().toString();
                        Log.e("phone", ""+phone+firstName+lastName+email+id);

                    }else{

                        for (int i=0;i<response.data().getCustomerUpdate().getUserErrors().size();i++){
                            String phonecheck=response.data().getCustomerUpdate().getUserErrors().get(i).getMessage();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(),phonecheck,Toast.LENGTH_SHORT).show();

                                }
                            });
                        }



                    }
                } else {
                    Log.e("error", "errormessage");
                }
            }

            @Override
            public void onFailure(@NonNull GraphError error) {

            }
        });
    }

}
