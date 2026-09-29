"use strict";
var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
exports.__esModule = true;
var react_native_1 = require("react-native");
var events_1 = require("events");
var NativeModule = react_native_1.NativeModules.RNHelpScoutBeacon;
var nativeEmitter = new react_native_1.NativeEventEmitter(NativeModule);
var events = new events_1.EventEmitter();
nativeEmitter.addListener('open', function () {
    events.emit('open');
});
nativeEmitter.addListener('close', function () {
    events.emit('close');
});
// Native methods declare the signature argument, and the bridge rejects calls with fewer arguments
// than declared, so always pass it ('' when absent means unsigned).
var Beacon = __assign(__assign({}, NativeModule), { events: events, open: function (signature) { return NativeModule.open(signature !== null && signature !== void 0 ? signature : ''); }, search: function (query, signature) { return NativeModule.search(query, signature !== null && signature !== void 0 ? signature : ''); }, contactForm: function (signature) { return NativeModule.contactForm(signature !== null && signature !== void 0 ? signature : ''); } });
exports["default"] = Beacon;
//# sourceMappingURL=beacon.js.map