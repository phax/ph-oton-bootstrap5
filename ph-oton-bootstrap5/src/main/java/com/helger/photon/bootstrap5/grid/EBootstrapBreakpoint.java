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

import com.helger.base.lang.EnumHelper;

/**
 * Grid breakpoint name
 *
 * @author Philip Helger
 */
public enum EBootstrapBreakpoint
{
  // Order should be in ascending size
  XS ("", 0, 576),
  SM ("-sm", 576, 768),
  MD ("-md", 768, 992),
  LG ("-lg", 992, 1200),
  XL ("-xl", 1200, 1400),
  XXL ("-xxl", 1400, -1);

  private final String m_sCSSClassNamePart;
  private final int m_nMinWidthIncl;
  private final int m_nMaxWidthExcl;

  EBootstrapBreakpoint (@NonNull final String sCSSClassNamePart, final int nMinWidthIncl, final int nMaxWidthExcl)
  {
    m_sCSSClassNamePart = sCSSClassNamePart;
    m_nMinWidthIncl = nMinWidthIncl;
    m_nMaxWidthExcl = nMaxWidthExcl;
  }

  @NonNull
  public String getCSSClassNamePart ()
  {
    return m_sCSSClassNamePart;
  }

  public boolean isForWidth (final int nPixels)
  {
    return nPixels >= m_nMinWidthIncl && (nPixels < m_nMaxWidthExcl || m_nMaxWidthExcl < 0);
  }

  @Nullable
  public static EBootstrapBreakpoint getForWidth (final int nPixels)
  {
    if (nPixels < 0)
      return null;
    return EnumHelper.findFirst (EBootstrapBreakpoint.class, x -> x.isForWidth (nPixels));
  }
}
