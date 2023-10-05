package in.co.vibrant.bindalsugar.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import in.co.vibrant.bindalsugar.view.Fragment.Done;
import in.co.vibrant.bindalsugar.view.Fragment.Pending;

public class TabAdapter extends FragmentPagerAdapter {
    public TabAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0)
        {
            return "Pending";
        }if (position==1)
        {
            return "Complete";
        }


        else {
            return "Complete";
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position==0)
        {
            return new Pending();
        }
        if (position==1)
        {
            return new Done();
        }
        else {
            return new Done();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
