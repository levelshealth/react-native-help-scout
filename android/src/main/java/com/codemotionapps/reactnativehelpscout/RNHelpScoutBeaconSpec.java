package com.codemotionapps.reactnativehelpscout;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;

abstract class NativeRNHelpScoutBeaconSpec extends ReactContextBaseJavaModule {
    public static final String NAME = "RNHelpScoutBeacon";

    public NativeRNHelpScoutBeaconSpec(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public abstract void init(String beaconId);

    @ReactMethod
    public abstract void open(String signature);

    @ReactMethod
    public abstract void identify(ReadableMap identity);

    @ReactMethod
    public abstract void logout();

    @ReactMethod
    public abstract void navigate(String route);

    @ReactMethod
    public abstract void search(String query);

    @ReactMethod
    public abstract void openArticle(String articleId);

    @ReactMethod
    public abstract void contactForm();

    @ReactMethod
    public abstract void previousMessages();

    @ReactMethod
    public abstract void dismiss(Promise promise);

    @ReactMethod
    public abstract void prefillForm(String subject, String content);

    @ReactMethod
    public abstract void clearFormPrefill();

    @ReactMethod
    public abstract void addListener(String eventName);

    @ReactMethod
    public abstract void removeListeners(double count);
}