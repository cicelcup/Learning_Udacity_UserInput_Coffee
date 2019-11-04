package com.example.android.coffee;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class PreferenceFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceChangeListener {

//    add the line between the recycler views in the preference screen
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        get the recycler view from the view created into the fragment
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
//        add the item decoration
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(),
                DividerItemDecoration.VERTICAL));
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
//        add the layout for the preference screen
        addPreferencesFromResource(R.xml.settings_main);

        //setting the summary values to the preferences
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

    //Setting the value of the preference according the option chose by the user
//        override of the method in the interface
    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {

        if(preference.getKey().equals(getString(R.string.email_key))){
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(value.toString()).matches()){
                Toast.makeText(preference.getContext(), R.string.add_email_value,
                        Toast.LENGTH_LONG).show();
                return false;
            }
        }
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



