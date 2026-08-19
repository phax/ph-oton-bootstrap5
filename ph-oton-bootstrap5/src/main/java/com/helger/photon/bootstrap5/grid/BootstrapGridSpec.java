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

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.concurrent.Immutable;
import com.helger.base.builder.IBuilder;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.html.hc.html.IHCElement;

@Immutable
public final class BootstrapGridSpec
{
  public static final BootstrapGridSpec NONE = builder ().build ();
  public static final BootstrapGridSpec EVENLY = builder ().xs (EBootstrapGridXS.EVENLY).build ();

  private final EBootstrapGridXS m_eXS;
  private final EBootstrapGridSM m_eSM;
  private final EBootstrapGridMD m_eMD;
  private final EBootstrapGridLG m_eLG;
  private final EBootstrapGridXL m_eXL;
  private final EBootstrapGridXXL m_eXXL;

  public BootstrapGridSpec (@Nullable final EBootstrapGridXS eXS,
                            @Nullable final EBootstrapGridSM eSM,
                            @Nullable final EBootstrapGridMD eMD,
                            @Nullable final EBootstrapGridLG eLG,
                            @Nullable final EBootstrapGridXL eXL,
                            @Nullable final EBootstrapGridXXL eXXL)
  {
    m_eXS = eXS;
    m_eSM = eSM;
    m_eMD = eMD;
    m_eLG = eLG;
    m_eXL = eXL;
    m_eXXL = eXXL;
  }

  @Nullable
  public EBootstrapGridXS getXS ()
  {
    return m_eXS;
  }

  @Nullable
  public EBootstrapGridSM getSM ()
  {
    return m_eSM;
  }

  @Nullable
  public EBootstrapGridMD getMD ()
  {
    return m_eMD;
  }

  @Nullable
  public EBootstrapGridLG getLG ()
  {
    return m_eLG;
  }

  @Nullable
  public EBootstrapGridXL getXL ()
  {
    return m_eXL;
  }

  @Nullable
  public EBootstrapGridXXL getXXL ()
  {
    return m_eXXL;
  }

  @NonNull
  public <T extends IHCElement <T>> T applyTo (@NonNull final T aElement)
  {
    ValueEnforcer.notNull (aElement, "Element");

    int nLastPartCount = IBootstrapGridElement.PARTS_NONE;
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
  public <T extends IHCElement <T>> T applyOffsetTo (@NonNull final T aElement)
  {
    ValueEnforcer.notNull (aElement, "Element");

    int nLastPartCount = IBootstrapGridElement.PARTS_NONE;
    if (m_eXS != null && m_eXS.getParts () > 0)
    {
      aElement.addClass (m_eXS.getCSSClassOffset ());
      nLastPartCount = m_eXS.getParts ();
    }
    // Apply only if different from the previous part count
    if (m_eSM != null && m_eSM.getParts () > 0 && m_eSM.getParts () != nLastPartCount)
    {
      aElement.addClass (m_eSM.getCSSClassOffset ());
      nLastPartCount = m_eSM.getParts ();
    }
    if (m_eMD != null && m_eMD.getParts () > 0 && m_eMD.getParts () != nLastPartCount)
    {
      aElement.addClass (m_eMD.getCSSClassOffset ());
      nLastPartCount = m_eMD.getParts ();
    }
    if (m_eLG != null && m_eLG.getParts () > 0 && m_eLG.getParts () != nLastPartCount)
    {
      aElement.addClass (m_eLG.getCSSClassOffset ());
      nLastPartCount = m_eLG.getParts ();
    }
    if (m_eXL != null && m_eXL.getParts () > 0 && m_eXL.getParts () != nLastPartCount)
    {
      aElement.addClass (m_eXL.getCSSClassOffset ());
      nLastPartCount = m_eXL.getParts ();
    }
    if (m_eXXL != null && m_eXXL.getParts () > 0 && m_eXXL.getParts () != nLastPartCount)
    {
      aElement.addClass (m_eXXL.getCSSClassOffset ());
      // nLastPartCount = m_eXXL.getParts ();
    }
    return aElement;
  }

  /**
   * Create the "inverse" grid specification of <code>this</code>, so that the two grid
   * specifications complement each other to the maximum number of parts per breakpoint. So the
   * inverse of <code>col-4</code> is <code>col-8</code> and vice versa. A breakpoint that is not
   * set inherits the value of the next smaller breakpoint - if no smaller breakpoint is set at all,
   * it counts as "0 parts" and therefore the inverse is the maximum part count. The special values
   * "auto" and "evenly" as well as the maximum part count are kept as-is, because they have no
   * numeric counterpart.
   *
   * @return A new {@link BootstrapGridSpec} object and never <code>null</code>.
   * @since 0.9.1
   */
  @NonNull
  public BootstrapGridSpec getInverse ()
  {
    final IBootstrapGridElement [] aSrc = new IBootstrapGridElement [] { m_eXS, m_eSM, m_eMD, m_eLG, m_eXL, m_eXXL };
    final int [] aInverseParts = new int [aSrc.length];

    // Nothing set means "0 parts" so far
    int nParts = 0;
    int nLastInverseParts = IBootstrapGridElement.PARTS_NONE;
    for (int i = 0; i < aSrc.length; ++i)
    {
      // Unset breakpoints inherit the value of the next smaller breakpoint
      if (aSrc[i] != null)
        nParts = aSrc[i].getParts ();

      final int nInverseParts = IBootstrapGridElement.getMatchingOpposite (nParts);
      // Set only if different from the previous breakpoint, so that the inheritance is retained
      aInverseParts[i] = nInverseParts == nLastInverseParts ? IBootstrapGridElement.PARTS_NONE : nInverseParts;
      nLastInverseParts = nInverseParts;
    }

    return builder ().xs (aInverseParts[0])
                     .sm (aInverseParts[1])
                     .md (aInverseParts[2])
                     .lg (aInverseParts[3])
                     .xl (aInverseParts[4])
                     .xxl (aInverseParts[5])
                     .build ();
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

  /**
   * @return A new builder with all breakpoints unset. Never <code>null</code>.
   * @since 0.9.1
   */
  @NonNull
  public static Builder builder ()
  {
    return new Builder ();
  }

  /**
   * Create a new builder that is filled with the values of the provided grid specification.
   *
   * @param aSrc
   *        The source grid specification to copy the values from. May be <code>null</code>.
   * @return A new builder. Never <code>null</code>.
   * @since 0.9.1
   */
  @NonNull
  public static Builder builder (@Nullable final BootstrapGridSpec aSrc)
  {
    final Builder ret = new Builder ();
    if (aSrc != null)
      ret.xs (aSrc.m_eXS).sm (aSrc.m_eSM).md (aSrc.m_eMD).lg (aSrc.m_eLG).xl (aSrc.m_eXL).xxl (aSrc.m_eXXL);
    return ret;
  }

  /**
   * Builder class for {@link BootstrapGridSpec}. Each breakpoint can either be set with the
   * respective enum entry or with the number of parts to span. Unset breakpoints inherit the value
   * of the next smaller breakpoint.
   *
   * @author Philip Helger
   * @since 0.9.1
   */
  public static class Builder implements IBuilder <BootstrapGridSpec>
  {
    private EBootstrapGridXS m_eXS;
    private EBootstrapGridSM m_eSM;
    private EBootstrapGridMD m_eMD;
    private EBootstrapGridLG m_eLG;
    private EBootstrapGridXL m_eXL;
    private EBootstrapGridXXL m_eXXL;

    protected Builder ()
    {}

    @NonNull
    public Builder xs (@Nullable final EBootstrapGridXS e)
    {
      m_eXS = e;
      return this;
    }

    @NonNull
    public Builder xs (final int nParts)
    {
      return xs (EBootstrapGridXS.getFromParts (nParts));
    }

    @NonNull
    public Builder sm (@Nullable final EBootstrapGridSM e)
    {
      m_eSM = e;
      return this;
    }

    @NonNull
    public Builder sm (final int nParts)
    {
      return sm (EBootstrapGridSM.getFromParts (nParts));
    }

    @NonNull
    public Builder md (@Nullable final EBootstrapGridMD e)
    {
      m_eMD = e;
      return this;
    }

    @NonNull
    public Builder md (final int nParts)
    {
      return md (EBootstrapGridMD.getFromParts (nParts));
    }

    @NonNull
    public Builder lg (@Nullable final EBootstrapGridLG e)
    {
      m_eLG = e;
      return this;
    }

    @NonNull
    public Builder lg (final int nParts)
    {
      return lg (EBootstrapGridLG.getFromParts (nParts));
    }

    @NonNull
    public Builder xl (@Nullable final EBootstrapGridXL e)
    {
      m_eXL = e;
      return this;
    }

    @NonNull
    public Builder xl (final int nParts)
    {
      return xl (EBootstrapGridXL.getFromParts (nParts));
    }

    @NonNull
    public Builder xxl (@Nullable final EBootstrapGridXXL e)
    {
      m_eXXL = e;
      return this;
    }

    @NonNull
    public Builder xxl (final int nParts)
    {
      return xxl (EBootstrapGridXXL.getFromParts (nParts));
    }

    /**
     * Set the number of parts of all breakpoints at once.
     *
     * @param nParts
     *        The number of parts to use for all breakpoints.
     * @return this for chaining
     */
    @NonNull
    public Builder all (final int nParts)
    {
      // The larger sizes inherit from the smaller sizes
      return xs (nParts).sm (null).md (null).lg (null).xl (null).xxl (null);
    }

    @NonNull
    public BootstrapGridSpec build ()
    {
      return new BootstrapGridSpec (m_eXS, m_eSM, m_eMD, m_eLG, m_eXL, m_eXXL);
    }
  }
}
