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
package com.helger.photon.bootstrap5.button;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.OverridingMethodsMustInvokeSuper;
import com.helger.annotation.style.OverrideOnDemand;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.html.EHTMLRole;
import com.helger.html.hc.IHCConversionSettingsToNode;
import com.helger.html.hc.IHCHasChildrenMutable;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.html.textlevel.AbstractHCA;
import com.helger.html.hc.impl.HCTextNode;
import com.helger.photon.bootstrap5.CBootstrapCSS;
import com.helger.photon.uicore.icon.IIcon;

/**
 * Bootstrap button based on an &lt;a&gt;
 *
 * @author Philip Helger
 */
public class BootstrapLinkButton extends AbstractHCA <BootstrapLinkButton>
{
  private EBootstrapButtonType m_eButtonType = EBootstrapButtonType.DEFAULT;
  private EBootstrapButtonSize m_eButtonSize = EBootstrapButtonSize.DEFAULT;
  private IIcon m_aIcon;

  public BootstrapLinkButton ()
  {
    this (EBootstrapButtonType.DEFAULT, EBootstrapButtonSize.DEFAULT);
  }

  public BootstrapLinkButton (@NonNull final EBootstrapButtonType eButtonType)
  {
    this (eButtonType, EBootstrapButtonSize.DEFAULT);
  }

  public BootstrapLinkButton (@NonNull final EBootstrapButtonSize eButtonSize)
  {
    this (EBootstrapButtonType.DEFAULT, eButtonSize);
  }

  public BootstrapLinkButton (@NonNull final EBootstrapButtonType eButtonType, @NonNull final EBootstrapButtonSize eButtonSize)
  {
    setRole (EHTMLRole.BUTTON);
    addClass (CBootstrapCSS.BTN);
    setButtonType (eButtonType);
    setButtonSize (eButtonSize);
  }

  @NonNull
  public EBootstrapButtonType getButtonType ()
  {
    return m_eButtonType;
  }

  @NonNull
  public final BootstrapLinkButton setButtonType (@NonNull final EBootstrapButtonType eButtonType)
  {
    m_eButtonType = ValueEnforcer.notNull (eButtonType, "ButtonType");
    return this;
  }

  @Nullable
  public EBootstrapButtonSize getButtonSize ()
  {
    return m_eButtonSize;
  }

  @NonNull
  public final BootstrapLinkButton setButtonSize (@NonNull final EBootstrapButtonSize eButtonSize)
  {
    m_eButtonSize = ValueEnforcer.notNull (eButtonSize, "ButtonSize");
    return this;
  }

  @Nullable
  public IIcon getIcon ()
  {
    return m_aIcon;
  }

  @NonNull
  public final BootstrapLinkButton setIcon (@Nullable final IIcon aIcon)
  {
    m_aIcon = aIcon;
    return this;
  }

  @Override
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected void onFinalizeNodeState (@NonNull final IHCConversionSettingsToNode aConversionSettings,
                                      @NonNull final IHCHasChildrenMutable <?, ? super IHCNode> aTargetNode)
  {
    super.onFinalizeNodeState (aConversionSettings, aTargetNode);
    // apply type and size
    addClasses (getButtonType (), getButtonSize ());

    // apply icon
    if (m_aIcon != null)
    {
      // For CSS styling
      addClass (BootstrapButton.CSS_CLASS_BTN_WITH_ICON);
      final boolean bAddSeparator = hasChildren ();
      addChildAt (0, m_aIcon.getAsNode ());
      if (bAddSeparator)
      {
        // Add spacer
        addChildAt (1, new HCTextNode (" "));
      }
    }
  }
}
