POS 

# Document History 

|**Version**|**Date**|**Reason**|
|---|---|---|
|v1.0.1|July, 2024|Document Creation|
|v1.0.2|September, 2025|Changes to field 120 for the POS specification & field 127.13 for<br>the NCS specification.|



# Introduction 

This specification document covers the interface specifications that must be supported by POS Terminals or other Payment Channels to integrate with Central Terminal Management System (CTMS). It contains definitions for all messages and the data elements (or fields) transmitted between the POS Terminals or other Payment Channels and CTMS. 

#### **Acronyms and Abbreviations** 

|NCS|Nigeria Central Switch|
|---|---|
|ATM|Automated Teller Machine.|
|POS|Point of Sale Machine|
|CMS|Card Management System|
|CTMS|Central Terminal Management System|



# External Message Types 

### **Protocol** 

The interface to CTMS will be over TCP/IP. The application data will be prefixed by a 2- byte length header field (Binary) indicating the length of the application data to follow. 

### **Bitmap** 

CTMS supports ASCII ISO bitmap representations. ASCII ISO bitmap representation is either 16- or 32-byte representation of the bitmap in Hex. 

### **Supported Message Type** 

Message type codes are used to identify the general function of messages, and one message type code is required in each message. CTMS supports the message types shown in the following table for both inbound and outbound messages. The message types in the table are divided according to the ISO standard message classes. 

|Message Class|Type|Description|
|---|---|---|
|Authorization|0100 &<br>0110|Authorization Request & Authorization Response|
|Financial<br>Transaction|0200 & 0210|Financial Transaction Request & Financial Transaction<br>Response|
|Reversal|0420 ,0421 &<br>0430|Reversal Advice, Reversal Advice Repeat & Reversal<br>Advice Response|
|Network<br>Management|0800 & 0810|Network Management Request & Network Management<br>Request Response|



**<mark>Note:</mark>** <mark>Due to the CBN Circular on Geo-Tagging PoS Terminals, we are introducing a new field 120 in the requests & repeats defined using Tag Length Values (TLVs) to send the geographic coordinates.</mark> 

# External Message Type Layouts 

CTMS uses the following codes to denote whether a data element should be present in its external message. 

**Conditional:** The element is mandatory under certain conditions. **Echo:** In response messages, this code indicates that the response message sender must include the same value it received in the data element in the associated request message. In other words, the responder must echo back the data element if it is present in the request message. 

**Mandatory:** The element is required in the message. 

**Optional:** The element is not always required in the message. 

### **Authorization Request/Repeat (0100)** 

|Bit Data|Element Name|Presence Indicator|
|---|---|---|
|2|Primary account number|Mandatory|
|3|Processing code|Mandatory|
|4|Amount, transaction|Mandatory|
|7|Transmission date and time|Mandatory|
|11|Systems trace audit number|Mandatory|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Mandatory|
|14|Date, expiration|Mandatory|
|18|Merchant’s type|Mandatory|
|22|POS entry mode|Mandatory|



|23|Card sequence number|Conditional|
|---|---|---|
|25|POS condition code|Mandatory|
|26|POS PIN capture code|Conditional|
|28|Amount, transaction fee|Mandatory|
|32|Acquiring institution id code|Mandatory|
|35|Track 2 data|Conditional|
|37|Retrieval reference number|Mandatory|
|40|Service restriction code|Optional|
|41|Card acceptor terminal id|Mandatory|
|42|Card acceptor id code|Mandatory|
|43|Card acceptor name/location|Mandatory|
|49|Currency code, transaction|Mandatory|
|52|PIN data|Conditional|
|53|Security related control information|Conditional|
|54|Additional amounts|Conditional|
|55|Integrated Circuit Card System Related Data|Conditional|
|56|Message reason code|Optional|
|59|Transport (echo) data|Optional|
|60|Payment Information|Conditional|
|62|Private Field, Management Data 1|Conditional|
|102|Account identification 1|Optional|
|103|Account identification 2|Optional|
|120|Additional Data – Private Use (PoS Geo<br>coordinates|Mandatory|
|123|POS data code|Mandatory|
|124|Near Field Communication Data|Conditional|
|128|Secondary Message Hash Value|Mandatory|



### **Authorization Request Response (0110)** 

|Bit Data|Element Name|Presence Indicator|
|---|---|---|
|2|Primary account number|Mandatory|
|3|Processing code|Mandatory|
|4|Amount, transaction|Mandatory|
|7|Transmission Date and Time|Conditional|
|11|Systems trace audit number|Conditional|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Conditional|
|14|Date, expiration|Conditional|
|15|Date, Settlement|Conditional|
|18|Merchant’s type|Mandatory|
|22|POS entry mode|Mandatory|
|23|Card sequence number|Conditional|
|25|POS condition code|Mandatory|
|28|Amount, transaction fee|Conditional|
|30|Amount, transaction processing fee|Conditional|
|32|Acquiring institution id code|Mandatory|
|33|Forwarding institution id code|Conditional|
|35|Track 2 data|Conditional|
|37|Retrieval reference number|Mandatory|
|38|Authorization id response|Conditional|
|39|Response code|Mandatory|
|40|Service restriction code|Conditional|
|41|Card acceptor terminal id|Optional|
|42|Card acceptor id code|Conditional|
|43|Card acceptor name/location|Conditional|
|49|Currency code, transaction|Mandatory|
|54|Additional amounts|Conditional|
|55|Integrated Circuit Card System Related Data|Conditional|
|59|Transport (echo) data|Conditional|
|60|Payment Information|Conditional|
|102|Account identification 1|Optional|
|103|Account identification 2|Optional|
|123|POS data code|Mandatory|
|124|Near Field Communication Data|Conditional|
|128|Secondary Message Hash Value|Mandatory|



### **Authorization Advice/Repeat (0120/0121)** 

|Bit Dat|a<br>Element Name|Presence Indicator|
|---|---|---|
|2|Primary account number|Conditional|
|3|Processing code|Mandatory|
|4|Amount, transaction|Mandatory|
|5|Amount, settlement|Conditional|
|7|Transmission date and time|Mandatory|
|9|Conversion rate, settlement|Conditional|
|11|Systems trace audit number|Mandatory|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Mandatory|
|14|Date, expiration|Conditional|
|15|Date, settlement|Conditional|
|16|Date, conversion|Conditional|
|18|Merchant’s type|Conditional|
|22|POS entry mode|Mandatory|
|23|Card sequence number|Conditional|
|25|POS condition code|Mandatory|
|28|Amount, transaction fee|Optional|
|29|Amount, settlement fee|Conditional|
|30|Amount, transaction processing fee|Optional|
|31|Amount, settle processing fee|Conditional|
|32|Acquiring institution id code|Conditional|
|33|Forwarding institution id code|Optional|
|35|Track 2 data|Conditional|
|37|Retrieval reference number|Optional|
|40|Service restriction code|Optional|
|41|Card acceptor terminal id|Mandatory|
|42|Card acceptor id code|Mandatory|
|43|Card acceptor name/location|Mandatory|
|45|Track 1 data|Optional|
|49|Currency code, transaction|Mandatory|
|50|Currency code, settlement|Conditional|
|54|Additional amounts|Conditional|
|55|Integrated Circuit Card System Related Data|Conditional|
|56|Message reason code|Optional|
|58|Authorizing agent id code|Conditional|
|59|Transport (echo) data|Optional|
|67|Extended payment code|Conditional|
|90|Original data elements|Conditional|
|95|Replacement amounts|Conditional|
|98|Payee|Conditional|
|100|Receiving institution id code|Conditional|
|102|Account identification 1|Conditional|
|103|Account identification 2|Conditional|
|120|Additional Data – Private Use (PoS Geo<br>coordinates|Mandatory|
|123|POS data code|Mandatory|
|124|Near Field Communication Data|Conditional|
|128|Secondary Message Hash Value|Mandatory|



### **Authorization Advice Response (0130)** 

|Bit Data|Element Name|Presence Indicator|
|---|---|---|
|2|Primary account number|Conditional|
|3|Processing code|Optional|
|4|Amount, transaction|Optional|
|5|Amount, settlement|Conditional|
|7|Transmission date and time|Mandatory|
|9|Conversion rate, settlement|Conditional|
|11|Systems trace audit number|Mandatory|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Mandatory|
|14|Date, expiration|Optional|
|15|Date, settlement|Conditional|
|16|Date, conversion|Conditional|
|18|Merchant’s type|Conditional|
|22|POS entry mode|Mandatory|
|23|Card sequence number|Optional|
|25|POS condition code|Mandatory|
|28|Amount, transaction fee|Optional|
|29|Amount, settlement fee|Conditional|
|30|Amount, transaction processing fee|Optional|
|31|Amount, settle processing fee|Conditional|
|32|Acquiring institution id code|Optional|
|33|Forwarding institution id code|Conditional|
|35|Track 2 data|Conditional|
|37|Retrieval reference number|Optional|
|38|Authorization id response|Conditional|
|39|Response code|Mandatory|
|40|Service restriction code|Conditional|
|41|Card acceptor terminal id|Mandatory|
|42|Card acceptor id code|Mandatory|
|43|Card acceptor name/location|Mandatory|
|44|Additional response data|Optional|
|48|Additional data|Conditional|
|49|Currency code, transaction|Mandatory|
|50|Currency code, settlement|Conditional|
|54|Additional amount|Mandatory|
|55|Integrated Circuit Card System Related<br>Data|Conditional|
|58|Authorizing agent id code|Conditional|
|59|Transport (echo) data|Conditional|
|90|Original data elements|Conditional|
|102|Account identification 1|Optional|
|103|Account identification 2|Optional|
|123|POS data code|Mandatory|
|124|Near Field Communication Data|Conditional|
|128|Secondary Message Hash Value|Mandatory|



### **Financial Request/Repeat (0200)** 

|Bit Data|Element Name|Presence Indicator|
|---|---|---|
|2|Primary account number|Mandatory|
|3|Processing code|Mandatory|
|4|Amount, transaction|Mandatory|
|7|Transmission date and time|Mandatory|
|11|Systems trace audit number|Mandatory|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Mandatory|
|14|Date, expiration|Mandatory|
|15|Date, settlement|Mandatory|
|16|Date, conversion|Mandatory|
|18|Merchant’s type|Mandatory|
|22|POS entry mode|Mandatory|
|23|Card sequence number|Conditional|
|25|POS condition code|Mandatory|
|26|POS PIN capture code|Conditional|
|28|Amount, transaction fee|Mandatory|
|29|Amount, settlement fee|Conditional|
|30|Amount, transaction processing fee|Optional|
|32|Acquiring institution id code|Mandatory|
|35|Track 2 data|Conditional|
|37|Retrieval reference number|Mandatory|
|40|Service restriction code|Optional|
|41|Card acceptor terminal id|Mandatory|
|42|Card acceptor id code|Mandatory|
|43|Card acceptor name/location|Mandatory|
|49|Currency code, transaction|Mandatory|
|52|PIN data|Conditional|
|53|Security related control information|Conditional|
|54|Additional amount|Conditional|
|55|Integrated Circuit Card System Related<br>Data|Conditional|
|56|Message reason code|Optional|
|59|Transport (echo) data|Optional|
|60|Payment Information|Conditional|
|62|Private Field, Management Data 1|Conditional|
|102|Account identification 1|Optional|
|103|Account identification 2|Optional|
|120|Additional Data – Private Use (PoS Geo<br>coordinates|Mandatory|
|123|POS data code|Mandatory|
|124|Near Field Communication Data|Conditional|
|128|Secondary Message Hash Value|Mandatory|



### **Financial Request Response (0210)** 

|Bit Data|Element Name|Presence Indicator|
|---|---|---|
|2|Primary account number|Mandatory|
|3|Processing code|Mandatory|
|4|Amount, transaction|Mandatory|
|7|Transmission date and time|Conditional|
|11|Systems trace audit number|Conditional|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Conditional|
|14|Date, expiration|Conditional|
|15|Date, settlement|Conditional|
|16|Date, conversion|Mandatory|
|18|Merchant’s type|Mandatory|
|22|POS entry mode|Conditional|
|23|Card sequence number|Conditional|
|25|POS condition code|Mandatory|
|26|POS PIN capture code|Conditional|
|28|Amount, transaction fee|Conditional|
|32|Acquiring institution id code|Mandatory|
|33|Forwarding institution id code|Conditional|
|35|Track 2 data|Conditional|
|37|Retrieval reference number|Mandatory|
|40|Service restriction code|Conditional|
|41|Card acceptor terminal id|Optional|
|42|Card acceptor id code|Conditional|
|43|Card acceptor name/location|Conditional|
|49|Currency code, transaction|Mandatory|
|54|Additional amount|Conditional|
|55|Integrated Circuit Card System Related<br>Data|Conditional|
|56|Message reason code|Optional|
|59|Transport (echo) data|Conditional|
|60|Payment Information|Conditional|
|62|Private Field, Management Data 1|Conditional|
|102|Account identification 1|Optional|
|103|Account identification 2|Optional|
|123|POS data code|Mandatory|
|124|Near Field Communication Data|Conditional|
|128|Secondary Message Hash Value|Mandatory|



### **Financial Advice/Repeat (Repeat) (0220/0221)** 

|Bit Data|Element Name|Presence Indicator|
|---|---|---|
|2|Primary account number|Conditional|
|3|Processing code|Mandatory|
|4|Amount, transaction|Mandatory|
|5|Amount, settlement|Conditional|
|7|Transmission date and time|Mandatory|
|9|Conversion rate, settlement|Conditional|
|11|Systems trace audit number|Mandatory|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Mandatory|
|14|Date, expiration|Conditional|
|15|Date, settlement|Conditional|
|16|Date, conversion|Conditional|
|18|Merchant’s type|Conditional|
|22|POS entry mode|Mandatory|
|23|Card sequence number|Conditional|
|25|POS condition code|Mandatory|
|26|POS PIN capture code|Conditional|
|28|Amount, transaction fee|Optional|
|29|Amount, settlement fee|Conditional|
|30|Amount, transaction processing fee|Optional|
|31|Amount, settle processing fee|Conditional|
|32|Acquiring institution id code|Conditional|
|33|Forwarding institution id code|Optional|
|35|Track 2 data|Conditional|
|37|Retrieval reference number|Optional|
|40|Service restriction code|Optional|
|41|Card acceptor terminal id|Mandatory|
|42|Card acceptor id code|Mandatory|
|43|Card acceptor name/location|Mandatory|
|45|Track 1 data|Optional|
|49|Currency code, transaction|Mandatory|
|50|Currency code, settlement|Conditional|
|54|Additional amount|Conditional|
|55|Integrated Circuit Card System Related Data|Conditional|
|56|Message reason code|Optional|
|58|Authorizing agent id code|Conditional|
|59|Transport (echo) data|Optional|
|67|Extended payment code|Conditional|
|90|Original data elements|Conditional|
|95|Replacement amounts|Conditional|
|98|Payee|Conditional|
|100|Receiving Institution ID Code|Optional|
|102|Account identification 1|Optional|
|103|Account identification 2|Optional|
|120|Additional Data – Private Use (PoS Geo<br>coordinates|Mandatory|
|123|POS data code|Mandatory|
|124|Near Field Communication Data|Conditional|
|128|Secondary Message Hash Value|Mandatory|



### **Financial Advice Response (0230)** 

Bit Data Element Name 

Presence Indicator 

|2|Primary account number|Mandatory|
|---|---|---|
|3|Processing code|Mandatory|
|4|Amount, transaction|Mandatory|
|7|Transmission date and time|Conditional|
|9|Conversion rate, settlement|Conditional|
|11|Systems trace audit number|Conditional|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Conditional|
|14|Date, expiration|Conditional|
|15|Date, settlement|Conditional|
|16|Date, conversion|Conditional|
|18|Merchant’s type|Mandatory|
|22|POS entry mode|Mandatory|
|23|Card sequence number|Conditional|
|25|POS condition code|Mandatory|
|26|POS PIN capture code|Conditional|
|28|Amount, transaction fee|Conditional|
|30|Amount, transaction processing fee|Conditional|
|31|Amount, settle processing fee|Conditional|
|32|Acquiring institution id code|Mandatory|
|33|Forwarding institution id code|Conditional|
|35|Track 2 data|Conditional|
|37|Retrieval reference number|Mandatory|
|38|Authorization id response|Conditional|
|40|Service restriction code|Conditional|
|41|Card acceptor terminal id|Optional|
|42|Card acceptor id code|Conditional|
|43|Card acceptor name/location|Conditional|
|44|Additional response data|Optional|
|48|Additional data|Conditional|
|49|Currency code, transaction|Mandatory|
|50|Currency code, settlement|Conditional|
|54|Additional amount|Conditional|
|55|Integrated Circuit Card System Related<br>Data|Conditional|
|56|Message reason code|Optional|
|59|Transport (echo) data|Conditional|
|60|Payment Information|Conditional|
|102|Account identification 1|Optional|
|103|Account identification 2|Optional|
|123|POS data code|Mandatory|
|124|Near Field Communication Data|Conditional|
|128|Secondary Message Hash Value|Mandatory|



### **Reversal Advice (Repeat) (0420/0421)** 

|Bit Data|Element Name|Presence Indicator|
|---|---|---|
|2|Primary account number| Mandatory |
|3|Processing code|Mandatory|
|4|Amount, transaction|Mandatory|
|7|Transmission date and time|Mandatory|
|11|Systems trace audit number|Mandatory|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Mandatory|
|14|Date, expiration|Mandatory|
|15|Date, settlement|Mandatory|
|16|Date, conversion|Mandatory|
|18|Merchant’s type|Mandatory|
|22|POS entry mode|Mandatory|
|23|Card sequence number|Conditional|
|25|POS condition code|Mandatory|
|26|POS PIN capture code|Conditional|
|28|Amount, transaction fee|Conditional|
|30|Amount, transaction processing fee|Conditional|
|32|Acquiring institution id code|Mandatory|
|35|Track 2 data|Conditional|
|37|Retrieval reference number|Mandatory|
|40|Service restriction code|Optional|
|41|Card acceptor terminal id|Mandatory|
|42|Card acceptor id code|Mandatory|
|43|Card acceptor name/location|Mandatory|
|49|Currency code, transaction|Mandatory|
|52|PIN data|Conditional|
|54|Additional amount|Conditional|
|56|Message reason code|Mandatory|
|59|Transport (echo) data|Optional|
|60|Payment Information|Conditional|
|90|Original data elements|Mandatory|
|95|Replacement Amounts|Mandatory|
|120|Additional Data – Private Use (PoS<br>Geo coordinates|Mandatory|
|123|POS data code|Mandatory|
|124|Near Field Communication Data|Conditional|
|128|Secondary Message Hash Value|Mandatory|



### **Reversal Advice Response (0430)** 

|Bit Data|Element Name|Presence Indicator|
|---|---|---|
|2|Primary account number|Mandatory|
|3|Processing code|Mandatory|
|4|Amount, transaction|Mandatory|
|7|Transmission date and time|Conditional|
|11|Systems trace audit number|Conditional|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Conditional|
|14|Date, expiration|Conditional|
|15|Date, settlement|Conditional|
|18|Merchant’s type|Mandatory|
|22|POS entry mode|Mandatory|
|23|Card sequence number|Optional|
|25|POS condition code|Mandatory|
|28|Amount, transaction fee|Conditional|
|30|Amount, transaction processing fee|Conditional|
|31|Amount, settle processing fee|Conditional|
|32|Acquiring institution id code|Mandatory|
|33|Forwarding institution id code|Conditional|
|35|Track 2 data|Conditional|
|37|Retrieval reference number|Mandatory|
|38|Authorization id response|Conditional|
|39|Response code|Mandatory|
|40|Service restriction code|Conditional|
|41|Card acceptor terminal id|Optional|
|42|Card acceptor id code|Conditional|
|43|Card acceptor name/location|Conditional|
|49|Currency code, transaction|Mandatory|
|54|Additional amount|Conditional|
|59|Transport (echo) data|Conditional|
|90|Original data elements|Mandatory|
|95|Replacement amount|Mandatory|
|102|Account identification 1|Optional|
|103|Account identification 2|Optional|
|123|POS data code|Mandatory|
|124|Near Field Communication Data|Conditional|
|128|Secondary Message Hash Value|Mandatory|



### **Network Management (Repeat) (0800)** 

|Bit Data|Element Name|Presence Indicator|
|---|---|---|
|3|Processing code|Mandatory|
|7|Transmission date and time|Mandatory|
|11|Systems trace audit number|Mandatory|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Mandatory|
|41|Card acceptor terminal id|Mandatory|
|62|Private, management data 1|Conditional|
|63|Private, management data 2|Conditional|
|64|Primary Message Hash Value|Conditional|



### **Network Management (0810)** 

|Bit Data|Element Name|Presence Indicator|
|---|---|---|
|7|Transmission date and time|Mandatory|
|11|Systems trace audit number|Mandatory|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Mandatory|
|39|Response code|Mandatory|
|41|Card acceptor terminal id|Mandatory|
|53|Security related control information|Conditional|
|62|Private, management data 1|Conditional|
|63|Private, management data 2|Conditional|
|64|Primary Message Hash Value|Conditional|



# Field Definition DE2 - DE9 

The following ISO Data Elements are supported by the Interface. 

#### **Field 2 – Primary Account Number** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|2|LLVAR|n ..19|



A number identifying the cardholder and the card issuer. Typically, this number is embossed on the front of the card and encoded on Track 2 of the magnetic stripe. 

#### **Field #3 - Processing Code** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|3||an 6|



The customer transaction type and the account types, if any, affected by the transaction. This is a fixed length field consisting of 3 data elements: 

- Transaction type (positions 1 - 2) 

- Account type affected for debits and inquiries and the "from" account for transfers 

- (positions 3 - 4) 

- Account type affected for credits and the "to" account for transfers (positions 5 - 6) 

Transaction Type 



|**Code**|**Description**|**Message Type**<br>**Identifier (MTI)**|
|---|---|---|
|00|Purchase|0200|
|01|Cash Advance|0200|
|20|Refund/Return|0200|
|21|Deposit|0200|
|09|Purchase with Cash back|0200|
|31|Balance Inquiry|0100|
|30|Link Account Inquiry|0100|
|38|Mini – Statement|0100|
|40|Fund Transfer|0200|
|48|Bill Payments|0200|
|50|Agency Transaction|0200|
|4A|Prepaid|0200|
|4B|Biller List Download|0800|
|4C|Product List Download|0800|
|4D|Biller Subscription Information Download|0800|
|4E|Payment Validation|0800|
|4F|Purchase with Additional Data|0200|
|60|POS Pre – Authorization|0100|
|61|POS Pre – Authorization Completion|0220|
|90|PIN Change|0100|
|9A|Terminal Master Key|0800|
|9B|Terminal Session Key|0800|
|9G|Terminal PIN Key|0800|
|9I|Initial PIN Encryption Key Download – Track2<br>Data|0800|
|9J|Initial PIN Encryption Key Download – EMV|0800|
|9C|Terminal Parameter Download|0800|
|9D|Call – home|0800|
|9E|CA Public Key Download|0800|
|9F|EMV Application AID Download|0800|
|9H|Daily Transaction Report Download|0800|
|9K|Dynamic Currency Conversion|0800|



#### Account Type Codes 

|**Code**|**Description**|
|---|---|
|00|Default – unspecified|
|10|Savings|
|20|Current|
|30|Credit|
|40|Universal Account|
|50|Investment Account|



#### **Field #4 - Amount, Transaction** 

|**Field No**<br>4|**Format**|**Attr**<br>n 12|
|---|---|---|



This field contains the transaction amount in the transaction currency. This amount is expressed in lowest denominations. 

#### **Field #5 - Amount, Settlement** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|5||n 12|



The funds to be transferred between the acquirer and card issuer equal to the amount, transaction as expressed in the settlement currency. 

#### **Field #7 - Transmission Date and Time** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|7|MMDDhhmmss|n 10|





The date and time, expressed in Coordinated Universal Time (UTC), when this message is sent by the message initiator. 

#### **Field #9 - Conversion Rate, Settlement** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|9||n 8|



This field contains the factor used in conversion from the transaction currency amount to the local currency amount. The left most digit indicates the number of decimal places from the right. 

# Field Definition DE11 - DE25 

#### **Field #11 - System Trace Audit Number** 



<!-- Start of picture text -->
Field No  Format  Attr<br>11  n 6<br><!-- End of picture text -->

A number assigned by a transaction originator to assist in identifying a transaction uniquely. The systems trace audit number remains unchanged for all messages within a transaction. 

#### **Field #12 - Time, Local Transaction** 



<!-- Start of picture text -->
Field No Format Attr<br>12 Hhmmss n 6<br><!-- End of picture text -->

The local time at which the transaction takes place at the card acceptor location in authorization and financial messages. 

For all other transactions, this field indicates the local time set by the initiator of the first message of the transaction. 

#### **Field #13 - Date, Local Transaction** 



<!-- Start of picture text -->
Field No Format Attr<br>13 MMDD n 4<br><!-- End of picture text -->

The local date at which the transaction takes place at the card acceptor location in authorization and financial messages. 

For all other transactions, this field indicates the local date set by the initiator of the first message of the transaction. 

#### **Field #14 - Date, Expiration** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**14**|**YYMM**|**n 4**|



The year and month after which the card expires. 

#### **Field #15 - Date, Settlement** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**15**|**MMDD**|**n 4**|



The month and day for which financial totals are reconciled between the acquirer and the issuer. 

#### **Field #16- Date, Conversion** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**16**|**MMDD**|**n 4**|



The month and day on which the currency for the transaction was converted. 

#### **Field #18 –Merchant Type** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**18**||**n 4**|



The classification of the merchant’s type of business product or service. Codes to be developed within each country. 

#### **Field #22 –POS Entry Mode** 

**Field No Format Attr 22 N3** 

A series of codes that identify the actual method used to capture the account number and expiry date when a terminal is used, and the PIN capture capability of the terminal. This is a fixed length field consisting of 2 data elements: 

- PAN entry mode (positions 1 - 2) 

- 00 – Unknown 

- 01 – Manual (i.e keypad) 

02 – Magnetic Stripe (possibly constructed manually, CVV may be checked) 

03 – Barcode 

04 – OCR 

05 – ICC (CVV may be checked) 

07 – Auto - entry via contactless ICC 

- 90 – Magnetic strip as read from track 2 

91 – Auto - entry via contactless magnetic stripe 

95 – ICC (CVV may not be checked) 

- PIN entry capability (position 3) 

- 0 – Unknown 

- 1 – Terminal can accept PIN 

- 2 – Terminal cannot accept PIN 

#### **Field #23 – Card Sequence number** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**23**||**n3**|



A number distinguishing between separate cards with the same primary account number or primary account number extended. 

#### **Field #25 –POS Condition Code** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**25**||**n2**|



A code that describes the condition under which the transaction takes place at the Point-OfService. 

|00|Normal presentment|
|---|---|
|01|Customer not present|
|02|Unattended terminal - card can be retained|
|03|Merchant suspicious|
|04|Electronic Cash Register interface|
|05|Customer present, card not present|
|06|Pre-authorized request|
|07|Telephone device required|
|08|Mail/telephone order|
|09|POS security alert|
|10|Customer identity verified|
|11|Suspected fraud|
|12|Security reasons|
|13|Representation of item|
|14|Public utility terminal|
|15|Customer's terminal|
|16|Administrative terminal|
|17|Returned item|
|18|No check in envelope – return|
|19|Deposit out of balance – return|
|20|Payment out of balance – return|
|21|Manual reversal|
|22|Terminal error – counted|
|23|Terminal error - not counted|
|24|Deposit out of balance – apply|
|25|Payment out of balance – apply|
|26|Withdrawal error – reversed|
|27|Unattended terminal - card cannot be retained|



Additional codes can be defined for private use. 

# Field Definition DE26 - DE38 

#### **Field #26 – POS PIN Capture Code** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**26**||**n2**|



The maximum number of PIN characters that can be accepted by the Point-of-Service device. 

Valid values are "04" to "12" ("00" to "03" are reserved by ISO) and if the POS device does not accept PINs or it is unknown whether the device does, this value should be set to "12". 

#### **Field #28 - Amount, Transaction Fee** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**28**||**x + n 8**|



A fee charged, by the acquirer to the issuer, for transaction activity, in the currency of the amount, transaction. X can have a value of either "C" or "D". 

#### **Field #29 - Amount, Settlement Fee** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**29**||**x + n 8**|



A fee charged, by the acquirer to the issuer, for transaction activity, in the currency of the amount, settlement. X can have a value of either "C" or "D". 

#### **Field #30 - Amount, Transaction Processing Fee** 

**Field No Format Attr** 

**30** 

**x + n 8** 

A fee charged by the network for the handling and routing of messages, in the currency of amount, transaction. This field is usually inserted by the network into the applicable messages. X can have a value of either "C" or "D". 

#### **Field #31 - Amount, Settlement Processing Fee** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**31**||**x + n 8**|



A fee charged by the network for the handling and routing of messages, in the currency of amount, settlement. This field is usually inserted by the network into the applicable messages. 

#### **Field #32 - Acquiring Institution Identification Code** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**32**|**LLVAR**|**an.. 11**|



A code identifying the financial institution acting as the acquirer of this customer transaction. The acquirer is the member or system user that signed the merchant, installed the ATM or dispensed cash. This field usually contains the BIN (see PAN) of the acquirer, but could be any other number assigned to it by the relevant authorities. 

When a processing center operates for multiple acquirers, this is the code for the individual member or system user, not a code for the processing center. 

#### **Field #33 - Forwarding Institution Identification Code** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**33**|**LLVAR**|**n.. 11**|



A code identifying the institution that forwards the transaction in an interchange system en route to the card issuer. For example, assume that an acquirer routes a transaction via a third-party EFT switch to the card issuer. In the request from the acquirer to the EFT switch, this field contains the code of the acquirer. When the request is forwarded by the EFT switch to the card issuer, this field contains the code assigned to the EFT switch. 

#### **Field #35 – Track 2 Data** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**35**|**LLVAR**|**z..37**|



The information encoded on Track 2 of the magnetic stripe as defined in ISO 7813, including field separators but excluding the begin sentinel, end sentinel and longitudinal redundancy check characters. The field separator (FS) can be either a "=" or a "D" character. The layout of this field is as follows: 

|**Field**|**Length**|
|---|---|
|Primary account number|up to 19 digits|
|Field separator|1 digit|
|Expiry date (YYMM)|4 digits (or a field separator if not present)|
|Service restriction code|3 digits (or a field separator if not present)|
|Discretionary data|balance of available digits|



The primary account number, expiry date and service restriction code fields are described in further detail under fields 2, 14 and 40 in this document. 

#### **Field #37 – Retrieval Reference Number** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**37**||**an 12**|



A reference number supplied by the system retaining the original source information and used to assist in locating that information or a copy thereof. 

#### **Field #38 – Authorization code** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**38**||**n 6**|



A code assigned by the authorizing institution indicating approval. 

# Field Definition DE39 - DE48 

#### **Field #39 - Response Code** 

|**Field**|**No**<br>**Format**|**Attr**|
|---|---|---|
|**39**||**an 2**|
|code  i<br>00|that defines the disposition of a transaction.<br>Approved or completed successfully||
|01|Refer to card issuer||
|02|Refer to card issuer, special condition||
|03|Invalid merchant||
|04|Pick-up card||
|05|Do not honor||
|06|Error||
|07|Pick-up card, special condition||
|08|Honor with identification||
|09|Request in progress||
|10|Approved, partial||
|11|Approved, VIP||
|12|Invalid transaction||
|13|Invalid amount||
|14|Invalid card number||
|15|No such issuer||
|16|Approved, update track 3|
|17|Customer cancellation|
|18|Customer dispute|
|19|Re-enter transaction|
|20|Invalid response|
|21|No action taken|
|22|Suspected malfunction|
|23|Unacceptable transaction fee|
|24|File update not supported|
|25|Unable to locate record|
|26|Duplicate record|
|27|File update edit error|
|28|File update file locked|
|29|File update failed|
|30|Format error|
|31|Bank not supported|
|32|Completed partially|
|33|Expired card, pick-up|
|34|Suspected fraud, pick-up|
|35|Contact acquirer, pick-up|
|36|Restricted card, pick-up|
|37|Call acquirer security, pick-up|
|38|PIN tries exceeded, pick-up|
|39|No credit account|
|40|Function not supported|
|41|Lost card|
|42|No universal account|
|43|Stolen card|
|44|No investment account|
|51|Not sufficient funds|
|52|No check account|
|53|No savings account|
|54|Expired card|
|55|Incorrect PIN|
|56|No card record|
|57|Transaction not permitted to cardholder|
|58|Transaction not permitted on terminal|
|59|Suspected fraud|
|60|Contact acquirer|
|61|Exceeds withdrawal limit|
|62|Restricted card|
|63|Security violation|
|64|Original amount incorrect|
|65|Exceeds withdrawal frequency|
|66|Call acquirer security||
|67|Hard capture||
|68|Response received too late||
|75|PIN tries exceeded||
|77|Intervene, bank approval required||
|78|Intervene, bank approval required for partial amount||
|90|Cut-off in progress||
|91|Issuer or switch inoperative||
|92|Routing error||
|93|Violation of law||
|94|Duplicate transaction||
|95|Reconcile error||
|96|System malfunction||
|98|Exceeds cash limit||
|**Field #**|**40 - Service Restriction Code**||
|**Field**|**No**<br>**Format**|**Attr**|
|**40**||**N3**|



An identification of geographic/service availability. Contains: 

- The area of usage and whether the card has additional read facilities 

- 1 International card 

|2|International card - integrated circuit facilities|
|---|---|
|5|National use only|
|6|National use only - integrated circuit facilities|
|9|Test card - online authorization mandatory|
|·|The authorization processing requirements for this card|
|0|Normal authorization|
|2|Online authorization mandatory|
|4|Online authorization mandatory|
|·|The range of services available and PIN requirements|
|0|PIN required|
|1|No restrictions - normal cardholder verification|
|2|Goods and services only|
|3|PIN required, ATM only|
|5|PIN required, goods and services only at POS, cash at ATM|
|6|PIN required if PIN pad present|
|7|PIN required if PIN pad present, goods and services only at POS, cash at ATM|
|**Fiel      i**|**d #41 - Card Acceptor Terminal Identification**|
|**Fie**|**ld No**<br>**Format**<br>**Attr**|
|**41**|<br>**ans 8**|



A unique code identifying a terminal at the card acceptor location. 

#### **Field #42 - Card Acceptor Identification Code** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**42**||**ans 15**|



A code identifying the card acceptor (typically a merchant). 

#### **Field #43 - Card Acceptor Name / Location** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**43**||**ans 40**|



The name and location of the card acceptor (such as a merchant or an ATM). This is a fixed length field consisting of 4 data elements: 

- The location information (positions 1 - 23), exclusive of city, state and country 

- The city (positions 24 - 36) in which the Point-of-Service is located 

- The state (positions 37 - 38) in which the Point-of-Service is located 

- The country (positions 39 - 40) in which the Point-of-Service is located 

#### **Field #44 - Additional Response Data** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**44**|**LLVAR**|**ans..25**|



Used to provide other supplemental data (such as a telephone number for referrals) that may be required in response to an authorization or other type of transaction request. 

After a **PIN Change** request has been processed, this field contains the PIN offset (or PVV) in a 0610 message to the Source Node, as well as in a 0620 message to the Sink Node. If set in a 0610 message from the Sink Node, this field again indicates the PIN offset (or PVV). 

For **Visa Cash** load transactions, this field is used to carry signature information. In load responses, it contains the Visa Cash Service Identifier ("SV:") followed by the load response signature (S2). This is a fixed length field consisting of 2 data elements: 

- The Visa cash service identifier (positions 1 - 3), a constant value of "SV:" 

- The load authorization signature (positions 4 - 19) for this load operation 

In load settlement advices, it contains the Visa Cash Service Identifier ("SV:") followed by the load completion signature (S3). This is a fixed length field consisting of 2 data elements: 

- The Visa cash service identifier (positions 1 - 3), a constant value of "SV:" 

- The load completion signature (positions 4 - 19) for this load operation 

#### **Field #45 - Track 1 Data** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**45**|**LLVAR**|**ans..76**|



The information encoded on Track 1 of the magnetic stripe as defined in ISO 7813, including field separators but excluding the begin sentinel, end sentinel and longitudinal redundancy check characters. 

Note that two structures are defined by ISO 7813, namely Structure A and Structure B. Structure A is reserved for proprietary use by card issuers, while Structure B is defined as follows: 

**Field** 

**Length** 



|Format Code|B (ASCII 66)|
|---|---|
|Primary account number|up to 19 digits|
|Field separator|1 character (ASCII 61 or 94)|
|Country Code|3 digits (or a field separator if not present)|
|Name|2 to 26 characters (this field is further described below)|
|Field separator|1 character (ASCII 61 or 94)|
|Expiry date (YYMM)|4 digits (or a field separator if not present)|
|Service restriction code|3 digits (or a field separator if not present)|
|Discretionary data|balance of available digits|



The primary account number, expiry date and service restriction code fields are described in further detail under fields 2, 14 and 40 in this document. 

The structure of the **Name** field is defined in the following table. Sub-fields are separated by means of a space character (ASCII 32). The minimum encoded data allowed is a single character followed by the surname separator. 

|**Field**|**Notes**|
|---|---|
|Surname||
|Surname separator|ASCII 47|
|First Name or Initial||
|Space|When required|
|Middle Name or Initial||
|Period|When followed by Title; ASCII 46|
|Title|When used|



The space character (ASCII 32) is required to separate the sub-fields of the **Name** field other than the surname. The separator terminating the surname should be encoded following the last sub-field of the **Name** field. If only the surname is encoded, it will follow the surname separator. 

#### **Field #48 - Additional Data** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**48**|**LLVAR**|**ans...999**|



Used to provide linked account or mini-statement information for a linked account inquiry or a mini-statement inquiry. 

#### **Mini – statement Information** 

The format for field 48 when mini-statement data is to be sent downstream is as follows: 

1. A mini-statement heading line, containing tags to identify the format of the ministatement data lines that follows, e.g. 

```
DATE_TIME|SEQ_NR|TRAN_TYPE|TRAN_AMOUNT~
```

The different fields of the mini-statement heading line are separated by bar characters ("|") and the line is terminated by a tilde character ("~"). 

2. One or more mini-statement data lines, each similar to the identifying string above in structure, but containing the actual transaction data to be printed per line, e.g. 

```
19971201123123|001234|01|000000005000~
```

Below is a list of tags supported. 

|**Field**|**Tag name**|**Format**|
|---|---|---|
|Sequence number|SEQ_NR|n6|
|Date and time|DATE_TIME|n14, CCYYMMDDhhmmss|
|Terminal ID|TERM_ID|n8|
|Transaction type|TRAN_TYPE|n2|
|From account|FROM_ACC|n2|
|To account|TO_ACC|n2|
|Transactionamount|TRAN_AMOUNT|n12|
|Account ID 1|ACC_ID1|ans28|
|Account ID 2|ACC_ID2|ans28|
|Authorization ID|AUTH_ID|n6|
|Currency code|CURR_CODE|n3 (Currency code of the Transaction Amount field)|
|Surcharge|SURCHARGE|n8|



#### **Linked Account Inquiry** 

In the case of a linked account inquiry, this field contains information relating to the accounts linked to the card that initiated the transaction. The information for up to 20 accounts can be returned. Note that when "00" is specified as an account type in the original request, a list of all accounts linked to the card is retrieved, and not only the linked accounts of the default account type. The format of the information associated with each account is as follows: 

|**Field**|**Length**|**Description**|
|---|---|---|
|Account<br>ID|28|The identifier uniquely identifying the account, left justified, space-<br>filled.|
|Account<br>type|2|The ISO 8583 account type of the account.|
|Currency<br>code|3|The ISO numeric currency code of the account.|
|Ledger<br>balance|13|The ledger balance of the account. The first character contains the<br>sign. A "D" indicates a debit (negative) balance and a "C" indicates a<br>credit (positive) balance.|



# Field Definition DE49 - DE58 

#### **Field #49 - Currency Code, Transaction** 

**Field No Format Attr 49 n 3** 

The local currency of the acquirer or source location of the transaction. This is the currency code used for the following amount fields: 

- amount, transaction 

- amount, transaction fee 

- amount, transaction processing fee 

#### **Field #50 - Currency Code, Settlement** 

**Field No Format Attr 50 n 3** 

A code identifying the currency of settlement. If this field is not present for a transaction, it is assumed that this field is the same as the currency code, transaction field. This is the currency code used for the following amount fields: 

- amount, settlement 

- amount, settlement fee 

- amount, settlement processing fee 

- amount, net settlement 

#### **Field #52 – PIN Data** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**52**||**Hex16**|



The PIN data field contains the PIN (a number assigned to a cardholder intended to uniquely identify that cardholder) of the cardholder formatted into a 64-bit block and encrypted with a DES key. 

#### **Field #53 – Security Related Control Information** 

**Field No Format Attr** 

**53 Hex96** 

Identifies security management information used in the current transaction or specifies security management information to be used in future transactions. 

In **PIN change** transactions, the first byte indicates the PIN to change: 

- binary 0 – insecure PIN (e.g. telephone PIN) 

- binary 1 – secure PIN (e.g. ATM PIN) 

The following 8 bytes of this field contains the new PIN formatted into a 64-bit block and encrypted with a DES key. 

In **key change** transactions, this field contains the encrypted key in the first 8-24 bytes (8 for single, 16 for double, 24 for triple length), followed by a 3-byte key check value (i.e. the first 3 bytes of a clear value of all zeroes encrypted with the key). 

#### **Field #54 – Additional Amounts** 

**Field No Format Attr** 

**an…120** 

**54** 

**LLLVAR** 

Information on up to 6 amounts and related account data for which specific data elements have not been defined. Each amount is a fixed length field consisting of 5 data elements: 

- Account type (positions 1 - 2) 

- Amount type (positions 3 - 4) 

- Currency code (positions 5 - 7) 

- Amount sign (position 8) - "C" or "D" 

- Amount (position 9 - 20) 

When this field is sent by the entity that performed currency conversion this field should contain amounts in the transaction and settlement currencies if they differ. 

In a response message from the NCS, this field will always contain the approved amounts and cash amounts, if applicable. 

#### **Field #55 – Integrated Circuit Card System Related Data** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**55**|**LLLVAR**|**ANS..510**|



This data element contains EMV data. This EMV data in this data element is structured in the following manner: 

<2-or-4-character Tag><2-character length field><variable length data> 

The 2-byte or 4-byte tag is an EMV tag (in hexadecimal format) that uniquely identifies the data content of the P-55 fields, and the 2-digit length field defines the length of the variable- length data that follows it. The length specifies the number of hexadecimal values represented in the data part, the actual string is twice as long as the length specifies. The format of the length field is binary converted to a hexadecimal string, e.g., 0x1F would be represented as “1F”. A tag identifying the type of data, followed immediately by a two-digit length (LL) identifying the length of the data, followed immediately by the data itself. 

The variable length data for a tag cannot exceed the maximum specified length specified for the data. When the variable length data is longer than the defined maximum length for the tag value, it is truncated. The following table describes each of the EMV tags that can be carried in the P-55 data element, the maximum length for each tag value, the transaction data elements (TDEs) to which each tag is mapped, and flags indicating whether the tag data is required (Y), optional (N), or not applicable (N/A) for requests and responses. 

|**EMV**<br>**Tag**|**Description**|**Max**<br>**Length**|**TDE**|**Requir**|**ed**|
|---|---|---|---|---|---|
|**Request**|**Response**|||||
|9F26|Authorization Request|H8|EMV Request|Y|N/A|
|9F27|Cryptogram Information<br>Data|H1|EMV Request|Y|N/A|
|9F10|Issuer Application<br>Discretionary data|32|EMV|Y|N/A|
|9F37|Unpredictable Number|H4|EMV Request|Y|N/A|
|9F36|Application Transaction<br>Counter|H2|EMV Request|Y|N/A|
|95|Terminal Verification Result|H5|EMV Request|Y|N/A|
|9A|Transaction Date|H3|EMV Request|Y|N/A|
|9C|Transaction Type|H1|EMV Request|Y|N/A|
|9F02|Transaction Amount|H6|EMV Request|Y|N/A|
|5F2A|Transaction Currency Code|H2|EMV Request|Y|N/A|
|82|Application Interchange<br>Profile|H2|EMV Request|Y|N/A|
|9F1A|Terminal Country Code|H2|EMV Request|Y|N/A|
|9F34|Cardholder Verification<br>Method Results|H4|EMV Discretional<br>Data|N|N/A|
|9F33|Terminal Capabilities|H3|EMV Discretional<br>Data|N|N/A|
|9F35|Terminal Type|H1|EMV Request|N|N/A|
|9F1E|Interface Device Serial<br>Number|H8|EMV Discretional<br>Data|N|N/A|
|84|Dedicated File Name|H16|EMV Discretional<br>Data|N|N/A|
||||EMV|||
|9F09|Application Version Number|H2|Discretionary<br>Data|N|N/A|
|9F03|Amount, other|H6|EMV Request|N|N/A|
|5F34|Application PAN Sequence<br>Number|H1|EMV Status|N|N|
|91|Issuer Authentication Data|H32|EMV Response|N/A|N|
|71|Issuer Script|H128|EMV Script|N/A|N|
|72|Issuer Script|H128|EMV Script|N/A|N|
|9F6E|Form Factor Indicator (FFI)|H4|EMV Request|N|N|



#### **Field #56 – Message Reason Code** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**56**|**LLLVAR**|**n..4**|



A code that provides the receiver of a request, advice or notification message with the reason, or purpose of that message. 

For original authorizations and financial transactions, it identifies why the type of message was sent (e.g. why an advice versus a request); for other messages, it states why this action was taken. 

|1003|Card issuer unavailable|
|---|---|
|1006|Under floor limit|
|1376|PIN verification failure|
|1377|Change dispensed|
|1378|IOU receipt printed|
|1510|Over floor limit|
|1800|Negative card|
|4000|Customer cancellation|
|4001|Unspecified, no action taken|
|4004|Completed partially|
|4021|Timeout waiting for response|



For place hold on card transactions, in Issuer File Update Advice (0322) or Administration (0600/0620) messages, it states why a card should be put on the hotcard list: 

|3000|Lost card|
|---|---|
|3001|Stolen card|
|3002|Undelivered card|
|3003|Counterfeit card|
|3700|Lost PIN|



If a hold response code has not been specified in these transactions, the message reason code field will be used to determine which hold response code to use for the transaction. A message reason code of "3001-Stolen card" will result in a hold response code of "43-Stolen card", otherwise "41-Lost card" will be used. 

In the case of a message to bank transaction, the message reason code specifies the type of message the cardholder wants to forward to the issuer. Note that in this case, the message reason code field is treated as a free format field that the user can use for any user specific code. 

Message reason codes are defined in the ISO 8583 (1993) specification, and this specification has been used as basis for the codes defined here. 

#### **Field #58 – Authorizing Agent Code** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**58**|**LLLVAR**|**n…11**|



A code identifying the authorizing agent institution. 

# Field Definition DE59 - DE67 

#### **Field #59 – Echo Data** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**59**|**LLLVAR**|**ans…255**|



Contains data from the originator of the message that shall be returned unaltered in the response message 

#### **Field #60 – Payment Information** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**60**|**LLLVAR**|**ans…999**|



This data element contains information relating to bills payments and token (recharge cards, tickets etc) purchases. This data element carries tagged data items. The tags defined for this data element are as listed in the table below: 

|**Transaction**<br>**Type**|**Tag**|**Description**|**Length**|<sup>**Presence**</sup><br>**Indicator**|**Request/Response**|
|---|---|---|---|---|---|
|Payment for<br>Bills|*41|Biller<br>Identification<br>Code|015|Mandatory|Request|
|||Product||||
||45|Identification||Conditional||
|||Code||||





||**50|Customer<br>Subscription<br>Information||Mandatory||
|---|---|---|---|---|---|
|||Customer||||
||**50|Subscription<br>Information||Optional|Response|
|Purchase of<br>||Biller||||
|Token<br>(Recharge Card,<br>ticket etc)|*41|Identification<br>Code|015|Mandatory|Request|
|||Product||||
||45|Identification<br>Code||Conditional||
|||Customer||||
||**50|Subscription<br>Information (or||Optional||
|||products)||||
||****51|Token (e.g. PIN<br>or Voucher)||Conditional|Response|
|||Customer||||
||**50|Subscription<br>Information||Optional||



|**Field #62 – Private Field, Management Data 1**||
|---|---|
|**Field No**<br>**Format**|**Attr**|
|**62**<br>**LLLVAR**|**ans…999**|



This data element carries tagged data items. The tags defined for this data element are listed in the table below. Tagged data items are structured in the following manner: a tag identifying the type of data, followed immediately by a three-digit length (LLL) identifying the length of the data, followed immediately by the data itself. 

|**Transaction**<br>**Type**|**Tag**|**Description**|**Length**|**Request/Response**|
|---|---|---|---|---|
|Terminal<br>Parameter<br>Download|01|POS/Payment Channel Serial<br>Number||Request|
||02|CTMS Date and Time|014|Response|
||03|Card Acceptor Identification Code|015||
||04|Timeout (maximum time interval to<br>wait for response – in seconds)|002||
||05|Currency Code|003||
||06|Country Code|003||
|||Call home time (maximum time|||
||07|interval idleness for which a call –<br>home must be done – in hours)|002||
||52|Merchant Name and Location|040||



||08|Merchant Category Code|004||
|---|---|---|---|---|
|Call – Home|01|POS/Payment Channel Serial<br>Number||Request|
||09|POS/Payment Channel Application<br>Version|003||
||10|POS Terminal/Payment Channel<br>Model|020||
||11|Call – Home Merchant<br>Information/Complaint/Comments|||
||12|Communications Service Provider|||
|Biller List|01|POS/Payment Channel Serial||Rt|
|Download||Number||eques|
||*41|Biller Identification Code|015|Response|
||42|Biller Name|||
|Biller|||||
|Subscription|01|POS/Payment Channel Serial||Request|
|Information||Number|||
|Download|||||
||*41|Biller Identification Code|015||
||43|Required Information Name||Response|
||44|Default Value|||
|Product List|01|POS/Payment Channel Serial||R|
|Download||Number||equest|
||*41|Biller Identification Code|015||
||45|Product Identification Code||Response|
||46|Product Name|||



||47|Product Amount|012||
|---|---|---|---|---|
|Payment<br>Validation|01|POS/Payment Channel Serial<br>Number||Request|
||*41|Biller Identification Code|015||
||***48|Payment Code|||
||***49|Payment Validated Information||Response|
|Base|||||
|Derivation<br>Key (BDK)|01|POS/Payment Channel Serial<br>Number||Request|
|Request|||||
||57|Key Serial Number|020||
|Dynamic<br>Currency<br>Conversion|01|POS/Payment Channel Serial<br>Number||Request|
||58|Cardholder Preferred Currency|003||
||59|Local Country Currency|003||
||60|DCC Identification Code|015||
||58|Cardholder Preferred Currency|003|Response|
||60|DCC Identification Code|015||
||61|Rate|||



Note: 

*Tag 41: Biller Identification Code is in the format below: 

1st position – transaction type the biller is setup for: 

- 4 – Token (e.g. Recharge Card) Sales 

5 – Bill Payments 

2nd – 4th position – VAS Provider Code 

5th – 8th position – Merchant Category Code 

9th – 15th position – Unique Number identifying the Biller 

**Tag 50: Customer Subscription Information, if more than one, it should be separated by ‘||’ e.g. name=Halima Eze Ajayi||customerid=00001||prod1=3||prod2=4 

From the example, name and customerid are values downloaded during Biller Subscription Information Downloadrequest and the values the customer entered are after the equals signs. prod1 and prod2 are products while values 3 and 4 after the equals signs are the quantities. 

***Tag 48: Payment Code and ***Tag 49: Payment Validated Information can be used inter – changeably. Format is same as tag 50. 

Note: For receipt printing of mCash Transactions on POS, send the Customer Information in the format of Tag 50 as above. For example: 

SellerCode=0731170253||Phone=07069302232||RefNo=10012344546567||Amount=1000.2 3 

****51: Token, if more than one, should be separated by comma ‘,’ 

#### **Field #63 – Private Field, Management Data 2** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**63**|**LLLLVAR**|**ans…9999**|



This data element carries tagged data items. The tags defined for this data element are listed in the table below. Tagged data items are structured in the following manner: a tag identifying the type of data, followed immediately by a three-digit length (LLL) identifying 

the length of the data, followed immediately by the data itself. Each block of data is separated by “~”. 

Tag + LLL + data. 

|**Transaction**<br>**Type**|**Tag**|**Description**|**Length**|**Request/Response**|
|---|---|---|---|---|
|EMV||POS/Payment|||
|Application|01|Channel Serial||Request|
|AID Download||Number|||
|13|AID Index||Response||
||Application||||
|14|Internal Reference<br>Number||||
||Application<br>i||||
|15|Identification<br>Number (EMV<br>AID)||||
|16|Match|001|||
|17|EMV Application<br>Name||||
|18|EMV Application<br>Version||||
|19|EMV Application<br>Selection Priority||||
|20|EMV DDOL||||
|21|EMV TDOL||||
|22|EMV TFL<br>Domestic||||
|23|EMV TFL<br>International|||
|24|EMV Offline<br>Threshold<br>Domestic|||
|25|EMV Max Target<br>Domestic|||
|26|EMV Max Target<br>International|||
||EMV Target|||
|27|Percentage|||
||Domestic|||
||EMV Target|||
|28|Percentage<br>International|||
|29|Default EMV TAC<br>Value|||
|30|EMV TAC Denial|||
|31|EMV TAC Online|||
|CA Public Key<br>Download|01|POS/Payment<br>Channel Serial<br>Number|Request|
||Certificate|||
|32|Authority (CA) Key<br>Index||Response|
||CA Key Internal|||
|33|Reference|||
||Number|||
|34|CA Key Name|||
|35|EMV RID|||
|36|Hash Algorithm|||
|37|EMV CA PK<br>Modulus|||
|38|EMV CA PK<br>Exponent|||
|39|EMV CA PK Hash|||
|40|Public Key<br>Algorithm|||
|Daily<br>Transaction<br>Report<br>Download|01|POS/Payment<br>Channel Serial<br>Number|Request|
|53|Transaction Date<br>and Time||Response|
|54|Response Code|||
|55|Amount|||
|56|Transaction Type|||



#### **Field #64 – Primary Message Hash Value** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**64**|**AN**|**64**|



This data element carries the hash value for the messages subject to no secondary data elements (DE65 through DE128) are included in the message. The algorithm is SHA-256. 

If the message contains secondary data elements, data element DE128 is used to carry the hash value. If the hash value is carried in data element DE128, data element DE64 is not included in the message. 

#### **Field #67 – Extended Payment Code** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**67**||**n 2**|



The number of months that the cardholder prefers to pay for this item if permitted by the card issuer. 

# Field Definition DE90 - DE128 

#### **Field #90 – Original Data Elements** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**90**||**n 42**|



The data elements contained in the original message intended for transaction matching (e.g. to identify a transaction for correction or reversal). It is a fixed length field consisting of 5 data elements: 

- Original message type (positions 1 - 4) - the message type identifier of the original 

- message of the transaction being reversed. 

- Original systems trace audit number (positions 5 - 10) – the system trace audit number 

- of the original message. 

- Original transmission date and time (positions 11 - 20) – the transmission date and 

- time of the original message 

- Original acquirer institution ID code (position 21 - 31) – the acquirer institution ID code of 

- the original message (right justified with leading zeroes). 

- Original forwarding institution ID code (position 32 - 42) – the forwarding institution ID 

- code of the original message (right justified with leading zeroes). 

#### **Field #95 – Replacement Amounts** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**95**||**n 42**|



The corrected amounts of a transaction in a partial or full reversal (or the final amounts for the transaction). It is a fixed length field consisting of 4 data elements: 

- Actual amount, transaction (positions 1 - 12) - the corrected, actual amount of the 

- customer’s transaction, in the currency of the transaction. 

- Actual amount, settlement (positions 13 - 24) - the corrected, actual amount of the 

- customer’s transaction, in the settlement currency. 

· Actual amount, transaction fee (positions 25 - 33) - the corrected, actual amount of the fee (in format x + n8) for this customer transaction, in the currency of the transaction. 

· Actual amount, settlement fee (positions 34 - 42) - the corrected, actual amount of the fee (in format x + n8) for this customer transaction, in the settlement currency. 

#### **Field #98 – Payee** 

**Field No Format Attr 98 ans25** 

A code or ID identifying the payee (recipient) of a payment transaction. This field is used in a payment transaction when the payee is an Institution defined payee. Customer defined payees do not have a payee ID. 

When using this field with the NCS Payments Engine, however, it contains the ID of the payee as maintained in the payee list for a cardholder. Because the Payments Engine assigns an ID to both institution and customer defined payees, this field can be used to refer to either type of payee. 

#### **Field #100 – Receiving Institution ID Code** 

**Field No Format Attr 100 LLVAR n…11** 

A code identifying the financial institution that should receive a request or advice. This identification code is used when it is not possible to route a message using the account number field in the message, e.g. in a check verification message. When this field is 

included in a request or advice, it takes precedence over all account number fields for routing. 

#### **Field #102 – Account Identification 1** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**102**|**LLVAR**|**n..28**|



A series of digits and/or characters used to identify a specific account held by the cardholder at the card issuer and if present, shall remain unchanged for the life of the transaction. This field usually contains the description of the "from" account. 

#### **Field #103 – Account Identification 2** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**103**|**LLVAR**|**n..28**|



A series of digits and/or characters used to identify a specific account held by the cardholder at the card issuer and if present, shall remain unchanged for the life of the transaction. This field usually contains the description of the "to" account. 

When used in payment transactions, this field specifies the bank account number of the payee. 

#### **Field #120 – Additional Data – Private Use (PoS Geo coordinates)** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**120**|**LLLCHAR**|**ans..999**|



**Sample value:** 010806.524400209003.37921 

Contains latitude & longitude of POS terminal at transaction time, used to identify the physical location of the device. 

#### **Example Breakdown** 

010806.524400209003.37921 

- **01** → Latitude tag 

- **08** → Latitude length 

- **06.52440** → Latitude = 6.52440° 

- **02** → Longitude tag 

- **09** → Longitude length 

- **003.37921** → Longitude = 3.37921° 

#### **Data Layout** 

|**Segment**|**Description**|**Format**|**Example**|
|---|---|---|---|
|01|Tag (Latitude)|2 digits|01|
|08|Latitude length|2 digits|08|
|06.52440|Latitude value (unsigned for|DD.ddddd or -|06.52440 / -|
||North,signed for South)|DD.ddddd|06.52440|
|02|Tag (Longitude)|2 digits|02|
|09|Longitude length|2 digits|09|
|003.37921|Longitude value (unsigned for|DDD.ddddd or -|003.37921 / -|
||East,signed for West)|DDD.ddddd|003.37921|



## **Formatting Rules** 

- **Latitude:** 

   - DD.ddddd → Positive values (North). 

   - -DD.ddddd → Negative values (South). 

   - Degrees: 00–90. 

- **Longitude:** 

   - DDD.ddddd → Positive values (East). 

   - -DDD.ddddd → Negative values (West). 

   - Degrees: 000–180. 

- **Decimal Places:** Minimum 5, extendable if needed. 

- **Zero Padding:** 

   - Latitude → 2 digits. 

   - Longitude → 3 digits. 

   - Decimal padded consistently. 

- **Poles/Meridian:** Use 00.00000 or 000.00000 as applicable. 

**Field #123 – POS Data Code** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**123**|**LLLVAR**|**an15**|



The field is used to identify terminal capability, terminal environment and presentation security data. It is used to indicate specific conditions that were present at the time a transaction took place at the Point-of-Service. This field consists of the following sub-fields: The card data input capability (position 1) of the terminal. Possible values are: 

- 0 – Unknown 

- 1 – Manual, no terminal 

- 2 – Magnetic Stripe 

- 3 – Barcode 

- 4 – OCR 

- 5 – Magnetic Stripe, Key Entry and ICC 

- 6 – Key Entry 

- 7 – Magnetic Stripe and Key Entry 

- 8 – Magnetic Stripe and ICC 

- 9 – ICC 

- A – Contactless ICC 

- B – Contactless Magnetic Strip 

- The cardholder authentication capability (position 2) of the terminal. 

Possible values are: 

- 0 – No electronic authentication 

1 – PIN 

- 2 – Electronic signature analysis 

- 3 – Biometric 

- 4 – Biographic 

- 5 – Electronic authentication inoperative 6 

- others 

- The card capture capability (position 3) of the terminal. 

Possible values are: 

- 0 – None 

- 1 - Capture 

- The operating environment (position 4) of the terminal. 

Possible values are: 

- 0 – No terminal use 

- 1 – On premise of card acceptor, attended 

- 2 – On premise of card acceptor, unattended 

- 3 – Off premise of card acceptor, attended 

- 4 – Off premise of card acceptor, unattended 5 

- On premise of cardholder, unattended 

- Indicates whether the cardholder is present (position 5). 

Possible values are: 

- 0 – Cardholder present 

- 1 – Cardholder not present, unspecified 

- 2 – Cardholder not present, mail order 

- 3 – Cardholder not present, telephone 

- 4 – Cardholder not present, standing authorization/recurring transaction 5 

- Cardholder not present, electronic order 

- Indicates whether the card is present (position 6) 

Possible values are: 

- 0 – Card not present 

- 1 – Card present 

- The actual card data input mode (position 7) of the transaction 

Possible values are: 

- 0 – Unknown 

- 1 – Manual, no terminal 

- 2 – Magnetic Stripe 

- 3 – Barcode 

- 4 – OCR 

- 5 – ICC 

- 6 – Key Entry 

- 7 – Contactless, ICC 

- 8 – Contactless, magnetic stripe 

- The actual cardholder authentication method (position 8) of the transaction. 

Possible values are: 

- 0 – no electronic authentication 1 

- PIN 

- 2 – Electronic signature analysis 3 

- Biometric 

- 4 – Biographic 

- 5 – Manual 

- 6 – Others 

- The cardholder authentication entity (position 9) of the transaction 

Possible values are: 

- 0 – not authenticated 

- 1 – ICC 

- 2 – Terminal 

- 3 – Authorizing agent 

- 4 – Merchant 

- 5 – Others 

- The card data output capability (position 10) of the terminal. 

Possible values are: 

- 0 – Unknown 

- 1 – None 

- 2 – Magnetic Stripe write 

- 3 – ICC 

- The terminal output capability (position 11) of the terminal. 

Possible values are: 

- 0 – Unknown 1 – None 

- 2 – Printing 3 – Display 

- 4 – Printing and display 

- The PIN capture capability (position 12) of the terminal. 

- 0 – No PIN capture capability 

- 1 – Device PIN capture capability unknown 4 – Four characters 

- 5 – Five characters 

- 6 – Six characters 

- 7 – Seven characters 

- 8 – Eight characters 

- 9 – Nine characters 

- A – Ten characters 

- B – Eleven characters 

- C – Twelve characters 

- The terminal operator (position 13). 

- Possible values are: 

- 0 – Customer operated 

- 1 – Card acceptor operated 

- Terminal Type (position 14 and 15) 

00 – Administrative Terminal 

- 01 – POS 

02 – ATM 

03 – Home Terminal 

90 – E – commerce – no encryption (no authentication) 

- 91 – E – commerce – FET/3D Secure Encryption: cardholder certificate not used (nonauthentication) 

- 92 – E – commerce – FET/3D Secure Encryption: cardholder certificate used (authentication) 

- 93 – E – commerce – FET/3D Secure Encryption: Chip Cryptography used: cardholder certificate not used 

94 – E – commerce – FET/3D Secure Encryption: cardholder certificate used 

95 – E – commerce – FET/3D Secure Encryption: channel encryption (SSL): cardholder certificate not used (non – authentication) 

96 – E – commerce – FET/3D Secure Encryption: Chip cryptography used: cardholder certificate not used 

21 – Smart Phone 

16 – Vending 

15 – Public Utility 

13 – Franchise Teller 

09 – Coupon Machine 

07 – Fuel Machine 

#### **Field #124 – Secondary Message Hash Value** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**124**|**LLLLVAR**|**ans…9999**|



This data element carries NFC Data. This would be transmitted to the processing system as received from POS Terminal. 

#### **Field #128 – Secondary Message Hash Value** 

**Field No Format Attr 128 AN 64** 

This data element carries the message hash value for the message, subject to at least one other secondary data element (DE65 through DE128) being included in the message. The algorithm is SHA-256. If the message does not contain at least one other secondary data element, the hash value is placed in data element DE64. 

# Key Management 

This section discusses how keys are managed between POS Terminals or other Payment Channels. The system has support for Master Key Session and Derived Unique Key Per Transaction (DUKPT). 

For Master Key session, the following are the keys to be exchanged: 

**1. Clear Terminal Master Key (CTMK):** This key is transmitted in clear form and injected into the POS Terminal or other Payment Channels. This can be generated for a group of POS Terminals. A group could be a logical grouping by Payment Terminal Service Provider (PTSP) or Merchant Acquirers. 

**2. Encrypted Terminal Master Key (TMK):** This key is transmitted in encrypted form to the POS Terminal or other Payment Channels. This is not injected into terminal manually. This is requested for online by the POS Terminal when it is being setup or if it is suspected that the key has been compromised. TMK is encrypted with CTMK. 

**3. Encrypted Terminal PIN Key (TPK):** This key is usually transmitted in encrypted form to the POS Terminal or other Payment Channels. This is not injected into the terminal manually. POS terminal requests online for this key at predefined time from the CTMS. TPK is encrypted with the TMK. At transaction, this key is used to encrypt the PIN block if it is sent in the message. 

**4. Encrypted Terminal Session Key (TSK):** This key is usually transmitted in encrypted form to the POS Terminal or other Payment Channels. This is not injected into terminal manually. POS Terminal requests online for this key at predefined time from the CTMS. TSK is encrypted with TMK. At transaction, this key is used for seeding the algorithm for generating hash value in DE64 or DE128. 

For DUKPT, the following are the keys to be exchanged: 

**1. Clear Terminal Master Key (CTMK):** This key is transmitted in clear form and injected into the POS Terminal or other Payment Channels. This can be generated for a group of POS Terminals. A group could be a logical grouping by Payment Terminal Service Provider (PTSP) or Merchant Acquirers. 

**2. Encrypted Terminal Master Key (TMK):** This key is transmitted in encrypted form to the POS Terminal or other Payment Channels. This is not injected into terminal manually. This is requested for online by the POS Terminal when it is being setup or if it is suspected that the key has been compromised. TMK is encrypted with CTMK. 

**3. Base Derivation Key (BDK):** This key is NOT transmitted to the POS Terminal. In clear form it is injected into the POS Terminal or other Payment Channels in two components manually. This can be generated for a group of POS Terminals. 

**4. Encrypted Terminal Session Key (TSK):** This key is usually transmitted in encrypted form to the POS Terminal or other Payment Channels. This is not injected into terminal manually. POS Terminal requests online for this key at predefined time from the CTMS. TSK is encrypted with TMK. At transaction, this key is used for seeding the algorithm for generating hash value in DE64 or DE128. 

**5. Encrypted Initial PIN Encryption Key (IPEK) for Track 2 Data:** This key is usually transmitted in encrypted form to the POS Terminal or other Payment Channels. This is not injected into terminal manually. POS Terminal requests online for this key at predefined time from the CTMS. IPEK is encrypted with TMK. 

**6. Encrypted Initial PIN Encryption Key (IPEK) for EMV:** This key is usually transmitted in encrypted form to the POS Terminal or other Payment Channels. This is not injected into terminal manually. POS Terminal requests online for this key at predefined time from the CTMS. IPEK is encrypted with TMK. 

Nigeria Central Switch Interface Specifications 

# Introduction 

This specification document covers the interface specifications that must be supported by any third party (Issuer or Acquirer) to integrate with NCS. It contains definitions for all messages and the data elements (or fields) transmitted from the NCS and a third-party  application 

#### **Acronyms and Abbreviations** 

|NCS|Nigeria Central Switch|
|---|---|
|ATM|Automated Teller Machine.|
|POS|Point of Sale Machine|
|Acquirer|The institution, which accepts the card for performing the transaction.|
|Issuer|The institution which has issued the card and to which the account belongs.|
|CMS|Card Management System (Credit Card)|



# External Message types 

#### **2.1 Protocol** 

The interface to NCS will be over TCP/IP. The application data will be prefixed by a 2-byte length header field (Binary) indicating the length of the application data to follow. 

#### **2.2 Bitmap** 

NCS supports ASCII ISO bitmap representations. ASCII ISO bitmap representation is either 16- or 32-byte representation of the bitmap in Hex. 

#### **2. 3 Supported Message Type** 

Message type codes are used to identify the general function of messages, and one 

Message type code is required in each message. NCS supports the message types shown in the following table for both inbound and outbound messages. The message types in the table are divided according to the ISO standard message classes. 

|**Message Class**|**Type**|**Description**|
|---|---|---|
|Authorization|0100|Authorization Request|
||0110|Authorization Response|
||0120|Authorization Advice|
||0130|Authorization Advice Response|
|Financial Transaction|0200|Financial Transaction Request|
||0202|Financial Transaction Completion|
||0210|Financial Transaction Response|
||0212|Financial Transaction Completion Response|
||0220|Financial Transaction Advice|
||0230|Financial Transaction Advice Response|
|Reversal|0420|Reversal Advice|
||0421|Reversal Advice Repeat|
||0430|Reversal Advice Response|
|Network Management|0800|Network Management Request|
||0810|Network Management Request Response|



# External Message Type Layouts 

NCS System uses the following codes to denote whether a data element should be present in its external message. 

**Conditional** : The element is mandatory under certain conditions. 

**Echo** : In response messages, this code indicates that the response message sender must include the same value it received in the data element in the associated request message. In other words, the responder must echo back the data element if it is present in the request message. 

**Mandatory** : The element is required in the message. 

### **3.2 Authorization Request/Repeat (0100/0101)** 

|**Bit Data**|**Element Name**|**Presence Indicator**|
|---|---|---|
|2|Primary account number|Conditional|
|3|Processing code|Mandatory|
|4|Amount, transaction|Mandatory|
|5|Amount, settlement|Conditional|
|7|Transmission date and time|Mandatory|
|9|Conversion rate, settlement|Conditional|
|11|Systems trace audit number|Mandatory|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Mandatory|
|14|Date, expiration|Conditional|
|15|Date, settlement|Conditional|
|16|Date, conversion|Conditional|
|18|Merchant’s type|Conditional|
|22|POS entry mode|Mandatory|
|23|Card sequence number|Conditional|
|25|POS condition code|Mandatory|
|26|POS PIN capture code|Conditional|
|27|Authorization id response length|Conditional|
|28|Amount, transaction fee|Optional|
|29|Amount, settlement fee|Conditional|
|30|Amount, transaction processing fee|Optional|
|31|Amount, settle processing fee|Conditional|
|32|Acquiring institution id code|Conditional|
|33|Forwarding institution id code|Optional|
|35|Track 2 data|Conditional|
|37|Retrieval reference number|Optional|
|40|Service restriction code|Optional|
|41|Card acceptor terminal id|Mandatory|
|42|Card acceptor id code|Mandatory|
|43|Card acceptor name/location|Mandatory|
|45|Track 1 data|Optional|
|49|Currency code, transaction|Mandatory|
|50|Currency code, settlement|Conditional|
|52|PIN data|Conditional|
|53|Security related control information|Conditional|
|54|Additional amounts|Conditional|
|56|Message reason code|Optional|
|57|Authorization life cycle|Optional|
|59|Transport (echo) data|Optional|
|67|Extended payment code|Conditional|
|90|Original data elements|Conditional|
|98|Payee|Conditional|
|100|Receiving institution id code|Conditional|
|102|Account identification 1|Conditional|
|103|Account identification 2|Conditional|
|123|POS data code|Mandatory|
|124|Near Field Communication Data|Conditional|
|127.1|Bitmap|Conditional|
|127.4|POS data|Optional|
|127.5|Service station data|Optional|
|127.7|Check data|Conditional|
|127.10|CVV2|Optional|
|127.12|Terminal owner|Conditional|
|127.13|POS Geographic data|Mandatory|
|127.14|Sponsor bank|Conditional|
|127.15|Address verification data|Conditional|
|127.18|Validation data|Optional|
|127.19|Bank Details|Conditional|
|127.20|Originator/Authorizer date settlement|Optional|
|127.23|Payee name and address|Conditional|
|127.24|Payer account|Optional|
|127.25|ICC data|Optional|
|127.26|Original node|Conditional|
|127.27|Card verification result|Optional|
|127.28|American Express Card Identifier (CID)|Optional|
|127.29|3D Secure Data|Optional|
|128|MAC Extended|Conditional|



### **3.3 Authorization Request Response (0110)** 

|**Bit Data**|**Element Name**|**Presence Indicator**|
|---|---|---|
|2|Primary account number|Mandatory|
|3|Processing code|Mandatory|
|4|Amount, transaction|Mandatory|
|7|Transmission Date and Time|Conditional|
|11|Systems trace audit number|Conditional|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Conditional|
|14|Date, expiration|Conditional|
|15|Date, Settlement|Conditional|
|18|Merchant’s type|Mandatory|
|22|POS entry mode|Mandatory|
|23|Card sequence number|Conditional|
|25|POS condition code|Mandatory|
|28|Amount, transaction fee|Conditional|
|30|Amount, transaction processing fee|Conditional|
|32|Acquiring institution id code|Mandatory|
|33|Forwarding institution id code|Conditional|
|35|Track 2 data|Conditional|
|37|Retrieval reference number|Mandatory|
|38|Authorization id response|Conditional|
|39|Response code|Mandatory|
|40|Service restriction code|Conditional|
|41|Card acceptor terminal id|Optional|
|42|Card acceptor id code|Conditional|
|43|Card acceptor name/location|Conditional|
|49|Currency code, transaction|Mandatory|
|54|Additional amounts|Conditional|
|59|Transport (echo) data|Conditional|
|102|Account identification 1|Optional|
|103|Account identification 2|Optional|
|123|POS data code|Mandatory|
|124|Near Field Communication Data|Conditional|
|127.1|Fieldmap|Conditional|
|127.6|Authorization profile|Optional|
|127.16|Address verification result|Conditional|
|127.17|Cardholder information|Optional|
|127.20|Originator/Authorizer date settlement|Optional|
|127.25|ICC data|Optional|
|127.27|Card verification result|Optional|
|127.30|3D Secure Result|Optional|
|128|MAC Extended|Conditional|



### **3.4 Authorization Advice (0120/0121)** 

|**Bit Data**|**Element Name**|**Presence Indicator**|
|---|---|---|
|2|Primary account number|Conditional|
|3|Processing code|Mandatory|
|4|Amount, transaction|Mandatory|
|5|Amount, settlement|Conditional|
|7|Transmission date and time|Mandatory|
|9|Conversion rate, settlement|Conditional|
|11|Systems trace audit number|Mandatory|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Mandatory|
|14|Date, expiration|Conditional|
|15|Date, settlement|Conditional|
|16|Date, conversion|Conditional|
|18|Merchant’s type|Conditional|
|22|POS entry mode|Mandatory|
|23|Card sequence number|Conditional|
|25|POS condition code|Mandatory|
|28|Amount, transaction fee|Optional|
|29|Amount, settlement fee|Conditional|
|30|Amount, transaction processing fee|Optional|
|31|Amount, settle processing fee|Conditional|
|32|Acquiring institution id code|Conditional|
|33|Forwarding institution id code|Optional|
|35|Track 2 data|Conditional|
|37|Retrieval reference number|Optional|
|40|Service restriction code|Optional|
|41|Card acceptor terminal id|Mandatory|
|42|Card acceptor id code|Mandatory|
|43|Card acceptor name/location|Mandatory|
|45|Track 1 data|Optional|
|49|Currency code, transaction|Mandatory|
|50|Currency code, settlement|Conditional|
|54|Additional amounts|Conditional|
|56|Message reason code|Optional|
|58|Authorizing agent id cod|Conditional|
|59|Transport (echo) data|Optional|
|67|Extended payment code|Conditional|
|90|Original data elements|Conditional|
|95|Replacement amounts|Conditional|
|98|Payee|Conditional|
|100|Receiving institution id code|Conditional|
|102|Account identification 1|Conditional|
|103|Account identification 2|Conditional|
|123|POS data code|Mandatory|
|124|Near Field Communication Data|Conditional|
|127.1|Bitmap|Conditional|
|127.4|POS data|Optional|
|127.5|Service station data|Optional|
|127.7|Check data|Conditional|
|127.12|Terminal owner|Conditional|
|127.13|POS Geographic data|Mandatory|
|127.14|Sponsor bank|Conditional|
|127.19|Bank Details|Conditional|
|127.20|Originator/Authorizer date settlement|Optional|
|127.23|Payee name and address|Conditional|
|127.24|Payer account|Optional|
|127.25|ICC data|Optional|
|127.26|Original node|Conditional|
|127.28|American Express Card Identifier (CID)|Optional|
|128|MAC Extended|Conditional|



### **3.5 Authorization Advice Response (0130)** 

|**Bit Data**|**Element Name**|**Presence Indicator**|
|---|---|---|
|2|Primary account number|Conditional|
|3|Processing code|Optional|
|4|Amount, transaction|Optional|
|5|Amount, settlement|Conditional|
|7|Transmission date and time|Mandatory|
|9|Conversion rate, settlement|Conditional|
|11|Systems trace audit number|Mandatory|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Mandatory|
|14|Date, expiration|Optional|
|15|Date, settlement|Conditional|
|16|Date, conversion|Conditional|
|18|Merchant’s type|Conditional|
|22|POS entry mode|Mandatory|
|23|Card sequence number|Optional|
|25|POS condition code|Mandatory|
|28|Amount, transaction fee|Optional|
|29|Amount, settlement fee|Conditional|
|30|Amount, transaction processing fee|Optional|
|31|Amount, settle processing fee|Conditional|
|32|Acquiring institution id code|Optional|
|33|Forwarding institution id code|Conditional|
|35|Track 2 data|Conditional|
|37|Retrieval reference number|Optional|
|38|Authorization id response|Conditional|
|39|Response code|Mandatory|
|40|Service restriction code|Conditional|
|41|Card acceptor terminal id|Mandatory|
|42|Card acceptor id code|Mandatory|
|43|Card acceptor name/location|Mandatory|
|44|Additional response data|Optional|
|48|Additional data|Conditional|
|49|Currency code, transaction|Mandatory|
|50|Currency code, settlement|Conditional|
|54|Additional amounts|Optional|
|58|Authorizing agent id code|Conditional|
|59|Transport (echo) data|Conditional|
|90|Original data elements|Conditional|
|102|Account identification 1|Optional|
|103|Account identification 2|Optional|
|123|POS data code|Mandatory|
|124|Near Field Communication Data|Conditional|
|127.1|Bitmap|Conditional|
|127.2|Switch Key|Optional|
|127.3|Routing Information|Mandatory|
|127.6|Authorization profile|Optional|
|127.12|Terminal Owner|Conditional|
|127.16|Address verification result|Conditional|
|127.17|Cardholder information|Optional|
|127.20|Originator/Authorizer date settlement|Optional|
|127.22|Structured Data|Conditional|
|127.25|ICC data|Optional|
|127.27|Card verification result|Optional|
|127.30|3D Secure Result|Optional|
|128|MAC Extended|Conditional|



### **3.6 Financial Request/Repeat (0200/0201)** 

|**Bit Data**|**Element Name**|**Presence Indicator**|
|---|---|---|
|2|Primary account number|Conditional|
|3|Processing code|Mandatory|
|4|Amount, transaction|Mandatory|
|5|Amount, settlement|Conditional|
|7|Transmission date and time|Mandatory|
|9|Conversion rate, settlement|Conditional|
|11|Systems trace audit number|Mandatory|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Mandatory|
|14|Date, expiration|Conditional|
|15|Date, settlement|Conditional|
|16|Date, conversion|Conditional|
|18|Merchant’s type|Conditional|
|22|POS entry mode|Mandatory|
|23|Card sequence number|Conditional|
|25|POS condition code|Mandatory|
|26|POS PIN capture code|Conditional|
|27|Authorization id response length|Conditional|
|28|Amount, transaction fee|Optional|
|29|Amount, settlement fee|Conditional|
|30|Amount, transaction processing fee|Optional|
|31|Amount, settle processing fee|Conditional|
|32|Acquiring institution id code|Conditional|
|33|Forwarding institution id code|Optional|
|35|Track 2 data|Conditional|
|37|Retrieval reference number|Mandatory|
|40|Service restriction code|Conditional|
|41|Card acceptor terminal id|Mandatory|
|42|Card acceptor id code|Mandatory|
|43|Card acceptor name/location|Mandatory|
|45|Track 1 data|Optional|
|49|Currency code, transaction|Mandatory|
|50|Currency code, settlement|Conditional|
|52|PIN data|Conditional|
|53|Security related control information|Conditional|
|54|Additional amounts|Conditional|
|56|Message reason code|Optional|
|57|Authorization life cycle|Optional|
|59|Transport (echo) data|Optional|
|67|Extended payment code|Conditional|
|90|Original data elements|Conditional|
|98|Payee|Conditional|
|100|Receiving institution id code|Conditional|
|102|Account identification 1|Conditional|
|103|Account identification 2|Conditional|
|123|POS data code|Mandatory|
|124|Near Field Communication Data|Conditional|
|127.1|Bitmap|Conditional|
|127.4|POS data|Optional|
|127.5|Service station data|Optional|
|127.7|Check or address verification data|Conditional|
|127.10|CVV2|Optional|
|127.12|Terminal owner|Conditional|
|127.13|POS Geographic data|Mandatory|
|127.14|Sponsor bank|Conditional|
|127.15|Address verification data|Conditional|
|127.18|Validation data|Optional|
|127.19|Bank Details|Conditional|
|127.20|Originator/Authorizer date settlement|Optional|
|127.22|Structured Data|Conditional|
|127.23|Payee name and address|Conditional|
|127.24|Payer account|Optional|
|127.25|ICC data|Optional|
|127.26|Original node|Conditional|
|127.27|Card verification result|Optional|
|127.28|American Express Card Identifier (CID)|Optional|
|127.29|3D Secure Data|Optional|



MAC Extended 

Conditional 

128 

### **3.6 Financial Request Response (0210)** 

|**Bit Data**|**Element Name**|**Presence Indicator**|
|---|---|---|
|2|Primary account number|Conditional|
|3|Processing code|Optional|
|4|Amount, transaction|Optional|
|5|Amount, settlement|Conditional|
|7|Transmission date and time|Mandatory|
|9|Conversion rate, settlement|Conditional|
|11|Systems trace audit number|Mandatory|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Mandatory|
|14|Date, expiration|Optional|
|15|Date, settlement|Conditional|
|16|Date, conversion|Conditional|
|18|Merchant’s type|Conditional|
|22|POS entry mode|Mandatory|
|23|Card sequence number|Optional|
|25|POS condition code|Mandatory|
|28|Amount, transaction fee|Optional|
|29|Amount, settlement fee|Conditional|
|30|Amount, transaction processing fee|Optional|
|31|Amount, settle processing fee|Conditional|
|32|Acquiring institution id code|Optional|
|33|Forwarding institution id code|Conditional|
|35|Track 2 data|Conditional|
|37|Retrieval reference number|Optional|
|38|Authorization id response|Conditional|
|39|Response code|Mandatory|
|40|Service restriction code|Conditional|
|41|Card acceptor terminal id|Mandatory|
|42|Card acceptor id code|Mandatory|
|43|Card acceptor name/location|Mandatory|
|44|Additional response data|Optional|
|48|Additional data|Conditional|
|49|Currency code, transaction|Mandatory|
|50|Currency code, settlement|Conditional|
|54|Additional amounts|Optional|
|58|Authorizing agent id code|Conditional|
|59|Transport (echo) data|Conditional|
|102|Account identification 1|Optional|
|103|Account identification 2|Optional|
|123|POS data code|Mandatory|
|124|Near Field Communication Data|Conditional|
|127.1|Bitmap|Conditional|
|127.2|Switch Key|Optional|
|127.3|Routing Information|Mandatory|
|127.6|Authorization profile|Optional|
|127.12|Terminal Owner|Conditional|
|127.16|Address verification result|Conditional|
|127.17|Cardholder information|Optional|
|127.20|Originator/Authorizer date settlement|Optional|
|127.22|Structured Data|Conditional|
|127.25|ICC data|Optional|
|127.27|Card verification result|Optional|
|127.30|3D Secure Result|Optional|
|128|MAC Extended|Conditional|



### **3.8 Financial Advice/Repeat (Repeat) (0220/0221)** 

|**Bit Data**|**Element Name**|**Presence Indicator**|
|---|---|---|
|2|Primary account number|Conditional|
|3|Processing code|Mandatory|
|4|Amount, transaction|Mandatory|
|5|Amount, settlement|Conditional|
|7|Transmission date and time|Mandatory|
|9|Conversion rate, settlement|Conditional|
|11|Systems trace audit number|Mandatory|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Mandatory|
|14|Date, expiration|Conditional|
|15|Date, settlement|Conditional|
|16|Date, conversion|Conditional|
|18|Merchant’s type|Conditional|
|22|POS entry mode|Mandatory|
|23|Card sequence number|Conditional|
|25|POS condition code|Mandatory|
|26|POS PIN capture code|Conditional|
|28|Amount, transaction fee|Optional|
|29|Amount, settlement fee|Conditional|
|30|Amount, transaction processing fee|Optional|
|31|Amount, settle processing fee|Conditional|
|32|Acquiring institution id code|Conditional|
|33|Forwarding institution id code|Optional|
|35|Track 2 data|Conditional|
|37|Retrieval reference number|Optional|
|38|Authorization id response|Conditional|
|40|Service restriction code|Optional|
|41|Card acceptor terminal id|Mandatory|
|42|Card acceptor id code|Mandatory|
|43|Card acceptor name/location|Mandatory|
|45|Track 1 data|Optional|
|49|Currency code, transaction|Mandatory|
|50|Currency code, settlement|Conditional|
|54|Additional amounts|Conditional|
|56|Message reason code|Optional|
|58|Authorizing agent id code|Conditional|
|59|Transport (echo) data|Optional|
|67|Extended payment code|Conditional|
|90|Original data elements|Conditional|
|95|Replacement amounts|Conditional|
|98|Payee|Conditional|
|100|Receiving Institution ID Code|Optional|
|102|Account identification 1|Conditional|
|103|Account identification 2|Conditional|
|123|POS data code|Mandatory|
|124|Near Field Communication Data|Conditional|
|127.1|Bitmap|Conditional|
|127.4|POS data|Optional|
|127.5|Service station data|Optional|
|127.6|Authorization profile|Optional|
|127.12|Terminal owner|Conditional|
|127.13|POS Geographic data|Mandatory|
|127.14|Sponsor bank|Conditional|
|127.19|Bank Details|Conditional|
|127.20|Originator/Authorizer date settlement|Optional|
|127.23|Payee name and address|Conditional|
|127.24|Payer account|Optional|
|127.25|ICC data|Optional|
|127.26|Original node|Conditional|
|127.27|Card verification result|Optional|
|128|MAC Extended|Conditional|



### **3.9 Financial Advice Response (0230)** 

|**Bit Data**|**Element Name**|**Presence Indicator**|
|---|---|---|
|2|Primary account number|Conditional|
|3|Processing code|Optional|
|4|Amount, transaction|Optional|
|5|Amount, settlement|Conditional|
|7|Transmission date and time|Mandatory|
|9|Conversion rate, settlement|Conditional|
|11|Systems trace audit number|Mandatory|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Mandatory|
|14|Date, expiration|Optional|
|15|Date, settlement|Conditional|
|16|Date, conversion|Conditional|
|18|Merchant’s type|Conditional|
|22|POS entry mode|Mandatory|
|23|Card sequence number|Optional|
|25|POS condition code|Mandatory|
|28|Amount, transaction fee|Optional|
|29|Amount, settlement fee|Conditional|
|30|Amount, transaction processing fee|Optional|
|31|Amount, settle processing fee|Conditional|
|32|Acquiring institution id code|Optional|
|33|Forwarding institution id code|Conditional|
|35|Track 2 data|Conditional|
|37|Retrieval reference number|Optional|
|38|Authorization id response|Conditional|
|39|Response code|Mandatory|
|40|Service restriction code|Conditional|
|41|Card acceptor terminal id|Mandatory|
|42|Card acceptor id code|Mandatory|
|43|Card acceptor name/location|Mandatory|
|44|Additional response data|Optional|
|48|Additional data|Conditional|
|49|Currency code, transaction|Mandatory|
|50|Currency code, settlement|Conditional|
|54|Additional amounts|Optional|
|58|Authorizing agent id code|Conditional|
|59|Transport (echo) data|Conditional|
|90|Original data elements|Conditional|
|102|Account identification 1|Optional|
|103|Account identification 2|Optional|
|123|POS data code|Mandatory|
|124|Near Field Communication Data|Conditional|
|127.1|Bitmap|Conditional|
|127.2|Switch Key|Optional|
|127.3|Routing Information|Mandatory|
|127.6|Authorization profile|Optional|
|127.12|Terminal Owner|Conditional|
|127.16|Address verification result|Conditional|
|127.17|Cardholder information|Optional|
|127.20|Originator/Authorizer date settlement|Optional|
|127.22|Structured Data|Conditional|
|127.25|ICC data|Optional|
|127.27|Card verification result|Optional|
|127.30|3D Secure Result|Optional|
|128|MAC Extended|Conditional|



### **3.10 Reversal Advice (Repeat) (0420/0421)** 

|**Bit Data**|**Element Name**|**Presence Indicator**|
|---|---|---|
|2|Primary account number|Conditional|
|3|Processing code|Mandatory|
|4|Amount, transaction|Mandatory|
|5|Amount, settlement|Conditional|
|7|Transmission date and time|Mandatory|
|9|Conversion rate, settlement|Conditional|
|11|Systems trace audit number|Mandatory|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Mandatory|
|14|Date, expiration|Conditional|
|15|Date, settlement|Conditional|
|16|Date, conversion|Conditional|
|18|Merchant’s type|Conditional|
|22|POS entry mode|Mandatory|
|23|Card sequence number|Conditional|
|25|POS condition code|Mandatory|
|28|Amount, transaction fee|Optional|
|29|Amount, settlement fee|Conditional|
|30|Amount, transaction processing fee|Optional|
|31|Amount, settle processing fee|Conditional|
|32|Acquiring institution id code|Conditional|
|33|Forwarding institution id code|Optional|
|35|Track 2 data|Conditional|
|37|Retrieval reference number|Optional|
|38|Authorization id response|Conditional|
|39|Response Code|Optional|
|40|Service restriction code|Optional|
|41|Card acceptor terminal id|Mandatory|
|42|Card acceptor id code|Mandatory|
|43|Card acceptor name/location|Mandatory|
|45|Track 1 data|Optional|
|49|Currency code, transaction|Mandatory|
|50|Currency code, settlement|Conditional|
|56|Message reason code|Optional|
|59|Transport (echo) data|Optional|
|67|Extended payment code|Conditional|
|90|Original data elements|Mandatory|
|95|Replacement amounts|Conditional|
|98|Payee|Conditional|
|100|Receiving Institution ID Code|Optional|
|123|POS data code|Mandatory|
|124|Near Field Communication Data|Conditional|
|127.1|Bitmap|Conditional|
|127.4|POS data|Optional|
|127.5|Service station data|Optional|
|127.12|Terminal owner|Conditional|
|127.13|POS Geographic data|Mandatory|
|127.14|Sponsor bank|Conditional|
|127.19|Bank Details|Conditional|
|127.20|Originator/Authorizer date settlement|Optional|
|127.23|Payee name and address|Conditional|
|127.24|Payer account|Optional|
|127.25|ICC data|Optional|
|127.26|Original node|Conditional|
|127.27|Card verification result|Optional|
|128|MAC Extended|Conditional|



### **3.11Reversal Advice Response (0430)** 

|**Bit Data**|**Element Name**|**Presence Indicator**|
|---|---|---|
|2|Primary account number|Conditional|
|3|Processing code|Mandatory|
|4|Amount, transaction|Mandatory|
|5|Amount, settlement|Conditional|
|7|Transmission date and time|Mandatory|
|9|Conversion rate, settlement|Conditional|
|11|Systems trace audit number|Mandatory|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Mandatory|
|14|Date, expiration|Conditional|
|15|Date, settlement|Conditional|
|16|Date, conversion|Conditional|
|18|Merchant’s type|Conditional|
|22|POS entry mode|Mandatory|
|23|Card sequence number|Conditional|
|25|POS condition code|Mandatory|
|29|Amount, settlement fee|Optional|
|31|Amount, settle processing fee|Optional|
|32|Acquiring institution id code|Conditional|
|33|Forwarding institution id code|Optional|
|35|Track 2 data|Conditional|
|37|Retrieval reference number|Optional|
|38|Authorization id response|Conditional|
|39|Response code|Mandatory|
|40|Service restriction code|Optional|
|41|Card acceptor terminal id|Mandatory|
|42|Card acceptor id code|Mandatory|
|49|Currency code, transaction|Mandatory|
|50|Currency code, settlement|Conditional|
|54|Additional amounts|Conditional|
|59|Transport (echo) data|Conditional|
|90|Original data elements|Mandatory|
|95|Replacement amounts|Conditional|
|124|Near Field Communication Data|Conditional|
|127.1|Bitmap|Conditional|
|127.3|Routing Information|Mandatory|
|128|MAC Extended|Conditional|



### **3.12 Network Management (Repeat) (0800/0801)** 

|**Bit Data**|**Element Name**|**Presence Indicator**|
|---|---|---|
|7|Transmission date and time|Mandatory|
|11|Systems trace audit number|Mandatory|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Mandatory|
|70|Network management info code|Mandatory|
|128|MAC Extended|Conditional|



### **3.13 Network Management (0810)** 

|**Bit Data**|**Element Name**|**Presence Indicator**|
|---|---|---|
|7|Transmission date and time|Mandatory|
|11|Systems trace audit number|Mandatory|
|12|Time, local transaction|Mandatory|
|13|Date, local transaction|Mandatory|
|39|Response code|Mandatory|
|53|Security related control information|Conditional|
|70|Network management info code|Always sent|
|125|Network management information|Conditional|
|128|MAC Extended|Conditional|



# DATA ELEMENT DEFINITION 

The following ISO Data Elements are supported by the Interface. 

## **Field #2 - Primary, Account Number** 

**Field No Format Attr** 2 LLVAR n ..19 

**A number identifying the cardholder and the card issuer. Typically, this number is embossed on the front of the card and encoded on Track 2 of the magnetic stripe.** 

## **Field #3 - Processing Code** 

**Field No Format Attr** 3 n 6 

The customer transaction type and the account types, if any, affected by the transaction. This is a fixed length field consisting of 3 data elements: 

- Transaction type (positions 1 - 2) 

- Account type affected for debits and inquiries and the "from" account for transfers 

- (positions 3 - 4) 

- Account type affected for credits and the "to" account for transfers (positions 5 - 6) 

The following processing codes will be supported in the Interface 

|**Transaction Type**|**Code**|
|---|---|
|Cash Withdrawal|01|
|Cheque Book Request|91|
|Adhoc Statement Request|90|
|Cash Advance|01|
|Purchase with Cash back|09|
|Balance Inquiry|31|
|Link Account Inquiry|30|
|Mini Statement|38|
|Fund Transfer|40|
|POS Pre – Authorization|60|
|POS Pre – Authorization Completion|61|
|PIN Change|90|
|Purchase|00|
|Refund|20|
|Deposit|21|
|Purchase Adjustment|02|
|Refund Adjustment|02|



Account Type Codes 

|**Code**|**Description**|
|---|---|
|00|Default – unspecified|



|10|Savings|
|---|---|
|20|Current|
|30|Credit|
|40|Universal Account|
|50|Investment Account|



## **Field #4 - Amount, Transaction** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|4||n 12|



This field contains the transaction amount in the transaction currency. This amount is expressed in lowest denominations. 

#### **Field #5 - Amount, Settlement** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|5||n 12|



The funds to be transferred between the acquirer and card issuer equal to the amount, transaction as expressed in the settlement currency. 

## **Field #7 - Transmission Date and Time** 

**Field No** 

**Format** 

**Attr** 

MMDDhhmmss 

n 10 

7 



The date and time, expressed in Coordinated Universal Time (UTC), when this message is sent by the message initiator. 

## **Field #9 - Conversion Rate, Settlement** 

**Field No Format Attr** 9 n 8 

This field contains the factor used in conversion from the transaction currency amount to the local currency amount. The left most digit indicates the number of decimal places from the right. 

## **Field #11 - System Trace Audit Number** 

**Field No Format Attr 11 n 6** A number assigned by a transaction originator to assist in identifying a transaction uniquely. The systems trace audit number remains unchanged for all messages within a transaction. 

## **Field #12 - Time, Local Transaction** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**12**|**Hhmmss**|**n 6**|



The local time at which the transaction takes place at the card acceptor location in authorization and financial messages. 

For all other transactions, this field indicates the local time set by the initiator of the first message of the transaction. 

## **Field #13 - Date, Local Transaction** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**13**|**MMDD**|**n 4**|



The local date at which the transaction takes place at the card acceptor location in authorization and financial messages. 

For all other transactions, this field indicates the local date set by the initiator of the first message of the transaction. 

## **Field #14 - Date, Expiration** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**14**|**YYMM**|**n 4**|



This field contains the date on which settlement between the gateway and intermediate network facilities will be done. 

## **Field #15 - Date, Settlement** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**13**|**MMDD**|**n 4**|



The month and day for which financial totals are reconciled between the acquirer and the issuer. 

## **Field #16- Date, Conversion** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**13**|**MMDD**|**n 4**|



The month and day on which the currency for the transaction was converted. 

## **Field #18 –Merchant Type** 

**Field No Format Attr 18 n 4** The classification of the merchant’s type of business product or service. Codes to be developed within each country. 

## **Field #22 –POS Entry Mode** 

**Field No Format Attr 22 N3** 

A series of codes that identify the actual method used to capture the account number and expiry date when a terminal is used, and the PIN capture capability of the terminal. This is a fixed length field consisting of 2 data elements: 

- PAN entry mode (positions 1 - 2) 

- PIN entry capability (position 3) 

## **Field #23 – Card Sequence number** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**23**||**n3**|



A number distinguishing between separate cards with the same primary account number or primary account number extended. 

## **Field #25 –POS Condition Code** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**25**||**n2**|



A code that describes the condition under which the transaction takes place at the Point-OfService. 

00 Normal presentment 

|01|Customer not present|
|---|---|
|02|Unattended terminal - card can be retained|
|03|Merchant suspicious|
|04|Electronic Cash Register interface|
|05|Customer present, card not present|
|06|Pre-authorized request|
|07|Telephone device required|
|08|Mail/telephone order|
|09|POS security alert|
|10|Customer identity verified|
|11|Suspected fraud|
|12|Security reasons|
|13|Representation of item|
|14|Public utility terminal|
|15|Customer's terminal|
|16|Administrative terminal|
|17|Returned item|
|18|No check in envelope – return|
|19|Deposit out of balance – return|
|20|Payment out of balance – return|
|21|Manual reversal|
|22|Terminal error – counted|
|23|Terminal error - not counted|
|24|Deposit out of balance – apply|
|25|Payment out of balance – apply|
|26|Withdrawal error – reversed|
|27|Unattended terminal - card cannot be retained|



Additional codes can be defined for private use. 

## **Field #26 – POS PIN Capture Code** 

**Field No Format Attr 26 n2** 

The maximum number of PIN characters that can be accepted by the Point-of-Service device. Valid values are "04" to "12" ("00" to "03" are reserved by ISO) and if the POS device does not accept PINs or it is unknown whether the device does, this value should be set to "12". 

## **Field #27 – Authorization ID Response Length** 

**Field No Format Attr 27 N1** 

The maximum length of the authorization ID response which the acquirer can accommodate. The card issuer or agent shall limit the authorization ID response to this 

length. 

## **Field #28 - Amount, Transaction Fee** 

**Field No Format Attr** 

**28 x + n 8** 

A fee charged, by the acquirer to the issuer, for transaction activity, in the currency of the amount, transaction. 

## **Field #29 - Amount, Settlement Fee** 

**Field No Format Attr 29 x + n 8** 

A fee charged, by the acquirer to the issuer, for transaction activity, in the currency of the amount, settlement. 

## **Field #30 - Amount, Transaction Processing Fee** 

**Field No Format Attr** 

**30 x + n 8** 

A fee charged by the network for the handling and routing of messages, in the currency of amount, transaction. This field is usually inserted by the network into the applicable ~~messages~~ 

## **Field #31 - Amount, Settlement Processing Fee** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**31**||**x + n 8**|



A fee charged by the network for the handling and routing of messages, in the currency of amount, settlement. This field is usually inserted by the network into the applicable messages. 

## **Field #32 - Acquiring Institution Identification Code** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**32**|**LLVAR**|**an.. 11**|



A code identifying the financial institution acting as the acquirer of this customer transaction. The acquirer is the member or system user that signed the merchant, installed the ATM or dispensed cash. This field usually contains the BIN (see PAN) of the acquirer, but could be any other number assigned to it by the relevant authorities. 

When a processing center operates for multiple acquirers, this is the code for the individual member or system user, not a code for the processing center. 

## **Field #33 - Forwarding Institution Identification Code** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**33**|**LLVAR**|**n.. 11**|



A code identifying the institution that forwards the transaction in an interchange system en route to the card issuer. For example, assume that an acquirer routes a transaction via a third-party EFT switch to the card issuer. In the request from the acquirer to the EFT switch, this field contains the code of the acquirer. When the request is forwarded by the EFT switch to the card issuer, this field contains the code assigned to the EFT switch. 

## **Field #35 – Track 2 Data** 

**Field No Format Attr 35 LLVAR z..37** 

The information encoded on Track 2 of the magnetic stripe as defined in ISO 7813, including field separators but excluding the begin sentinel, end sentinel and longitudinal redundancy check characters. The field separator (FS) can be either a "=" or a "D" character. The layout of this field is as follows: 

|**Field**|**Length**|
|---|---|
|Primary account number|up to 19 digits|
|Field separator|1 digit|
|Expiry date (YYMM)|4 digits (or a field separator if not present)|
|Service restriction code|3 digits (or a field separator if not present)|
|Discretionary data|balance of available digits|



The primary account number, expiry date and service restriction code fields are described in further detail under fields 2, 14 and 40 in this document. 

For **Visa Cash** load transactions, this field contains the Visa Cash load signature data from the chip that is sent to the issuer to allow the issuer to verify the Visa Load Request Signature (S1). The layout of this field is as follows: 

|**Field**|**Length**|
|---|---|
|Visa Cash card<br>number|16 digits|
|Field separator|1 digit (can be a "=" or a "D" character)|
|Expiry date (YYMM)|4 digits: Only the YYMM portion of the Visa Cash expiration<br>date|
|Service restriction<br>code|3 digits (must be "101")|
|Visa Cash balance|6 digits|
|Transaction number|5 digits|
|GMT offset|2 digits|



## **Field #37 – Retrieval Reference Number** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**37**||**an 12**|



A reference number supplied by the system retaining the original source information and used to assist in locating that information or a copy thereof. 

## **Field #38 – Authorization code** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**38**||**n 6**|



A code assigned by the authorizing institution indicating approval. 

## **Field #39 - Response Code** 

|**Field**|**No**<br>**Format**|**Attr**|
|---|---|---|
|**39**||**an 2**|
|code  i<br>00|that defines the disposition of a transaction.<br>Approved or completed successfully||
|01|Refer to card issuer||
|02|Refer to card issuer, special condition||
|03|Invalid merchant||
|04|Pick-up card||
|05|Do not honor||
|06|Error||
|07|Pick-up card, special condition||
|08|Honor with identification||
|09|Request in progress||
|10|Approved, partial||
|11|Approved, VIP||
|12|Invalid transaction||
|13|Invalid amount||
|14|Invalid card number||



A code that defines the disposition of a transaction. 

|15|No such issuer|
|---|---|
|16|Approved, update track 3|
|17|Customer cancellation|
|18|Customer dispute|
|19|Re-enter transaction|
|20|Invalid response|
|21|No action taken|
|22|Suspected malfunction|
|23|Unacceptable transaction fee|
|24|File update not supported|
|25|Unable to locate record|
|26|Duplicate record|
|27|File update edit error|
|28|File update file locked|
|29|File update failed|
|30|Format error|
|31|Bank not supported|
|32|Completed partially|
|33|Expired card, pick-up|
|34|Suspected fraud, pick-up|
|35|Contact acquirer, pick-up|
|36|Restricted card, pick-up|
|37|Call acquirer security, pick-up|
|38|PIN tries exceeded, pick-up|
|39|No credit account|
|40|Function not supported|
|41|Lost card|
|42|No universal account|
|43|Stolen card|
|44|No investment account|
|51|Not sufficient funds|
|52|No check account|
|53|No savings account|
|54|Expired card|
|55|Incorrect PIN|
|56|No card record|
|57|Transaction not permitted to cardholder|
|58|Transaction not permitted on terminal|
|59|Suspected fraud|
|60|Contact acquirer|
|61|Exceeds withdrawal limit|
|62|Restricted card|
|63|Security violation|
|64|Original amount incorrect|
|65|Exceeds withdrawal frequency|
|66|Call acquirer security|
|67|Hard capture|
|68|Response received too late|
|75|PIN tries exceeded|
|77|Intervene, bank approval required|
|78|Intervene, bank approval required for partial amount|
|90|Cut-off in progress|
|91|Issuer or switch inoperative|
|92|Routing error|
|93|Violation of law|
|94|Duplicate transaction|
|95|Reconcile error|
|96|System malfunction|
|98|Exceeds cash limit|



## **Field #40 - Service Restriction Code** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**40**||**N3**|



An identification of geographic/service availability. Contains: 

- The area of usage and whether the card has additional read facilities 

|1|International card|
|---|---|
|2|International card - integrated circuit facilities|
|5|National use only|
|6|National use only - integrated circuit facilities|
|9|Test card - online authorization mandatory|



- The authorization processing requirements for this card 

|0|Normal authorization|
|---|---|
|2|Online authorization mandatory|
|4|Online authorization mandatory|



- The range of services available and PIN requirements 

- 0 PIN required 1 No restrictions - normal cardholder verification 

- 2 Goods and services only 

- 3 PIN required, ATM only 

- 5 PIN required, goods and services only at POS, cash at ATM 

- 6 PIN required if PIN pad present 

- 7 PIN required if PIN pad present, goods and services only at POS, cash at ATM 

- **Field #41 - Card Acceptor Terminal Identification** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**41**||**ans 8**|



A unique code identifying a terminal at the card acceptor location. 

#### **Field #42 - Card Acceptor Identification Code** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**42**||**ans 15**|



A code identifying the card acceptor (typically a merchant). 

## **Field #43 - Card Acceptor Name / Location** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**43**||**ans 40**|



The name and location of the card acceptor (such as a merchant or an ATM). This is a fixed length field consisting of 4 data elements: 

- The location information (positions 1 - 23), exclusive of city, state and country 

- The city (positions 24 - 36) in which the Point-of-Service is located 

- The state (positions 37 - 38) in which the Point-of-Service is located 

- The country (positions 39 - 40) in which the Point-of-Service is located 

For **Visa Cash** load transactions, this field contains the Visa Cash Service Identifier ("SV:") followed by the load request signature (S1), the load acquirer BIN and other location information. This is a fixed length field consisting of 5 data elements: 

- The Visa cash service identifier (positions 1 - 3), a constant value of "SV:" 

- The Visa load request signature (positions 4 - 19) 

- The Visa load acquirer BIN (positions 20 - 25) 

- The city (positions 26 - 38) in which the Point-of-Service is located 

- The country (positions 39 - 40) in which the Point-of-Service is located. 

## **Field #44 - Additional Response Data** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**44**|**LLVAR**|**ans..25**|



Used to provide other supplemental data (such as a telephone number for referrals) that may be required in response to an authorization or other type of transaction request. 

After a **PIN Change** request has been processed, this field contains the PIN offset (or PVV) in a 0610 message to the Source Node, as well as in a 0620 message to the Sink Node. If set in a 0610 message from the Sink Node, this field again indicates the PIN offset (or PVV). 

For **Visa Cash** load transactions, this field is used to carry signature information. In load responses, it contains the Visa Cash Service Identifier ("SV:") followed by the load response signature (S2). This is a fixed length field consisting of 2 data elements: 

- The Visa cash service identifier (positions 1 - 3), a constant value of "SV:" 

- The load authorization signature (positions 4 - 19) for this load operation 

In load settlement advices, it contains the Visa Cash Service Identifier ("SV:") followed by the load completion signature (S3). This is a fixed length field consisting of 2 data elements: 

- The Visa cash service identifier (positions 1 - 3), a constant value of "SV:" 

- The load completion signature (positions 4 - 19) for this load operation 

## **Field #45 - Track 1 Data** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**45**|**LLVAR**|**ans..76**|



The information encoded on Track 1 of the magnetic stripe as defined in ISO 7813, including field separators but excluding the begin sentinel, end sentinel and longitudinal redundancy check characters. 

Note that two structures are defined by ISO 7813, namely Structure A and Structure B. Structure A is reserved for proprietary use by card issuers, while Structure B is defined as follows: 

|**Field**|**Length**|
|---|---|
|Format Code|B (ASCII 66)|
|Primary account number|up to 19 digits|
|Field separator|1 character (ASCII 61 or 94)|
|Country Code|3 digits (or a field separator if not present)|
|Name|2 to 26 characters (this field is further described below)|
|Field separator|1 character (ASCII 61 or 94)|
|Expiry date (YYMM)|4 digits (or a field separator if not present)|
|Service restriction code|3 digits (or a field separator if not present)|
|Discretionary data|balance of available digits|



The primary account number, expiry date and service restriction code fields are described in further detail under fields 2, 14 and 40 in this document. 

The structure of the **Name** field is defined in the following table. Sub-fields are separated by means of a space character (ASCII 32). The minimum encoded data allowed is a single character followed by the surname separator. 

|**Field**|**Notes**|
|---|---|
|Surname||
|Surname separator|ASCII 47|
|First Name or Initial||
|Space|When required|
|Middle Name or Initial||
|Period|When followed by Title; ASCII 46|
|Title|When used|



The space character (ASCII 32) is required to separate the sub-fields of the **Name** field other than the surname. The separator terminating the surname should be encoded following the last sub-field of the **Name** field. If only the surname is encoded, it will follow the surname separator. 

**Note:** The NCS currently performs no validation on track 1 data and in no way attempts to unpack the sub-fields. The contents of this field are simply saved in the transaction record and passed upstream unchanged. 

## **Field #48 – Additional Data** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**48**|**LLVAR**|**ans...999**|



Used to provide linked account or mini-statement information for a linked account inquiry 

or a mini-statement inquiry. 

#### **Mini – statement Information** 

The format for field 48 when mini-statement data is to be sent downstream is as follows: 

3. A mini-statement heading line, containing tags to identify the format of the ministatement data lines that follows, e.g. 

```
DATE_TIME|SEQ_NR|TRAN_TYPE|TRAN_AMOUNT~
```

The different fields of the mini-statement heading line are separated by bar characters ("|") and the line is terminated by a tilde character ("~"). 

4. One or more mini-statement data lines, each similar to the identifying string above in structure, but containing the actual transaction data to be printed per line, e.g. 

```
19971201123123|001234|01|000000005000~
```

Below is a list of tags supported. 

|**Field**|**Tag name**|**Format**|
|---|---|---|
|Sequence number|SEQ_NR|n6|
|Date and time|DATE_TIME|n14, CCYYMMDDhhmmss|
|Terminal ID|TERM_ID|n8|
|Transaction type|TRAN_TYPE|n2|
|From account|FROM_ACC|n2|
|To account|TO_ACC|n2|
|Transaction<br>amount|TRAN_AMOUNT|n12|
|Account ID 1|ACC_ID1|ans28|
|Account ID 2|ACC_ID2|ans28|
|Authorization ID|AUTH_ID|n6|
|Currency code|CURR_CODE|n3 (Currency code of the Transaction Amount field)|
|Surcharge|SURCHARGE|n8|



#### **Linked Account Inquiry** 

In the case of a linked account inquiry, this field contains information relating to the accounts linked to the card that initiated the transaction. The information for up to 20 accounts can be returned. Note that when "00" is specified as an account type in the original request, a list of all accounts linked to the card is retrieved, and not only the linked accounts of the default account type. The format of the information associated with each account is as follows: 

|**Field**|**Length**|**Description**|
|---|---|---|
|Account<br>ID|28|The identifier uniquely identifying the account, left justified, space-<br>filled.|
|Account<br>type|2|The ISO 8583 account type of the account.|
|Currency<br>code|3|The ISO numeric currency code of the account.|
|Ledger<br>balance|13|The ledger balance of the account. The first character contains the<br>sign. A "D" indicates a debit (negative) balance and a "C" indicates a<br>credit (positive) balance.|



## **Field #49 - Currency Code, Transaction** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**49**||**n 3**|



The local currency of the acquirer or source location of the transaction. This is the currency code used for the following amount fields: 

- amount, transaction 

- amount, transaction fee 

- amount, transaction processing fee 

## **Field #50 - Currency Code, Settlement** 

**Field No Format Attr 50 n 3** 

A code identifying the currency of settlement. If this field is not present for a transaction, it is assumed that this field is the same as the currency code, transaction field. This is the currency code used for the following amount fields: 

- amount, settlement 

- · amount, settlement fee 

- amount, settlement processing fee 

- amount, net settlement 

## **Field #52 – PIN Data** 



<!-- Start of picture text -->
Field No  Format  Attr<br>52  Hex16<br><!-- End of picture text -->

The PIN data field contains the PIN (a number assigned to a cardholder intended to uniquely identify that cardholder) of the cardholder formatted into a 64-bit block and encrypted with a DES key. 

## **Field #53 – Security Related Control Information** 

**Field No Format Attr 53 Hex96** 

Identifies security management information used in the current transaction or specifies security management information to be used in future transactions. 

If the **DUKPT** scheme is used, the first 8 bytes of this field in authorization and financial transaction request messages containing an encrypted PIN block, contain the DUKPT key sequence number. 

In **PIN change** transactions, the first byte indicates the PIN to change: 

- binary 0 – insecure PIN (e.g. telephone PIN) 

- binary 1 – secure PIN (e.g. ATM PIN) 

The following 8 bytes of this field contains the new PIN formatted into a 64-bit block and encrypted with a DES key. It may be followed by the 8-byte DUKPT key sequence number if the DUKPT scheme is used. 

In **key change** transactions, this field contains the encrypted key in the first 8-24 bytes (8 for single, 16 for double, 24 for triple length), followed by a 3-byte key check value (i.e. the first 3 bytes of a clear value of all zeroes encrypted with the key). 

## **Field #54 – Additional Amounts** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**54**|**LLLVAR**|**an…120**|



Information on up to 6 amounts and related account data for which specific data elements have not been defined. Each amount is a fixed length field consisting of 5 data elements: 

- Account type (positions 1 - 2) 

- Amount type (positions 3 - 4) 

· Currency code (positions 5 - 7) 

- Amount sign (position 8) - "C" or "D" 

- Amount (position 9 - 20) 

When this field is sent by the entity that performed currency conversion this field should contain amounts in the transaction and settlement currencies if they differ. 

In a response message from the NCS, this field will always contain the approved amounts and cash amounts, if applicable. 

## **Field #56 – Message Reason Code** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**56**|**LLLVAR**|**n4**|



A code that provides the receiver of a request, advice or notification message with the reason, or purpose of that message. 

For original authorizations and financial transactions, it identifies why the type of message was sent (e.g. why an advice versus a request); for other messages, it states why this action was taken. 

|1003|Card issuer unavailable|
|---|---|
|1006|Under floor limit|
|1376|PIN verification failure|
|1377|Change dispensed|
|1378|IOU receipt printed|
|1510|Over floor limit|
|1800|Negative card|
|4000|Customer cancellation|
|4001|Unspecified, no action taken|
|4004|Completed partially|
|4021|Timeout waiting for response|



For place hold on card transactions, in Issuer File Update Advice (0322) or Administration (0600/0620) messages, it states why a card should be put on the hotcard list: 

|3000|Lost card|
|---|---|
|3001|Stolen card|
|3002|Undelivered card|
|3003|Counterfeit card|
|3700|Lost PIN|



If a hold response code has not been specified in these transactions, the message reason code field will be used to determine which hold response code to use for the transaction. A message reason code of "3001-Stolen card" will result in a hold response code of "43-Stolen card", otherwise "41-Lost card" will be used. 

In the case of a message to bank transaction, the message reason code specifies the type of message the cardholder wants to forward to the issuer. Note that in this case, the message reason code field is treated as a free-format field that the user can use for any user specific code. 

Message reason codes are defined in the ISO 8583 (1993) specification, and this specification has been used as basis for the codes defined here. 

The use of the following message reason codes have been deprecated: 

|1801|Card lost|
|---|---|
|1802|Card stolen|



## **Field #57 – Authorization Life – Cycle Code** 

**Field No Format Attr 57 LLLVAR n3** 

A value in calendar days, hours or minutes which defines the time period for which the acquirer is requesting guarantee of funds, or that the card issuer shall guarantee funds for a financial transaction which may follow. It consists of 2 fields: 

|-|Time code (position 1)|
|---|---|
|0|reserved for ISO use|
|1|calendar days|
|2|hours|
|3|minutes|
|4-5|reserved for ISO use|
|6-7|reserved for national use|
|8-9|reserved for private use|



- Time interval (positions 2 - 3): A value of 01 through 99 indicating the number of 

- reiterations indicated in position 1. 

## **Field #58 – Authorizing Agent ID Code** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**58**|**LLLVAR**|**n…11**|



A code identifying the authorizing agent institution. 

**Field #59 – Echo Data** 

**Field No Format Attr 59 LLLVAR ans…255** 

Contains data from the originator of the message that shall be returned unaltered in the response message. 

## **Field #67 – Extended Payment Code** 

**Field No Format Attr 67 n 2** 

The number of months that the cardholder prefers to pay for this item if permitted by the card issuer. 

## **Field #70 – Network Management Information Code** 

**Field No Format Attr 70 n 3** 

The code that defines the type of network management needed. 

|001|Sign On request|
|---|---|
|002|Sign Off request|
|101|Pin Working Key Change|
|160|MAC Working Key Change|
|301|Echo test|
|999|Text|



## **Field #90 – Original Data Elements** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**90**||**n 42**|



The data elements contained in the original message intended for transaction matching (e.g. to identify a transaction for correction or reversal). It is a fixed length field consisting of 5 data elements: 

- Original message type (positions 1 - 4) - the message type identifier of the original 

- message of the transaction being reversed. 

· Original systems trace audit number (positions 5 - 10) - the systems trace audit number of the original message. 

· Original transmission date and time (positions 11 - 20) - the transmission date and time of the original message 

· Original acquirer institution ID code (position 21 - 31) - the acquirer institution ID code of the original message (right justified with leading zeroes). 

- Original forwarding institution ID code (position 32 - 42) - the forwarding institution ID 

- code of the original message (right justified with leading zeroes). 

## **Field #95 – Replacement Amounts** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**95**||**n 42**|



The corrected amounts of a transaction in a partial or full reversal (or the final amounts for the transaction). It is a fixed length field consisting of 4 data elements: 

- Actual amount, transaction (positions 1 - 12) - the corrected, actual amount of the 

- customer’s transaction, in the currency of the transaction. 

- Actual amount, settlement (positions 13 - 24) - the corrected, actual amount of the 

- customer’s transaction, in the settlement currency. 

- Actual amount, transaction fee (positions 25 - 33) - the corrected, actual amount of the 

- fee (in format x + n8) for this customer transaction, in the currency of the transaction. 

- Actual amount, settlement fee (positions 34 - 42) - the corrected, actual amount of the fee (in 

- format x + n8) for this customer transaction, in the settlement currency. 

## **Field #97 – Amount, Net Settlement** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**97**||**X + n16**|



The net of all gross debit and gross credit amounts for a settlement period for a specific settlement entity. Specified in the settlement currency. 

## **Field #98 – Payee** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**98**||**ans25**|



A code or ID identifying the payee (recipient) of a payment transaction. This field is used in a payment transaction when the payee is an Institution defined payee. Customer defined payees do not have a payee ID. 

When using this field with the NCS Payments Engine, however, it contains the ID of the payee as maintained in the payee list for a cardholder. Because the Payments Engine assigns an ID to both institution and customer defined payees, this field can be used to refer to type of payee. 

## **Field #100 – Receiving Institution ID Code** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**100**|**LLVAR**|**n…11**|



A code identifying the financial institution that should receive a request or advice. This identification code is used when it is not possible to route a message using the account number field in the message, e.g. in a check verification message. When this field is included in a request or advice, it takes precedence over all account number fields for routing. 

## **Field #102 – Account Identification 1** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**102**|**LLVAR**|**n..28**|



A series of digits and/or characters used to identify a specific account held by the cardholder at the card issuer and if present, shall remain unchanged for the life of the transaction. This field usually contains the description of the "from" account. 

## **Field #103 – Account Identification 2** 

**Field No Format Attr 103 LLVAR n..28** 

A series of digits and/or characters used to identify a specific account held by the cardholder at the card issuer and if present, shall remain unchanged for the life of the transaction. This field usually contains the description of the "to" account. 

When used in payment transactions, this field specifies the bank account number of the payee. 

## **Field #118 – Payments, Number** 

**Field No Format Attr 118 LLLVAR n10** 

The total number of payments processed since the last settlement cutover (i.e. the sum number of all authorization transactions where positions 1 – 2 of the processing code in the financial transaction indicated a payment (50-59)). 

## **Field #119 – Payments, Reversal Numbers** 

|**Field No**|**Format**|**Attr**|
|---|---|---|
|**119**|**LLLVAR**|**n10**|



The total number of payment transactions reversed since the last settlement cutover (i.e. the sum number of all reversal transactions where positions 1 – 2 of the processing code in 

the reversal transaction indicated a payment (50-59) and where the original message type identifier indicates a financial transaction (02xx)). 

## **Field #123 – POS Data Code** 

**Field No Format Attr 123 LLLVAR an15** 

The field is used to identify terminal capability, terminal environment and presentation security data. It is used to indicate specific conditions that were present at the time a transaction took place at the Point-of-Service. This field consists of the following sub-fields: 

- The card data input capability (position 1) of the terminal. 

- The cardholder authentication capability (position 2) of the terminal. 

- The card capture capability (position 3) of the terminal. 

- The operating environment (position 4) of the terminal. 

- Indicates whether the cardholder is present (position 5). 

- Indicates whether the card is present (position 6). 

- The actual card data input mode (position 7) of the transaction. 

- The actual cardholder authentication method (position 8) of the transaction. 

- The cardholder authentication entity (position 9) of the transaction. 

- The card data output capability (position 10) of the terminal. 

- The terminal output capability (position 11) of the terminal. 

- The PIN capture capability (position 12) of the terminal. 

- The terminal operator (position 13). 

## **Field #124 – Near Field Communication Data** 

**Field No Format Attr 124 LLLLVAR ans…9999** 

This data element carries NFC Data. This would be transmitted to the processing system as received from POS Terminal. 

## **Field #125 – Network Management Information** 

**Field No Format Attr 125 LLLVAR ans..40** 

In key change transactions, this field contains the encrypted key in the first 16-32 positions of this field (16 for Single Length, 32 for Double Length), followed by 6 positions for the key check value. The encrypted key and key check value are encoded as hexadecimal characters in this field. (When using this field for key exchange messages Triple Length keys cannot be used) 

## **Field #127 – Reserved for Private Use** 

**Field No Format Attr** 

**127** 

**ans…999** 

**LLVAR** 

## **Field 127.1 - Bitmap** 

The bitmap signifies the presence (1) or absence (0) of data sub-elements contained within field 127. 

## **Field 127.2 – Switch Key** 

ANS..32. LLVAR 

The Switch Key is used by some processors to uniquely identify transactions. 

## **Field 127.3 - Routing Information** 

The totals group, Source Node, Sink Node and systems trace audit numbers associated with a customer transaction. Although passed in a variable length field, this is a fixed length field of 48 characters consisting of 5 data elements: 

- The source node (positions 1 - 12), left justified, space-filled. 

- The sink node (positions 13 - 24), left justified, space-filled. 

- The source node systems trace audit number (positions 25 - 30). 

- The sink node systems trace audit number (positions 31 - 36). 

- The totals group (positions 37 - 48), left justified, space-filled 

## **Field 127.4 - POS Data** 

Contains data passed from the Point-of-Service (POS) system, e.g. a cash register. Although passed in a variable length field, this is a fixed length field of 22 characters consisting of 3 data elements: 

- The POS terminal ID (positions 1 - 8) identifying the POS terminal, e.g. a cash register, on 

- which the transaction was performed. 

- The POS sequence number (positions 9 - 14) specifying the POS terminal sequence 

- number. 

- The POS operator ID (positions 15 - 22) specifying the cashier that performed the 

- transaction. 

## **Field 127.5 - Service Station Data** 

Contains data passed from a service station, i.e. forecourt system, for forwarding to the card issuer. This data is typically used for fleet management. This data element consists of 2 mandatory fields and up to 3 repetitions of a group of 3 fields. Note that this is a fixed format data element. All unused fields should contain spaces. 

- The vehicle usage (position 1) indicates whether the product(s) was purchased for 

- business or private use (0 - private, 1 - business). 

- The odometer reading (positions 2 - 7) of the vehicle. 

The following group of fields represents a fuel product and can be repeated up to 3 times (i.e. 3 different products are supported): 

- The product ID (positions 1 - 2) specifying the fuel product purchased. 

- The literage (positions 3 - 10) specifying the literage of the product purchased. 

- The amount (positions 11 - 22) of the product purchased. 

## **Field 127.6 – Authorization Profile** 

Provides additional information on the conditions under which authorization should be or was performed. 

#### **Field 127.7 – Check Data** 

Contains check guarantee or check verification data. This field can be in a number of different formats. The first 2 digits indicate the format. This field may be extended to new formats in the future. 

#### **Field 127.10 – CVV2** 

The embossed Visa CVV2 value manually entered when the magnetic stripe could not be read. 

#### **Field 127.13 - POS Geographic data** 

**Field No Format Attr 127.13 LLCHAR an..17** 

**Sample value:** 06.5244,003.3792 

Contains latitude & longitude of POS terminal at transaction time, used to identify the physical location of the device. 

#### **Example Breakdown** 

06.5244,003.3792 

#### **Data Layout** 

|**Component**|**Length**|**Type **|**Description**|**Example**|
|---|---|---|---|---|
|Latitude Degrees|2|Numeric|Degrees 00–90|06|
|Latitude Fractions|4|Numeric|Fractionalpart(4 decimals)|5244|
|Latitude Sign|0/1|Symbol|Optional(- onlyif South)|(–)|
|Longitude Degrees|3|Numeric|Degrees 000–180|003|
|Longitude Fractions|4|Numeric|Fractionalpart(4 decimals)|3792|
|Longitude Sign|0/1|Symbol|Optional(- onlyif West)|–|



#### **Formatting Rules** 

- **Latitude:** 

`o` DD.dddd → Positive values (North). `o` -DD.dddd → Negative values (South). `o` Degrees range: 00–90. 

- **Longitude:** 

`o` DDD.dddd → Positive values (East). `o` -DDD.dddd → Negative values (West). `o` Degrees range: 000–180. • **Decimal Places:** Variable 4 places. 

- **Comma Seperator:** , 

- **Zero Padding:** 

   - Latitude degrees → always 2 digits. 

   - `o` Longitude degrees → always 3 digits. `o` Decimal portion → Variable 4 digits. 

- **Poles/Meridian:** Use .0000 fraction where applicable. 

#### **Field 127.14 - Sponsor Bank** 

Identifies the institution sponsoring the card acceptor POS terminal. 

#### **Field 127.15 - Address Verification Data** 

This field can contain cardholder address information for a mail order or airline transaction. The layout is as follows: 

- The alphanumeric postal/ZIP code (positions 1 - 9). 

- The alphanumeric cardholder address (positions 10 - 29). 

#### **Field 127.16 - Address Verification Result** 

Contains the result of a transaction involving address verification. 

|A|Address matches, postal/ZIP code does not|
|---|---|
|E|Error|
|N|Neither address nor postal/ZIP code matches|
|R|Retry|
|U|Unavailable|
|Y|Address and postal/ZIP code matches|
|Z|postal/ZIP code matches, address does not|



**Field 127.17 - Cardholder Information** 

Contains cardholder related response information. This information is typically returned by the authorizer of the transaction. 

#### **Field 127.18 - Validation data** 

Contains cardholder related validation information. This information is typically supplied by the originator of the transaction as an additional security mechanism. This data can be used by the authorizer when authorizing a transaction. For example, this field could contain the identification number of the cardholder. If this value differs from the value carried in the cardholder database of the authorizer, the transaction may be declined. 

## **Field 127.19 - Bank details** 

Contains bank and branch details. 

This field is used to specify the bank details of the payee (recipient) of a payment transaction. Payments made to Customer defined payees require either bank details or address details to be specified in the transaction message. 

|**Field**|**Length**|**Format**|**Description**|
|---|---|---|---|
||||The financial institution where the bank account of the|
|Bank<br>ID|9|ANS|recipient is kept. Typically, the field will be space filled to the<br>right when less than 9 characters are available. The<br>interpretation of this field varies by country.|
||||The branch where the bank account is kept. Typically, thefield|
|Branch<br>ID|22|ANS|will be space filled to the right when less than 22<br>characters are available. The interpretation of this field<br>varies by country.|



#### **Field 127.20 - Originator / Authorizer date settlement** 

This field contains the settlement date of the authorizer. 

#### **Field 127.22 – Structured Data** 

### **Format** 

ans..9999, LLLLLLVAR 

### **Remarks** 

This is a Postilion specific addition to the ISO 8583 standard. As of Postilion 4.0 Service Pack 5, this field is stored in the Postilion Transaction Table. 

### **Description** 

This field uses a key/value pair structure to represent data. The idea behind the field is to provide a flexible mechanism with which to transport data through Postilion. There is therefore no limitation to what either the key or value may contain, provided of course that the format of the field is adhered to. 

The physical layout of the field is as follows: 

1 byte length indicator of the key length indicator 

Length indicator of the key 

Key 

1 byte length indicator of the value length indicator Length 

indicator of the value 

Value 

The table above describes the physical layout of a single key/value pair, it is therefore repeated for each key/value pair in the field. There is no limitation to the number of key value pairs that may be used, besides the overall field length. 

### **XML content** 

Postilion defines DTDs for the following: 

- StatementData 

- CardManagementUpdateData 

· CardManagementUpdateLoad 

## **Field 127.23 - Payee name and address** 

Contains identification and billing information for a payee. All fields are left-justified, space filled to the right. 

This field is used when making a payment to a Customer defined payee, where the payee is defined by its address details. A bank, for example, would use these details to post a check to the payee on the customers behalf. 

|**Field**|**Length**|**Format**|**Description**|
|---|---|---|---|
|Name|35|ANS|Name of the payee.|
|Address line 1|35|ANS|First address line.|
|Address line 2|35|ANS|Second address line.|
|Address line 3|35|ANS|Third address line.|
|City|35|ANS|City payee is located in.|
|Region|20|ANS|Region / State the City is located in.|
|Postal code|20|ANS|Postal code of the City.|
|Country code|3|A|A 3 letter ISO country code.|
|Phone|35|ANS|Phone number of payee.|



#### **Field 127.24 - Payer account** 

Defines the payer account ID at the payee. 

## **Field 127.25 – Integrated circuit card (ICC) Data** 

The field contains data for use with integrated circuit cards that implement the EMV specification. This field can contain ICC Data in XML formats 

IccData (field 127.25) is formatted as an XML document, as per a DTD. This XML document has an outer tag (IccData), and 2 primary inner tags (IccRequest and IccResponse). The IccRequest tag is used in request messages, and the IccResponse tag is used in response messages. 

A request message would have the following structure for example 

<?xml version="1.0"?> 

<IccData> 

<IccRequest> 

</IccRequest> 

</IccData> 

A response message would have the following structure for example: 

<?xml version="1.0"?> 

<IccData> 

<IccResponse> 

#### </IccResponse> 

#### </IccData> 

The IccRequest and IccResponse tags have a number of sub-tags which are defined as follows: 

## **ICC Data - IccRequest** 

The IccRequest tag may be present for a request or advice message (0100, 0120, 0200, 0220 or 0420). 

|**Field**|**Format**|**Condition**|**Description**|
|---|---|---|---|
|AmountAuthorized|n12|Conditional|The amount of the<br>transaction.|
||||Secondary amount<br>associated with the|
|AmountOther|n12|Conditional|transaction,<br>representing a<br>cashback amount.|
||||Identifies the<br>application on the ICC<br>as described in ISO/IEC|
|ApplicationIdentifier|ans..32|Optional|7816-5.<br>(Field value: translate<br>16 byte binary EMV<br>value to 32 byte hex<br>string.)|
|ApplicationInterchangeProfile|ans4|Conditional|Indicates the<br>capabilities of the ICC<br>to support specific<br>functions in the<br>application.<br>(Field value: translate 2<br>byte binary EMV value<br>to 4 byte hex string.)|
||||Counter maintained by<br>the application in the<br>ICC. (Incrementing the|
|ApplicationTransactionCounter|ans4|Conditional|ATC is managed by the<br>ICC).|
||||(Field value: translate 2<br>byte binary EMV value<br>to 4 byte hex string.)|
||||Indicates the issuer's<br>specified restrictions on<br>the geographic usage|
|ApplicationUsageControl|ans4|Optional|and services allowed for<br>the application.|
||||(Field value: translate 2<br>byte binary EMV value<br>to 4 byte hex string.)|
||||Code returned by the<br>issuer or generated by|
|AuthorizationResponseCode|an2|Optional|the terminal if it did not<br>receive an online|
||||response from the<br>issuer.|
|CardAuthenticationReliabilityIndicator|n1|Optional|May be set and sent by<br>the acquirer when the<br>acquirer or issuer is<br>inactive for card<br>authentication.|
|CardAuthenticationResultsCode|ans1|Optional|An issuer-supplied code<br>indicating card<br>authentication results.|
||||Indicates the status of|
|ChipConditionCode|n1|Optional|last chip attempt at the<br>terminal.|
||||The cryptogram<br>generated by the ICC.<br>Consists of one of the<br>following: Authorization<br>Request Cryptogram<br>(ARQC) for an<br>authorization request,<br>Application|
|Cryptogram|ans16|Conditional|Authentication<br>Cryptogram (AAC) for a<br>declined transaction, or<br>Transaction Certificate<br>(TC) for an approved<br>transaction.|
||||(Field value: translate 8<br>byte binary EMV value|
||||to 16 byte hex string.)|
|CryptogramInformationData|ans2|Conditional|Indicates the type of<br>cryptogram returned by<br>the ICC (ARQC, AAC or<br>TC) and the actions to<br>be performed by the<br>terminal.<br>(Field value: translate 1<br>byte binary EMV value<br>to 2 byte hex string.)|
||||Identifies the cardholder<br>verification methods<br>(CVMs) supported by|
|CvmList|ans..504|Optional|the application.<br>(Field value: translate<br>252 byte binary EMV<br>value to 504 byte hex<br>string.)|
||||Cardholder verification<br>method (CVM) results<br>indicating the results of|
|CvmResults|ans6|Optional|the last CVM performed.<br>(Field value: translate 3<br>byte binary EMV value<br>to 6 byte hex string.)|
||||A unique and<br>permanent serial|
|InterfaceDeviceSerialNumber|an8|Conditional|number assigned to the<br>interface device (IFD) by<br>the terminal<br>manufacturer|
|IssuerActionCode||Optional|Card parameters that<br>instruct the terminal<br>about actions to take<br>under various<br>conditions.|
|Default|ans10|Conditional|Specifies the issuer's<br>conditions that cause a<br>transaction to be<br>rejected if it might have<br>been approved online,<br>but the terminal is<br>unable to process the<br>transaction online.<br>(Field value: translate 5<br>byte binary EMV value<br>to 10 byte hex string.)|
||||Specifies the issuer's<br>conditions that cause|
||||the denial of a|
|Denial|ans10|Conditional|transaction without<br>attempt to go online.|
||||(Field value: translate 5<br>byte binary EMV value<br>to 10 byte hex string.)|
||||Specifies the issuer's<br>conditions that cause a<br>transaction to be|
|Online|ans10|Conditional|transmitted online.<br>(Field value: translate 5|
||||byte binary EMV value|
||||to 10 byte hex string.)|
Proprietary application data for transmission from the ICC to the issuer. May contain the following subfields: Scheme Discretionary Data, Issuer Discretionary Data, Derivation Key Index, IssuerApplicationData ans..64 Conditional Cryptogram Version Number, Card Verification Results, DAC. The layout of this field is specific to the issuer. (Field value: translate 32 byte binary EMV value to 64 byte hex string.) Indicates the result of the terminal script processing. IssuerScriptResults ans.. Optional (Field value: translate binary EMV value to hex string.) Version number assigned by the TerminalApplicationVersionNumber ans4 Optional payment system for the application.
||||Indicates the card data|
|TerminalCapabilities|ans6|Optional|input, CVM, and security<br>capabilities of the<br>terminal.<br>(Field value: translate 3<br>byte binary EMV value<br>to 6 byte hex string.)|
||||Indicates the country of|
|TerminalCountryCode|n3|Conditional|the terminal,<br>represented according<br>to ISO 3166.|
||||Indicates the<br>environment of the|
|TerminalType|n2|Optional|terminal, its<br>communications<br>capability, and its<br>operational control.|
||||Status of the different<br>functions as seen from|
|TerminalVerificationResult|ans10|Conditional|the terminal.<br>(Field value: translate 5<br>byte binary EMV value<br>to 10 byte hex string.)|
||||Defines the type of<br>transaction for which|
|TransactionCategoryCode|ans1|Optional|authorization is being<br>requested. Used in risk<br>management.|
||||Indicates the currency|
|TransactionCurrencyCode|n3|Conditional|code of the transaction<br>according to ISO 4217.|
|TransactionDate|n6|Conditional|The local date (in<br>YYMMDD format) on<br>which the transaction<br>was authorized.|
||||Counter maintained bythe|
|TransactionSequenceCounter|n..8|Optional|terminal and<br>incremented by one for<br>each transaction.|
||||Indicates the type of the<br>transaction, represented|
|TransactionType|n2|Conditional   i|by the first two digits of<br>the ISO 8583:1997<br>Processing Code.|
||||Value to provide<br>uniqueness to the<br>generation of the|
|UnpredictableNumber|ans8|Conditional|cryptogram.<br>(Field value: translate 4<br>byte binary EMV value<br>to 8 byte hex string.)|
||||Indicates the form<br>factor of the consumer<br>payment device and the<br>type of contactless|
|FormFactorIndicator|n8|Conditional|interface over which the<br>transaction was<br>conducted. This<br>information is made<br>available to the issuer<br>host.|
||||Identifies and|
|ApplicationPrimaryAccountNumber|n2|Conditional|differentiates cards with<br>the same PAN|
||||Identifies the name of|
|DedicatedFileName||Conditional|i|
||5–16||the DF|



## **ICC Data - IccResponse** 

The IccResponse tag may be present for request response message (0110 or 0210). 

|**Field**|**Format**|**Condition**|**Description**|
|---|---|---|---|
|ApplicationTransactionCounter|ans4|Conditional|Counter maintained by the<br>application in the ICC.<br>(Incrementing the ATC is<br>managed by the ICC).<br>(Field value: translate 2 byte<br>binary EMV value to 4 byte hex<br>string.)|
||||An issuer-supplied code|
|CardAuthenticationResultsCode|ans1|Optional|indicating card authentication<br>results.|
|IssuerAuthenticationData|ans32|Optional|Data sent to the ICC for online<br>issuer authentication. May<br>contain the following subfields:<br>Authorization Response<br>Cryptogram (ARPC),<br>Authorization Response Code.<br>The layout of this field is<br>specific to the issuer.<br>(Field value: translate binary<br>EMV value to hex string.)|
|IssuerScriptTemplate1|ans..|Optional|A command from the issuer for<br>transmission to the ICC. This<br>script is processed by the<br>terminal before sending the<br>second GENERATE AC<br>command to the ICC.<br>(Field value: translate 16 byte<br>binary EMV value to 32 byte<br>hex string.)|
||||A command from the issuer for<br>transmission to the ICC. This<br>script is processed by the|
|IssuerScriptTemplate2|ans..|Optional|terminal after sending the<br>second GENERATE AC<br>command to the ICC.|
||||(Field value: translate binary<br>EMV value to hex string.)|



#### **Field 127.26 - Original Node** 

Contains the name of the original source node for this transaction. 

This field is used for linking transactions across source nodes, to handle cases where (for example) the authorization request arrives from one source node, while the matching transaction request or reversal is sent from another. 

#### **Field 127.27 - Card Verification Result** 

Contains the result of a transaction involving card verification. 

- M CVV2 valid (match), CVV valid or not available 

- N CVV2 invalid (non-match), CVV valid or not available 

- P Unable to process CVV2, CVV valid or not available 

- U Issuer unregistered to process CVV2, CVV valid or not available 

Y CVV invalid 

## **Field 127.28 - American Express Card Identifier (CID)** 

The American Express Card Identifier (CID) is similar to Visa's CVV2 and MasterCard's CVC2. It is used for additional validation in card-not-present transactions. 

Note that if the NCS is configured to perform stand-in, it will not validate the American Express Card Identifier (CID) while performing stand-in. Hence if the NCS performs stand-in for a transaction where this field is present, it does not imply that the data in this field is valid and correct. 

The American Express Card Identifier (CID) is passed through the NCS unaltered and not saved in the database. 

#### **Field 127.29 - 3D Secure Data** 

3D-Secure Data is made up of the following two subfields (both with format b20): 

- The Transaction Identifier (XID) (positions 1 to 20). 

- The Cardholder Authentication Verification Value (CAVV) (positions 21 to 40). 

3D-Secure data is passed through the NCS unaltered and not saved in the database. The 

NCS is not allowed to perform stand-in authorization for 3D-Secure transactions. **Field** 

#### **127.30 - 3D Secure Result** 

Contains the Cardholder Authentication Verification Value (CAVV) results code. 

blank CAVV not validated (a space) 

|0|CAVV not validated due to erroneous data submitted|
|---|---|
|1|CAVV failed validation|
|2|CAVV passed validation|
|3|CAVV validation could not be performed. Issuer attempts incomplete.|
|4|CAVV validation could not be performed. Issuer system error.|



3D-Secure Result is passed through NCS unaltered and saved in the database. 

#### **Note:** 

- 1.) We strongly suggest that clients connecting to NCS perform period key exchange at a specific time interval. 

- 2.) Also, when there is a disconnection between and NCS and the client(s), we advise that the client initiate a sign-on and fresh key exchange to NCS in order to ensure message security. 

