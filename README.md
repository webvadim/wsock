# wsock

A simple test program, which emulates the work of banking service. 
The same approach can be used for the web based chats or other web interactions. 
Programm needs a modern browser for the work (IE10, for example), because of WebSockets usage.
Websockets were chosen as a most robust implementation of web communications. 
(I also considered "comet" for this project, but decided that it is not too good - "long polling"  do new requests for each event(transaction) and have delays,
when "streaming" stores all events in the browser's buffer up to closing of HTTP connection.)

UI of program consist of two web pages: presentation and input form. 
You can start a lot of presentation pages and produce messages using one or many input forms. All messages will be shown on each presentation form. 
Transaction processing is also almost an emulation. I just sums an incoming values by currency exchange pairs to show these sums on the graphic in the presentation form.

The business logic of the project uses JMS Topic in order to have horizontal scaling option(deploy on several servers) for the support of many users.
 Input messages are going to the JMS Topic, and then distributed to the presentation levels of each server.
 Distributed deployment is only an intention. Since this program is a test, I use a local emulation of database. In the case of real cluster deployment, data values, of course, should not be stored locally. 
 Web interface of program is made using JSF 2.0 and Chart.js javascrypt library.

The program is accessible on the http://websock.tenplanets.net 
(The endpoint for incoming messages from an external program is wss://websock.tenplanets.net/datainp,
with basic authentification header: test/test, and with accepting of unvalid SSL server sertificates)

Note: I had a big problem with deployment on Glassfish under Linux (the same version of Glassfish under Windows worked fine).
It looks like a bug with websockets in the Glassfish and in tyrus library. The first problem was resolved by upgrading of Glassfish up to 4.1.
The bug in the tyrus, which prevents normal deployment on the Linux is open on this day. Need to disable logging in the Glassfish for the project(virtual host) in order to avoid troubles.