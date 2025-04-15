package com.example.notificationsjava;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public static final String TAG = "MyBottomSheetDialogFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View customView = inflater.inflate(R.layout.custom_view, null);

        customView.findViewById(R.id.button_custom_view).setOnClickListener(view -> {
            String text = customView.<EditText>findViewById(R.id.editText_custom_view).getText().toString();
            ((MainActivity) requireActivity()).onDialogResult(text);
            dismiss();
        });

        return customView;
    }
}