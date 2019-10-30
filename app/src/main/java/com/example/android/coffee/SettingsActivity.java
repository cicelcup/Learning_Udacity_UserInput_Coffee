package com.example.android.coffee;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class CoffeePreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

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
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            preference.setSummary(stringValue);
            return true;
        }

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
}
