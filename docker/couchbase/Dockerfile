FROM couchbase/server:6.0.0

COPY scripts/configure-node.sh /

COPY queries /queries

#HEALTHCHECK --interval=5s --timeout=3s CMD curl --fail http://localhost:8091/pools || exit 1

RUN ["chmod", "+x", "/configure-node.sh"]

ENTRYPOINT ["/configure-node.sh"]
