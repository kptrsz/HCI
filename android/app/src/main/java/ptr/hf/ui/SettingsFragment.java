package ptr.hf.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ptr.hf.R;
import ptr.hf.ui.map.MapFragment;

public class SettingsFragment extends Fragment {

    //    @BindView(R.id.spinner_connectors)
//    Spinner spinnerConnectors;
    Unbinder unbinder;
    @BindView(R.id.chargers)
    MaterialSpinner chargers;
    @BindView(R.id.distance)
    TextView distance;
    @BindView(R.id.chargers2)
    MaterialSpinner chargers2;
    @BindView(R.id.chargers3)
    MaterialSpinner chargers3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.settings_in_pager, container, false);
//        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_connectors);
        ButterKnife.bind(this, view);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.list_connectors, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);


        chargers.setItems("CHAdeMO ", "CCS ", "CATARC", "COMBO-1", "COMBO-2");
//        distance.setItems("100-150km", "150-200km", "200-250km", "250-300km", "300+ km");
        chargers2.setItems("Nissan", "Tesla", "BMW", "Volkswagen", "egyéb");
        chargers3.setItems("2011", "2012", "2013", "2014", "2015", "2016", "2017");


        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.send_reservation)
    public void onViewClicked() {
        Snackbar
                .make(getActivity().findViewById(android.R.id.content),
                        "Adatok sikeresen elmentve!",
                        Snackbar.LENGTH_LONG)
                .show();
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, new MapFragment());
        ft.commit();
    }
}