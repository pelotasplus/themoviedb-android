package pl.pelotasplus.themoviedb.demo.screens.main;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import pl.pelotasplus.themoviedb.demo.R;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
//        final Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//
//        // Create a new instance of DatePickerDialog and return it
//        return new DatePickerDialog(getActivity(), this, year, month, day);

        final Context themeContext = getContext();
        final LayoutInflater inflater = LayoutInflater.from(themeContext);
        final AlertDialog.Builder d = new AlertDialog.Builder(themeContext);
        View dialogView = inflater.inflate(R.layout.dialog_year_picker, null);
        d.setTitle(R.string.year_dialog_title);
        d.setMessage(R.string.year_dialog_message);
        d.setView(dialogView);
        final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.dialog_number_picker);
        numberPicker.setMaxValue(2017);
        numberPicker.setMinValue(1970);
        numberPicker.setValue(2010);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener((numberPicker1, i, i1) -> Log.d("XXX", "onValueChange: " + i));
        d.setPositiveButton(R.string.filter, (dialogInterface, i) -> Log.d("XXX", "onClick: " + numberPicker.getValue()));
        d.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
        });

        return d.create();
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
    }

    public static void show(FragmentManager fragmentManager) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(fragmentManager, "datePicker");
    }
}