#!/bin/sh

echo "The application will start in ${SLEEP}s..." && sleep ${SLEEP}

exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -cp /app/resources/:/app/classes/:/app/libs/* "com.couchbase.financial.example.FinancialExampleApplication"  "$@"
