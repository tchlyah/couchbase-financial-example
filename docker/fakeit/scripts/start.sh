#!/bin/bash

echo "waiting for bucket ${BUCKET}@${HOST} to be ready"
while [ "$(curl -u ${BUCKET}:${PASSWORD} -Isw '%{http_code}' -o /dev/null http://${HOST}:8091/pools/default/buckets/${BUCKET})" != 200 ]
do
    sleep 10
done

/run couchbase --server ${HOST} --bucket ${BUCKET} --password ${PASSWORD} --timeout ${TIMEOUT} -c ${COUNT} -v /data/*