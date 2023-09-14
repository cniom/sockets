# Sockets

## Purpose

This is a small project to demonstrate how to build a non-blocking socket server in Java.

This type of server can be used for local IPC for example. It has a small footprint, uses very little memory and is
generally fast. Alternatives exist but can be less elegant (e.g, MappedByteBuffer), are specific to the operating
system (e.g., named pipes) and most are not simple.

Getting this to production quality requires a bit more work.

The code is provided "as is" without license and without any guarantees.

## How to run

1. Clone repository
1. Open in IDEA or another editor
1. Start Server Main
1. Run the Client to send requests to the Server

## Author

Mo'in Creemers
https://www.linkedin.com/in/moincreemers/