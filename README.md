# wsock

This program emulates the work of banking service, in order to spread currency exchange transactions to the interested users.
Programm need modern browsers for work (IE10), because I use WebSockets. Websockets were chosen as most robust implementation of web communications.
(I also considered "comet" for this project, but decided that it is not too good - "long polling"  do new requests for each event(transaction) and have delays,
when "streaming" stores all events in the browser's buffer up to closing of HTTP connection.)
This project also use JMS topic in order to give the possibility to horizontally scale(deploy on several servers) this application.
In the project I use emulation of database to store currency exchange transactions and processing results.
In case of distributed deployment it have to be changed to the real database.
Processing of transactions is also almost an emulation. I just sums incoming values by currency exchange pairs to show this information to users in real time.
UI of program consist of to web pages: presentation (with graph) and a message generator page.

The program should be accessible on the http://websock.tenplanets.net but currently I have the problem with deployment on Glassfish under Linux (the same version of Glassfish under Windows works fine).
It looks like a bug with websockets in the Glassfish. I hope to fix it soon. For the time being you can download this program and try it in your java container.
(The endpoint for incoming messages from an external program should be wss://websock.tenplanets.net/datainp ,with bacic authentification header: test/test, and with accepting of unvalid SSL server sertificates)

