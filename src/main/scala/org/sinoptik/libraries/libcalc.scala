package org.sinoptik.libraries

import com.sun.jna.Library

trait libcalc extends Library
{
  def add(num1: Int, num2: Int):Int
}
