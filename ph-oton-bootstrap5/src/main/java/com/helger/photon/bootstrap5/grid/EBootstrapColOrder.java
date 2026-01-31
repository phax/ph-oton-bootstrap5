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
package com.helger.photon.bootstrap5.grid;

import org.jspecify.annotations.Nullable;

import com.helger.html.css.ICSSClassProvider;
import com.helger.photon.bootstrap5.CBootstrapCSS;

/**
 * Bootstrap5 grid order.
 *
 * @author Philip Helger
 */
public enum EBootstrapColOrder implements ICSSClassProvider
{
  ORDER_FIRST (-1, CBootstrapCSS.ORDER_FIRST),
  ORDER_0 (0, CBootstrapCSS.ORDER_0),
  ORDER_1 (1, CBootstrapCSS.ORDER_1),
  ORDER_2 (2, CBootstrapCSS.ORDER_2),
  ORDER_3 (3, CBootstrapCSS.ORDER_3),
  ORDER_4 (4, CBootstrapCSS.ORDER_4),
  ORDER_5 (5, CBootstrapCSS.ORDER_5),
  ORDER_LAST (6, CBootstrapCSS.ORDER_LAST);

  private final int m_nParts;
  private final ICSSClassProvider m_aCSSClass;

  EBootstrapColOrder (final int nParts, @Nullable final ICSSClassProvider aCSSClass)
  {
    m_nParts = nParts;
    m_aCSSClass = aCSSClass;
  }

  public int getParts ()
  {
    return m_nParts;
  }

  @Nullable
  public String getCSSClass ()
  {
    return m_aCSSClass == null ? null : m_aCSSClass.getCSSClass ();
  }

  @Nullable
  public static EBootstrapColOrder getFromParts (final int nParts)
  {
    return switch (nParts)
    {
      case -1 -> ORDER_FIRST;
      case 0 -> ORDER_0;
      case 1 -> ORDER_1;
      case 2 -> ORDER_2;
      case 3 -> ORDER_3;
      case 4 -> ORDER_4;
      case 5 -> ORDER_5;
      case 6 -> ORDER_LAST;
      default -> null;
    };
  }
}
