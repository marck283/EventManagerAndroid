package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.FragmentEventListBinding;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.PublicEvents;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.publicEvent.PublicEvent;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.views.cards.CardView;

public class EventListFragment extends Fragment {

    private FragmentEventListBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EventListViewModel eventListViewModel =
                new ViewModelProvider(this).get(EventListViewModel.class);

        binding = FragmentEventListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        eventListViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ConstraintLayout layout = (ConstraintLayout)view.findViewById(R.id.eventListLayout);
        PublicEvents pubEv = new PublicEvents();
        String token = null, nomeAtt = null, categoria = null, durata = null, indirizzo = null,
        citta = null;
        if (savedInstanceState != null) {
            token = savedInstanceState.getString("token");
            nomeAtt = savedInstanceState.getString("nomeAtt");
            categoria = savedInstanceState.getString("categoria");
            durata = savedInstanceState.getString("durata");
            indirizzo = savedInstanceState.getString("indirizzo");
            citta = savedInstanceState.getString("citta");
        }
        for(PublicEvent j: pubEv.getEvents(token, nomeAtt, categoria, durata, indirizzo, citta).getList()) {
            try {
                //Do not test until events are modified with the possibility of creating them with an image
                //Now the problem is: we downloaded the informations about events, but how do we show them?
                CardView card = new CardView(this.requireContext(), j.getString("eventPic"), j.getString("nomeAtt"),
                        j.getString("categoria"), j.getString("durata"), j.getString("indirizzo"),
                        j.getString("citta"));
                card.setBody();
                layout.addView(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}