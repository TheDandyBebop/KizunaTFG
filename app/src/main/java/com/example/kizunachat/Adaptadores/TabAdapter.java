package com.example.kizunachat.Adaptadores;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

/*
 * Esta clase es el adaptador encargado de gestionar las ventanas dentro del TabLayout
 */

public class TabAdapter extends FragmentStateAdapter {
    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    // Constructor de la clase
    public TabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    // Método para crear un fragmento en una posición dada
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    // Método para agregar un fragmento a la lista de fragmentos
    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
    }

    // Método para obtener la cantidad total de fragmentos
    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
