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
package com.helger.photon.bootstrap5.badge;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.html.css.ICSSClassProvider;
import com.helger.photon.bootstrap5.CBootstrapCSS;

/**
 * Type of badge
 *
 * @author Philip Helger
 */
public enum EBootstrapBadgeType implements ICSSClassProvider
{
  PRIMARY (CBootstrapCSS.TEXT_BG_PRIMARY),
  SECONDARY (CBootstrapCSS.TEXT_BG_SECONDARY),
  SUCCESS (CBootstrapCSS.TEXT_BG_SUCCESS),
  DANGER (CBootstrapCSS.TEXT_BG_DANGER),
  WARNING (CBootstrapCSS.TEXT_BG_WARNING),
  INFO (CBootstrapCSS.TEXT_BG_INFO),
  LIGHT (CBootstrapCSS.TEXT_BG_LIGHT),
  DARK (CBootstrapCSS.TEXT_BG_DARK);

  private final ICSSClassProvider m_aCSSClass;

  EBootstrapBadgeType (@NonNull final ICSSClassProvider aCSSClass)
  {
    m_aCSSClass = aCSSClass;
  }

  @Nullable
  public String getCSSClass ()
  {
    return m_aCSSClass.getCSSClass ();
  }
}
