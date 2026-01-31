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

import org.jspecify.annotations.NonNull;

import com.helger.annotation.Nonempty;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.id.IHasID;
import com.helger.base.id.factory.GlobalIDFactory;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.render.HCRenderer;
import com.helger.html.jscode.IJSExpression;
import com.helger.html.jscode.JSAssocArray;
import com.helger.html.jscode.JSExpr;

@NotThreadSafe
public class BootstrapTreeViewItem implements IHasID <String>
{
  private final String m_sID;
  private final IJSExpression m_aText;
  private boolean m_bSelectable = true;
  private boolean m_bDisabled = false;
  private boolean m_bChecked = false;

  public BootstrapTreeViewItem (@NonNull final IHCNode aText)
  {
    // No nonce needed
    this (HCRenderer.getAsHTMLStringWithoutNamespaces (aText));
  }

  public BootstrapTreeViewItem (@NonNull final String sText)
  {
    this (JSExpr.lit (sText));
  }

  public BootstrapTreeViewItem (@NonNull final IJSExpression aText)
  {
    m_sID = "id" + GlobalIDFactory.getNewIntID ();
    m_aText = ValueEnforcer.notNull (aText, "Text");
  }

  @NonNull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @NonNull
  public IJSExpression getText ()
  {
    return m_aText;
  }

  @NonNull
  public BootstrapTreeViewItem setSelectable (final boolean bSelectable)
  {
    m_bSelectable = bSelectable;
    return this;
  }

  public boolean isSelectable ()
  {
    return m_bSelectable;
  }

  @NonNull
  public BootstrapTreeViewItem setDisabled (final boolean bDisabled)
  {
    m_bDisabled = bDisabled;
    return this;
  }

  public boolean isDisabled ()
  {
    return m_bDisabled;
  }

  @NonNull
  public BootstrapTreeViewItem setChecked (final boolean bChecked)
  {
    m_bChecked = bChecked;
    return this;
  }

  public boolean isChecked ()
  {
    return m_bChecked;
  }

  @NonNull
  @ReturnsMutableCopy
  public JSAssocArray getAsJSAssocArray ()
  {
    final JSAssocArray ret = new JSAssocArray ();
    ret.add ("name", m_aText);
    if (!m_bSelectable || m_bDisabled)
      ret.add ("selectable", false);
    if (m_bChecked)
      ret.add ("selected", true);
    return ret;
  }
}
