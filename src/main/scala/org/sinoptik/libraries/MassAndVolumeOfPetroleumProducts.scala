package org.sinoptik.libraries

import com.sun.jna.Library
import com.sun.jna.ptr.{DoubleByReference, PointerByReference}

trait MassAndVolumeOfPetroleumProducts extends Library {

  // double rt_roundd_snf(double u);
  def _Z13rt_roundd_snfd(u: Double): Double

  // void limit_abs_error_meas_dens(int Rho_type, int Fuel_sub_type, double *delta_Ro_max, double *delta_Ro_min);
  def _Z25limit_abs_error_meas_densiiPdS_(Rho_type: Int, Fuel_sub_type: Int,
                                          delta_Ro_max: DoubleByReference, delta_Ro_min: DoubleByReference)

  //class Ident_Info -> std::string Info();
  def _ZN10Ident_Info4InfoB5cxx11Ev(Info_str: PointerByReference)

}



