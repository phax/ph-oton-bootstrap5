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
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonempty;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.html.hc.IHCConversionSettingsToNode;
import com.helger.html.hc.IHCHasChildrenMutable;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.html.IHCElement;
import com.helger.html.hc.html.grouping.AbstractHCDiv;
import com.helger.html.hc.html.grouping.HCDiv;
import com.helger.html.hc.html.sections.HCH5;
import com.helger.html.hc.impl.HCNodeList;
import com.helger.html.hc.impl.HCTextNode;
import com.helger.photon.bootstrap5.CBootstrapCSS;
import com.helger.photon.bootstrap5.utils.BootstrapCloseIcon;

/**
 * Bootstrap 5 Offcanvas component. Use {@link #applyToggleTo(IHCElement)} to make a button or link
 * open this Offcanvas element without custom JS code.
 *
 * @author Philip Helger
 */
public class BootstrapOffcanvas extends AbstractHCDiv <BootstrapOffcanvas>
{
  public static final boolean DEFAULT_SHOW_CLOSE = true;

  private EBootstrapOffcanvasPlacement m_ePlacement;
  private boolean m_bShowClose = DEFAULT_SHOW_CLOSE;
  private IHCNode m_aHeader;
  private IHCNode m_aBody;

  public BootstrapOffcanvas ()
  {
    this (EBootstrapOffcanvasPlacement.DEFAULT);
  }

  public BootstrapOffcanvas (@NonNull final EBootstrapOffcanvasPlacement ePlacement)
  {
    ensureID ();
    setPlacement (ePlacement);
  }

  @NonNull
  public final EBootstrapOffcanvasPlacement getPlacement ()
  {
    return m_ePlacement;
  }

  @NonNull
  public final BootstrapOffcanvas setPlacement (@NonNull final EBootstrapOffcanvasPlacement ePlacement)
  {
    ValueEnforcer.notNull (ePlacement, "Placement");
    m_ePlacement = ePlacement;
    return this;
  }

  public final boolean isShowClose ()
  {
    return m_bShowClose;
  }

  @NonNull
  public final BootstrapOffcanvas setShowClose (final boolean bShowClose)
  {
    m_bShowClose = bShowClose;
    return this;
  }

  /**
   * Enable body scrolling while the Offcanvas is open. By default it is disabled.
   *
   * @param bBodyScroll
   *        <code>true</code> to enable scrolling
   * @return this for chaining
   */
  @NonNull
  public final BootstrapOffcanvas setBodyScroll (final boolean bBodyScroll)
  {
    customAttrs ().setDataAttr ("bs-scroll", Boolean.toString (bBodyScroll));
    return this;
  }

  /**
   * Disable the backdrop, or make it "static" so that clicking it does not close the Offcanvas.
   *
   * @param sBackdrop
   *        "true", "false" or "static"
   * @return this for chaining
   */
  @NonNull
  public final BootstrapOffcanvas setBackdrop (@NonNull @Nonempty final String sBackdrop)
  {
    ValueEnforcer.notEmpty (sBackdrop, "Backdrop");
    customAttrs ().setDataAttr ("bs-backdrop", sBackdrop);
    return this;
  }

  @NonNull
  public final BootstrapOffcanvas setHeader (@Nullable final String sHeader)
  {
    return setHeader (HCTextNode.createOnDemand (sHeader));
  }

  @NonNull
  public final BootstrapOffcanvas setHeader (@Nullable final IHCNode aHeader)
  {
    m_aHeader = aHeader;
    return this;
  }

  @NonNull
  public final BootstrapOffcanvas setBody (@Nullable final String sBody)
  {
    return setBody (HCTextNode.createOnDemand (sBody));
  }

  @NonNull
  public final BootstrapOffcanvas setBody (@Nullable final IHCNode aBody)
  {
    m_aBody = aBody;
    return this;
  }

  @NonNull
  public final BootstrapOffcanvas setBody (@Nullable final Iterable <? extends IHCNode> aBody)
  {
    return setBody (new HCNodeList ().addChildren (aBody));
  }

  @NonNull
  @Nonempty
  private String _getTitleID ()
  {
    return getID () + "title";
  }

  /**
   * Make the passed element (usually a button or a link) the toggle for this Offcanvas element.
   *
   * @param aElement
   *        The element to modify. May not be <code>null</code>.
   * @return this for chaining
   */
  @NonNull
  public final BootstrapOffcanvas applyToggleTo (@NonNull final IHCElement <?> aElement)
  {
    ValueEnforcer.notNull (aElement, "Element");
    aElement.customAttrs ().setDataAttr ("bs-toggle", "offcanvas");
    aElement.customAttrs ().setDataAttr ("bs-target", "#" + getID ());
    aElement.customAttrs ().setAriaControls (getID ());
    return this;
  }

  @Override
  protected void onFinalizeNodeState (@NonNull final IHCConversionSettingsToNode aConversionSettings,
                                      @NonNull final IHCHasChildrenMutable <?, ? super IHCNode> aTargetNode)
  {
    super.onFinalizeNodeState (aConversionSettings, aTargetNode);
    addClasses (CBootstrapCSS.OFFCANVAS, m_ePlacement).setTabIndex (-1);

    if (m_aHeader != null)
    {
      final String sTitleID = _getTitleID ();
      customAttrs ().setAriaLabeledBy (sTitleID);
      final HCDiv aHeader = addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.OFFCANVAS_HEADER));
      aHeader.addChild (new HCH5 ().addClass (CBootstrapCSS.OFFCANVAS_TITLE).setID (sTitleID).addChild (m_aHeader));
      if (m_bShowClose)
      {
        final BootstrapCloseIcon aCloseIcon = new BootstrapCloseIcon ();
        aCloseIcon.customAttrs ().setDataAttr ("bs-dismiss", "offcanvas");
        aHeader.addChild (aCloseIcon);
      }
    }
    if (m_aBody != null)
      addChild (new HCDiv ().addClass (CBootstrapCSS.OFFCANVAS_BODY).addChild (m_aBody));
  }
}
