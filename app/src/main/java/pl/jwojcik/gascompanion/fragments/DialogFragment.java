package pl.jwojcik.gascompanion.fragments;


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
import pl.jwojcik.gascompanion.models.GasType;
import pl.jwojcik.gascompanion.models.Price;
import pl.jwojcik.gascompanion.services.FirebaseService;
import pl.jwojcik.gascompanion.services.ObjectResultListener;

public class DialogFragment extends android.support.v4.app.DialogFragment {

    private EditText pricePerLitreEditText;
    private DatePicker priceEntryDatePicker;
    private Button priceEntrySubmitBtn;

    private static FirebaseService firebaseService = FirebaseService.shared;

    public DialogFragment() {
        //wymagany pusty konstruktor
    }

    public static DialogFragment newInstance(String title) {
        DialogFragment frag = new DialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
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
                Price newPrice = new Price(Double.parseDouble(pricePerLitreEditText.getText().toString()), GasType.PB95);
                firebaseService.createPrice(newPrice, new ObjectResultListener() {
                    @Override
                    public void onResult(boolean isSuccess, String error, Object object) {
                        if(isSuccess){
                            Toast.makeText(getActivity(), "Dodano cenę", Toast.LENGTH_LONG).show();
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }

}
