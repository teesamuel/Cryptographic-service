## Decryption and Encryption service for  Teasypay

######Author: Samuel Olufemi

This is a simple encryption and decryption java  spring boot service. It has two endpoint that can be found on 
the api below 

http://BASE_URL:server.port/swagger-ui/index.html

This application is configured to work on JAVA 8

RUNNING_PORT can be found in the respective activation.properties file


### Cryptographic algorithms
API uses complex RSA+AES encryption to provide security

Requests/Responses format
All api endpoints accept JSON body (POST). This JSON requests and responses have special format:

ek: Base64 encoded Encrypted key
em: Base64 encoded Encrypted message
sig: Base64 encoded Signature
