package pl.jwojcik.gascompanion.activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import pl.jwojcik.gascompanion.Constants;
import pl.jwojcik.gascompanion.MyApplication;
import pl.jwojcik.gascompanion.R;
import pl.jwojcik.gascompanion.adapters.GasPriceAdapter;
import pl.jwojcik.gascompanion.fragments.DialogFragment;
import pl.jwojcik.gascompanion.models.Food;
import pl.jwojcik.gascompanion.models.GasStation;
import pl.jwojcik.gascompanion.services.FirebaseService;
import pl.jwojcik.gascompanion.services.ResultListener;

/**
 * Created by king on 17/08/2017.
 */

public class GasStationActivity extends AppCompatActivity implements View.OnClickListener {

    private final String PB95_GAS_TYPE = "PB95";
    private final String PB98_GAS_TYPE = "PB98";
    private final String ON_GAS_TYPE = "ON";
    private final String LPG_GAS_TYPE = "LPG";

    private ImageView btnBack;
    private ImageView ivImage;
    private TextView tvTitle;
    private TextView tvAddress;

    private Button addPB95Btn;
    private Button addPB98Btn;
    private Button addONBtn;
    private Button addLPGBtn;

    private RecyclerView mRecyclerViewPB95;
    private RecyclerView mRecyclerViewPB98;
    private RecyclerView mRecyclerViewON;
    private RecyclerView mRecyclerViewLPG;
    private ProgressBar progressBar;

    private List<Food> mStarters;
    private List<Food> mDrinks;
    private List<Food> mDishes;
    private List<Food> mDesserts;

    private ProgressDialog progressDialog;

    private GasStation gasStation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_station);

        Bundle bundle = getIntent().getExtras();
        gasStation = bundle.getParcelable("value");

        btnBack = findViewById(R.id.iv_back);
        ivImage = findViewById(R.id.imageView);
        tvTitle = findViewById(R.id.tv_title);
        tvAddress = findViewById(R.id.tv_address);

        addPB95Btn = findViewById(R.id.btn_add_pb95);
        addPB98Btn = findViewById(R.id.btn_add_pb98);
        addONBtn = findViewById(R.id.btn_add_on);
        addLPGBtn = findViewById(R.id.btn_add_lpg);

        addPB95Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(PB95_GAS_TYPE);
            }
        });
        addPB98Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(PB98_GAS_TYPE);
            }
        });
        addONBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(ON_GAS_TYPE);
            }
        });
        addLPGBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(LPG_GAS_TYPE);
            }
        });

        mRecyclerViewPB95 = findViewById(R.id.listview_pb95);
        mRecyclerViewPB98 = findViewById(R.id.listview_pb98);
        mRecyclerViewON = findViewById(R.id.listview_on);
        mRecyclerViewLPG = findViewById(R.id.listview_lpg);
        progressBar = findViewById(R.id.progressBar);

        btnBack.setOnClickListener(this);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewPB95.setLayoutManager(layoutManager1);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewPB98.setLayoutManager(layoutManager2);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewON.setLayoutManager(layoutManager3);
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewLPG.setLayoutManager(layoutManager4);

        initData();
        loadData();
    }

    @Override
    public void onPause() {
        if (progressDialog != null)
            progressDialog.dismiss();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (progressDialog != null)
            progressDialog.dismiss();
        super.onDestroy();
        Runtime.getRuntime().gc();
        System.gc();
    }

    private void initData() {
        if (gasStation != null) {
            tvTitle.setText(gasStation.getName());
            tvAddress.setText(gasStation.getAddress());

            if (gasStation.getPhotos() == null || gasStation.getPhotos().isEmpty()) {

            } else {
                String photoValue = gasStation.getPhotos().get(0);
                String photoUrl = String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s", photoValue, getString(R.string.google_server_api_key));
                MyApplication.mImageLoader.displayImage(photoUrl, ivImage, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    private void loadData() {
        mStarters = new ArrayList<>();
        mDrinks = new ArrayList<>();
        mDishes = new ArrayList<>();
        mDesserts = new ArrayList<>();

        progressDialog = ProgressDialog.show(this, "Loading datas...", "");
        FirebaseService.shared.getFoods(new ResultListener() {
            @Override
            public void onResult(boolean isSuccess, String error, List data) {

                if (isSuccess) {
                    for (int i = 0; i < data.size(); i ++) {
                        Food food = (Food) data.get(i);
                        if (food.type.equals(Constants.TYPE_STARTERS)) {
                            mStarters.add(food);
                        } else if (food.type.equals(Constants.TYPE_DRINKS)) {
                            mDrinks.add(food);
                        } else if (food.type.equals(Constants.TYPE_DISHES)){
                            mDishes.add(food);
                        } else {
                            mDesserts.add(food);
                        }
                    }

                    GasPriceAdapter startersAdapter = new GasPriceAdapter(GasStationActivity.this, mStarters);
                    mRecyclerViewPB95.setAdapter(startersAdapter);

                    GasPriceAdapter drinksAdapter = new GasPriceAdapter(GasStationActivity.this, mDrinks);
                    mRecyclerViewPB98.setAdapter(drinksAdapter);

                    GasPriceAdapter dishesAdapter = new GasPriceAdapter(GasStationActivity.this, mDishes);
                    mRecyclerViewON.setAdapter(dishesAdapter);

                    GasPriceAdapter dessertsAdapter = new GasPriceAdapter(GasStationActivity.this, mDesserts);
                    mRecyclerViewLPG.setAdapter(dessertsAdapter);

                }

                progressDialog.dismiss();
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void showDialog(String gasType){
        FragmentManager fm = getSupportFragmentManager();
        DialogFragment editNameDialogFragment = DialogFragment.newInstance("Dodaj cenę " + gasType);
        editNameDialogFragment.show(fm, "fragment_edit_name");

    }
}
