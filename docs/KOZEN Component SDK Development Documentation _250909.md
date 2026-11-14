# KOZEN Component SDK Development Documentation 

V1.4 

Shanghai Xiangcheng Communication Technology Co., Ltd. 

## **Table of Contents** 

|<br>1. Revision History**......................................................................................................................4**|
|---|
|<br>2. Overview**................................................................................................................................ 5**|
|2.1 Introduction............................................................................................................................. 5|
|2.2 Android version and IDE version supported by the SDK................................................................. 5|
|2.3 Feature Introduction..................................................................................................................5|
|2.3.1 Component SDK Engine Module...............................................................................................5|
|2.3.2 SecondaryScreen Module.........................................................................................................5|
|2.3.3 Keyboard Module...................................................................................................................5|
|2.4 Importing the SDK.................................................................................................................... 6|
|2.5 Initializing the SDK...................................................................................................................6|
|<br>3. API Interface Introduction**........................................................................................................ 7**|
|<br>3.1 Component SDK initialization.............................................................................................. 7|
|3.1.1 Initialize the SDK...................................................................................................................7|
|3.1.2 Reverse Initialization SDK.......................................................................................................7|
|3.1.3 Get the secondary screen module..............................................................................................7|
|3.1.4 Get the keyboard module.........................................................................................................8|
|-- ComponentService instance callback - InitListener --................................................................... 8|
|3.1.5 Initialization results................................................................................................................8|
|<br>3.2 Keyboard Operation module.................................................................................................8|
|-- Get keyboard operation module - getKeyboardManager --............................................................. 8|
|3.2.1 Start listening to physical keys and return key values.................................................................. 9|
|3.2.2 Stop listening to physical keys..................................................................................................9|
|3.2.3 Enable or disable physical key press sound................................................................................9|
|3.2.4 Get physical key press sound status.......................................................................................... 9|
|-- Keyboard callback - InputCallback--....................................................................................... 10|
|3.2.3 Initialization results..............................................................................................................10|
|<br>3.3 Secondary Screen Operation module....................................................................................10|
|-- Get secondary screen operation module - getSecondaryScreenManager--.......................................10|
|3.3.1 Display a single image.......................................................................................................... 11|
|3.3.2 Image slideshow...................................................................................................................11|
|3.3.3 Display a video.................................................................................................................... 11|
|3.3.4 Power on/off the screen......................................................................................................... 12|
|3.3.5 Set the screen brightness.......................................................................................................12|
|3.3.6 Set the boot logo image..........................................................................................................12|
|3.3.7 Get the screen resolution of the secondary screen......................................................................13|
|3.3.8 Display a custom view...........................................................................................................13|
|3.3.9 Display a custom view with optional image slideshow support..................................................... 13|
|-- Secondary screen listener - IResultCallback --.......................................................................... 14|
|3.3.9 View display success............................................................................................................ 14|
|3.3.10 View display failure............................................................................................................14|
|2<br><br>4. Error Code Definition**............................................................................................................. 14**|



|4.1 CommonError.........................................................................................................................14|
|---|
|4.2 KeyboardError........................................................................................................................14|
|4.5 SecondaryScreenError............................................................................................................. 15|
|<br>5. Entity Class Definition**............................................................................................................15**|
|5.1 KeyboardConstant.KeyCode..................................................................................................... 15|
|5.2 com.kozen.component.constant.KeyboardConstant.KeyAction....................................................... 16|



3 

##  1. Revision History 

|Version|Release|Modify Record|Adapted SDK version|Author|
|---|---|---|---|---|
|1.4|2025/09/09|Add PCI Statement|ComponentService1.2.x|Johnny|
|1.3|2025/08/12|<br>Add keyboard voice broadcast<br><br>Fixed documentation errors regarding<br>CommonError issues|ComponentService1.2.x|Johnny|
|1.2|2025/07/03|Secondary screen image display with GIF support|ComponentService1.1.x|Johnny|
|1.1|2025/04/25|Switch to English version|ComponentService1.0.x|Johnny|
|1.0|2025/04/24|Renamed setDefaultBackground to setBootLogo|ComponentService1.0.x|Yanan.Zhu|
|0.4|2025/04/24|Updated key definitions in Key Module|ComponentService1.0.x|Yue.Cui|
|0.3|2025/04/22|Functionality adjustments and optimizations|ComponentService1.0.x|Yanan.Zhu|
|0.2|2025/04/21|Add Key Module|ComponentService1.0.x|Yue.Cui|
|0.1|2025/04/15|Added Secondary Screen Module|ComponentService1.0.x|Yue.Cui|



4 

##  2. Overview 

### 2.1 Introduction 

KozenComponentSDK Service is a hardware-based API SDK provided by KOZEN, specifically designed for Java and Android developers. This SDK enables developers to quickly access hardware operation interfaces, facilitating efficient business logic implementation. 

This document serves as the Component SDK Service API reference. 

### 2.2 Android version and IDE version supported by the SDK 

|System environment|Platform|Compile environment|
|---|---|---|
|Android 6.0 and above|ARM 64，ARM 32|Android Studio, Intellij|



### 2.3 Feature Introduction 

#### 2.3.1 Component SDK Engine Module 

- This module handles SDK initialization and provides access to various module operation classes. 

- Operation class object: ComponentEngine 

#### 2.3.2 SecondaryScreen Module 

- This module handles functions related to the secondary screen. 

- Operation class object: ISecondaryScreen 

- Example to get the module operation class: 

JAVA: ComponentEngine.INSTANCE.getSecondaryScreenManager() 

Kotlin: ComponentEngine.secondaryScreenManager 

#### 2.3.3 Keyboard Module 

- This module handles functions related to the keyboard. 

- Operation class object: IKeyboard 

- Example to get the module operation class: 

- JAVA: ComponentEngine.INSTANCE.getKeyboardManager() 

- Kotlin: ComponentEngine.keyboardManager 

5 



<!-- Start of picture text -->
kotlin O Sill<br>dependencies {<br>implementation( files ( ))<br>by<br><!-- End of picture text -->



<!-- Start of picture text -->
lactor Build Run Tools Git Window Help File Edit View Navigate Code Refactor Build Run Ioo<br>> T Make Project (1, Koze! i New<br>Run Generate Sources Gradle Tasks } Recent Projects<br>Recompile '‘MyApplication.kt Close Project<br>ialsDt i Close All Projects<br>Close Other Projects<br>Edit Build Types. }<br>Edit Flavors ml . Settings.<br>pei— nial————asa File Properties<br>Local History<br>Generate Signed App Bundle / APK. ial.Save.A\<br>Analyze APK. &? Sync Project with Gradle Files tri + Shift+<br>Analyze Build Performance *7 Reload All trom Disk<br>Clean Project Repair IDE<br>Rebuild Project Manage IDE Settings<br>New Projects Setup<br><!-- End of picture text -->



<!-- Start of picture text -->
kotlin O Sil<br>ComponentEngine. ( ) { code, msg -><br>message = (code == @) {<br>} {<br>u<br>}<br>Toast.makeText( , message, Toast.LENGTH_SHORT ).show()<br>ii<br><!-- End of picture text -->

After the Component SDK is successfully initialized, use ComponentEngine to obtain the operation objects for each module. Otherwise, an error code -10001 (service not connected) will be thrown. 

If the component service is disconnected during use, the InitListener will be called again with result == -1. Upon receiving this callback, reinitialize the Component SDK. 

If an interface throws the error code -10001 (service not connected) during use, reinitialize the Component SDK. 

##  3. API Interface Introduction 

|void deInit()|Reverse Initialization SDK|
|---|---|
|IKeyboard getKeyboardManager()|Get the keyboard module|
|ISecondaryScreen getSecondaryScreenManager()|Get the secondary screen module|
|void init(android.content.Context application,|Initialize the SDK|
|InitListener callback)||



###  3.1 Component SDK initialization 

#### 3.1.1 Initialize the SDK 

|Prototype|void init(android.content.Context application,<br>InitListener callback)|
|---|---|
|Function|Initialize the SDK|
|Parameters|Parameter:<br>application - context<br>callback - initialization callback|
|Return Value||
|Notes|SDK initialization with the provided context and callback function.|



#### 3.1.2 Reverse Initialization SDK 

|Prototype|void deInit()|
|---|---|
|Function|Reverse Initialization SDK|
|Parameters||
|Return Value||
|Notes|This method is used to reverse initialization the SDK.|



#### 3.1.3 Get the secondary screen module 

|Prototype|ISecondaryScreen getSecondaryScreenManager()|
|---|---|
|Function|Get the secondaryscreen module|



7 

|Parameters||
|---|---|
|Return Value|Return:|
||ISecondaryScreen|
|Notes|This method retrieves the secondary screen manager module.|



#### 3.1.4 Get the keyboard module 

|Prototype|IKeyboard getKeyboardManager()|
|---|---|
|Function|Get the keyboard module|
|Parameters||
|Return Value|Return:<br>IKeyboard|
|Notes|This method retrieves the keyboard manager module.|



-- ComponentService instance callback - InitListener -- 

void onResult(int result, String errorMsg) Initialization results 

#### 3.1.5 Initialization results 

|Prototype|void onResult(int result, String errorMsg)|
|---|---|
|Function|Initialization result|
|Parameters|Parameters:<br>result - Initialization result. 0: success; other values: failure<br>errorMsg - Detailed error information, refer to CommonError|
|Return Value||
|Notes|This method handles the initialization result, providing success or failure along with an error<br>message.|



###  3.2 Keyboard Operation module 

#### Keyboard Interface Declaration 

The keyboard is designed solely for general input and interaction purposes. It functions as a non-secure module and is not connected via a secure chip, thus it does not have the capability to process or display sensitive information. To ensure user data security and comply with information security standards and regulatory requirements, it is strictly prohibited to use the keyboard to input any cardholder information, transaction PINs, or other sensitive content, in order to prevent unauthorized interception, tampering, or misuse of such data. 

-- Get keyboard operation module - getKeyboardManager -- 

int startPhysicalKeyboard(InputCallback callback) Start listening to physical key presses, return key value 

8 

|int stopPhysicalKeyboard()|Stop listening to physical key presses|
|---|---|
|int isKeyButtonVoiceEnable()|Check if the physical key press sound is enabled|
|int switchKeyButtonVoiceEnable(boolean enable)|Enable or disable physical key press sound|



#### 3.2.1 Start listening to physical keys and return key values 

|Prototype|int startPhysicalKeyboard(InputCallback callback)|
|---|---|
|Function|Start listening to physical keys and return key values|
|Parameters|Parameters:<br>callback - Callback triggered by physical key events|
|Return Value|Return:<br>0 - Operation succeeded;<br>Others - Operation failed.<br>Specific error codes can be found in CommonError and KeyboardError|
|Notes|This method starts the listener for physical key events and returns the key values.|



#### 3.2.2 Stop listening to physical keys 

|Prototype|int stopPhysicalKeyboard()|
|---|---|
|Function|Stop listening to physical keys|
|Parameters||
|Return Value|Return:<br>0 - Operation succeeded;|
||Others - Operation failed.|
||Specific error codes can be found in CommonError and KeyboardError|
|Notes|This method stops the listener for physical key events.|



#### 3.2.3 Enable or disable physical key press sound 

|Prototype|int switchKeyButtonVoiceEnable(boolean enable)|
|---|---|
|Function|Enable or disable physical key press sound|
|Parameters|Parameters:<br>enable - true: Enable sound; false: Disable sound|
|Return Value|Return:<br>0 - Operation succeeded;<br>Others - Operation failed.|
||See CommonError and KeyboardError for details|
|Notes|Controls whether the device plays a sound when a physical key is pressed.|



#### 3.2.4 Get physical key press sound status 

|Prototype|int isKeyButtonVoiceEnable()|
|---|---|
|Function<br>Parameters|Get physical key press sound status|



9 

||Return:<br>0 - Sound enabled|
|---|---|
|Return Value|1 - Sound disabled<br>Others - Operation failed.|
||See CommonError and KeyboardError for details|
|Notes|Queries whether the physical key press sound is currently enabled.|



-- Keyboard callback - InputCallback-- 

void onKey(KeyboardConstant.KeyCode keyCode, Callback for physical keyboard KeyboardConstant.KeyAction action) 

#### 3.2.3 Initialization results 

|Prototype|void onKey(KeyboardConstant.KeyCode keyCode,<br>KeyboardConstant.KeyAction action)|
|---|---|
|Function|Physical key callback|
|Parameters|Parameters:<br>keyCode - Key value from the KeyCode enum|
||action - Key action from the KeyAction enum. 0: Pressed, 1: Released|
|Return Value||
|Notes|This method handles the callback for physical key events based on the key code and action.|



###  3.3 Secondary Screen Operation module 

#### Secondary Display Interface Declaration 

The secondary display is designed solely for the presentation and interaction of non-critical data. Its interface is a non-secure module and is not connected via a secure chip, thus it does not have the capability to process or display sensitive information. 

To ensure user data security and comply with information security standards and regulatory requirements, it is strictly prohibited to display any cardholder information, transaction data, or other sensitive content on the secondary display, in order to prevent unauthorized interception, tampering, or misuse of such data. 

-- Get secondary screen operation module - getSecondaryScreenManager-- 

|int[] getScreenResolution()|Get the resolution of the secondary screen|
|---|---|
|int power(boolean on)|Power on/off the screen|
|int setBrightness(int value)|Set the screen brightness|
|int setBootLogo(String filePath)|Set the boot logo image|
|void show(android.view.View view,|Displaya custom View|



10 

|IResultCallback resultCallback)||
|---|---|
|int showPic(String picPath)|Display a single image|
|int showPic(ArrayList<String> picPathList,|Image slideshow|
|int intervalSeconds)||
|int showVideo(String videoPath)|Display a video|
|void show(android.view.View view,|Display a custom View with support for both|
|ArrayList<PicData> picDataList,|static and animated images|
|IResultCallback resultCallback)||



#### 3.3.1 Display a single image 

|Prototype|int showPic(String picPath)|
|---|---|
|Function|Display a single image|
|Parameters|Parameters:<br>picPath - Image path, supports PNG, JPEG, GIF. Before using this API, manually place the image<br>on the TF card and pass its absolute path, e.g., sdcard/xxx/xxx.png|
|Return Value|Return:<br>0 - Operation succeeded;<br>Others - Operation failed.<br>Specific error codes can be found in SecondaryScreenError and CommonError|
|Notes|This method displays a single image on the screen.|



#### 3.3.2 Image slideshow 

|Prototype|int showPic(ArrayList<String> picPathList,<br>int intervalSeconds)|
|---|---|
|Function|Image slideshow|
|Parameters|Parameters:<br>picPathList - Array of image paths, supports PNG, JPEG, GIF. Before using this API, manually<br>place the images on the TF card and pass their absolute paths, e.g., sdcard/xxx/xxx.png<br>intervalSeconds - Interval in seconds between image transitions|
|Return Value|Return:<br>0 - Operation succeeded (only if all image types are valid);<br>Others - Operation failed.<br>Specific error codes can be found in SecondaryScreenError and CommonError|
|Notes|This method starts an image slideshow with the given list of images and interval.|



#### 3.3.3 Display a video 

|Prototype|int showVideo(String videoPath)|
|---|---|
|Function|Display a video|
|Parameters|Parameters:<br>videoPath - Videopath, supports MP4. Before usingthis API, manually place the video on the|



11 

||TF card and pass its absolute path.|
|---|---|
|Return Value|Return:<br>0 - Operation succeeded;|
||Others - Operation failed.|
||Specific error codes can be found in SecondaryScreenError and CommonError|
|Notes|This method plays a video on the screen.|



#### 3.3.4 Power on/off the screen 

|Prototype|int power(boolean on)|
|---|---|
|Function|Power on/off the screen|
|Parameters|Parameters:<br>on - true: Power on; false: Power off|
|Return Value|Return:<br>0 - Operation succeeded;<br>Others - Operation failed.<br>Specific error codes can be found in SecondaryScreenError and CommonError|
|Notes|This method controls the power state of the screen.|



#### 3.3.5 Set the screen brightness 

|Prototype|int setBrightness(int value)|
|---|---|
|Function|Set the screen brightness|
|Parameters|Parameters:<br>value - Integer value between 0 and 100|
|Return Value|Return:<br>0 - Operation succeeded;<br>Others - Operation failed.|
||Specific error codes can be found in SecondaryScreenError and CommonError|
|Notes|This method adjusts the brightness of the screen.|



#### 3.3.6 Set the boot logo image 

|Prototype|int setBootLogo(String filePath)|
|---|---|
|Function|Set the boot logo image|
|Parameters|Parameters:<br>filePath - Path to the image file, supports BMP, JPEG, and PNG. Before using this API, manually<br>place the image on the TF card and pass its absolute path.|
|Return Value|Return:<br>0 - Operation succeeded;<br>Others - Operation failed.|
||Specific error codes can be found in SecondaryScreenError and CommonError|



12 

|Notes|This method sets the default background image on the screen.|
|---|---|



#### 3.3.7 Get the screen resolution of the secondary screen 

|Prototype|int[] getScreenResolution()|
|---|---|
|Function|Get the screen resolution of the secondary screen|
|Parameters||
|Return Value|Return:|
||int[0] - Horizontal resolution (width); int[1] - Vertical resolution (height)|
|Notes|This method returns the screen resolution of the secondary screen.|



#### 3.3.8 Display a custom view 

|Prototype|void show(android.view.View view,<br>IResultCallback resultCallback)|
|---|---|
|Function|Display a custom view|
|Parameters|Parameters:<br>view - Custom view<br>resultCallback - Callback for the result of showing the custom view|
|Return Value||
|Notes|1. The view size must match the secondary screen resolution, otherwise, the view may display<br>incorrectly and touch events may fail.<br>2. Scrolling layouts are not supported.<br>3. On models with touch, only click events are supported; long press, double-click, or other<br>gesture events are not supported<br>4. After modifying the view, the API needs to be called again to avoid.|



#### 3.3.9 Display a custom view with optional image slideshow support 

|Prototype|void show(android.view.View view,<br>ArrayList<PicData> picDataList,<br>IResultCallback resultCallback)|
|---|---|
|Function|Display a custom View with optional image slideshow support|
|Parameters|Parameters:<br>view - Custom view to be displayed<br>picDataList - List of images for slideshow (supports PNG, JPEG, or GIF; one at a time)<br>resultCallback - Callback for view display result|
|Return Value||
|Notes|1. The view size must match the secondary screen resolution, otherwise, the view may display<br>incorrectly and touch events may fail.<br>2. Scrolling layouts are not supported.<br>3. On models with touch, only click events are supported; long press, double-click, or other<br>gesture events are not supported<br>4. After modifying the view, the API needs to be called again to avoid.|



13 

#### -- Secondary screen listener - IResultCallback -- 

|void onFailure(int errorCode,|View display failed|
|---|---|
|String msg)||
|void onSuccess()|View display succeeded|



#### 3.3.9 View display success 

|Prototype|void onSuccess()|
|---|---|
|Function|View display success|
|Parameters||
|Return Value||
|Notes|This method is called when the view is successfully displayed.|



#### 3.3.10 View display failure 

|Prototype|void onFailure(int errorCode,<br>String msg)|
|---|---|
|Function|View display failure|
|Parameters|Parameters:<br>errorCode - Error code, refer to SecondaryScreenError for meanings<br>msg - Error details|
|Return Value||
|Notes|This method is called when the view fails to display, providing error code and details.|



##  4. Error Code Definition 

#### 4.1 CommonError 

|Error Code|Error Description|Error Value|
|---|---|---|
|FEATURE_NOPERMISSION|Feature no-permission|-10011|
|DEVICE_BUSY|Device is busy|-10005|
|FEATURE_UNSUPPORTED|Feature unsupported|-10003|
|PARAMETERS_INVALID|Parameters invalid|-10002|
|SERVICE_DISCONNECT|Services are not connected, please<br>initialize financial services|-10001|
|VERSION_NOT_MATCH|Version mismatch|-10004|



#### 4.2 KeyboardError 

|Error Code|Error Description|Error Value|
|---|---|---|
|KEYBOARD_UNKNOWN|Unknown error|-30001|



14 

|KEYCODE_UNDEFINED|Key not defined<br>-30002|
|---|---|



4.5 SecondaryScreenError 

|Error Code|Error Description|Error Value|
|---|---|---|
|VICE_SCREEN_CANCELED|Operation terminated|-20005|
|VICE_SCREEN_DECODE_FAILED|Decoding failed|-20003|
|VICE_SCREEN_INVALID_DATA|Invalid data|-20002|
|VICE_SCREEN_TIMEOUT|Interface call timeout|-20001|
|VICE_SCREEN_UNKNOWN|Unknown error|-20004|



##  5. Entity Class Definition 

5.1 KeyboardConstant.KeyCode 

|Constant Name|Description|Type|HexValue|
|---|---|---|---|
|BUTTON_0|Number key 0|ENUM|0x30|
|BUTTON_1|Number key 1|ENUM|0x31|
|BUTTON_2|Number key 2|ENUM|0x32|
|BUTTON_3|Number key 3|ENUM|0x33|
|BUTTON_4|Number key 4|ENUM|0x34|
|BUTTON_5|Number key 5|ENUM|0x35|
|BUTTON_6|Number key 6|ENUM|0x36|
|BUTTON_7|Number key 7|ENUM|0x37|
|BUTTON_8|Number key 8|ENUM|0x38|
|BUTTON_9|Number key 9|ENUM|0x39|
|BUTTON_ASTERISK|Asterisk key (*) or Multiply key (x)|ENUM|0x2A|
|BUTTON_PLUS|Plus key (+)|ENUM|0x2B|
|BUTTON_MINUS|Minus key (-)|ENUM|0x2D|
|BUTTON_DIVIDE|Divide key (/)|ENUM|0x2F|
|BUTTON_POUND|Pound key (#)|ENUM|0x23|
|BUTTON_DOT|Dot key (.)|ENUM|0x2E|
|BUTTON_ENTER|Enter key|ENUM|0x0D|
|BUTTON_BACKSPACE|Backspace key|ENUM|0x08|
|BUTTON_ESC|ESC key|ENUM|0x1B|
|BUTTON_FN|FN key (keyboard extension)|ENUM|0xE0|
|BUTTON_HORN|Horn key (for communication)|ENUM|0x98|
|BUTTON_PAGEDOWN|Page Down key|ENUM|0x22|
|BUTTON_PAGEUP|Page Up key|ENUM|0x21|
|BUTTON_USER_DEFINED|User-defined key (custom key value, Kozen button)|ENUM|0x99|



15 

5.2 com.kozen.component.constant.KeyboardConstant.KeyAction 

|Constant Name|Description|Type|Value|
|---|---|---|---|
|ACTION_DOWN|Key press down|int|0|
|ACTION_UP|Key release|int|1|



16 

