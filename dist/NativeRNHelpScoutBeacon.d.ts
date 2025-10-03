import { TurboModule } from 'react-native';
export interface Identity {
    email?: string;
    name?: string;
    [key: string]: string | undefined;
}
export interface Spec extends TurboModule {
    readonly init: (beaconId: string) => void;
    readonly open: (signature?: string) => void;
    readonly identify: (identity: Identity) => void;
    readonly logout: () => void;
    readonly navigate: (route: string) => void;
    readonly search: (query: string) => void;
    readonly openArticle: (articleId: string) => void;
    readonly contactForm: () => void;
    readonly previousMessages: () => void;
    readonly dismiss: () => Promise<void>;
    readonly prefillForm: (subject: string, content: string) => void;
    readonly clearFormPrefill: () => void;
    readonly addListener: (eventName: string) => void;
    readonly removeListeners: (count: number) => void;
}
declare const _default: Spec | null;
export default _default;
