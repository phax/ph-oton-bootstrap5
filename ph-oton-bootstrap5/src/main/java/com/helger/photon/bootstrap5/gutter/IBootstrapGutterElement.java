package com.helger.photon.bootstrap5.gutter;

import org.jspecify.annotations.NonNull;

import com.helger.annotation.Nonnegative;
import com.helger.html.css.ICSSClassProvider;
import com.helger.photon.bootstrap5.grid.EBootstrapBreakpoint;

public interface IBootstrapGutterElement extends ICSSClassProvider
{
  int PARTS_NONE = -1;

  @NonNull
  EBootstrapBreakpoint getGridType ();

  @Nonnegative
  int getParts ();

  @NonNull
  ICSSClassProvider getCSSClassX ();

  @NonNull
  ICSSClassProvider getCSSClassY ();
}
