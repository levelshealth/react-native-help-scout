## 1.0.0 (2025-10-03)

**🚨 BREAKING CHANGES - NEW ARCHITECTURE ONLY:**

- **NEW ARCHITECTURE EXCLUSIVELY**: This library ONLY supports React Native's new architecture (TurboModules)
- **NO backward compatibility**: Old bridge architecture is NOT supported
- **Minimum React Native version**: Requires React Native >= 0.68.0
- **New architecture MUST be enabled**: Library will not work without new architecture
- **Method signature change**: `dismiss()` now returns a Promise instead of accepting a callback
- **Android implementation**: TurboModule-only implementation
- **iOS implementation**: TurboModule-only, removed all old bridge fallback code
- **TypeScript**: Full TypeScript with proper codegen and type safety

**NEW FEATURES:**

- Full TurboModule specification with JSI bindings
- Proper event emission on both iOS and Android platforms
- Better performance through native TurboModules
- Type-safe API with TypeScript codegen
- Future-proof implementation for React Native 0.76+

**TECHNICAL CHANGES:**

- iOS: Removed all `#ifdef RN_NEW_ARCH_ENABLED` conditionals
- iOS: Always uses `RCTEventEmitter` with TurboModule spec
- Android: Uses `TurboReactPackage` exclusively
- Package structure: Single source directory (`src/`), removed duplicate `js/`
- Podspec: Always enables new architecture
- Clean build with no old architecture remnants

**MIGRATION GUIDE:**

1. **Required**: Update React Native to >= 0.68.0
2. **Required**: Enable new architecture in your app (this is mandatory)
3. Update code: Replace `dismiss(callback)` calls with `await dismiss()` or `.then()`
4. Test: Verify events (`open`, `close`) work correctly
5. **Important**: If you cannot enable new architecture, do NOT upgrade to 1.0.0

## 0.1.6 (2021-11-02)

- Bumps native iOS sdk version to 2.2.4.
- Bumps native Android sdk version to 3.0.2.
