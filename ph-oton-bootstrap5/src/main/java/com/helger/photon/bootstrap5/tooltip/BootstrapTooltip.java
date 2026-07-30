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
import com.helger.html.jscode.JSArray;
import com.helger.html.jscode.JSAssocArray;
import com.helger.html.jscode.JSExpr;
import com.helger.html.jscode.JSInvocation;
import com.helger.html.jscode.JSParam;

/**
 * Bootstrap 5 Tooltip. Uses the native <code>bootstrap.Tooltip</code> JS API - no jQuery involved.
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
  private final ICommonsList <EBootstrapTooltipPosition> m_aFallbackPlacements = new CommonsArrayList <> ();
  private EBootstrapTooltipBoundary m_eBoundary;
  private String m_sCustomClass;

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

  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <EBootstrapTooltipPosition> getFallbackPlacements ()
  {
    return m_aFallbackPlacements.getClone ();
  }

  /**
   * Define the placements Popper v2 may fall back to, if the tooltip does not fit in the desired
   * placement. If none is set, the Bootstrap default (top, right, bottom, left) is used.
   *
   * @param aFallbackPlacements
   *        The fallback placements in the order of preference. May be <code>null</code>.
   * @return this
   */
  @NonNull
  public BootstrapTooltip setFallbackPlacements (@Nullable final EBootstrapTooltipPosition... aFallbackPlacements)
  {
    m_aFallbackPlacements.setAll (aFallbackPlacements);
    return this;
  }

  @Nullable
  public String getCustomClass ()
  {
    return m_sCustomClass;
  }

  @NonNull
  public BootstrapTooltip setCustomClass (@Nullable final String sCustomClass)
  {
    m_sCustomClass = sCustomClass;
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
    if (m_aFallbackPlacements.isNotEmpty ())
    {
      final JSArray aFallbacks = new JSArray ();
      for (final EBootstrapTooltipPosition ePosition : m_aFallbackPlacements)
        aFallbacks.add (ePosition.getValue ());
      aOptions.add ("fallbackPlacements", aFallbacks);
    }
    if (m_eBoundary != null)
      aOptions.add ("boundary", m_eBoundary.getValue ());
    if (StringHelper.isNotEmpty (m_sCustomClass))
      aOptions.add ("customClass", m_sCustomClass);
    return aOptions;
  }

  /**
   * @return The JS expression selecting all matching elements:
   *         <code>document.querySelectorAll (selector)</code>
   */
  @NonNull
  public JSInvocation jsQuerySelectorAll ()
  {
    return JSExpr.ref ("document").invoke ("querySelectorAll").arg (m_aSelector.getExpression ());
  }

  @NonNull
  private JSInvocation _jsForEachElement (@NonNull final JSAnonymousFunction aCallback)
  {
    return jsQuerySelectorAll ().invoke ("forEach").arg (aCallback);
  }

  /**
   * @return The JS invocation that creates a <code>new bootstrap.Tooltip</code> for each matching
   *         element, using the options of this object.
   */
  @NonNull
  public JSInvocation jsAttach ()
  {
    final JSAnonymousFunction aFn = new JSAnonymousFunction ();
    final JSParam aElem = aFn.param ("el");
    aFn.body ()
       .add (new JSInvocation (JSExpr.ref (JSExpr.ref ("bootstrap"), "Tooltip")).arg (aElem).arg (getJSOptions ()));
    return _jsForEachElement (aFn);
  }

  @NonNull
  private JSInvocation _jsInstanceCall (@NonNull final String sMethod)
  {
    final JSAnonymousFunction aFn = new JSAnonymousFunction ();
    final JSParam aElem = aFn.param ("el");
    aFn.body ()
       .add (JSExpr.ref (JSExpr.ref ("bootstrap"), "Tooltip")
                   .invoke ("getOrCreateInstance")
                   .arg (aElem)
                   .invoke (sMethod));
    return _jsForEachElement (aFn);
  }

  @NonNull
  public JSInvocation jsShow ()
  {
    return _jsInstanceCall ("show");
  }

  @NonNull
  public JSInvocation jsHide ()
  {
    return _jsInstanceCall ("hide");
  }

  @NonNull
  public JSInvocation jsToggle ()
  {
    return _jsInstanceCall ("toggle");
  }

  @NonNull
  public JSInvocation jsDispose ()
  {
    return _jsInstanceCall ("dispose");
  }

  @NonNull
  public JSInvocation jsEnable ()
  {
    return _jsInstanceCall ("enable");
  }

  @NonNull
  public JSInvocation jsDisable ()
  {
    return _jsInstanceCall ("disable");
  }

  @NonNull
  public JSInvocation jsToggleEnabled ()
  {
    return _jsInstanceCall ("toggleEnabled");
  }

  @NonNull
  public JSInvocation jsUpdate ()
  {
    return _jsInstanceCall ("update");
  }

  @Override
  protected void onFinalizeNodeState (@NonNull final IHCConversionSettingsToNode aConversionSettings,
                                      @NonNull final IHCHasChildrenMutable <?, ? super IHCNode> aTargetNode)
  {
    super.onFinalizeNodeState (aConversionSettings, aTargetNode);
    setOnDocumentReadyCode (jsAttach ());
  }
}
