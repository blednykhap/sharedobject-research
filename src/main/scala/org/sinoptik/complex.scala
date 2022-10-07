package org.sinoptik
import java.nio.charset.StandardCharsets

import com.sun.jna.Native
import com.sun.jna.ptr.{DoubleByReference, PointerByReference}
import org.apache.spark.sql.SparkSession

import scala.util.Try
import org.sinoptik.libraries.MassAndVolumeOfPetroleumProducts

import scala.io.Codec.UTF8

object complex {

  def main(args: Array[String]): Unit = {

    Try {

      val spark = SparkSession
        .builder()
        .appName("MASS Complex")
        .master("local[*]")
        .getOrCreate()

      val testData = Seq((1, 0, 23.2), (2, 1, 12.56), (3, 0, 9.21), (3, 1, 13.8))

      import spark.implicits._

      val df = testData.toDF("Num1", "Num2", "Num3")

      val df2 = df.rdd.mapPartitions(partition => {

        val libc = Native.load("MassAndVolumeOfPetroleumProducts",
          classOf[MassAndVolumeOfPetroleumProducts])

        val newPartition = partition.map(record => {

          val num1 = record.getInt(record.fieldIndex("Num1"))
          val num2 = record.getInt(record.fieldIndex("Num2"))
          val num3 = record.getDouble(record.fieldIndex("Num3"))

          val roundd = libc._Z13rt_roundd_snfd(num3)

          val delta_Ro_max: DoubleByReference = new DoubleByReference()
          val delta_Ro_min: DoubleByReference = new DoubleByReference()
          libc._Z25limit_abs_error_meas_densiiPdS_(num1, num2, delta_Ro_max, delta_Ro_min)

          val pointerByReference = new PointerByReference()
          libc._ZN10Ident_Info4InfoB5cxx11Ev(pointerByReference)
          val info = pointerByReference.getValue.getString(0, StandardCharsets.UTF_8.name()).mkString(" ")

          (num1, num2, num3, roundd, delta_Ro_max.getValue(), delta_Ro_min.getValue(), info)

        }).toList

        newPartition.iterator
      })

      val newDf = df2.toDF("Num1", "Num2", "Num3", "Round", "delta_Ro_max", "delta_Ro_min", "Info")

      newDf.show()

      newDf.write.csv("/tmp/tst02")

    }
  }
}
