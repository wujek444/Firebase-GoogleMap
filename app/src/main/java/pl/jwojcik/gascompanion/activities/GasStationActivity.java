package pl.jwojcik.gascompanion.activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.jwojcik.gascompanion.MyApplication;
import pl.jwojcik.gascompanion.R;
import pl.jwojcik.gascompanion.adapters.GasPriceAdapter;
import pl.jwojcik.gascompanion.fragments.AddPriceDialogFragment;
import pl.jwojcik.gascompanion.models.GasStation;
import pl.jwojcik.gascompanion.models.Price;
import pl.jwojcik.gascompanion.services.ResultListener;
import pl.jwojcik.gascompanion.services.firebase.GasStationService;
import pl.jwojcik.gascompanion.utils.PriceComparator;


public class GasStationActivity extends AppCompatActivity implements View.OnClickListener {

    private final String PB95_GAS_TYPE = "pb95";
    private final String PB98_GAS_TYPE = "pb98";
    private final String ON_GAS_TYPE = "on";
    private final String LPG_GAS_TYPE = "lpg";

    private ImageView btnBack;
    private ImageView ivImage;
    private TextView tvTitle;
    private TextView tvAddress;

    private Button addPB95Btn;
    private Button addPB98Btn;
    private Button addONBtn;
    private Button addLPGBtn;

    private ListView listViewPB95;
    private ListView listViewPB98;
    private ListView listViewON;
    private ListView listViewLPG;
    private ProgressBar progressBar;

    private List<Price> pb95Prices;
    private List<Price> pb98Prices;
    private List<Price> onPrices;
    private List<Price> lpgPrices;

    private ProgressDialog progressDialog;

    private GasStation gasStation;
    private GasStationService gasStationService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_station);

        gasStationService = GasStationService.getInstance();

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

        listViewPB95 = findViewById(R.id.listview_pb95);
        listViewPB98 = findViewById(R.id.listview_pb98);
        listViewON = findViewById(R.id.listview_on);
        listViewLPG = findViewById(R.id.listview_lpg);
        progressBar = findViewById(R.id.progressBar);

        btnBack.setOnClickListener(this);

        loadData();
        initData();
        gasStationService.createGasStationIfNotPresent(gasStation);

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
        pb95Prices = new ArrayList<>();
        pb98Prices = new ArrayList<>();
        onPrices = new ArrayList<>();
        lpgPrices = new ArrayList<>();

        //spinner
        progressDialog = ProgressDialog.show(this, "Ładowanie danych", "");


        gasStationService.getGasStationPrices(gasStation, new ResultListener() {
            List<Price> allPrices = new ArrayList<>();

            @Override
            public void onResult(boolean isSuccess, String error, List data) {
                if (isSuccess) {
                    allPrices = data;
                    for (Price price : allPrices) {
                        switch (price.getGasType()) {
                            case PB95_GAS_TYPE:
                                pb95Prices.add(price);
                                break;
                            case PB98_GAS_TYPE:
                                pb98Prices.add(price);
                                break;
                            case LPG_GAS_TYPE:
                                lpgPrices.add(price);
                                break;
                            case ON_GAS_TYPE:
                                onPrices.add(price);
                                break;
                        }
                    }

                    GasPriceAdapter pb95Adapter = new GasPriceAdapter(GasStationActivity.this, 0);
                    Collections.sort(pb95Prices, new PriceComparator());
                    pb95Adapter.setList(pb95Prices);
                    listViewPB95.setAdapter(pb95Adapter);

                    GasPriceAdapter pb98Adapter = new GasPriceAdapter(GasStationActivity.this, 0);
                    Collections.sort(pb98Prices, new PriceComparator());
                    pb98Adapter.setList(pb98Prices);
                    listViewPB98.setAdapter(pb98Adapter);

                    GasPriceAdapter onAdapter = new GasPriceAdapter(GasStationActivity.this, 0);
                    Collections.sort(onPrices, new PriceComparator());
                    onAdapter.setList(onPrices);
                    listViewON.setAdapter(onAdapter);

                    GasPriceAdapter lpgAdapter = new GasPriceAdapter(GasStationActivity.this, 0);
                    Collections.sort(lpgPrices, new PriceComparator());
                    lpgAdapter.setList(lpgPrices);
                    listViewLPG.setAdapter(lpgAdapter);
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

    private void showDialog(String gasType) {
        FragmentManager fm = getSupportFragmentManager();
        AddPriceDialogFragment editNameDialogFragment = AddPriceDialogFragment.newInstance(gasType, gasStation);
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }
}
