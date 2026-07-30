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
package com.helger.photon.bootstrap5.tooltip;

import org.jspecify.annotations.NonNull;

import com.helger.annotation.Nonempty;

/**
 * Overflow constraint boundary of the tooltip, as understood by Popper v2's "preventOverflow"
 * modifier. The Popper v1 values (viewport, window, scrollParent) no longer exist.
 */
public enum EBootstrapTooltipBoundary
{
  CLIPPING_PARENTS ("clippingParents");

  private final String m_sValue;

  EBootstrapTooltipBoundary (@NonNull @Nonempty final String sValue)
  {
    m_sValue = sValue;
  }

  @NonNull
  @Nonempty
  public String getValue ()
  {
    return m_sValue;
  }
}
