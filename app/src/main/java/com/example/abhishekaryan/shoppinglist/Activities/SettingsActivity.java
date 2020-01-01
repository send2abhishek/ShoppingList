package com.example.abhishekaryan.shoppinglist.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import com.example.abhishekaryan.shoppinglist.Infrastructure.Utils;
import com.example.abhishekaryan.shoppinglist.R;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content,new SortPrefernceFragment()).commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public static class SortPrefernceFragment extends PreferenceFragment implements
            Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefences);

            //Extracting the key from the prefernce
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_sort_list_name)));


        }


        private void bindPreferenceSummaryToValue(Preference preference){

            preference.setOnPreferenceChangeListener(this);
            setPreferenceSummary(preference,PreferenceManager.getDefaultSharedPreferences(preference.getContext())

                    .getString(preference.getKey(),""));
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            setPreferenceSummary(preference,newValue);
            SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getActivity());

            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString(Utils.LIST_ORDER_PREFERENCES,newValue.toString()).apply();

            return true;
        }


        private void setPreferenceSummary(Preference preference,Object value){

            String stringValue=value.toString();

            if(preference instanceof ListPreference){
                ListPreference listPreference=(ListPreference)preference;

                int prefernceIndex=listPreference.findIndexOfValue(stringValue);

                if(prefernceIndex >=0){

                    preference.setSummary(listPreference.getEntries()[prefernceIndex]);
                }
            }

        }
    }
}
