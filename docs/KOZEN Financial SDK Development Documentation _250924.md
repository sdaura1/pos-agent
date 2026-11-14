# 4 K 

## Table of Contents 

|<br>1. Revision History**......................................................................................................................8**|
|---|
|<br>2. Overview**.............................................................................................................................. 10**|
|2.1 Introduction........................................................................................................................... 10|
|2.2 Android version and IDE version supported by the SDK............................................................... 10|
|2.3 Feature Introduction................................................................................................................10|
|2.3.1 Financial SDK Engine Module............................................................................................... 10|
|2.3.2 Cardreader Module...............................................................................................................10|
|2.3.3 EMV Module....................................................................................................................... 10|
|2.3.4. General Module.................................................................................................................. 10|
|2.3.5. Pinpad Module....................................................................................................................11|
|2.3.6. Printer Module....................................................................................................................11|
|2.3.7. Scanner Module.................................................................................................................. 11|
|2.3.8. Security Module.................................................................................................................. 11|
|2.3.9. ECR Module.......................................................................................................................11|
|2.4 Importing the Financial SDK.................................................................................................... 12|
|2.5 Initializing the Financial SDK...................................................................................................12|
|<br>3. API Interface Introduction**...................................................................................................... 14**|
|<br>3.1 Financial SDK initialization............................................................................................... 14|
|3.1.1 Initialize the FinancialService instance................................................................................... 14|
|-- FinancialService instance callback - InitListener --....................................................................14|
|3.1.2 Initialization results..............................................................................................................14|
|<br>3.2 Scanner Operation module.................................................................................................15|
|-- Get scanner operation module - getScannerManager --................................................................15|
|3.2.1 Open the scanner module...................................................................................................... 15|
|3.2.2 Close the scanner module......................................................................................................15|
|3.2.3 Register the callback listener for the code scanning result..........................................................15|
|3.2.4 Trigger the barcode scanning action and start scanning..............................................................16|
|3.2.5 Stop scanning...................................................................................................................... 16|
|3.2.6 Enable/Disable specified barcode type to be recognized.............................................................16|
|3.2.7 Enable/Disable all supported barcode types............................................................................. 17|
|3.2.8 Check whether a certain barcode type can be recognized............................................................17|
|-- Scanner connection status listener - IConnectionStatusListener --................................................ 17|
|3.2.9 Monitoring barcode scanning service is connected.................................................................... 17|
|3.2.10 Monitoring barcode scanning service is disconnected...............................................................17|
|3.2.11 Monitoring barcode scanning service error messages................................................................18|
|-- Scanner Result Callback - IScannerResultCallback --................................................................ 18|
|3.2.12 Get the scan result and code type..........................................................................................18|
|<br>3.3 Card Reader Operation module...........................................................................................18|
|-- Get card reader operation module - getCardReaderManager --..................................................... 18|
|3.3.1 Power on the card.................................................................................................................19|
|3.3.2 Power off the card.................................................................................................................19|
|2<br>3.3.3 Start checking card...............................................................................................................19|



|3.3.4 Stop checking card............................................................................................................... 20|
|---|
|3.3.5 Check whether the card is in the card reader slot...................................................................... 21|
|3.3.6 Transmit APDU command to card...........................................................................................21|
|3.3.7 write HCE data via NFC tag...................................................................................................22|
|-- Card check listener - ICheckCardListener --............................................................................. 22|
|3.3.7 Magstripe card found successfully.......................................................................................... 22|
|3.3.8 Contact card found successfully..............................................................................................22|
|3.3.9 Contactless card found successfully........................................................................................ 23|
|3.3.10 Felica card found successfully..............................................................................................23|
|3.3.11 Card detection error............................................................................................................23|
|3.3.12 Card detection timeout........................................................................................................ 24|
|<br>3.4 EMV Operation module.....................................................................................................24|
|-- Get EMV operation module - getEmvManager --........................................................................ 24|
|3.4.1 Set the terminal information for the EMV kernel....................................................................... 25|
|3.4.2 Get the terminal information for the EMV kernel.......................................................................25|
|3.4.3 Set AID...............................................................................................................................26|
|3.4.4 Delete AID..........................................................................................................................26|
|3.4.5 Retrieve AID List.................................................................................................................26|
|3.4.6 Add CAPK..........................................................................................................................26|
|3.4.7 Delete CAPK.......................................................................................................................27|
|3.4.8 Retrieve the CAPK list..........................................................................................................27|
|3.4.9 Set Exception File................................................................................................................ 27|
|3.4.10 Delete Exception File..........................................................................................................27|
|3.4.11 Retrieve the configured Exception File.................................................................................. 27|
|3.4.12 Set RevocationIPK..............................................................................................................27|
|3.4.13 Delete RevocationIPK.........................................................................................................28|
|3.4.14 Retrieve the configured CAPK Revocation parameters............................................................. 28|
|3.4.15 Set Dynamic Reader Limits (DRL) configuration parameters.....................................................28|
|3.4.16 Delete Dynamic Reader Limits (DRL) configuration parameters................................................ 28|
|3.4.17 Retrieve Dynamic Reader Limits (DRL) configuration parameters............................................. 29|
|3.4.18 Set RuPay terminal parameters.............................................................................................29|
|3.4.19 Delete RuPay terminal parameters........................................................................................ 29|
|3.4.20 Retrieve RuPay terminal parameters..................................................................................... 29|
|3.4.21 Set Apple VAS transaction parameters.................................................................................. 30|
|3.4.22 Retrieve Apple VAS transaction parameters...........................................................................30|
|3.4.23 Set Apple Merchant transaction parameters............................................................................30|
|3.4.24 Delete Apple Merchant transaction parameters....................................................................... 30|
|3.4.25 Retrieve Apple Merchant transaction parameters.................................................................... 30|
|3.4.26 Retrieve Kernel version information...................................................................................... 31|
|3.4.27 Start EMV transaction.........................................................................................................31|
|3.4.28 Stop EMV transaction......................................................................................................... 31|
|3.4.29 Set EMV Tag/Object parameters...........................................................................................32|
|3.4.30 Retrieve EMV Tag/Object parameters....................................................................................32|
|3.4.31 Set the result of multiple application selection........................................................................ 32|
|3.4.32 Set card information confirmation result.................................................................................32|



3 

|3.4.33 Set Pin input result.............................................................................................................32|
|---|
|3.4.34 Set the online result............................................................................................................ 33|
|3.4.35 Detect card........................................................................................................................33|
|3.4.36 Multiple application selection callback..................................................................................34|
|3.4.37 Card information confirmation callback................................................................................. 34|
|3.4.38 Kernel card scheme type callback.........................................................................................34|
|3.4.39 Second tap card callback..................................................................................................... 35|
|3.4.40 Request PIN input callback................................................................................................. 35|
|3.4.41 Request online process callback...........................................................................................35|
|3.4.42 Transaction result callback.................................................................................................. 35|
|<br>3.5 General Operation function................................................................................................36|
|-- Get general operation module - getGeneralManager --................................................................ 36|
|3.5.1 Set buzzer........................................................................................................................... 36|
|3.5.2 Set system navigation bar...................................................................................................... 37|
|3.5.3 Set system status bar.............................................................................................................37|
|3.5.4 Wake up device................................................................................................................... 37|
|3.5.5 Set system time.................................................................................................................... 38|
|3.5.6 Get time from system............................................................................................................ 38|
|3.5.7 Get time zone.......................................................................................................................38|
|3.5.8 Set time zone....................................................................................................................... 38|
|3.5.9 Reboot device......................................................................................................................38|
|3.5.10 Shut down device................................................................................................................39|
|3.5.11 Set system property value.................................................................................................... 39|
|3.5.12 Get system property value....................................................................................................39|
|3.5.13 Configures screen rotation function....................................................................................... 39|
|3.5.14 Toggle LED light on/off....................................................................................................... 40|
|3.5.15 Show or hide the LED view.................................................................................................. 40|
|3.5.16 Checks the version of dependency library.............................................................................. 40|
|<br>3.6 PINPAD Operation module................................................................................................41|
|-- Get pinpad module - getPinpadManager --................................................................................41|
|3.6.1 Start Input PIN by provided view............................................................................................41|
|3.6.2 Start Input PIN by default view...............................................................................................42|
|3.6.3 Cancel PIN entry..................................................................................................................42|
|-- PIN input callback - PinpadInputCallback --............................................................................ 42|
|3.6.4 Key press event....................................................................................................................42|
|3.6.5 Error callback......................................................................................................................43|
|3.6.6 Return when confirm PIN input..............................................................................................43|
|3.6.7 Screen rotation callback when use PINPAD............................................................................. 43|
|<br>3.7 Printing Operation module.................................................................................................43|
|-- Get printing operation module - getPrinterManager --.................................................................43|
|3.7.1 Open the printer module........................................................................................................44|
|3.7.2 Get the current status of the printer.........................................................................................44|
|3.7.3 Feed paper before starting printing......................................................................................... 44|
|3.7.4 Set print grayscale................................................................................................................45|
|3.7.5 Set print font........................................................................................................................45|



4 

|3.7.6 Set print line space...............................................................................................................45|
|---|
|3.7.7 Add the text to the print cache............................................................................................... 46|
|3.7.8 Add a line of text with different styles to the print cache............................................................ 46|
|3.7.9 Add a blank line to the print cache......................................................................................... 46|
|3.7.10 Add the barcode to the print cache........................................................................................47|
|3.7.11 Add the bitmap to the print cache......................................................................................... 47|
|3.7.12 Trigger the printing operation to print out the content in the print cache..................................... 47|
|3.7.13 Trigger the printing operation to print out the content in the print cache..................................... 47|
|3.7.14 Turn off the printer............................................................................................................. 48|
|-- Print result callback - IPrintResultCallback --...........................................................................48|
|3.7.15 Callback when the printing finishes successfully.....................................................................48|
|3.7.16 Callback when an error occurs during the printing process....................................................... 48|
|<br>3.8 Security module............................................................................................................... 49|
|-- Get security module - getSecurityManager --............................................................................. 49|
|3.8.1 Write keys...........................................................................................................................50|
|3.8.2 Write DUKPT keys...............................................................................................................51|
|3.8.3 Generate RSA keys...............................................................................................................52|
|3.8.4 Write RSA keys................................................................................................................... 52|
|3.8.5 Read RSA keys....................................................................................................................52|
|3.8.6 Get random numbers.............................................................................................................53|
|3.8.7 Get KCV for the corresponding key type.................................................................................. 53|
|3.8.8 Encrypt or decrypt data by DUKPT.........................................................................................54|
|3.8.9 RSA encryption or decryption................................................................................................ 54|
|3.8.10 3DES data encryption and decryption....................................................................................55|
|3.8.11 Calculate MAC...................................................................................................................55|
|3.8.12 Calculate MAC by DUKPT.................................................................................................. 56|
|3.8.13 Get the KSN for the key at the specified index........................................................................ 57|
|3.8.14 Increase KSN.....................................................................................................................57|
|3.8.15 Calculate MAC using the DUKPT_TDES algorithm................................................................. 57|
|3.8.16 Erase all keys.................................................................................................................... 58|
|3.8.17 Write DUKPT_DES key...................................................................................................... 58|
|3.8.18 Write MK/SK key............................................................................................................... 59|
|<br>3.9 ECR module.................................................................................................................... 60|
|-- Get ECR module - getEcrManager --........................................................................................60|
|3.9.1 Create a connection to a device.............................................................................................. 60|
|3.9.2 Close connection(s)...............................................................................................................60|
|3.9.3 Enumerate and return available ports......................................................................................60|
|3.9.4 Hide all displayed pages....................................................................................................... 61|
|3.9.5 Register a connection listener................................................................................................ 61|
|3.9.6 Unregister the connection listener...........................................................................................61|
|3.9.7 Get local BT/HOST information and display as QR code............................................................ 61|
|3.9.8 Display QR scanning interface and obtain showHostByQR result................................................ 61|
|-- ECR connector object- EcrConnector --....................................................................................62|
|3.9.9 Read data............................................................................................................................62|
|3.9.10 Write data......................................................................................................................... 62|



5 

|3.9.11 Get connection information.................................................................................................. 62|
|---|
|-- ECR connection listener - ConnectionListener--........................................................................ 63|
|3.9.12 Connection state callback.................................................................................................... 63|
|3.9.13 Called when a connection is established................................................................................ 63|
|<br>3.10 Scanner module..............................................................................................................63|
|-- Get scanner module - getScannerManager --............................................................................. 63|
|3.10.1 Open the scanner module.................................................................................................... 63|
|3.10.2 Open the scanner module with a specific camera.....................................................................64|
|3.10.3 Close the scanner module.................................................................................................... 64|
|3.10.4 Register a scanner result callback.........................................................................................64|
|3.10.5 Start scanning.................................................................................................................... 64|
|3.10.6 Stop scanning.....................................................................................................................65|
|3.10.7 Enable or disable specific barcode types................................................................................65|
|3.10.8 Enable or disable all barcode types....................................................................................... 65|
|3.10.9 Check if a barcode type is enabled........................................................................................65|
|3.10.10 Toggle the flashlight..........................................................................................................66|
|-- Scanner result callback - ScannerResultCallback --................................................................... 66|
|3.10.11 Receive scan result and symbology type...............................................................................66|
|-- Connection Status Listener- IConnectionStatusListener--............................................................ 66|
|3.10.12 Called when the connection to the scanner service is established.............................................66|
|3.10.13 Called when the connection to the scanner service fails..........................................................67|
|3.10.14 Called when the connection to the scanner service is lost........................................................67|
|<br>4. Error Code Definition**............................................................................................................. 67**|
|4.1 CardReaderError.................................................................................................................... 67|
|4.2 CommonError.........................................................................................................................68|
|4.3 GeneralError..........................................................................................................................68|
|4.4 PinpadError........................................................................................................................... 69|
|4.5 PrinterError........................................................................................................................... 69|
|4.6 ScannerError..........................................................................................................................69|
|4.7 SecurityError..........................................................................................................................70|
|4.8 EmvError...............................................................................................................................70|
|4.9 PosEmvErrorCode...................................................................................................................72|
|4.10 EcrError.............................................................................................................................. 72|
|<br>5. Entity Class Definition**............................................................................................................73**|
|5.1 com.kozen.financial.aidl.emv.EmvCapk..................................................................................... 73|
|5.2 ConstantCardReader............................................................................................................... 73|
|5.3 ConstantCardReader.CardType.................................................................................................73|
|5.4 ConstantEmv.POIEmvCoreManager...........................................................................................74|
|5.5 ConstantEmv.POIEmvCoreManager.AppleTerminalConstraints..................................................... 75|
|5.6 ConstantEmv.POIEmvCoreManager.EmvCardInfoConstraints........................................................76|
|5.7 ConstantEmv.POIEmvCoreManager.EmvDrlConstraints............................................................... 76|
|5.7 ConstantEmv.POIEmvCoreManager.EmvOnlineConstraints.......................................................... 76|
|5.8 ConstantEmv.POIEmvCoreManager.EmvPinConstraints............................................................... 77|
|5.9 ConstantEmv.POIEmvCoreManager.EmvResultConstraints...........................................................78|
|6<br>5.10 ConstantEmv.POIEmvCoreManager.EmvServiceConstraints........................................................78|



|5.11 ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints..................................................... 79|
|---|
|5.12 ConstantEmv.POIEmvCoreManager.EmvTransDataConstraints....................................................81|
|5.13 ConstantPrinter.....................................................................................................................83|
|5.14 ConstantPrinter.Align............................................................................................................ 83|
|5.15 ConstantPrinter.BarcodeFormat...............................................................................................83|
|5.16 ConstantPrinter.PrintFailurePolicy.......................................................................................... 84|
|5.17 ConstantPrinter.FontSize........................................................................................................84|
|5.18 ConstantSecurity...................................................................................................................84|
|5.19 ConstantScanner................................................................................................................... 85|
|5.20 ConstantScanner.BarcodeFormat............................................................................................. 86|
|5.21 ConstantScanner.ScannerCameraType......................................................................................86|
|5.22 ConstantScanner.BarcodeSupport............................................................................................87|
|5.23 com.kozen.financial.pinpad.PinViewEnum............................................................................... 87|
|<br>6. Access permission**................................................................................................................. 87**|



7 

##  1. Revision History 

|Version|Release|Mod|ify Record|Adapted SDK version|Author|
|---|---|---|---|---|---|
|1.5|2025/09/24||Fix the problem of getCardExistStatus<br>description|FinnancialService1.2.x|Johnny|
|1.4|2025/09/15|<br><br><br><br><br><br><br><br><br><br><br>|Fix<br>documentation<br>regarding<br>EMVListener<br>errors<br>Add key-value enumeration description for<br>PinViewEnum<br>Add ECR pairing<br>Add LED display<br>Add interface to obtain library dependency<br>versions<br>Add PINPAD screen rotation support<br>Add scanner default interface<br>Add default PICC logo<br>Add printer pop-up reminders for high/low<br>temperature and paper shortage<br>Add voice notifications for high/low temperature<br>and paper shortage in the printer.<br>Add support for SM4 (Only for the China region)<br>Add permission control for each module|FinnancialService1.2.x|Johnny|
|1.3|2025/08/22|<br><br>|Add explanations for three DEKPT_DES<br>functions<br>Include instructions for MK/SK writing<br>Fix the issue with ConstantSecurity in the<br>previous version of the document.|FinnancialService1.1.x|Johnny|
|1.2|2025/07/24|<br><br><br>|Add NFC TAG, ECR (only includes serial port<br>& USB-to-serial), Felica card support, front and<br>rear camera scanning<br>Support for separate updates of the Kozen SDK<br>Fix the API level of the system environment to<br>23.<br>getCardExistStatus adds a return value for card<br>presence.|FinnancialService1.1.x|Johnny|
|1.1|2025/03/31|<br>|Supplemental printing & barcode module error<br>codes/constants<br>Remove duplicate error messages in EMV<br>module<br>Add Pinpad rotation support|FinnancialService1.0.x|Yue.Cui<br>Yao.Zhang<br>Tong.Liu<br>Johnny|
|1.0|2025/03/10||Add error code definition, entity definition,<br>access permission|FinnancialService1.0.x|Yao.zhang<br>Sunan|
||||8<br>Add EMV module||Johnny|



|||<br>Add SDK integration description|||
|---|---|---|---|---|
|0.5|2025/03/03|<br>Add a description of the financial SDK engine<br>module|FinnancialService1.0.x|Sunan<br>Johnny|
|0.4|2025/02/27|<br>Update card reader, password keyboard, and<br>some interfaces for general operations|FinnancialService1.0.x|Johnny|
|0.3|2025/1/24|<br>Add EMV module, printer module, scanner<br>module, and security module<br><br>Update the API description for card reader,<br>PINPAD, and general operation<br><br>Add card detection and card positioning<br>functions; Add PINPAD parameter configuration|FinnancialService1.0.x|Johnny|
|0.2|2025/1/2|<br>Add object description|FinnancialService1.0.x|Johnny|
|0.1|2024/11/12|<br>Initial version|FinnancialService1.0.x|Johnny|



9 

##  2. Overview 

## 2.1 Introduction 

KozenFinancialService is a hardware firmware-based API SDK provided by KOZEN. Designed specifically for Java and Android developers. This SDK enables developers to quickly access hardware operation interfaces for KOZEN financial terminal, facilitating efficient business logic implementation. 

The SDK primarily includes the following modules: Basic Device Information, Card Operations, PIN Pad, EMV, and Security mode. 

This document serves as the KozenFinancialService API Reference. 

## 2.2 Android version and IDE version supported by the SDK 

|System environment|Platform|Compile environment|
|---|---|---|
|Android 6.0 and above|ARM 64，ARM 32|Android Studio, Intellij|



## 2.3 Feature Introduction 

## 2.3.1 Financial SDK Engine Module 

- This module handles SDK initialization and provides access to various module operation classes. 

- Operation class object: FinancialEngine 

## 2.3.2 Cardreader Module 

- This module handles card reader functionality. 

- Operation class object: ICardReaderManager 

- Example to get the module operation class: 

- JAVA: FinancialEngine.INSTANCE.getCardReaderManager() 

- Kotlin: FinancialEngine.cardReaderManager() 

## 2.3.3 EMV Module 

- This module handles EMV functionality. 

- Operation class object: IEmvManager 

- Example to get the module operation class: 

- JAVA: FinancialEngine.INSTANCE.getEmvManager() 

- Kotlin: FinancialEngine.emvManager() 

## 2.3.4. General Module 

- This module handles basic device control functionalities. 

- Operation class object: IGeneralManager 

10 

- Example to get the module operation class: 

JAVA: FinancialEngine.INSTANCE.getGeneralManager() 

Kotlin: FinancialEngine.generalManager() 

## 2.3.5. Pinpad Module 

- This module handles Pinpad functionality. 

- Operation class object: IPinpadManager 

- Example to get the module operation class: 

- JAVA: FinancialEngine.INSTANCE.getPinpadManager() 

- Kotlin: FinancialEngine.pinpadManager() 

## 2.3.6. Printer Module 

- This module handles printing functionality. 

- Operation class object: IPrinterManager 

- Example to get the module operation class: 

- JAVA: FinancialEngine.INSTANCE.getPrinterManager() 

- Kotlin: FinancialEngine.printerManager() 

## 2.3.7. Scanner Module 

- This module handles scanning functionality. 

- Operation class object: IScannerManager 

- Example to get the module operation class: 

- JAVA: FinancialEngine.INSTANCE.getScannerManager() 

- Kotlin: FinancialEngine.scannerManager() 

## 2.3.8. Security Module 

- This module handles encryption/decryption algorithms and key-related functionalities. 

- Operation class object: ISecurityManager 

- Example to get the module operation class: 

- JAVA: FinancialEngine.INSTANCE.getSecurityManager() 

- Kotlin: FinancialEngine.securityManager() 

## 2.3.9. ECR Module 

- This module handles Kiosk functionalities. 

- Operation class object: IEcrManager 

- Example to get the module operation class: 

- JAVA: FinancialEngine.INSTANCE.getEcrManager() 

- Kotlin: FinancialEngine.ecrManager() 

11 



<!-- Start of picture text -->
kotlin<br><!-- End of picture text -->



<!-- Start of picture text -->
Bil<br><!-- End of picture text -->

dependencies { implementation(files("libs/FinancialLib-1.0.@-release.aar")) 



<!-- Start of picture text -->
actor Build Run Tools Git Window Help File Edit View Navigate Code Refactor Build Run Too<br>7 T Make Project 01, Koze =! New<br>1, Make Module 'FinancialSDKDemo.app.mair © Oper<br>ialsDt Close All Projects<br>OI BG ee<br>Edit Build Types. ]<br>Select Build Variant. ea Tae<br>Build App Bundle(s) / APK(s) sece EY<br>Generate Signed App Bundle / APK<br>Analyze APK. &? Sync Project with Gradle Files trl + Shift+<br>Ruaivan Boskd Porkocance +7 Reload All trom Disk<br>Rebuild Project Manage IDE Settings<br>New Projects Setup<br>. Emon<br><!-- End of picture text -->

### kotlin 



<!-- Start of picture text -->
Sil<br><!-- End of picture text -->

class DemoApplication : Application() { override fun onCreate() { 

super.onCreate() 

FinancialEngine.init(this, object : InitListener { override fun onResult(result: Int, errorMsg: String?) { Toast .makeText ( 

this@DemoApplication, 

if (result == @) "success" else "failed, $errorMsg", Toast. LENGTH_SHORT ) . show() } }) } aS Jj 

After the Financial SDK is successfully initialized, use FinancialEngine to obtain the operation objects for each module. Otherwise, an error code -10001 (Financial service not connected) will be thrown. 

If the financial service is disconnected during use, the InitListener will be called again with result == -1. Upon receiving this callback, reinitialize the Financial SDK. 

If an interface throws the error code -10001 (Financial service not connected) during use, reinitialize the Financial SDK. 

13 

##  3. API Interface Introduction 

|void init(android.content.Context application,<br>InitListener callback)|Financial SDK initialization|
|---|---|
|ICardReaderManager getCardReaderManager()|Card Reader Operation module|
|IEmvManager getEmvManager()|EMV Operation module|
|IGeneralManager getGeneralManager()|Device Basic Operation function|
|IPinpadManager getPinpadManager()|PINPAD Operation module|
|IPrinterManager getPrinterManager()|Printing Operation module|
|ISecurityManager getSecurityManager()|Security module|
|IEcrManager getEcrManager()|ECR Operation module|
|IScannerManager getScannerManager()|Scanner Operation module|



##  3.1 Financial SDK initialization 

## 3.1.1 Initialize the FinancialService instance 

|Prototype|void init(android.content.Context application,<br>InitListener callback)|
|---|---|
|Function|Initialize the FinancialService instance|
|Parameters|Parameters:<br>application - context<br>callback - initialization callback|
|Return value||
|Notes||



- -- FinancialService instance callback - InitListener -- 

|void onResult(int result, String errorMsg)|Initialization results|
|---|---|



## 3.1.2 Initialization results 

|Prototype|void onResult(int result,<br>String errorMsg)|
|---|---|
|Function|Initialization results|
|Parameters|Parameters:<br>result - initialization result<br>0: success|
||Others: failure - more details to see CommonError<br>errorMsg - error message|
|Return value||
|Notes||



14 

##  3.2 Scanner Operation module 

-- Get scanner operation module - getScannerManager -- 

|void close()|Close the scanner module.|
|---|---|
|boolean isBarcodeEnabled(ConstantScanner.BarcodeFormat type)|Check whether a certain barcode type can be<br>recognized.|
|void open(IConnectionStatusListener callback)|Open the scanner module.|
|int registerResultCallback(IScannerResultCallback callback)|Register the callback listener for the barcode<br>scanning result.|
|int setBarcodeEnable(boolean enable)|Enable/Disable all supported barcode types.|
|int setBarcodeEnable(List<ConstantScanner.BarcodeFormat> types,<br>boolean enable)|Enable/Disable specified barcode type to be<br>recognized.|
|int startScan()|Trigger the barcode scanning action and start<br>scanning.|
|int stopScan()|Stop scanning.|



## 3.2.1 Open the scanner module. 

|Prototype|void open(IConnectionStatusListener callback)|
|---|---|
|Function|Open the barcode scanning module<br>Initialize the barcode scanning module. Only after successful called, other interfaces can be<br>available.|
|Parameters|Parameters:<br>callback - connection status callback|
|Return value||
|Notes||



## 3.2.2 Close the scanner module 

|Prototype|void close()|
|---|---|
|Function|Close the barcode scanning module.<br>When the barcode scanning module is no longer needed, use this interface to release the resources|
|Parameters||
|Return value||
|Notes||



## 3.2.3 Register the callback listener for the code scanning result 

|Prototype|int registerResultCallback(IScannerResultCallback callback)|
|---|---|
|Function|Register the callback listener for the scan result. After successful scanning, the barcode type and<br>scan result will be returned through this callback. It needs to be called after OPEN API.|
|Parameters|Parameters:<br>callback - callback for scanning result|
|Return value|Return:<br>0: The operation is successfully executed;|



15 



Others: The operation fails. 

For the specific meaning of the error code, please refer to the definitions in ScannerError and CommonError 

Notes 

## 3.2.4 Trigger the barcode scanning action and start scanning 

|Prototype|int startScan()|
|---|---|
|Function|Trigger the barcode scanning action and start scanning. It needs to be called after OPEN API.|
|Parameters||
|Return value|Return:<br>0: The operation is successfully executed;<br>Others: The operation fails.|
||For the specific meaning of the error code, please refer to the definitions in ScannerError and<br>CommonError|
|Notes||



## 3.2.5 Stop scanning 

|Prototype|int stopScan()|
|---|---|
|Function|Stop scanning. It needs to be called after OPEN API.|
|Parameters||
|Return value|Return:<br>0: The operation is successfully executed;<br>Others: The operation fails.|
||For the specific meaning of the error code, please refer to the definitions in ScannerError and<br>CommonError|
|Notes||



## 3.2.6 Enable/Disable specified barcode type to be recognized 

|Prototype|int setBarcodeEnable(List<ConstantScanner.BarcodeFormat> types,<br>boolean enable)|
|---|---|
|Function|Enable/disable support for the specified type of barcode. When support is enabled, the scanner<br>can recognize the barcode. Otherwise, it is disabled. This function needs to be called after the<br>OPEN API is called.|
|Parameters|Parameters:<br>types - An array of ConstantScanner.BarcodeFormat enumeration types, used to specify the<br>barcode types to be enabled or disabled.<br>enable - true: enable support; false: disable support.|
|Return value|Return:<br>0: The operation is successfully executed;<br>Others: The operation fails.<br>For the specific meaning of the error code, please refer to the definitions in ScannerError and<br>CommonError|
|Notes|16|



## 3.2.7 Enable/Disable all supported barcode types. 

|Prototype|int setBarcodeEnable(boolean enable)|
|---|---|
|Function|Enable/Disable all supported barcode types. This function needs to be called after the OPEN API<br>is called.|
|Parameters|Parameters:<br>enable - true: Enable all code system support; false: Disable all code system support. The scan<br>button will light up at this time, but no barcode can be recognized|
|Return value|Return:<br>0: The operation is successfully executed;<br>Others: The operation fails.<br>For the specific meaning of the error code, please refer to the definitions in ScannerError and<br>CommonError|
|Notes||



## 3.2.8 Check whether a certain barcode type can be recognized. 

|Prototype|boolean isBarcodeEnabled(ConstantScanner.BarcodeFormat type)|
|---|---|
|Function|Check whether a certain barcode type can be recognized. This function needs to be called after the<br>OPEN API is called.|
|Parameters|Parameters:<br>type - code type|
|Return value|Return:<br>true: The scanner can recognize the barcode type; false: The scanner cannot recognize the barcode<br>type. Please note that false will also be returned if the SDK status is abnormal.|
|Notes||



## -- Scanner connection status listener - IConnectionStatusListener -- 

|void onConnected()|Monitoring barcode scanning service is connected|
|---|---|
|void onDisconnected()|Monitoring barcode scanning service is disconnected|
|void onError(int error,|Monitoring barcode scanning service error messages|
|String msg)||



## 3.2.9 Monitoring barcode scanning service is connected 

|Prototype|void onConnected()|
|---|---|
|Function|Monitoring barcode scanning service is connected|
|Parameters||
|Return value||
|Notes|Will be called when a connection is established with the code scanning service, indicating that the<br>code scanning module has been initialized successfully|



## 3.2.10 Monitoring barcode scanning service is disconnected 

|Prototype<br>void|onDisconnected()|
|---|---|



17 

|Function|Monitoring barcode scanning service is disconnected|
|---|---|
|Parameters||
|Return value||
|Notes|Will be called when the connection with the code scanning service is lost|



## 3.2.11 Monitoring barcode scanning service error messages 

|Prototype|void onError(int error,<br>String msg)|
|---|---|
|Function|Monitoring barcode scanning service error messages|
|Parameters|Parameters:<br>error - error code. For the specific meaning of the error code, please refer to the definitions in the<br>ScannerError and CommonError<br>msg - error description|
|Return value||
|Notes||



-- -- Scanner Result Callback - IScannerResultCallback 

|void onResult(String sym, String barcode)<br>Get the scan result and code type|
|---|



## 3.2.12 Get the scan result and code type 

|Prototype|void onResult(String sym, String barcode)|
|---|---|
|Function|Get the scan result and code type|
|Parameters|Parameters:<br>sym - code type<br>barcode - scan result|
|Return value||
|Notes||



##  3.3 Card Reader Operation module 

-- Get card reader operation module - getCardReaderManager -- 

|int powerOff(int cardType)|Power off the card|
|---|---|
|int powerOn(int cardType)|Power on the card|
|int checkCard(int cardType,|Start checking card|
|int timeout,||
|ICheckCardListener callback)||
|int getCardExistStatus(int cardType)|Check whether the card is in the card reader slot|
|int stopCheck()|Stop checking card|
|Int transmitApdu(int cardType,<br>byte[] sendBuff,|Transmit APDU command to card|
|CustomByteArrayrspBuf,||



18 

|CustomByteArray swBuf)||
|---|---|
|int hceWrite(android.nfc.NdefMessage msg,|Write NDEF data via HCE|
|int timeout)||



## 3.3.1 Power on the card 

|Prototype|int powerOn(int cardType)|
|---|---|
|Function|Card reader powered on|
|Parameters|Parameters:<br>cardType - card type<br>1. Supports single card and multiple card types. If input ConstantCardReader.CardType.ALL, can<br>detect all card types.<br>2. Multiple card types are detected such as follows:<br>ConstantCardReader.CardType.CONTACT | ConstantCardReader.CardType.MAGNETIC<br>3. More card type details in ConstantCardReader.CardType|
|Return value|Returns:<br>0: Success<br>Non-0: Failure - see CardReaderError|
|Notes|Note:<br>1. Due to protocol conflicts, Felica cards cannot be powered on simultaneously with other<br>contactless cards.<br>2. When powering on cards, if the parameter ConstantCardReader.CardType.ALL is passed, only<br>other contactless cards will be powered on by default, excluding Felica cards.<br>3. When powering on other contactless cards, Felica cards will be powered off; likewise, when<br>powering on Felica cards, other contactless cards will be powered off.|



## 3.3.2 Power off the card 

|Prototype|int powerOff(int cardType)|
|---|---|
|Function|Power off the card|
|Parameters|Parameters:<br>cardType - card type<br>1. Supports single card and multiple card types. If input ConstantCardReader.CardType.ALL, can<br>detect all card types.<br>2. Multiple card types are detected such as follows:<br>ConstantCardReader.CardType.CONTACT | ConstantCardReader.CardType.MAGNETIC<br>3. More card type details in ConstantCardReader.CardType|
|Return value|Returns:<br>0: Success<br>Non-0: Failure - see CardReaderError|
|Notes||



## 3.3.3 Start checking card 

|Prototype|int checkCard(int cardType,|
|---|---|
||int timeout,|



19 

||ICheckCardListener callback)|
|---|---|
|Function|Start checking card|
|Parameters|Parameters:<br>cardType - card type<br>1. Supports single card and multiple card types. If input ConstantCardReader.CardType.ALL,<br>can detect all card types.<br>2. Multiple card types are detected such as follows:<br>ConstantCardReader.CardType.CONTACT | ConstantCardReader.CardType.MAGNETIC<br>3. If both CardType.ALL and specific card types are passed, the specific card types take<br>precedence.<br>Example: ConstantCardReader.CardType.ALL | ConstantCardReader.CardType.CONTACT<br>will only detect contact cards during card detection.<br>4. More card type details in ConstantCardReader.CardType<br>timeout - timeout, in seconds<br>callback - card check callback|
|Return value|Returns:<br>0: Success<br>Non-0: Failure - see CardReaderError|
|Notes|Note:<br>1. The card will not be powered off automatically after the card checking is completed. Please<br>note that the card must be powered off manually after the checking is completed;<br>2. During the card checking process, a card checking error will not terminate the process, but<br>the onError callback will be called;<br>3. The checking process will continue until one type of card is detected or checking time out.<br>4.There is a protocol conflict between contactless cards and Felica cards; they cannot be<br>detected simultaneously.<br>5. If the card type is set to detect all cards (ConstantCardReader.CardType.ALL), felica cards<br>will not be detected by default.|



## 3.3.4 Stop checking card 

|Prototype|int stopCheck()|
|---|---|
|Function|Stop checking card|
|Parameters||
|Return value|Returns:<br>0: Success<br>Non-0: Failure - see CardReaderError|
|Notes|Note:<br>1. If you want to interrupt checking manually during the card checking process, please call this<br>method to end the card inspection<br>2. In the normal process, such as card is found or timeout occurs, the process will automatically<br>stop<br>3. Stop the card checking process will not power off card;<br>4. After this function, please use the poweroff function to avoid affecting the power consumption of<br>the machine.|



20 

## 3.3.5 Check whether the card is in the card reader slot 

|Prototype|int getCardExistStatus(int cardType)|
|---|---|
|Function|Check whether the card is in the card reader slot|
|Parameters|cardType - card type<br>1. Only supports single card<br>2. Supported card types:<br>2.1 ConstantCardReader.CardType.CONTACT - contact card<br>2.2 ConstantCardReader.CardType.CONTACTLESS - contactless card<br>2.3 ConstantCardReader.CardType.FELICA - felica card|
|Return value|Return:<br>0: Card present<br>-1: Card not present<br>Other: Failure – see CardReaderError for details|
|Notes|Note:<br>1. This function does not support composite cards; it only supports a single card type;<br>2. This function will not automatically power on the card being checked;<br>3. The function must be called when the card is already powered on; otherwise, it will return an<br>error code indicating the card is powered off.|



## 3.3.6 Transmit APDU command to card 

|Prototype|int transmitApdu(int cardType,<br>byte[] sendBuff,<br>CustomByteArray rspBuf,<br>CustomByteArray swBuf)|
|---|---|
|Function|Transmit APDU command to card|
|Parameters|Parameters:<br>cardType - card type<br>1. Only supports single card<br>2. Supported card types:<br>2.1 ConstantCardReader.CardType.CONTACT - contact card<br>2.2 ConstantCardReader.CardType.CONTACTLESS - contactless card<br>2.3 ConstantCardReader.CardType.FELICA - felica card<br>sendBuff - data to be transparently transmitted to the card, maximum 1929B<br>rspBuf - card response data<br>swBuf - card response data - software version number|
|Return value|Return:<br>0: Card is in the slot<br>Others: Card is not in slot, see CardReaderError|
|Notes|Note:<br>1. This function does not support multiple cards, only single card types;<br>2. This function will power on the checked card automatically , but will not power off card after the<br>detection is completed;<br>3. After this function, please use the poweroff function to avoid affecting the power consumption of|



21 



the machine. 

4. APDU command transmission is allowed only after successful card check; this method will fail if the card is powered on without card check 

## 3.3.7 write HCE data via NFC tag 

|Prototype|int hceWrite(android.nfc.NdefMessage msg,<br>int timeout)|
|---|---|
|Function|Write NDEF data via HCE|
|Parameters|Parameters:<br>msg - Data of type NdefMessage , max 255 bytes<br>timeout - Timeout in seconds (timeout <= 0 means no timeout)|
||Return:|
|Return Value|0 - Success;|
||Others - Failure. Refer to CardReaderError for details|
|Notes|This method writes NDEF data using Host Card Emulation.|



## -- Card check listener - ICheckCardListener -- 

|void findContactCard(android.os.Bundle info)|Contact card found successfully|
|---|---|
|void findMagstripeCard(android.os.Bundle info)|Magstripe card found successfully|
|void findContactlessCard(android.os.Bundle info)|Contactless card found successfully|
|void findFelicaCard(android.os.Bundle info)|Felica card found successfully|
|void onError(int code, String message)|Card detection error|
|void onTimeout()|Card detection timeout|



## 3.3.7 Magstripe card found successfully 

|Prototype|void findMagstripeCard(android.os.Bundle info)|
|---|---|
|Function|Magstripe card found successfully|
|Parameters|Parameters:<br>info - return data|
|Return value||
|Notes|Parameter constant value<br>The following data will be returned during the card detection process<br>ConstantCardReader.CARD_CHANNEL - logical channel number<br>ConstantCardReader.CARD_SERIAL_NUM - Card serial number<br>ConstantCardReader.CARD_ATTRIBUTE - ATR<br>ConstantCardReader.TRACK1 - Track 1 data<br>ConstantCardReader.TRACK2 - Track 2 data<br>ConstantCardReader.TRACK3 - Track 3 data|



## 3.3.8 Contact card found successfully 

|Prototype|void findContactCard(android.os.Bundle info)|
|---|---|
|Function|Contact card found successfully|



22 

|Parameters|Parameters:<br>info - return data|
|---|---|
|Return value||
|Notes|Parameter constant value|
||The following data will be returned during the card detection process<br>ConstantCardReader.CARD_CHANNEL - logical channel number|
||ConstantCardReader.CARD_SERIAL_NUM - Card serial number|
||ConstantCardReader.CARD_ATTRIBUTE - ATR|



## 3.3.9 Contactless card found successfully 

|Prototype|void findContactlessCard(android.os.Bundle info)|
|---|---|
|Function|Contactless card found successfully|
|Parameters|Parameters:<br>info - return data|
|Return value||
|Notes|Parameter constant value<br>The following data will be returned during the card detection process<br>ConstantCardReader.CARD_CHANNEL - logical channel number<br>ConstantCardReader.CARD_SERIAL_NUM - Card serial number<br>ConstantCardReader.CARD_ATTRIBUTE - ATR|



## 3.3.10 Felica card found successfully 

|Prototype|void findFelicaCard(android.os.Bundle info)|
|---|---|
|Function|Felica card found successfully|
|Parameters|Parameters:<br>info - return data|
|Return Value||
||Parameter constant value<br>The following data will be returned during the card detection process|
|Notes|ConstantCardReader.ID_FOR_MANUFACTURER - Unique card<br>ConstantCardReader.PARAMETER_FOR_MANUFACTURER - Card parameters<br>ConstantCardReader.REQUEST_DATA - Command response|



## 3.3.11 Card detection error 

|Prototype|void onError(int code,<br>String message)|
|---|---|
|Function|Card detection error|
|Parameters|Parameters:<br>code - error code, more details to see CardReaderError<br>message - error description|
|Return value||
|Notes||



23 

## 3.3.12 Card detection timeout 

|Prototype|void onTimeout()|
|---|---|
|Function|Card detection timeout|
|Parameters||
|Return value||
|Notes||



##  3.4 EMV Operation module 

-- Get EMV operation module - getEmvManager -- 

|int deleteAid()|Delete AID|
|---|---|
|int deleteAppleMerchant()|Delete Apple Merchant transaction parameters|
|int deleteCapk()|Delete CAPK|
|int deleteDRL(int type)|Delete Dynamic Reader Limits (DRL) configuration<br>parameters|
|int deleteExceptionFile()|Delete Exception File|
|int deleteRevocationIPK()|Delete Revocation IPK|
|int deleteService()|Delete RuPay payment terminal parameters|
|List<EmvAid> getAid()|Get AID list|
|int getAppleMerchant(android.os.Bundle bundle)|Get Apple Merchant transaction parameters|
|int getAppleTerminal(android.os.Bundle bundle)|Get Apple VAS transaction parameters|
|List<EmvCapk> getCapk()|Get CAPK list|
|int getDRL(int type, android.os.Bundle bundle)|Get Dynamic Reader Limits (DRL) configuration<br>parameters|
|List<EmvExceptionFile> getExceptionFile()|Get configured Exception File|
|byte[] getKernel(String[] tags)|Get EMV Tag/Object parameters|
|List<EmvRevocationIPK> getRevocationIPK()|Get configured CAPK Revocation parameters|
|int getService(android.os.Bundle bundle)|Get RuPay payment terminal parameters|
|int getTerminal(int type, android.os.Bundle bundle)|Get EMV kernel terminal information|
|String getVersion(int type)|Get Kernel version information|
|int setAid(EmvAid emvAid)|Load AID|
|int setAppleMerchant(android.os.Bundle bundle)|Set Apple Merchant transaction parameters|
|int setAppleTerminal(android.os.Bundle bundle)|Set Apple VAS transaction parameters|
|int setCapk(EmvCapk emvCapk)|Add CAPK|
|int setCardInfoResponse(android.os.Bundle bundle)|Set card information confirmation result|
|int setDRL(int type, android.os.Bundle bundle)|Set Dynamic Reader Limits (DRL) configuration<br>parameters|
|int setExceptionFile(EmvExceptionFile exceptionFile)|Set Exception File|
|int setKernel(byte[] tlv)|Set EMV Tag/Object parameters|
|int setOnlineResponse(android.os.Bundle bundle)|Set online transaction result|
|int setPinResponse(android.os.Bundle bundle)|Set PIN input result|
|int setRevocationIPK(EmvRevocationIPK revocationIPK)|24<br>Set Revocation IPK|



|int setSelectApplicationResponse(int position)|Set multiple application selection result|
|---|---|
|int setService(android.os.Bundle bundle)|Set RuPay terminal parameters|
|int setTerminal(int type, android.os.Bundle bundle)|Set EMV kernel terminal information|
|int startTransaction(android.os.Bundle bundle,<br>IEmvListener callback)|Start EMV transaction|
|int stopTransaction()|Stop transaction|



## 3.4.1 Set the terminal information for the EMV kernel 

|Prototype|int setTerminal(int type,<br>android.os.Bundle bundle)|
|---|---|
|Function|Set the terminal information for the EMV kernel.|
|Parameters|type: Terminal type, with the following enumerated values:<br>ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_TERMINAL<br>ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_CONFIG<br>ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_VISA<br>ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_UNIONPAY<br>ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_MASTERCARD<br>ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_DISCOVER<br>ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_AMEX<br>ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_MIR<br>ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_RUPAY<br>ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_INTERAC<br>bundle: Terminal parameters. For constant details, refer<br>to ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



## 3.4.2 Get the terminal information for the EMV kernel 

|Prototype|int getTerminal(int type,<br>android.os.Bundle bundle)|
|---|---|
|Function|Get the terminal information for the EMV kernel.|
||type: Terminal type, with the following enumerated values:<br>ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_TERMINAL<br>ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_CONFIG<br>ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_VISA|
|Parameters|ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_UNIONPAY<br>ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_MASTERCARD<br>ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_DISCOVER<br>ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_AMEX<br>ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_MIR|



25 

||ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_RUPAY<br>ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_INTERAC|
|---|---|
||bundle: Terminal parameters. For details, refer to the class definition<br>of ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



## 3.4.3 Set AID 

|Prototype|int setAid(EmvAid emvAid)|
|---|---|
|Function|Set AID|
|Parameters|emvAid: Parameter entity. For details, refer to EmvAid.|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



## 3.4.4 Delete AID 

|Prototype|int deleteAid()|
|---|---|
|Function|Delete AID|
|Parameters|None|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



## 3.4.5 Retrieve AID List 

|Prototype|List<EmvAid> getAid()|
|---|---|
|Function|Retrieve the AID list|
|Parameters|None|
|Return value|List of EmvAid. For details, refer to EmvAid.|
|Notes||



## 3.4.6 Add CAPK 

|Prototype|int setCapk(EmvCapk emvCapk)|
|---|---|
|Function|Add CAPK|
|Parameters|emvCapk: Parameter entity. For details, refer to EmvCapk.|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



26 

## 3.4.7 Delete CAPK 

|Prototype|int deleteCapk()|
|---|---|
|Function|Delete CAPK|
|Parameters|None|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



## 3.4.8 Retrieve the CAPK list 

|Prototype|List<EmvCapk> getCapk()|
|---|---|
|Function|Retrieve the CAPK list|
|Parameters|None|
|Return value|List of EmvCapk. For details, refer to EmvCapk.|
|Notes||



## 3.4.9 Set Exception File 

|Prototype|int setExceptionFile(EmvExceptionFile exceptionFile)|
|---|---|
|Function|Set Exception File|
|Parameters|exceptionFile: Parameter entity. For details, refer to EmvExceptionFile.|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



## 3.4.10 Delete Exception File 

|Prototype|int deleteExceptionFile()|
|---|---|
|Function|Delete Exception File|
|Parameters|None|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



## 3.4.11 Retrieve the configured Exception File 

|Prototype|List<EmvExceptionFile> getExceptionFile()|
|---|---|
|Function|Retrieve the configured Exception File|
|Parameters|None|
|Return value|List of EmvExceptionFile. For details, refer to EmvExceptionFile.|
|Notes||



## 3.4.12 Set RevocationIPK 

Prototype int setRevocationIPK(EmvRevocationIPK revocationIPK) 

27 

|Function|Set RevocationIPK|
|---|---|
|Parameters|revocationIPK: Parameter entity. For details, refer to EmvRevocationIPK.|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



## 3.4.13 Delete RevocationIPK 

|Prototype|int deleteRevocationIPK()|
|---|---|
|Function|Delete RevocationIPK|
|Parameters|None|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



## 3.4.14 Retrieve the configured CAPK Revocation parameters 

|Prototype|List<EmvRevocationIPK> getRevocationIPK()|
|---|---|
|Function|Retrieve the configured CAPK Revocation parameters|
|Parameters|None|
|Return value|List of EmvRevocationIPK. For details, refer to EmvRevocationIPK.|
|Notes||



## 3.4.15 Set Dynamic Reader Limits (DRL) configuration parameters 

|Prototype|int setDRL(int type,<br>android.os.Bundle bundle)|
|---|---|
|Function|Set Dynamic Reader Limits (DRL) configuration parameters|
|Parameters|type: DRL type. Supported card scheme types are as follows:<br>ConstantEmv.POIEmvCoreManager.EmvDrlConstraints.TYPE_VISA<br>ConstantEmv.POIEmvCoreManager.EmvDrlConstraints.TYPE_AMEX|
||bundle: Parameter values. For details, refer<br>to ConstantEmv.POIEmvCoreManager.EmvDrlConstraints.|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



## 3.4.16 Delete Dynamic Reader Limits (DRL) configuration parameters 

|Prototype|int deleteDRL(int type)|
|---|---|
|Function|Delete Dynamic Reader Limits (DRL) configuration parameters|
||type: DRL type. Supported card scheme types are as follows:|
|Parameters|ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_VISA<br>ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_AMEX|
|Return value|0: Success|



28 

Others: Failure. Refer to EmvError for error codes. 

Notes 

## 3.4.17 Retrieve Dynamic Reader Limits (DRL) configuration parameters 

|Prototype|int getDRL(int type,<br>android.os.Bundle bundle)|
|---|---|
|Function|Retrieve Dynamic Reader Limits (DRL) configuration parameters|
|Parameters|type: DRL type. Supported card scheme types are as follows:<br>ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_VISA<br>ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints.TYPE_AMEX|
||bundle: Parameter values. For details, refer<br>to ConstantEmv.POIEmvCoreManager.EmvDrlConstraints.|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



## 3.4.18 Set RuPay terminal parameters 

|Prototype|int setService(android.os.Bundle bundle)|
|---|---|
|Function|Set RuPay terminal parameters|
|Parameters|bundle: Parameter values. Enumerated values are as follows:<br>Bundle_Key: ConstantEmv.POIEmvCoreManager.EmvServiceConstraints.CONFIG<br>Bundle_value: ByteArray in TLV format.<br>For specific parameter values, refer<br>to ConstantEmv.POIEmvCoreManager.EmvServiceConstraints.|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



## 3.4.19 Delete RuPay terminal parameters 

|Prototype|int deleteService()|
|---|---|
|Function|Delete RuPay payment terminal parameters|
|Parameters|None|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



## 3.4.20 Retrieve RuPay terminal parameters 

|Prototype|int getService(android.os.Bundle bundle)|
|---|---|
|Function|Retrieve RuPay payment terminal parameters|
|Parameters|bundle: Parameter values. For details, refer<br>to ConstantEmv.POIEmvCoreManager.EmvServiceConstraints.|



29 

0: Success Return value Others: Failure. Refer to EmvError for error codes. Notes 

## 3.4.21 Set Apple VAS transaction parameters 

|Prototype|int setAppleTerminal(android.os.Bundle bundle)|
|---|---|
|Function|Set Apple VAS transaction parameters|
|Parameters|bundle: Parameter values. For details, refer<br>to ConstantEmv.POIEmvCoreManager.AppleTerminalConstraints.|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



## 3.4.22 Retrieve Apple VAS transaction parameters 

|Prototype|int getAppleTerminal(android.os.Bundle bundle)|
|---|---|
|Function|Retrieve Apple VAS transaction parameters|
|Parameters|bundle: Parameter values. For details, refer<br>to ConstantEmv.POIEmvCoreManager.AppleTerminalConstraints.|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



## 3.4.23 Set Apple Merchant transaction parameters 

|Prototype|int setAppleMerchant(android.os.Bundle bundle)|
|---|---|
|Function|Set Apple Merchant transaction parameters|
|Parameters|bundle: Parameter values. For details, refer<br>to ConstantEmv.POIEmvCoreManager.AppleTerminalConstraints.|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



## 3.4.24 Delete Apple Merchant transaction parameters 

|Prototype|int deleteAppleMerchant()|
|---|---|
|Function|Delete Apple Merchant transaction parameters|
|Parameters|None|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



## 3.4.25 Retrieve Apple Merchant transaction parameters 

|Prototype|int getAppleMerchant(android.os.Bundle bundle)|
|---|---|



30 

|Function|Retrieve Apple Merchant transaction parameters|
|---|---|
|Parameters|bundle: Parameter values. For details, refer<br>to ConstantEmv.POIEmvCoreManager.AppleTerminalConstraints.|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



## 3.4.26 Retrieve Kernel version information 

|Prototype|String getVersion(int type)|
|---|---|
|Function|Retrieve Kernel version information|
|Parameters|type: Kernel type. Enumerated values are as follows:<br>ConstantEmv.POIEmvCoreManager.GET_LIB_VERSION<br>ConstantEmv.POIEmvCoreManager.GET_VERSION_EMV<br>ConstantEmv.POIEmvCoreManager.GET_VERSION_VISA<br>ConstantEmv.POIEmvCoreManager.GET_VERSION_MASTERCARD<br>ConstantEmv.POIEmvCoreManager.GET_VERSION_DISCOVER<br>ConstantEmv.POIEmvCoreManager.GET_VERSION_AMEX<br>ConstantEmv.POIEmvCoreManager.GET_VERSION_MIR<br>ConstantEmv.POIEmvCoreManager.GET_VERSION_RUPAY<br>ConstantEmv.POIEmvCoreManager.GET_VERSION_INTERAC<br>ConstantEmv.POIEmvCoreManager.GET_VERSION_APPLE|
|Return value|Version information|
|Notes||



## 3.4.27 Start EMV transaction 

|Prototype|int startTransaction(android.os.Bundle bundle, IEmvListener callback)|
|---|---|
|Function|Start EMV transaction|
|Parameters|bundle: Transaction parameters. For details, refer<br>to ConstantEmv.POIEmvCoreManager.EmvTransDataConstraints.<br>callback: EMV process callback.|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



## 3.4.28 Stop EMV transaction 

|Prototype|int stopTransaction()|
|---|---|
|Function|Stop EMV transaction|
|Parameters|None|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



31 

## 3.4.29 Set EMV Tag/Object parameters 

|Prototype|int setKernel(byte[] tlv)|
|---|---|
|Function|Set EMV Tag/Object parameters|
|Parameters|tlv: TAG in TLV format.<br>Example: HexUtil.parseHex("9F02060000000000119F0306000000000011").|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes|After setting, this method is only used to change TAG values during the EMV transaction process.<br>The set TAG values cannot be retrieved using getKernel.|



## 3.4.30 Retrieve EMV Tag/Object parameters 

|Prototype|byte[] getKernel(String[] tags)|
|---|---|
|Function|Retrieve EMV Tag/Object parameters|
|Parameters|tags: Array of TAGs. Example: new String[]{"4F", "50", "87", "9F12"}.|
|Return value|TAG values in TLV format.|
|Notes||



## 3.4.31 Set the result of multiple application selection 

|Prototype|int setSelectApplicationResponse(int position)|
|---|---|
|Function|Set the result of multiple application selection|
|Parameters|position: The selected position in the multiple application selection callback data.|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



## 3.4.32 Set card information confirmation result 

|Prototype|int setCardInfoResponse(android.os.Bundle bundle)|
|---|---|
|Function|Set card information confirmation result|
|Parameters|bundle - parameter value<br>For details, see ConstantEmv.POIEmvCoreManager.EmvCardInfoConstraints|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



## 3.4.33 Set Pin input result 

|Prototype|int setPinResponse(android.os.Bundle bundle)|
|---|---|
|Function|Set Pin input result|
|Parameters|bundle - parameter value<br>For details, see ConstantEmv.POIEmvCoreManager.EmvPinConstraints|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|



32 

## Notes 

## 3.4.34 Set the online result 

|Prototype|int setOnlineResponse(android.os.Bundle bundle)|
|---|---|
|Function|Set the online result|
|Parameters|bundle - parameter value<br>For details, see ConstantEmv.POIEmvCoreManager.EmvOnlineConstraints|
|Return value|0: Success<br>Others: Failure. Refer to EmvError for error codes.|
|Notes||



## -- EMV Listener- IEmvListener-- 

|void onConfirmCardInfo(int mode,<br>android.os.Bundle info)|Callback for card information confirmation|
|---|---|
|void onEmvProcess(int type,<br>android.os.Bundle info)|Callback when a card is detected|
|void onKernelType(int type)|Callback for kernel card scheme type|
|void onRequestInputPin(android.os.Bundle info)|Callback to request PIN input|
|void onRequestOnlineProcess(android.os.Bundle info)|Callback to request online processing|
|void onSecondTapCard()|Callback for second card tap|
|void onSelectApplication(List<String> appList,<br>boolean isFirstSelect)|Callback for multiple application selection|
|void onTransactionResult(int resultCode,<br>android.os.Bundle info)|Callback for transaction result|



## 3.4.35 Detect card 

|Prototype|void onEmvProcess(int type,<br>android.os.Bundle info)|
|---|---|
|Function|Detect card|
|Parameters|Parameter:<br>type: Card type. Enumerated values are as follows:<br>ConstantEmv.POIEmvCoreManager.DEVICE_CONTACT<br>ConstantEmv.POIEmvCoreManager.DEVICE_CONTACTLESS<br>ConstantEmv.POIEmvCoreManager.DEVICE_MAGSTRIPE<br>ConstantEmv.POIEmvCoreManager.DEVICE_MIFARE_CLASSIC<br>ConstantEmv.POIEmvCoreManager.DEVICE_MIFARE_ULTRALIGHT<br>ConstantEmv.POIEmvCoreManager.DEVICE_MIFARE_PLUS<br>ConstantEmv.POIEmvCoreManager.DEVICE_MIFARE_DESFIRE<br>info: Card information parameters.|
|Return value||
|Notes|If the transaction card type is a magnetic stripe card,<br>the card data will be returned in this Bundle. For specific parameter constants, refer to|



33 

## ConstantEmv.POIEmvCoreManager.EmvCardInfoConstraints. 

## 3.4.36 Multiple application selection callback 

|Prototype|void onSelectApplication(List<String> appList,<br>boolean isFirstSelect)|
|---|---|
|Function|Multiple application selection callback|
|Parameters|Parameter:<br>appList: Application selection list.<br>isFirstSelect: Whether it is the first selection.|
|Return value||
|Notes||



## 3.4.37 Card information confirmation callback 

|Prototype|void onConfirmCardInfo(int mode,<br>android.os.Bundle info)|
|---|---|
|Function|Card information confirmation callback|
|Parameters<br>Return value|Parameter:<br>mode: Current mode. Enumerated values are as follows:<br>ConstantEmv.POIEmvCoreManager.CMD_TRY_OTHER_APPLICATION<br>ConstantEmv.POIEmvCoreManager.CMD_AMOUNT_CONFIG<br>ConstantEmv.POIEmvCoreManager.CMD_ISSUER_REFERRAL<br>ConstantEmv.POIEmvCoreManager.CMD_GPO_FILTER<br>ConstantEmv.POIEmvCoreManager.CMD_READ_RECORD_FILTER<br>ConstantEmv.POIEmvCoreManager.CMD_SELECT_APPLICATION<br>ConstantEmv.POIEmvCoreManager.CMD_READ_RECORD<br>ConstantEmv.POIEmvCoreManager.CMD_GAC1<br>ConstantEmv.POIEmvCoreManager.CMD_GAC2<br>ConstantEmv.POIEmvCoreManager.CMD_SELECT_KERNEL<br>ConstantEmv.POIEmvCoreManager.CMD_SELECT_AFTER<br>ConstantEmv.POIEmvCoreManager.CMD_GPO_BEFORE<br>ConstantEmv.POIEmvCoreManager.CMD_CARD_READ_SUCCESS<br>info: Card information data. For specific parameter constants, refer<br>to ConstantEmv.POIEmvCoreManager.EmvCardInfoConstraints.|
|Notes||



## 3.4.38 Kernel card scheme type callback 

|Prototype|void onKernelType(int type)|
|---|---|
|Function|Kernel card scheme type callback|
|Parameters|Parameter:<br>type: Kernel card scheme type. Enumerated values are as follows:|
||ConstantEmv.POIEmvCoreManager.EMV_CARD_NOT|



34 

||ConstantEmv.POIEmvCoreManager.EMV_CARD_VISA<br>ConstantEmv.POIEmvCoreManager.EMV_CARD_UNIONPAY<br>ConstantEmv.POIEmvCoreManager.EMV_CARD_MASTERCARD<br>ConstantEmv.POIEmvCoreManager.EMV_CARD_DISCOVER<br>ConstantEmv.POIEmvCoreManager.EMV_CARD_AMEX<br>ConstantEmv.POIEmvCoreManager.EMV_CARD_JCB<br>ConstantEmv.POIEmvCoreManager.EMV_CARD_MIR<br>ConstantEmv.POIEmvCoreManager.EMV_CARD_RUPAY<br>ConstantEmv.POIEmvCoreManager.EMV_CARD_PURE<br>ConstantEmv.POIEmvCoreManager.EMV_CARD_INTERAC<br>ConstantEmv.POIEmvCoreManager.EMV_CARD_EFTPOS|
|---|---|
|Return value||
|Notes|1. This parameter is only returned for contactless card types.<br>2. For contact transactions and magnetic stripe transactions, this parameter will not be returned.|



## 3.4.39 Second tap card callback 

|Prototype|void onSecondTapCard()|
|---|---|
|Function|Second tap card callback|
|Parameters||
|Return value||
|Note||



## 3.4.40 Request PIN input callback 

|Prototype|void onRequestInputPin(android.os.Bundle info)|
|---|---|
|Function|Request PIN input callback|
|Parameters|Parameter:<br>info: PIN parameters.<br>For details, refer to ConstantEmv.POIEmvCoreManager.EmvPinConstraints.|
|Return value||
|Notes||



## 3.4.41 Request online process callback 

|Prototype|void onRequestOnlineProcess(android.os.Bundle info)|
|---|---|
|Function|Request online process callback|
|Parameters|Parameter:<br>info: Online parameters.<br>For details, refer to ConstantEmv.POIEmvCoreManager.EmvOnlineConstraints.|
|Return value||
|Notes||



## 3.4.42 Transaction result callback 

|Prototype|void onTransactionResult(int resultCode,|
|---|---|



35 

||android.os.Bundle info)|
|---|---|
|Function|Transaction result callback|
|Parameters|Parameter:<br>resultCode: Transaction result code.<br>0: Success|
||Others: Failure.<br>Refer to ConstantEmv.PosEmvErrorCode/EmvError.<br>info: Transaction data.|
||For details, refer to ConstantEmv.POIEmvCoreManager.EmvResultConstraints.|
|Return value||
|Notes||



##  3.5 General Operation function 

- -- Get general operation module - getGeneralManager -- 

|int setTime(long time)|Set time|
|---|---|
|long getTime()|Get time|
|int setTimeZone(String timeZone)|Set time zone|
|String getTimeZone()|Get time zone|
|int setBeep(boolean enable,<br>int times,<br>int freq)|Set Beep|
|int setNavigationBar(int type,<br>boolean isHide)|Set navigation bar|
|int setStatusBar(boolean isHide)|Set status bar|
|int wakeUp()|Wake up device|
|int shutdown()|Shut down device|
|string getSystemProperty(String key)|Get system property value|
|setSystemProperty(String key,<br>String value)|Set system property value|
|int reboot()|Reboot device|
|int setScreenRotation(boolean enable)|Configure screen rotation functionality|
|void led(int type,<br>int status)|Switches the LED light between ON and<br>OFF states|
|void setLedVisible(boolean visible)|Sets the visibility of the LedView|
|String checkDependencyVersion(int type)|Checks the version of dependency library|



## 3.5.1 Set buzzer 

|Prototype|int setBeep(boolean enable,<br>int times,<br>int freq)|
|---|---|
|Function|Set Beep|
|Parameters|Parameters:|



36 

||enable - begin or close buzzer<br>times - buzzer duration<br>freq - buzzer frequency|
|---|---|
|Return value|Return:<br>0: success|
||Others: failure - see GeneralError|
|Notes||



## 3.5.2 Set system navigation bar 

|Prototype|int setNavigationBar(int type,<br>boolean isHide)|
|---|---|
|Function|Set system navigation bar|
|Parameters|Parameters:<br>type - the type of navigation bar to be operated<br>1: back 2: home 3: recent<br>isHide - the state of the navigation bar to be operated<br>true: hidden false: displayed|
|Return value|Return:<br>0: success<br>Others: failure - see GeneralError|
|Notes||



## 3.5.3 Set system status bar 

|Prototype|int setStatusBar(boolean isHide)|
|---|---|
|Function|Set system status bar|
|Parameters|isHide - the state of the navigation bar to be operated<br>true: hidden false: displayed|
|Return value|Return:<br>0: success|
||Others: failure - see GeneralError|
|Notes||



## 3.5.4 Wake up device 

|Prototype|int wakeUp()|
|---|---|
|Function|Wake up device|
|Parameters||
|Return value|Return:<br>0: success|
||Others: failure - see GeneralError|
|Notes||



37 

## 3.5.5 Set system time 

|Prototype|int setTime(long time)|
|---|---|
|Function|Set system time|
|Parameters||
|Return value|Return:<br>0: success|
||Others: failure - see GeneralError|
|Notes||



## 3.5.6 Get time from system 

|Prototype|int getTime()|
|---|---|
|Function|Get time from system|
|Parameters||
|Return value|Return:<br>0: success|
||Others: failure - see GeneralError|
|Notes||



## 3.5.7 Get time zone 

|Prototype|String getTimeZone()|
|---|---|
|Function|Get time zone|
|Parameters||
|Return value|Returns:<br>Time zone|
|Notes||



## 3.5.8 Set time zone 

|Prototype|int setTimeZone(String timeZone)|
|---|---|
|Function|Set time zone|
|Parameters|Parameters:<br>timeZone - time zone id, supports two formats:<br>1. Region/City<br>2. GMT<br>Example 1: setTimeZone("Europe/Moscow")<br>Example 2: setTimeZone("GMT+9")|
|Return value|Return:<br>0: success<br>Others: failure - see GeneralError|
|Notes||



## 3.5.9 Reboot device 

|Prototype<br>int reboot()|
|---|



38 

|Function|Reboot device|
|---|---|
|Parameters||
|Return value|Return:<br>0: success|
||Others: failure - see GeneralError|
|Notes||



## 3.5.10 Shut down device 

|Prototype|int shutdown()|
|---|---|
|Function|Shut down device|
|Parameters||
|Return value|Return:<br>0: success|
||Others: failure - see GeneralError|
|Notes||



## 3.5.11 Set system property value 

|Prototype|int setSystemProperty(String key, String value)|
|---|---|
|Function|Set system property value|
|Parameters|Parameters:<br>key - the key of the system property(ro.product.model)<br>value - the value of the system property|
|Return value|Return:<br>0: success<br>Others: failure - see GeneralError|
|Notes||



## 3.5.12 Get system property value 

|Prototype|String getSystemProperty(String key)|
|---|---|
|Function|Get system property value|
|Parameters|Parameters:<br>key - the key of the system property (ro.product.model)|
|Return value|Return:<br>Return the value of the system property by string form, or null if it cannot be read|
|Notes||



## 3.5.13 Configures screen rotation function 

|Prototype|int setScreenRotation(boolean enable)|
|---|---|
|Function|Configures screen rotation function|
|Parameters|Parameters:<br>enable|
||- true: Enable screen rotation|



39 

||- false: Disable screen rotation|
|---|---|
|Return value|0: Success<br>Others: failure - see GeneralError|
|Notes||



## 3.5.14 Toggle LED light on/off 

|Prototype|void led(int type,<br>int status)|
|---|---|
|Function|Toggle LED light on/off|
|Parameters|Parameters:<br>type - LED light type (range: 1–4):<br>1 - Blue<br>2 - Yellow<br>3 - Green<br>4 - Red<br>status- Switch state:<br>1 - Turn on<br>0 - Turn off|
|Return Value||
|Notes|This method is used to control the on/off state of different colored LED lights.|



## 3.5.15 Show or hide the LED view 

|Prototype|void setLedVisible(boolean visible)|
|---|---|
|Function|Show or hide the LED view|
|Parameters|Parameters:<br>visible<br>true: show the LED view<br>false: hide the LED view|
|Return Value||
|Notes|Controls the visibility of the on-screen LED view component.|



## 3.5.16 Checks the version of dependency library 

|Prototype|String checkDependencyVersion(int type)|
|---|---|
|Function|Checks the version of dependency library|
|Parameters|Parameters:<br>type - Dependency type:<br>0 - EMV kernel<br>1 - POI<br>2 - UART<br>3 - Scanner box|
|Return Value|Return:<br>Version string of the specified dependency|
|Notes|This method returns the version number of the selected module by type.|



40 

##  3.6 PINPAD Operation module 

-- Get pinpad module - getPinpadManager -- 

|void cancelInputPin()|Cancel PIN entry|
|---|---|
|void startInputPin(android.os.Bundle params,|Start Input PIN, take over TP according to the|
|android.app.Activity parent,|default view|
|PinpadInputCallback callback)||
|void startInputPin(android.os.Bundle params,<br>android.content.Context context,|Start Input PIN, take over TP according to the<br>incoming view|
|Map<String,android.view.View> keyViews,||
|PinpadInputCallback callback)||



## 3.6.1 Start Input PIN by provided view 

|Prototype|void startInputPin(android.os.Bundle params,<br>android.content.Context context,<br>Map<String,android.view.View> keyViews,<br>PinpadInputCallback callback)|
|---|---|
|Function|Start input PIN, take over TP according to the provided view|
|Parameters|Parameters:<br>params - PIN configuration parameters<br>for detailed information refer to ConstantEmv.POIEmvCoreManager.EmvPinConstraints<br>keyViews - a collection of views, pass in the following format:<br>Map<String, View> keyViews = new HashMap<>();<br>keyViews.put(PinViewEnum.BUTTON0.getType(), button0);<br>keyViews.put(PinViewEnum.BUTTON1.getType(), button1);<br>keyViews.put(PinViewEnum.BUTTON2.getType(), button2);<br>keyViews.put(PinViewEnum.BUTTON3.getType(), button3);<br>keyViews.put(PinViewEnum.BUTTON4.getType(), button4);<br>keyViews.put(PinViewEnum.BUTTON5.getType(), button5);<br>keyViews.put(PinViewEnum.BUTTON6.getType(), button6);<br>keyViews.put(PinViewEnum.BUTTON7.getType(), button7);<br>keyViews.put(PinViewEnum.BUTTON8.getType(), button8);<br>keyViews.put(PinViewEnum.BUTTON9.getType(), button9);<br>keyViews.put(PinViewEnum.BUTTON_ENTER.getType(), buttonEnter);<br>keyViews.put(PinViewEnum.BUTTON_CLEAR.getType(), buttonClear);<br>keyViews.put(PinViewEnum.BUTTON_ESC.getType(), buttonEsc);<br>context - the context object<br>callback - the PIN input callback|
|Return value||
|Notes|take over TP according to the provided view|



41 

## 3.6.2 Start Input PIN by default view 

|Prototype|void startInputPin(android.os.Bundle params,<br>android.app.Activity parent,<br>PinpadInputCallback callback)|
|---|---|
|Function|Start input PIN, take over TP according to the default view|
|Parameters|Parameters:<br>params - PIN configuration parameters<br>Detail information refer to ConstantEmv.POIEmvCoreManager.EmvPinConstraints<br>parent - display the default view based on the provided activity<br>callback - the PIN input callback|
|Return value||
|Notes|take over TP according to the default view|



## 3.6.3 Cancel PIN entry 

|Prototype|void cancelInputPin()|
|---|---|
|Function|Cancel PIN entry|
|Parameters||
|Return value||
|Notes||



## -- PIN input callback - PinpadInputCallback -- 

|void onInput(int len,|Key press event|
|---|---|
|int key)||
|void onPinError(int verifyResult,|Callback on error|
|int pinTryCntOut)||
|void onPinSuccess(int verifyResult,|Invoked when the user confirms PIN entry|
|byte[] pinBlock,||
|String ksn)||
|void onScreenRotation()|Screen rotation callback|



## 3.6.4 Key press event 

|Prototype|void onInput(int len,<br>int key)|
|---|---|
|Function|Key press event|
|Parameters|Parameters:<br>len - The length of the password that has been entered<br>key - The current key value, uniformly returns *|
|Return value||
|Notes||



42 

## 3.6.5 Error callback 

|Prototype|void onPinError(int verifyResult,<br>int pinTryCntOut)|
|---|---|
|Function|Error callback|
|Parameters|Parameters:<br>verifyResult - Error code, refer to ConstantEmv.POIEmvCoreManager.EmvPinConstraints<br>pinTryCntOut - Number of PIN retry attempts|
|Return value||
|Notes||



## 3.6.6 Return when confirm PIN input 

|Prototype|void onPinSuccess(int verifyResult, byte[] pinBlock, String ksn)|
|---|---|
|Function|Return when confirm PIN input|
|Parameters|Parameters:<br>verifyResult - The result of the PIN confirmation, 0 means successful PIN confirmation.<br>pinBlock - The ciphertext of the password calculated by PINPAD.<br>ksn - Will returned when DUKPT type|
|Return value||
|Notes||



## 3.6.7 Screen rotation callback when use PINPAD 

|Prototype|void onScreenRotation()|
|---|---|
|Function|Screen rotation callback when use PINPAD|
|Parameters||
|Return Value||
|Notes|This method is triggered when the screen orientation changes.|



##  3.7 Printing Operation module 

-- Get printing operation module - getPrinterManager -- 

|int addBarcode(BarcodePrintLine barcodeLine)|Add the barcode to the print cache.|
|---|---|
|int addBitmap(BitmapPrintLine bitmapLine)|Add the bitmap to the print cache.|
|int addText(TextPrintLine textLine)|Add the text to the print cache.|
|int addText(List<TextPrintLine> textLines)|Add a line of text with different styles to the<br>print cache.|
|void close()|Turn off the printer.|
|int feedPaper(int lines)|Perform the paper feeding action before<br>starting printing. The main purpose is to<br>adjust the paper position of the printer to<br>ensure that the printed content can be output<br>accurately.|



43 

|int getPrinterStatus()|Get the current status of the printer.|
|---|---|
|int open()|Turn on the printer.|
|int setFont(String path)|Set print font.|
|int setGray(int gray)|Set print grayscale.|
|int setLineSpace(int line)|Set print line space.|
|int startPrint(ConstantPrinter.PrintFailurePolicy failurePolicy,<br>IPrintResultCallback callback)|Execute print job to print the buffered content|
|int startPrint(IPrintResultCallback callback)|Trigger the printing operation to print out the<br>content in the print cache.|
|int wrapLine(int lines)|Add a blank line to the print cache, which<br>serves the function of line breaking.|



## 3.7.1 Open the printer module 

|Prototype|int open()|
|---|---|
|Function|Open the printer module|
|Parameters||
|Return value|Return:<br>0: The operation is successfully executed;<br>Others: The operation fails.<br>For the specific meaning of the error code, please refer to the definitions in PrinterError and<br>CommonError|
|Notes||



## 3.7.2 Get the current status of the printer 

|Prototype|int getPrinterStatus()|
|---|---|
|Function|Get the current status of the printer.|
|Parameters||
|Return value|Return:<br>A negative return value: The operation fails;|
||For the specific meaning of the error code, please refer to the definitions in the PrinterError and<br>CommonError.|
||A positive return value: The operation is successfully executed;<br>For the specific meaning of the status code, please refer to the definitions in the ConstantPrint.|
|Notes|Must be used after the printer has been opened.|



## 3.7.3 Feed paper before starting printing 

|Prototype|int feedPaper(int lines)|
|---|---|
|Function|It is used to feed paper before starting the printing process.|
|Parameters|Parameters:|
||lines - The number of lines to feed (must be an integer greater than 0).|
|Return value|Return:|



44 

||0: The operation is successfully executed;<br>Others: The operation fails.<br>For the specific meaning of the error code, please refer to the definitions in PrinterError and<br>CommonError|
|---|---|
|Notes|The main purpose is to adjust the printer's paper position, ensuring accurate print output.<br>Must be used after the printer has been opened.|



## 3.7.4 Set print grayscale 

|Prototype|int setGray(int gray)|
|---|---|
|Function|Set print grayscale|
|Parameters|Parameters:<br>gray - Default: 1200.<br>Smaller values make prints lighter; larger values make prints darker. Adjust in steps of 200 for<br>consistent results.|
|Return value|Return:<br>0: The operation is successfully executed;<br>Others: The operation fails.<br>For the specific meaning of the error code, please refer to the definitions in PrinterError and<br>CommonError|
|Notes|Must be used after the printer has been opened|



## 3.7.5 Set print font 

|Prototype|int setFont(String path)|
|---|---|
|Function|Set print font|
|Parameters|Parameters:<br>path - The path to the font resource package.|
|Return value|Return:<br>0: The operation is successfully executed;<br>Others: The operation fails.<br>For the specific meaning of the error code, please refer to the definitions in PrinterError and<br>CommonError|
|Notes|Must be used after the printer has been opened.|



## 3.7.6 Set print line space 

|Prototype|int setLineSpace(int line)|
|---|---|
|Function|Set print line space|
|Parameters|Parameters:<br>line - An integer greater than 0.|
|Return value|Return:<br>0: The operation is successfully executed;<br>Others: The operation fails.<br>For the specific meaning of the error code, please refer to the definitions in PrinterError and<br>CommonError|



45 

|Notes<br>Must be used|after the printer has been opened.|
|---|---|



## 3.7.7 Add the text to the print cache 

|Prototype|int addText(TextPrintLine textLine)|
|---|---|
|Function|Add the text to the print cache|
|Parameters|Parameters:<br>textLine - TextPrintLine type(used to define the text content and style for printing).|
|Return value|Return:<br>0: The operation is successfully executed;<br>Others: The operation fails.<br>For the specific meaning of the error code, please refer to the definitions in PrinterError and<br>CommonError|
|Notes|Must be used after the printer has been opened.|



## 3.7.8 Add a line of text with different styles to the print cache 

|Prototype|int addText(List<TextPrintLine> textLines)|
|---|---|
|Function|Add a line of text with different styles to the print cache|
|Parameters|Parameters:<br>textLines - An array of the TextPrintLine type. Recommend to use the TextPrintLineHelper utility<br>class to construct the TextPrintLine array. This can simplify the development process and make<br>the calls clearer and more straightforward.|
|Return value|Return:<br>0: The operation is successfully executed;<br>Others: The operation fails.<br>For the specific meaning of the error code, please refer to the definitions in PrinterError and<br>CommonError|
|Notes|Must be used after the printer has been opened.|



## 3.7.9 Add a blank line to the print cache 

|Prototype|int wrapLine(int lines)|
|---|---|
|Function|Add a blank line to the print cache, which serves the function of line breaking|
|Parameters|Parameters:<br>lines - Number of lines, an integer greater than 0.|
|Return value|Return:<br>0: The operation is successfully executed;<br>Others: The operation fails.<br>For the specific meaning of the error code, please refer to the definitions in PrinterError and<br>CommonError|
|Notes|Must be used after the printer has been opened.|



46 

## 3.7.10 Add the barcode to the print cache 

|Prototype|int addBarcode(BarcodePrintLine barcodeLine)|
|---|---|
|Function|Add the barcode to the print cache|
|Parameters|Parameters:<br>barcodeLine - BarcodePrintLine Type. Used to define the print content, type, size, and style of the<br>barcode.|
|Return value|Return:<br>0: The operation is successfully executed;<br>Others: The operation fails.<br>For the specific meaning of the error code, please refer to the definitions in PrinterError and<br>CommonError|
|Notes|Must be used after the printer has been opened.|



## 3.7.11 Add the bitmap to the print cache 

|Prototype|int addBitmap(BitmapPrintLine bitmapLine)|
|---|---|
|Function|Add the bitmap to the print cache|
|Parameters|Parameters:<br>bitmapLine - BitmapPrintLine Type. Used to define the picture to be printed and its style.|
|Return value|Return:<br>0: The operation is successfully executed;<br>Others: The operation fails.<br>For the specific meaning of the error code, please refer to the definitions in PrinterError and<br>CommonError|
|Notes|Must be used after the printer has been opened.|



## 3.7.12 Trigger the printing operation to print out the content in the print cache 

|Prototype|int startPrint(ConstantPrinter.PrintFailurePolicy failurePolicy,<br>IPrintResultCallback callback)|
|---|---|
|Function|Execute print job to print the buffered content|
|Parameters|Parameters:<br>failurePolicy - Policy for handling failed prints (how to process remaining tasks after a single print<br>failure);<br>callback - Callback for monitoring print status.|
|Return value|Return:<br>0: The operation is successfully executed;<br>Others: The operation fails.<br>For the specific meaning of the error code, please refer to the definitions in PrinterError and<br>CommonError|
|Notes|Must be used after the printer has been opened|



## 3.7.13 Trigger the printing operation to print out the content in the print cache 

|Prototype<br>int startPrint(IPrintResultCallback callback)|
|---|



47 

|Function|Trigger the printing operation to print out the content in the print cache|
|---|---|
|Parameters|Parameters:<br>callback - Monitor the status callbacks of the printing process.|
|Return value|Return:<br>0: The operation is successfully executed;<br>Others: The operation fails.<br>For the specific meaning of the error code, please refer to the definitions in PrinterError and<br>CommonError|
|Notes|Must be used after the printer has been opened.|



## 3.7.14 Turn off the printer 

|Prototype|void close()|
|---|---|
|Function|Turn off the printer|
|Parameters||
|Return value||
|Notes|Turn off the printer and release related resources after the printing operation is completed.|



## -- Print result callback - IPrintResultCallback -- 

|void onFinish()|Callback when the printing finishes successfully.|
|---|---|
|void onError(int error, String msg)|Callback when an error occurs during the printing process.|



## 3.7.15 Callback when the printing finishes successfully 

|Prototype|void onFinish()|
|---|---|
|Function|Callback when the printing finishes successfully|
|Parameters||
|Return value||
|Notes|Developers can handle the post - printing - success logic in this method, such as releasing<br>resources, updating the interface status, and closing the printer using the close method.|



## 3.7.16 Callback when an error occurs during the printing process 

|Prototype|void onError(int error,String msg)|
|---|---|
|Function|Callback when an error occurs during the printing process|
|Parameters|Parameters:<br>error - Error code. Refer to PrinterError.<br>msg - Error message.|
|Return value||
|Notes|When receiving the PRINTER_ERROR_PRINT error type, you can try closing the printer first,<br>then reopening it and retrying.|



48 

##  3.8 Security module 

-- Get security module - getSecurityManager -- 

|int calcDes(int keyIndex,<br>3DES data encryption and decryption|
|---|
|int mode,|
|byte[] dataIn,<br>byte[] dataOut)|
|int calcDukpt(int index,<br>int keyMode,<br>int algType,<br>int mode,<br>byte[] dataIn,<br>byte[] aesIv,<br>Encrypt or decrypt data using DUKPT|
|byte[] dataOut)|
|int calcMac(int index,<br>int keyType,<br>byte[] iv,<br>byte[] dataIn,<br>Calculate MAC|
|byte[] dataOut)|
|int calcMacDukpt(int index,<br>int keyType,<br>int mode,<br>Calculate MAC using DUKPT|
|byte[] dataIn,|
|byte[] dataOut)|
|int calcRsa(int keyIndex,<br>int mode,<br>byte[] dataIn,<br>RSA encryption or decryption|
|byte[] dataOut)|
|int generateRsaKey(int pubKeyIndex,<br>int priKeyIndex, int size)<br>Generate RSA keys|
|int getKCV(int keyIndex,<br>int keyType,<br>Get the KCV for the corresponding key type|
|byte[] kcvOut)|
|int getKsnDukpt(int index,<br>byte[] ksnOut)<br>Get the KSN value by specified index|
|int getRandom(int length,<br>byte[] keyOut)<br>Get random numbers compliant with NIST<br>SP800-90A|
|int increaseKsnDukpt(int index)<br>Increase KSN|
|int readRsaKey(int keyIndex,<br>Read RSA keys|
|byte[] keyOut,<br>byte[] modulusOut,|
|byte[] exponentOut)|
|int writeKey(int srcKeyType,<br>Write keys|
|49<br>int srcKeyIndex,|



|int keyType,||
|---|---|
|int index,||
|byte[] keyIn, byte[] kcv)||
|int writeKeyDukpt(int index,<br>byte[] keyIn,|Write DUKPT keys|
|byte[] ksnIn,||
|byte[] kcv)||
|int writeRsaKey(int keyIndex,<br>byte[] modulus,|Write RSA keys|
|byte[] exponent)||
|int calcMacDukptDes(int tikIndex,|Calculate MAC using DUKPT_TDES|
|int operationMode,|algorithm|
|int ksnMode,||
|int macAlgorithm,||
|byte[] dataIn,||
|byte[] aesIv,||
|byte[] dataOut,||
|byte[] usedKsn)||
|int eraseAllKey()|Erase all keys|
|int writeKeyDukptDes(int tikIndex,|Write DUKPT_DES keys|
|int tlkIndex,||
|byte[] keyIn,||
|byte[] ksnIn,||
|byte[] kcv)||
|int writeKeyMKSK(int srcKeyType,<br>int srcKeyIndex,<br>int keyType,|Inject MK/SK keys|
|int encryptionAlgorithm,||
|int index,||
|byte[] keyIn,||
|byte[] kcv)||



## 3.8.1 Write keys 

|Prototype|int writeKey(int srcKeyType,<br>int srcKeyIndex,<br>int keyType,<br>int index,<br>byte[] keyIn,<br>byte[] kcv)|
|---|---|
|Function|Write keys|
|Parameters|Parameters:<br>srcKeyType - Source key type<br>Such as ConstantSecurity.PED_TLK, ConstantSecurity.PED_TMK<br>srcKeyIndex - Source keyindex|



50 

||the key index used to decrypt the target key.<br>TLK: Only 1 group is supported, and the index range is [1,1].<br>TMK: Supports 64 groups, the index range is [1, 64].<br>TPK: Supports 64 groups, the index range is [1, 64].<br>TAK: Supports 64 groups, the index range is [1, 64].<br>TDK: Supports 64 groups, the index range is [1, 64].<br>TEK: Supports 64 groups, the index range is [1, 64].<br>TTK: Supports 64 groups, the index range is [1, 64].<br>Note: TPK, TAK, TDK, TEK, TTK share the index space. Key indexes can not be duplicated. If<br>duplication occurs, the later written key will overwrite the previously injected key.<br>keyType - Key type, as follows:<br>ConstantSecurity.PED_TLK<br>ConstantSecurity.PED_TMK<br>ConstantSecurity.PED_TPK<br>ConstantSecurity.PED_TAK<br>ConstantSecurity.PED_TDK<br>ConstantSecurity.PED_TEK<br>ConstantSecurity.PED_TTK<br>index - Key index, as follows:<br>TLK: Only 1 group is supported, and the index range is [1,1].<br>TMK: Supports 64 groups, the index range is [1, 64].<br>TPK: Supports 64 groups, the index range is [1, 64].<br>TAK: Supports 64 groups, the index range is [1, 64].<br>TDK: Supports 64 groups, the index range is [1, 64].<br>TEK: Supports 64 groups, the index range is [1, 64].<br>TTK: Supports 64 groups, the index range is [1, 64].<br>Note: TPK, TAK, TDK, TEK, TTK share the index space. Key indexes should not be duplicated. If<br>duplication occurs, the later written key will overwrite the previously injected key.<br>keyIn - Key information, supported key lengths: [8, 16, 24, 32]<br>kcv - KCV value|
|---|---|
|Return value<br>Notes|Returns:<br>0: The operation is successfully executed;<br>Others: The operation fails. Please refer to SecurityError for more information|



## 3.8.2 Write DUKPT keys 

|Prototype|int writeKeyDukpt(int index,<br>byte[] keyIn,<br>byte[] ksnIn,<br>byte[] kcv)|
|---|---|
|Function|Write DUKPT keys|
|Parameters|Parameters:|
||index - Key index (supported range 1～10)|



51 

||keyIn - Key data<br>ksnIn - Initialization KSN<br>kcv - KCV value|
|---|---|
|Return value|Returns:|
||0: The operation is successfully executed;|
||Others: The operation fails. Please refer to SecurityError for more information|
|Notes||



## 3.8.3 Generate RSA keys 

|Prototype|int generateRsaKey(int pubKeyIndex,<br>int priKeyIndex,<br>int size)|
|---|---|
|Function|Generate RSA keys|
|Parameters|Parameters:<br>pubKeyIndex - Public key index<br>priKeyIndex - Private key index<br>size - Key size|
|Return value|Returns:<br>0: The operation is successfully executed;<br>Others: The operation fails. Please refer to SecurityError for more information|
|Notes|Note: The key index range is 1～4, only support two sets of public and private keys.|



## 3.8.4 Write RSA keys 

|Prototype|int writeRsaKey(int keyIndex,<br>byte[] modulus,<br>byte[] exponent)|
|---|---|
|Function|Write RSA keys|
|Parameters|Parameters:<br>keyIndex - Key index<br>modulus - Modulus<br>exponent - Exponent|
|Return value|Returns:<br>0: The operation is successfully executed;<br>Others: The operation fails. Please refer to SecurityError for more information|
|Notes|Note: If the length of the exponent is less than the length of the modulus, the key type written is a<br>public key. If the length of the exponent is equal to the length of the modulus, the key type written<br>is private key.|



## 3.8.5 Read RSA keys 

|Prototype|int readRsaKey(int keyIndex,|
|---|---|
||byte[] keyOut,|
||byte[] modulusOut,|
||byte[] exponentOut)|



52 

|Function|Read RSA keys|
|---|---|
|Parameters|Parameters:<br>keyIndex - Key index<br>keyOut - Key information<br>modulusOut - Key modulus<br>exponentOut - Key exponent|
|Return value|Returns:<br>0: The operation is successfully executed;<br>Others: The operation fails. Please refer to SecurityError for more information|
|Notes||



## 3.8.6 Get random numbers 

|Prototype|int getRandom(int length,<br>byte[] keyOut)|
|---|---|
|Function|Get random numbers compliant with NIST SP800-90A|
|Parameters|Parameters:<br>length - Length of the random number<br>keyOut - Random number|
|Return value|Returns:<br>0: The operation is successfully executed;<br>Others: The operation fails. Please refer to SecurityError for more information|
|Notes||



## 3.8.7 Get KCV for the corresponding key type 

|Prototype|int getKCV(int keyIndex,<br>int keyType,<br>byte[] kcvOut)|
|---|---|
|Function|Get KCV for the corresponding key type|
|Parameters|Parameters:<br>keyIndex - Key index<br>keyType - Key type, as follows:<br>ConstantSecurity.PED_TLK<br>ConstantSecurity.PED_TMK<br>ConstantSecurity.PED_TPK<br>ConstantSecurity.PED_TAK<br>ConstantSecurity.PED_TDK<br>ConstantSecurity.PED_TEK<br>ConstantSecurity.PED_TIK<br>ConstantSecurity.PED_TTK<br>kcvOut - KCV value|
|Return value|Returns:<br>0: The operation is successfully executed;<br>Others: The operation fails. Please refer to SecurityError for more information|



53 

Notes 

## 3.8.8 Encrypt or decrypt data by DUKPT 

|Prototype|int calcDukpt(int index,<br>int keyMode,<br>int algType,<br>int mode,<br>byte[] dataIn,<br>byte[] aesIv,<br>byte[] dataOut)|
|---|---|
|Function|Encrypt or decrypt data by DUKPT|
|Parameters|Parameters:<br>index - Key index<br>keyMode - Dukpt mode, can be as follows<br>ConstantSecurity.PED_CALC_DUKPT_MODE_DEC: 0x01<br>ConstantSecurity.PED_CALC_DUKPT_MODE_ENC: 0x02<br>algType - Algorithm type, can be as follows<br>ConstantSecurity.KEY_ALG_TYPE_2TDEA<br>ConstantSecurity.KEY_ALG_TYPE_3TDEA<br>ConstantSecurity.KEY_ALG_TYPE_AES_128<br>ConstantSecurity.KEY_ALG_TYPE_AES_192<br>ConstantSecurity.KEY_ALG_TYPE_AES_256<br>mode - Operation Mode, can be as follows<br>ConstantSecurity.DUKPT_MAC_MODE_ECB: ECB Mode.<br>ConstantSecurity.DUKPT_MAC_MODE_CBC: CBC Mode.<br>dataIn - Input data<br>aesIv - Initialization vector, pass null for ECB mode, or pass an 8-byte vector for other encryption<br>modes<br>dataOut - Encrypted data|
|Return value|Returns:<br>0: The operation is successfully executed;<br>Others: The operation fails. Please refer to SecurityError for more information|
|Notes||



## 3.8.9 RSA encryption or decryption 

|Prototype|int calcRsa(int keyIndex, int mode, byte[] dataIn, byte[] dataOut)|
|---|---|
|Function|RSA encryption or decryption|
|Parameters|Parameters:<br>keyIndex - Key index|
||mode - RSA padding Mode, can be as follows|
||ConstantSecurity.PED_CALC_RSA_MODE_NO_PADDING|
||ConstantSecurity.PED_CALC_RSA_MODE_PKCS1_PADDING|
||ConstantSecurity.PED_CALC_RSA_MODE_OAEP_PADDING|



54 

||dataIn - Input data<br>dataOut - Encrypted or decrypted data|
|---|---|
|Return value|Returns:|
||0: The operation is successfully executed;|
||Others: The operation fails. Please refer to SecurityError for more information|
|Notes||



## 3.8.10 3DES data encryption and decryption 

|Prototype|int calcDes(int keyIndex,<br>int mode,<br>byte[] dataIn,<br>byte[] dataOut)|
|---|---|
|Function|3DES data encryption and decryption|
|Parameters|Parameters:<br>keyIndex - Index of TDK.<br>mode - Encryption and Decryption Mode, can be as follows<br>ConstantSecurity.PED_CALC_DES_MODE_ECB_DEC<br>ConstantSecurity.PED_CALC_DES_MODE_ECB_ENC<br>ConstantSecurity.PED_CALC_DES_MODE_CBC_DEC<br>ConstantSecurity.PED_CALC_DES_MODE_CBC_ENC<br>dataIn - Input data<br>dataOut - Encrypted or decrypted data|
|Return value|Returns:<br>0: The operation is successfully executed;<br>Others: The operation fails. Please refer to SecurityError for more information|
|Notes||



## 3.8.11 Calculate MAC 

|Prototype|int calcMac(int index,<br>int keyType,<br>byte[] iv,<br>byte[] dataIn,<br>byte[] dataOut)|
|---|---|
|Function|Calculate MAC|
|Parameters|Parameters:<br>index - TAK key index<br>keyType - MAC algorithm type, can be as follows<br>ConstantSecurity.MAC_MODE_CBC: CBC-MAC.<br>ConstantSecurity.MAC_MODE_XOR_ECB_MAC: XOR-ECB-MAC.<br>ConstantSecurity.MAC_MODE_ANSI_X9_19: ANSI-X9.19 MAC.<br>ConstantSecurity.MAC_MODE_ANSI_X9_9: ANSI-X9.9 MAC.<br>iv - Initialization vector<br>dataIn - input data|



55 

||dataOut - Calculated data|
|---|---|
|Return value|Returns:|
||0: The operation is successfully executed;|
||Others: The operation fails. Please refer to SecurityError for more information|
|Notes||



## 3.8.12 Calculate MAC by DUKPT 

|Prototype|int calcMacDukpt(int index,<br>int keyType,<br>int mode,<br>byte[] dataIn,<br>byte[] dataOut)|
|---|---|
|Function|Calculate MAC by DUKPT|
|Parameters|index - dukpt key index<br>keyType - Key Type :<br>This Code is Coded by Two Part: X | Y<br>E.g 0x00 | 0x40.<br>X (Key Usage) can be as follows<br>0x00 : ConstantSecurity.AUTHENTICATION_GENERATION.<br>0x01 : ConstantSecurity.AUTHENTICATION_VERIFICATION.<br>0x02 : ConstantSecurity.AUTHENTICATION_BOTH.<br>Y (Derive Key Algorithm Type)<br>can be as follows<br>0x00 : ConstantSecurity.KEY_ALG_TYPE_2TDEA.<br>0x10: ConstantSecurity.KEY_ALG_TYPE_3TDEA.<br>0x20: ConstantSecurity.KEY_ALG_TYPE_AES_128.<br>0x30: ConstantSecurity.KEY_ALG_TYPE_AES_192.<br>0x40: ConstantSecurity.KEY_ALG_TYPE_AES_256.<br>mode - MAC Operation Control Code.<br>This Code is Coded by Three Part: X | Y | Z.<br>E.g 0x00 | 0x04 | 0x80.<br>X (Algorithm Type) can be as follows<br>0x00 : ConstantSecurity.MAC_MODE_CBC.<br>0x01 : ConstantSecurity.MAC_MODE_XOR_ECB_MAC.<br>0x02 : ConstantSecurity.MAC_MODE_ANSI_X9_19.<br>Y (KSN Self Increasing Mode) can be as follows<br>0x00 : ConstantSecurity.NOT_SELF_INCREASING.<br>0x40: ConstantSecurity.SELF_INCREASING.<br>Z (Dukpt Mode,default is 0x80) can be as follows<br>0x80 : ConstantSecurity.DUKPT_MODE_AES_MODE.<br>dataIn - data involved in the calculation<br>dataOut - data after calculation|
|Return value|56<br>Returns:<br>0: The operation is successfully executed;<br>Others: The operation fails. Please refer to SecurityError for more information|



Notes 

## 3.8.13 Get the KSN for the key at the specified index 

|Prototype|int getKsnDukpt(int index,<br>byte[] ksnOut)|
|---|---|
|Function|Get the KSN for the key at the specified index|
|Parameters|Parameters:<br>index - Key index<br>ksnOut - KSN value|
|Return value|Returns:<br>0: The operation is successfully executed;<br>Others: The operation fails. Please refer to SecurityError for more information|
|Notes||



## 3.8.14 Increase KSN 

|Prototype|int increaseKsnDukpt(int index)|
|---|---|
|Function|Increase KSN|
|Parameters|Parameters:<br>index - Key index|
|Return value|Returns:|
||0: The operation is successfully executed;<br>Others: The operation fails. Please refer to SecurityError for more information|
|Notes||



## 3.8.15 Calculate MAC using the DUKPT_TDES algorithm 

|Prototype|int calcMacDukptDes(int tikIndex,<br>int operationMode,<br>int ksnMode,<br>int macAlgorithm,<br>byte[] dataIn,<br>byte[] aesIv,<br>byte[] dataOut,<br>byte[] usedKsn)|
|---|---|
|Function|Calculate MAC using the DUKPT_TDES algorithm|
|Parameters|57<br>Parameters:<br>tikIndex - DUKPT key index<br>operationMode - Encryption mode:<br>•OPERATION_MODE_ECB<br>•OPERATION_MODE_CBC<br>ksnMode - KSN mode:<br>•KSN_AUTO_INCREASING_BY_DUKPT_TDES_MAC_BOTH_KEY<br>•KSN_NOT_AUTO_INCREASING_BY_DUKPT_TDES_MAC_BOTH_KEY<br>•KSN_NOT_AUTO_INCREASING_BY_DUKPT_TDES_MAC_RSP_KEY|



||macAlgorithm - MAC algorithm:<br>•MAC_ALGORITHM_CBC<br>•MAC_ALGORITHM_XOR_ECB_MAC<br>•MAC_ALGORITHM_ANSI_X9_19 (TDES only)<br>•MAC_ALGORITHM_ANSI_X9_9 (TDES only)<br>dataIn - Input data to be used in MAC calculation<br>aesIv - Initialization vector (8 bytes for CBC; null for ECB)<br>dataOut - Output MAC result (8 bytes)<br>usedKsn - KSN used in calculation|
|---|---|
|Return Value|Return:<br>0 - Success;<br>Others - Failure. See SecurityError for details|
|Notes|This method calculates a MAC value using the DUKPT TDES algorithm with the provided input,<br>mode, and algorithm settings.|



## 3.8.16 Erase all keys 

|Prototype|int eraseAllKey()|
|---|---|
|Function|Erase all keys|
|Parameters||
|Return Value|Return:<br>0 - Success;|
||Others - Failure. See SecurityError for details|
|Notes|This method deletes all stored cryptographic keys.|



## 3.8.17 Write DUKPT_DES key 

|Prototype|int writeKeyDukptDes(int tikIndex,<br>int tlkIndex,<br>byte[] keyIn,<br>byte[] ksnIn,<br>byte[] kcv)|
|---|---|
|Function|Write DUKPT_DES key|
|Parameters|Parameters:<br>tikIndex - TIK key index (valid range: 1–10)<br>tlkIndex - TLK key index (set to 0 if TLK is not used)<br>keyIn - Key data<br>ksnIn - Initial KSN<br>kcv - Key Check Value|
|Return Value|Return:<br>0 - Success;<br>Others - Failure. See SecurityError for details|
|Notes|This method writes a DUKPT DES key along with its initial KSN and KCV to the specified index.|



58 

3.8.18 Write MK/SK key 

|Prototype|int writeKeyMKSK(int srcKeyType,<br>int srcKeyIndex,<br>int keyType,<br>int encryptionAlgorithm,<br>int index,<br>byte[] keyIn,<br>byte[] kcv)|
|---|---|
|Function|Write MK/SK key|
|Parameters|Parameters:<br>srcKeyType - Source key type:<br>•ConstantSecurity.PED_TLK<br>•ConstantSecurity.PED_TMK<br>srcKeyIndex - Source key index:<br>•ConstantSecurity.TLK: only index 1 is supported<br>•ConstantSecurity.TMK: index range [1, 64]<br>keyType - Target key type:<br>•ConstantSecurity.PED_TLK<br>•ConstantSecurity.PED_TMK<br>•ConstantSecurity.PED_TPK<br>•ConstantSecurity.PED_TAK<br>•ConstantSecurity.PED_TDK<br>•ConstantSecurity.PED_TEK<br>•ConstantSecurity.PED_TTK<br>encryptionAlgorithm - Encryption algorithm:<br>•ConstantSecurity.ENCRYPTION_ALGORITHM_TDES<br>•ConstantSecurity.ENCRYPTION_ALGORITHM_AES<br>•ConstantSecurity.ENCRYPTION_ALGORITHM_SM4<br>index - Key index:<br>•TLK: [1,1]; others (TMK, TPK, etc.): [1,64]<br>Note: TPK, TAK, TDK, TEK, TTK share the same index space;<br>avoid duplication to prevent overwriting.<br>keyIn - Key data (supported lengths: 8, 16, 24, 32 bytes)<br>kcv - Key check value|
|Return Value|Return:<br>0 - Success;<br>Others - Failure. See SecurityError for details|
|Notes|This method writes Master or Session keys (MK/SK). Not applicable for DUKPT key injection.|



59 

##  3.9 ECR module 

-- Get ECR module - getEcrManager -- 

|int close(String port)|Close<br>the<br>connection<br>on<br>the<br>specified<br>port;<br>pass 'null' to close all ports|
|---|---|
|ArrayList<String> getLocal()|Enumerate and return available port numbers|
|EcrConnector openOrConnect(String connection)|Create a connection;<br>Supported types: Serial, USB-to-Serial, Cash Drawer|
|void hideHost()|Hide all displayed pages|
|void registerConnectionListener(ConnectionListener listener)|Register a connection listener|
|void unregisterConnectionListener()|Unregister the connection listener|
|String showClientByQR(int timeout,|Display the interface using QR scan,|
|ConstantScanner.ScannerCameraType cameraType)|and get the result by showHostByQR|
|String showHostByQR(ConstantEcr.ConnectType type)|Show the host info as a QR code though BT/HOST|



## 3.9.1 Create a connection to a device 

|Prototype|EcrConnector openOrConnect(String connection)|
|---|---|
|Function|Create a connection to a device|
|Parameters|Parameters:<br>connection - Connection parameters in the format:<br>Serial/USB/Cash Drawer: "PORT,BAUD"<br>Bluetooth: "UUID,BT"<br>WiFi: "URL,WIFI"|
|Return Value|Return:<br>EcrConnector - Operation object|
|Notes|Supports multiple connection types including serial, USB-serial, cash drawer, Bluetooth, and<br>WiFi.|



## 3.9.2 Close connection(s) 

|Prototype|int close(String port)|
|---|---|
|Function|Close connection(s)|
|Parameters|Parameters:<br>port - Port name to close, or 'null' to close all ports|
|Return Value|Return:|
||0 - Success;|
||Others - Failure|
|Notes|Closes the specified port or all connections.|



## 3.9.3 Enumerate and return available ports 

|Prototype|ArrayList<String> getLocal()|
|---|---|
|Function<br>Parameters|Enumerate and return available ports|



60 

|Return Value|Return:|
|---|---|
||List of local port names (e.g., "tty0" for /dev/ttySx)|
|Notes|Used to detect available local communication ports.|



## 3.9.4 Hide all displayed pages 

|Prototype|void hideHost()|
|---|---|
|Function|Hide all displayed pages|
|Parameters||
|Return Value||
|Notes|Cancels or clears all currently displayed pages from the host screen.|



## 3.9.5 Register a connection listener 

|Prototype|void registerConnectionListener(ConnectionListener listener)|
|---|---|
|Function|Register a connection listener|
|Parameters|Parameters:<br>listener - Listener for connection events|
|Return Value||
|Notes|Registers a listener to receive callbacks when a connection is established.|



## 3.9.6 Unregister the connection listener 

|Prototype|void unregisterConnectionListener()|
|---|---|
|Function|Unregister the connection listener|
|Parameters||
|Return Value||
|Notes|Used to detect available local communication ports.|



## 3.9.7 Get local BT/HOST information and display as QR code 

|Prototype|String showHostByQR(ConstantEcr.ConnectType type)|
|---|---|
|Function|Get local BT/HOST information and display as QR code|
|Parameters|Parameters:<br>type - Connection type:<br>•ConstantEcr.ConnectType.BT - Bluetooth<br>•ConstantEcr.ConnectType.HOST - Local network|
|Return Value|Return:<br>Local BT or HOST information string|
|Notes|This method returns the device’s Bluetooth or network info in a format suitable for QR code<br>display.|



## 3.9.8 Display QR scanning interface and obtain showHostByQR result 

|Prototype|String showClientByQR(int timeout,|
|---|---|
||ConstantScanner.ScannerCameraType cameraType)|



61 

|Function|Display QR scanning interface and obtain showHostByQR result|
|---|---|
|Parameters|Parameters:timeout - Timeout for reading (in seconds)<br>cameraType - Camera used for scanning:<br>•CAMERA_REAR - Rear camera<br>•CAMERA_FRONT - Front camera<br>•SCANNER - Dedicated scan engine|
|Return Value|Return:<br>Result string from showHostByQR|
|Notes|Opens a QR code scanning UI to retrieve host information broadcast by showHostByQR.|



## -- ECR connector object- EcrConnector -- 

|String getConnectInfo()|Return connection information|
|---|---|
|byte[] read()|Read data|
|void write(byte[] commands)|Write data|



## 3.9.9 Read data 

|Prototype|byte[] read()|
|---|---|
|Function|Read data|
|Parameters||
|Return Value|Return:<br>Byte array containing the read data|
|Notes|Reads data from the scanner module or connected device.|



## 3.9.10 Write data 

|Prototype|void write(byte[] commands)|
|---|---|
|Function|Write data|
|Parameters|Parameters:<br>commands - Command data to write (max 4096 bytes)|
|Return Value||
|Notes|Sends command data to the connected device or module.|



## 3.9.11 Get connection information 

|Prototype|String getConnectInfo()|
|---|---|
|Function|Get connection information|
|Parameters||
|Return Value|Return:<br>Connection information string|
|Notes|Returns current connection details of the scanner or related module.|



62 

-- ECR connection listener - ConnectionListener-- 

|void onConnected()|Will be called when a connection is established|
|---|---|
|void onState(int state, String msg)|Will be callback for connection state changes|



## 3.9.12 Connection state callback 

|Prototype|void onState(int state, String msg)|
|---|---|
|Function|Connection state callback|
|Parameters|Parameters:|
||state - Connection state value. See ConstantEcr.ConnectState for constants<br>msg - Detailed description of the state|
|Return Value||
|Notes|This method is triggered to notify the current connection state and a descriptive message.|



## 3.9.13 Called when a connection is established 

|Prototype|void onConnected()|
|---|---|
|Function|Called when a connection is established|
|Parameters||
|Return Value||
|Notes|This method is invoked when the device has successfully connected.|



##  3.10 Scanner module 

-- Get scanner module - getScannerManager -- 

|void close()|Close the scanner module|
|---|---|
|boolean isBarcodeEnabled(ConstantScanner.BarcodeFormat type)|Check whether a specific barcode format is<br>enabled|
|void open(ConstantScanner.ScannerCameraType cameraType,<br>IConnectionStatusListener callback)|Open the scanner module with a specific<br>camera type|
|void open(IConnectionStatusListener callback)|Open the scanner module|
|int registerResultCallback(IScannerResultCallback callback)|Register a callback to receive scan results|
|int setBarcodeEnable(boolean enable)|Enable/disable all supported barcode formats|
|int setBarcodeEnable(List<ConstantScanner.BarcodeFormat> types,<br>boolean enable)|Enable/disable specified barcode formats|
|int startScan()|Start scanning|
|int stopScan()|Stop scanning|
|int switchLight()|Toggle the fill light on or off|



## 3.10.1 Open the scanner module 

|Prototype|void open(IConnectionStatusListener callback)|
|---|---|
|Function|Open the scanner module|



63 

|Parameters|Parameters:<br>callback - Connection status callback|
|---|---|
|Return Value||
|Notes|Initializes the scanner module. Other scanner interfaces should only be used after a successful<br>initialization.|



## 3.10.2 Open the scanner module with a specific camera 

|Prototype|void open(ConstantScanner.ScannerCameraType cameraType,<br>IConnectionStatusListener callback)|
|---|---|
|Function|Open the scanner module with a specific camera|
|Parameters|Parameters:<br>cameraType - Camera type for scanning:<br>1. CAMERA_REAR,<br>2. CAMERA_FRONT,<br>3. SCANNER<br>callback - Connection status callback|
|Return Value||
|Notes|Initializes the scanner with the selected camera. Other interfaces should only be used after<br>successful initialization.|



## 3.10.3 Close the scanner module 

|Prototype|void close()|
|---|---|
|Function|Close the scanner module|
|Parameters||
|Return Value||
|Notes|Unbinds from the scanner service and releases related resources.|



## 3.10.4 Register a scanner result callback 

|Prototype|int registerResultCallback(IScannerResultCallback callback)|
|---|---|
|Function|Register a scanner result callback|
|Parameters|Parameters:<br>callback - Callback for scan results including type and data|
|Return Value|Return:<br>0 - Success;|
||Others - Failure. See ScannerError and CommonError for details|
|Notes|Must be called after open() is successfully executed.|



## 3.10.5 Start scanning 

|Prototype|int startScan()|
|---|---|
|Function|Start scanning|
|Parameters||
|Return Value|Return:0 - Success;|



64 

||Others - Failure. See ScannerError and CommonError for details|
|---|---|
|Notes|Triggers a scan. Must be called after open() is successfully executed.|



## 3.10.6 Stop scanning 

|Prototype|int stopScan()|
|---|---|
|Function|Stop scanning|
|Parameters||
|Return Value|Return:0 - Success;|
||Others - Failure. See ScannerError and CommonError for details|
|Notes|Stops the current scan. Must be called after open() is successfully executed.|



## 3.10.7 Enable or disable specific barcode types 

|Prototype|int setBarcodeEnable(List<ConstantScanner.BarcodeFormat> types,<br>boolean enable)|
|---|---|
|Function|Enable or disable specific barcode types|
|Parameters|Parameters:<br>types - List of barcode formats to enable/disable<br>enable - true to enable, false to disable|
|Return Value|Return:<br>0 - Success;<br>Others - Failure. See ScannerError and CommonError for details|
|Notes|Must be called after open() is successfully executed.|



## 3.10.8 Enable or disable all barcode types 

|Prototype|int setBarcodeEnable(boolean enable)|
|---|---|
|Function|Enable or disable all barcode types|
|Parameters|Parameters:<br>Enable - true to enable all types; false to disable all types|
|Return Value|Return:<br>0 - Success;|
||Others - Failure. See ScannerError and CommonError for details|
|Notes|Enable or disable all supported code types. This method must be called after a successful open().|



## 3.10.9 Check if a barcode type is enabled 

|Prototype|boolean isBarcodeEnabled(ConstantScanner.BarcodeFormat type)|
|---|---|
|Function|Check if a barcode type is enabled|
|Parameters|Parameters:<br>type - The barcode format to check|
|Return Value|Return:<br>true to enabled; false to disabled or SDK error|
|Notes|Must be called after open() is successfully executed.|



65 

## 3.10.10 Toggle the flashlight 

|Prototype|int switchLight()|
|---|---|
|Function|Toggle the flashlight|
|Parameters||
|Return Value|Return:<br>0 - Success;|
||Others - Failure. See ScannerError and CommonError for details|
|Notes|Requires hardware support; Not supported by SCANNER<br>Must be called after open() is successfully executed.|



## -- Scanner result callback - ScannerResultCallback -- 

|void onResult(String sym,|Retrieve the scan result and the barcode|
|---|---|
|String barcode)|format|



## 3.10.11 Receive scan result and symbology type 

|Prototype|void onResult(String sym,<br>String barcode)|
|---|---|
|Function|Receive scan result and symbology type|
||Parameters:|
|Parameters|sym - Barcode symbology type<br>barcode - Scanned barcode result|
|Return Value||
|Notes|This method is triggered upon a successful scan, returning the barcode data and type.|



## -- Connection Status Listener- IConnectionStatusListener-- 

|void onConnected()|Called when the connection to the scanning service is established|
|---|---|
|void onDisconnected()|Called when the connection to the scanning service is lost|
|void onError(int error,|Called when the connection to the scanning service fails|
|String msg)||



## 3.10.12 Called when the connection to the scanner service is established 

|Prototype|void onConnected()|
|---|---|
|Function|Called when the connection to the scanner service is established|
|Parameters||
|Return Value||
|Notes|Indicates that the scanner module has been successfully initialized.|



66 

## 3.10.13 Called when the connection to the scanner service fails 

|Prototype|void onError(int error,<br>String msg)|
|---|---|
|Function|Called when the connection to the scanner service fails|
||Parameters:|
|Parameters|error - Error code. See ScannerError and CommonError for definitions<br>msg - Error description|
|Return Value||
|Notes|Triggered upon failure to establish a connection with the scanner service.|



## 3.10.14 Called when the connection to the scanner service is lost 

|Prototype|void onDisconnected()|
|---|---|
|Function|Called when the connection to the scanner service is lost|
|Parameters||
|Return Value||
|Notes|Indicates that the scanner service is no longer connected.|



##  4. Error Code Definition 

## 4.1 CardReaderError 

|Error Code|Error Description|Error Value|
|---|---|---|
|CARD_APDU_OTHER_ERROR|APDU Error|-70004|
|CARD_CHECK_OTHER_ERROR|Card Check Other Error|-70003|
|CARD_CONTACT_ATR_ERROR|Contact Card ATR Data Error|-72001|
|CARD_CONTACT_NO_SUPPORT|IC Card Not Supported|-72000|
|CARD_CONTACT_OTHERS_ERROR|Contact Card Insertion Failed|-72002|
|CARD_CONTACTLESS_ATS_ERROR|Contactless Card ATS Data Error|-73001|
|CARD_CONTACTLESS_NO_SUPPORT|IC Card Not Supported|-73000|
|CARD_CONTACTLESS_OTHERS_ERROR|Contactless Card Other Error|-73002|
|CARD_EXIST_STATUS_OTHER_ERROR|Card Present Status Other Error|-70005|
|CARD_FELICA_ATS_ERROR|Contactless Card ATS Data Error|-74001|
|CARD_FELICA_NO_SUPPORT|IC Card Not Supported|-74000|
|CARD_FELICA_OTHERS_ERROR|Contactless Card Other Error|-74002|
|CARD_MAG_INVALID_ERROR|Magnetic Track or Data Verification<br>Failed|-71003|
|CARD_MAG_NEED_RETRY_ERROR|Magnetic Card Swipe Failed, Please Retry|-71002|
|CARD_MAG_NO_DATA_ERROR|No Data in Magnetic Track|-71001|
|CARD_MAG_NO_SUPPORT|Magnetic Card Not Supported|-71000|
|CARD_MAG_OTHERS_ERROR|Magnetic Card Swipe Failed|-71004|
|CARD_READER_CONTACT_ALREADY_CLOSE|IC Card AlreadyPowered Off|-72006|



67 

|CARD_READER_CONTACT_CHECK_ERROR<br>CARD_READER_CONTACT_OFF_ERROR|Contact Card Check Failed<br>Contact Card Power-Off Failed|-72004<br>-72005|
|---|---|---|
|CARD_READER_CONTACT_OPEN_ERROR|Magnetic Card Check Open Failed|-72003|
|CARD_READER_CONTACTLESS_ALREADY_CLOSE|Contactless Card Already Powered Off|-73006|
|CARD_READER_CONTACTLESS_CHECK_ERROR|Contactless Card Check Failed|-73004|
|CARD_READER_CONTACTLESS_MULTI_CARD|Multiple Contactless Cards Detected|-73007|
|CARD_READER_CONTACTLESS_OFF_ERROR|Contactless Card Power-Off Failed|-73005|
|CARD_READER_CONTACTLESS_OPEN_ERROR|Magnetic Card Check Open Failed|-73003|
|CARD_READER_FELICA_ALREADY_CLOSE|Contactless Card Already Powered Off|-74006|
|CARD_READER_FELICA_CHECK_ERROR|Contactless Card Check Failed|-74004|
|CARD_READER_FELICA_OFF_ERROR|Contactless Card Power-Off Failed|-74005|
|CARD_READER_FELICA_OPEN_ERROR|Magnetic Card Check Open Failed|-74003|
|CARD_READER_MAG_CHECK_ERROR|Magnetic Card Check Failed|-71006|
|CARD_READER_MAG_OFF_ERROR|Magnetic Card Power-Off Failed|-71007|
|CARD_READER_MAG_OPEN_ERROR|Magnetic Card Check Open Failed|-71005|
|CARD_READER_OFF_OTHER_ERROR|Card Power-Off Other Error|-70002|
|CARD_TYPE_ERROR|Card Type Error|-70001|
|NFC_TAG_ALREADY_CLOSE|NFC TAG already closed|-75006|
|NFC_TAG_NDEF_MESSAGE_ERROR|NFC TAG data error|-75004|
|NFC_TAG_NDEF_MESSAGE_LENGTH_ERROR|NDEFMessage data length exceeds limit<br>(max: 255 bytes)|-75005|
|NFC_TAG_NO_SUPPORT|NFC TAG not supported|-75000|
|NFC_TAG_OFF_ERROR|NFC TAG close failed|-75002|
|NFC_TAG_OPEN_ERROR|NFC TAG open failed|-75001|
|NFC_TAG_OTHERS_ERROR|Other NFC TAG error|-75007|
|NFC_TAG_WRITE_MESSAGE_ERROR|NFC TAG write failed|-75003|
|NOT_SUPPORT_CHECK_CONTACTLESS_AND_FELIC<br>A_SIMULTANEOUS|Simultaneous contactless and Felica card<br>check not supported|-73008|
|NOT_SUPPORT_OPEN_CONTACTLESS_AND_FELICA<br>_SIMULTANEOUS|Simultaneous contactless and Felica card<br>power-on not supported|-73009|



|4.2 CommonError|
|---|



|Error Code|Error Description|Error Value|
|---|---|---|
|FINANCIAL_PARAMETERS_INVALID|Illegal parameters|-10002|
|FINANCIAL_SERVICE_DISCONNECT|Financial services are not connected,<br>please initialize financial services|-10001|
|FINANCIAL_VERSION_NOT_MATCH|Financial<br>Services<br>SDK<br>version<br>mismatch|-10000|
|4.3 GeneralError|||



|Error Code<br>Error Description<br>Error Value|
|---|



68 

|GENERAL_ERROR_INIT|Basic operation module is not<br>initialized|-20000|
|---|---|---|
|GENERAL_NAVIGATION_BUTTON_TYPE_INVALID|Navigation bar button type is invalid;<br>only back, home, and recent buttons<br>are supported by default|-20003|
|GENERAL_OTHER_ERROR|Other exceptions, e.g., system interface<br>call errors|-20001|
|GENERAL_PARAMETERS_INVALID|Invalid parameters|-20002|
|GENERAL_SCREEN_ROTATION_ERROR|Screen rotation anomaly configuration|-20004|



## 4.4 PinpadError 

|Error Code|Error Description|Error Value|
|---|---|---|
|PINPAD_CANCEL_ERROR|Other Errors|-60002|
|PINPAD_OTHER_ERROR|Other Errors|-60000|
|PINPAD_SCREEN_ORIENTATION_CHANGED|Screen rotation|-60003|
|PINPAD_START_ERROR|Other Errors|-60001|



## 4.5 PrinterError 

|Error Code|Error Description|Error Value|
|---|---|---|
|PRINTER_ERROR_INIT|Printer not initialized or initialization<br>failed|-50000|
|PRINTER_ERROR_LOW_POWER|System power is below the low power<br>threshold|-50006|
|PRINTER_ERROR_NO_CONTENT|No print content added|-50007|
|PRINTER_ERROR_NO_PAPER|Printer out of paper|-50005|
|PRINTER_ERROR_NO_PRINTER|No printing device found|-50001|
|PRINTER_ERROR_NOT_OPENED|Printer not opened|-50002|
|PRINTER_ERROR_OTHER|Other unknown errors|-50008|
|PRINTER_ERROR_OVERHEAT|Printer overheating|-50004|
|PRINTER_ERROR_PRINT|Printing failed|-50003|
|PRINTER_ERROR_QUEUE_OVER_FLOW|Print Job Buffer Overflow|-50009|
|PRINTER_ERROR_SCREEN_OFF|Printing Disabled in Sleep Mode|-50010|



## 4.6 ScannerError 

|Error Code|Error Description|Error Value|
|---|---|---|
|SCANNER_OTHER_ERROR|Other unknown errors|-30004|
|SCANNER_SERVICE_CONNECT_FAILED|Failed to connect to the scanning<br>service|-30002|



69 

|SCANNER_SERVICE_DISCONNECT|The scanning service is not connected,|-30003|
|---|---|---|
||please call the open method to initialize<br>it first||
|SCANNER_SERVICE_UNAVAILABLE|The scanning service is unavailable|-30001|
|SCANNER_SERVICE_NOT_FIND|The scanning service was not found|-30000|



- 4.7 SecurityError 

|Error Code|Error Description|Error Value|
|---|---|---|
|SECURITY_ERROR_INIT|Security algorithm module is not<br>initialized|-40000|
|SECURITY_OTHER_ERROR|Other error, e.g., system interface call<br>error|-40001|
|SECURITY_PARAMETERS_INVALID|Illegal parameters, e.g., parameters are<br>empty or in an invalid format.|-40002|
|SECURITY_KEY_INDEX_OUT_OF_RANGE|Key index out of range|-40003|



## 4.8 EmvError 

|Error Code|Error Description|Error Value|
|---|---|---|
|EMV_AID_OTHER_ERROR|Other AID parameter error|-80100|
|EMV_APPLE_TERMINAL_OTHER_ERROR|Other Apple Terminal configuration<br>error|-80400|
|EMV_CAPK_OTHER_ERROR|Other CAPK error|-80200|
|EMV_DELETE_AID_ERROR|Error deleting AID parameter|-80109|
|EMV_DELETE_APPLE_MERCHANT_ERROR|Error deleting Apple Merchant|-80405|
|EMV_DELETE_APPLE_TERMINAL_ERROR|Error deleting Apple Terminal|-80402|
|EMV_DELETE_CAPK_ERROR|Error deleting CAPK|-80209|
|EMV_DELETE_DRL_ERROR|Error deleting DRL configuration|-80310|
|EMV_DELETE_EXCEPTION_FILE_ERROR|Error deleting ExceptionFile|-80305|
|EMV_DELETE_REVOCATION_IPK_ERROR|Error deleting RevocationIPK|-80308|
|EMV_DELETE_SERVICE_ERROR|Error deleting RuPay Service|-80314|
|EMV_DRL_OTHER_ERROR|Other DRL configuration error|-80309|
|EMV_GET_APPLE_MERCHANT_ERROR|Error retrieving Apple Merchant|-80404|
|EMV_GET_DRL_ERROR|Error retrieving DRL configuration|-80311|
|EMV_GET_KERNEL_VERSION_ERROR|Error retrieving Kernel version|-80316|
|EMV_GET_SERVICE_ERROR|Error retrieving RuPay Service|-80315|
|EMV_GET_TERMINAL_ERROR|Error retrieving terminal parameters|-80302|
|EMV_OTHER_ERROR|Other EMV error|-80001|
|EMV_SERVICE_OTHER_ERROR|Other RuPay Service error|-80312|
|EMV_SET_AID_ERROR|Error setting AID parameter|-80108|
|EMV_SET_AID_ERROR_AID_NULL|Error loading AID parameter, AID is<br>null|-80101|
|EMV_SET_AID_ERROR_DDOL_NULL|70<br>Error loading AID parameter, DDOL is<br>null|-80103|



|EMV_SET_AID_ERROR_TACDEFAULT_NULL|Error<br>loading<br>AID<br>parameter,<br>TACDefault is null|-80107|
|---|---|---|
|EMV_SET_AID_ERROR_TACDENIAL_NULL|Error<br>loading<br>AID<br>parameter,<br>TACDenial is null|-80105|
|EMV_SET_AID_ERROR_TACONLINE_NULL|Error<br>loading<br>AID<br>parameter,<br>TACOnline is null|-80106|
|EMV_SET_AID_ERROR_TDOL_NULL|Error loading AID parameter, TDOL is<br>null|-80104|
|EMV_SET_AID_ERROR_VERSION_NULL|Error loading AID parameter, Version<br>is null|-80102|
|EMV_SET_APPLE_MERCHANT_ERROR|Error setting Apple Merchant|-80403|
|EMV_SET_APPLE_TERMINAL_ERROR|Error setting Apple Terminal|-80401|
|EMV_SET_CAPK_ERROR|Error setting CAPK|-80208|
|EMV_SET_CAPK_ERROR_ALGORITHM_IND_NULL|Error<br>loading<br>CAPK<br>parameter,<br>AlgorithmInd is null|-80206|
|EMV_SET_CAPK_ERROR_CAPK_INDEX_NULL|Error<br>loading<br>CAPK<br>parameter,<br>CapkIndex is null|-80202|
|EMV_SET_CAPK_ERROR_CHECK_SUM_NULL|Error<br>loading<br>CAPK<br>parameter,<br>Checksum is null|-80205|
|EMV_SET_CAPK_ERROR_Exponent_NULL|Error<br>loading<br>CAPK<br>parameter,<br>Exponent is null|-80204|
|EMV_SET_CAPK_ERROR_HASH_IND_NULL|Error<br>loading<br>CAPK<br>parameter,<br>HashInd is null|-80207|
|EMV_SET_CAPK_ERROR_MODULE_NULL|Error<br>loading<br>CAPK<br>parameter,<br>Module is null|-80203|
|EMV_SET_CAPK_ERROR_RID_NULL|Error loading CAPK parameter, RID is<br>null|-80201|
|EMV_SET_CARD_INFO_RESPONSE_ERROR|Error setting card confirmation result|-81004|
|EMV_SET_DRL_ERROR|Error setting DRL configuration|-80309|
|EMV_SET_EXCEPTION_FILE_ERROR|Error setting ExceptionFile|-80304|
|EMV_SET_KERNEL_ERROR|Error setting KernelTag|-80317|
|EMV_SET_ONLINE_RESPONSE_ERROR|Error setting online result|-81006|
|EMV_SET_PIN_RESPONSE_ERROR|Error setting PIN result|-81005|
|EMV_SET_REVOCATION_IPK_ERROR|Error setting RevocationIPK|-80307|
|EMV_SET_SELECT_RESPONSE_ERROR|Error<br>setting<br>multi-application<br>selection result|-81003|
|EMV_SET_SELECT_RESPONSE_POSITION_ERROR|Error<br>confirming<br>position<br>in<br>multi-application selection result|-81007|
|EMV_SET_SERVICE_ERROR|Error setting RuPay Service|-80313|
|EMV_SET_TERMINAL_ERROR|Error loading terminal parameters|-80301|
|EMV_START_TRANSACTION_ERROR|Error starting transaction|-81001|
|EMV_STOP_TRANSACTION_ERROR|Error stopping transaction|-81002|
|EMV_SUCCESS|Success|0|
|71<br>EMV_TERMINAL_EXCEPTION_FILE_OTHER_ERROR|Other ExceptionFile error|-80303|



|EMV_TERMINAL_OTHER_ERROR|Other terminal parameter error|-80300|
|---|---|---|
|EMV_TERMINAL_REVOCATION_IPK_OTHER_ERROR|Other RevocationIPK error|-80306|



## 4.9 PosEmvErrorCode 

|Error Code|Error Description|Error Value|
|---|---|---|
|APPLE_VAS_APPROVED|Apple Vas Approved.|6|
|APPLE_VAS_FAILED|Apple Vas Failed.|-41|
|APPLE_VAS_UNTREATED|Apple Vas Untreated.|-40|
|APPLE_VAS_WAITING_ACTIVATION|Apple Vas Waiting Activation.|-43|
|APPLE_VAS_WAITING_INTERVENTION|Apple Vas Waiting Intervention.|-42|
|EMV_APP_BLOCKED|Application Blocked.|-7|
|EMV_APP_EMPTY|No Application.|-9|
|EMV_APPROVED|Transaction Approved.|1|
|EMV_APPROVED_ONLINE|Transaction Online Approved.|2|
|EMV_CANCEL|Trans Cancel.|-1|
|EMV_CARD_BLOCKED|Card Locked.|-8|
|EMV_CARD_ERROR|Abecs Fallback.|-40|
|EMV_COMMAND_FAIL|Read the Card Fail.|-3|
|EMV_DATA_ERROR|Card Data Error.|-81|
|EMV_DECLINED|Transaction Declined.|3|
|EMV_DELAYED_APPROVED|Transaction Delayed Approved.|5|
|EMV_ENCRYPT_ERROR|Trade Encrypt Error.|-30|
|EMV_FALLBACK|Fallback.|-4|
|EMV_FORCE_APPROVED|Transaction Force Approved.|4|
|EMV_GPO_6985|Command GPO 6985.|-50|
|EMV_MULTI_CONTACTLESS|Read Multi Contactless.|-5|
|EMV_NOT_ACCEPTED|Not Accepted.|-11|
|EMV_NOT_ALLOWED|Not Allowed.|-10|
|EMV_OK|Handle OK.|0|
|EMV_OTHER_ERROR|Other Error Exceptions.|-999|
|EMV_OTHER_ICC_INTERFACE|Not Pure Magnetic Strip.|-6|
|EMV_OTHER_INTERFACE|Try Another Interface.|-14|
|EMV_READ_RECORD_ERROR|Command Read Record Error.|-80|
|EMV_SEE_PHONE|See Phone.|-13|
|EMV_TERMINATED|Terminated.|-12|
|EMV_TIMEOUT|Trans Timeout.|-2|
|EMV_UNENCRYPTED|Trade Unencrypted.|-31|
|EXCEPTION_ERROR|Exception Error.|255|
|PARAMETER_ERROR|Parameter Error.|254|



## 4.10 EcrError 

|Error Code|Error Description|Error Value|
|---|---|---|
|ECR_CONNECT_CLOSE_FAILED|Failed to close connection|-90006|
|72|||



|ECR_CONNECT_FAILED|Connection failed|-90003|
|---|---|---|
|ECR_CONNECT_INFO_EMPTY|Connection parameter info is empty|-90001|
|ECR_CONNECT_INFO_FORMAT_EXCEPTION|Connection parameter format exception|-90002|
|ECR_ERROR_INIT|ECR module not initialized|-90000|
|ECR_FD_EMPTY|File handle is null|-90004|
|ECR_FD_FORMAT_EXCEPTION|File handle format exception|-90005|
|ECR_WRITE_DATA_EMPTY|Write data is empty|-90007|
|ECR_WRITE_DATA_FAILED|Write data exception|-90008|



##  5. Entity Class Definition 

## 5.1 com.kozen.financial.aidl.emv.EmvCapk 

|Constant Name|Type|Value|
|---|---|---|
|ALGO_IND_RSA|int|1|
|ALGO_IND_SM|int|4|
|HASH_IND_NOT|int|0|
|HASH_IND_SHA1|int|1|



## 5.2 ConstantCardReader 

|Constant Name|Type|Value|
|---|---|---|
|ATR|String|"cardAtr"|
|ATS|String|"cardAs"|
|CARD_ATTRIBUTE|String|"cardAttribute"|
|CARD_CATEGORY|String|"cardCategory"|
|CARD_CHANNEL|String|"cardChannel"|
|CARD_SERIAL_NUM|String|"cardSerialNum"|
|CARD_TYPE|String|"cardType"|
|EXPIRED_DATE|String|"cardExpDate"|
|ID_FOR_MANUFACTURER|String|"IDm"|
|PAN|String|"cardPan"|
|PARAMETER_FOR_MANUFACTURER|String|"PMm"|
|REQUEST_DATA|String|"RequestData"|
|SERVICE_CODE|String|"cardServiceCode"|
|TIMEOUT|String|"cardTimeout"|
|TRACK1|String|"cardTrack1"|
|TRACK2|String|"cardTrack2"|
|TRACK3|String|"cardTrack3"|



- 5.3 ConstantCardReader.CardType 

|Constant Name<br>Type<br>Value|
|---|



73 

|ALL|int|0|
|---|---|---|
|CONTACT|int|2|
|CONTACTLESS|int|4|
|FELICA|int|32|
|MAGNETIC|int|1|
|MIFARE|int|8|
|NFC_TAG|int|64|
|PSAM|int|16|



## 5.4 ConstantEmv.POIEmvCoreManager 

|Constant Name|Type|Value|
|---|---|---|
|CMD_AMOUNT_CONFIG|int|1|
|CMD_CARD_READ_SUCCESS|int|64|
|CMD_GAC1|int|18|
|CMD_GAC2|int|19|
|CMD_GPO_BEFORE|int|49|
|CMD_GPO_FILTER|int|16|
|CMD_ISSUER_REFERRAL|int|2|
|CMD_READ_RECORD|int|17|
|CMD_READ_RECORD_FILTER|int|17|
|CMD_SELECT_AFTER|int|48|
|CMD_SELECT_APPLICATION|int|16|
|CMD_SELECT_KERNEL|int|32|
|CMD_TRY_OTHER_APPLICATION|int|0|
|DEVICE_CONTACT|int|1|
|DEVICE_CONTACTLESS|int|2|
|DEVICE_MAGSTRIPE|int|4|
|DEVICE_MIFARE_CLASSIC|int|8|
|DEVICE_MIFARE_DESFIRE|int|64|
|DEVICE_MIFARE_PLUS|int|32|
|DEVICE_MIFARE_ULTRALIGHT|int|16|
|DEVICE_VICC|int|128|
|EMV_ADMINISTRATIVE|int|7|
|EMV_BALANCE_ENQUIRY|int|13|
|EMV_BALANCE_UPDATE|int|14|
|EMV_CARD_AMEX|int|5|
|EMV_CARD_DISCOVER|int|4|
|EMV_CARD_EFTPOS|int|11|
|EMV_CARD_INTERAC|int|10|
|EMV_CARD_JCB|int|6|
|EMV_CARD_MASTERCARD|int|3|
|EMV_CARD_MIR|int|7|
|EMV_CARD_NOT|74<br>int|0|



|EMV_CARD_PURE|int|9|
|---|---|---|
|EMV_CARD_RUPAY|int|8|
|EMV_CARD_UNIONPAY|int|2|
|EMV_CARD_VISA|int|1|
|EMV_CASH|int|3|
|EMV_CASHBACK|int|4|
|EMV_DEPOSIT|int|10|
|EMV_DISBURSEMENT|int|8|
|EMV_GOODS|int|1|
|EMV_INQUIRY|int|11|
|EMV_MONEY_ADD|int|12|
|EMV_PAYMENT|int|6|
|EMV_REFUND|int|9|
|EMV_SERVICE|int|2|
|EMV_SERVICE_CREATION|int|16|
|EMV_TRANSFER|int|5|
|EMV_VOID|int|15|
|GET_LIB_VERSION|int|0|
|GET_VERSION_AMEX|int|6|
|GET_VERSION_APPLE|int|16|
|GET_VERSION_CL1|int|14|
|GET_VERSION_DISCOVER|int|5|
|GET_VERSION_EFTPOS|int|12|
|GET_VERSION_EMV|int|1|
|GET_VERSION_INTERAC|int|11|
|GET_VERSION_JCB|int|7|
|GET_VERSION_L1|int|13|
|GET_VERSION_MASTERCARD|int|4|
|GET_VERSION_MIR|int|8|
|GET_VERSION_PURE|int|10|
|GET_VERSION_RUPAY|int|9|
|GET_VERSION_UNIONPAY|int|3|
|GET_VERSION_VISA|int|2|
|PIN_ENCIPHER_PIN|int|2|
|PIN_ONLINE_PIN|int|1|
|PIN_PLAIN_PIN|int|0|



5.5 ConstantEmv.POIEmvCoreManager.AppleTerminalConstraints 

|Constant Name|Type|Value|
|---|---|---|
|CAPABILITY|String|"capability"|
|CAPABILITY_DUAL_MODE|int|1|
|CAPABILITY_PAYMENT_ONLY|int|3|
|CAPABILITY_SINGLE_MODE|int|0|



75 

|CAPABILITY_VAS_ONLY|int|2|
|---|---|---|
|DATA|String|"data"|
|PROTOCOL|String|"protocol"|
|PROTOCOL_FULL_VAS|int|1|
|PROTOCOL_URL_ONLY|int|0|
|TAG_APPLE_SET_DELIMITER|String|"DF01"|
|TAG_APPLE_SET_FILTER|String|"9F2B"|
|TAG_APPLE_SET_MERCHANT_ID|String|"9F25"|
|TAG_APPLE_SET_MERCHANT_URL|String|"9F29"|



- 5.6 ConstantEmv.POIEmvCoreManager.EmvCardInfoConstraints 

|Constant Name|Type|Value|
|---|---|---|
|ATR|String|"atr"|
|CARD|String|"card"|
|DATA|String|"data"|
|OUT_AMOUNT|String|"amount"|
|OUT_AMOUNT_OTHER|String|"amountOther"|
|OUT_CONFIRM|String|"confirm"|
|OUT_TLV|String|"tlv"|
|OUT_TVR|String|"tvr"|
|TRACK1|String|"track1"|
|TRACK2|String|"track2"|
|TRACK3|String|"track3"|



- 5.7 ConstantEmv.POIEmvCoreManager.EmvDrlConstraints 

|Constant Name|Type|Value|
|---|---|---|
|CONFIG|String|"Config"|
|TAG_DRL_SET_CVM_REQUIRED_LIMIT|String|"DF24"|
|TAG_DRL_SET_DELIMITER|String|"DF01"|
|TAG_DRL_SET_ENTRY_POINT|String|"DF30"|
|TAG_DRL_SET_FLOOR_LIMIT|String|"DF25"|
|TAG_DRL_SET_PROGRAM_ID|String|"9F5A"|
|TAG_DRL_SET_STATUS_ZERO_AMOUNT|String|"DF32"|
|TAG_DRL_SET_TRANSACTION_LIMIT|String|"DF23"|
|TYPE_AMEX|int|2|
|TYPE_VISA|int|1|



- 5.7 ConstantEmv.POIEmvCoreManager.EmvOnlineConstraints 

|Constant Name|Type|Value|
|---|---|---|
|APPLE_DATA|String|"appleData"|
|APPLE_MERCHANT|String|"appleMerchant"|
|APPLE_RESULT|String|"appleResult"|



76 

|EMV_DATA|String|"emvData"|
|---|---|---|
|EMV_ONLINE_APPROVE|int|0|
|EMV_ONLINE_DENIAL|int|2|
|EMV_ONLINE_FAIL|int|1|
|EMV_ONLINE_REFER_TO_CARD_ISSUER|int|3|
|ENCRYPT_DATA|String|"encryptData"|
|ENCRYPT_RESULT|String|"encryptResult"|
|OUT_AUTH_CODE|String|"outAuthCode"|
|OUT_AUTH_DATA|String|"outAuthData"|
|OUT_AUTH_RESP_CODE|String|"outAuthRespCode"|
|OUT_ISSUER_SCRIPT|String|"outIssuerScript"|
|OUT_SPECIAL_AUTH_RESP_CODE|String|"outSpecialAuthRespCode"|



5.8 ConstantEmv.POIEmvCoreManager.EmvPinConstraints 

|Constant Name|Type|Value|
|---|---|---|
|OUT_PIN_BLOCK|String|"outPinBlock"|
|OUT_PIN_TRY_COUNTER|String|"outPinTryCounter"|
|OUT_PIN_VERIFY_RESULT|String|"outPinVerifyResult"|
|PIN_BLOCK_FORMAT|String|"pinBlockFormat"|
|PIN_BYPASS|String|"pinBypass"|
|PIN_CARD|String|"pinCard"|
|PIN_CARD_RANDOM|String|"pinCardRandom"|
|PIN_COUNTER|String|"pinCounter"|
|PIN_DUKPT_KEY_LENGTH|String|"pinDukptKeyLength"|
|PIN_ENCRYPT|String|"pinEncrypt"|
|PIN_EXPONENT|String|"pinExponent"|
|PIN_IS_ORDER|String|"isOrder "|
|PIN_ISO_FMT0|int|0|
|PIN_ISO_FMT1|int|1|
|PIN_ISO_FMT1_SM4|int|5|
|PIN_ISO_FMT2|int|2|
|PIN_ISO_FMT2_SM4|int|6|
|PIN_ISO_FMT3|int|3|
|PIN_ISO_FMT3_SM4|int|7|
|PIN_ISO_FMT4|int|4|
|PIN_KEY_INDEX|String|"pinKeyId"|
|PIN_KEY_MODE|String|"pinKeyMode"|
|PIN_KEY_MODE_DUKPT|int|3|
|PIN_KEY_MODE_TPK|int|1|
|PIN_LENGTH_LIMIT|String|"lengthLimit"|
|PIN_MODULE|String|"pinModule"|
|PIN_TIMEOUT|String|"pinTimeout"|
|PIN_TYPE|77<br>String|"pinType"|



|VERIFY_CANCELED|int|4|
|---|---|---|
|VERIFY_ERROR|int|3|
|VERIFY_NO_PASSWORD|int|1|
|VERIFY_PIN_BLOCK|int|2|
|VERIFY_SUCCESS|int|0|
|VERIFY_TIMEOUT|int|5|



- 5.9 ConstantEmv.POIEmvCoreManager.EmvResultConstraints 

|Constant Name|Type|Value|
|---|---|---|
|APPLE_DATA|String|"appleData"|
|APPLE_MERCHANT|String|"appleMerchant"|
|APPLE_RESULT|String|"appleResult"|
|CVM|String|"cvm"|
|CVM_CONFIRMATION_CODE_VERIFIED|int|2|
|CVM_NO_CVM|int|0|
|CVM_SIGNATURE|int|1|
|EMV_DATA|String|"emvData"|
|ENCRYPT_DATA|String|"encryptData"|
|ENCRYPT_RESULT|String|"encryptResult"|
|SCRIPT_RESULT|String|"scriptResult"|
|SECOND_TAP_CANCEL|int|3|
|SECOND_TAP_FAIL|int|1|
|SECOND_TAP_RESULT|String|"secondTapResult"|
|SECOND_TAP_SUCCESS|int|0|
|SECOND_TAP_TIMEOUT|int|2|



- 5.10 ConstantEmv.POIEmvCoreManager.EmvServiceConstraints 

|Constant Name|Type|Value|
|---|---|---|
|CONFIG|String|"Config"|
|TAG_PRMACQ_SET_DELIMITER|String|"DF02"|
|TAG_PRMACQ_SET_INDEX|String|"DF30"|
|TAG_PRMACQ_SET_KCV|String|"DF32"|
|TAG_PRMACQ_SET_KEY|String|"DF31"|
|TAG_SERVICE_SET_DATA|String|"DF19"|
|TAG_SERVICE_SET_DELIMITER|String|"DF01"|
|TAG_SERVICE_SET_ID|String|"DF16"|
|TAG_SERVICE_SET_MANAGEMENT|String|"DF18"|
|TAG_SERVICE_SET_PRIORITY|String|"DF17"|
|TAG_SERVICE_SET_PRMACQ|String|"DF21"|
|TAG_SERVICE_SET_PRMISS|String|"DF20"|



78 

## 5.11 ConstantEmv.POIEmvCoreManager.EmvTerminalConstraints 

|Constant Name|Type|Value|
|---|---|---|
|BYPASS_PIN_ENTRY|String|"BypassPINEntry"|
|CARD_HOLDER_CONFIRM|String|"CardHolderConfirm"|
|CONFIG|String|"Config"|
|DEFAULT_DDOL|String|"DefaultDDOL"|
|DEFAULT_TDOL|String|"DefaultTDOL"|
|EXCEPTION_FILE|String|"ExceptionFile"|
|FLOOR_LIMIT_CHECKING|String|"FloorLimitChecking"|
|FORCED_ACCEPT|String|"ForcedAccept"|
|FORCED_ONLINE|String|"ForcedOnline"|
|GET_DATA_FOR_PIN_COUNTER|String|"GetDataForPINCounter"|
|IFD_SERIAL_NUMBER|String|"IfdSerialNumber"|
|ISSUER_REFERRAL|String|"IssuerReferral"|
|LANGUAGE_SELECT|String|"LanguageSelect"|
|MERCHANT_CATEGORY_CODE|String|"MerchantCategoryCode"|
|MERCHANT_ID|String|"MerchantId"|
|MERCHANT_NAME|String|"MerchantName"|
|PSE|String|"Pse"|
|RANDOM_TRANSACTION_SELECTION|String|"RandomTransactionSelection"|
|REVOCATION_ISSUER_PUBLIC_KEY|String|"RevocationIssuerPublicKey"|
|SETTINGS_AMEX|int|24|
|SETTINGS_DISCOVER|int|23|
|SETTINGS_EFTPOS|int|30|
|SETTINGS_INTERAC|int|29|
|SETTINGS_JCB|int|25|
|SETTINGS_MASTERCARD|int|22|
|SETTINGS_MIR|int|26|
|SETTINGS_PURE|int|28|
|SETTINGS_RUPAY|int|27|
|SETTINGS_UNIONPAY|int|21|
|SETTINGS_VISA|int|20|
|SUBSEQUENT_BYPASS_PIN_ENTRY|String|"SubsequentBypassPINEntry"|
|TAG_AMEX_SET_ENTRY_POINT|String|"DF30"|
|TAG_AMEX_SET_KERNEL_CONFIG|String|"DF1B"|
|TAG_AMEX_SET_QUALIFIERS|String|"9F6E"|
|TAG_AMEX_SET_STATUS_ZERO_AMOUNT|String|"DF32"|
|TAG_CARD_DATA_INPUT_CAPABILITY|String|"DF8117"|
|TAG_DISCOVER_SET_ENTRY_POINT|String|"DF30"|
|TAG_DISCOVER_SET_QUALIFIERS|String|"9F66"|
|TAG_DISCOVER_SET_STATUS_ZERO_AMOUNT|String|"DF32"|
|TAG_EFTPOS_SET_ENTRY_POINT|String|"DF30"|
|TAG_EFTPOS_SET_KERNEL_CONFIG|79<br>String|"DF1B"|



|TAG_EFTPOS_SET_QUALIFIERS|String|"9F66"|
|---|---|---|
|TAG_EFTPOS_SET_STATUS|String|"DF31"|
|TAG_EFTPOS_SET_ZERO_AMOUNT|String|"DF32"|
|TAG_JCB_SET_ENTRY_POINT|String|"DF30"|
|TAG_JCB_SET_KERNEL_CONFIG|String|"DF1B"|
|TAG_JCB_SET_QUALIFIERS|String|"9F53"|
|TAG_JCB_SET_STATUS|String|"DF31"|
|TAG_JCB_SET_ZERO_AMOUNT|String|"DF32"|
|TAG_MASTERCARD_SET_CVM_CAPABILITIES|String|"DF8118"|
|TAG_MASTERCARD_SET_DEFAULT_UDOL|String|"DF811A"|
|TAG_MASTERCARD_SET_KERNEL_CONFIG|String|"DF811B"|
|TAG_MASTERCARD_SET_KERNEL_ID|String|"DF810C"|
|TAG_MASTERCARD_SET_MAGSTRIPE_APP_VE<br>RSION|String|"9F6D"|
|TAG_MASTERCARD_SET_MAGSTRIPE_CVM_C<br>APABILITIES|String|"DF811E"|
|TAG_MASTERCARD_SET_MAGSTRIPE_NO_CV<br>M_CAPABILITIES|String|"DF812C"|
|TAG_MASTERCARD_SET_MOBILE_SUPPORT_I<br>NDICATOR|String|"9F7E"|
|TAG_MASTERCARD_SET_NO_CVM_CAPABILIT<br>IES|String|"DF8119"|
|TAG_MASTERCARD_SET_RRP_ACCURACY_TH<br>RESHOLD|String|"DF8136"|
|TAG_MASTERCARD_SET_RRP_CAPDU_EXPEC<br>TED|String|"DF8134"|
|TAG_MASTERCARD_SET_RRP_MAX_GRACE|String|"DF8133"|
|TAG_MASTERCARD_SET_RRP_MIN_GRACE|String|"DF8132"|
|TAG_MASTERCARD_SET_RRP_MISMATCH_TH<br>RESHOLD|String|"DF8137"|
|TAG_MASTERCARD_SET_RRP_RAPDU_EXPEC<br>TED|String|"DF8135"|
|TAG_MIR_SET_ENTRY_POINT|String|"DF30"|
|TAG_MIR_SET_QUALIFIERS|String|"9F66"|
|TAG_MIR_SET_STATUS_ZERO_AMOUNT|String|"DF32"|
|TAG_PURE_SET_ENTRY_POINT|String|"DF30"|
|TAG_PURE_SET_KERNEL_CONFIG|String|"DF1B"|
|TAG_PURE_SET_QUALIFIERS|String|"C7"|
|TAG_PURE_SET_STATUS|String|"DF31"|
|TAG_PURE_SET_ZERO_AMOUNT|String|"DF32"|
|TAG_SECURITY_CAPABILITY|String|"DF811F"|
|TAG_UNIONPAY_SET_ENTRY_POINT|String|"DF30"|
|TAG_UNIONPAY_SET_QUALIFIERS|String|"9F66"|
|TAG_UNIONPAY_SET_STATUS_ZERO_AMOUNT|80<br>String|"DF32"|



|TAG_VISA_SET_ENTRY_POINT|String|"DF30"|
|---|---|---|
|TAG_VISA_SET_KERNEL_CONFIG|String|"DF1B"|
|TAG_VISA_SET_QUALIFIERS|String|"9F66"|
|TAG_VISA_SET_STATUS_ZERO_AMOUNT|String|"DF32"|
|TERMINAL_CAPABILITY|String|"TerminalCapability"|
|TERMINAL_COUNTRY_CODE|String|"TerminalCountryCode"|
|TERMINAL_ENTRY_MODE|String|"TerminalEntryMode"|
|TERMINAL_EX_CAPABILITY|String|"TerminalExCapability"|
|TERMINAL_ID|String|"TerminalId"|
|TERMINAL_TYPE|String|"TerminalType"|
|TRANS_CURRENCY_CODE|String|"TransCurrencyCode"|
|TRANS_CURRENCY_EXP|String|"TransCurrencyExp"|
|TRANS_REFER_CURRENCY_CODE|String|"TransReferCurrencyCode"|
|TRANS_REFER_CURRENCY_EXP|String|"TransReferCurrencyExp"|
|TYPE_AMEX|int|7|
|TYPE_CONFIG|int|2|
|TYPE_DISCOVER|int|6|
|TYPE_INTERAC|int|10|
|TYPE_MASTERCARD|int|5|
|TYPE_MIR|int|8|
|TYPE_RUPAY|int|9|
|TYPE_TERMINAL|int|1|
|TYPE_UNIONPAY|int|4|
|TYPE_VISA|int|3|
|UNABLE_TO_GO_ONLINE|String|"UnableToGoOnline"|
|VELOCITY_CHECKING|String|"VelocityChecking"|



5.12 ConstantEmv.POIEmvCoreManager.EmvTransDataConstraints 

|Constant Name|Type|Value|
|---|---|---|
|ACCOUNT_MASK_HEAD|String|"accountMaskHead"|
|ACCOUNT_MASK_TAIL|String|"accountMaskTail"|
|ACCOUNT_TYPE|String|"accountType"|
|AMOUNT_CONFIG|String|"amountConfig"|
|APPLE_VAS|String|"appleVas"|
|CL_SPECIAL_TYPE|String|"clSpecialType"|
|CT_SPECIAL_TYPE|String|"ctSpecialType"|
|ENCRYPT_BASE64|String|"encryptBase64"|
|ENCRYPT_CONTACT|String|"encryptContact"|
|ENCRYPT_CONTACTLESS|String|"encryptContactless"|
|ENCRYPT_EMV_DATA|String|"encryptEmvData"|
|ENCRYPT_KEY_INDEX|String|"encryptKeyIndex"|
|ENCRYPT_KEY_MODE|String|"encryptKeyMode"|



81 

|ENCRYPT_KEY_MODE_TRANS_ARMOR|int|1|
|---|---|---|
|ENCRYPT_MAGSTRIPE|String|"encryptMagstripe"|
|ENCRYPT_MODE|String|"encryptMode"|
|ENCRYPT_MODE_CBC|int|2|
|ENCRYPT_MODE_ECB|int|1|
|ENCRYPT_OPEN_CONTACT|int|1|
|ENCRYPT_OPEN_CONTACTLESS|int|2|
|ENCRYPT_OPEN_MAGSTRIPE|int|4|
|ENCRYPT_PADDING|String|"encryptPadding"|
|ENCRYPT_SHA1|String|"encryptSHA1"|
|ENCRYPT_TYPE|String|"encryptType"|
|ENCRYPT_TYPE_DUKPT_DATA_REQUEST|int|3|
|ENCRYPT_TYPE_DUKPT_DATA_RESPONSE|int|4|
|ENCRYPT_TYPE_DUKPT_MAC|int|2|
|ENCRYPT_TYPE_DUKPT_PIN|int|5|
|ENCRYPT_TYPE_RSA|int|6|
|ENCRYPT_TYPE_TDK|int|1|
|ENCRYPT_TYPE_TTK|int|7|
|ENCRYPT_VECTOR|String|"encryptVector"|
|GOOGLE_SMART_TAP|String|"googleSmartTap"|
|OPEN_ENCRYPT|String|"openEncrypt"|
|RSA_TRANS_ARMOR_KEY_ID|String|"rsaTransArmorKeyId"|
|RSA_TRANS_ARMOR_POS_ID|String|"rsaTransArmorPosId"|
|SPECIAL_CONTACT|String|"specialContact"|
|SPECIAL_CONTACT_TIME|String|"specialContactTime"|
|SPECIAL_MAGSTRIPE|String|"specialMagstripe"|
|SPECIAL_MAGSTRIPE_TIME|String|"specialMagstripeTime"|
|SPECIAL_START_MODE|String|"specialStartMode"|
|SPECIAL_TYPE|String|"specialType"|
|START_A|int|0|
|START_B|int|1|
|START_C|int|2|
|START_D|int|3|
|TARNS_COUNTER|String|"tarnsCounter"|
|TRANS_AMOUNT|String|"transAmount"|
|TRANS_AMOUNT_OTHER|String|"transAmountOther"|
|TRANS_DATE|String|"transDate"|
|TRANS_FALLBACK|String|"transFallback"|
|TRANS_MODE|String|"transMode"|
|TRANS_TIME|String|"transTime"|
|TRANS_TIMEOUT|String|"transTimeout"|
|TRANS_TYPE|String|"transType"|
|USE_ABECS|String|"useABECS"|
|USE_CARD_READ_SUCCESS|82<br>String|"useCardReadSuccess"|



|USE_CT_RUPAY|String|"useCTRupay"|
|---|---|---|
|USE_DELAY_PIN|String|"useDelayPIN"|
|USE_ENCRYPT_AMEX_TRACK|String|"useEncryptAmexTrack"|
|USE_FILTER|String|"useFilter"|
|USE_FORCED_AID_SELECTION|String|"useForcedAIDSelection"|
|USE_FORCED_ICC_AID_SELECTION|String|"useForcedIccAIDSelection"|
|USE_FORCED_RETURN_OF_CARD|String|"useForcedReturnOfCard"|
|USE_GAC1_FILTER|String|"useGac1Filter"|
|USE_GAC2_FILTER|String|"useGac2Filter"|
|USE_GPO_BEFORE_FILTER|String|"useGpoBeforeFilter"|
|USE_LOG|String|"log"|
|USE_MAGSTRIPE_FILTER|String|"useMagstripeFilter"|
|USE_PPSE_FAIL_SEND_AIDS_OPTION|String|"usePPSEFailSendAidsOption"|
|USE_SELECT_AFTER_FILTER|String|"useSelectAfterFilter"|
|USE_SELECT_KERNEL|String|"useSelectKernel"|
|USE_SPECIAL_AID_SELECTION|String|"useSpecialAIDSelection"|
|USE_USA_VISA|String|"useUSAVisa"|



## 5.13 ConstantPrinter 

|Constant Name|Type|Value|
|---|---|---|
|STATUS_IDLE|int|0|
|STATUS_NO_PAPER|int|3|
|STATUS_OVERHEAT|int|2|
|STATUS_PRINTING|int|1|



5.14 ConstantPrinter.Align 

|Enum Constant|Type|Description|
|---|---|---|
|CENTER|ENUM|Center alignment|
|LEFT|ENUM|Left alignment|
|RIGHT|ENUM|Right alignment|



- 5.15 ConstantPrinter.BarcodeFormat 

|Enum Constant|TYPE|Description|
|---|---|---|
|CODABAR|ENUM|CODABAR 1D format.|
|CODE_128|ENUM|Code 128 1D format.|
|CODE_39|ENUM|Code 39 1D format.|
|CODE_93|ENUM|Code 93 1D format.|
|DATA_MATRIX|ENUM|Data Matrix 2D barcode format.|
|EAN_8|ENUM|EAN-8 1D format.|
|QR_CODE|ENUM|QR Code 2D barcode format.|
|UPC_E|ENUM|UPC-E 1D format.|



83 

## 5.16 ConstantPrinter.PrintFailurePolicy 

|Enum Constant|TYPE|Description|
|---|---|---|
|ABORT_ALL|ENUM|Aborts all pending print jobs in the<br>queue|
|IGNORE_AND_CONTINUE|ENUM|Ignores current error and continues<br>next print|



## 5.17 ConstantPrinter.FontSize 

|Constant Name|Type|Value|
|---|---|---|
|LARGE|float|36.0f|
|NORMAL|float|24.0f|
|SMALL|float|16.0f|



## 5.18 ConstantSecurity 

|Constant Name|Type|Value|
|---|---|---|
|AUTHENTICATION_BOTH|int|2|
|AUTHENTICATION_GENERATION|int|0|
|AUTHENTICATION_VERIFICATION|int|1|
|DUKPT_KEY_SELECT_DATA_REQUEST|int|1|
|DUKPT_KEY_SELECT_DATA_RESPONSE|int|2|
|DUKPT_KEY_SELECT_MAC_REQUEST_OR_RESPONSE|int|0|
|DUKPT_KEY_SELECT_PIN_ENCRYPTION|int|3|
|DUKPT_MAC_MODE_CBC|int|2|
|DUKPT_MAC_MODE_ECB|int|0|
|DUKPT_MODE_AES_MODE|int|128|
|ENCRYPTION_ALGORITHM_AES|int|16|
|ENCRYPTION_ALGORITHM_SM4|int|32|
|ENCRYPTION_ALGORITHM_TDES|int|0|
|ENCRYPTION_MECHANISM_DUKPT|int|2|
|ENCRYPTION_MECHANISM_MK_SK|int|1|
|KEY_ALG_TYPE_2TDEA|int|0|
|KEY_ALG_TYPE_3TDEA|int|16|
|KEY_ALG_TYPE_AES_128|int|32|
|KEY_ALG_TYPE_AES_192|int|48|
|KEY_ALG_TYPE_AES_256|int|64|
|KSN_AUTO_INCREASING_BY_DUKPT_TDES_MAC_BOTH_KEY|int|0|
|KSN_NOT_AUTO_INCREASING_BY_DUKPT_TDES_MAC_BOTH_KEY|int|20|
|KSN_NOT_AUTO_INCREASING_BY_DUKPT_TDES_MAC_RSP_KEY|int|40|
|MAC_ALGORITHM_ANSI_X9_19|int|2|
|MAC_ALGORITHM_ANSI_X9_9|int|3|
|MAC_ALGORITHM_CBC|int|0|
|84<br>MAC_ALGORITHM_XOR_ECB_MAC|int|1|



|MAC_MODE_ANSI_X9_19|int|2|
|---|---|---|
|MAC_MODE_ANSI_X9_9|int|3|
|MAC_MODE_CBC|int|0|
|MAC_MODE_XOR_ECB_MAC|int|1|
|NOT_SELF_INCREASING|int|0|
|OPERATION_DIRECTION_DECRYPT|int|0|
|OPERATION_DIRECTION_ENCRYPT|int|1|
|OPERATION_MODE_CBC|int|2|
|OPERATION_MODE_ECB|int|0|
|PED_CALC_DES_MODE_CBC_DEC|int|2|
|PED_CALC_DES_MODE_CBC_ENC|int|3|
|PED_CALC_DES_MODE_ECB_DEC|int|0|
|PED_CALC_DES_MODE_ECB_ENC|int|1|
|PED_CALC_DUKPT_MODE_DEC|int|0|
|PED_CALC_DUKPT_MODE_ENC|int|1|
|PED_CALC_RSA_MODE_NO_PADDING|int|0|
|PED_CALC_RSA_MODE_OAEP_PADDING|int|2|
|PED_CALC_RSA_MODE_PKCS1_PADDING|int|1|
|PED_TAK|int|4|
|PED_TDK|int|5|
|PED_TEK|int|6|
|PED_TIK|int|7|
|PED_TLK|int|1|
|PED_TMK|int|2|
|PED_TPK|int|3|
|PED_TTK|int|9|
|PINBLOCK_DUKPT_FMT_ISO9564_0|int|32|
|PINBLOCK_DUKPT_FMT_ISO9564_0_KSN_INC|int|0|
|PINBLOCK_DUKPT_FMT_ISO9564_1|int|33|
|PINBLOCK_DUKPT_FMT_ISO9564_1_KSN_INC|int|1|
|PINBLOCK_DUKPT_FMT_ISO9564_2|int|34|
|PINBLOCK_DUKPT_FMT_ISO9564_2_KSN_INC|int|2|
|PINBLOCK_DUKPT_FMT_ISO9564_4|int|36|
|PINBLOCK_DUKPT_FMT_ISO9564_4_KSN_INC|int|4|
|PINBLOCK_TPK_FMT_ISO9564_0|int|0|
|PINBLOCK_TPK_FMT_ISO9564_1|int|1|
|PINBLOCK_TPK_FMT_ISO9564_3|int|2|
|PINBLOCK_TPK_FMT_ISO9564_4|int|4|
|SELF_INCREASING|int|64|



|5.19 ConstantScanner|
|---|



|Constant Name|Type|
|---|---|
|ALL_BARCODES|ConstantScanner.BarcodeFormat[]|



85 

ONE_DIMENSIONAL_BARCODES TWO_DIMENSIONAL_BARCODES 

ConstantScanner.BarcodeFormat[] ConstantScanner.BarcodeFormat[] 

5.20 ConstantScanner.BarcodeFormat 

|Enum Constant|Type|Description|
|---|---|---|
|CODABAR|1D|Used in blood banks and libraries, supports digits and some special characters|
|CODABLOCKF|1D|Stacked barcode format used in logistics and industrial applications|
|CODE11|1D|Primarily for telecom equipment, supports digits and hyphens|
|CODE128|1D|High-density linear barcode supporting ASCII, widely used in logistics and retail|
|CODE39|1D|General-purpose alphanumeric barcode for industrial applications|
|CODE93|1D|Improved version of Code 39 with higher density character support|
|EAN13|1D|International standard retail barcode (13-digit)|
|EAN8|1D|Compact version of EAN-13 for small products (8-digit)|
|GS1_128|1D|Supply chain barcode based on Code 128 standard|
|GS1_DATABAR|1D|Compact barcode for small retail items like produce|
|HK25|1D|Hong Kong variant of Interleaved 2 of 5 barcode|
|IATA25|1D|Air cargo specific barcode format|
|INDUSTRIAL25|1D|Industrial variant of 25 barcodes|
|ITF25|1D|High-density numeric barcode for carton labeling|
|MATRIX25|1D|Variant of 25 barcode format|
|MSI|1D|Inventory management barcode (digits only)|
|TELEPEN|1D|UK barcode standard supporting full ASCII|
|UPCA|1D|North American retail product barcode (12-digit)|
|UPCE|1D|Compressed version of UPC-A for small packages|
|USPS4ST|1D|US Postal Service tracking barcode|
|AZTEC|2D|Compact matrix barcode suitable for small spaces|
|DATAMATRIX|2D|2D barcode for product identification and marking|
|DOTCODE|2D|Dot-based barcode for high-speed industrial printing|
|GRIDMATRIX|2D|Chinese-developed 2D barcode for Chinese characters|
|GS1_DATAMATRIX|2D|GS1-compliant version of Data Matrix|
|HANXIN|2D|Chinese national standard 2D barcode|
|MAXICODE|2D|Fixed-size matrix barcode used in logistics|
|MICROPDF|2D|Compact PDF417 variant for ID documents|
|PDF417|2D|Stacked 2D barcode for transportation and ID cards|
|QRCODE|2D|Popular matrix barcode for general-purpose use|



## 5.21 ConstantScanner.ScannerCameraType 

|Enum Constant|TYPE|Description|
|---|---|---|
|CAMERA_FRONT|ENUM|Front Camera|
|CAMERA_REAR|ENUM|Rear Camera|
|SCANNER|ENUM|E-Series Scanner|



86 

5.22 ConstantScanner.BarcodeSupport 

|Enum Constant|TYPE|Description|
|---|---|---|
|ALL_SUPPORT|ENUM|This barcode type is supported by both cameras and scanner|
|CAMERA_SUPPORT|ENUM|This barcode type is only supported by front/rear cameras|
|SCANNER_SUPPORT|ENUM|This barcode type is only supported by scanner|



## 5.23 com.kozen.financial.pinpad.PinViewEnum 

|Enum Constant|TYPE|Description|
|---|---|---|
|BUTTON_BACKSPACE|ENUM|Backspace key|
|BUTTON_ENTER|ENUM|Enter key|
|BUTTON_ESC|ENUM|Escape key|
|BUTTON0|ENUM|Number key 0|
|BUTTON1|ENUM|Number key 1|
|BUTTON2|ENUM|Number key 2|
|BUTTON3|ENUM|Number key 3|
|BUTTON4|ENUM|Number key 4|
|BUTTON5|ENUM|Number key 5|
|BUTTON6|ENUM|Number key 6|
|BUTTON7|ENUM|Number key 7|
|BUTTON8|ENUM|Number key 8|
|BUTTON9|ENUM|Number key 9|



##  6. Access permission 

|Permission Name|Related Module|Tooltips|
|---|---|---|
|android.permission.SUPER_PERMISSIONS_P<br>RINTER|PrinterManager|Control and use built-in printer|
|android.permission.SUPER_PERMISSIONS_P<br>INPAD|PinpadManager|Access encrypted pinpad for input|
|android.permission.SUPER_PERMISSIONS_S<br>CANNER|ScannerManager|Scan barcodes and QR codes|
|android.permission.SUPER_PERMISSIONS_C<br>ARD_READER|CardReaderManager|Access magstripe, IC, and contactless cards|
|android.permission.SUPER_PERMISSIONS_E<br>MV|EmvManager|Perform EMV card transactions|
|android.permission.SUPER_PERMISSIONS_G<br>ENERAL|GeneralManager|Access general device functions (buzzer,<br>LED)|
|android.permission.SUPER_PERMISSIONS_S<br>ERURITY|SecurityManager|Use<br>security<br>module<br>(key<br>management,<br>encryption)|
|android.permission.SUPER_PERMISSIONS_E<br>CR|EcrManager|Interact with external ECR system|



87 

88 

