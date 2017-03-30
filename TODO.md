
Notes:
- https://github.com/jitsi/libjitsi
- https://github.com/usnistgov/jsip
- http://vkslabs.com/sip-register-request-using-jain-sip/
- http://vkslabs.com/digest-authorization-in-sip-with-md5-challenge/



# Register

```
[Mar 30 13:35:40] 
[Mar 30 13:35:40] <--- SIP read from UDP:10.49.0.1:5064 --->
[Mar 30 13:35:40] jaK
[Mar 30 13:35:40] <------------->
[Mar 30 13:35:47] Really destroying SIP dialog '128340970' Method: REGISTER
[Mar 30 13:35:51] 
[Mar 30 13:35:51] <--- SIP read from UDP:10.49.0.1:5064 --->
[Mar 30 13:35:51] REGISTER sip:10.49.0.2 SIP/2.0
[Mar 30 13:35:51] Via: SIP/2.0/UDP 192.168.18.90:5064;rport;branch=z9hG4bK1756628507
[Mar 30 13:35:51] From: <sip:kk80ei@10.49.0.2>;tag=619781818
[Mar 30 13:35:51] To: <sip:kk80ei@10.49.0.2>
[Mar 30 13:35:51] Call-ID: 254718588
[Mar 30 13:35:51] CSeq: 1 REGISTER
[Mar 30 13:35:51] Contact: <sip:kk80ei@10.49.0.1:5064;line=00586725135b489>
[Mar 30 13:35:51] Max-Forwards: 70
[Mar 30 13:35:51] User-Agent: Linphone/3.6.1 (eXosip2/4.1.0)
[Mar 30 13:35:51] Expires: 3600
[Mar 30 13:35:51] Content-Length: 0
[Mar 30 13:35:51] 
[Mar 30 13:35:51] 
[Mar 30 13:35:51] <------------->
[Mar 30 13:35:51] --- (11 headers 0 lines) ---
[Mar 30 13:35:51] Sending to 10.49.0.1:5064 (no NAT)
[Mar 30 13:35:51] Sending to 10.49.0.1:5064 (no NAT)
[Mar 30 13:35:51] 
[Mar 30 13:35:51] <--- Transmitting (no NAT) to 10.49.0.1:5064 --->
[Mar 30 13:35:51] SIP/2.0 401 Unauthorized
[Mar 30 13:35:51] Via: SIP/2.0/UDP 192.168.18.90:5064;branch=z9hG4bK1756628507;received=10.49.0.1;rport=5064
[Mar 30 13:35:51] From: <sip:kk80ei@10.49.0.2>;tag=619781818
[Mar 30 13:35:51] To: <sip:kk80ei@10.49.0.2>;tag=as3f24d9d0
[Mar 30 13:35:51] Call-ID: 254718588
[Mar 30 13:35:51] CSeq: 1 REGISTER
[Mar 30 13:35:51] Server: XiVO PBX
[Mar 30 13:35:51] Allow: INVITE, ACK, CANCEL, OPTIONS, BYE, REFER, SUBSCRIBE, NOTIFY, INFO, PUBLISH, MESSAGE
[Mar 30 13:35:51] Supported: replaces, timer
[Mar 30 13:35:51] WWW-Authenticate: Digest algorithm=MD5, realm="xivo", nonce="360fd7e8"
[Mar 30 13:35:51] Content-Length: 0
[Mar 30 13:35:51] 
[Mar 30 13:35:51] 
[Mar 30 13:35:51] <------------>
[Mar 30 13:35:51] Scheduling destruction of SIP dialog '254718588' in 32000 ms (Method: REGISTER)
[Mar 30 13:35:51] 
[Mar 30 13:35:51] <--- SIP read from UDP:10.49.0.1:5064 --->
[Mar 30 13:35:51] REGISTER sip:10.49.0.2 SIP/2.0
[Mar 30 13:35:51] Via: SIP/2.0/UDP 192.168.18.90:5064;rport;branch=z9hG4bK2138608018
[Mar 30 13:35:51] From: <sip:kk80ei@10.49.0.2>;tag=619781818
[Mar 30 13:35:51] To: <sip:kk80ei@10.49.0.2>
[Mar 30 13:35:51] Call-ID: 254718588
[Mar 30 13:35:51] CSeq: 2 REGISTER
[Mar 30 13:35:51] Contact: <sip:kk80ei@10.49.0.1:5064;line=00586725135b489>
[Mar 30 13:35:51] Authorization: Digest username="kk80ei", realm="xivo", nonce="360fd7e8", uri="sip:10.49.0.2", response="5f80d6a4c02e256508e62bfbb06c6057", algorithm=MD5
[Mar 30 13:35:51] Max-Forwards: 70
[Mar 30 13:35:51] User-Agent: Linphone/3.6.1 (eXosip2/4.1.0)
[Mar 30 13:35:51] Expires: 3600
[Mar 30 13:35:51] Content-Length: 0
[Mar 30 13:35:51] 
[Mar 30 13:35:51] 
[Mar 30 13:35:51] <------------->
[Mar 30 13:35:51] --- (12 headers 0 lines) ---
[Mar 30 13:35:51] Sending to 10.49.0.1:5064 (no NAT)
[Mar 30 13:35:51] 
[Mar 30 13:35:51] <--- Transmitting (no NAT) to 10.49.0.1:5064 --->
[Mar 30 13:35:51] SIP/2.0 200 OK
[Mar 30 13:35:51] Via: SIP/2.0/UDP 192.168.18.90:5064;branch=z9hG4bK2138608018;received=10.49.0.1;rport=5064
[Mar 30 13:35:51] From: <sip:kk80ei@10.49.0.2>;tag=619781818
[Mar 30 13:35:51] To: <sip:kk80ei@10.49.0.2>;tag=as3f24d9d0
[Mar 30 13:35:51] Call-ID: 254718588
[Mar 30 13:35:51] CSeq: 2 REGISTER
[Mar 30 13:35:51] Server: XiVO PBX
[Mar 30 13:35:51] Allow: INVITE, ACK, CANCEL, OPTIONS, BYE, REFER, SUBSCRIBE, NOTIFY, INFO, PUBLISH, MESSAGE
[Mar 30 13:35:51] Supported: replaces, timer
[Mar 30 13:35:51] Expires: 3600
[Mar 30 13:35:51] Contact: <sip:kk80ei@10.49.0.1:5064;line=00586725135b489>;expires=3600
[Mar 30 13:35:51] Date: Thu, 30 Mar 2017 11:35:51 GMT
[Mar 30 13:35:51] Content-Length: 0
[Mar 30 13:35:51] 
[Mar 30 13:35:51] 
[Mar 30 13:35:51] <------------>
[Mar 30 13:35:51] Scheduling destruction of SIP dialog '254718588' in 32000 ms (Method: REGISTER)
[Mar 30 13:36:06] 
[Mar 30 13:36:06] <--- SIP read from UDP:10.49.0.1:5064 --->
[Mar 30 13:36:06] jaK
[Mar 30 13:36:06] <------------->
```
