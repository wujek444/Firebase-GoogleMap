package pl.jwojcik.gascompanion.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import pl.jwojcik.gascompanion.R;
import pl.jwojcik.gascompanion.activities.GasStationActivity;
import pl.jwojcik.gascompanion.models.GasStation;
import pl.jwojcik.gascompanion.models.Price;
import pl.jwojcik.gascompanion.services.ObjectResultListener;
import pl.jwojcik.gascompanion.services.firebase.GasStationService;

public class AddPriceDialogFragment extends android.support.v4.app.DialogFragment {

    private EditText pricePerLitreEditText;
    private DatePicker priceEntryDatePicker;
    private Button priceEntrySubmitBtn;

    private GasStationService gasStationService = GasStationService.getInstance();
    private static GasStation currentGasStation;
    private static String gasType;

    public AddPriceDialogFragment() {
        //wymagany pusty konstruktor
    }

    public static AddPriceDialogFragment newInstance(String gasTypeString, GasStation gasStation) {
        currentGasStation = gasStation;
        AddPriceDialogFragment frag = new AddPriceDialogFragment();
        Bundle args = new Bundle();
        gasType = gasTypeString;
        args.putString("title", "Dodaj cenę " + gasType);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pricePerLitreEditText = view.findViewById(R.id.pricePerLitreEditText);
        priceEntryDatePicker = view.findViewById(R.id.priceEntryDatePicker);
        priceEntrySubmitBtn = view.findViewById(R.id.priceEntrySubmit);

        String title = getArguments().getString("title");
        getDialog().setTitle(title);

        //ustaw focus i włącz klawiaturę ekranową
        pricePerLitreEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        priceEntrySubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Price newPrice = new Price(Double.parseDouble(pricePerLitreEditText.getText().toString()), gasType);

                gasStationService.addPrice(currentGasStation, newPrice,  new ObjectResultListener() {
                    @Override
                    public void onResult(boolean isSuccess, String error, Object object) {
                        if(isSuccess){
                            Toast.makeText(getActivity(), "Dodano cenę", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getContext(), GasStationActivity.class);
                            intent.putExtra("value", currentGasStation);
                            getDialog().dismiss();
                            getActivity().finish();
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

}
