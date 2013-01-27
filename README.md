Support library for Google Maps Android API V2
===========

`google_maps_v2_support` is a library to make easy use of [Google Maps Android API V2](https://developers.google.com/maps/documentation/android/). It includes several features including `Marker` and `Polyline` managing, handy changing map position, and so on.

## Features
========
### Marker management

* add a `Marker` with simple. By putting marker's title and position, it's done. See `SupportGoogleMap.addMarker(String title, LatLng position)`.
* You can update the `Marker` that you was added before. Just put title, snippet, position to be updated.
* Supports custom id assignment.

### Polyline management
* Append a new point to existing `Polyline` with ease. See `SupportGoogleMap.appendPolyline(long, LatLng, boolean)`.
* Supports custom id assignment

### Handy camera movement
* Supports move/animate to specific `LatLng`, `SupportMarker` position.

### Automatic saving&restoring last viewpoint
* With `SupportGoogleMap`, it remembers your last viewing point, zoom level of the map.
* Can be enabled/disabled by using `SupportGoogleMap.rememberAndLoadLastCameraPosotion(boolean)`.

## How do I use it?
========

1. Add `google-play-services-sdk_lib` and `google_maps_v2_support` on workspace. (File > New... > Others... > Android Project from existing code) Make sure `google_maps_v2_support` project references `google-play-services-sdk_lib` as a library.

2. Create your project, open *Project properties*, select *Android* tab on the left side, then click *Add...* button in the *Library* area.

3. Select `google_maps_v2_support`. Then `google_maps_v2_support` will be shown at *Library* area. Now, you are ready to use `google_maps_v2_support` library.

4. Create a new `SupportGoogleMap` instance, which covers all of the features in the `GoogleMap` class.

5. Implement your application by using `SupportGoogleMap`-provided handy methods.

## Tutorial?
========
You can check out brief usage of `google_maps_v2_support` on API sample, which can be found at `sample/v2_support_sample`.

## Contacts
========
Please send an e-mail to [jyte82@gmail.com](mailto:jyte82@gmail.com) for *Help request*.  
For *Bug report* or *Opinion*, please use *Issues* menu. If you have some suggestions, bugfixes, or something by your own, please let me know via *Pull request*.

## License
========

    Copyright 2013 Taeho Kim (jyte82@gmail.com)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
