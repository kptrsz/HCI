package ptr.hf.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ptr.hf.R;
import ptr.hf.model.ReservationResponse;
import ptr.hf.network.ApiHelper;
import ptr.hf.network.ErrorResponse;
import ptr.hf.network.IApiResultListener;

public class MyReservationFragment extends Fragment {
    @BindView(R.id.act)
    TextView act;
    @BindView(R.id.past1)
    TextView past1;
    @BindView(R.id.past2)
    TextView past2;
    @BindView(R.id.past3)
    TextView past3;
    @BindView(R.id.past4)
    TextView past4;
    Unbinder unbinder;
    ReservationResponse reservationResponse;
    @BindView(R.id.delete_reservation)
    ImageView deleteReservation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_reservations, container, false);

        unbinder = ButterKnife.bind(this, view);
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        ApiHelper.INSTANCE.getReservation(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString(), new IApiResultListener<ReservationResponse>() {
            @Override
            public void success(ReservationResponse result) {
//                Calendar calendar = Calendar.getInstance();
                reservationResponse = result;
//                Date date = calendar.getTime();
                if (result.getData().size() > 4) {
                    DateTimeFormatter dtf = DateTimeFormat.forPattern("YYYY/MM/dd HH:mm");
                    DateTime dateTime = new DateTime();
                    DateTime lastReservation = new DateTime(new Long(result.getData().get(result.getData().size() - 1).getTo()) * 1000L);

                    if (dateTime.isBefore(lastReservation)) {
                        deleteReservation.setVisibility(View.VISIBLE);
                        act.setText(new DateTime(new Long(result.getData().get(result.getData().size() - 1).getFrom()) * 1000L).toString(dtf) + " - " + new DateTime(new Long(result.getData().get(result.getData().size() - 1).getTo()) * 1000L).toString(dtf));
                    }
                    if (result.getData().size() > 4) {
                        past1.setText(new DateTime(new Long(result.getData().get(1).getFrom()) * 1000L).toString(dtf) + " - " + new DateTime(new Long(result.getData().get(1).getFrom()) * 1000L).toString(dtf));
                        past2.setText(new DateTime(new Long(result.getData().get(2).getFrom()) * 1000L).toString(dtf) + " - " + new DateTime(new Long(result.getData().get(2).getFrom()) * 1000L).toString(dtf));
                        past3.setText(new DateTime(new Long(result.getData().get(3).getFrom()) * 1000L).toString(dtf) + " - " + new DateTime(new Long(result.getData().get(3).getFrom()) * 1000L).toString(dtf));
                        past4.setText(new DateTime(new Long(result.getData().get(4).getFrom()) * 1000L).toString(dtf) + " - " + new DateTime(new Long(result.getData().get(4).getFrom()) * 1000L).toString(dtf));
                    }
                }
            }

            @Override
            public void error(ErrorResponse errorResponse) {

            }

            @Override
            public void fail() {

            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.delete_reservation)
    public void onViewClicked() {
        if (reservationResponse != null)
            ApiHelper.INSTANCE.deleteReservation(reservationResponse.getData().get(reservationResponse.getData().size() - 1).getId(), new IApiResultListener<ReservationResponse>() {
                @Override
                public void success(ReservationResponse result) {
//                    if (result.getSuccess()) {

                    act.setText("Nincs aktuális foglalás!");
                    deleteReservation.setVisibility(View.GONE);
                            Snackbar
                                .make(getActivity().findViewById(android.R.id.content),
                            "Sikeresen törölte a foglalást!",
                            Snackbar.LENGTH_LONG)
                            .show();
                    FragmentTransaction tr = getFragmentManager().beginTransaction();
                    tr.replace(R.id.container, new MyReservationFragment());
                    tr.commit();
//                    } else
//                        fail();
                }

                @Override
                public void error(ErrorResponse errorResponse) {
                    Snackbar
                            .make(getActivity().findViewById(android.R.id.content),
                                    "Hiba lépett fel a regisztráció során." +
                                            "\nKérem próbálja meg később!",
                                    Snackbar.LENGTH_LONG)
                            .setAction("ÚJRA", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    onViewClicked();
                                }
                            }).show();
                }

                @Override
                public void fail() {
                    Snackbar
                            .make(getActivity().findViewById(android.R.id.content),
                                    "Hiba lépett fel a regisztráció során." +
                                            "\nKérem próbálja meg később!",
                                    Snackbar.LENGTH_LONG)
                            .setAction("ÚJRA", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    onViewClicked();
                                }
                            }).show();
                }
            });
    }
}
