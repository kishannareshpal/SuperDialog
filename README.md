# SuperDialog
A very cool looking android dialog library.

[![API 14](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)[![Download](https://api.bintray.com/packages/kishannareshpal/maven/superdialog/images/download.svg)](https://bintray.com/kishannareshpal/maven/superdialog/_latestVersion)



## Setup

To begin using **SuperDialog** in your projects just add the dependecy via *Gradle* or *Maven*.

##### a) Gradle

1. Add the library to the **dependencies { ... }** section of your **app** level `build.gradle` file:

```groovy
dependencies {
    // ...
    implementation 'com.kishannareshpal:superdialog:1.4'
    // ...
}
```



##### b) Maven

1. Add the library as a dependency to your **app** level `build.gradle` file:

   ```xml
   <dependency>
     <groupId>com.kishannareshpal</groupId>
     <artifactId>superdialog</artifactId>
     <version>1.3</version>
     <type>pom</type>
   </dependency>
   ```







## Getting Started

```java
// To show the dialog later via .show(FragmentManager);
FragmentManager fm = getSupportFragmentManager()

// With title only
new SuperDialog().title("Hello World!").show(fm);

// With message only
new SuperDialog().message("What you see, is what you get").show(fm);

// With title and message
new SuperDialog().title("Sweet!")
    .message("What you see, is what you get").show(fm);

// With custom icon drawable
new SuperDialog().iconMode(IconMode.CUSTOM_IMAGE)
    .customImageRes(R.drawable.yelloFace)
    .show(fm);

// and so much more...
```



### All of the properties

```java
new SuperDialog()
    .iconMode(IconMode mode) // NORMAL, SUCCESS, ERROR, WARNING, PROGRESS, CUSTOM_IMAGE
    .customIconRes(@DrawableRes drawableRes) // When iconMode is CUSTOM_IMAGE, you can select that icon image using this.
    
    .title(String title)
	.message(String body)
	
	.cancelable(boolean isCancelable) // If true, disallows dismiss when touched outside of the dialog.
	
	.positiveText(String positiveButtonText)
	.positiveColorRes(@ColorRes positiveButtonBackgroundColor)
	.positiveTextColorRes(@ColorRes positiveButtonTextColor)
	
	.negativeText(String negativeButtonText)
	.negativeColorRes(@ColorRes negativeButtonBackgroundColor)
	.negativeTextColorRes(@ColorRes negativeButtonTextColor)
	
	.cancelText(String cancelButtonText)
	.cancelColorRes(@ColorRes cancelButtonBackgroundColor)
	.cancelTextColorRes(@ColorRes cancelButtonTextColor)
	
	.onPositive(OnButtonClickListener onPositiveButtonClick)
	.onNegative(OnButtonClickListener onNegativeButtonClick)
	.onCancel(OnButtonClickListener onCancelButtonClick)
	
	.show(FragmentManager fm) // to show the dialog.
```

   

##### You may change any of the dialog properties anytime, by simply calling it's methods on the dialog. Example: 

```java
SuperDialog sd = new SuperDialog()
    	.iconMode(IconMode.INDEFINITE_PROGRESS)
    	.title("Loading...");
	sd.show();
	
// ... later on:
sd.iconMode(IconMode.SUCCESS)
    .title("Yay!! You rock!")
    .message("Some text body just for you to read.")
    .show(fm);
```



## Licence

```html
Copyright 2018 Kishan Nareshpal Jadav

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
