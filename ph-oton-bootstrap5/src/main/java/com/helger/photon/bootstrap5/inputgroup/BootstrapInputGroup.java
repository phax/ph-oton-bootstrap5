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
package com.helger.photon.bootstrap5.inputgroup;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.style.OverrideOnDemand;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.string.StringHelper;
import com.helger.html.css.DefaultCSSClassProvider;
import com.helger.html.css.ICSSClassProvider;
import com.helger.html.hc.IHCConversionSettingsToNode;
import com.helger.html.hc.IHCHasChildrenMutable;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.html.forms.AbstractHCButton;
import com.helger.html.hc.html.grouping.AbstractHCDiv;
import com.helger.html.hc.html.grouping.HCDiv;
import com.helger.html.hc.html.textlevel.HCSpan;
import com.helger.photon.bootstrap5.CBootstrapCSS;
import com.helger.photon.bootstrap5.dropdown.BootstrapDropdownMenu;

/**
 * Bootstrap input group. Children must be added in the correct order. Use
 * {@link #addChildPrefix(String)}, {@link #addChildPrefix(IHCNode)},
 * {@link #addChildSuffix(String)} and {@link #addChildSuffix(IHCNode)} for the prepends and
 * appends.
 *
 * @author Philip Helger
 */
public class BootstrapInputGroup extends AbstractHCDiv <BootstrapInputGroup>
{
  private static final ICSSClassProvider CSS_CLASS_IG_PREPEND = DefaultCSSClassProvider.create ("input-group-prepend");
  private static final ICSSClassProvider CSS_CLASS_IG_APPEND = DefaultCSSClassProvider.create ("input-group-append");

  private EBootstrapInputGroupSize m_eSize;

  public BootstrapInputGroup ()
  {
    this (EBootstrapInputGroupSize.DEFAULT);
  }

  public BootstrapInputGroup (@NonNull final EBootstrapInputGroupSize eSize)
  {
    setSize (eSize);
  }

  @NonNull
  public final EBootstrapInputGroupSize getSize ()
  {
    return m_eSize;
  }

  @NonNull
  public final BootstrapInputGroup setSize (@NonNull final EBootstrapInputGroupSize eSize)
  {
    ValueEnforcer.notNull (eSize, "Size");
    m_eSize = eSize;
    return this;
  }

  /**
   * @return The DIV with class "input-group-prepend". Never <code>null</code>.
   */
  @NonNull
  @OverrideOnDemand
  public HCDiv createGroupPrepend ()
  {
    return new HCDiv ().addClass (CSS_CLASS_IG_PREPEND);
  }

  /**
   * @return The DIV with class "input-group-append". Never <code>null</code>.
   */
  @NonNull
  @OverrideOnDemand
  public HCDiv createGroupAppend ()
  {
    return new HCDiv ().addClass (CSS_CLASS_IG_APPEND);
  }

  /**
   * If an existing DIV with class "input-group-prepend" is present, reuse it. Else create a new one
   * and append it. Elements in here are prepended to the date edit.
   *
   * @return Never <code>null</code>.
   * @see #createGroupPrepend()
   */
  @NonNull
  public HCDiv getOrCreateGroupPrepend ()
  {
    // Existing "prepend" present?
    final HCDiv aDiv = (HCDiv) findFirstChild (x -> x instanceof final HCDiv xd &&
                                                    xd.containsClass (CSS_CLASS_IG_PREPEND));
    // Prepend group MUST always be the first child, so before any control
    return aDiv != null ? aDiv : addAndReturnChildAt (0, createGroupPrepend ());
  }

  /**
   * If an existing DIV with class "input-group-append" is present, reuse it. Else create a new one
   * and append it. Elements in here are appended to the date edit.
   *
   * @return Never <code>null</code>.
   * @see #createGroupAppend()
   */
  @NonNull
  public HCDiv getOrCreateGroupAppend ()
  {
    // Existing "append" present?
    final HCDiv aDiv = (HCDiv) findFirstChild (x -> x instanceof final HCDiv xd &&
                                                    xd.containsClass (CSS_CLASS_IG_APPEND));
    return aDiv != null ? aDiv : addAndReturnChild (createGroupAppend ());
  }

  @NonNull
  public static HCSpan getWrapped (@NonNull final String sText)
  {
    return new HCSpan ().addClass (CBootstrapCSS.INPUT_GROUP_TEXT).addChild (sText);
  }

  @NonNull
  public static IHCNode getWrapped (@NonNull final IHCNode aNode)
  {
    // Buttons and dropdowns don't need a surrounding div
    if (aNode instanceof AbstractHCButton <?> || aNode instanceof BootstrapDropdownMenu)
      return aNode;
    return new HCDiv ().addClass (CBootstrapCSS.INPUT_GROUP_TEXT).addChild (aNode);
  }

  /**
   * Add a new text element to the prepend group.
   *
   * @param sText
   *        The text to be added. May be <code>null</code>.
   * @return this for chaining
   * @see #getOrCreateGroupPrepend()
   * @see #getWrapped(String)
   */
  @NonNull
  public final BootstrapInputGroup addChildPrefix (@Nullable final String sText)
  {
    if (StringHelper.isNotEmpty (sText))
      getOrCreateGroupPrepend ().addChild (getWrapped (sText));
    return this;
  }

  /**
   * Add a new node to the prepend group.
   *
   * @param aNode
   *        The node to be added. May be <code>null</code>.
   * @return this for chaining
   * @see #getOrCreateGroupPrepend()
   * @see #getWrapped(IHCNode)
   */
  @NonNull
  public final BootstrapInputGroup addChildPrefix (@Nullable final IHCNode aNode)
  {
    if (aNode != null)
      getOrCreateGroupPrepend ().addChild (getWrapped (aNode));
    return this;
  }

  /**
   * Add a new text element to the append group.
   *
   * @param sText
   *        The text to be added. May be <code>null</code>.
   * @return this for chaining
   * @see #getOrCreateGroupAppend()
   * @see #getWrapped(String)
   */
  @NonNull
  public final BootstrapInputGroup addChildSuffix (@Nullable final String sText)
  {
    if (StringHelper.isNotEmpty (sText))
      getOrCreateGroupAppend ().addChild (getWrapped (sText));
    return this;
  }

  /**
   * Add a new node to the append group.
   *
   * @param aNode
   *        The node to be added. May be <code>null</code>.
   * @return this for chaining
   * @see #getOrCreateGroupAppend()
   * @see #getWrapped(IHCNode)
   */
  @NonNull
  public final BootstrapInputGroup addChildSuffix (@Nullable final IHCNode aNode)
  {
    if (aNode != null)
      getOrCreateGroupAppend ().addChild (getWrapped (aNode));
    return this;
  }

  @Override
  protected void onFinalizeNodeState (@NonNull final IHCConversionSettingsToNode aConversionSettings,
                                      @NonNull final IHCHasChildrenMutable <?, ? super IHCNode> aTargetNode)
  {
    super.onFinalizeNodeState (aConversionSettings, aTargetNode);
    addClasses (CBootstrapCSS.INPUT_GROUP, m_eSize);
  }
}
