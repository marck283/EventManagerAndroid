package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate;

import android.app.Activity;
import android.app.AlertDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.fragment.NavHostFragment;

import java.io.IOException;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.EventViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.LuogoEv;

public class EventLocationViewModel extends ViewModel {
    private EventLocationFragment f;
    private String provincia;
    private final MutableLiveData<Boolean> ok;

    public EventLocationViewModel() {
        ok = new MutableLiveData<>(false);
    }

    public void setDialogFragment(EventLocationFragment f) {
        this.f = f;
    }

    private void setAlertDialog(int title, String message) {
        AlertDialog ad = new AlertDialog.Builder(f.requireContext()).create();
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
        ad.show();
    }

    private void setAddress(@NonNull List<Address> addresses, LuogoEv luogo, EventViewModel evm) {
        int i = 0;

        //Esempio indirizzo non riconosciuto: Vicolo (Giorgio) Tebaldeo, 3, 27036 Mortara PV. Perché?
        for (Address a : addresses) {
            Log.i("addresses", addresses.toString());
            String indirizzo = a.getAddressLine(i);
            if (indirizzo != null && indirizzo.contains(luogo.getAddress())) {
                ok.postValue(true);
                evm.setLuogoEv(luogo);

                Activity activity = f.getActivity();
                if(activity != null && f.isAdded()) {
                    f.requireActivity().runOnUiThread(() ->
                            NavHostFragment.findNavController(f).navigate(R.id.action_eventLocationFragment_to_SecondFragment));
                }
                break;
            } else {
                ++i;
                if(i == addresses.size()) {
                    Activity activity = f.getActivity();
                    if(activity != null && f.isAdded()) {
                        f.requireActivity().runOnUiThread(() ->
                                setAlertDialog(R.string.incorrect_location_format_title, f.getString(R.string.incorrect_location_format)));
                    }
                }
            }
        }
    }

    private String parseProvince() {
        if (provincia != null && !provincia.equals("")) {
            switch (provincia) {
                case "Agrigento":
                    return "AG";
                case "Alessandria":
                    return "AL";
                case "Ancona":
                    return "AN";
                case "Aosta":
                    return "AO";
                case "Arezzo":
                    return "AR";
                case "Ascoli Piceno":
                    return "AP";
                case "Asti":
                    return "AT";
                case "Avellino":
                    return "AV";
                case "Bari":
                    return "BA";
                case "Barletta - Andria - Trani":
                    return "BT";
                case "Belluno":
                    return "BL";
                case "Benevento":
                    return "BN";
                case "Bergamo":
                    return "BG";
                case "Biella":
                    return "BI";
                case "Bologna":
                    return "BO";
                case "Bolzano":
                    return "BZ";
                case "Brescia":
                    return "BS";
                case "Brindisi":
                    return "BR";
                case "Cagliari":
                    return "CA";
                case "Caltanissetta":
                    return "CL";
                case "Campobasso":
                    return "CB";
                case "Carbonia-Iglesias":
                    return "CI";
                case "Caserta":
                    return "CE";
                case "Catania":
                    return "CT";
                case "Catanzaro":
                    return "CZ";
                case "Chieti":
                    return "CH";
                case "Como":
                    return "CO";
                case "Cosenza":
                    return "CS";
                case "Cremona":
                    return "CR";
                case "Crotone":
                    return "KR";
                case "Cuneo":
                    return "CN";
                case "Enna":
                    return "EN";
                case "Fermo":
                    return "FM";
                case "Ferrara":
                    return "FE";
                case "Firenze":
                    return "FI";
                case "Foggia":
                    return "FG";
                case "Forlì-Cesena":
                    return "FC";
                case "Frosinone":
                    return "FR";
                case "Genova":
                    return "GE";
                case "Gorizia":
                    return "GO";
                case "Grosseto":
                    return "GR";
                case "Imperia":
                    return "IM";
                case "Isernia":
                    return "IS";
                case "L'Aquila":
                    return "AQ";
                case "La Spezia":
                    return "SP";
                case "Latina":
                    return "LT";
                case "Lecce":
                    return "LE";
                case "Livorno":
                    return "LI";
                case "Lodi":
                    return "LO";
                case "Lucca":
                    return "LU";
                case "Macerata":
                    return "MC";
                case "Mantova":
                    return "MN";
                case "Massa e Carrara":
                    return "MS";
                case "Matera":
                    return "MT";
                case "Medio Campidano":
                    return "VS";
                case "Messina":
                    return "ME";
                case "Milano":
                    return "MI";
                case "Modena":
                    return "MO";
                case "Monza e Brianza":
                    return "MB";
                case "Napoli":
                    return "NA";
                case "Novara":
                    return "NO";
                case "Nuoro":
                    return "NU";
                case "Ogliastra":
                    return "OG";
                case "Olbia-Tempio":
                    return "OT";
                case "Oristano":
                    return "OR";
                case "Padova":
                    return "PD";
                case "Palermo":
                    return "PA";
                case "Parma":
                    return "PR";
                case "Pavia":
                    return "PV";
                case "Perugia":
                    return "PG";
                case "Pesaro e Urbino":
                    return "PU";
                case "Pescara":
                    return "PE";
                case "Piacenza":
                    return "PC";
                case "Pisa":
                    return "PI";
                case "Pistoia":
                    return "PT";
                case "Pordenone":
                    return "PN";
                case "Potenza":
                    return "PZ";
                case "Prato":
                    return "PO";
                case "Ragusa":
                    return "RG";
                case "Ravenna":
                    return "RA";
                case "Reggio Calabria":
                    return "RC";
                case "Reggio Emilia":
                    return "RE";
                case "Rieti":
                    return "RI";
                case "Rimini":
                    return "RN";
                case "Roma":
                    return "RM";
                case "Rovigo":
                    return "RO";
                case "Salerno":
                    return "SA";
                case "Sassari":
                    return "SS";
                case "Savona":
                    return "SV";
                case "Siena":
                    return "SI";
                case "Siracusa":
                    return "SR";
                case "Sondrio":
                    return "SO";
                case "Sud Sardegna":
                    return "SU";
                case "Taranto":
                    return "TA";
                case "Teramo":
                    return "TE";
                case "Terni":
                    return "TR";
                case "Torino":
                    return "TO";
                case "Trapani":
                    return "TP";
                case "Trento":
                    return "TN";
                case "Treviso":
                    return "TV";
                case "Trieste":
                    return "TS";
                case "Udine":
                    return "UD";
                case "Varese":
                    return "VA";
                case "Venezia":
                    return "VE";
                case "Verbano-Cusio-Ossola":
                    return "VB";
                case "Vercelli":
                    return "VC";
                case "Verona":
                    return "VR";
                case "Vibo Valentia":
                    return "VV";
                case "Vicenza":
                    return "VI";
                case "Viterbo":
                    return "VT";
                default:
                    return "Nessuna provincia italiana nota con quel nome.";
            }
        }
        return provincia;
    }

    public void setProvincia(String val) {
        provincia = val;
    }

    public LiveData<Boolean> getOk() {
        return ok;
    }

    public void parseAddress(boolean priv, @NonNull EditText t2, @NonNull EditText t3, @NonNull EditText t4,
                                @NonNull EditText t5, @NonNull EventViewModel evm, @NonNull NewDateViewModel ndvm) {
        if (provincia == null || provincia.equals("")) {
            setAlertDialog(R.string.incorrect_province_format_title, f.getString(R.string.incorrect_province_format));
            return;
        }

        String location = t2.getText().toString().trim() + ", " + t3.getText().toString().trim() + ", "
                + t4.getText().toString().trim() + ", " + t5.getText().toString().trim() + ", " + parseProvince().trim();
        Geocoder geocoder = new Geocoder(f.getContext());

        try {
            String[] split = location.split(", ");
            LuogoEv luogo;

            if(!priv) {
                luogo = new LuogoEv(split[0], split[1], Integer.parseInt(split[3]), split[2],
                        split[4], ndvm.getPosti(), ndvm.getData(), ndvm.getOra(), 0, false);
            } else {
                luogo = new LuogoEv(split[0], split[1], Integer.parseInt(split[3]), split[2],
                        split[4], 0, ndvm.getData(), ndvm.getOra(), 0, false);
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocationName(location, 5, addresses -> setAddress(addresses, luogo, evm));
            } else {
                Thread t1 = new Thread() {
                    @Override
                    public void run() {
                        List<Address> addresses;
                        try {
                            //Exception: only one Looper may be created per thread
                            addresses = geocoder.getFromLocationName(location, 5);
                            if (addresses != null && !addresses.isEmpty()) {
                                setAddress(addresses, luogo, evm);
                            } else {
                                Activity activity = f.getActivity();
                                if(activity != null && f.isAdded()) {
                                    f.requireActivity().runOnUiThread(() -> setAlertDialog(R.string.no_location_result_title, f.getString(R.string.no_location_result)));
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                t1.start();
            }
        } catch (NumberFormatException ex) {
            setAlertDialog(R.string.incorrect_location_format_title, f.getString(R.string.incorrect_location_format));
        }
    }
}