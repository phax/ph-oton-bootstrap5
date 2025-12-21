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
package com.helger.photon.bootstrap5.grid;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.html.css.ICSSClassProvider;
import com.helger.photon.bootstrap5.CBootstrapCSS;

/**
 * Bootstrap4 grid columns. Extra large (&ge;1200px)
 *
 * @author Philip Helger
 */
public enum EBootstrapGridXXL implements IBootstrapGridElement
{
  XXL_1 (1, CBootstrapCSS.COL_XXL_1, CBootstrapCSS.OFFSET_XXL_1),
  XXL_2 (2, CBootstrapCSS.COL_XXL_2, CBootstrapCSS.OFFSET_XXL_2),
  XXL_3 (3, CBootstrapCSS.COL_XXL_3, CBootstrapCSS.OFFSET_XXL_3),
  XXL_4 (4, CBootstrapCSS.COL_XXL_4, CBootstrapCSS.OFFSET_XXL_4),
  XXL_5 (5, CBootstrapCSS.COL_XXL_5, CBootstrapCSS.OFFSET_XXL_5),
  XXL_6 (6, CBootstrapCSS.COL_XXL_6, CBootstrapCSS.OFFSET_XXL_6),
  XXL_7 (7, CBootstrapCSS.COL_XXL_7, CBootstrapCSS.OFFSET_XXL_7),
  XXL_8 (8, CBootstrapCSS.COL_XXL_8, CBootstrapCSS.OFFSET_XXL_8),
  XXL_9 (9, CBootstrapCSS.COL_XXL_9, CBootstrapCSS.OFFSET_XXL_9),
  XXL_10 (10, CBootstrapCSS.COL_XXL_10, CBootstrapCSS.OFFSET_XXL_10),
  XXL_11 (11, CBootstrapCSS.COL_XXL_11, CBootstrapCSS.OFFSET_XXL_11),
  XXL_12 (12, CBootstrapCSS.COL_XXL_12, null),
  AUTO (PARTS_AUTO, CBootstrapCSS.COL_XXL_AUTO, null),
  EVENLY (PARTS_EVENLY, CBootstrapCSS.COL_XXL, null);

  private final int m_nParts;
  private final ICSSClassProvider m_aCSSClass;
  private final ICSSClassProvider m_aCSSClassOffset;

  EBootstrapGridXXL (final int nParts,
                     @Nullable final ICSSClassProvider aCSSClass,
                     @Nullable final ICSSClassProvider aCSSClassOffset)
  {
    m_nParts = nParts;
    m_aCSSClass = aCSSClass;
    m_aCSSClassOffset = aCSSClassOffset;
  }

  @NonNull
  public EBootstrapGridType getGridType ()
  {
    return EBootstrapGridType.XL;
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
  public ICSSClassProvider getCSSClassOffset ()
  {
    return m_aCSSClassOffset;
  }

  public boolean isMax ()
  {
    return this == XXL_12;
  }

  @Nullable
  public static EBootstrapGridXXL getFromParts (final int nParts)
  {
    return switch (nParts)
    {
      case 1 -> XXL_1;
      case 2 -> XXL_2;
      case 3 -> XXL_3;
      case 4 -> XXL_4;
      case 5 -> XXL_5;
      case 6 -> XXL_6;
      case 7 -> XXL_7;
      case 8 -> XXL_8;
      case 9 -> XXL_9;
      case 10 -> XXL_10;
      case 11 -> XXL_11;
      case 12 -> XXL_12;
      case PARTS_AUTO -> AUTO;
      case PARTS_EVENLY -> EVENLY;
      default -> null;
    };
  }
}
