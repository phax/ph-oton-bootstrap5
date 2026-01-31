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
package com.helger.photon.bootstrap5.uictrls.treeview;

import java.util.function.Function;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.collection.CollectionHelper;
import com.helger.collection.commons.ICommonsList;
import com.helger.collection.hierarchy.visit.DefaultHierarchyVisitorCallback;
import com.helger.collection.hierarchy.visit.EHierarchyVisitorReturn;
import com.helger.collection.stack.NonBlockingStack;
import com.helger.html.hc.IHCConversionSettingsToNode;
import com.helger.html.hc.IHCHasChildrenMutable;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.html.grouping.AbstractHCDiv;
import com.helger.html.hc.html.script.HCScriptInline;
import com.helger.html.jscode.JSArray;
import com.helger.html.jscode.JSAssocArray;
import com.helger.html.jscode.JSDefinedClass;
import com.helger.html.jscode.JSExpr;
import com.helger.html.jscode.JSInvocation;
import com.helger.html.jscode.JSPackage;
import com.helger.html.jscode.JSRef;
import com.helger.html.jscode.JSVar;
import com.helger.html.jscode.html.JSHtml;
import com.helger.photon.app.html.PhotonCSS;
import com.helger.photon.app.html.PhotonJS;
import com.helger.photon.bootstrap5.uictrls.EBootstrapUICtrlsCSSPathProvider;
import com.helger.photon.bootstrap5.uictrls.EBootstrapUICtrlsJSPathProvider;
import com.helger.tree.BasicTree;
import com.helger.tree.DefaultTree;
import com.helger.tree.DefaultTreeItem;
import com.helger.tree.ITreeItem;
import com.helger.tree.util.TreeVisitor;

/**
 * Bootstrap Tree View based on Quercus.js
 *
 * @author Philip Helger
 */
public class BootstrapTreeView extends AbstractHCDiv <BootstrapTreeView>
{
  private final DefaultTree <BootstrapTreeViewItem> m_aTree;

  // Quercus.js options
  private boolean m_bSearchEnabled = false;
  private String m_sSearchPlaceholder;
  private boolean m_bInitiallyExpanded = false;
  private boolean m_bMultiSelectEnabled = false;
  private boolean m_bShowSelectAllButton = false;
  private boolean m_bShowInvertSelectionButton = false;
  private boolean m_bShowExpandCollapseAllButtons = false;
  private boolean m_bNodeSelectionEnabled = true;
  private boolean m_bCascadeSelectChildren = false;
  private boolean m_bCheckboxSelectionEnabled = false;

  public BootstrapTreeView (@NonNull final DefaultTree <BootstrapTreeViewItem> aTree)
  {
    m_aTree = ValueEnforcer.notNull (aTree, "Tree");
    ensureID ();
  }

  @NonNull
  public BootstrapTreeView setSearchEnabled (final boolean bSearchEnabled)
  {
    m_bSearchEnabled = bSearchEnabled;
    return this;
  }

  @NonNull
  public BootstrapTreeView setSearchPlaceholder (@Nullable final String sSearchPlaceholder)
  {
    m_sSearchPlaceholder = sSearchPlaceholder;
    return this;
  }

  @NonNull
  public BootstrapTreeView setInitiallyExpanded (final boolean bInitiallyExpanded)
  {
    m_bInitiallyExpanded = bInitiallyExpanded;
    return this;
  }

  @NonNull
  public BootstrapTreeView setMultiSelectEnabled (final boolean bMultiSelectEnabled)
  {
    m_bMultiSelectEnabled = bMultiSelectEnabled;
    return this;
  }

  @NonNull
  public BootstrapTreeView setShowSelectAllButton (final boolean bShowSelectAllButton)
  {
    m_bShowSelectAllButton = bShowSelectAllButton;
    return this;
  }

  @NonNull
  public BootstrapTreeView setShowInvertSelectionButton (final boolean bShowInvertSelectionButton)
  {
    m_bShowInvertSelectionButton = bShowInvertSelectionButton;
    return this;
  }

  @NonNull
  public BootstrapTreeView setShowExpandCollapseAllButtons (final boolean bShowExpandCollapseAllButtons)
  {
    m_bShowExpandCollapseAllButtons = bShowExpandCollapseAllButtons;
    return this;
  }

  @NonNull
  public BootstrapTreeView setNodeSelectionEnabled (final boolean bNodeSelectionEnabled)
  {
    m_bNodeSelectionEnabled = bNodeSelectionEnabled;
    return this;
  }

  @NonNull
  public BootstrapTreeView setCascadeSelectChildren (final boolean bCascadeSelectChildren)
  {
    m_bCascadeSelectChildren = bCascadeSelectChildren;
    return this;
  }

  @NonNull
  public BootstrapTreeView setCheckboxSelectionEnabled (final boolean bCheckboxSelectionEnabled)
  {
    m_bCheckboxSelectionEnabled = bCheckboxSelectionEnabled;
    return this;
  }

  @NonNull
  public JSRef getJSInstance ()
  {
    // document.getElementById('id').quercus
    return JSHtml.documentGetElementById (getID ()).ref ("quercus");
  }

  @NonNull
  public JSInvocation getJSExpandAllInvocation ()
  {
    return getJSInstance ().invoke ("expandAll");
  }

  @NonNull
  public JSInvocation getJSCollapseAllInvocation ()
  {
    return getJSInstance ().invoke ("collapseAll");
  }

  private static void _recursiveFillJSTree (@Nullable final ICommonsList <DefaultTreeItem <BootstrapTreeViewItem>> aTreeItems,
                                            @NonNull final JSArray aTargetArray)
  {
    if (CollectionHelper.isNotEmpty (aTreeItems))
      for (final DefaultTreeItem <BootstrapTreeViewItem> aTreeItem : aTreeItems)
      {
        // Main tree view item
        final JSAssocArray aJSNode = aTreeItem.getData ().getAsJSAssocArray ();

        // Child nodes
        final JSArray aChildNodes = new JSArray ();
        _recursiveFillJSTree (aTreeItem.getAllChildren (), aChildNodes);
        if (aChildNodes.isNotEmpty ())
          aJSNode.add ("children", aChildNodes);

        // Append to result list
        aTargetArray.add (aJSNode);
      }
  }

  @NonNull
  @ReturnsMutableCopy
  public JSArray getJSDataArray ()
  {
    final JSArray aTreeArray = new JSArray ();
    _recursiveFillJSTree (m_aTree.getRootItem ().getAllChildren (), aTreeArray);
    return aTreeArray;
  }

  @NonNull
  @ReturnsMutableCopy
  public JSAssocArray getJSOptions ()
  {
    // JS Code
    final JSAssocArray aJSOptions = new JSAssocArray ();
    aJSOptions.add ("containerId", getID ());
    aJSOptions.add ("data", getJSDataArray ());

    if (m_bSearchEnabled)
      aJSOptions.add ("searchEnabled", true);
    if (m_sSearchPlaceholder != null)
      aJSOptions.add ("searchPlaceholder", m_sSearchPlaceholder);
    if (m_bInitiallyExpanded)
      aJSOptions.add ("initiallyExpanded", true);
    if (m_bMultiSelectEnabled)
      aJSOptions.add ("multiSelectEnabled", true);
    if (m_bShowSelectAllButton)
      aJSOptions.add ("showSelectAllButton", true);
    if (m_bShowInvertSelectionButton)
      aJSOptions.add ("showInvertSelectionButton", true);
    if (m_bShowExpandCollapseAllButtons)
      aJSOptions.add ("showExpandCollapseAllButtons", true);
    if (!m_bNodeSelectionEnabled)
      aJSOptions.add ("nodeSelectionEnabled", false);
    if (m_bCascadeSelectChildren)
      aJSOptions.add ("cascadeSelectChildren", true);
    if (m_bCheckboxSelectionEnabled)
      aJSOptions.add ("checkboxSelectionEnabled", true);

    return aJSOptions;
  }

  @Override
  protected void onFinalizeNodeState (@NonNull final IHCConversionSettingsToNode aConversionSettings,
                                      @NonNull final IHCHasChildrenMutable <?, ? super IHCNode> aTargetNode)
  {
    super.onFinalizeNodeState (aConversionSettings, aTargetNode);

    // JS Code
    final JSAssocArray aJSOptions = getJSOptions ();
    final JSPackage aPkg = new JSPackage ();
    final JSVar aVar = aPkg.variable ("tv" + getID (), new JSDefinedClass ("Treeview")._new ().arg (aJSOptions));
    aPkg.assign (JSExpr.ref (JSExpr.invoke (JSExpr.ref ("document"), "getElementById").arg (getID ()), "quercus"),
                 aVar);
    aTargetNode.addChild (new HCScriptInline (aPkg));
  }

  @Override
  protected void onRegisterExternalResources (@NonNull final IHCConversionSettingsToNode aConversionSettings,
                                              final boolean bForceRegistration)
  {
    PhotonJS.registerJSIncludeForThisRequest (EBootstrapUICtrlsJSPathProvider.TREE_VIEW);
    PhotonCSS.registerCSSIncludeForThisRequest (EBootstrapUICtrlsCSSPathProvider.TREE_VIEW);
  }

  @NonNull
  public static <DATATYPE, ITEMTYPE extends ITreeItem <DATATYPE, ITEMTYPE>> BootstrapTreeView create (@NonNull final BasicTree <DATATYPE, ITEMTYPE> aTree,
                                                                                                      @NonNull final Function <DATATYPE, BootstrapTreeViewItem> aConverter)
  {
    final DefaultTree <BootstrapTreeViewItem> aNewTree = new DefaultTree <> ();
    final NonBlockingStack <DefaultTreeItem <BootstrapTreeViewItem>> aParents = new NonBlockingStack <> ();
    aParents.push (aNewTree.getRootItem ());
    TreeVisitor.visitTree (aTree, new DefaultHierarchyVisitorCallback <ITEMTYPE> ()
    {
      @Override
      public EHierarchyVisitorReturn onItemBeforeChildren (@NonNull final ITEMTYPE aItem)
      {
        final DefaultTreeItem <BootstrapTreeViewItem> aChildItem = aParents.peek ()
                                                                           .createChildItem (aConverter.apply (aItem.getData ()));
        aParents.push (aChildItem);
        return EHierarchyVisitorReturn.CONTINUE;
      }

      @Override
      public EHierarchyVisitorReturn onItemAfterChildren (@NonNull final ITEMTYPE aItem)
      {
        aParents.pop ();
        return EHierarchyVisitorReturn.CONTINUE;
      }
    });
    return new BootstrapTreeView (aNewTree);
  }
}
