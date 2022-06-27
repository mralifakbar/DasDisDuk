package id.coolva.dasdisduk.data.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreference {
    private static final String PREFS_NAME = "user_pref";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    private final SharedPreferences preferences;

    public UserPreference(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setUser(UserModel value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PASSWORD, value.password);
        editor.putString(EMAIL, value.email);
        editor.apply();
    }

    public UserModel getUser() {
        UserModel model = new UserModel();
        model.setEmail(preferences.getString(EMAIL, ""));
        model.setPassword(preferences.getString(PASSWORD, ""));
        return model;
    }
}
