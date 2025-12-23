/*
 * Copyright (C) 2018-2025 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.photon.bootstrap5.utils;

import org.jspecify.annotations.NonNull;

import com.helger.annotation.Nonempty;
import com.helger.html.css.ICSSClassProvider;
import com.helger.photon.bootstrap5.CBootstrapCSS;
import com.helger.photon.bootstrap5.grid.EBootstrapBreakpoint;

/**
 * Text alignment. See https://getbootstrap.com/docs/4.1/utilities/text/
 *
 * @author Philip Helger
 */
public enum EBootstrapTextAlignType implements ICSSClassProvider
{
  START (CBootstrapCSS.TEXT_START, EBootstrapBreakpoint.XS),
  CENTER (CBootstrapCSS.TEXT_CENTER, EBootstrapBreakpoint.XS),
  END (CBootstrapCSS.TEXT_END, EBootstrapBreakpoint.XS),
  SM_START (CBootstrapCSS.TEXT_SM_START, EBootstrapBreakpoint.SM),
  SM_CENTER (CBootstrapCSS.TEXT_SM_CENTER, EBootstrapBreakpoint.SM),
  SM_END (CBootstrapCSS.TEXT_SM_END, EBootstrapBreakpoint.SM),
  MD_START (CBootstrapCSS.TEXT_MD_START, EBootstrapBreakpoint.MD),
  MD_CENTER (CBootstrapCSS.TEXT_MD_CENTER, EBootstrapBreakpoint.MD),
  MD_END (CBootstrapCSS.TEXT_MD_END, EBootstrapBreakpoint.MD),
  LG_START (CBootstrapCSS.TEXT_LG_START, EBootstrapBreakpoint.LG),
  LG_CENTER (CBootstrapCSS.TEXT_LG_CENTER, EBootstrapBreakpoint.LG),
  LG_END (CBootstrapCSS.TEXT_LG_END, EBootstrapBreakpoint.LG),
  XL_START (CBootstrapCSS.TEXT_XL_START, EBootstrapBreakpoint.XL),
  XL_CENTER (CBootstrapCSS.TEXT_XL_CENTER, EBootstrapBreakpoint.XL),
  XL_END (CBootstrapCSS.TEXT_XL_END, EBootstrapBreakpoint.XL),
  XXL_START (CBootstrapCSS.TEXT_XXL_START, EBootstrapBreakpoint.XXL),
  XXL_CENTER (CBootstrapCSS.TEXT_XXL_CENTER, EBootstrapBreakpoint.XXL),
  XXL_END (CBootstrapCSS.TEXT_XXL_END, EBootstrapBreakpoint.XXL);

  private final ICSSClassProvider m_aCSSClass;
  private final EBootstrapBreakpoint m_eGridType;

  EBootstrapTextAlignType (@NonNull final ICSSClassProvider aCSSClass, @NonNull final EBootstrapBreakpoint eGridType)
  {
    m_aCSSClass = aCSSClass;
    m_eGridType = eGridType;
  }

  @NonNull
  @Nonempty
  public String getCSSClass ()
  {
    return m_aCSSClass.getCSSClass ();
  }

  /**
   * @return The grid type on which this text alignment is applied.
   */
  @NonNull
  public EBootstrapBreakpoint getGridType ()
  {
    return m_eGridType;
  }
}
