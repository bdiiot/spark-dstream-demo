#!/bin/bash

# chmod a+r /etc/security/keytabs/bigdata.keytab
# pcs /tmp/kafka_bigdata_jaas.conf

export SPARK_MAJOR_VERSION=2

spark-submit \
--master yarn \
--deploy-mode client \
--jars "spark-dstream-demo-1.0-SNAPSHOT-jar-with-dependencies.jar" \
--conf "spark.executor.extraJavaOptions=-Djava.security.auth.login.config=/tmp/kafka_bigdata_jaas.conf" \
--class com.bdiiot.spark.serializable.main.SparkMain \
spark-dstream-demo-1.0-SNAPSHOT.jar