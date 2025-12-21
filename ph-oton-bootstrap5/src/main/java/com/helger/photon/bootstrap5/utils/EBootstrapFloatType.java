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
import com.helger.photon.bootstrap5.grid.EBootstrapGridType;

/**
 * Floating. See https://getbootstrap.com/docs/4.1/utilities/float/
 *
 * @author Philip Helger
 */
public enum EBootstrapFloatType implements ICSSClassProvider
{
  START (CBootstrapCSS.FLOAT_START, EBootstrapGridType.XS),
  END (CBootstrapCSS.FLOAT_END, EBootstrapGridType.XS),
  NONE (CBootstrapCSS.FLOAT_NONE, EBootstrapGridType.XS),
  SM_START (CBootstrapCSS.FLOAT_SM_START, EBootstrapGridType.SM),
  SM_END (CBootstrapCSS.FLOAT_SM_END, EBootstrapGridType.SM),
  SM_NONE (CBootstrapCSS.FLOAT_SM_NONE, EBootstrapGridType.SM),
  MD_START (CBootstrapCSS.FLOAT_MD_START, EBootstrapGridType.MD),
  MD_END (CBootstrapCSS.FLOAT_MD_END, EBootstrapGridType.MD),
  MD_NONE (CBootstrapCSS.FLOAT_MD_NONE, EBootstrapGridType.MD),
  LG_START (CBootstrapCSS.FLOAT_LG_START, EBootstrapGridType.LG),
  LG_END (CBootstrapCSS.FLOAT_LG_END, EBootstrapGridType.LG),
  LG_NONE (CBootstrapCSS.FLOAT_LG_NONE, EBootstrapGridType.LG),
  XL_START (CBootstrapCSS.FLOAT_XL_START, EBootstrapGridType.XL),
  XL_END (CBootstrapCSS.FLOAT_XL_END, EBootstrapGridType.XL),
  XL_NONE (CBootstrapCSS.FLOAT_XL_NONE, EBootstrapGridType.XL),
  XXL_START (CBootstrapCSS.FLOAT_XXL_START, EBootstrapGridType.XXL),
  XXL_END (CBootstrapCSS.FLOAT_XXL_END, EBootstrapGridType.XXL),
  XXL_NONE (CBootstrapCSS.FLOAT_XXL_NONE, EBootstrapGridType.XXL);

  private final ICSSClassProvider m_aCSSClass;
  private final EBootstrapGridType m_eGridType;

  EBootstrapFloatType (@NonNull final ICSSClassProvider aCSSClass, @NonNull final EBootstrapGridType eGridType)
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
   * @return The grid type to be used. Never <code>null</code>.
   */
  @NonNull
  public EBootstrapGridType getGridType ()
  {
    return m_eGridType;
  }
}
