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
package com.helger.photon.bootstrap5.gutter;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonempty;
import com.helger.annotation.Nonnegative;
import com.helger.html.css.ICSSClassProvider;
import com.helger.photon.bootstrap5.CBootstrapCSS;
import com.helger.photon.bootstrap5.grid.EBootstrapBreakpoint;

/**
 * Bootstrap5 grid gutter XS
 *
 * @author Philip Helger
 */
public enum EBootstrapGutterXS implements IBootstrapGutterElement
{
  G_0 (0, CBootstrapCSS.G_0, CBootstrapCSS.GX_0, CBootstrapCSS.GY_0),
  G_1 (1, CBootstrapCSS.G_1, CBootstrapCSS.GX_1, CBootstrapCSS.GY_1),
  G_2 (2, CBootstrapCSS.G_2, CBootstrapCSS.GX_2, CBootstrapCSS.GY_2),
  G_3 (3, CBootstrapCSS.G_3, CBootstrapCSS.GX_3, CBootstrapCSS.GY_3),
  G_4 (4, CBootstrapCSS.G_4, CBootstrapCSS.GX_4, CBootstrapCSS.GY_4),
  G_5 (5, CBootstrapCSS.G_5, CBootstrapCSS.GX_5, CBootstrapCSS.GY_5);

  private final int m_nParts;
  private final ICSSClassProvider m_aCSSClass;
  private final ICSSClassProvider m_aCSSClassX;
  private final ICSSClassProvider m_aCSSClassY;

  EBootstrapGutterXS (final int nParts,
                      @NonNull final ICSSClassProvider aCSSClass,
                      @NonNull final ICSSClassProvider aCSSClassX,
                      @NonNull final ICSSClassProvider aCSSClassY)
  {
    m_nParts = nParts;
    m_aCSSClass = aCSSClass;
    m_aCSSClassX = aCSSClassX;
    m_aCSSClassY = aCSSClassY;
  }

  @NonNull
  public EBootstrapBreakpoint getGridType ()
  {
    return EBootstrapBreakpoint.XS;
  }

  @Nonnegative
  public int getParts ()
  {
    return m_nParts;
  }

  @NonNull
  @Nonempty
  public String getCSSClass ()
  {
    return m_aCSSClass.getCSSClass ();
  }

  @NonNull
  public ICSSClassProvider getCSSClassX ()
  {
    return m_aCSSClassX;
  }

  @NonNull
  public ICSSClassProvider getCSSClassY ()
  {
    return m_aCSSClassY;
  }

  @Nullable
  public static EBootstrapGutterXS getFromParts (final int nParts)
  {
    return switch (nParts)
    {
      case 0 -> G_0;
      case 1 -> G_1;
      case 2 -> G_2;
      case 3 -> G_3;
      case 4 -> G_4;
      case 5 -> G_5;
      default -> null;
    };
  }
}
