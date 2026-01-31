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
 * Embed aspect ratio. See https://getbootstrap.com/docs/4.1/utilities/embed/
 *
 * @author Philip Helger
 */
public enum EBootstrapRatio implements ICSSClassProvider
{
  AR_21_9 (CBootstrapCSS.RATIO_21X9),
  AR_16_9 (CBootstrapCSS.RATIO_16X9),
  AR_4_3 (CBootstrapCSS.RATIO_4X3),
  AR_1_1 (CBootstrapCSS.RATIO_1X1);

  private final ICSSClassProvider m_aCSSClass;

  EBootstrapRatio (@NonNull final ICSSClassProvider aCSSClass)
  {
    m_aCSSClass = aCSSClass;
  }

  @NonNull
  public String getCSSClass ()
  {
    return m_aCSSClass.getCSSClass ();
  }
}
