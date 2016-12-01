package co.broccli.spacify.Settings;

import android.os.Bundle;
import android.preference.Preference;

import com.google.firebase.auth.FirebaseAuth;

import co.broccli.logic.SpacifyApi;
import co.broccli.spacify.R;

public class SettingsActivity extends com.fnp.materialpreferences.PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setPreferenceFragment(new MyPreferenceFragment());
    }

    public static class MyPreferenceFragment extends com.fnp.materialpreferences.PreferenceFragment
    {
        @Override
        public int addPreferencesFromResource() {
            return R.xml.preferences;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            Preference logout = findPreference("logout");
            logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    FirebaseAuth.getInstance().signOut();
                    SpacifyApi.auth().logout(getActivity());
                    return true;
                }
            });

//            Preference changePassword = findPreference("change_password");
//            changePassword.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//                @Override
//                public boolean onPreferenceChange(Preference preference, Object o) {
//                    return false;
//                }
//            });
        }
    }
}
