/*
 * Copyright (C) 2025-2026 Philip Helger (www.helger.com)
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

import com.helger.html.css.ICSSClassProvider;
import com.helger.photon.bootstrap5.CBootstrapCSS;

/**
 * Border type. See https://getbootstrap.com/docs/4.1/utilities/borders/
 *
 * @author Philip Helger
 */
public enum EBootstrapBorderType implements ICSSClassProvider
{
  /** All 4 sides */
  BORDER (CBootstrapCSS.BORDER),
  /** Only top border */
  TOP_BORDER (CBootstrapCSS.BORDER_TOP),
  /** Only right border */
  END_BORDER (CBootstrapCSS.BORDER_END),
  /** Only bottom border */
  BOTTOM_BORDER (CBootstrapCSS.BORDER_BOTTOM),
  /** Only left border */
  START_BORDER (CBootstrapCSS.BORDER_START),
  /** No border */
  NONE (CBootstrapCSS.BORDER_0),
  /** No top border */
  NO_TOP_BORDER (CBootstrapCSS.BORDER_TOP_0),
  /** No right border */
  NO_END_BORDER (CBootstrapCSS.BORDER_END_0),
  /** No bottom border */
  NO_BOTTOM_BORDER (CBootstrapCSS.BORDER_BOTTOM_0),
  /** No left border */
  NO_START_BORDER (CBootstrapCSS.BORDER_START_0);

  private final ICSSClassProvider m_aCSSClass;

  EBootstrapBorderType (@NonNull final ICSSClassProvider aCSSClass)
  {
    m_aCSSClass = aCSSClass;
  }

  @NonNull
  public String getCSSClass ()
  {
    return m_aCSSClass.getCSSClass ();
  }
}
