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

import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.string.StringHelper;
import com.helger.collection.commons.CommonsArrayList;
import com.helger.collection.commons.ICommonsList;
import com.helger.html.hc.IHCConversionSettingsToNode;
import com.helger.html.hc.IHCHasChildrenMutable;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.html.forms.AbstractHCButton;
import com.helger.html.hc.html.grouping.AbstractHCDiv;
import com.helger.html.hc.html.textlevel.HCSpan;
import com.helger.photon.bootstrap5.CBootstrapCSS;
import com.helger.photon.bootstrap5.dropdown.BootstrapDropdownMenu;

/**
 * Bootstrap input group. In Bootstrap 5 all prefix and suffix elements are direct children of the
 * input group - the Bootstrap 4 "input-group-prepend" and "input-group-append" wrapper elements
 * were removed. Use {@link #addChildPrefix(String)}, {@link #addChildPrefix(IHCNode)},
 * {@link #addChildSuffix(String)} and {@link #addChildSuffix(IHCNode)} for the prefixes and
 * suffixes - they are emitted before respectively after the regular children.
 *
 * @author Philip Helger
 */
public class BootstrapInputGroup extends AbstractHCDiv <BootstrapInputGroup>
{
  private EBootstrapInputGroupSize m_eSize;
  private final ICommonsList <IHCNode> m_aPrefixes = new CommonsArrayList <> ();
  private final ICommonsList <IHCNode> m_aSuffixes = new CommonsArrayList <> ();

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

  @NonNull
  public static HCSpan getWrapped (@NonNull final String sText)
  {
    return new HCSpan ().addClass (CBootstrapCSS.INPUT_GROUP_TEXT).addChild (sText);
  }

  @NonNull
  public static IHCNode getWrapped (@NonNull final IHCNode aNode)
  {
    // Buttons and dropdowns don't need a surrounding span
    if (aNode instanceof AbstractHCButton <?> || aNode instanceof BootstrapDropdownMenu)
      return aNode;
    return new HCSpan ().addClass (CBootstrapCSS.INPUT_GROUP_TEXT).addChild (aNode);
  }

  /**
   * Add a new text element before the contained control.
   *
   * @param sText
   *        The text to be added. May be <code>null</code>.
   * @return this for chaining
   * @see #getWrapped(String)
   */
  @NonNull
  public final BootstrapInputGroup addChildPrefix (@Nullable final String sText)
  {
    if (StringHelper.isNotEmpty (sText))
      m_aPrefixes.add (getWrapped (sText));
    return this;
  }

  /**
   * Add a new node before the contained control.
   *
   * @param aNode
   *        The node to be added. May be <code>null</code>.
   * @return this for chaining
   * @see #getWrapped(IHCNode)
   */
  @NonNull
  public final BootstrapInputGroup addChildPrefix (@Nullable final IHCNode aNode)
  {
    if (aNode != null)
      m_aPrefixes.add (getWrapped (aNode));
    return this;
  }

  /**
   * Add an already wrapped node before the contained control and before all other prefixes.
   *
   * @param aNode
   *        The node to be added as-is. May not be <code>null</code>.
   * @return this for chaining
   */
  @NonNull
  protected final BootstrapInputGroup addChildPrefixAtFront (@NonNull final IHCNode aNode)
  {
    ValueEnforcer.notNull (aNode, "Node");
    m_aPrefixes.add (0, aNode);
    return this;
  }

  /**
   * Add a new text element after the contained control.
   *
   * @param sText
   *        The text to be added. May be <code>null</code>.
   * @return this for chaining
   * @see #getWrapped(String)
   */
  @NonNull
  public final BootstrapInputGroup addChildSuffix (@Nullable final String sText)
  {
    if (StringHelper.isNotEmpty (sText))
      m_aSuffixes.add (getWrapped (sText));
    return this;
  }

  /**
   * Add a new node after the contained control.
   *
   * @param aNode
   *        The node to be added. May be <code>null</code>.
   * @return this for chaining
   * @see #getWrapped(IHCNode)
   */
  @NonNull
  public final BootstrapInputGroup addChildSuffix (@Nullable final IHCNode aNode)
  {
    if (aNode != null)
      m_aSuffixes.add (getWrapped (aNode));
    return this;
  }

  public final boolean hasPrefixes ()
  {
    return m_aPrefixes.isNotEmpty ();
  }

  public final boolean hasSuffixes ()
  {
    return m_aSuffixes.isNotEmpty ();
  }

  @Override
  protected void onFinalizeNodeState (@NonNull final IHCConversionSettingsToNode aConversionSettings,
                                      @NonNull final IHCHasChildrenMutable <?, ? super IHCNode> aTargetNode)
  {
    super.onFinalizeNodeState (aConversionSettings, aTargetNode);
    addClasses (CBootstrapCSS.INPUT_GROUP, m_eSize);

    int nPrefixIndex = 0;
    for (final IHCNode aPrefix : m_aPrefixes)
      addChildAt (nPrefixIndex++, aPrefix);
    for (final IHCNode aSuffix : m_aSuffixes)
      addChild (aSuffix);
  }
}
