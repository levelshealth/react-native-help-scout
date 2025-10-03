import { EventEmitter } from 'events';
interface IIdentity {
    email?: string;
    name?: string;
    [key: string]: string | undefined;
}
interface IBeacon {
    init(beaconId: string): void;
    open(signature?: string): void;
    identify(identity: IIdentity): void;
    logout(): void;
    navigate(route: string): void;
    search(query: string): void;
    openArticle(articleId: string): void;
    contactForm(): void;
    previousMessages(): void;
    dismiss(): Promise<void>;
    prefillForm(subject: string, content: string): void;
    clearFormPrefill(): void;
    addListener(eventName: string): void;
    removeListeners(count: number): void;
}
type Events = {
    open: [];
    close: [];
};
interface BeaconEventEmitter extends EventEmitter {
    on<K extends keyof Events>(event: K, listener: (...args: Events[K]) => void): this;
    once<K extends keyof Events>(event: K, listener: (...args: Events[K]) => void): this;
    emit<K extends keyof Events>(event: K, ...args: Events[K]): boolean;
}
type BeaconWithEvents = IBeacon & {
    events: BeaconEventEmitter;
};
declare const _default: BeaconWithEvents;
export default _default;
