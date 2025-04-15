package com.example.notificationsjava;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class MyDialogFragmentWithCustomView extends DialogFragment {

    public static final String TAG = "MyDialogFragmentWithCustomView";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View customView = inflater.inflate(R.layout.custom_view, null);
        customView.findViewById(R.id.button_custom_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = customView.<EditText>findViewById(R.id.editText_custom_view).getText().toString();
                ((MainActivity) requireActivity()).onDialogResult(text);
                // Метод диалога, который позволяет его просто закрыть
                // (по аналогии с Activity finish())
                dismiss();
            }
        });

        return customView;
    }

}
