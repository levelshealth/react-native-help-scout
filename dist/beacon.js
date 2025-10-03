"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const react_native_1 = require("react-native");
const events_1 = require("events");
const NativeRNHelpScoutBeacon_1 = __importDefault(require("./NativeRNHelpScoutBeacon"));
const NativeModule = NativeRNHelpScoutBeacon_1.default;
const nativeEmitter = new react_native_1.NativeEventEmitter(NativeModule);
const events = new events_1.EventEmitter();
nativeEmitter.addListener('open', () => {
    events.emit('open');
});
nativeEmitter.addListener('close', () => {
    events.emit('close');
});
NativeModule.events = events;
exports.default = NativeModule;
//# sourceMappingURL=beacon.js.map