package ptr.hf.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ptr.hf.R;
import ptr.hf.model.ReservationResponse;
import ptr.hf.network.ApiHelper;
import ptr.hf.network.ErrorResponse;
import ptr.hf.network.IApiFinishedListener;
import ptr.hf.network.IApiResultListener;
import ptr.hf.ui.auth.LoginActivity;
import ptr.hf.ui.map.MapFragment;
import ptr.hf.ui.map.MapFragment2;

public class ReservationFragment extends Fragment {

    //    @BindView(R.id.edittext_chargerid)
//    EditText edittextChargerid;
    @BindView(R.id.datepicker_from)
    Button datepickerFrom;
    @BindView(R.id.timepicker_from)
    Button timepickerFrom;
    @BindView(R.id.datepicker_to)
    Button datepickerTo;
    @BindView(R.id.timepicker_to)
    Button timepickerTo;
    @BindView(R.id.send_reservation)
    Button sendReservation;
    Unbinder unbinder;

    private int yearFrom, monthFrom, dayFrom, hourFrom, minuteFrom;
    private int yearTo, monthTo, dayTo, hourTo, minuteTo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reservation, container, false);
//        View view = inflater.inflate(R.layout.fragment_reservation, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (savedInstanceState == null) {


//        Calendar calendar = Calendar.getInstance();
//        Date currentDate = calendar.getTime();
//        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
            DateTime currentDate = new DateTime();
            yearFrom = currentDate.getYear();
            monthFrom = currentDate.getMonthOfYear();
            dayFrom = currentDate.getDayOfMonth();
            hourFrom = currentDate.getHourOfDay();
            minuteFrom = currentDate.getMinuteOfHour();
            yearTo = currentDate.getYear();
            monthTo = currentDate.getMonthOfYear();
            dayTo = currentDate.getDayOfMonth();
            hourTo = currentDate.getHourOfDay();


//        String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday


            datepickerFrom.setText(currentDate.toString(DateTimeFormat.forPattern("YYYY/MM/dd")));
            datepickerTo.setText(currentDate.toString(DateTimeFormat.forPattern("YYYY/MM/dd")));
            timepickerFrom.setText(currentDate.toString(DateTimeFormat.forPattern("HH:mm")));
//        calendar.add(Calendar.MINUTE, 30);
//        minuteTo = calendar.getTime().getMinutes();
//        currentDate.plusMinutes(30);
            timepickerTo.setText(currentDate.plusMinutes(30).toString(DateTimeFormat.forPattern("HH:mm")));
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

//    @OnClick(R.id.datepicker)
//    public void showTimePickerDialog(View v) {
//        DialogFragment newFragment = new TimePickerFragment();
//        newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
//    }

    @OnClick(R.id.send_reservation)
    public void onViewClicked() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        LocalDateTime fromD = new LocalDateTime(yearFrom, monthFrom, dayFrom, hourFrom, minuteFrom);
        LocalDateTime toD = new LocalDateTime(yearTo, monthTo, dayTo, hourTo, minuteTo);
        DateTime fromDT = fromD.toDateTime(DateTimeZone.UTC);
        DateTime toDT = toD.toDateTime(DateTimeZone.UTC);
        long from = fromDT.getMillis() / 1000;
        long to = toDT.getMillis() / 1000;

        Log.i("resetvation:", "93815" + " " +
                user.getEmail() + " "
                + from + " "
                + to);
        ApiHelper
                .INSTANCE
                .postReservation(
                        "93815",
                        user.getEmail(),
                        Long.toString(from),
                        Long.toString(to),
                        new IApiResultListener<ArrayList<ReservationResponse>>() {
                            @Override
                            public void success(ArrayList<ReservationResponse> result) {
                                Snackbar
                                        .make(getActivity().findViewById(android.R.id.content),
                                                "Sikeres foglalás!",
                                                Snackbar.LENGTH_LONG)
                                        .show();
                                removeFragment();
                            }


                            @Override
                            public void error(ErrorResponse errorResponse) {
                                Snackbar
                                        .make(getActivity().findViewById(android.R.id.content),
                                                "Sikeres foglalás!",
                                                Snackbar.LENGTH_LONG)
                                        .show();
                                removeFragment();
                            }

                            @Override
                            public void fail() {
                                Snackbar
                                        .make(getActivity().findViewById(android.R.id.content),
                                                "Sikeres foglalás!",
                                                Snackbar.LENGTH_LONG)
                                        .show();
                                removeFragment();
                            }
                        }
//                        new IApiFinishedListener() {
//                            @Override
//                            public void success() {
//                                Snackbar
//                                        .make(getActivity().findViewById(android.R.id.content),
//                                                "Sikeres foglalás!",
//                                                Snackbar.LENGTH_LONG)
//                                        .show();
//                                final FragmentTransaction ft = getFragmentManager().beginTransaction();
//                                ft.replace(R.id.container, new MapFragment());
//                                ft.commit();
//                            }
                );
    }

    private void removeFragment() {
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, new MapFragment2());
        ft.commit();
    }

    public static Date getDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.ERA, GregorianCalendar.AD);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month + 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    @OnClick({R.id.datepicker_from, R.id.timepicker_from, R.id.datepicker_to, R.id.timepicker_to})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.datepicker_from:
                DatePickerFragment dialogFragmentDateFrom = new DatePickerFragment();
                dialogFragmentDateFrom.show(getActivity().getSupportFragmentManager(), "datePicker");
                dialogFragmentDateFrom.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        datepickerFrom.setText(Integer.toString(year) + "/" + Integer.toString(month+1) + "/" + Integer.toString(dayOfMonth));
                        yearFrom = year;
                        monthFrom = month+1;
                        dayFrom = dayOfMonth;
                    }
                });
                break;
            case R.id.timepicker_from:
                TimePickerFragment dialogFragmentTimeFrom = new TimePickerFragment();
                dialogFragmentTimeFrom.show(getActivity().getSupportFragmentManager(), "timePicker");
                dialogFragmentTimeFrom.setListener(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timepickerFrom.setText(Integer.toString(hourOfDay) + ":" + Integer.toString(minute));
                        hourFrom = hourOfDay;
                        minuteFrom = minute;
                    }
                });
                break;
            case R.id.datepicker_to:
                DatePickerFragment dialogFragmentDateTo = new DatePickerFragment();
                dialogFragmentDateTo.show(getActivity().getSupportFragmentManager(), "datePicker");
                dialogFragmentDateTo.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        datepickerTo.setText(Integer.toString(year) + "/" + Integer.toString(month+1) + "/" + Integer.toString(dayOfMonth));
                        yearTo = year;
                        monthTo = month+1;
                        dayTo = dayOfMonth;
                    }
                });
                break;
            case R.id.timepicker_to:
                TimePickerFragment dialogFragmentTimeTo = new TimePickerFragment();
                dialogFragmentTimeTo.show(getActivity().getSupportFragmentManager(), "timePicker");
                dialogFragmentTimeTo.setListener(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timepickerTo.setText(Integer.toString(hourOfDay) + ":" + Integer.toString(minute));
                        hourTo = hourOfDay;
                        minuteTo = minute;
                    }
                });
                break;
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private DatePickerDialog.OnDateSetListener listener;

        public void setListener(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            listener.onDateSet(view, year, month, day);
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private TimePickerDialog.OnTimeSetListener listener;

        public void setListener(TimePickerDialog.OnTimeSetListener listener) {
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            listener.onTimeSet(view, hourOfDay, minute);
        }
    }

}

