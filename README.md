# fablab-server

fablab-server is a REST server based on [Dropwizard](http://www.dropwizard.io) which is currently used by the following apps:
- [fablab-android](https://github.com/FAU-Inf2/fablab-android)
- [fablab-iOS](https://github.com/FAU-Inf2/fablab-ios)
- [fablab-HTML](https://github.com/FAU-Inf2/fablab-html)

## Deployment

The following files have to be present in order to run the server:

- Apple Push Certificate
- Java Keystore with ssl cert inside

Copy them to `app-server/src/dist/`.

After that you have to copy `app-server/src/dist/{config.yml.example,minimumVersion.yml.example}` to `app-server/src/dist/{`config.yml,minimumVersion.yml}` and adapt them to your needs.

Finally, build and run the server with

```bash
sudo ./manage-docker.sh up
```

The container will listen on port 443 for application requests and port 8081 (SSL) for administrative requests. 

To find where these ports are bound to execute

```bash
sudo ./manage-docker.sh port
```

## Libraries
fablab-server uses the following libraries and software:
* [docker-library/mysql/5.6](https://github.com/docker-library/mysql/tree/1f430aeee538aec3b51554ca9fc66955231b3563/5.6)     License: [GNU GPL Version 2](https://github.com/docker-library/mysql/blob/1f430aeee538aec3b51554ca9fc66955231b3563/LICENSE)
* [jsonrpc2-client:1.15](http://software.dzhuvinov.com/json-rpc-2.0-client.html)        License: [Apache 2.0](http://software.dzhuvinov.com/files/jsonrpc2server/LICENSE.txt)
* [ical4j:1.0.5](https://github.com/ical4j/ical4j)    License: (https://github.com/ical4j/ical4j/blob/master/LICENSE)
* [jackson-dataformat-xml:2.5.1](https://github.com/FasterXML/jackson-dataformat-xml)   License: [Apache 2.0](https://github.com/FasterXML/jackson-dataformat-xml/wiki#licensing)
* [jsoup:1.8.2](http://jsoup.org)  License: [MIT License](http://jsoup.org/license)
* [cron4j:2.2.5](http://www.sauronsoftware.it/projects/cron4j/) License: [LGPL](http://www.sauronsoftware.it/projects/cron4j/)
* [pushy:0.4.3](https://github.com/relayrides/pushy) License: [MIT](https://github.com/relayrides/pushy)

## License
