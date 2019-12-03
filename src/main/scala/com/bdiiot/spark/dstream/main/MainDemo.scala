package com.bdiiot.spark.dstream.main

import com.bdiiot.spark.dstream.utils.Constant._
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object MainDemo {
  def main(args: Array[String]): Unit = {
    if (SECURITY == "SASL_PLAINTEXT") {
      System.setProperty("java.security.krb5.conf", "/etc/krb5.conf")
      System.setProperty("java.security.auth.login.config", "/tmp/kafka_bigdata_jaas.conf")
    }

    val sparkConf = new SparkConf().setAppName(APP_NAME)
    val ssc = new StreamingContext(sparkConf, Seconds(2))

    val kafkaParams = Map[String, Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> BROKERS,
      ConsumerConfig.GROUP_ID_CONFIG -> GROUP_ID,
      ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> OFFSETS,
      ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> (false: java.lang.Boolean),
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      "security.protocol" -> SECURITY
    )

    val messages = KafkaUtils.createDirectStream[String, String](ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](Set(TOPICS), kafkaParams)
    )

    messages.foreachRDD { rdd =>
      val spark = SparkSession.builder.config(rdd.sparkContext.getConf).getOrCreate()
      import spark.implicits._

      println("key===================")
      rdd.map(_.key()).toDF().show()
      println("key===================")
      println("value===================")
      rdd.map(_.value()).toDF().show()
      println("value===================")
    }

    val lines = messages.map(_.value())
    val words = lines.flatMap(_.split(" "))
    val wordCounts = words.map(x => (x, 1L)).reduceByKey(_ + _)
    wordCounts.print()

    ssc.start()
    ssc.awaitTermination()
  }

}
