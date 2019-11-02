package com.example.android.coffee;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

public class PreferenceFragment extends PreferenceFragmentCompat{
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.settings_main);
    }
}

/*
//    inner class to implement the fragment
public static class CoffeePreferenceFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//            adding the fragment to the activity
        addPreferencesFromResource(R.xml.settings_main);

//            setting the summary values to the preferences
        Preference coffee_price =
                findPreference(getString(R.string.coffee_price_key));
        bindPreferenceSummaryToValue(coffee_price);

        Preference whipped_cream_price =
                findPreference(getString(R.string.whipped_cream_price_key));
        bindPreferenceSummaryToValue(whipped_cream_price);

        Preference chocolate_price =
                findPreference(getString(R.string.chocolate_price_key));
        bindPreferenceSummaryToValue(chocolate_price);

        Preference email =
                findPreference(getString(R.string.email_key));
        bindPreferenceSummaryToValue(email);

    }

    @Override
    //Setting the value of the preference according the option chose by the user
//        override of the method in the interface
    public boolean onPreferenceChange(Preference preference, Object value) {

        if (value.toString().length() != 0) {
            String stringValue = value.toString();
            preference.setSummary(stringValue);
            return true;
        }
        else {
            Toast.makeText(preference.getContext(), R.string.No_preference_value,
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }

    //        setting the preference listener and change the value in the preference screen
    private void bindPreferenceSummaryToValue(Preference preference) {
        //Set on preference change listener to verify that the preference is observed
        preference.setOnPreferenceChangeListener(this);
        //Get the preference
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(preference.getContext());
        //Get the value
        String preferenceString = preferences.getString(preference.getKey(), "");
        //Setting the value
        assert preferenceString != null;
        onPreferenceChange(preference, preferenceString);
    }
}
*/

