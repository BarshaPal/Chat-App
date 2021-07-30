package com.example.chatappfinal.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.chatappfinal.Fragments.CallsFragment;
import com.example.chatappfinal.Fragments.ChatFragment;
import com.example.chatappfinal.Fragments.StatusFragment;

public class FragmentAdapter extends FragmentPagerAdapter {


    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
        @Override
        public Fragment getItem ( int position){
            switch (position) {
                case 0:
                    return new ChatFragment();
                case 1:
                    return new StatusFragment();
                case 2:
                    return new CallsFragment();
                default:
                    return new ChatFragment();
            }
        }

        @Override
        public int getCount () {
            return 3;
        }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String t="";
        if(position==0)
        {
            t="CHATS";
        }
        if(position==1)
        {
            t="STATUS";
        }
        if(position==2)
        {
            t="CHATS";
        }
        return t;
    }
}
