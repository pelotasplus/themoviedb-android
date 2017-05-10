package pl.pelotasplus.themoviedb.demo.screens.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import pl.pelotasplus.themoviedb.demo.R;

public class DatePickerFragment extends DialogFragment {
    private final static String TAG = DatePickerFragment.class.getSimpleName();
    private final static String EXTRA_SELECTED_YEAR = "DatePickerFragment/EXTRA_SELECTED_YEAR";
    private OnDatePicked onDatePicked = OnDatePicked.NOOP;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context themeContext = getContext();
        final LayoutInflater inflater = LayoutInflater.from(themeContext);
        final AlertDialog.Builder d = new AlertDialog.Builder(themeContext);

        View dialogView = inflater.inflate(R.layout.dialog_year_picker, null);

        d.setTitle(R.string.year_dialog_title);
        d.setMessage(R.string.year_dialog_message);
        d.setView(dialogView);

        final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.dialog_number_picker);
        numberPicker.setMaxValue(2020);
        numberPicker.setMinValue(1970);
        numberPicker.setValue(getArguments().getInt(EXTRA_SELECTED_YEAR, 2017));
        numberPicker.setWrapSelectorWheel(false);

        d.setPositiveButton(R.string.filter, (dialogInterface, i) -> onDatePicked.onYearPicked(numberPicker.getValue()));
        d.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
        });

        return d.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnDatePicked) {
            onDatePicked = (OnDatePicked) activity;
        }
    }

    @Override
    public void onDetach() {
        onDatePicked = OnDatePicked.NOOP;

        super.onDetach();
    }

    public static void show(FragmentManager fragmentManager, int selectedYear) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_SELECTED_YEAR, selectedYear);

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(args);
        newFragment.show(fragmentManager, TAG);
    }

    interface OnDatePicked {
        void onYearPicked(int year);

        OnDatePicked NOOP = year -> {
        };
    }
}