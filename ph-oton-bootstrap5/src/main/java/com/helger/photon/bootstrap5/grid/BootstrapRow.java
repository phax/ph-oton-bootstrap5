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

import com.helger.annotation.Nonnegative;
import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.html.hc.IHCConversionSettingsToNode;
import com.helger.html.hc.IHCHasChildrenMutable;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.html.grouping.HCDiv;
import com.helger.photon.bootstrap5.CBootstrapCSS;
import com.helger.photon.bootstrap5.base.AbstractBootstrapDiv;
import com.helger.photon.bootstrap5.gutter.BootstrapGutterSpec;

/**
 * Defines a Bootstrap row that contains columns.
 *
 * @author Philip Helger
 */
public class BootstrapRow extends AbstractBootstrapDiv <BootstrapRow>
{
  private EBootstrapRowVerticalAlign m_eVertAlign;
  private BootstrapGutterSpec m_aGutter;
  private BootstrapGutterSpec m_aGutterX;
  private BootstrapGutterSpec m_aGutterY;

  public BootstrapRow ()
  {
    addClass (CBootstrapCSS.ROW);
  }

  @Nullable
  public EBootstrapRowVerticalAlign getVertAlign ()
  {
    return m_eVertAlign;
  }

  @NonNull
  public BootstrapRow setVerticalAlign (@Nullable final EBootstrapRowVerticalAlign eVertAlign)
  {
    m_eVertAlign = eVertAlign;
    return this;
  }

  @Nullable
  public BootstrapGutterSpec getGutter ()
  {
    return m_aGutter;
  }

  @NonNull
  public BootstrapRow setGutter (@Nullable final BootstrapGutterSpec aGutter)
  {
    m_aGutter = aGutter;
    return this;
  }

  @Nullable
  public BootstrapGutterSpec getGutterX ()
  {
    return m_aGutterX;
  }

  @NonNull
  public BootstrapRow setGutterX (@Nullable final BootstrapGutterSpec aGutterX)
  {
    m_aGutterX = aGutterX;
    return this;
  }

  @Nullable
  public BootstrapGutterSpec getGutterY ()
  {
    return m_aGutterY;
  }

  @NonNull
  public BootstrapRow setGutterY (@Nullable final BootstrapGutterSpec aGutterY)
  {
    m_aGutterY = aGutterY;
    return this;
  }

  @NonNull
  public BootstrapCol createColumn (final int nParts)
  {
    return createColumn (BootstrapGridSpec.create (nParts));
  }

  @NonNull
  public BootstrapCol createColumn (final int nPartsXS,
                                    final int nPartsSM,
                                    final int nPartsMD,
                                    final int nPartsLG,
                                    final int nPartsXL,
                                    final int nPartsXXL)
  {
    return createColumn (BootstrapGridSpec.create (nPartsXS, nPartsSM, nPartsMD, nPartsLG, nPartsXL, nPartsXXL));
  }

  @NonNull
  public BootstrapCol createColumn (@Nullable final EBootstrapGridXS eXS,
                                    @Nullable final EBootstrapGridSM eSM,
                                    @Nullable final EBootstrapGridMD eMD,
                                    @Nullable final EBootstrapGridLG eLG,
                                    @Nullable final EBootstrapGridXL eXL,
                                    @Nullable final EBootstrapGridXXL eXXL)
  {
    return createColumn (new BootstrapGridSpec (eXS, eSM, eMD, eLG, eXL, eXXL));
  }

  @NonNull
  public BootstrapCol createColumn (@NonNull final BootstrapGridSpec aGridSpec)
  {
    ValueEnforcer.notNull (aGridSpec, "GridSpec");

    final BootstrapCol aDiv = addAndReturnChild (new BootstrapCol ());
    aGridSpec.applyTo (aDiv);
    return aDiv;
  }

  @NonNull
  public HCDiv createNewLine ()
  {
    return addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.W_100));
  }

  @Nullable
  public BootstrapCol getFirstColumn ()
  {
    return getColumnAtIndex (0);
  }

  @Nullable
  public BootstrapCol getColumnAtIndex (@Nonnegative final int nIndex)
  {
    int nCols = 0;
    for (final IHCNode aChild : getChildren ())
      if (aChild instanceof BootstrapCol)
      {
        if (nCols == nIndex)
          return (BootstrapCol) aChild;
        ++nCols;
      }
    return null;
  }

  @Nullable
  public BootstrapCol getLastColumn ()
  {
    for (final IHCNode aChild : getAllChildren ().reverse ())
      if (aChild instanceof BootstrapCol)
        return (BootstrapCol) aChild;
    return null;
  }

  @Override
  protected void onFinalizeNodeState (@NonNull final IHCConversionSettingsToNode aConversionSettings,
                                      @NonNull final IHCHasChildrenMutable <?, ? super IHCNode> aTargetNode)
  {
    super.onFinalizeNodeState (aConversionSettings, aTargetNode);
    addClass (m_eVertAlign);
    if (m_aGutter != null)
      m_aGutter.applyTo (this);
    if (m_aGutterX != null)
      m_aGutterX.applyXTo (this);
    if (m_aGutterY != null)
      m_aGutterY.applyYTo (this);
  }

  @NonNull
  @ReturnsMutableCopy
  public static BootstrapRow createRowWithOneColumn (final int nParts, @NonNull final IHCNode aCtrl)
  {
    final BootstrapRow aRow = new BootstrapRow ();
    aRow.createColumn (nParts).addChild (aCtrl);
    return aRow;
  }

  @NonNull
  @ReturnsMutableCopy
  public static BootstrapRow createRowWithOneColumn (final int nPartsXS,
                                                     final int nPartsSM,
                                                     final int nPartsMD,
                                                     final int nPartsLG,
                                                     final int nPartsXL,
                                                     final int nPartsXXL,
                                                     @NonNull final IHCNode aCtrl)
  {
    final BootstrapRow aRow = new BootstrapRow ();
    aRow.createColumn (nPartsXS, nPartsSM, nPartsMD, nPartsLG, nPartsXL, nPartsXXL).addChild (aCtrl);
    return aRow;
  }

  @NonNull
  @ReturnsMutableCopy
  public static BootstrapRow createRowWithOneColumn (@NonNull final BootstrapGridSpec aParts,
                                                     @NonNull final IHCNode aCtrl)
  {
    final BootstrapRow aRow = new BootstrapRow ();
    aRow.createColumn (aParts).addChild (aCtrl);
    return aRow;
  }
}
