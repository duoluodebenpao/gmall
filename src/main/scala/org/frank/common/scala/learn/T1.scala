package org.frank.common.scala.learn

object T1 {
  def main(args: Array[String]): Unit = {

    val a = "nihao"

    val str = s"this is ${a}"
    val why =
      s"""
         |select  * from
       """.stripMargin
    print(str)
  }

}
