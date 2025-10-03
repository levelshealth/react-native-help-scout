package com.codemotionapps.reactnativehelpscout;

import android.app.Application;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.helpscout.beacon.Beacon;
import com.helpscout.beacon.model.BeaconScreens;
import com.helpscout.beacon.model.PreFilledForm;
import com.helpscout.beacon.ui.BeaconActivity;
import com.helpscout.beacon.ui.BeaconEventLifecycleHandler;
import com.helpscout.beacon.ui.BeaconOnClosedListener;
import com.helpscout.beacon.ui.BeaconOnOpenedListener;

import java.lang.Exception;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

public class HelpScoutModule extends ReactContextBaseJavaModule {
	private final ReactApplicationContext reactContext;

	private String userEmail;
	private String userName;

	public HelpScoutModule(final ReactApplicationContext reactContext) {
		super(reactContext);
		this.reactContext = reactContext;

		BeaconEventLifecycleHandler eventLifecycleHandler = new BeaconEventLifecycleHandler(
				new BeaconOnOpenedListener() {
					@Override
					public void onOpened() {
						reactContext
								.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
								.emit("open", null);
					}
				},
				new BeaconOnClosedListener() {
					@Override
					public void onClosed() {
						reactContext
								.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
								.emit("close", null);
					}
				}
		);

		Application application = (Application) reactContext.getApplicationContext();
		application.registerActivityLifecycleCallbacks(eventLifecycleHandler);
	}

	@Override
	public String getName() {
		return "RNHelpScoutBeacon";
	}

	@ReactMethod
	public void init(String beaconId) {
		new Beacon.Builder()
				.withBeaconId(beaconId)
				.build();
	}

	@ReactMethod
	public void open(String signature) {
		if(signature != null && !signature.isEmpty()){
			BeaconActivity.openInSecureMode(reactContext, signature);
		}else{
			BeaconActivity.open(reactContext);
		}
	}

	@ReactMethod
	public void identify(ReadableMap identity) {
		this.userEmail = identity.hasKey("email") ? identity.getString("email") : "";
		if (identity.hasKey("name")) {
			this.userName = identity.getString("name");
			Beacon.identify(this.userEmail, this.userName);
		} else {
			Beacon.identify(this.userEmail);
		}

		Iterator<Map.Entry<String, Object>> i = identity.getEntryIterator();

		while (i.hasNext()) {
			Map.Entry<String, Object> entry = i.next();
			String key = entry.getKey();
			if (key == "email" || key == "name") continue;
			Beacon.addAttributeWithKey(key, (String) entry.getValue());
		}

		try {
		  Beacon.setSessionAttributes(new HashMap<String, String>() {{
			put("OS", "Android");
			put("AppVersion", reactContext.getPackageManager().getPackageInfo(reactContext.getPackageName(), 0).versionName);
		    }}
		  );
		} catch (Exception e) {
		}
	}

	@ReactMethod
	public void logout() {
		Beacon.logout();
	}

	@ReactMethod
	public void navigate(String route) {

	}

	@ReactMethod
	public void search(String query) {
		ArrayList<String> list = new ArrayList<String>();
		list.add(query);
		BeaconActivity.open(this.reactContext, BeaconScreens.SEARCH_SCREEN, list);
	}

	@ReactMethod
	public void openArticle(String query) {
		ArrayList<String> list = new ArrayList<String>();
		list.add(query);
		BeaconActivity.open(this.reactContext, BeaconScreens.ARTICLE_SCREEN, list);
	}

	@ReactMethod
	public void previousMessages() {
		BeaconActivity.open(this.reactContext, BeaconScreens.PREVIOUS_MESSAGES, new ArrayList<String>());
	}

	@ReactMethod
	public void contactForm() {
		BeaconActivity.open(this.reactContext, BeaconScreens.CONTACT_FORM_SCREEN, new ArrayList<String>());
	}

//	@ReactMethod
//	public void chat() {
//		BeaconActivity.open(this.reactContext, BeaconScreens.CHAT, new ArrayList<String>());
//	}

	@ReactMethod
	public void dismiss(Callback callback) {

	}

	@ReactMethod
	public void prefillForm(String subject, String content) {
	  Beacon.addPreFilledForm(new PreFilledForm(
		this.userName,
		subject,
		content,
		Collections.<Integer, String>emptyMap(),
		Collections.<String>emptyList(),
		this.userEmail));
	}


	@ReactMethod
	public void clearFormPrefill() {
	  Beacon.contactFormReset();
	}
}
