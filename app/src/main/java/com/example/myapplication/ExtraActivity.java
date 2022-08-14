package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.actionitembadge.library.ActionItemBadge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExtraActivity extends AppCompatActivity {
    private TextView tvTitle;

    private com.example.myapplication.dealsAdapter rvAdapter;
    private RecyclerView rvDeals;
    private ArrayList<deals> dealsArrayList;
    private BottomNavigationView nav_View;
    private ProgressBar progressBar;
    private Product product;
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayout errorLayout;
    private Button btnRetry;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);
        tvTitle = findViewById(R.id.tvTitle);
        nav_View = findViewById(R.id.nav_View);
        btnRetry=findViewById(R.id.btnRetry);
        progressBar=findViewById(R.id.progressBar);
        errorLayout=findViewById(R.id.errorLayout);
        swipeRefresh=findViewById(R.id.swipeRefresh);







        ImageSlider imageSlider = (ImageSlider) findViewById(R.id.image_slider);

        ArrayList<SlideModel> imageList = new ArrayList<SlideModel>();

        imageList.add(new SlideModel("https://admin.indolj.io/upload/1638438889-Mutton-Kabab-Masala-min.jpg", "Mutton Kabab Masala"));
        imageList.add(new SlideModel("https://admin.indolj.io/upload/1638438849-Mutton-Chanp-Masala-min.jpg","Mutton Chanp Masla"));
        imageList.add(new SlideModel("https://admin.indolj.io/upload/1638438943-Chicken-Makhni-Tawa-min.jpg", "Chiken Tawa Makhni"));
        imageSlider.setImageList(imageList);




        rvDeals = findViewById(R.id.rvDeals);


        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                Intent intent = new Intent(ExtraActivity.this, NewArrivalActivity.class);
                startActivity(intent);

            }
        });

        nav_View.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.nav_Home) {



                } else if (id == R.id.nav_category){

                    Intent mainIntent = new Intent(ExtraActivity.this, com.example.myapplication.MainActivity.class);
                    startActivity(mainIntent);
                    return true;


                }
                else if (id == R.id.nav_Fav) {

                    Intent mainIntent = new Intent(ExtraActivity.this, com.example.myapplication.FavoriteActivity.class);
                    startActivity(mainIntent);
                    return true;


                }

                else if(id==R.id.nav_About)
                {
                    Toast.makeText(ExtraActivity.this,"Logout Successfull.....",Toast.LENGTH_SHORT).show();
                        SessionHelper.Logout(ExtraActivity.this);
                  Intent intent = new Intent(ExtraActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
                return false;
            }
        });

        fetchDeals();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchDeals();
            }
        });
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchDeals();
            }
        });

    }

    private void fetchDeals() {
        progressBar.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
        if (swipeRefresh.isRefreshing()) {
            progressBar.setVisibility(View.GONE);
            errorLayout.setVisibility(View.GONE);
        }



        dealsArrayList = new ArrayList<>();
        String dealsUrl = "https://sherlocked00069.000webhostapp.com/ShopEasy/categoryDetail.php?category_id=13";
        StringRequest request = new StringRequest(dealsUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int status = jsonResponse.getInt("status");

                    if (status == 0) {
                        String message = jsonResponse.getString("message");
                        Toast.makeText(ExtraActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray categoriesArray = jsonResponse.getJSONArray("products");
                        dealsArrayList = new Gson().fromJson(categoriesArray.toString(), new TypeToken<ArrayList<deals>>() {
                        }.getType());
                        rvDeals.setLayoutManager(new GridLayoutManager(ExtraActivity.this, 2));
                        rvAdapter = new com.example.myapplication.dealsAdapter(dealsArrayList, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                Intent intent = new Intent(ExtraActivity.this, ExtraDetailActivity.class);
                                intent.putExtra("product", dealsArrayList.get(position));

                                startActivity(intent);

                            }
                        });

                        rvDeals.setAdapter(rvAdapter);


                    }

                } catch (JSONException e) {
                    errorLayout.setVisibility(View.VISIBLE);


                    e.printStackTrace();
                    Toast.makeText(ExtraActivity.this, "Unable to process server response ", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);


                error.printStackTrace();

                Toast.makeText(ExtraActivity.this, "Unable to get data from server ", Toast.LENGTH_SHORT).show();

            }
        });
        request.setShouldCache(false);

        Volley.newRequestQueue(getApplicationContext()).add(request);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        DbHelper dbHelper = new DbHelper(ExtraActivity.this);

        int cartCount = dbHelper.getCartCount();
        if (cartCount == 0) {
           // ActionItemBadge.hide(menu.findItem(R.id.action_cart));
            VectorDrawableCompat drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_baseline_shopping_cart_24, null);
            ActionItemBadge.update(ExtraActivity.this, menu.findItem(R.id.action_cart), drawable, ActionItemBadge.BadgeStyles.YELLOW, cartCount);
        } else {
            VectorDrawableCompat drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_baseline_shopping_cart_24, null);
            ActionItemBadge.update(ExtraActivity.this, menu.findItem(R.id.action_cart), drawable, ActionItemBadge.BadgeStyles.YELLOW, cartCount);
        }


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_cart) {
            Intent intent = new Intent(ExtraActivity.this, CartActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.action_share)
        {
            String app="https://play.google.com/store/apps/details?id=com.example.myapplication";
            String message="I am using this awesome food ordering app you should try this App :,"+app;
            new ShareCompat.IntentBuilder(ExtraActivity.this)
                    .setText(message)
                    .setSubject("Sharing App")
                    .setType("plain/text")
                    .startChooser();
        }
        else if(id==R.id.action_privacy)
        {
            AlertDialog dialog=new AlertDialog.Builder(ExtraActivity.this)
                    .setTitle("Privacy Policy")
                    .setMessage("Privacy Policy\n" +
                            "FoodSpot built the FoodSpot app as a Commercial app. This SERVICE is provided by FoodSpot and is intended for use as is.\n" +
                            "\n" +
                            "This page is used to inform visitors regarding our policies with the collection, use, and disclosure of Personal Information if anyone decided to use our Service.\n" +
                            "\n" +
                            "If you choose to use our Service, then you agree to the collection and use of information in relation to this policy. The Personal Information that we collect is used for providing and improving the Service. We will not use or share your information with anyone except as described in this Privacy Policy.\n" +
                            "\n" +
                            "The terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, which are accessible at FoodSpot unless otherwise defined in this Privacy Policy.\n" +
                            "\n" +
                            "Information Collection and Use\n" +
                            "\n" +
                            "For a better experience, while using our Service, we may require you to provide us with certain personally identifiable information. The information that we request will be retained by us and used as described in this privacy policy.\n" +
                            "\n" +
                            "The app does use third-party services that may collect information used to identify you.\n" +
                            "\n" +
                            "Link to the privacy policy of third-party service providers used by the app\n" +
                            "\n" +
                            "Google Play Services\n" +
                            "Log Data\n" +
                            "\n" +
                            "We want to inform you that whenever you use our Service, in a case of an error in the app we collect data and information (through third-party products) on your phone called Log Data. This Log Data may include information such as your device Internet Protocol (“IP”) address, device name, operating system version, the configuration of the app when utilizing our Service, the time and date of your use of the Service, and other statistics.\n" +
                            "\n" +
                            "Cookies\n" +
                            "\n" +
                            "Cookies are files with a small amount of data that are commonly used as anonymous unique identifiers. These are sent to your browser from the websites that you visit and are stored on your device's internal memory.\n" +
                            "\n" +
                            "This Service does not use these “cookies” explicitly. However, the app may use third-party code and libraries that use “cookies” to collect information and improve their services. You have the option to either accept or refuse these cookies and know when a cookie is being sent to your device. If you choose to refuse our cookies, you may not be able to use some portions of this Service.\n" +
                            "\n" +
                            "Service Providers\n" +
                            "\n" +
                            "We may employ third-party companies and individuals due to the following reasons:\n" +
                            "\n" +
                            "To facilitate our Service;\n" +
                            "To provide the Service on our behalf;\n" +
                            "To perform Service-related services; or\n" +
                            "To assist us in analyzing how our Service is used.\n" +
                            "We want to inform users of this Service that these third parties have access to their Personal Information. The reason is to perform the tasks assigned to them on our behalf. However, they are obligated not to disclose or use the information for any other purpose.\n" +
                            "\n" +
                            "Security\n" +
                            "\n" +
                            "We value your trust in providing us your Personal Information, thus we are striving to use commercially acceptable means of protecting it. But remember that no method of transmission over the internet, or method of electronic storage is 100% secure and reliable, and we cannot guarantee its absolute security.\n" +
                            "\n" +
                            "Links to Other Sites\n" +
                            "\n" +
                            "This Service may contain links to other sites. If you click on a third-party link, you will be directed to that site. Note that these external sites are not operated by us. Therefore, we strongly advise you to review the Privacy Policy of these websites. We have no control over and assume no responsibility for the content, privacy policies, or practices of any third-party sites or services.\n" +
                            "\n" +
                            "Children’s Privacy\n" +
                            "\n" +
                            "These Services do not address anyone under the age of 13. We do not knowingly collect personally identifiable information from children under 13 years of age. In the case we discover that a child under 13 has provided us with personal information, we immediately delete this from our servers. If you are a parent or guardian and you are aware that your child has provided us with personal information, please contact us so that we will be able to do the necessary actions.\n" +
                            "\n" +
                            "Changes to This Privacy Policy\n" +
                            "\n" +
                            "We may update our Privacy Policy from time to time. Thus, you are advised to review this page periodically for any changes. We will notify you of any changes by posting the new Privacy Policy on this page.\n" +
                            "\n" +
                            "This policy is effective as of 2022-06-05\n" +
                            "\n" +
                            "Contact Us\n" +
                            "\n" +
                            "If you have any questions or suggestions about our Privacy Policy, do not hesitate to contact us at FoodSpot@gmail.com.\n" +
                            "\n" +
                            "This privacy policy page was created at privacypolicytemplate.net and modified/generated by App Privacy Policy Generator")
                    .setPositiveButton("Agree",null)

                    .create();
            dialog.show();
        }
            else if(id==R.id.action_about)
        {
            AlertDialog dialog=new AlertDialog.Builder(ExtraActivity.this)
                    .setTitle("FoodSpot")
                    .setIcon(R.drawable.ic_baseline_fastfood_24)
                    .setMessage("you can find us at Foodspot@gmail.com")
                    .setPositiveButton("ok",null)
                    .create();
            dialog.show();


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

}
