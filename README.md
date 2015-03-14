# wsock

This program emulates the work of banking service, in order to spread currency exchange transactions to the interested users.
Programm need modern browsers for work (IE10), because of usage of WebSockets. Websockets were chosen as most robust implementation of web communications.
(I also considered "comet" for this project, but decided that it is not too good - "long polling"  do new requests for each event(transaction) and have delays,
when "streaming" stores all events in the browser's buffer up to closing of HTTP connection.)
This project also use JMS topic in order to give the possibility to horizontally scale(deploy on several servers) this application.
In the project I use emulation of database to store currency exchange transactions and processing results.
In case of distributed deployment it have to be changed to the real database.
Processing of transactions is also almost an emulation. I just sums incoming values by currency exchange pairs to show this information to users in real time.
UI of program consist of to web pages: presentation (with graph) and a message generator page.

The program is accessible on the http://websock.tenplanets.net 
(The endpoint for incoming messages from an external program is wss://websock.tenplanets.net/datainp,
with basic authentification header: test/test, and with accepting of unvalid SSL server sertificates)
Note:
I had a big problem with deployment on Glassfish under Linux (the same version of Glassfish under Windows worked fine).
It looks like a bug with websockets in the Glassfish and in tyrus library. The first problem was resolved by upgrading of Glassfish up to 4.1.
The bug in the tyrus, which prevents normal deployment on the Linux is open on this day. 
Need to disable logging in the Glassfish for the project(virtual host) in order to avoid troubles.