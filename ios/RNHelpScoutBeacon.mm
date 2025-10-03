#import <React/RCTConvert.h>
#import "RNHelpScoutBeacon.h"
#import <Beacon/Beacon.h>

@implementation RNHelpScoutBeacon
{
    NSString *formSubject;
    NSString *formText;
    
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
        if (signature != nil && signature.length > 0) {
            [HSBeacon openBeacon:self->settings signature:signature];
        } else {
            [HSBeacon openBeacon:self->settings];
        }
    });
}

RCT_EXPORT_METHOD(navigate:(NSString *)route)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        [HSBeacon navigate:route beaconSettings:self->settings];
    });
}

RCT_EXPORT_METHOD(previousMessages)
{
//    dispatch_async(dispatch_get_main_queue(), ^{
//        [HSBeacon navigate:@"/previous-messages/" beaconSettings:self->settings];
//    });
}

RCT_EXPORT_METHOD(contactForm)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        [HSBeacon navigate:@"/ask/message/" beaconSettings:self->settings];
    });
}

//RCT_EXPORT_METHOD(chat)
//{
//    dispatch_async(dispatch_get_main_queue(), ^{
//        [HSBeacon navigate:@"/ask/chat/" beaconSettings:self->settings];
//    });
//}

RCT_EXPORT_METHOD(search:(NSString *)query)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        [HSBeacon search:query beaconSettings:self->settings];
    });
}

RCT_EXPORT_METHOD(openArticle:(NSString *)articleId)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        [HSBeacon openArticle:articleId beaconSettings:self->settings];
    });
}

RCT_EXPORT_METHOD(dismiss:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    [self closeWithResolve:resolve];
}

RCT_EXPORT_METHOD(identify:(NSDictionary *)identity)
{
    if (identity == nil || ![identity isKindOfClass:[NSDictionary class]]) {
        return;
    }
    
    // Run on main thread since HSBeacon requires it
    dispatch_async(dispatch_get_main_queue(), ^{
        HSBeaconUser *user = [[HSBeaconUser alloc] init];
        if (!user) {
            return;
        }

        id email = identity[@"email"];
        if (email != nil && ![email isKindOfClass:[NSNull class]] && [email isKindOfClass:[NSString class]]) {
            user.email = (NSString *)email;
        }

        id name = identity[@"name"];
        if (name != nil && ![name isKindOfClass:[NSNull class]] && [name isKindOfClass:[NSString class]]) {
            user.name = (NSString *)name;
        }
        
        for (NSString *key in identity) {
            if (![key isKindOfClass:[NSString class]]) continue;
            if ([key isEqualToString:@"email"] || [key isEqualToString:@"name"]) continue;
            
            id value = identity[key];
            if (value != nil && ![value isKindOfClass:[NSNull class]]) {
                NSString *stringValue = nil;
                if ([value isKindOfClass:[NSString class]]) {
                    stringValue = (NSString *)value;
                } else {
                    stringValue = [NSString stringWithFormat:@"%@", value];
                }
                
                if (stringValue && stringValue.length > 0) {
                    [user addAttributeWithKey:key value:stringValue];
                }
            }
        }
        
        [HSBeacon login:user];
    });
}

RCT_EXPORT_METHOD(logout)
{
    [HSBeacon logout];
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

- (void)close:(RCTResponseSenderBlock)callback
{
    [HSBeacon dismissBeacon: callback == NULL ? ^{} : ^{
        callback(NULL);
    }];
}

- (void)closeWithResolve:(RCTPromiseResolveBlock)resolve
{
    [HSBeacon dismissBeacon:^{
        resolve(nil);
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

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeRNHelpScoutBeaconSpecJSI>(params);
}

@end
