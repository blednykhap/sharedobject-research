package org.sinoptik

import com.sun.jna.Native
import com.sun.jna.NativeLibrary
import org.apache.spark.sql.SparkSession
import org.sinoptik.libraries.libcalc

import scala.util.Try

object simple {

  def main(args: Array[String]): Unit = {

    NativeLibrary.addSearchPath("libcalc","/tmp/so")

    Try {

      val spark = SparkSession
        .builder()
        .appName("SO Simple")
        .master("local[*]")
        .getOrCreate()

      val testData = Seq((1, 11), (2, 12), (3, 13))

      import spark.implicits._

      val df = testData.toDF("Num1", "Num2")

      val df2 = df.rdd.mapPartitions(partition => {

        val libc = Native.load("calc", classOf[libcalc])

        val newPartition = partition.map(record => {

          val num1 = record.getInt(record.fieldIndex("Num1"))
          val num2 = record.getInt(record.fieldIndex("Num2"))

          val sum = libc.add(num1, num2)
          (num1, num2, sum)

        }).toList

        newPartition.iterator
      })

      val newDf = df2.toDF("Num1", "Num2", "Sum")

      newDf.show()

    }
  }
}
