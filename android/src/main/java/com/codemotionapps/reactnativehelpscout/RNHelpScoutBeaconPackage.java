package com.codemotionapps.reactnativehelpscout;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.model.ReactModuleInfoProvider;
import com.facebook.react.module.model.ReactModuleInfo;
import com.facebook.react.TurboReactPackage;

import java.util.HashMap;
import java.util.Map;

public class RNHelpScoutBeaconPackage extends TurboReactPackage {

    @Nullable
    @Override
    public NativeModule getModule(String name, ReactApplicationContext reactContext) {
        if (name.equals(RNHelpScoutBeaconModule.NAME)) {
            return new RNHelpScoutBeaconModule(reactContext);
        } else {
            return null;
        }
    }

    @Override
    public ReactModuleInfoProvider getReactModuleInfoProvider() {
        return () -> {
            final Map<String, ReactModuleInfo> moduleInfos = new HashMap<>();
            boolean isTurboModule = true; // Always true for new architecture
            moduleInfos.put(
                    RNHelpScoutBeaconModule.NAME,
                    new ReactModuleInfo(
                            RNHelpScoutBeaconModule.NAME,
                            RNHelpScoutBeaconModule.NAME,
                            false, // canOverrideExistingModule
                            false, // needsEagerInit
                            true, // hasConstants
                            false, // isCxxModule
                            isTurboModule // isTurboModule
                    ));
            return moduleInfos;
        };
    }
}