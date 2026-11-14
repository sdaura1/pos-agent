

# KOZEN Terminal Management SDK Development Documentation 

V1.2 

Shanghai Xiangcheng Communication Technology Co., Ltd. 

## Table of Contents 

|<br>1. Revision History**......................................................................................................................5**|
|---|
|<br>2. Overview**................................................................................................................................ 6**|
|2.1 Introduction............................................................................................................................. 6|
|2.2 Android version and IDE version supported by the SDK................................................................. 6|
|2.3 Feature Introduction..................................................................................................................6|
|2.3.1 Terminal Management Module................................................................................................. 6|
|2.3.2 Certificate Management Module............................................................................................... 6|
|2.3.3 Device Management Module.................................................................................................... 6|
|2.3.4 Device Information Management Module................................................................................... 6|
|2.3.5 Location Management Module..................................................................................................7|
|2.3.6 Network Management Module.................................................................................................. 7|
|2.3.7 Resource Management Module.................................................................................................7|
|2.4 Importing the TerminalManagerService SDK.................................................................................7|
|2.5 Initializing the TerminalManagerService SDK...............................................................................8|
|<br>3. API Interface Introduction**........................................................................................................ 8**|
|<br>3.1 Terminal manager SDK initialization.....................................................................................9|
|3.1.1 Initialize the TerminalManagerService instance..........................................................................9|
|-- TerminalManagerService instance callback - InitCallBack --.........................................................9|
|3.1.2 Initialization results................................................................................................................9|
|<br>3.2 Certification module............................................................................................................9|
|-- Get Certification module - getCertificationManager--....................................................................9|
|3.2.1 Updates the application's signature certificate............................................................................ 9|
|3.2.2 Deletes the application's signature certificate........................................................................... 10|
|3.2.3 Get the application's signature certificate information................................................................10|
|<br>3.3 Device information module................................................................................................ 10|
|-- Get device information module - getDeviceInfoManager--........................................................... 10|
|3.3.1 Get the SDK service version number....................................................................................... 11|
|3.3.2 Get Terminal serial number................................................................................................... 11|
|3.3.3 Get the IMSI number............................................................................................................ 11|
|3.3.4 Get the IMEI number............................................................................................................11|
|3.3.5 Get the equipment supplier name (XC, Kozen)..........................................................................11|
|3.3.6 Get the device model............................................................................................................ 12|
|3.3.7 Get the Android version number.............................................................................................12|
|3.3.8 Get the Linux kernel version number.......................................................................................12|
|3.3.9 Get the MCU version number.................................................................................................12|
|3.3.10 Get the hardware version number..........................................................................................12|
|3.3.11 Get the EMV kernel version number......................................................................................13|
|3.3.12 Get TUSN (Only for the China region)....................................................................................13|
|3.3.13 Get customer serial number..................................................................................................13|
|<br>3.4 Device module................................................................................................................. 13|
|2<br>-- Get device module - getDeviceManager--..................................................................................13|



|3.4.1 Sets the system time............................................................................................................. 13|
|---|
|3.4.2 Returns the current system time as a timestamp........................................................................14|
|3.4.3 Sets the time zone.................................................................................................................14|
|3.4.4 Get the current time zone.......................................................................................................14|
|3.4.5 Reboots the device............................................................................................................... 14|
|3.4.6 Shuts down the device...........................................................................................................15|
|3.4.7 Schedules a timed reboot.......................................................................................................15|
|3.4.8 Cancels the scheduled reboot.................................................................................................15|
|3.4.9 Enable or disable silent installation........................................................................................ 15|
|3.4.10 Enforce runtime permission checks.......................................................................................15|
|<br>3.5 Location module...............................................................................................................16|
|-- Get Location module - getLocationManager--............................................................................ 16|
|3.5.1 Enables the location module and initializes related resources..................................................... 17|
|3.5.2 Enables the location module and initializes related resources by key........................................... 17|
|3.5.3 Set location parameters......................................................................................................... 17|
|3.5.4 Start a single location request.................................................................................................17|
|3.5.5 Registers a location callback listener...................................................................................... 18|
|3.5.6 Removes the location listener.................................................................................................18|
|3.5.7 Stop the location service........................................................................................................18|
|3.5.8 Creates a circular geofence.................................................................................................... 18|
|3.5.9 Registers a listener for geofence creation results....................................................................... 19|
|3.5.10 Set the action for receiving geofence status broadcasts............................................................. 19|
|3.5.11 Removes all geofences.........................................................................................................19|
|3.5.12 Add an APK to a block list...................................................................................................19|
|3.5.13 Remove an APK from the blocked list................................................................................... 19|
|3.5.14 Check if an APK is in block.................................................................................................20|
|3.5.15 Retrieves the list of blocked APKs........................................................................................ 20|
|3.5.16 Disables the geofence feature............................................................................................... 20|
|3.5.17 Enable the location module and initialize with the specified method.......................................... 20|
|3.5.18 Enable the location module and initialize with the specified key and method...............................20|
|-- Location changed listener - ILocationChangedListener--.............................................................21|
|3.5.19 Location changed callback...................................................................................................21|
|-- GeoFence create listener - GeoFenceCreateListener --................................................................21|
|3.5.20 Callback for geofence creation completion..............................................................................21|
|<br>3.6 Network module............................................................................................................... 22|
|-- Get network module - getNetworkManager--..............................................................................22|
|3.6.1 Adds an APN configuration................................................................................................... 22|
|3.6.2 Enables an APN configuration................................................................................................22|
|<br>3.7 Resource module..............................................................................................................22|
|-- Get resource module - getResourceManager--............................................................................22|
|3.7.1 Installs or updates an app......................................................................................................23|
|3.7.2 Uninstalls an app................................................................................................................. 23|
|3.7.3 Updates the device system version or MCU firmware version...................................................... 23|
|3.7.4 Updates the resource package................................................................................................ 23|
|3.7.5 Install or update an app with listner........................................................................................ 24|



3 

|3.7.6 Update the resource package with listener............................................................................... 24|
|---|
|3.7.7 Update the device system version / MCU firmware version with listener....................................... 24|
|-- Upgrade listener - OnAppUpgradeListener--.............................................................................24|
|3.7.8 Upgrade error/failure............................................................................................................ 25|
|3.7.9 Upgrade successful...............................................................................................................25|
|-- Upgrade listener - OnUpdateCustomResListener --.................................................................... 25|
|3.7.10 Upgrade error/failure...........................................................................................................25|
|3.7.11 Upgrade successful.............................................................................................................25|
|-- Upgrade listener - OnUpdateOTAListener --............................................................................. 25|
|3.7.12 Upgrade error/failure...........................................................................................................26|
|3.7.13 Upgrade successful.............................................................................................................26|
|<br>4. Error Code Definition**............................................................................................................. 26**|
|4.1 CertificationError....................................................................................................................26|
|4.2 DeviceError........................................................................................................................... 26|
|4.3 LocationError......................................................................................................................... 26|
|4.4 NetworkError......................................................................................................................... 28|
|4.5 ResourceError........................................................................................................................ 28|
|<br>5. Entity Class Definition**............................................................................................................28**|
|5.1 com.kozen.terminalmanager.Const.............................................................................................28|
|5.2 com.kozen.terminalmanager.location.constant.LocationConstant.................................................... 29|
|5.3 LocationConstant.GeoLanguage................................................................................................ 29|
|5.4 LocationConstant.LocationMode................................................................................................30|
|5.5 LocationConstant.SignalMode...................................................................................................30|
|5.6 ApnConfiguration....................................................................................................................30|
|<br>6. Access permission**................................................................................................................. 31**|



4 

##  1. Revision History 

|Version|Release|Modify Record|Adapted SDK version|Author|
|---|---|---|---|---|
|1.2|2025/09/18|<br>Add forcePermission API for mandatory<br>permission check<br><br>Add silentInstall API for silent<br>installation<br><br>Add CSN API<br><br>Add TUSN reading<br><br>Extend getLocationManager with 2 open<br>interfaces to specify location provider<br><br>Add application install API with listener<br><br>Add OTA install API with compatibility<br>support<br><br>Add resource install API with listener<br><br>Add module permission<br><br>Add a security statement for location|TerminalManagerService 1.1.x|Johnny|
|1.1|2025/03/31|<br>Positioning module constants<br>supplement;<br><br>Enhance the resource module with<br>extended error codes and implement<br>upgrade listener for the MCU|TerminalManagerService 1.0.x|Yue.Cui<br>Tong.Liu<br>Johnny|
|1.0|2025/03/19|<br>Version verification and release|TerminalManagerService 1.0.x|Yao.zhang<br>Sunan<br>Johnny|
|0.2|2025/03/15|<br>Update LocationManager module, add<br>LocationError|TerminalManagerService 1.0.x|Johnny|
|0.1|2025/03/14|<br>Initial version|TerminalManagerService 1.0.x|Johnny|



5 

##  2. Overview 

### 2.1 Introduction 

This document is interfaces for TMS and AppStore clients, with broader OS operation permissions compared to the Financial SDK. Main functionalities include installing/uninstalling apps on the OS, retrieving system information, location data, consumable status, and performing operations such as shutting down or rebooting the OS. After client integration, the cloud can be used to manage applications and information on terminals in bulk. 

This document serves as the KozenTerminalManagerService API Reference. 

### 2.2 Android version and IDE version supported by the SDK 

|System environment|Platform|Compile environment|
|---|---|---|
|Android 6.0 and above|ARM 64，ARM 32|Android Studio, Intellij|



### 2.3 Feature Introduction 

#### 2.3.1 Terminal Management Module 

- This class handles SDK initialization and provides access to operation classes for each module. 

- The operation class object is: TerminalManager. 

#### 2.3.2 Certificate Management Module 

- This module provides certificate management functionalities. 

- The operation class object is: ICertificationManager. 

- Example to obtain the operation class: 

JAVA: TerminalManager.INSTANCE.getCertificationManager() 

- Kotlin: TerminalManager.certificationManager() 

#### 2.3.3 Device Management Module 

- This module handles device-related functionalities. 

- The operation class object is: IDeviceManager. 

- Example to obtain the operation class: 

- JAVA: TerminalManager.INSTANCE.getDeviceManager() 

- Kotlin: TerminalManager.deviceManager() 

#### 2.3.4 Device Information Management Module 

- This module provides functionalities for retrieving device information. 

- The operation class object is: IDeviceInfoManager. 

- Example to obtain the operation class: 

6 



<!-- Start of picture text -->
gradie<br><!-- End of picture text -->



<!-- Start of picture text -->
Ph<br><!-- End of picture text -->

} 



<!-- Start of picture text -->
actor |Build Run Tools Git Window Help File Edit View Navigate Code Refactor Build Run Too<br>7 T Make Project (1, Koze i New<br>1 Make Module ‘FinancialSDKDemo.app.main Lt pen.<br>Recompile ‘MyApplication.kt Close Project<br>ialsDt lose All Projects<br>Build App Bundle(s) / APK(s)za Local History<br>Anke hoy 7 Sync Project with Gradle Files trl +Shift+<br>Analyze Build Performance So reer ee eee arte<br>lean Project t RepairnvalidateIDE Caches.<br>Rebuild Project Manage IDE Settings<br>New Projects Setup<br>: Emon<br><!-- End of picture text -->

##### kotlin 



<!-- Start of picture text -->
a<br><!-- End of picture text -->

class MyApplication : Application() { override fun onCreate(}) + super.onCreate() 

val terminal = TerminalManager 

terminal. init( 

applicationContext ) { result, error -> Log.d("Application", “[terminal-manager] init result=$result, error=$error") | F 

###  3.1 Terminal manager SDK initialization 

#### 3.1.1 Initialize the TerminalManagerService instance 

|Prototype|void init(Context context,<br>InitCallBack callBack)|
|---|---|
|Function|Initialize the TerminalManagerService instance|
|Parameters|Parameters:<br>context - android context<br>callback - initialization callback|
|Return value||
|Notes||



-- TerminalManagerService instance callback - InitCallBack -- 

|void onInitResult(boolean result, String error)|Initialization results|
|---|---|



#### 3.1.2 Initialization results 

|Prototype|void onInitResult(boolean result,<br>String error)|
|---|---|
|Function|Initialization results|
|Parameters|Parameters:<br>result - Initialization result (true or false)<br>error - error message|
|Return value||
|Notes||



###  3.2 Certification module 

-- Get Certification module - getCertificationManager-- 

|int deleteAppSignature(String certData)|Delete the app signature certificate|
|---|---|
|List<String> getAppSignatureInfo()|Get the app signature certificate information|
|int updateAppSignature(String certData)|Update the app signature certificate|



#### 3.2.1 Updates the application's signature certificate 

|Prototype|int updateAppSignature(String certData)|
|---|---|
|Function|Updates the application's signature certificate.|
|Parameters|Parameters:<br>certData- Certificate Data|
|Return value|Return:|



9 

- 0: Success - Others: Failure (specific error codes refer to CertificationError). Notes For failure cases, refer to CertificationError for detailed error codes. 

#### 3.2.2 Deletes the application's signature certificate 

|Prototype|int deleteAppSignature(String certData)|
|---|---|
|Function|Deletes the application's signature certificate.|
|Parameters|Parameters:<br>certData- Certificate Data to Be Deleted|
||Return:|
|Return value|- 0: Success<br>- Others: Failure (specific error codes refer to CertificationError).|
|Notes|For failure cases, refer to CertificationError for detailed error codes.|



#### 3.2.3 Get the application's signature certificate information 

|Prototype|List<String> getAppSignatureInfo()|
|---|---|
|Function|Get the application's signature certificate information.|
|Parameters||
|Return value|Return:<br>Certificate Details List|
|Notes||



###  3.3 Device information module 

-- Get device information module - getDeviceInfoManager-- 

|String getEmvKernelVersion()|EMV kernel version number|
|---|---|
|String getHardwareVersion()|Get hardware version number|
|String[] getImei()|Get IMEI number; if multiple exist, return<br>multiple|
|String[] getImsi()|Get IMSI number; if multiple exist, return<br>multiple|
|String getKernelVersion()|Get Linux kernel version number|
|String getMcuVersion()|Get MCU version number|
|String getOsVersion()|Get Android version number|
|String getSdkServiceVersion()|Get SDK service version number|
|String getSerialNo()|Get device serial number|
|String getDeviceModel()|Get device model|
|String getVendorName()|Get vendor name(XC, Kozen)|
|String getCSN()|Get customer serial number|
|String getTUSN()|Get TUSN(Only for the China region)|



10 

#### 3.3.1 Get the SDK service version number 

|Prototype|String getSdkServiceVersion()|
|---|---|
|Function|Get the SDK service version number.|
|Parameters||
|Return value|Return:<br>The SDK service version number.|
|Notes||



#### 3.3.2 Get Terminal serial number 

|Prototype|String getSerialNo()|
|---|---|
|Function|Get Terminal serial number|
|Parameters||
|Return value|Return:<br>The device serial number.|
|Notes||



#### 3.3.3 Get the IMSI number 

|Prototype|String[] getImsi()|
|---|---|
|Function|Retrieves the IMSI number; if multiple exist, returns multiple.|
|Parameters||
|Return value|Return:<br>The IMSI number(s).|
|Notes||



#### 3.3.4 Get the IMEI number 

|Prototype|String[] getImei()|
|---|---|
|Function|Retrieves the IMEI number; if multiple exist, returns multiple.|
|Parameters||
|Return value|Return:<br>The IMEI number(s).|
|Notes||



#### 3.3.5 Get the equipment supplier name (XC, Kozen) 

|Prototype|String getVendorName()|
|---|---|
|Function|Get the equipment supplier name (XC, Kozen)|
|Parameters||
|Return value|Return:<br>Equipment supplier name|
|Notes||



11 

#### 3.3.6 Get the device model 

|Prototype|String getDeviceModel()|
|---|---|
|Function|Get the device model.|
|Parameters||
|Return value|Return:<br>The device model.|
|Notes||



#### 3.3.7 Get the Android version number 

|Prototype|String getOsVersion()|
|---|---|
|Function|Get the Android version number.|
|Parameters||
|Return value|Return:<br>The Android version number.|
|Notes||



#### 3.3.8 Get the Linux kernel version number 

|Prototype|String getKernelVersion()|
|---|---|
|Function|Get the Linux kernel version number.|
|Parameters||
|Return value|Return:<br>The kernel version number.|
|Notes||



#### 3.3.9 Get the MCU version number 

|Prototype|String getMcuVersion()|
|---|---|
|Function|Retrieves the MCU version number.|
|Parameters||
|Return value|Return:<br>The MCU version number.|
|Notes||



#### 3.3.10 Get the hardware version number 

|Prototype|String getHardwareVersion()|
|---|---|
|Function|Get the hardware version number.|
|Parameters||
|Return value|Return:<br>The hardware version number.|
|Notes||



12 

#### 3.3.11 Get the EMV kernel version number 

|Prototype|String getEmvKernelVersion()|
|---|---|
|Function|Get the EMV kernel version number.|
|Parameters||
|Return value|Return:<br>The EMV kernel version number.|
|Notes||



#### 3.3.12 Get TUSN (Only for the China region) 

|Prototype|String getTUSN()|
|---|---|
|Function|Get TUSN (Only for the China region)|
|Parameters||
|Return Value|Return:TUSN|
|Notes|Returns the unique terminal serial number.|



#### 3.3.13 Get customer serial number 

|Prototype|String getCSN()|
|---|---|
|Function|Get customer serial number|
|Parameters||
|Return Value|Return:CSN|
|Notes|Returns the customer serial number|



###  3.4 Device module 

-- Get device module - getDeviceManager-- 

|int cancelPCIReboot()|Cancel scheduled reboot|
|---|---|
|long getSystemTime()|Return the timestamp of the current system<br>time|
|String getTimeZone()|Get the current time zone|
|void reboot()|Reboot the device|
|int setPCIReboot(long time)|Schedule a reboot|
|int setSystemTime(long time)|Set the system time|
|int setTimeZone(String timeZone)|Set the time zone|
|void shutdown()|Shut down the device|
|void forcePermission(boolean isOpen)|Forced permission request|
|Void setSilentInstall(boolean isOpen)|Silent app installation|



#### 3.4.1 Sets the system time 

|Prototype|int setSystemTime(long time)|
|---|---|



13 

|Function|Sets the system time.|
|---|---|
|Parameters|time - Timestamp.|
||Return:|
|Return value|0: Success,|
||Others: Failure (refer to DeviceError).|
|Notes||



#### 3.4.2 Returns the current system time as a timestamp 

|Prototype|long getSystemTime()|
|---|---|
|Function|Returns the current system time as a timestamp.|
|Parameters||
|Return value|Return:<br>The timestamp.|
|Notes||



#### 3.4.3 Sets the time zone 

|Prototype|int setTimeZone(String timeZone)|
|---|---|
|Function|Sets the time zone.|
||timeZone - Time zone ID, supports two formats:|
|Parameters|1. Region/City (e.g., Europe/Moscow)<br>2. GMT (e.g., GMT+9).|
|Return value|Return:<br>0: Success,<br>Others: Failure (refer to DeviceError).|
|Notes||



#### 3.4.4 Get the current time zone 

|Prototype|String getTimeZone()|
|---|---|
|Function|Retrieves the current time zone.|
|Parameters||
|Return value|Return:<br>The time zone.|
|Notes||



#### 3.4.5 Reboots the device 

|Prototype|void reboot()|
|---|---|
|Function|Reboots the device|
|Parameters||
|Return value||
|Notes||



14 

#### 3.4.6 Shuts down the device 

|Prototype|void shutdown()|
|---|---|
|Function|Shuts down the device.|
|Parameters||
|Return value||
|Notes||



#### 3.4.7 Schedules a timed reboot 

|Prototype|int setPCIReboot(long time)|
|---|---|
|Function|Schedules a timed reboot.|
|Parameters|time - Device will reboot after running for this duration (e.g., 1000 * 60 * 60 for 1 hour).|
|Return value|Return:<br>0: Success, Others: Failure (refer to DeviceError).|
|Notes|1. The policy will automatically run after reboot unless canceled by cancelPCIReboot.<br>2. A reboot prompt will be shown before rebooting.<br>3. It is recommended not to exceed 24 hours.<br>4. If the set time is less than the device's uptime, it will reboot immediately.|



#### 3.4.8 Cancels the scheduled reboot 

|Prototype|int cancelPCIReboot()|
|---|---|
|Function|Cancels the scheduled reboot.|
|Parameters||
|Return value|Return:<br>0: Success, Others: Failure (refer to DeviceError).|
|Notes||



#### 3.4.9 Enable or disable silent installation 

|Prototype|void setSilentInstall(boolean isOpen)|
|---|---|
|Function|Enable or disable silent installation|
|Parameters|Parameters:<br>isOpen -<br>true: Do not show app installation prompt (silent install)<br>false: Show installation prompt|
|Return Value||
|Notes|When enabled, applications can be installed silently without prompting the user.|



#### 3.4.10 Enforce runtime permission checks 

|Prototype|void forcePermission(boolean isOpen)|
|---|---|
|Function|Enforce runtime permission checks|
|Parameters|Parameters:<br>isOpen -|



15 

||true: The app must request permission to access a module<br>false: No permission enforcement|
|---|---|
|Return Value||
|Notes|When enabled, modules cannot be accessed unless the app has explicitly been granted the<br>required permissions.|



###  3.5 Location module 

Declaration on Location Interface 

Our location interface is designed solely to provide location-related services to applications. 

- It does not actively collect device information such as SN, IMEI, or hardware identifiers. 

- It does not transmit any device information or location data to external servers without explicit application request and authorization. 

- All data access is limited to the scope necessary for location service functionality. 

This ensures that the interface complies with user privacy protection and security requirements. 

-- Get Location module - getLocationManager-- 

|int addToBlockOpenAppList(String pkgName)|Add an APK to a block list|
|---|---|
|List<String> getBlockOpenAppList()|Get the list of blocked APKs|
|boolean isInBlockOpenAppList(String pkgName)|Check if an APK is in block|
|void onDestroy()|Destroy location and geofencing resources|
|int open()|Start<br>the<br>location<br>module<br>and<br>initialize<br>related resources.|
|int open(String key)|Start<br>the<br>location<br>module<br>and<br>initialize<br>related resources by key.|
|int open(LocationConstant.LocationType locationType)|Enable the positioning module, specify the<br>positioning method, and initialize the related<br>resources.|
|int open(String key,<br>LocationConstant.LocationType locationType)|Enable the positioning module, specify the<br>positioning key and method, and initialize the<br>related resources.|
|int registerGeoFenceCreateListener(IGeoFenceCreateListener<br>listener)|Register a listener for geofence creation<br>results|
|int registerLocationListener(ILocationChangedListener listener)|Register a location callback listener|
|int removeAllGeoFence()|Remove all geofences|
|int removeFromBlockOpenAppList(String pkgName)|Remove an APK from the blocked list|
|int setGeoFenceResultAction(String action,<br>String pkgName)|Set the action to receive geofence status<br>broadcasts.|
|int setLocationOption(LocationClientOption locationClientOption)|Set location parameters|
|int startOnceLocation()|Start a single location request|
|int stopLocation()|Stop location services|
|int unRegisterLocationListener()|Remove the location listener|
|16<br>int addGeoFence(double longitude,|Create a circular geofence|



double latitude, float radius, String customId) 

#### 3.5.1 Enables the location module and initializes related resources 

|Prototype|int open()|
|---|---|
|Function|Enables the location module and initializes related resources.|
|Parameters||
|Return value|0: Operation successful;<br>Others: Operation failed. Refer to LocationError for specific error codes.|
|Notes||



#### 3.5.2 Enables the location module and initializes related resources by key 

|Prototype|int open(String key)|
|---|---|
|Function|Enables the location module and initializes related resources.|
|Parameters|key - The API key for location.<br>By default, the system-signed key is used.|
|Return value|0: Operation successful;<br>Others: Operation failed. Refer to LocationError for specific error codes.|
||If the system-signed key fails (location fails with error code ERROR_CODE_FAILURE_AUTH),|
|Notes|Please contact Kozen FAE to generate a special API key. Then call onDestroy first, and use this<br>API to reinitialize the location module by passing the API key.|



#### 3.5.3 Set location parameters 

|Prototype|int setLocationOption(LocationClientOption locationClientOption)|
|---|---|
|Function|Set location parameters. Must be used after open is successful.|
|Parameters|locationClientOption - Location parameters.|
|Return value|0: Operation successful; Others: Operation failed. Refer to LocationError for specific error codes.|
|Notes|Default configuration parameters:<br>1. High-accuracy mode;<br>2. Location request interval is 2000ms;<br>3. Network location timeout is 30s;<br>4. No reverse geocoding address information;<br>5. Reverse geocoding language is selected based on the region;<br>6. Signal selection is network plus GPS|



#### 3.5.4 Start a single location request 

|Prototype|int startOnceLocation()|
|---|---|
|Function|Start a single location request. Must be used after open is successful.|
|Parameters||
|Return value|0: Operation successful;<br>Others: Operation failed. Refer to LocationError for specific error codes.|



17 

The location request will automatically stop after completion. You can also call stopLocation to Notes terminate the location request during the process 

#### 3.5.5 Registers a location callback listener 

|Prototype|int registerLocationListener(ILocationChangedListener listener)|
|---|---|
|Function|Registers a location callback listener|
|Parameters|listener - Callback interface for location updates.|
|Return value|0: Operation successful;<br>Others: Operation failed. Refer to LocationError for specific error codes.|
||Must be used after open is successful. Only one listener can be registered globally; new|
|Notes|registrations will overwrite previous ones.<br>Call unRegisterLocationListener to remove the listener when it is no longer needed|



#### 3.5.6 Removes the location listener 

|Prototype|int unRegisterLocationListener()|
|---|---|
|Function|Removes the location listener.|
|Parameters||
|Return value|0: Operation successful;<br>Others: Operation failed. Refer to LocationError for specific error codes.|
|Notes|Must be used after open is successful.|



#### 3.5.7 Stop the location service 

|Prototype|int stopLocation()|
|---|---|
|Function|Stop the location service.|
|Parameters||
|Return value|0: Operation successful;<br>Others: Operation failed. Refer to LocationError for specific error codes.|
|Notes|Must be used after open is successful.|



#### 3.5.8 Creates a circular geofence 

|Prototype|int addGeoFence(double longitude,<br>double latitude,<br>float radius,<br>String customId)|
|---|---|
|Function|Creates a circular geofence.|
|Parameters|longitude - Longitude of the geofence center;<br>latitude - Latitude of the geofence center;<br>radius - Radius of the geofence (minimum 100 meters);<br>customId - Custom geofence ID, must be unique.|
|Return value|0: Operation successful;<br>Others: Operation failed. Refer to LocationError for specific error codes.|
|Notes|Must be used after open is successful.|



18 

#### 3.5.9 Registers a listener for geofence creation results 

|Prototype|int registerGeoFenceCreateListener(IGeoFenceCreateListener listener)|
|---|---|
|Function|Registers a listener for geofence creation results.|
|Parameters|listener - Callback interface for geofence creation.|
|Return value|0: Operation successful;<br>Others: Operation failed. Refer to LocationError for specific error codes.|
|Notes|Must be used after open is successful.|



#### 3.5.10 Set the action for receiving geofence status broadcasts 

|Prototype|int setGeoFenceResultAction(String action, String pkgName)|
|---|---|
|Function|Set the action for receiving geofence status broadcasts.|
|Parameters|action - Broadcast action;<br>pkgName - Package name for receiving the broadcast.|
|Return value|0: Operation successful;<br>Others: Operation failed. Refer to LocationError for specific error codes.|
|Notes|Multiple settings will use the last one. Must be used after open is successful.|



#### 3.5.11 Removes all geofences 

|Prototype|int removeAllGeoFence()|
|---|---|
|Function|Removes all geofences.|
|Parameters||
|Return value|0: Operation successful;<br>Others: Operation failed. Refer to LocationError for specific error codes.|
|Notes|Must be used after open is successful.|



#### 3.5.12 Add an APK to a block list 

|Prototype|int addToBlockOpenAppList(String pkgName)|
|---|---|
|Function|Add an APK to a block list|
|Parameters|pkgName - Package name of the APK.|
|Return value|0: Operation successful;<br>Others: Operation failed. Refer to LocationError for specific error codes.|
|Notes||



#### 3.5.13 Remove an APK from the blocked list 

|Prototype|int removeFromBlockOpenAppList(String pkgName)|
|---|---|
|Function|Remove an APK from the blocked list|
|Parameters|pkgName - Package name of the APK.|
|Return value|0: Operation successful;<br>Others: Operation failed. Refer to LocationError for specific error codes.|
|Notes||



19 

#### 3.5.14 Check if an APK is in block 

|Prototype|boolean isInBlockOpenAppList(String pkgName)|
|---|---|
|Function|Check if an APK is in block|
|Parameters|pkgName - Package name of the APK.|
|Return value|true: APK is disabled;<br>false: APK is not disabled.|
|Notes||



#### 3.5.15 Retrieves the list of blocked APKs 

|Prototype|List<String> getBlockOpenAppList()|
|---|---|
|Function|Retrieves the list of blocked APKs.|
|Parameters||
|Return value|Returns the list of disabled APK package names.|
|Notes||



#### 3.5.16 Disables the geofence feature 

|Prototype|void onDestroy()|
|---|---|
|Function|Destroys the location and geofence resources.|
|Parameters||
|Return value||
|Notes|Call this method when the location module is no longer needed.|



#### 3.5.17 Enable the location module and initialize with the specified method 

|Prototype|int open(LocationConstant.LocationType locationType)|
|---|---|
|Function|Enable the location module and initialize with the specified method|
|Parameters|Parameters:<br>locationType - The desired location method (from LocationConstant.LocationType)|
|Return Value|Return:<br>0 - Operation succeeded<br>Others - Operation failed (see LocationError for detailed error codes)|
|Notes|Initializes the location module and allocates necessary resources according to the specified<br>method.|



#### 3.5.18 Enable the location module and initialize with the specified key and method 

|Prototype|int open(String key,<br>LocationConstant.LocationType locationType)|
|---|---|
|Function|Enable the location module and initialize with the specified key and method|
|Parameters|Parameters:<br>key - API key for location services.<br>By default, the system-signed key is used.|
||If the system key fails (error code ERROR_CODE_FAILURE_AUTH), contact support for a|



20 

||special API key.<br>After obtaining the key, call onDestroy() first, then re-initialize using this API with the new key.<br>locationType - The desired location method (from LocationConstant.LocationType)|
|---|---|
|Return Value|Return:<br>0 - Operation succeeded<br>Others - Operation failed (see LocationError for detailed error codes)|
|Notes|This method starts the location module and allocates necessary resources for the specified location<br>method.|



-- Location changed listener - ILocationChangedListener-- 

|void onLocationChanged(MapLocation mapLocation,|Location changed callback|
|---|---|
|int errorCode,||
|String errorDetail)||



#### 3.5.19 Location changed callback 

|Prototype|void onLocationChanged(MapLocation mapLocation,<br>int errorCode,<br>String errorDetail)|
|---|---|
|Function|Location changed callback|
||mapLocation - Location result information;|
|Parameters|errorCode - Error code;<br>errorDetail - Detailed error information.|
|Return value||
|Notes|This method is called when the location is completed.|



-- GeoFence create listener - GeoFenceCreateListener -- 

|void onGeoFenceCreateFinished(List<GeoFence> geoFenceList,|Callback for geofence creation completion|
|---|---|
|int errorCode,||
|String customId)||



#### 3.5.20 Callback for geofence creation completion 

|Prototype|void onGeoFenceCreateFinished(List<GeoFence> geoFenceList,<br>int errorCode,<br>String customId)|
|---|---|
|Function|Callback for geofence creation completion.|
|Parameters|geoFenceList - List of created geofences (only available if creation is successful, otherwise<br>empty);<br>errorCode - Error code;<br>customId - Custom business ID associated with this operation.|
|Return value||



21 

Notes 

###  3.6 Network module 

-- Get network module - getNetworkManager-- 

|int addApn(ApnConfiguration config)|Add an APN|
|---|---|
|int enableApn(String name)|Enable an APN|



#### 3.6.1 Adds an APN configuration 

|Prototype|int addApn(ApnConfiguration config)|
|---|---|
|Function|Adds an APN configuration.|
|Parameters|config - The APN configuration to add.|
||Return:|
|Return value|0: Success<br>Others: Failure (refer to NetworkError).|
|Notes||



#### 3.6.2 Enables an APN configuration 

|Prototype|int enableApn(String name)|
|---|---|
|Function|Enables an APN configuration.|
|Parameters|name - The name of the APN. Pass null to disable the currently used APN.|
||Return:|
|Return value|0: Success<br>Others: Failure (refer to NetworkError).|
|Notes||



###  3.7 Resource module 

-- Get resource module - getResourceManager-- 

|int installOrUpdate(String path)|Install or update an app|
|---|---|
|int unInstall(String pkgName)|Uninstall an app|
|int updateCustomRes(String path)|Update the resource package|
|int updateOTA(String path)|Update the device system version / MCU<br>firmware version|
|int installOrUpdateWithListener(String path,|Install or update an app with listener|
|OnAppUpdateListener listener)||
|int updateCustomResWithListener(String path,|Update the resource package with listener|
|OnUpdateCustomResListener listener)||



22 

int updateOTAWithListener(String path, OnUpdateOTAListener listener) 

Update the device system version / MCU firmware version with listener 

#### 3.7.1 Installs or updates an app 

|Prototype|int installOrUpdate(String path)|
|---|---|
|Function|Installs or updates an app.|
|Parameters|path - The path of the app installation package.|
||Return:|
|Return value|0: Success<br>Others: Failure (refer to ResourceError).|
|Notes||



#### 3.7.2 Uninstalls an app 

|Prototype|int unInstall(String pkgName)|
|---|---|
|Function|Uninstalls an app.|
|Parameters|pkgName - The package name of the app to uninstall.|
|Return value|Return:<br>0: Success, Others: Failure (refer to ResourceError).|
|Notes||



#### 3.7.3 Updates the device system version or MCU firmware version 

|Prototype|int updateOTA(String path)|
|---|---|
|Function|Updates the device system version or MCU firmware version.|
|Parameters|path - The path of the update package (supports /storage/emulated/0/ or /sdcard/).<br>- .zip: Recognized as a system version package.<br>- .bin: Recognized as an MCU firmware package.|
||Return:|
|Return value|0: Package verification successful, starting upgrade.<br>Others: Failure (refer to ResourceError).|
|Notes||



#### 3.7.4 Updates the resource package 

|Prototype|int updateCustomRes(String path)|
|---|---|
|Function|Updates the resource package.|
|Parameters|path - The path of the resource package.|
||Return:|
|Return value|0: Package verification successful, starting resource update.<br>Others: Failure (refer to ResourceError).|
|Notes||



23 

#### 3.7.5 Install or update an app with listner 

|Prototype|int installOrUpdateWithListener(String path,<br>OnAppUpdateListener listener)|
|---|---|
|Function|Install or update an app with listner|
|Parameters|Parameters:<br>path - Path to the app package (either a file URI string or absolute accessible file path)<br>listener - Callback for app installation result (see OnAppUpdateListener)|
|Return Value|Return:<br>0 - API call successful<br>Others - API call failed (see CommonError for details)|
|Notes||



#### 3.7.6 Update the resource package with listener 

|Prototype|int updateCustomResWithListener(String path,<br>OnUpdateCustomResListener listener)|
|---|---|
|Function|Update the resource package with listener|
|Parameters|Parameters:<br>path - Path to the resource package (either a file URI string or absolute accessible file path)<br>listener - Callback for update result (see OnUpdateCustomResListener )|
|Return Value|Return:<br>0 - API call successful<br>Others - API call failed (see CommonError for details)|
|Notes||



#### 3.7.7 Update the device system version / MCU firmware version with listener 

|Prototype|int updateOTAWithListener(String path,<br>OnUpdateOTAListener listener)|
|---|---|
|Function|Update the device system version / MCU firmware version with listener|
|Parameters|Parameters:<br>path - Path to the OTA package (either a file URI string or absolute accessible file path)<br>listener - Callback for update result (see OnUpdateOTAListener )|
|Return Value|Return:<br>0 - API call successful<br>Others - API call failed (see CommonError for details)|
|Notes||



#### -- Upgrade listener - OnAppUpgradeListener-- 

|void onError(String msg, int code)|Upgrade error/failure|
|---|---|
|void onSuccess()|Upgrade successful|



24 

#### 3.7.8 Upgrade error/failure 

|Prototype|void onError(String msg, int code)|
|---|---|
|Function|Upgrade failed|
|Parameters|msg - Error message<br>code - Error code (refer to ResourceError)|
|Return value||
|Notes||



#### 3.7.9 Upgrade successful 

|Prototype|void onSuccess()|
|---|---|
|Function|Upgrade successful|
|Parameters||
|Return value||
|Notes||



#### -- Upgrade listener - OnUpdateCustomResListener -- 

|void onError(String msg, int code)|Upgrade error/failure|
|---|---|
|void onSuccess(String resultPath)|Upgrade successful|



#### 3.7.10 Upgrade error/failure 

|Prototype|void onError(String msg, int code)|
|---|---|
|Function|Upgrade failed|
|Parameters|msg - Error message<br>code - Error code (refer to ResourceError)|
|Return value||
|Notes||



#### 3.7.11 Upgrade successful 

|Prototype|void onSuccess()|
|---|---|
|Function|Upgrade successful|
|Parameters||
|Return value||
|Notes||



-- Upgrade listener - OnUpdateOTAListener -- 

|void onError(String msg, int code)|Upgrade error/failure|
|---|---|
|void onSuccess()|Upgrade successful|



25 

#### 3.7.12 Upgrade error/failure 

|Prototype|void onError(String msg, int code)|
|---|---|
|Function|Upgrade failed|
|Parameters|msg - Error message<br>code - Error code (refer to ResourceError)|
|Return value||
|Notes||



#### 3.7.13 Upgrade successful 

|Prototype|void onSuccess()|
|---|---|
|Function|Upgrade successful|
|Parameters||
|Return value||
|Notes||



##  4. Error Code Definition 

#### 4.1 CertificationError 

|Error Code|Error Description|Error Value|
|---|---|---|
|CERTIFICATION_ERROR_INIT|Certification<br>manager<br>service<br>initialization exception|-10000|
|CERTIFICATION_ERR_PARA_ERROR|Parameter exception|-10001|
|CERTIFICATION_CERT_LIST_CHECK_FAIL_ERROR|Certificate chain validation exception|-10002|
|CERTIFICATION_OTHER_ERROR|Other exceptions|-19999|



#### 4.2 DeviceError 

|Error Code|Error Description|Error Value|
|---|---|---|
|DEVICE_ERROR_INIT|Device manager service initialization<br>exception|-20000|
|DEVICE_OTHER_ERROR|Other exceptions|-20001|
|DEVICE_PARAMETERS_INVALID|Invalid parameters|-20002|



#### 4.3 LocationError 

|Error Code|Error Description|Error Value|
|---|---|---|
|MANAGER_SERVICE_DISCONNECT|Device<br>management<br>service<br>is<br>not<br>connected, please initialize the device<br>management service first.|-30001|
|ERROR_INIT|Positioning module is not open or<br>initialized.|-30002|



26 

|ERROR_POSITIONING_PROGRESS|Positioning is in progress. If positioning<br>is in progress, repeated positioning is<br>prohibited. Stop positioning first and<br>then restart it.|-30003|
|---|---|---|
|ERROR_UNKNOWN|Unknown error.|-30004|
|ERROR_PARAMETERS_INVALID|Invalid parameters.|-30005|
|ERROR_MISS_PERMISSIONS|Missing location permissions.|-30006|
|ERROR_CODE_INVALID_PARAMETER|Some important parameters are empty.|-31001|
|ERROR_CODE_FAILURE_WIFI_INFO|Positioning failed because the device<br>only scanned a single WiFi, and the<br>location<br>information<br>cannot<br>be<br>accurately calculated.|-31002|
|ERROR_CODE_FAILURE_LOCATION_PARAMETER|The obtained request parameters are<br>empty, possibly due to an exception<br>during the process.|-31003|
|ERROR_CODE_FAILURE_CONNECTION|Network<br>connection<br>exception.<br>Detailed information can be obtained<br>through<br>AMapLocation.getLocationDetail().|-31004|
|ERROR_CODE_FAILURE_PARSER|XML parsing error.|-31005|
|ERROR_CODE_FAILURE_LOCATION|Positioning result error.|-31006|
|ERROR_CODE_FAILURE_AUTH|KEY error.|-31007|
|ERROR_CODE_UNKNOWN|Other errors.|-31008|
|ERROR_CODE_FAILURE_INIT|Initialization exception.|-31009|
|ERROR_CODE_SERVICE_FAIL|Positioning<br>service<br>startup<br>failed.<br>Please<br>check<br>if<br>the<br>service<br>is<br>configured and if the service tag in the<br>manifest<br>is<br>placed<br>inside<br>the<br>application tag.|-31010|
|ERROR_CODE_FAILURE_CELL|Incorrect<br>base<br>station<br>information.<br>Please<br>check<br>if<br>the<br>SIM<br>card<br>is<br>installed.|-31011|
|ERROR_CODE_FAILURE_LOCATION_PERMISSION|Missing location permissions. Please<br>check<br>if<br>location<br>permissions<br>are<br>configured<br>and<br>enable<br>location<br>permissions in security software and<br>settings.|-31012|
|ERROR_CODE_FAILURE_NOWIFIANDAP|Network<br>positioning<br>failed.<br>Please<br>check if the device has a SIM card<br>inserted, mobile network is enabled, or<br>the WiFi module is turned on.|-31013|
|ERROR_CODE_FAILURE_NOENOUGHSATELLITES|Satellite<br>positioning<br>failed<br>due<br>to<br>insufficient available satellites.|-31014|
|ERROR_CODE_FAILURE_SIMULATION_LOCATION|The location may be simulated.|-31015|
|27<br>ERROR_CODE_AIRPLANEMODE_WIFIOFF|Positioning failed. Airplane mode is on|-31018|



||and the WiFi switch is off. Please turn<br>off airplane mode or turn on the WiFi<br>switch.||
|---|---|---|
|ERROR_CODE_NOCGI_WIFIOFF|Positioning<br>failed.<br>No<br>SIM<br>card<br>detected and the WiFi switch is off.<br>Please turn on the WiFi switch or<br>insert a SIM card.|-31019|
|ERROR_CODE_FAILURE_COARSE_LOCATION|Positioning failed due to an exception<br>under coarse permissions.|-31020|
|GEO_ERROR_CODE_INVALID_PARAMETER|Parameter error.|-32001|
|GEO_ERROR_CODE_FAILURE_CONNECTION|Network connection exception.|-32004|
|GEO_ERROR_CODE_FAILURE_PARSER|Data parsing failed (possibly due to<br>connecting to a network that requires<br>login but not logged in).|-32005|
|GEO_ERROR_CODE_FAILURE_AUTH|Authentication failed.|-32007|
|GEO_ERROR_CODE_UNKNOWN|Other unknown errors.|-32008|
|GEO_ERROR_NO_VALIDFENCE|No valid geofence available.|-32016|
|GEO_ERROR_CODE_EXISTS|The same fence already exists and does<br>not need to be added again. Applies<br>when the geofence's customID is the<br>same.|-32017|



#### 4.4 NetworkError 

|Error Code|Error Description|Error Value|
|---|---|---|
|NETWORK_ERROR_INIT|Network module initialization error|-40000|
|NETWORK_OTHER_ERROR|Other exceptions|-40001|
|NETWORK_PARAMETERS_INVALID|Invalid parameters|-40002|



#### 4.5 ResourceError 

|Error Code|Error Description|Error Value|
|---|---|---|
|RESOURCE_ERROR_INIT|Resource module initialization error|-50000|
|RESOURCE_OTHER_ERROR|Other errors|-50001|
|RESOURCE_REGISTER_UPGRADE_LISTENER_ERROR|MCU upgrade callback registration<br>exception|-50002|
|RESOURCE_MCU_UPGRADE_FILE_PATH_ERROR|File path exception|-50003|
|RESOURCE_MCU_UPGRADE_FILE_ERROR|File exception|-50004|
|RESOURCE_MCU_UPGRADE_ERROR|Upgrade exception|-50005|



##  5. Entity Class Definition 

5.1 com.kozen.terminalmanager.Const 

|Constant Name|Type|Value|
|---|---|---|
||28||



|FUN_ERROR|int|-1|
|---|---|---|
|FUN_SUCC|int|0|
|TERMINAL_MANAGER_SERVICE_ACTION|String|"android.intent.action.XC_TERMINAL_MANAGE|
|||R_SERVICE"|
|TERMINAL_MANAGER_SERVICE_CLASS|String|"com.kozen.terminalmanager.service.TerminalMana|
|||gerService"|
|TERMINAL_MANAGER_SERVICE_PACKAGE|String|"com.kozen.terminalmanager.service"|



5.2 com.kozen.terminalmanager.location.constant.LocationConstant 

|Constant Name|Type|Value|
|---|---|---|
|GEO_BUNDLE_KEY_CUSTOMID|String|"customId"|
|GEO_BUNDLE_KEY_FENCESTATUS|String|"event"|
|GEO_BUNDLE_KEY_LOCERRORCODE|String|"location_errorcode"|
|GEO_STATUS_IN|int|1|
|GEO_STATUS_LOCFAIL|int|4|
|GEO_STATUS_OUT|int|2|
|GPS_ACCURACY_BAD|int|0|
|GPS_ACCURACY_GOOD|int|1|
|GPS_ACCURACY_UNKNOWN|int|-1|
|GPS_STATUS_MODE_SAVING|int|3|
|GPS_STATUS_NOGPSPERMISSION|int|4|
|GPS_STATUS_NOGPSPROVIDER|int|1|
|GPS_STATUS_OFF|int|2|
|GPS_STATUS_OK|int|0|
|LOCATION_PROVIDER_GPS|String|"gps"|
|LOCATION_PROVIDER_LBS|String|"lbs"|
|LOCATION_TYPE_CELL|int|6|
|LOCATION_TYPE_COARSE_LOCATION|int|11|
|LOCATION_TYPE_FIX_CACHE|int|4|
|LOCATION_TYPE_GPS|int|1|
|LOCATION_TYPE_LAST_LOCATION_CACHE|int|9|
|LOCATION_TYPE_OFFLINE|int|8|
|LOCATION_TYPE_SAME_REQ|int|2|
|LOCATION_TYPE_WIFI|int|5|
|TRUSTED_LEVEL_BAD|int|4|
|TRUSTED_LEVEL_HIGH|int|1|
|TRUSTED_LEVEL_LOW|int|3|
|TRUSTED_LEVEL_NORMAL|int|2|



5.3 LocationConstant.GeoLanguage 

|Enum Constant|TYPE|Description|
|---|---|---|
|DEFAULT|ENUM|Returns reverse geocoding info in local language|
|EN|ENUM|Always returns reverse geocoding info in English|



29 

|ZH<br>ENUM|Always returns reverse geocoding info in Chinese|
|---|---|



#### 5.4 LocationConstant.LocationMode 

|Enum Constant|TYPE|Description|
|---|---|---|
|Battery_Saving|ENUM|Low-power positioning mode|
|Device_Sensors|ENUM|Device-only positioning mode|
|Hight_Accuracy|ENUM|High-accuracy positioning mode|



- 5.5 LocationConstant.SignalMode 

|Enum Constant|TYPE|Description|
|---|---|---|
|BEIDOU_FIRST|ENUM|In high-accuracy positioning mode, performs a single location<br>operation and prioritizes returning BeiDou satellite positioning<br>information.|
|DEFAULT|ENUM|In high-accuracy positioning mode, performs a single location<br>operation, and the system returns the first available positioning<br>result.|
|GPS_FIRST|ENUM|In high-accuracy positioning mode, performs a single location<br>operation and prioritizes returning GPS satellite positioning<br>information|



#### 5.6 ApnConfiguration 

|Constant Name|TYPE|Description|
|---|---|---|
|apn|String|APN name.|
|authType|int|Authentication type.|
|current|boolean|Enable current APN.|
|mcc|String|Mobile Country Code (MCC).|
|mmsc|String|MMSC URL.|
|mmsPort|String|MMS proxy port.|
|mmsProxy|String|MMS proxy address.|
|mnc|String|MNC Mobile Network Code (MNC).|
|name|String|Entry name.|
|numeric|String|Operator Network Identification.|
|password|String|APN password.|
|port|String|Proxy port.|
|protocol|String|The protocol to use to connect to this APN.|
|proxy|String|Proxy address.|
|roaming_protocol|String|The protocol to use to connect to this APN when roaming.|
|server|String|Server address.|
|type|String|Comma-delimited list of APN types.|
|user|String|APN username.|



30 

####  6. Access permission 

|Permission Name|Related Module|Tooltips|
|---|---|---|
|android.permission.SUPER_PERMISSIONS_D<br>EVICE|DeviceManager|Read and modify system time, timezone, and<br>other system settings|
|android.permission.SUPER_PERMISSIONS_R<br>ESOURCE|ResourceManager|Install or update resource packages (e.g.,<br>fonts, images, applications)|
|android.permission.SUPER_PERMISSIONS_L<br>OCATION|LocationManager|Get device location (GPS / Cell tower)|
|android.permission.SUPER_PERMISSIONS_D<br>EVICE_INFO|DeviceInfoManager|Collect device info (SN, IMEI, hardware)|
|android.permission.SUPER_PERMISSIONS_N<br>ETWORK|NetworkManager|Manage network access and firewall rules|



31 

