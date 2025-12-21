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
package com.helger.photon.bootstrap5.tooltip;

import java.util.Collection;
import java.util.Set;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonnegative;
import com.helger.annotation.style.CodingStyleguideUnaware;
import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.string.StringHelper;
import com.helger.base.string.StringImplode;
import com.helger.collection.commons.CommonsArrayList;
import com.helger.collection.commons.CommonsLinkedHashSet;
import com.helger.collection.commons.CommonsTreeSet;
import com.helger.collection.commons.ICommonsList;
import com.helger.html.EHTMLElement;
import com.helger.html.annotation.OutOfBandNode;
import com.helger.html.hc.IHCConversionSettingsToNode;
import com.helger.html.hc.IHCHasChildrenMutable;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.html.IHCElement;
import com.helger.html.hc.html.script.HCScriptInlineOnDocumentReady;
import com.helger.html.hc.render.HCRenderer;
import com.helger.html.jquery.IJQuerySelector;
import com.helger.html.jquery.JQuerySelector;
import com.helger.html.jscode.JSAnonymousFunction;
import com.helger.html.jscode.JSAssocArray;
import com.helger.html.jscode.JSInvocation;

/**
 * Bootstrap Tooltip
 *
 * @author Philip Helger
 */
@OutOfBandNode
public class BootstrapTooltip extends HCScriptInlineOnDocumentReady
{
  /**
   * This event fires immediately when the show instance method is called.
   */
  public static final String JS_EVENT_SHOW = "show.bs.tooltip";
  /**
   * This event is fired when the tooltip has been made visible to the user (will wait for CSS
   * transitions to complete).
   */
  public static final String JS_EVENT_SHOWN = "shown.bs.tooltip";
  /**
   * This event is fired immediately when the hide instance method has been called.
   */
  public static final String JS_EVENT_HIDE = "hide.bs.tooltip";
  /**
   * This event is fired when the tooltip has finished being hidden from the user (will wait for CSS
   * transitions to complete).
   */
  public static final String JS_EVENT_HIDDEN = "hidden.bs.tooltip";
  /**
   * This event is fired after the show.bs.tooltip event when the tooltip template has been added to
   * the DOM.
   */
  public static final String JS_EVENT_INSERTED = "inserted.bs.tooltip";

  public static final boolean DEFAULT_ANIMATION = true;
  public static final boolean DEFAULT_HTML = false;
  public static final EBootstrapTooltipPosition DEFAULT_PLACEMENT = EBootstrapTooltipPosition.TOP;
  @CodingStyleguideUnaware
  public static final Set <EBootstrapTooltipTrigger> DEFAULT_TRIGGER = new CommonsLinkedHashSet <> (EBootstrapTooltipTrigger.HOVER,
                                                                                                    EBootstrapTooltipTrigger.FOCUS).getAsUnmodifiable ();
  public static final EBootstrapTooltipFallbackPlacement DEFAULT_FALLBACK_PLACEMENT = EBootstrapTooltipFallbackPlacement.FLIP;
  public static final EBootstrapTooltipBoundary DEFAULT_BOUNDARY = EBootstrapTooltipBoundary.SCROLL_PARENT;

  private final IJQuerySelector m_aSelector;
  private boolean m_bAnimation = DEFAULT_ANIMATION;
  private IJQuerySelector m_aContainer;
  private int m_nDelayShow = 0;
  private int m_nDelayHide = 0;
  private boolean m_bHTML = DEFAULT_HTML;
  private EBootstrapTooltipPosition m_ePlacement = DEFAULT_PLACEMENT;
  private JSAnonymousFunction m_aPlacementFunc;
  private String m_sSelector;
  private String m_sTooltipTitle;
  private JSAnonymousFunction m_aTooltipTitleFunc;
  @CodingStyleguideUnaware
  private Set <EBootstrapTooltipTrigger> m_aTrigger = DEFAULT_TRIGGER;
  private String m_sOffset;
  private EBootstrapTooltipFallbackPlacement m_eFallbackPlacement = DEFAULT_FALLBACK_PLACEMENT;
  private EBootstrapTooltipBoundary m_eBoundary = DEFAULT_BOUNDARY;

  public BootstrapTooltip (@NonNull final IHCElement <?> aElement)
  {
    this (JQuerySelector.id (aElement));
  }

  public BootstrapTooltip (@NonNull final IJQuerySelector aSelector)
  {
    ValueEnforcer.notNull (aSelector, "Selector");
    m_aSelector = aSelector;
  }

  public boolean isAnimation ()
  {
    return m_bAnimation;
  }

  @NonNull
  public BootstrapTooltip setAnimation (final boolean bAnimation)
  {
    m_bAnimation = bAnimation;
    return this;
  }

  public boolean isHTML ()
  {
    return m_bHTML;
  }

  @NonNull
  public BootstrapTooltip setHTML (final boolean bHTML)
  {
    m_bHTML = bHTML;
    return this;
  }

  @Nullable
  public EBootstrapTooltipPosition getPlacementPosition ()
  {
    return m_ePlacement;
  }

  @Nullable
  public JSAnonymousFunction getPlacementFunction ()
  {
    return m_aPlacementFunc;
  }

  @NonNull
  public BootstrapTooltip setPlacement (@NonNull final EBootstrapTooltipPosition ePosition)
  {
    ValueEnforcer.notNull (ePosition, "Position");
    m_ePlacement = ePosition;
    m_aPlacementFunc = null;
    return this;
  }

  /**
   * @param aFunction
   *        Callback function that is called with the tooltip DOM node as its first argument and the
   *        triggering element DOM node as its second. The this context is set to the tooltip
   *        instance.
   * @return this
   */
  @NonNull
  public BootstrapTooltip setPlacement (@NonNull final JSAnonymousFunction aFunction)
  {
    ValueEnforcer.notNull (aFunction, "Function");
    m_ePlacement = null;
    m_aPlacementFunc = aFunction;
    return this;
  }

  @Nullable
  public String getSelector ()
  {
    return m_sSelector;
  }

  @NonNull
  public BootstrapTooltip setSelector (@Nullable final String sSelector)
  {
    m_sSelector = sSelector;
    return this;
  }

  @Nullable
  public String getTooltipTitleString ()
  {
    return m_sTooltipTitle;
  }

  @Nullable
  public JSAnonymousFunction getTooltipTitleFunction ()
  {
    return m_aTooltipTitleFunc;
  }

  @NonNull
  public BootstrapTooltip setTooltipTitle (@Nullable final String sTitle)
  {
    m_sTooltipTitle = sTitle;
    m_aTooltipTitleFunc = null;
    return this;
  }

  @NonNull
  public BootstrapTooltip setTooltipTitle (@Nullable final IHCNode aTooltipTitle)
  {
    setHTML (true);
    // No nonce needed
    m_sTooltipTitle = aTooltipTitle == null ? null : HCRenderer.getAsHTMLStringWithoutNamespaces (aTooltipTitle);
    m_aTooltipTitleFunc = null;
    return this;
  }

  /**
   * @param aFunction
   *        Callback function with 1 parameter: <code>(this.$element[0])</code>
   * @return this
   */
  @NonNull
  public BootstrapTooltip setTooltipTitle (@Nullable final JSAnonymousFunction aFunction)
  {
    m_sTooltipTitle = null;
    m_aTooltipTitleFunc = aFunction;
    return this;
  }

  @Nullable
  @ReturnsMutableCopy
  public ICommonsList <EBootstrapTooltipTrigger> getTrigger ()
  {
    return new CommonsArrayList <> (m_aTrigger);
  }

  @NonNull
  public BootstrapTooltip setTrigger (@Nullable final EBootstrapTooltipTrigger... aTrigger)
  {
    // Avoid duplicates!
    m_aTrigger = new CommonsTreeSet <> (aTrigger);
    return this;
  }

  @NonNull
  public BootstrapTooltip setTrigger (@Nullable final Collection <EBootstrapTooltipTrigger> aTrigger)
  {
    // Avoid duplicates!
    m_aTrigger = new CommonsTreeSet <> (aTrigger);
    return this;
  }

  @Nonnegative
  public int getDelayShow ()
  {
    return m_nDelayShow;
  }

  @Nonnegative
  public int getDelayHide ()
  {
    return m_nDelayHide;
  }

  @NonNull
  public BootstrapTooltip setDelay (@Nonnegative final int nDelay)
  {
    return setDelay (nDelay, nDelay);
  }

  @NonNull
  public BootstrapTooltip setDelay (@Nonnegative final int nShowDelay, @Nonnegative final int nHideDelay)
  {
    ValueEnforcer.isGE0 (nShowDelay, "ShowDelay");
    ValueEnforcer.isGE0 (nHideDelay, "HideDelay");

    m_nDelayShow = nShowDelay;
    m_nDelayHide = nHideDelay;
    return this;
  }

  @Nullable
  public IJQuerySelector getContainer ()
  {
    return m_aContainer;
  }

  @NonNull
  public BootstrapTooltip setContainer (@NonNull final EHTMLElement eContainer)
  {
    ValueEnforcer.notNull (eContainer, "Container");

    return setContainer (JQuerySelector.element (eContainer));
  }

  @NonNull
  public BootstrapTooltip setContainer (@Nullable final IJQuerySelector aContainer)
  {
    m_aContainer = aContainer;
    return this;
  }

  @Nullable
  public String getOffset ()
  {
    return m_sOffset;
  }

  @NonNull
  public BootstrapTooltip setOffset (@Nullable final String sOffset)
  {
    m_sOffset = sOffset;
    return this;
  }

  @NonNull
  public BootstrapTooltip setOffset (final int nOffset)
  {
    return setOffset (Integer.toString (nOffset));
  }

  @Nullable
  public EBootstrapTooltipFallbackPlacement getFallbackPlacement ()
  {
    return m_eFallbackPlacement;
  }

  @NonNull
  public BootstrapTooltip setFallbackPlacement (@Nullable final EBootstrapTooltipFallbackPlacement eFallbackPlacement)
  {
    m_eFallbackPlacement = eFallbackPlacement;
    return this;
  }

  @Nullable
  public EBootstrapTooltipBoundary getBoundary ()
  {
    return m_eBoundary;
  }

  @NonNull
  public BootstrapTooltip setBoundary (@Nullable final EBootstrapTooltipBoundary eBoundary)
  {
    m_eBoundary = eBoundary;
    return this;
  }

  @NonNull
  public JSAssocArray getJSOptions ()
  {
    final JSAssocArray aOptions = new JSAssocArray ();
    if (m_bAnimation != DEFAULT_ANIMATION)
      aOptions.add ("animation", m_bAnimation);
    if (m_aContainer != null)
      aOptions.add ("container", m_aContainer.getExpression ());
    if (m_nDelayShow > 0 || m_nDelayHide > 0)
    {
      if (m_nDelayShow == m_nDelayHide)
        aOptions.add ("delay", m_nDelayShow);
      else
        aOptions.add ("delay", new JSAssocArray ().add ("show", m_nDelayShow).add ("hide", m_nDelayHide));
    }
    if (m_bHTML != DEFAULT_HTML)
      aOptions.add ("html", m_bHTML);
    if (m_ePlacement != null)
      aOptions.add ("placement", m_ePlacement.getValue ());
    else
      aOptions.add ("placement", m_aPlacementFunc);
    if (StringHelper.isNotEmpty (m_sSelector))
      aOptions.add ("selector", m_sSelector);
    if (StringHelper.isNotEmpty (m_sTooltipTitle))
      aOptions.add ("title", m_sTooltipTitle);
    else
      if (m_aTooltipTitleFunc != null)
        aOptions.add ("title", m_aTooltipTitleFunc);
    if (!m_aTrigger.isEmpty () && !DEFAULT_TRIGGER.equals (m_aTrigger))
      aOptions.add ("trigger", StringImplode.getImplodedMapped (' ', m_aTrigger, EBootstrapTooltipTrigger::getValue));
    if (StringHelper.isNotEmpty (m_sOffset))
      aOptions.add ("offset", m_sOffset);
    if (m_eBoundary != null)
      aOptions.add ("boundary", m_eBoundary.getValue ());
    return aOptions;
  }

  @NonNull
  public JSInvocation jsInvoke ()
  {
    return m_aSelector.invoke ().invoke ("tooltip");
  }

  @NonNull
  public JSInvocation jsAttach ()
  {
    return jsInvoke ().arg (getJSOptions ());
  }

  @NonNull
  public JSInvocation jsShow ()
  {
    return jsInvoke ().arg ("show");
  }

  @NonNull
  public JSInvocation jsHide ()
  {
    return jsInvoke ().arg ("hide");
  }

  @NonNull
  public JSInvocation jsToggle ()
  {
    return jsInvoke ().arg ("toggle");
  }

  @NonNull
  public JSInvocation jsDispose ()
  {
    return jsInvoke ().arg ("dispose");
  }

  @NonNull
  public JSInvocation jsEnable ()
  {
    return jsInvoke ().arg ("enable");
  }

  @NonNull
  public JSInvocation jsDisable ()
  {
    return jsInvoke ().arg ("disable");
  }

  @NonNull
  public JSInvocation jsToggleEnabled ()
  {
    return jsInvoke ().arg ("toggleEnabled");
  }

  @NonNull
  public JSInvocation jsUpdate ()
  {
    return jsInvoke ().arg ("update");
  }

  @Override
  protected void onFinalizeNodeState (@NonNull final IHCConversionSettingsToNode aConversionSettings,
                                      @NonNull final IHCHasChildrenMutable <?, ? super IHCNode> aTargetNode)
  {
    super.onFinalizeNodeState (aConversionSettings, aTargetNode);
    setOnDocumentReadyCode (jsAttach ());
  }
}
