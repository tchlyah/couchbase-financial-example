# Financial transactions example with Couchbase

This example shows how to implement a simple data model for financial transactions, generate data using [fakeit](https://www.npmjs.com/package/fakeit), query data using a [Spring Boot](https://spring.io/projects/spring-boot) JAVA application and optimize [N1QL](https://www.couchbase.com/products/n1ql) queries using [GSI indexes](https://docs.couchbase.com/server/6.0/learn/services-and-indexes/indexes/global-secondary-indexes.html) and [prepared statements](https://docs.couchbase.com/server/6.0/n1ql/n1ql-language-reference/prepare.html).

## Start application

[docker-compose](https://docs.docker.com/compose/) is used to start everything with minimum of efforts.

First, you need to build the application, which generates a docker image using [jib](https://github.com/GoogleContainerTools/jib) : 

```bash
./mvnw clean install
```

Then, go to `docker` directory, and start everything :
```bash
docker-compose up --build
```

If you have your local instance of Couchbase or you don't want to use Docker, you can do the following : 

### Generate data

[Fakeit](https://www.npmjs.com/package/fakeit) is used to describe a simple financial transactions data model, and populate Couchbase with generated data.

To start generating data, make sure that you have [npm](https://www.npmjs.com) installed, and Couchbase started, then execute the following :

```bash
npm install
npm start
```

### Spring-boot application

The application is a Java [REST](https://fr.wikipedia.org/wiki/Representational_state_transfer) service implemented using [Spring Boot](https://spring.io/projects/spring-boot), with [Swagger-UI](https://swagger.io/tools/swagger-ui/) to test requests, and [Couchmove](couchmove) to automatically create necessary N1QL indexes and prepared statements for Couchbase.

The Couchbase repositories are implemented using stock [java SDK](https://docs.couchbase.com/java-sdk/2.7/start-using-sdk.html) instead of [Spring Data Couchbase](https://spring.io/projects/spring-data-couchbase) to demonstrate how it works.

To build and start the application : 

```bash
./mvnw
```

## Test the application

When everything is up, open your browser to [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

For example, go to [transaction-resource/getByBates](http://localhost:8080/swagger-ui.html#/transaction-resource/getByDatesUsingGET), click on **Try it out**, then enter the _fromDate_ and _toDate_ in [ISO-8601](https://www.iso.org/iso-8601-date-and-time-format.html) instant format (2018-11-01T00:00:00.00Z)

### Prepared queries

In addition to indexes, Couchmove also create some prepared queries that we can test using [cbc-n1qlback](http://docs.couchbase.com/sdk-api/couchbase-c-client-2.6.1/md_doc_cbc-n1qlback.html).

Some queries are also automatically copied to Couchbase Docker instance from `docker/couchbase/queries`

Go to `docker` directory, then execute the following to open an interactive bash shell

```bash
docker-compose exec couchbase bash
```

To compare prepared vs simple queries, execute the following :

```bash 
cbc-n1qlback -U couchbase://localhost/finance -P password -t 5 -f simple_queries.txt

cbc-n1qlback -U couchbase://localhost/finance -P password -t 5 -f prepared_queries.txt
```
