# LifeOS iOS

Native iOS app built in Swift and SwiftUI. No React Native, Expo, UIKit-based cross-platform wrapper, WebView, or shared UI runtime.

## Current foundation
- SwiftUI app entry and native NavigationStack/TabView
- System-adaptive color scheme
- Five destinations: Today, Areas, Calendar, Insights, Coach
- Interactive habit check-offs with state restoration during the app session
- iOS 26 Liquid Glass for system-native tab chrome and eligible floating controls, with graceful native material fallback on earlier supported releases

## Liquid Glass policy
Prefer system-provided Liquid Glass on native navigation and controls. Use `.glassEffect()` only for small, intentional control surfaces when building with an SDK that includes the API. Do not turn content cards, long text surfaces, or every screen background into glass. Preserve contrast, reduce-transparency behavior, accessibility and scroll performance.

## Lumen UI
Use the native SwiftUI Lumen package (`LumenUI` product from `https://github.com/santi020k/lumen`) for shared primitives, pinned to a reviewed release. Do not use the web or React Native Lumen packages. The app's first native pass uses standard SwiftUI components until the selected Lumen release is pinned and integrated.

## Build
Open `LifeOS.xcodeproj` in Xcode 26 or later. Deployment target is iOS 17; Liquid Glass enhancements are availability-gated for iOS 26.
