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

import it.disi.unitn.lasagna.eventcreation.helpers.LuogoEv;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.EventViewModel;

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
            return switch (provincia) {
                case "Agrigento" -> "AG";
                case "Alessandria" -> "AL";
                case "Ancona" -> "AN";
                case "Aosta" -> "AO";
                case "Arezzo" -> "AR";
                case "Ascoli Piceno" -> "AP";
                case "Asti" -> "AT";
                case "Avellino" -> "AV";
                case "Bari" -> "BA";
                case "Barletta - Andria - Trani" -> "BT";
                case "Belluno" -> "BL";
                case "Benevento" -> "BN";
                case "Bergamo" -> "BG";
                case "Biella" -> "BI";
                case "Bologna" -> "BO";
                case "Bolzano" -> "BZ";
                case "Brescia" -> "BS";
                case "Brindisi" -> "BR";
                case "Cagliari" -> "CA";
                case "Caltanissetta" -> "CL";
                case "Campobasso" -> "CB";
                case "Carbonia-Iglesias" -> "CI";
                case "Caserta" -> "CE";
                case "Catania" -> "CT";
                case "Catanzaro" -> "CZ";
                case "Chieti" -> "CH";
                case "Como" -> "CO";
                case "Cosenza" -> "CS";
                case "Cremona" -> "CR";
                case "Crotone" -> "KR";
                case "Cuneo" -> "CN";
                case "Enna" -> "EN";
                case "Fermo" -> "FM";
                case "Ferrara" -> "FE";
                case "Firenze" -> "FI";
                case "Foggia" -> "FG";
                case "Forlì-Cesena" -> "FC";
                case "Frosinone" -> "FR";
                case "Genova" -> "GE";
                case "Gorizia" -> "GO";
                case "Grosseto" -> "GR";
                case "Imperia" -> "IM";
                case "Isernia" -> "IS";
                case "L'Aquila" -> "AQ";
                case "La Spezia" -> "SP";
                case "Latina" -> "LT";
                case "Lecce" -> "LE";
                case "Livorno" -> "LI";
                case "Lodi" -> "LO";
                case "Lucca" -> "LU";
                case "Macerata" -> "MC";
                case "Mantova" -> "MN";
                case "Massa e Carrara" -> "MS";
                case "Matera" -> "MT";
                case "Medio Campidano" -> "VS";
                case "Messina" -> "ME";
                case "Milano" -> "MI";
                case "Modena" -> "MO";
                case "Monza e Brianza" -> "MB";
                case "Napoli" -> "NA";
                case "Novara" -> "NO";
                case "Nuoro" -> "NU";
                case "Ogliastra" -> "OG";
                case "Olbia-Tempio" -> "OT";
                case "Oristano" -> "OR";
                case "Padova" -> "PD";
                case "Palermo" -> "PA";
                case "Parma" -> "PR";
                case "Pavia" -> "PV";
                case "Perugia" -> "PG";
                case "Pesaro e Urbino" -> "PU";
                case "Pescara" -> "PE";
                case "Piacenza" -> "PC";
                case "Pisa" -> "PI";
                case "Pistoia" -> "PT";
                case "Pordenone" -> "PN";
                case "Potenza" -> "PZ";
                case "Prato" -> "PO";
                case "Ragusa" -> "RG";
                case "Ravenna" -> "RA";
                case "Reggio Calabria" -> "RC";
                case "Reggio Emilia" -> "RE";
                case "Rieti" -> "RI";
                case "Rimini" -> "RN";
                case "Roma" -> "RM";
                case "Rovigo" -> "RO";
                case "Salerno" -> "SA";
                case "Sassari" -> "SS";
                case "Savona" -> "SV";
                case "Siena" -> "SI";
                case "Siracusa" -> "SR";
                case "Sondrio" -> "SO";
                case "Sud Sardegna" -> "SU";
                case "Taranto" -> "TA";
                case "Teramo" -> "TE";
                case "Terni" -> "TR";
                case "Torino" -> "TO";
                case "Trapani" -> "TP";
                case "Trento" -> "TN";
                case "Treviso" -> "TV";
                case "Trieste" -> "TS";
                case "Udine" -> "UD";
                case "Varese" -> "VA";
                case "Venezia" -> "VE";
                case "Verbano-Cusio-Ossola" -> "VB";
                case "Vercelli" -> "VC";
                case "Verona" -> "VR";
                case "Vibo Valentia" -> "VV";
                case "Vicenza" -> "VI";
                case "Viterbo" -> "VT";
                default -> "Nessuna provincia italiana nota con quel nome.";
            };
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
        Geocoder geocoder = new Geocoder(f.requireContext());

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