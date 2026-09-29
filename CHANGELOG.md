## 0.1.8 (2026-09-29)

* Supports Help Scout Beacon Secure Mode: `identify` accepts a `signature`, and `open`, `search` and `contactForm` accept an optional trailing signature (falling back to the identified one). Signed opens use `openBeacon:signature:` / `search:beaconSettings:signature:` / `navigate:beaconSettings:signature:` on iOS and `BeaconActivity.openInSecureMode` on Android; without a signature they open unsigned as before.
* `signature` is no longer sent as a custom attribute; Android now excludes `email`/`name` from custom attributes too (string comparison fix).
* iOS uses `HSBeacon identify:` instead of the deprecated `login:`.

## 0.1.6 (2021-11-02)

* Bumps native iOS sdk version to 2.2.4.
* Bumps native Android sdk version to 3.0.2.
