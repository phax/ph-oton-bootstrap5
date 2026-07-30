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
package com.helger.photon.bootstrap5.offcanvas;

import org.jspecify.annotations.NonNull;

import com.helger.annotation.Nonempty;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.html.css.ICSSClassProvider;
import com.helger.photon.bootstrap5.CBootstrapCSS;

/**
 * Placement of the Offcanvas element.
 *
 * @author Philip Helger
 */
public enum EBootstrapOffcanvasPlacement implements ICSSClassProvider
{
  START (CBootstrapCSS.OFFCANVAS_START),
  END (CBootstrapCSS.OFFCANVAS_END),
  TOP (CBootstrapCSS.OFFCANVAS_TOP),
  BOTTOM (CBootstrapCSS.OFFCANVAS_BOTTOM);

  public static final EBootstrapOffcanvasPlacement DEFAULT = START;

  private final ICSSClassProvider m_aCSSClass;

  EBootstrapOffcanvasPlacement (@NonNull final ICSSClassProvider aCSSClass)
  {
    ValueEnforcer.notNull (aCSSClass, "CSSClass");
    m_aCSSClass = aCSSClass;
  }

  @NonNull
  @Nonempty
  public String getCSSClass ()
  {
    return m_aCSSClass.getCSSClass ();
  }
}
