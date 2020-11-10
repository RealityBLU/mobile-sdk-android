# Index

* [Technical overview](#overview)
* [Installation](#installation)
* [Compatibility](#compatibility)
* [BLUairspace SDK](#bluairspace_sdk)
    * [Initialization](#initialization)
    * [Markerbased](#markerbased)
    * [Markerless](#markerless)
    * [Markerless models loading progress](#markerless_models_progress)
* [UI Customization](#customization)
* [Permissions handling](#permissions)

## <a name="overview"/>Technical overview

All methods for getting data from sdk written with [kotlin coroutines.](https://kotlinlang.org/docs/reference/coroutines-overview.html) Then you may not use different libraries for thread handling. All callbacks calls only on main thread.
All required dependencies will load automatically and hidden for developers, except coroutines dependency.
For better controll of downloading data use presentation layer patterns, such as `MVVM`, `RxPM` and other.

## <a name="installation"/>Installation
You can integrate BLU SDK into your project manually by following steps:
1.	Add sdk dependencies via implementation in your app's `build.gradle` file;
2.	Your applicationId must be equals with your id in RealityBlu system

Look at an example of `build.gradle` file:
```groovy
android {
    defaultConfig {
        applicationId "your_package"
        //…
    }
}

repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/RealityBLU/mobile-sdk-android")
        credentials {
            username = "your_github_username"
            password = "your_github_token"
        }
    }
}

dependencies {
   implementation 'com.ar.bluairspace:sdk-android:1.2.1'
}
```

## <a name="compatibility"/>Compatibility

 * **Minimum Android SDK**: BLU sdk requires a minimum API level of 21.
 * **All required permissions** should be handled by a developer. The guide you can find [here](#permissions).


## <a name="bluairspace_sdk"/>BLUairspace SDK
BLUairspace SDK provides very simple interface that allows you to feel all the magic of Augumented Reality. BLUairspace SDK supports Markerbased AR and Markerless AR workflows and all the functionality of the BLUairspace platform.

**Markerbased AR** gives you an opportunity to detect, track and build entertaining experiences around a marker image.

With the help of **Markerless AR** you can place the object on the surface and it will be shown as if it's in the physical location. It can be moved, resized and (you can) interacted with from all angles.

## <a name="initialization"/>Initialization
Before we start please visit our [BLUairspace](https://www.realityblu.com) site to obtain unique `RealityBLU license key` linked to bundle identifier of your application.
Using either Markerbased or Markerless AR functionality of SDK, requires an initialization procedure to make basic SDK preparations for AR experiences. To do this just add the following code inside your Application class:
```kotlin
Blu.init(context: Context, sdkKey: String = "your_sdk_key", callback: TaskCallback?)
```

## <a name="markerbased"/>Markerbased
Marker-based API has two main features:
1.	Launching scanning screen
2.	Downloading markers (optional)

#### Launching Scanning Screen
Calling this API opens a new screen that allows scanning markers and displaying experiences when the marker is detected.        
Launch the scanning screen, you need to make the following call:
```kotlin
Blu.startMarkerbased(activity: FragmentActivity, callback: TaskCallback, markerBasedSettings: MarkerBasedSettings);
```
SDK opens the camera screen and the user can point the camera at the marker. If the marker is recognized, the experience will be downloaded and shown.

#### Marker Images
BLUairspace platform allows to upload markers and associate them with your application. This is sufficient for letting your end-users download and print markers on their own. Please note that the list of downloadable markers is configured separately from the markers you use in your experiences. 
The object that represents such an entity called `MarkerbasedMarker`.

You can get the list of configured markers using `BLUDataHelper`:
```kotlin
BluDataHelper.getMarkerbasedMarkers(callback: DataCallback<MarkerbasedMarker>)
```

## <a name="markerless"/>Markerless

Markerless API allows you to upload your own models and create groups with them, that allows user find what they need to see. 

For handy work within this structure BLU SDK provides objects of two types: `MarkerlessGroup` and `MarkerlessExperience`. One `MarkerlessGroup` could contain many `MarkerlessExperience`.

`BluDataHelper` is object responsible for managing of all the BLUs data models.
To get the list of formed groups, you'll need to make the following method call:

```kotlin
BluDataHelper.getMarkerlessGroups(callback: DataCallback<MarkerlessGroup>)
```

After that you could obtain `MarkerlessExperience` list out of selected group simply sending `groupId` parameter into `getMarkerlessExperiences` static function: 

```kotlin
BluDataHelper.getMarkerlessExperiences(groupId: Int, callback: DataCallback<MarkerlessExperience>)
BluDataHelper.getMarkerlessExperienceById(expId: Int, callback: DataCallback<MarkerlessExperience>)
```

Markerless API helps you to download and initialize user selected experiences as well as prepare augmented reality camera screen to appear. To achieve this simply use `startMarkerless` method:

```kotlin
Blu.startMarkerless(activity: FragmentActivity, list: List<MarkerlessExperience>, callback: TaskCallback)
```
Or you can open markerless screen only with 1 experience with:
```kotlin
Blu.startMarkerlessById(activity: FragmentActivity, id: Int, callback: TaskCallback)
```

## <a name="markerless_models_progress"/>Markerless models loading progress

You can observe progress of loading markerless models with help of `TaskCallback` interface. Ovveride `onProgress` callback
and update your ui.

You can check and cancel task, when models for markerless is loading with help of `BluDataHelper` object:
```kotlin
BluDataHelper.isMarkerlessLoadingFilesTaskActive()
BluDataHelper.cancelMarkerlessLoadingFilesTask()
```

## <a name="customization"/>UI customization

You can customize UI components such as images of scanning and icons of buttons by adding `BLUcustomization` folder into your project's `assets` folder.

`BLUcustomization` requires `customization.json` file inside, desribing folder structure for the image and font resources.

Basic json file should look as follows:
```javascript
{
    "markerbased": {
        "scanning-spinner": "/markerbased/scanning-spinner.png",
        "scanning-spinner-text-svg": "/markerbased/scanning-spinner.svg",
        "loading-spinner": "/markerbased/loading-spinner.png",
        "loading-spinner-frames": 48,
        "scanning-spinner-size": 60,    //(default: 50)
        "camera-switch": "/markerbased/camera-switch.png",
        "lock-screen-on": "/markerbased/lock-screen-on.png",
        "lock-screen-off": "/markerbased/lock-screen-off.png",
        "qr-button": "/markerbased/qr-button.png"
    },
    "markerless": {
        "back-to-gallery": "/markerless/back-to-gallery.png",
        "snapshot": "/markerless/cam-photo.png",
        "surface-spinner-sprite-search": "/markerless/sprite_color.png",
        "surface-spinner-sprite-ready": "/markerless/sprite_white.png"
    },
    "common": {
        "back-button": "/common/back-button.png",
        "snapshot": "/common/snapshot.png",
        "flight-off": "/common/flight-off.png",
        "flight-on": "/common/flight-on.png",
        "icons-size" : 12   //(default: 9)
    }
}
```
You can find customization folder [here](https://github.com/RealityBLU/mobile-sdk-android/tree/develop/app/src/main/assets/BLUcustomization).

## <a name="permissions"/>Permissions handling
SDK needs cameras, locations and storage permissions. All permissions should be handled by a developer, but you can use SDK’s util methods in `PermissionUtil` object:
```kotlin
isPermissionsGranted(context: Context): Boolean
askPermission(activity: Activity)
isPermissionsGrantedAndAsk(activity: Activity, requestCode: Int, grantResults: IntArray): Boolean
setSnackBarTextColor(color: Int)
setSnackBarBackgroundColor(color: Int)
```
For example:
```kotlin
if (!PermissionUtil.isPermissionsGranted(getContext())) {
    PermissionUtil.askPermission(getActivity())
}


override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    if (PermissionUtil.isPermissionsGrantedAndAsk(this, requestCode, grantResults)) {
        //all permissions granted. Your code here
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
}

```
You can find code [here](https://github.com/RealityBLU/mobile-sdk-android/blob/develop/app/src/main/java/com/ar/bluairspace/activity/AbstractActivity.kt).
