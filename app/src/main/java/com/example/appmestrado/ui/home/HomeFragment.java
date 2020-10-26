package com.example.appmestrado.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.appmestrado.MainActivity;
import com.example.appmestrado.R;
import com.example.appmestrado.ui.gallery.GalleryFragment;
import com.example.appmestrado.MainActivity;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private TextView textInfo;
    private ImageView cardEletricidade;
    private ImageView cardLocalizacao;



    private static MainActivity myContext;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        textInfo = (TextView) root.findViewById(R.id.text_info);
        cardLocalizacao = (ImageView) root.findViewById(R.id.iconLocalizacaoId);
        cardEletricidade =(ImageView) root.findViewById(R.id.imageView2);

        cardLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Clicou", Toast.LENGTH_SHORT).show();

                // Create new fragment and transaction
                Fragment galleryFragment = new GalleryFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.cardLocationId, galleryFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        textInfo.setText("Eletricidade");



//        final TextView textView = root.findViewById(R.id.text_home);
//
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}