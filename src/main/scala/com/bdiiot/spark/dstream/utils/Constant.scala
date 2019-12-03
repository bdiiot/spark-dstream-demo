package com.bdiiot.spark.dstream.utils

object Constant {
  final val APP_NAME = "spark_serializable_demo"

  final val BROKERS = "h11.bdiiot.com:6667,h12.bdiiot.com:6667,h13.bdiiot.com:6667"
  final val TOPICS = "test_phoenix"
  final val GROUP_ID = "test_phoenix"
  // kafka.security [PLAINTEXT, SASL_PLAINTEXT]
  final val SECURITY = "SASL_PLAINTEXT"
  // auto.offset.reset [latest, earliest, none]
  final val OFFSETS = "earliest"

  final val JDBC_URL = "jdbc:phoenix:h11.bdiiot.com,h12.bdiiot.com,h13.bdiiot.com:2181:/hbase-secure:hbase-bdiiot@BDIIOT.COM:/etc/security/keytabs/hbase.headless.keytab"
  final val PHOENIX_DRIVER = "org.apache.phoenix.jdbc.PhoenixDriver"
}
