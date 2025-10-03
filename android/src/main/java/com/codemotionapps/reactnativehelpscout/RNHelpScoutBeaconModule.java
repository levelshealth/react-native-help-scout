package com.codemotionapps.reactnativehelpscout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.module.annotations.ReactModule;

import android.app.Application;

import com.helpscout.beacon.Beacon;
import com.helpscout.beacon.model.BeaconScreens;
import com.helpscout.beacon.model.PreFilledForm;
import com.helpscout.beacon.ui.BeaconActivity;
import com.helpscout.beacon.ui.BeaconEventLifecycleHandler;
import com.helpscout.beacon.ui.BeaconOnClosedListener;
import com.helpscout.beacon.ui.BeaconOnOpenedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@ReactModule(name = RNHelpScoutBeaconModule.NAME)
public class RNHelpScoutBeaconModule extends NativeRNHelpScoutBeaconSpec {
    
    public static final String NAME = "RNHelpScoutBeacon";
    private ReactApplicationContext reactContext;
    private String formSubject;
    private String formText;
    private String userEmail;
    private String userName;

    public RNHelpScoutBeaconModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        
        // Set up event lifecycle handler for open/close events
        BeaconEventLifecycleHandler eventLifecycleHandler = new BeaconEventLifecycleHandler(
            new BeaconOnOpenedListener() {
                @Override
                public void onOpened() {
                    sendEvent("open", null);
                }
            },
            new BeaconOnClosedListener() {
                @Override
                public void onClosed() {
                    clearFormPrefill();
                    Beacon.clear();
                    sendEvent("close", null);
                }
            }
        );
        
        Application application = (Application) reactContext.getApplicationContext();
        application.registerActivityLifecycleCallbacks(eventLifecycleHandler);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public void init(String beaconId) {
        new Beacon.Builder()
                .withBeaconId(beaconId)
                .build();
    }

    @Override
    public void open(@Nullable String signature) {
        if (signature != null && !signature.isEmpty()) {
            BeaconActivity.openInSecureMode(reactContext, signature);
        } else {
            BeaconActivity.open(reactContext);
        }
    }

    @Override
    public void identify(ReadableMap identity) {
        this.userEmail = identity.hasKey("email") ? identity.getString("email") : "";
        
        if (identity.hasKey("name")) {
            this.userName = identity.getString("name");
            Beacon.identify(this.userEmail, this.userName);
        } else {
            Beacon.identify(this.userEmail);
        }
        
        // Add custom attributes
        Iterator<Map.Entry<String, Object>> iterator = identity.getEntryIterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            if (!"email".equals(key) && !"name".equals(key)) {
                Object value = entry.getValue();
                if (value != null) {
                    Beacon.addAttributeWithKey(key, value.toString());
                }
            }
        }
    }

    @Override
    public void logout() {
        Beacon.logout();
    }

    @Override
    public void navigate(String route) {
        // Navigate is a no-op on Android - routes handled by specific methods
    }

    @Override
    public void search(String query) {
        ArrayList<String> list = new ArrayList<String>();
        list.add(query);
        BeaconActivity.open(reactContext, BeaconScreens.SEARCH_SCREEN, list);
    }

    @Override
    public void openArticle(String articleId) {
        ArrayList<String> list = new ArrayList<String>();
        list.add(articleId);
        BeaconActivity.open(reactContext, BeaconScreens.ARTICLE_SCREEN, list);
    }

    @Override
    public void contactForm() {
        BeaconActivity.open(reactContext, BeaconScreens.CONTACT_FORM_SCREEN, new ArrayList<String>());
    }

    @Override
    public void previousMessages() {
        BeaconActivity.open(reactContext, BeaconScreens.PREVIOUS_MESSAGES, new ArrayList<String>());
    }

    @Override
    public void dismiss(Promise promise) {
        // Dismiss is a no-op on Android - user dismisses manually
        promise.resolve(null);
    }

    @Override
    public void prefillForm(String subject, String content) {
        this.formSubject = subject;
        this.formText = content;
        Beacon.addPreFilledForm(new PreFilledForm(
            this.userName,
            subject,
            content,
            Collections.<Integer, String>emptyMap(),
            Collections.<String>emptyList(),
            this.userEmail
        ));
    }

    @Override
    public void clearFormPrefill() {
        this.formSubject = null;
        this.formText = null;
        Beacon.contactFormReset();
    }

    @Override
    public void addListener(String eventName) {
        // Required for RCTEventEmitter
    }

    @Override
    public void removeListeners(double count) {
        // Required for RCTEventEmitter
    }

    private void sendEvent(String eventName, @Nullable WritableMap params) {
        if (reactContext.hasActiveCatalystInstance()) {
            reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
        }
    }
}