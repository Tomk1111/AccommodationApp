package ie.ul.accommodationapp;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import ie.ul.accommodationapp.Adapters.PageAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeTabFragment extends Fragment {

    private SearchView searchView;
    private Toolbar mToolbar;
    private PageAdapter pageAdapter;
    private Fragment tab1, tab2;
    private TabLayout tabLayout;

    public HomeTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_tab, container, false);
        tabLayout = view.findViewById(R.id.home_tab_layout);
        mToolbar = getActivity().findViewById(R.id.main_toolbar);
        mToolbar.setTitle("Home");
        mToolbar.setNavigationIcon(null);
        searchView = getActivity().findViewById(R.id.search_view);
        searchView.setVisibility(View.GONE);
        tab1 = new HomeShowFragment();
        tab2 = new HomeListFragment();
        final ViewPager viewPager = view.findViewById(R.id.home_tab_pager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            pageAdapter = new PageAdapter(getChildFragmentManager(), tabLayout.getTabCount(), tab1, tab2);
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        return view;
    }

}
