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

import com.helpscout.beacon.Beacon;
import com.helpscout.beacon.model.BeaconUser;
import com.helpscout.beacon.ui.BeaconActivity;
import com.helpscout.beacon.ui.BeaconOnClosedListener;
import com.helpscout.beacon.ui.BeaconOnOpenedListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@ReactModule(name = RNHelpScoutBeaconModule.NAME)
public class RNHelpScoutBeaconModule extends NativeRNHelpScoutBeaconSpec implements BeaconOnOpenedListener, BeaconOnClosedListener {
    
    public static final String NAME = "RNHelpScoutBeacon";
    private ReactApplicationContext reactContext;
    private String formSubject;
    private String formText;

    public RNHelpScoutBeaconModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public void init(String beaconId) {
        Beacon.Builder builder = new Beacon.Builder()
                .withBeaconId(beaconId)
                .withLogsEnabled(false);
        
        Beacon beacon = builder.build();
        Beacon.setBeacon(beacon);
        
        // Set listeners
        BeaconActivity.setOnOpenedListener(this);
        BeaconActivity.setOnClosedListener(this);
    }

    @Override
    public void open(@Nullable String signature) {
        if (getCurrentActivity() != null) {
            if (signature != null && !signature.isEmpty()) {
                BeaconActivity.openWithSignature(getCurrentActivity(), signature);
            } else {
                BeaconActivity.open(getCurrentActivity());
            }
        }
    }

    @Override
    public void identify(ReadableMap identity) {
        BeaconUser.Builder builder = new BeaconUser.Builder();
        
        if (identity.hasKey("email") && !identity.isNull("email")) {
            builder.withEmail(identity.getString("email"));
        }
        
        if (identity.hasKey("name") && !identity.isNull("name")) {
            builder.withName(identity.getString("name"));
        }
        
        // Add custom attributes
        Iterator<Map.Entry<String, Object>> iterator = identity.getEntryIterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            if (!"email".equals(key) && !"name".equals(key)) {
                Object value = entry.getValue();
                if (value != null) {
                    builder.withAttribute(key, value.toString());
                }
            }
        }
        
        BeaconUser user = builder.build();
        Beacon.login(user);
    }

    @Override
    public void logout() {
        Beacon.logout();
    }

    @Override
    public void navigate(String route) {
        if (getCurrentActivity() != null) {
            BeaconActivity.openWithRoute(getCurrentActivity(), route);
        }
    }

    @Override
    public void search(String query) {
        if (getCurrentActivity() != null) {
            BeaconActivity.openWithSearch(getCurrentActivity(), query);
        }
    }

    @Override
    public void openArticle(String articleId) {
        if (getCurrentActivity() != null) {
            BeaconActivity.openArticle(getCurrentActivity(), articleId);
        }
    }

    @Override
    public void contactForm() {
        if (getCurrentActivity() != null) {
            BeaconActivity.openWithRoute(getCurrentActivity(), "/ask/message/");
        }
    }

    @Override
    public void previousMessages() {
        if (getCurrentActivity() != null) {
            BeaconActivity.openWithRoute(getCurrentActivity(), "/previous-messages/");
        }
    }

    @Override
    public void dismiss(Promise promise) {
        BeaconActivity.dismiss();
        promise.resolve(null);
    }

    @Override
    public void prefillForm(String subject, String content) {
        this.formSubject = subject;
        this.formText = content;
    }

    @Override
    public void clearFormPrefill() {
        this.formSubject = null;
        this.formText = null;
    }

    @Override
    public void addListener(String eventName) {
        // Required for RCTEventEmitter
    }

    @Override
    public void removeListeners(double count) {
        // Required for RCTEventEmitter
    }

    // BeaconOnOpenedListener
    @Override
    public void onOpened() {
        sendEvent("open", null);
    }

    // BeaconOnClosedListener
    @Override
    public void onClosed() {
        clearFormPrefill();
        Beacon.clear();
        sendEvent("close", null);
    }

    private void sendEvent(String eventName, @Nullable WritableMap params) {
        if (reactContext.hasActiveCatalystInstance()) {
            reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
        }
    }
}