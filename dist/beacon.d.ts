import { EventSubscriptionVendor } from 'react-native';
import { EventEmitter } from 'events';
interface IIdentity {
    email?: string;
    name?: string;
    signature?: string;
    [key: string]: string | undefined;
}
interface IBeacon extends EventSubscriptionVendor {
    init(beaconId: string): void;
    open(signature?: string): void;
    identify(identity: IIdentity): void;
    logout(): void;
    navigate(route: string): void;
    search(query: string, signature?: string): void;
    openArticle(articleId: string): void;
    contactForm(signature?: string): void;
    previousMessages(): void;
    dismiss(callback: () => void): void;
    prefillForm(subject: string, content: string): void;
    clearFormPrefill(): void;
}
declare type Events = {
    open: [];
    close: [];
};
interface BeaconEventEmitter extends EventEmitter {
    on<K extends keyof Events>(event: K, listener: (...args: Events[K]) => void): this;
    once<K extends keyof Events>(event: K, listener: (...args: Events[K]) => void): this;
    emit<K extends keyof Events>(event: K, ...args: Events[K]): boolean;
}
declare type BeaconWithEvents = IBeacon & {
    events: BeaconEventEmitter;
};
declare const Beacon: BeaconWithEvents;
export default Beacon;
