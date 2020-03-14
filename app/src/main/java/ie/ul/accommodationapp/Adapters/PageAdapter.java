package ie.ul.accommodationapp.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentStatePagerAdapter {

    private int count;
    private Fragment fragment1;
    private Fragment fragment2;

    public PageAdapter(@NonNull FragmentManager fm, int count, Fragment fragment1, Fragment fragment2) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.count = count;
        this.fragment1 = fragment1;
        this.fragment2 = fragment2;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return fragment1;
            case 1:
                return fragment2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}
