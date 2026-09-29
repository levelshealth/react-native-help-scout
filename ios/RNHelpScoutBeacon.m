#import <React/RCTConvert.h>

#import "Beacon.h"

#import "RNHelpScoutBeacon.h"

@implementation RNHelpScoutBeacon
{
    NSString *formSubject;
    NSString *formText;
    NSString *identifiedSignature;
    
    HSBeaconSettings *settings;
    bool hasListeners;
}

- (void)dealloc
{
    [self close:NULL];
}

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(init:(NSString *)beaconId)
{
    settings = [[HSBeaconSettings alloc] initWithBeaconId:beaconId];
    settings.delegate = self;
}

RCT_EXPORT_METHOD(open:(NSString *)signature)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        NSString *secureSignature = [self secureModeSignature:signature];
        if (secureSignature != nil) {
            [HSBeacon openBeacon:self->settings signature:secureSignature];
        } else {
            [HSBeacon openBeacon:self->settings];
        }
    });
}

RCT_EXPORT_METHOD(navigate:(NSString *)route)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        NSString *secureSignature = [self secureModeSignature:nil];
        if (secureSignature != nil) {
            [HSBeacon navigate:route beaconSettings:self->settings signature:secureSignature];
        } else {
            [HSBeacon navigate:route beaconSettings:self->settings];
        }
    });
}

RCT_EXPORT_METHOD(previousMessages)
{
//    dispatch_async(dispatch_get_main_queue(), ^{
//        [HSBeacon navigate:@"/previous-messages/" beaconSettings:self->settings];
//    });
}

RCT_EXPORT_METHOD(contactForm:(NSString *)signature)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        NSString *secureSignature = [self secureModeSignature:signature];
        if (secureSignature != nil) {
            [HSBeacon navigate:@"/ask/message/" beaconSettings:self->settings signature:secureSignature];
        } else {
            [HSBeacon navigate:@"/ask/message/" beaconSettings:self->settings];
        }
    });
}

//RCT_EXPORT_METHOD(chat)
//{
//    dispatch_async(dispatch_get_main_queue(), ^{
//        [HSBeacon navigate:@"/ask/chat/" beaconSettings:self->settings];
//    });
//}

RCT_EXPORT_METHOD(search:(NSString *)query signature:(NSString *)signature)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        NSString *secureSignature = [self secureModeSignature:signature];
        if (secureSignature != nil) {
            [HSBeacon search:query beaconSettings:self->settings signature:secureSignature];
        } else {
            [HSBeacon search:query beaconSettings:self->settings];
        }
    });
}

RCT_EXPORT_METHOD(openArticle:(NSString *)articleId)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        NSString *secureSignature = [self secureModeSignature:nil];
        if (secureSignature != nil) {
            [HSBeacon openArticle:articleId beaconSettings:self->settings signature:secureSignature];
        } else {
            [HSBeacon openArticle:articleId beaconSettings:self->settings];
        }
    });
}

RCT_EXPORT_METHOD(dismiss:(RCTResponseSenderBlock)callback)
{
    [self close:callback];
}

// identify, logout and every open run on the main queue, so they apply in JS call order
// and an open never sees a signature that a later logout or identify replaced.
RCT_EXPORT_METHOD(identify:(NSDictionary *)identity)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        HSBeaconUser *user = [[HSBeaconUser alloc] init];

        if ([identity objectForKey:@"email"] != NULL) {
            user.email = [RCTConvert NSString:identity[@"email"]];
        }

        if ([identity objectForKey:@"name"] != NULL) {
            user.name = [RCTConvert NSString:identity[@"name"]];
        }

        // HSBeaconUser has no signature property in Beacon 3.x; the signature is passed on each open instead.
        self->identifiedSignature = [RCTConvert NSString:identity[@"signature"]];

        for (NSString *key in identity) {
            if ([key isEqual:@"email"] || [key isEqual:@"name"] || [key isEqual:@"signature"]) continue;
            id value = identity[key];
            [user addAttributeWithKey:key value:[RCTConvert NSString:value]];
        }

        [HSBeacon identify:user];
    });
}

RCT_EXPORT_METHOD(logout)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        self->identifiedSignature = nil;
        [HSBeacon logout];
    });
}

RCT_EXPORT_METHOD(prefillForm:(NSString *)subject content:(NSString *)text)
{
    formSubject = subject;
    formText = text;
}


RCT_EXPORT_METHOD(clearFormPrefill)
{
    formSubject = nil;
    formText = nil;
}

// The explicit signature, else the one from identify; nil means open unsigned.
- (NSString *)secureModeSignature:(NSString *)signature
{
    NSString *resolved = signature.length > 0 ? signature : identifiedSignature;
    return resolved.length > 0 ? resolved : nil;
}

- (void)close:(RCTResponseSenderBlock)callback
{
    [HSBeacon dismissBeacon: callback == NULL ? ^{} : ^{
        callback(NULL);
    }];
}

- (void)startObserving {
    hasListeners = YES;
}

- (void)stopObserving {
    hasListeners = NO;
}

- (NSArray<NSString *> *)supportedEvents
{
	return @[@"open", @"close"];
}

- (NSDictionary<NSString *,NSString *> *)sessionAttributes {
    return @{
        @"OS": @"iOS",
        @"AppVersion": [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleShortVersionString"]
    };
}

- (void)onBeaconOpen:(HSBeaconSettings *)beaconSettings
{
    if (!hasListeners) return;
	[self sendEventWithName:@"open" body:NULL];
}

- (void)onBeaconClose:(HSBeaconSettings *)beaconSettings
{
    [self clearFormPrefill];
    [HSBeacon reset];

    if (!hasListeners) return;
	[self sendEventWithName:@"close" body:NULL];
}

-(void)prefill:(HSBeaconContactForm *)form {
    form.subject = formSubject;
    form.text = formText;
}

@end
