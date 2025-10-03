# React Native Help Scout

[![npm version](https://img.shields.io/npm/v/react-native-help-scout.svg)](https://www.npmjs.com/package/react-native-help-scout)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](http://makeapullrequest.com)

Help Scout's Beacon for React Native built exclusively for **React Native New Architecture** (TurboModules).

> ⚠️ **Version 1.0.0+ ONLY supports React Native's New Architecture**. This library does not support the old bridge architecture. If you need old bridge support, this library is not compatible with your project.

## Installation

```sh
yarn add react-native-help-scout
```

### Prerequisites

This library requires React Native's **New Architecture** to be enabled in your app:

1. **React Native >= 0.68.0**
2. **New Architecture enabled** in your app configuration
3. **TurboModules and Fabric** must be enabled

For enabling the new architecture, follow the [React Native New Architecture Guide](https://reactnative.dev/docs/the-new-architecture-landing-page).

### iOS Setup

Please visit https://developer.helpscout.com/beacon-2/ios/#additional-setup and complete the steps.

### Android Setup

The Android implementation is included and will be automatically configured with the TurboModule system.

## Usage

```javascript
import { Beacon } from 'react-native-help-scout'

// Initialize Beacon
Beacon.init('beacon-id')

// Open Beacon
Beacon.open()

// Open Beacon in Secure mode
Beacon.open(signature)

// Set user information in Beacon
Beacon.identify({
	email: 'joshuaheywood@live.com',
	name: 'Joshua Heywood',
	company: 'Megatronic',
	jobTitle: 'Marketing Manager',
})

// Unset user information in Beacon
Beacon.logout()

// Navigate to a specific screen (iOS only)
Beacon.navigate('/ask/message/')

// Open with search
Beacon.search('query')

// Open article
Beacon.openArticle('DOCS_ARTICLE_ID')

// Open contact from
Beacon.contactForm()

// Open previous messages (Android only)
Beacon.previousMessages()

// Dismissing the Beacon (iOS only) - Now returns a Promise
Beacon.dismiss().then(() => console.log('Beacon dismissed'))
// or with async/await:
const dismissBeacon = async () => {
  await Beacon.dismiss()
  console.log('Beacon dismissed')
}

// Event handlers
const openHandler = () => console.log('Beacon opened')
Beacon.events.on('open', openHandler)
Beacon.events.off('open', openHandler)
Beacon.events.off('open')
Beacon.events.once('open', () => console.log('This will only get called the first time the open event is triggered'))

const closeHandler = () => console.log('Beacon closed')
Beacon.events.on('close', closeHandler)
Beacon.events.off('close', closeHandler)
Beacon.events.off('close')
Beacon.events.once('close', () => console.log('This will only get called the first time the close event is triggered'))
```

## Requirements

- **React Native >= 0.68.0**
- **React Native New Architecture enabled** (TurboModules + Fabric)
- **iOS 11.0+**
- **Android API 21+**

## Migration from 0.x

If you're upgrading from version 0.x:

1. Update your React Native version to >= 0.68.0
2. Enable the new architecture in your app
3. Replace `Beacon.dismiss(callback)` with `await Beacon.dismiss()` or `.then()`
4. All other APIs remain the same
