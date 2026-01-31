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

import com.helger.annotation.concurrent.Immutable;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.html.hc.html.IHCElement;

@Immutable
public final class BootstrapGutterSpec
{
  public static final BootstrapGutterSpec NONE = new BootstrapGutterSpec (null, null, null, null, null, null);

  private final EBootstrapGutterXS m_eXS;
  private final EBootstrapGutterSM m_eSM;
  private final EBootstrapGutterMD m_eMD;
  private final EBootstrapGutterLG m_eLG;
  private final EBootstrapGutterXL m_eXL;
  private final EBootstrapGutterXXL m_eXXL;

  public BootstrapGutterSpec (@Nullable final EBootstrapGutterXS eXS,
                              @Nullable final EBootstrapGutterSM eSM,
                              @Nullable final EBootstrapGutterMD eMD,
                              @Nullable final EBootstrapGutterLG eLG,
                              @Nullable final EBootstrapGutterXL eXL,
                              @Nullable final EBootstrapGutterXXL eXXL)
  {
    m_eXS = eXS;
    m_eSM = eSM;
    m_eMD = eMD;
    m_eLG = eLG;
    m_eXL = eXL;
    m_eXXL = eXXL;
  }

  @Nullable
  public EBootstrapGutterXS getXS ()
  {
    return m_eXS;
  }

  @Nullable
  public EBootstrapGutterSM getSM ()
  {
    return m_eSM;
  }

  @Nullable
  public EBootstrapGutterMD getMD ()
  {
    return m_eMD;
  }

  @Nullable
  public EBootstrapGutterLG getLG ()
  {
    return m_eLG;
  }

  @Nullable
  public EBootstrapGutterXL getXL ()
  {
    return m_eXL;
  }

  @Nullable
  public EBootstrapGutterXXL getXXL ()
  {
    return m_eXXL;
  }

  @NonNull
  public <T extends IHCElement <T>> T applyTo (@NonNull final T aElement)
  {
    ValueEnforcer.notNull (aElement, "Element");

    int nLastPartCount = IBootstrapGutterElement.PARTS_NONE;
    if (m_eXS != null)
    {
      aElement.addClass (m_eXS);
      nLastPartCount = m_eXS.getParts ();
    }
    // Apply only if different from the previous part count
    if (m_eSM != null && m_eSM.getParts () != nLastPartCount)
    {
      aElement.addClass (m_eSM);
      nLastPartCount = m_eSM.getParts ();
    }
    if (m_eMD != null && m_eMD.getParts () != nLastPartCount)
    {
      aElement.addClass (m_eMD);
      nLastPartCount = m_eMD.getParts ();
    }
    if (m_eLG != null && m_eLG.getParts () != nLastPartCount)
    {
      aElement.addClass (m_eLG);
      nLastPartCount = m_eLG.getParts ();
    }
    if (m_eXL != null && m_eXL.getParts () != nLastPartCount)
    {
      aElement.addClass (m_eXL);
      nLastPartCount = m_eXL.getParts ();
    }
    if (m_eXXL != null && m_eXXL.getParts () != nLastPartCount)
    {
      aElement.addClass (m_eXXL);
      // nLastPartCount = m_eXXL.getParts ();
    }
    return aElement;
  }

  @NonNull
  public <T extends IHCElement <T>> T applyXTo (@NonNull final T aElement)
  {
    ValueEnforcer.notNull (aElement, "Element");

    int nLastPartCount = IBootstrapGutterElement.PARTS_NONE;
    if (m_eXS != null && m_eXS.getParts () > 0)
    {
      aElement.addClass (m_eXS.getCSSClassX ());
      nLastPartCount = m_eXS.getParts ();
    }
    // Apply only if different from the previous part count
    if (m_eSM != null && m_eSM.getParts () > 0 && m_eSM.getParts () != nLastPartCount)
    {
      aElement.addClass (m_eSM.getCSSClassX ());
      nLastPartCount = m_eSM.getParts ();
    }
    if (m_eMD != null && m_eMD.getParts () > 0 && m_eMD.getParts () != nLastPartCount)
    {
      aElement.addClass (m_eMD.getCSSClassX ());
      nLastPartCount = m_eMD.getParts ();
    }
    if (m_eLG != null && m_eLG.getParts () > 0 && m_eLG.getParts () != nLastPartCount)
    {
      aElement.addClass (m_eLG.getCSSClassX ());
      nLastPartCount = m_eLG.getParts ();
    }
    if (m_eXL != null && m_eXL.getParts () > 0 && m_eXL.getParts () != nLastPartCount)
    {
      aElement.addClass (m_eXL.getCSSClassX ());
      nLastPartCount = m_eXL.getParts ();
    }
    if (m_eXXL != null && m_eXXL.getParts () > 0 && m_eXXL.getParts () != nLastPartCount)
    {
      aElement.addClass (m_eXXL.getCSSClassX ());
      // nLastPartCount = m_eXXL.getParts ();
    }
    return aElement;
  }

  @NonNull
  public <T extends IHCElement <T>> T applyYTo (@NonNull final T aElement)
  {
    ValueEnforcer.notNull (aElement, "Element");

    int nLastPartCount = IBootstrapGutterElement.PARTS_NONE;
    if (m_eXS != null && m_eXS.getParts () > 0)
    {
      aElement.addClass (m_eXS.getCSSClassY ());
      nLastPartCount = m_eXS.getParts ();
    }
    // Apply only if different from the previous part count
    if (m_eSM != null && m_eSM.getParts () > 0 && m_eSM.getParts () != nLastPartCount)
    {
      aElement.addClass (m_eSM.getCSSClassY ());
      nLastPartCount = m_eSM.getParts ();
    }
    if (m_eMD != null && m_eMD.getParts () > 0 && m_eMD.getParts () != nLastPartCount)
    {
      aElement.addClass (m_eMD.getCSSClassY ());
      nLastPartCount = m_eMD.getParts ();
    }
    if (m_eLG != null && m_eLG.getParts () > 0 && m_eLG.getParts () != nLastPartCount)
    {
      aElement.addClass (m_eLG.getCSSClassY ());
      nLastPartCount = m_eLG.getParts ();
    }
    if (m_eXL != null && m_eXL.getParts () > 0 && m_eXL.getParts () != nLastPartCount)
    {
      aElement.addClass (m_eXL.getCSSClassY ());
      nLastPartCount = m_eXL.getParts ();
    }
    if (m_eXXL != null && m_eXXL.getParts () > 0 && m_eXXL.getParts () != nLastPartCount)
    {
      aElement.addClass (m_eXXL.getCSSClassY ());
      // nLastPartCount = m_eXXL.getParts ();
    }
    return aElement;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("xs", m_eXS)
                                       .append ("sm", m_eSM)
                                       .append ("md", m_eMD)
                                       .append ("lg", m_eLG)
                                       .append ("xl", m_eXL)
                                       .append ("xxl", m_eXXL)
                                       .getToString ();
  }

  @NonNull
  public static BootstrapGutterSpec create (final int nParts)
  {
    // The larger sizes inherit from the smaller sizes
    return create (nParts,
                   IBootstrapGutterElement.PARTS_NONE,
                   IBootstrapGutterElement.PARTS_NONE,
                   IBootstrapGutterElement.PARTS_NONE,
                   IBootstrapGutterElement.PARTS_NONE,
                   IBootstrapGutterElement.PARTS_NONE);
  }

  @NonNull
  public static BootstrapGutterSpec create (final int nPartsXS,
                                            final int nPartsSM,
                                            final int nPartsMD,
                                            final int nPartsLG,
                                            final int nPartsXL,
                                            final int nPartsXXL)
  {
    return new BootstrapGutterSpec (EBootstrapGutterXS.getFromParts (nPartsXS),
                                    EBootstrapGutterSM.getFromParts (nPartsSM),
                                    EBootstrapGutterMD.getFromParts (nPartsMD),
                                    EBootstrapGutterLG.getFromParts (nPartsLG),
                                    EBootstrapGutterXL.getFromParts (nPartsXL),
                                    EBootstrapGutterXXL.getFromParts (nPartsXXL));
  }
}
