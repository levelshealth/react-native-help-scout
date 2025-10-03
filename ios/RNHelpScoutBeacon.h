#import "Beacon.h"
#import <RNHelpScoutBeaconSpec/RNHelpScoutBeaconSpec.h>
#import <React/RCTEventEmitter.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNHelpScoutBeacon : RCTEventEmitter <NativeRNHelpScoutBeaconSpec, HSBeaconDelegate>

@end

NS_ASSUME_NONNULL_END
